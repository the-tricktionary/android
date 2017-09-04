package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.AddTrace;

import java.util.ArrayList;
import java.util.Collections;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.customviews.MyGridView;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickData;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickListAdapter;


public class Tricktionary extends BaseActivity{
    private ProgressBar loadingTricks;
    private FrameLayout tricktionaryLayout;
    private CheckBox showCompletedTricks;
    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private FirebaseAuth mAuth;

    public static final String DASHES="  ";
    @Override
    @AddTrace(name = "tricktionaryOnCreateTrace")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tricktionary);
        setupWindowAnimations();
        tricktionary = ((GlobalData) this.getApplication()).getTricktionary();
        completedTricks = ((GlobalData) this.getApplication()).getCompletedTricks();
        mAuth = ((GlobalData) this.getApplication()).getmAuth();
        loadingTricks = (ProgressBar)findViewById(R.id.loading_tricks);
        tricktionaryLayout=(FrameLayout)findViewById(R.id.tricktionary_layout);
        showCompletedTricks=(CheckBox)findViewById(R.id.checkBox);

        showCompletedTricks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
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
                else {
                    if (isChecked) {
                        for (int j = 0; j < tricktionary.size(); j++) {
                            for (Trick mTrick : tricktionary.get(j)) {
                                if (mTrick.isCompleted()) {
                                    mTrick.setChecklist(false);
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < tricktionary.size(); j++) {
                            for (Trick mTrick : tricktionary.get(j)) {
                                for (int i = 0; i < completedTricks.size(); i++) {
                                    for (Trick mCompletedTrick : completedTricks.get(i)) {
                                        if (mTrick.equals(mCompletedTrick)) {
                                            mTrick.setChecklist(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    populateLists();
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(100);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
            getWindow().setEnterTransition(fade);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(tricktionary!=null) {
            if (showCompletedTricks.isChecked()) {
                for (int j = 0; j < tricktionary.size(); j++) {
                    for(Trick mTrick:tricktionary.get(j)) {
                        if (mTrick.isCompleted()) {
                            mTrick.setChecklist(false);
                        }
                    }
                }
                populateLists();
            } else {
                if (tricktionary != null && completedTricks != null) {
                    for (int j = 0; j < tricktionary.size(); j++) {
                        for(Trick mTrick:tricktionary.get(j)) {
                            for (int i = 0; i < completedTricks.size(); i++) {
                                for(Trick mCompletedTrick:completedTricks.get(i)) {
                                    if (mTrick.equals(mCompletedTrick)) {
                                        mTrick.setChecklist(true);
                                    }
                                }
                            }
                        }
                    }
                    populateLists();
                }
            }
        }
        else{
            finish();
        }
    }
    public void openSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void populateLists(){


        final MyGridView basicsGridView = (MyGridView) findViewById(R.id.basics_grid_view);
        final ArrayList<Trick> basicsList = new ArrayList<Trick>();

        final MyGridView level1GridView = (MyGridView) findViewById(R.id.level_1_grid_view);
        final ArrayList<Trick> level1List = new ArrayList<Trick>();

        final MyGridView level2GridView = (MyGridView) findViewById(R.id.level_2_grid_view);
        final ArrayList<Trick> level2List = new ArrayList<Trick>();

        final MyGridView level3GridView = (MyGridView) findViewById(R.id.level_3_grid_view);
        final ArrayList<Trick> level3List = new ArrayList<Trick>();

        final MyGridView level4GridView = (MyGridView) findViewById(R.id.level_4_grid_view);
        final ArrayList<Trick> level4List = new ArrayList<Trick>();

        if(tricktionary.size()==0){
            return;
        }
        for(int j=0;j<tricktionary.size();j++){
            for(Trick mTrick:tricktionary.get(j)) {
                if (mTrick.isChecklist()) {
                    Log.d("Tricks",mTrick.getName());
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
            trickListGridViews[j].setAdapter(new TrickListAdapter(this, R.layout.trick_list_layout, trickLists[j]));
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
        ((ScrollView)findViewById(R.id.scrollView3)).smoothScrollTo(0,0);
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

