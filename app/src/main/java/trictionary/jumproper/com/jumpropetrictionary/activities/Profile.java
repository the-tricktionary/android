package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.customviews.MyGridView;
import trictionary.jumproper.com.jumpropetrictionary.utils.DownloadImageTask;
import trictionary.jumproper.com.jumpropetrictionary.utils.Friend;
import trictionary.jumproper.com.jumpropetrictionary.utils.FriendRequestListAdapter;
import trictionary.jumproper.com.jumpropetrictionary.utils.FriendsListAdapter;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickListAdapter;

import static trictionary.jumproper.com.jumpropetrictionary.activities.Tricktionary.DASHES;

public class Profile extends BaseActivity {
    private TextView profileName;
    private ImageView profileImage,shareProfile;
    private Button viewOtherProfile;
    private int levels;
    private TextView numTricks,numLevel1Tricks,numLevel2Tricks,numLevel3Tricks,numLevel4Tricks,numLevel5Tricks,profileUsername;
    private int numTricksCount,numLevel1Count,numLevel2Count,numLevel3Count,numLevel4Count,numLevel5Count;
    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private ArrayList<ArrayList<Trick>> friendsCompletedTricks;
    private FirebaseAuth mAuth;
    private String[]trickTypes;
    private String uId="";
    private String imageURL="";
    private boolean usernameTaken;
    ArrayList<Friend> friends = new ArrayList<>();
    ArrayList<String> friendUsernames = new ArrayList<>();
    ArrayList<String> friendRequests = new ArrayList<>();
    AlertDialog friendsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        trickTypes = getResources().getStringArray(R.array.trick_types);
        tricktionary = (ArrayList<ArrayList<Trick>>)((GlobalData) this.getApplication()).getTricktionary().clone();
        completedTricks = new ArrayList<ArrayList<Trick>>(((GlobalData) this.getApplication()).getCompletedTricks());

        mAuth = ((GlobalData) this.getApplication()).getmAuth();

