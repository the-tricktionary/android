package trictionary.jumproper.com.jumpropetrictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;


public class Tricktionary extends ActionBarActivity{
    public static Trick[] tricktionary;
    public static ArrayList<Trick> completedTricks;
    ProgressBar loadingTricks;
    FrameLayout tricktionaryLayout;
    CheckBox showCompletedTricks;
    int delay = 100; //milliseconds
    int completedIndex=0;
    Handler h;
    private FirebaseAuth mAuth;

    public static final String DASHES="  ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricktionary_toolbar_layout);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        loadingTricks = (ProgressBar)findViewById(R.id.loading_tricks);
        tricktionaryLayout=(FrameLayout)findViewById(R.id.tricktionary_layout);
        showCompletedTricks=(CheckBox)findViewById(R.id.checkBox);

        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        TrickList.level=-1;
        TrickList.alphabet=true;
        TrickList.type="all";
        tricktionary=TrickData.getTricktionary();




        DrawerCreate drawer=new DrawerCreate();
        drawer.makeDrawer(this, this, mAuth, toolbar," ");
        getSupportActionBar().setTitle("");

        if(mAuth.getCurrentUser()!=null){
            TrickData.uId=mAuth.getCurrentUser().getUid();
        }
        showCompletedTricks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mAuth.getCurrentUser()==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tricktionary.this); //new alert dialog
                    LayoutInflater inflater = (LayoutInflater)Tricktionary.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
                    final View textBoxes=inflater.inflate(R.layout.complete_tricks_dialog,null); //custom layout file now a view object
                    builder.setView(textBoxes); //set view to custom layout
                    builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Tricktionary.this, SignIn.class);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    showCompletedTricks.setChecked(true);
                }
                h.removeCallbacks(r);
                completedIndex=0;
                if(b){
                    for(int j=0;j<tricktionary.length;j++){
                        if(tricktionary[j].isCompleted()){
                            tricktionary[j].setChecklist(false);
                        }
                    }
                    h.post(r);
                }
                else{
                    for(int j=0;j<tricktionary.length;j++){
                        for(int i=completedIndex;i<completedTricks.size();i++){
                            if (tricktionary[j].equals(completedTricks.get(i))) {
                                completedIndex++;
                                tricktionary[j].setChecklist(true);
                            }
                        }
                    }
                    h.post(r);
                }
            }
        });

        toolbar.setTitle("");

        h = new Handler();
        h.postDelayed(r, delay);


    }

    private void setupWindowAnimations() {
        Fade fade = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fade = new Fade();
            fade.setDuration(25);
            getWindow().setEnterTransition(fade);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        completedIndex=0;
        if(showCompletedTricks.isChecked()){
            for(int j=0;j<tricktionary.length;j++){
                if(tricktionary[j].isCompleted()){
                    tricktionary[j].setChecklist(false);
                }
            }
            h.post(r);
        }
        else{
            if(tricktionary!=null && completedTricks!=null) {
                for (int j = 0; j < tricktionary.length; j++) {
                    for (int i = completedIndex; i < completedTricks.size(); i++) {
                        if (tricktionary[j].equals(completedTricks.get(i))) {
                            completedIndex++;
                            tricktionary[j].setChecklist(true);
                        }
                    }
                }
                h.post(r);
            }
        }
    }
    public Runnable r=new Runnable() {
        @Override
        public void run() {
            //do something
            if(mAuth.getCurrentUser()!=null){
                TrickData.uId=mAuth.getCurrentUser().getUid();
            }
            if(tricktionary==null){
                loadingTricks.setVisibility(View.VISIBLE);
                Log.e("TrickCheck","Array is null");
                tricktionary=TrickData.getTricktionary();
            }
            else{
                Log.e("TrickCheck","Array is full!");
                populateLists();
                loadingTricks.setVisibility(View.GONE);
                tricktionaryLayout.refreshDrawableState();
                delay=60000; //change update time to 1 minute
                h.removeCallbacks(r);
            }
            h.postDelayed(this, delay);
        }
    };

    public void populateLists(){


        final MyGridView basicsGridView = (MyGridView) findViewById(R.id.basics_grid_view);
        final ArrayList<String> basicsList = new ArrayList<String>();

        final MyGridView level1GridView = (MyGridView) findViewById(R.id.level_1_grid_view);
        final ArrayList<String> level1List = new ArrayList<String>();

        final MyGridView level2GridView = (MyGridView) findViewById(R.id.level_2_grid_view);
        final ArrayList<String> level2List = new ArrayList<String>();

        final MyGridView level3GridView = (MyGridView) findViewById(R.id.level_3_grid_view);
        final ArrayList<String> level3List = new ArrayList<String>();

        final MyGridView level4GridView = (MyGridView) findViewById(R.id.level_4_grid_view);
        final ArrayList<String> level4List = new ArrayList<String>();

        if(tricktionary==null){
            h.postDelayed(r, delay);
            return;
        }
        for(int j=0;j<tricktionary.length;j++){
            if(tricktionary[j].isChecklist()){
                j++;
            }
            else if(tricktionary[j].getType().equals("Basics")){
                basicsList.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==1){
                level1List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==2){
                level2List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==3){
                level3List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==4){
                level4List.add(tricktionary[j].getName());
            }
        }
        Collections.sort(basicsList);
        final ArrayList<String>level1Sorted=addWhiteSpace(sortTrickList(level1List, 1));
        final ArrayList<String>level2Sorted=addWhiteSpace(sortTrickList(level2List, 2));
        final ArrayList<String>level3Sorted=addWhiteSpace(sortTrickList(level3List, 3));
        final ArrayList<String>level4Sorted=addWhiteSpace(sortTrickList(level4List, 4));

        final String[]ignoredStrings={"Multiples","Power","Manipulation","Releases",DASHES};

        ArrayAdapter<String> basicsAdapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, basicsList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                int color;
                if(completedTricks.size()>0) {
                    for(int j=0;j<completedTricks.size();j++) {
                        if (basicsList.get(position).equals(completedTricks.get(j).getName())) {
                            color = getResources().getColor(R.color.colorAccent); // Material Red
                            view.setBackgroundColor(color);
                            view.setClickable(true);
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setTextColor(Color.WHITE);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }
                    }
                }
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
                if(completedTricks.size()>0) {
                    for(int j=0;j<completedTricks.size();j++) {
                        if (level1Sorted.get(position).equals(completedTricks.get(j).getName())) {
                            //completedTricks.remove(j);
                            color = getResources().getColor(R.color.colorAccent); // Material Red
                            view.setBackgroundColor(color);
                            view.setClickable(true);
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setTextColor(Color.WHITE);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }
                    }
                }
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
                if(completedTricks.size()>0) {
                    for(int j=0;j<completedTricks.size();j++) {
                        if (level2Sorted.get(position).equals(completedTricks.get(j).getName())) {
                            //completedTricks.remove(j);
                            color = getResources().getColor(R.color.colorAccent); // Material Red
                            view.setBackgroundColor(color);
                            view.setClickable(true);
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setTextColor(Color.WHITE);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }
                    }
                }
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
                if(completedTricks.size()>0) {
                    for(int j=0;j<completedTricks.size();j++) {
                        if (level3Sorted.get(position).equals(completedTricks.get(j).getName())) {
                            //completedTricks.remove(j);
                            color = getResources().getColor(R.color.colorAccent); // Material Red
                            view.setBackgroundColor(color);
                            view.setClickable(true);
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setTextColor(Color.WHITE);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }
                    }
                }
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
                if(completedTricks.size()>0) {
                    for(int j=0;j<completedTricks.size();j++) {
                        if (level4Sorted.get(position).equals(completedTricks.get(j).getName())) {
                            //completedTricks.remove(j);
                            color = getResources().getColor(R.color.colorAccent); // Material Red
                            view.setBackgroundColor(color);
                            view.setClickable(true);
                            view.setVisibility(View.VISIBLE);
                            ((TextView) view).setTextColor(Color.WHITE);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }
                    }
                }
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
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
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
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
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
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
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
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
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
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        tricktionaryLayout.refreshDrawableState();
        h.removeCallbacks(r);
    }



    public static ArrayList<String> sortTrickList(ArrayList<String> list, int level){
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

            if(getTrickFromName(list.get(j), tricktionary).getType().equals("Multiples")){
                acc.add(multiples + 1, list.get(j));
                power++;
                manipulation++;
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Power")){
                acc.add(power+1,list.get(j));
                manipulation++;
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Manipulation")){
                acc.add(manipulation+1,list.get(j));
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Releases")){
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
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);


            }
            if(list.get(j).equals("Power")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }
            if(list.get(j).equals("Manipulation")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }
            if(list.get(j).equals("Releases")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }

        }
        return acc;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tricktionary, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static void fillTricktionary(){
        tricktionary=TrickData.getTricktionary();
    }
    public static Trick getTrickFromName(String name, Trick[]arr){

        for (Trick anArr : arr) {
            if (anArr.getName().equals(name)) {
                return anArr;
            }
        }
        return arr[0];
    }


    public void viewCurrentSettings(View v){
        TrickList.alphabet=true;
        TrickList.type="all";
        TrickList.level=-1;
        Intent intent = new Intent(this, TrickList.class);
        startActivity(intent);




    }
    public void mainMenu(View v){
        finish();
    }
    public void back(View v){ finish(); }

    public void startSearch(View v){
        Intent intent = new Intent(this, SearchTricks.class);
        startActivity(intent);
    }
    public void submitTrick(View v){
        Intent intent = new Intent(this, Submit.class);
        startActivity(intent);
    }
    public void viewStats(View v){
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
    }
    public void viewProfile(View v){
        if(mAuth.getCurrentUser()==null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Profile");
            mBuilder.setMessage("You must sign in to access your profile and store trick statistics.");
            mBuilder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(Tricktionary.this,SignIn.class);
                    startActivity(intent);
                }
            });
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            mBuilder.show();
        }
        else{
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
    }
}

