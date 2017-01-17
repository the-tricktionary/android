package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Profile extends AppCompatActivity {
    private Trick[]tricktionary=Tricktionary.tricktionary;
    private ArrayList<Trick>completedTricks=Tricktionary.completedTricks;
    private FirebaseAuth mAuth;
    private int delay = 100; //milliseconds
    private Handler h;
    private int completedIndex=0;
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
        DatabaseReference myRef=fb.getReference("checklist");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTricksCount=0;
                numLevel1Count=0;
                numLevel2Count=0;
                numLevel3Count=0;
                numLevel4Count=0;
                for(DataSnapshot ids:dataSnapshot.getChildren()){
                    for(DataSnapshot id0:ids.getChildren()){
                        if(Integer.parseInt(id0.getKey().toString())==0 && id0.getValue()==true){
                            numLevel1Count++;
                        }
                        if(Integer.parseInt(id0.getKey().toString())==1 && id0.getValue()==true){
                            numLevel2Count++;
                        }
                        if(Integer.parseInt(id0.getKey().toString())==2 && id0.getValue()==true){
                            numLevel3Count++;
                        }
                        if(Integer.parseInt(id0.getKey().toString())==3 && id0.getValue()==true){
                            numLevel4Count++;
                        }
                        if(id0.getValue()==true)
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
        h.postDelayed(r, delay);

        DrawerCreate drawer=new DrawerCreate();
        drawer.makeDrawer(this, this, mAuth, toolbar, " ");
    }
    public void populateLists(){


        final MyGridView basicsGridView = (MyGridView) findViewById(R.id.basics_grid_view_profile);
        final ArrayList<String> basicsList = new ArrayList<String>();

        final MyGridView level1GridView = (MyGridView) findViewById(R.id.level_1_grid_view_profile);
        final ArrayList<String> level1List = new ArrayList<String>();

        final MyGridView level2GridView = (MyGridView) findViewById(R.id.level_2_grid_view_profile);
        final ArrayList<String> level2List = new ArrayList<String>();

        final MyGridView level3GridView = (MyGridView) findViewById(R.id.level_3_grid_view_profile);
        final ArrayList<String> level3List = new ArrayList<String>();

        final MyGridView level4GridView = (MyGridView) findViewById(R.id.level_4_grid_view_profile);
        final ArrayList<String> level4List = new ArrayList<String>();

        if(tricktionary==null){
            h.postDelayed(r, delay);
            return;
        }
        if(completedTricks==null){
            h.postDelayed(r, delay);
            return;
        }
        for(int j=0;j<completedTricks.size();j++){
            if(!completedTricks.get(j).isCompleted()){
                j++;
            }
            else if(completedTricks.get(j).getType().equals("Basics")){
                basicsList.add(completedTricks.get(j).getName());
            }
            else if(completedTricks.get(j).getDifficulty()==1){
                level1List.add(completedTricks.get(j).getName());
            }
            else if(completedTricks.get(j).getDifficulty()==2){
                level2List.add(completedTricks.get(j).getName());
            }
            else if(completedTricks.get(j).getDifficulty()==3){
                level3List.add(completedTricks.get(j).getName());
            }
            else if(completedTricks.get(j).getDifficulty()==4){
                level4List.add(completedTricks.get(j).getName());
            }
        }
        Collections.sort(basicsList);
        final ArrayList<String>level1Sorted=addWhiteSpace(sortTrickList(level1List, 1));
        final ArrayList<String>level2Sorted=addWhiteSpace(sortTrickList(level2List, 2));
        final ArrayList<String>level3Sorted=addWhiteSpace(sortTrickList(level3List, 3));
        final ArrayList<String>level4Sorted=addWhiteSpace(sortTrickList(level4List, 4));

        final String[]ignoredStrings={"Multiples","Power","Manipulation","Releases",Tricktionary.DASHES};

        ArrayAdapter<String> basicsAdapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, basicsList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setClickable(false);
                return view;
            }
        };
        ArrayAdapter<String> level1Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level1Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                int color;
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level1Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Material Red
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };

        ArrayAdapter<String> level2Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level2Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level2Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        ArrayAdapter<String> level3Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level3Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level3Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        ArrayAdapter<String> level4Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level4Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = getResources().getColor(R.color.materialRed); // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level4Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        // Assign basicsAdapter to ListView
        basicsGridView.setAdapter(basicsAdapter);
        level1GridView.setAdapter(level1Adapter);
        level2GridView.setAdapter(level2Adapter);
        level3GridView.setAdapter(level3Adapter);
        level4GridView.setAdapter(level4Adapter);

        // ListView Item Click Listener
        basicsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) basicsGridView.getItemAtPosition(position);
                TrickList.index = Tricktionary.getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

        level1GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level1GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level1GridView.getItemAtPosition(position);
                TrickList.index = Tricktionary.getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level2GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level2GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level2GridView.getItemAtPosition(position);
                TrickList.index = Tricktionary.getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level3GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level3GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level3GridView.getItemAtPosition(position);
                TrickList.index = Tricktionary.getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level4GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for (String ignoredString : ignoredStrings) {
                    if (level4GridView.getItemAtPosition(position).equals(ignoredString))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level4GridView.getItemAtPosition(position);
                TrickList.index = Tricktionary.getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        h.removeCallbacks(r);
    }
    public ArrayList<String> sortTrickList(ArrayList<String> list, int level){
        ArrayList<String>acc= new ArrayList<>();
        int multiples=0;
        int power=1;
        int manipulation=2;
        int releases=3;
        Collections.sort(list);
        Collections.reverse(list);

        acc.add(0, "Releases");
        acc.add(0, "Manipulation");
        acc.add(0, "Power");
        acc.add(0,"Multiples");
        for(int j=0;j<list.size();j++){

            if(Tricktionary.getTrickFromName(list.get(j), tricktionary).getType().equals("Multiples")){
                acc.add(multiples + 1, list.get(j));
                power++;
                manipulation++;
                releases++;
            }
            else if(Tricktionary.getTrickFromName(list.get(j), tricktionary).getType().equals("Power")){
                acc.add(power+1,list.get(j));
                manipulation++;
                releases++;
            }
            else if(Tricktionary.getTrickFromName(list.get(j), tricktionary).getType().equals("Manipulation")){
                acc.add(manipulation+1,list.get(j));
                releases++;
            }
            else if(Tricktionary.getTrickFromName(list.get(j), tricktionary).getType().equals("Releases")){
                acc.add(releases+1,list.get(j));
            }

        }



        return acc;
    }
    public ArrayList<String> addWhiteSpace(ArrayList<String> list){
        int index;
        ArrayList<String> acc= new ArrayList<>();
        for(int j=0;j<list.size();j++){
            acc.add(list.get(j));
            if(list.get(j).equals("Multiples")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,Tricktionary.DASHES);
                acc.add(index+2,Tricktionary.DASHES);


            }
            if(list.get(j).equals("Power")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,Tricktionary.DASHES);
                acc.add(index+2,Tricktionary.DASHES);

            }
            if(list.get(j).equals("Manipulation")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,Tricktionary.DASHES);
                acc.add(index+2,Tricktionary.DASHES);

            }
            if(list.get(j).equals("Releases")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,Tricktionary.DASHES);
                acc.add(index+2,Tricktionary.DASHES);

            }

        }
        return acc;
    }
    public Runnable r=new Runnable() {
        @Override
        public void run() {
            if(mAuth.getCurrentUser()!=null){
                TrickData.uId=mAuth.getCurrentUser().getUid();
            }
            if(tricktionary==null){
                Log.e("TrickCheck","Array is null");
                tricktionary=TrickData.getTricktionary();
            }
            else{
                Log.e("TrickCheck","Array is full!");
                populateLists();
                delay=60000; //change update time to 1 minute
                h.removeCallbacks(r);
            }
            h.postDelayed(this, delay);
        }
    };
    public void back(View v){
        finish();
    }

}
