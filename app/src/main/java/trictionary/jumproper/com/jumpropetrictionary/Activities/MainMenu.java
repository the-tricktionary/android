package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.perf.metrics.AddTrace;

import java.util.Locale;
import java.util.Set;

import trictionary.jumproper.com.jumpropetrictionary.show.Names;
import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickData;


public class MainMenu extends BaseActivity {
    private VideoView myVideoView;
    ImageView header, settingsGear,webApp,contact,upload,profile;
    TextView title, viewTricktionary;
    TextView viewShowmaker,viewTrickTree,viewSpeedData;

    public static SharedPreferences settings;

    //auth object for contact dialog
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 4236;
    SignInButton signInButton;




    @Override
    @AddTrace(name = "mainMenuOnCreateTrace")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TrickData.getTricktionaryData();

        mAuth=FirebaseAuth.getInstance();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        header=(ImageView)findViewById(R.id.header);
        settingsGear =(ImageView)findViewById(R.id.settings);
        webApp=(ImageView)findViewById(R.id.view_webapp);
        contact=(ImageView)findViewById(R.id.view_contact);
        upload=(ImageView)findViewById(R.id.upload);
        profile=(ImageView)findViewById(R.id.profile_icon);
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
                myVideoView.start();


            }

        });
        fadeViews();

        settings=getSharedPreferences(SettingsActivity.PREFS_NAME,0);
        getSupportActionBar().setTitle("");
        setupWindowAnimations();

        scaleTitleText(title);
        scaleText(viewTricktionary);
        scaleText(viewShowmaker);
        scaleText(viewTrickTree);
        scaleText(viewSpeedData);

        if(mAuth.getCurrentUser()!=null){
            TrickData.uId=mAuth.getCurrentUser().getUid();
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.google_sign_in_auth_id))
                .requestIdToken(getString(R.string.google_sign_in_auth_id))
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("Auth","Connection Failed");
                    }
                })
                .build();
        //add auth listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    TrickData.uId=user.getUid();
                    
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");

                }
            }
        };
    }
    @Override
    public void onResume(){
        super.onResume();
        myVideoView.seekTo((int)(Math.random()*myVideoView.getDuration()));
    }
    @Override
    public void onStart(){
        super.onStart();
        if(settings.getString(SettingsActivity.LANGUAGE_SETTING,null)==null){
            if(Locale.getDefault().getDisplayLanguage()!=null) {
                if (Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
                    SettingsActivity.setLanguage(Locale.getDefault().getDisplayLanguage());
                }
                if (Locale.getDefault().getDisplayLanguage().equals("Svenska")) {
                    SettingsActivity.setLanguage(Locale.getDefault().getDisplayLanguage());
                } else {
                    SettingsActivity.setLanguage("English");
                }
            }
            else{
                SettingsActivity.setLanguage("English");
            }
        }
        TrickData.getTricktionaryData();
    }

    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(100);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
            getWindow().setExitTransition(fade);
        }
    }

    private void fadeViews(){
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
        ValueAnimator fadeWebApp = ObjectAnimator.ofFloat(webApp, "alpha", 0f, .75f);
        fadeWebApp.setDuration(2500);
        ValueAnimator fadeContact = ObjectAnimator.ofFloat(contact, "alpha", 0f, .75f);
        fadeContact.setDuration(2500);
        ValueAnimator fadeUpload = ObjectAnimator.ofFloat(upload, "alpha", 0f, .75f);
        fadeUpload.setDuration(2500);
        ValueAnimator fadeProfile = ObjectAnimator.ofFloat(profile, "alpha", 0f, .75f);
        fadeProfile.setDuration(2500);

        AnimatorSet anim=new AnimatorSet();
        anim.play(fadeHeaderIn)
                .with(fadeTitleIn)
                .with(fadeViewIn)
                .with(fadeShowIn)
                .with(fadeSettingsIn)
                .with(fadeTrickTreeIn)
                .with(fadeSpeedData)
                .with(fadeContact)
                .with(fadeWebApp)
                .with(fadeUpload)
                .with(fadeProfile);
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }
    public void randomTrick(View v){
        if(TrickData.tricktionary2d==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
            builder.setTitle("Please connect to internet");
            LayoutInflater inflater = (LayoutInflater)MainMenu.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View noInternetDialog=inflater.inflate(R.layout.no_internet_dialog,null);
            builder.setView(noInternetDialog);
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
            int randId0=(int)(Math.random()*4);
            int randId1=(int)(Math.random()*TrickData.getTricktionary().get(randId0).size());
            MainActivity.currentTrick = TrickData.getTricktionary().get(randId0).get(randId1);
            Intent intent = new Intent(this, MainActivity.class);
            View sharedView = v;
            String transitionName = getString(R.string.logo_transition);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainMenu.this, sharedView, transitionName);
                startActivity(intent,transitionActivityOptions.toBundle());
            }
            else {
                startActivity(intent);
            }

        }
    }
    public void viewTricktionary(View v){
        if(TrickData.tricktionary2d==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
            builder.setTitle("Please connect to internet");
            LayoutInflater inflater = (LayoutInflater)MainMenu.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View noInternetDialog=inflater.inflate(R.layout.no_internet_dialog,null);
            builder.setView(noInternetDialog);
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
            else {
                startActivity(intent);
            }
        }
    }
    public void viewShowmaker(View v){
        Intent intent = new Intent(this, Names.class);
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

        v.setTextSize(width/dpi*18);
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
    public void viewWeb(View v){
        String url = "https://the-tricktionary.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void viewContact(View v){
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
            return;
        }
        else {
            signIn();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.e("Auth","Intent: "+signInIntent.toString());
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    public void userSignOut(View v){
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //data=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.e("Auth","Request Code: "+requestCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("Auth","result success: "+result.isSuccess());
            Log.e("Auth","result: "+result.getStatus().toString());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Auth", "firebaseAuthWithGoogle:" + acct.getId());
        Log.d("Auth", "firebaseAuthWithGoogleToken:" + acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(task.isComplete()){
                            Toast.makeText(MainMenu.this, "Signed in as "+mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            viewContact(contact);
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithCredential", task.getException());
                            Toast.makeText(MainMenu.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    public void submitTrick(View v){
        Intent intent = new Intent(this, Submit.class);
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
                    Intent intent=new Intent(MainMenu.this,SignIn.class);
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
