package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import trictionary.jumproper.com.jumpropetrictionary.utils.DownloadImageTask;
import trictionary.jumproper.com.jumpropetrictionary.customviews.MyGridView;
import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickData;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickListAdapter;

import static trictionary.jumproper.com.jumpropetrictionary.activities.Tricktionary.DASHES;

public class Profile extends BaseActivity {
    private ArrayList<ArrayList<Trick>>tricktionary= TrickData.getTricktionary();
    private ArrayList<ArrayList<Trick>>completedTricks=TrickData.getCompletedTricks();
    private FirebaseAuth mAuth;
    private int delay = 100; //milliseconds
    private Handler h;
    private TextView profileName;
    private ImageView profileImage;
    private TextView numTricks,numLevel1Tricks,numLevel2Tricks,numLevel3Tricks,numLevel4Tricks;
    private int numTricksCount,numLevel1Count,numLevel2Count,numLevel3Count,numLevel4Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();


        profileName = (TextView)findViewById(R.id.profile_name);
        profileName.setText(mAuth.getCurrentUser().getDisplayName());
        profileImage = (ImageView)findViewById(R.id.profile_image);

        numTricks=(TextView)findViewById(R.id.num_tricks_profile);
        numLevel1Tricks=(TextView)findViewById(R.id.num_level_1_tricks_profile);
        numLevel2Tricks=(TextView)findViewById(R.id.num_level_2_tricks_profile);
        numLevel3Tricks=(TextView)findViewById(R.id.num_level_3_tricks_profile);
        numLevel4Tricks=(TextView)findViewById(R.id.num_level_4_tricks_profile);

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference myRef=fb.getReference("checklist").child(mAuth.getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTricksCount=0;
                numLevel1Count=0;
                numLevel2Count=0;
                numLevel3Count=0;
                numLevel4Count=0;
                    for(DataSnapshot id0:dataSnapshot.getChildren()) {
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
                            if (id1.getValue().toString().equals("true"))
                                numTricksCount++;
                        }
                    }
                numTricks.setText(""+numTricksCount);
                numLevel1Tricks.setText(""+numLevel1Count);
                numLevel2Tricks.setText(""+numLevel2Count);
                numLevel3Tricks.setText(""+numLevel3Count);
                numLevel4Tricks.setText(""+numLevel4Count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DownloadImageTask downloadImage=new DownloadImageTask(mAuth,profileImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            downloadImage.execute(mAuth.getCurrentUser().getPhotoUrl().toString());
        }
        h = new Handler();
        h.postDelayed(r, 100);
    }
    @Override
    public void onResume() {
        delay=100;
        h.postDelayed(r,delay);
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        delay=60000;
        h.removeCallbacks(r);
    }
    public void populateLists(){


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

        if(tricktionary==null){
            h.postDelayed(r, delay);
            return;
        }
        if(completedTricks==null){
            h.postDelayed(r, delay);
            return;
        }
        for(int j=0;j<completedTricks.size();j++){
            for(Trick mTrick:completedTricks.get(j)) {
                if (!mTrick.isCompleted()) {
                    j++;
                } else if (mTrick.getType().equals("Basics")) {
                    basicsList.add(mTrick);
                } else if (mTrick.getDifficulty() == 1) {
                    level1List.add(mTrick);
                } else if (mTrick.getDifficulty() == 2) {
                    level2List.add(mTrick);
                } else if (mTrick.getDifficulty() == 3) {
                    level3List.add(mTrick);
                } else if (mTrick.getDifficulty() == 4) {
                    level4List.add(mTrick);
                }
            }
        }
        Collections.sort(basicsList,TrickData.compareName);
        final ArrayList<Trick>level1Sorted=addWhiteSpace(sortTrickList(level1List));
        final ArrayList<Trick>level2Sorted=addWhiteSpace(sortTrickList(level2List));
        final ArrayList<Trick>level3Sorted=addWhiteSpace(sortTrickList(level3List));
        final ArrayList<Trick>level4Sorted=addWhiteSpace(sortTrickList(level4List));

        final String[]ignoredStrings={"Multiples","Power","Manipulation","Releases", DASHES};

        final ArrayList[] trickLists = new ArrayList[]{basicsList,
                level1Sorted,
                level2Sorted,
                level3Sorted,
                level4Sorted};

        final MyGridView[] trickListGridViews= new MyGridView[]{basicsGridView,
                level1GridView,
                level2GridView,
                level3GridView,
                level4GridView};

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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            MainActivity.currentTrick.getName(), Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        h.removeCallbacks(r);
    }
    public static ArrayList<Trick> sortTrickList(ArrayList<Trick> list){
        ArrayList<Trick>sortedTricks= new ArrayList<>();
        int multiples=0;
        int power=1;
        int manipulation=2;
        int releases=3;
        Collections.sort(list, TrickData.compareName);
        Collections.reverse(list);

        sortedTricks.add(0,new Trick("Releases"));
        sortedTricks.add(0,new Trick("Manipulation"));
        sortedTricks.add(0,new Trick("Power"));
        sortedTricks.add(0,new Trick("Multiples"));
        for(int j=0;j<list.size();j++){

            if(list.get(j).getType().equals("Multiples")){
                sortedTricks.add(multiples + 1, list.get(j));
                power++;
                manipulation++;
                releases++;
            }
            else if(list.get(j).getType().equals("Power")){
                sortedTricks.add(power+1,list.get(j));
                manipulation++;
                releases++;
            }
            else if(list.get(j).getType().equals("Manipulation")){
                sortedTricks.add(manipulation+1,list.get(j));
                releases++;
            }
            else if(list.get(j).getType().equals("Releases")){
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
            if(list.get(j).getName().equals("Multiples")){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(""));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));


            }
            if(list.get(j).getName().equals("Power")){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(""));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }
            if(list.get(j).getName().equals("Manipulation")){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(""));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }
            if(list.get(j).getName().equals("Releases")){
                index=sortedTricks.size()-1;
                while(index%3>0){
                    sortedTricks.add(index,new Trick(""));
                    index++;
                }
                sortedTricks.add(index,new Trick(DASHES));
                sortedTricks.add(index+2,new Trick(DASHES));

            }

        }
        return sortedTricks;
    }
    public Runnable r=new Runnable() {
        @Override
        public void run() {
            if(mAuth.getCurrentUser()!=null){
                TrickData.uId=mAuth.getCurrentUser().getUid();
            }
            if(completedTricks==null){
                tricktionary=TrickData.getTricktionary();
                completedTricks=TrickData.getCompletedTricks();
                h.postDelayed(this,delay);
            }
            else if (completedTricks.size()==0){
                completedTricks=TrickData.getCompletedTricks();
                h.postDelayed(this, delay);
            }
            else{
                populateLists();
                delay=60000; //change update time to 1 minute
                h.removeCallbacks(r);
            }

        }
    };
    public void back(View v){
        finish();
    }

}