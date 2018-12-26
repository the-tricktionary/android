package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.AddTrace;

import java.util.ArrayList;
import java.util.Collections;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.customviews.MyGridView;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickListAdapter;



public class Tricktionary extends BaseActivity{
    private ProgressBar loadingTricks;
    private FrameLayout tricktionaryLayout;
    private CheckBox showCompletedTricks;
    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String[]trickTypes;
    private final String RANDOM_TRICKS = "Random";
    private int randomTricks;
    private InterstitialAd mInterstitialAd;


    public static final String DASHES="";
    @Override
    @AddTrace(name = "tricktionaryOnCreateTrace")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tricktionary);

        MobileAds.initialize(this, "ca-app-pub-2959515976305980~3811712667");

        trickTypes = getResources().getStringArray(R.array.trick_types);
        tricktionary = ((GlobalData) this.getApplication()).getTricktionary();
        completedTricks = ((GlobalData) this.getApplication()).getCompletedTricks();
        mAuth = ((GlobalData) this.getApplication()).getmAuth();
        loadingTricks = (ProgressBar)findViewById(R.id.loading_tricks);
        tricktionaryLayout=(FrameLayout)findViewById(R.id.tricktionary_layout);
        showCompletedTricks=(CheckBox)findViewById(R.id.checkBox);

        //initialize analytic object and log an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        if(mAuth.getCurrentUser()!=null){
            bundle.putString("user", mAuth.getCurrentUser().getUid());
        }
        else{
            bundle.putString("user", "Guest");
        }
        mFirebaseAnalytics.logEvent("view_tricktionary", bundle);


        showCompletedTricks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mAuth.getCurrentUser()==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tricktionary.this); //new alert dialog
                    LayoutInflater inflater = (LayoutInflater)Tricktionary.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
                    final View textBoxes=inflater.inflate(R.layout.complete_tricks_dialog,null); //custom layout file now a view object
                    builder.setView(textBoxes); //set view to custom layout
                    builder.setPositiveButton(getString(R.string.sign_in), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Tricktionary.this, SignIn.class);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    showCompletedTricks.setChecked(true);
                    return;
                }
                else if(tricktionary==null){
                    showCompletedTricks.setChecked(!isChecked);
                    return;
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
        SharedPreferences settings = ((GlobalData) this.getApplication()).getSettings();
        if(settings != null) {
            randomTricks = settings.getInt(RANDOM_TRICKS, 0);
            if (randomTricks > 5) {
                randomTricks = 5;
            }
            boolean ads = true;
            try {
                ads = ((GlobalData) this.getApplication()).getAds();
            } catch (Exception e){
                ads = true;
            }
            if (ads) {
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId("ca-app-pub-2959515976305980/4123407956");
            }
        }
    }

    public void randomTrick(View v){
        SharedPreferences settings = ((GlobalData) this.getApplication()).getSettings();
        if(settings != null) {
            randomTricks = settings.getInt(RANDOM_TRICKS, 0);
            SharedPreferences.Editor editor = settings.edit();
            randomTricks++;
            editor.putInt(RANDOM_TRICKS, randomTricks);
            editor.commit();
            if (randomTricks > 5) {
                boolean ads = true;
                try {
                    ads = ((GlobalData) this.getApplication()).getAds();
                } catch (Exception e){
                    ads = true;
                }
                if (ads) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("2CC2625EB00F3EB58B6E5BC0B53C5A1D").build());
                }
            }
        }
        int size = 0;
        for (int i = 0; i < tricktionary.size(); i ++){
            size += tricktionary.get(i).size();
        }
        int index = (int)(Math.random() * size);
        int prevSize = 0;
        for (int i = 0; i < tricktionary.size(); i ++){
            if (Math.abs(prevSize - index) < tricktionary.get(i).size()){
                Log.e("Random", "" + index + ", " + prevSize);
                MainActivity.currentTrick=(Trick)tricktionary.get(i).get(Math.abs(prevSize - index));
            }
            prevSize += tricktionary.get(i).size();

        }

        if(MainActivity.currentTrick==null){
            return;
        }
        else if((MainActivity.currentTrick.getName()).equals(" ")){
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

    @Override
    public void onStart(){
        super.onStart();
    }


    @Override
    public void onResume(){
        super.onResume();
        boolean ads = true;
        try {
            ads = ((GlobalData) this.getApplication()).getAds();
        } catch (Exception e){
            ads = true;
        }

        if (mInterstitialAd == null) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-2959515976305980/4123407956");
        }
        if(tricktionary!=null) {
            Log.e("Ads", "random " + randomTricks);
            if (randomTricks > 5){
                if (ads) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        SharedPreferences settings = ((GlobalData) this.getApplication()).getSettings();
                        SharedPreferences.Editor editor = settings.edit();
                        randomTricks = 0;
                        editor.putInt(RANDOM_TRICKS,randomTricks);
                        editor.commit();
                    }
                } else {
                    Log.d("Ads", "The interstitial wasn't loaded yet.");
                }
            }
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

        final MyGridView level5GridView = (MyGridView) findViewById(R.id.level_5_grid_view);
        final ArrayList<Trick> level5List = new ArrayList<Trick>();

        if(tricktionary.size()==0){
            return;
        }
        for(int j=0;j<tricktionary.size();j++){
            for(Trick mTrick:tricktionary.get(j)) {
                if (mTrick.isChecklist()) {
                    Log.d("Tricks",mTrick.getName());
                } else if (mTrick.getType().equals(trickTypes[0])) {
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
            }
        }
        Collections.sort(basicsList,((GlobalData) this.getApplication()).getCompareName());
        final ArrayList<Trick>level1Sorted=addWhiteSpace(sortTrickList(level1List));
        final ArrayList<Trick>level2Sorted=addWhiteSpace(sortTrickList(level2List));
        final ArrayList<Trick>level3Sorted=addWhiteSpace(sortTrickList(level3List));
        final ArrayList<Trick>level4Sorted=addWhiteSpace(sortTrickList(level4List));
        //final ArrayList<Trick>level5Sorted=addWhiteSpace(sortTrickList(level5List));
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
            trickListGridViews[j].setAdapter(new TrickListAdapter(this, R.layout.trick_list_layout, trickLists[j]));
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
        ((ScrollView)findViewById(R.id.scrollView3)).smoothScrollTo(0,0);
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
        //sortedTricks.add(0,new Trick(trickTypes[0]));
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
        //TODO change back to stats, not network for testing
        Intent intent = new Intent(this, TrickNetwork.class);
        startActivity(intent);
    }
    public void viewProfile(View v){
        if(mAuth.getCurrentUser()==null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(getString(R.string.title_activity_profile));
            mBuilder.setMessage(getString(R.string.profile_sign_in));
            mBuilder.setPositiveButton(getString(R.string.sign_in), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(Tricktionary.this,SignIn.class);
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
        else{
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
    }
}

