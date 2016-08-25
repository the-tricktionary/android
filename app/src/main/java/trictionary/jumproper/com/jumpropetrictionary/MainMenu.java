package trictionary.jumproper.com.jumpropetrictionary;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainMenu extends AppCompatActivity {
    private VideoView myVideoView;
    ImageView header, settingsGear;
    TextView title, viewTricktionary;
    TextView viewShowmaker,viewTrickTree,viewSpeedData;

    public static SharedPreferences settings;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_toolbar_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Jump Rope Tricktionary");
        setSupportActionBar(toolbar);





        TrickData.getTricktionary();
        header=(ImageView)findViewById(R.id.header);
        settingsGear =(ImageView)findViewById(R.id.settings);
        settingsGear.setMaxHeight(settingsGear.getHeight()/2);
        settingsGear.setMaxWidth(settingsGear.getWidth()/2);
        title=(TextView)findViewById(R.id.title);
        viewTricktionary=(TextView)findViewById(R.id.view_tricktionary);
        viewShowmaker=(TextView)findViewById(R.id.view_showmaker);
        viewTrickTree=(TextView)findViewById(R.id.view_trick_tree);
        viewSpeedData=(TextView)findViewById(R.id.speed_data);
        myVideoView = (VideoView) findViewById(R.id.video_view);
        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video_test));

        myVideoView.requestFocus();

        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                myVideoView.seekTo((int)(Math.random()*myVideoView.getDuration()));
                myVideoView.start();


            }

        });

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");
        PrimaryDrawerItem rafikiItem=new PrimaryDrawerItem().withName("Rafiki Program");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background)

                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Jump Rope Tricktionary")
                                .withIcon(getResources().getDrawable(R.drawable.icon_alpha))
                                .withNameShown(false)
                                .withEnabled(true)

                )
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(true)
                .build();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        mainMenuItem,
                        new DividerDrawerItem(),
                        tricktionaryItem,
                        new DividerDrawerItem(),
                        speedItem,
                        new DividerDrawerItem(),
                        randomTrickItem,
                        new DividerDrawerItem(),
                        showWriterItem,
                        new DividerDrawerItem(),
                        settingsItem,
                        new DividerDrawerItem(),
                        rafikiItem
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position==0){
                            return true;
                        }
                        if(position==1){
                            return true;
                        }
                        else if(position==3) {
                            Intent intent = new Intent(MainMenu.this, Tricktionary.class);
                            startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(MainMenu.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(MainMenu.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(MainMenu.this, Names.class);
                            startActivity(intent);
                        }
                        else if(position==11){
                            Intent intent = new Intent(MainMenu.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if(position==13){
                            Intent intent = new Intent(MainMenu.this, Rafiki.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();
        toolbar.setTitle("Jump Rope Tricktionary");




        scaleTitleText(title);

        scaleText(viewTricktionary);
        scaleText(viewShowmaker);
        scaleText(viewTrickTree);
        scaleText(viewSpeedData);

        ValueAnimator fadeHeaderIn = ObjectAnimator.ofFloat(header, "alpha", 0f, .75f);
        fadeHeaderIn.setDuration(2500);
        ValueAnimator fadeSettingsIn = ObjectAnimator.ofFloat(settingsGear, "alpha", 0f, .75f);
        fadeSettingsIn.setDuration(2500);
        ValueAnimator fadeTitleIn = ObjectAnimator.ofFloat(title, "alpha", 0f, .75f);
        fadeTitleIn.setDuration(2500);
        ValueAnimator fadeViewIn = ObjectAnimator.ofFloat(viewTricktionary, "alpha", 0f, .75f);
        fadeViewIn.setDuration(2500);
        ValueAnimator fadeShowIn = ObjectAnimator.ofFloat(viewShowmaker, "alpha", 0f, .75f);
        fadeShowIn.setDuration(2500);
        ValueAnimator fadeTrickTreeIn = ObjectAnimator.ofFloat(viewTrickTree, "alpha", 0f, .75f);
        fadeTrickTreeIn.setDuration(2500);
        ValueAnimator fadeSpeedData = ObjectAnimator.ofFloat(viewSpeedData, "alpha", 0f, .75f);
        fadeSpeedData.setDuration(2500);

        AnimatorSet anim=new AnimatorSet();
        anim.play(fadeHeaderIn).with(fadeTitleIn).with(fadeViewIn).with(fadeShowIn).with(fadeSettingsIn).with(fadeTrickTreeIn).with(fadeSpeedData);
        anim.start();

        TrickData.getTricktionaryData();








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
    public void randomTrick(View v){
        if(TrickData.tricktionary==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
            builder.setTitle("Please connect to internet");
            LayoutInflater inflater = (LayoutInflater)MainMenu.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View jackpotDialog=inflater.inflate(R.layout.no_internet_dialog,null);
            builder.setView(jackpotDialog);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        }
        else {
            TrickList.index = ((int) (Math.random() * TrickData.getTricktionary().length));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void viewTricktionary(View v){
        if(TrickData.tricktionary==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
            builder.setTitle("Please connect to internet");
            LayoutInflater inflater = (LayoutInflater)MainMenu.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View jackpotDialog=inflater.inflate(R.layout.no_internet_dialog,null);
            builder.setView(jackpotDialog);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        }
        else {
            Intent intent = new Intent(this, Tricktionary.class);
            startActivity(intent);
        }
    }
    public void viewShowmaker(View v){
        Intent intent = new Intent(this, Names.class);
        startActivity(intent);
    }
    public void viewTrickTree(View v){
        TrickTree.viewedTrick=TrickData.getTricktionary()[1];
        Intent intent = new Intent(this, TrickTree.class);
        startActivity(intent);
    }
    public void openSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void scaleTitleText(TextView v){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        int dpi = metrics.densityDpi;

        v.setTextSize(width/dpi*20);
    }
   public void scaleText(TextView v){
       DisplayMetrics metrics = new DisplayMetrics();
       getWindowManager().getDefaultDisplay().getMetrics(metrics);
       int width = metrics.widthPixels;

       int dpi = metrics.densityDpi;

       v.setTextSize(width/dpi*11);
   }
    public void viewSpeed(View v){
        Intent intent = new Intent(this, Speed.class);
        startActivity(intent);
    }

    public void loadSpeedData(View v){
        SpeedGraph.loadingData=true;
        Intent intent = new Intent(this, SpeedGraph.class);
        startActivity(intent);
    }
}