        profileName = (TextView)findViewById(R.id.profile_name);
        profileUsername = (TextView)findViewById(R.id.profile_username);
        profileUsername.setText(((GlobalData) this.getApplication()).getUsername());
        profileName.setText(mAuth.getCurrentUser().getDisplayName());
        if(mAuth.getCurrentUser()!=null) {
            uId = mAuth.getCurrentUser().getUid();
            if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
                imageURL = mAuth.getCurrentUser().getPhotoUrl().toString();
            }
        }
        viewOtherProfile = (Button)findViewById(R.id.view_another_profile);
        viewOtherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), Friends.class);
                //startActivity(intent);
                FirebaseDatabase fb = FirebaseDatabase.getInstance();
                DatabaseReference myRef = fb.getReference("users").child(uId).child("profile");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("username")){ //user has set a username

                            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                            LayoutInflater inflater = (LayoutInflater)Profile.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
                            final View textBoxes=inflater.inflate(R.layout.friends_dialog,null); //custom layout file now a view object
                            final EditText friendUsername = (EditText)textBoxes.findViewById(R.id.friend_username_field);
                            TextView friendRequestsHeader = (TextView)textBoxes.findViewById(R.id.friend_requests_header);
                            ListView friendsList = (ListView) textBoxes.findViewById(R.id.friends_list);
                            ListView friendRequestsList = (ListView) textBoxes.findViewById(R.id.friend_requests_list);
                            populateFriendsList(friendsList);
                            populateFriendRequestsList(friendRequestsList,friendRequestsHeader);
                            Button addFriendButton = (Button)textBoxes.findViewById(R.id.add_friend_button);
                            addFriendButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addFriend(friendUsername.getText().toString(),friendUsername);
                                }
                            });
                            builder.setView(textBoxes);
                            builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            friendsDialog = builder.show();
                        }
                        else{ //user has no username
                            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                            LayoutInflater inflater = (LayoutInflater)Profile.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
                            final View textBoxes=inflater.inflate(R.layout.create_username_dialog,null); //custom layout file now a view object
                            final EditText usernameField = (EditText)textBoxes.findViewById(R.id.set_username_field);
                            builder.setTitle(R.string.create_username);
                            builder.setView(textBoxes);
                            builder.setPositiveButton(getString(R.string.create_username), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkUsername(usernameField.getText().toString());
                                    if(!usernameTaken){
                                        FirebaseDatabase fb = FirebaseDatabase.getInstance();
                                        fb.getReference("users").child(uId).child("profile").child("username").setValue(usernameField.getText().toString().toLowerCase());
                                        dialog.cancel();
                                        viewOtherProfile.performClick();
                                    }

                                }
                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        profileImage = (ImageView)findViewById(R.id.profile_image);
        shareProfile = (ImageView)findViewById(R.id.share_profile);

        numTricks=(TextView)findViewById(R.id.num_tricks_profile);
        numLevel1Tricks=(TextView)findViewById(R.id.num_level_1_tricks_profile);
        numLevel2Tricks=(TextView)findViewById(R.id.num_level_2_tricks_profile);
        numLevel3Tricks=(TextView)findViewById(R.id.num_level_3_tricks_profile);
        numLevel4Tricks=(TextView)findViewById(R.id.num_level_4_tricks_profile);
        numLevel5Tricks=(TextView)findViewById(R.id.num_level_5_tricks_profile);
        DownloadImageTask downloadImage = new DownloadImageTask(mAuth, profileImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (mAuth.getCurrentUser().getPhotoUrl() != null) {
                downloadImage.execute(imageURL);
            }
        }
        loadProfile(uId,completedTricks);
    }
    public void addFriend(String username, EditText usernameField){
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fb.getReference("users").child(uId).child("friends").child(username).child("mutual");
        myRef.setValue(false);
        usernameField.setText("");
        Toast.makeText(Profile.this, "Request sent to " + username,
                Toast.LENGTH_SHORT).show();
    }
    public void populateFriendRequestsList(final ListView listView, final TextView header) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fb.getReference("users").child(uId).child("friendrequests");
        final DatabaseReference friendsRef = fb.getReference("users").child(uId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendRequests.clear();
                Log.e("Friends list", dataSnapshot.getChildren().toString());
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    Log.e("Friends list", friend.getKey());
                    Log.e("Friends list", friend.getValue().toString());
                    friendRequests.add(friend.child("username").getValue().toString());
                }
                if(friendRequests.size() == 0){
                    header.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                }
                FriendRequestListAdapter adapter = new FriendRequestListAdapter(Profile.this,
                        R.layout.friend_request_list_item, friendRequests, friendsRef);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;
                        Toast.makeText(getApplicationContext(),
                                friendRequests.get(position), Toast.LENGTH_LONG)
                                .show();

                    }

                });
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                return;
            }
        });
    }
    public void checkUsername(final String username){
        if(username.length()>20){
            Toast.makeText(getApplicationContext(), "Sorry usernames must be less than 20 characters.",Toast.LENGTH_SHORT);
            usernameTaken = true;
        }
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference usernamesRef = fb.getReference("usernames");
        usernamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username)){
                    Toast.makeText(getApplicationContext(), "Sorry that username is taken.",Toast.LENGTH_SHORT);
                    usernameTaken = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.username_error,Toast.LENGTH_LONG);
                usernameTaken = true;
            }
        });
    }
    public void populateFriendsList(final ListView listView) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fb.getReference("users").child(uId).child("friends");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                Log.e("Friends list", dataSnapshot.getChildren().toString());
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    Log.e("Friends list", friend.getKey());
                    Log.e("Friends list", friend.getValue().toString());
                    if (friend.child("mutual").getValue().toString().equals("true")) {
                        String friendUsername = friend.child("username").getValue().toString();
                        Friend f = new Friend(friendUsername);
                        friends.add(f);
                    }
                }
                FriendsListAdapter adapter = new FriendsListAdapter(Profile.this,
                        R.layout.friend_list_item, friends);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Friend f = friends.get(position);
                        Toast.makeText(getApplicationContext(),
                                f.getUsername(), Toast.LENGTH_LONG)
                                .show();

                        friendsCompletedTricks = getCompletedTricks(f.getuId());
                        if(f.getName()!=null) {
                            if(f.getName()[0] == null || f.getName()[1] == null){
                                profileName.setText(f.getUsername());
                                profileUsername.setText("");
                            }
                            else {
                                profileName.setText(f.getName()[0] + " " + f.getName()[1]);
                                profileUsername.setText(f.getUsername());
                            }
                        }
                        else{
                            profileName.setText(f.getUsername());
                            profileUsername.setText("");
                        }
                        DownloadImageTask downloadImage = new DownloadImageTask(mAuth, profileImage);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE && f.getImageUrl() != null) {
                            downloadImage.execute(f.getImageUrl());
                        }
                        else{
                            profileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                        }
                        shareProfile.setVisibility(View.INVISIBLE);
                        friendsDialog.cancel();
                    }

                });
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                return;
            }
        });
    }
    public void loadProfile(String mUid, ArrayList<ArrayList<Trick>> completedTricks){
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fb.getReference("checklist").child(mUid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTricksCount = 0;
                numLevel1Count = 0;
                numLevel2Count = 0;
                numLevel3Count = 0;
                numLevel4Count = 0;
                numLevel5Count = 0;
                for (DataSnapshot id0 : dataSnapshot.getChildren()) {
                    for (DataSnapshot id1 : id0.getChildren()) {
                        if (Integer.parseInt(id0.getKey().toString()) == 0 && id1.getValue().toString().equals("true")) {
                            numLevel1Count++;
                        }
                        if (Integer.parseInt(id0.getKey().toString()) == 1 && id1.getValue().toString().equals("true")) {
                            numLevel2Count++;
                        }
                        if (Integer.parseInt(id0.getKey().toString()) == 2 && id1.getValue().toString().equals("true")) {
                            numLevel3Count++;
                        }
                        if (Integer.parseInt(id0.getKey().toString()) == 3 && id1.getValue().toString().equals("true")) {
                            numLevel4Count++;
                        }
                        if (Integer.parseInt(id0.getKey().toString()) == 4 && id1.getValue().toString().equals("true")) {
                            numLevel5Count++;
                        }
                        if (id1.getValue().toString().equals("true"))
                            numTricksCount++;
                    }
                }
                numTricks.setText("" + numTricksCount);
                numLevel1Tricks.setText("" + numLevel1Count);
                numLevel2Tricks.setText("" + numLevel2Count);
                numLevel3Tricks.setText("" + numLevel3Count);
                numLevel4Tricks.setText("" + numLevel4Count);
                numLevel5Tricks.setText("" + numLevel5Count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        populateLists(completedTricks);
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    public ArrayList<ArrayList<Trick>> getCompletedTricks(final String mUid){
        Log.e("checklist", mUid);
        final ArrayList<ArrayList<Trick>> completedTricks = new ArrayList<>();
        levels = ((GlobalData) Profile.this.getApplication()).getLevels();
        if(mUid.length()>0 && tricktionary.size()>0) {
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference checklist=fb.getReference("checklist").child(mUid);
            for(int j=0;j<levels;j++){
                completedTricks.add(new ArrayList<Trick>());
            }
            checklist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot id0:dataSnapshot.getChildren()){
                        for(DataSnapshot id1:id0.getChildren()){
                            if(Integer.parseInt(id0.getKey())<0){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id0.getKey())>=  tricktionary.size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id1.getKey())>=  tricktionary.get(Integer.parseInt(id0.getKey())).size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else {
                                Log.e("Checklist",tricktionary.get(Integer.parseInt(id0.getKey()))
                                        .get(Integer.parseInt(id1.getKey())).getName());
                                /**
                                tricktionary.get(Integer.parseInt(id0.getKey()))
                                        .get(Integer.parseInt(id1.getKey()))
                                        .setCompleted(true);
                                 **/
                                completedTricks.get(Integer.parseInt(id0.getKey()))
                                        .add(tricktionary.get(Integer.parseInt(id0.getKey()))
                                                .get(Integer.parseInt(id1.getKey())));
                            }
                        }
                    }
                    loadProfile(mUid,completedTricks);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("checklist", databaseError.getMessage().toString() + " : " + databaseError.getDetails());
                }
            });
        }
        return completedTricks;
    }
    public void populateLists(ArrayList<ArrayList<Trick>> completedTricks){


        final MyGridView basicsGridView = (MyGridView) findViewById(R.id.basics_grid_view_profile);
        final ArrayList<Trick> basicsList = new ArrayList<>();

        final MyGridView level1GridView = (MyGridView) findViewById(R.id.level_1_grid_view_profile);
        final ArrayList<Trick> level1List = new ArrayList<>();

        final MyGridView level2GridView = (MyGridView) findViewById(R.id.level_2_grid_view_profile);
        final ArrayList<Trick> level2List = new ArrayList<>();

        final MyGridView level3GridView = (MyGridView) findViewById(R.id.level_3_grid_view_profile);
        final ArrayList<Trick> level3List = new ArrayList<>();

        final MyGridView level4GridView = (MyGridView) findViewById(R.id.level_4_grid_view_profile);
        final ArrayList<Trick> level4List = new ArrayList<>();

        final MyGridView level5GridView = (MyGridView) findViewById(R.id.level_5_grid_view_profile);
        final ArrayList<Trick> level5List = new ArrayList<>();

        for(int j=0;j<completedTricks.size();j++){
            for(Trick mTrick:completedTricks.get(j)) {
                //if (!mTrick.isCompleted()) {
                //    j++;
                if (mTrick.getType().equals(trickTypes[0])) {
                    basicsList.add(mTrick);
                } else if (mTrick.getDifficulty() == 1) {
                    level1List.add(mTrick);
                } else if (mTrick.getDifficulty() == 2) {
                    level2List.add(mTrick);
                } else if (mTrick.getDifficulty() == 3) {
                    level3List.add(mTrick);
                } else if (mTrick.getDifficulty() == 4) {
                    level4List.add(mTrick);
                } else if (mTrick.getDifficulty() == 5) {
                    level5List.add(mTrick);
                }
                else{
                    j++;
                }
            }
        }
        Collections.sort(basicsList,((GlobalData) this.getApplication()).getCompareName());
        final ArrayList<Trick>level1Sorted=addWhiteSpace(sortTrickList(level1List));
        final ArrayList<Trick>level2Sorted=addWhiteSpace(sortTrickList(level2List));
        final ArrayList<Trick>level3Sorted=addWhiteSpace(sortTrickList(level3List));
        final ArrayList<Trick>level4Sorted=addWhiteSpace(sortTrickList(level4List));

        final String[]ignoredStrings={trickTypes[1],trickTypes[2],trickTypes[3],trickTypes[4], DASHES};

        final ArrayList[] trickLists = new ArrayList[]{basicsList,
                level1Sorted,
                level2Sorted,
                level3Sorted,
                level4Sorted,
                level5List};

        final MyGridView[] trickListGridViews= new MyGridView[]{basicsGridView,
                level1GridView,
                level2GridView,
                level3GridView,
                level4GridView,
                level5GridView};

        for(int j=0;j<trickListGridViews.length;j++){
            trickListGridViews[j].setAdapter(new TrickListAdapter(this, R.layout.trick_list_layout, trickLists[j],false));
            trickListGridViews[j].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    // ListView Clicked item value
                    MainActivity.currentTrick=(Trick)parent.getItemAtPosition(position);
                    if(MainActivity.currentTrick==null){
                        return;
                    }
                    else if((((Trick) parent.getItemAtPosition(position)).getName()).equals(" ")){
                        return;
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        // Show Alert
                        Toast.makeText(getApplicationContext(),
                                MainActivity.currentTrick.getName(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        ((ScrollView)findViewById(R.id.content_profile)).smoothScrollTo(0,0);
    }
    public ArrayList<Trick> sortTrickList(ArrayList<Trick> list){
        ArrayList<Trick>sortedTricks= new ArrayList<>();
        int multiples=0;
        int power=1;
        int manipulation=2;
        int releases=3;
        Collections.sort(list, ((GlobalData) this.getApplication()).getCompareName());
        Collections.reverse(list);

        sortedTricks.add(0,new Trick(trickTypes[4]));
        sortedTricks.add(0,new Trick(trickTypes[3]));
        sortedTricks.add(0,new Trick(trickTypes[2]));
        sortedTricks.add(0,new Trick(trickTypes[1]));
        for(int j=0;j<list.size();j++){

            if(list.get(j).getType().equals(trickTypes[1])){
                sortedTricks.add(multiples + 1, list.get(j));
                power++;
                manipulation++;
                releases++;
            }
            else if(list.get(j).getType().equals(trickTypes[2])){
                sortedTricks.add(power+1,list.get(j));
                manipulation++;
                releases++;
            }
            else if(list.get(j).getType().equals(trickTypes[3])){
                sortedTricks.add(manipulation+1,list.get(j));
                releases++;
            }
            else if(list.get(j).getType().equals(trickTypes[4])){
                sortedTricks.add(releases+1,list.get(j));
            }

        }



        return sortedTricks;
    }
    public ArrayList<Trick> addWhiteSpace(ArrayList<Trick> list){
        int index;
        ArrayList<Trick> sortedTricks= new ArrayList<>();
        for(int j=0;j<list.size();j++){
            sortedTricks.add(list.get(j));
            if(list.get(j).getName().equals(trickTypes[1])){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(" "));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));


            }
            if(list.get(j).getName().equals(trickTypes[2])){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(" "));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }
            if(list.get(j).getName().equals(trickTypes[3])){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(" "));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }
            if(list.get(j).getName().equals(trickTypes[4])){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(" "));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }

        }
        return sortedTricks;
    }
    public void back(View v){
        finish();
    }

    public void shareProfile(View v){
        if(((GlobalData)this.getApplication()).getSettings().getBoolean(SettingsActivity.PUBLIC_PROFILE_SETTING,false)) {
            //create the send intent
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);

            //set the type
            shareIntent.setType("text/plain");

            //add a subject
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    getString(R.string.profile_share));

            //build the body of the message to be shared
            String shareMessage = "https://the-tricktionary.com/profile/" + uId + "\n\nUser ID: "+ uId;

            //add the message
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    shareMessage);

            //start the chooser for sharing
            startActivity(Intent.createChooser(shareIntent,
                    getString(R.string.profile_share_with)));
        }
        else{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(R.string.profile_public);
            mBuilder.setMessage(R.string.profile_public_prompt);
            mBuilder.setPositiveButton(R.string.profile_go_to_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(Profile.this,SettingsActivity.class);
                    startActivity(intent);
                }
            });
            mBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            mBuilder.show();
        }
    }

    public void getProfileTricks(){
        completedTricks = new ArrayList<>();
        Log.e("checklist", uId);
        if(uId.length()>0 && tricktionary.size()>0) {
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference checklist=fb.getReference("checklist").child(uId);
            checklist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long levels = dataSnapshot.getChildrenCount();
                    for(int j=0;j<levels;j++){
                        completedTricks.add(new ArrayList<Trick>());
                    }
                    for(DataSnapshot id0:dataSnapshot.getChildren()){
                        for(DataSnapshot id1:id0.getChildren()){
                            if(Integer.parseInt(id0.getKey())<0){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id0.getKey())>=  tricktionary.size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id1.getKey())>=  tricktionary.get(Integer.parseInt(id0.getKey())).size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else {
                                tricktionary.get(Integer.parseInt(id0.getKey()))
                                        .get(Integer.parseInt(id1.getKey()))
                                        .setCompleted(true);
                                completedTricks.get(Integer.parseInt(id0.getKey()))
                                        .add(tricktionary.get(Integer.parseInt(id0.getKey()))
                                                .get(Integer.parseInt(id1.getKey())));
                            }
                        }
                    }
                    loadProfile(uId,completedTricks);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("checklist", databaseError.getMessage().toString() + " : " + databaseError.getDetails());
                }
            });
        }
        else{

        }
    }

}
