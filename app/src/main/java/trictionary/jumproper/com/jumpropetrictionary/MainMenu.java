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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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


public class MainMenu extends AppCompatActivity {
    private VideoView myVideoView;
    ImageView header, settingsGear,webApp,contact,upload;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_toolbar_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Jump Rope Tricktionary");
        setSupportActionBar(toolbar);





        mAuth=FirebaseAuth.getInstance();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        header=(ImageView)findViewById(R.id.header);
        settingsGear =(ImageView)findViewById(R.id.settings);
        webApp=(ImageView)findViewById(R.id.view_webapp);
        contact=(ImageView)findViewById(R.id.view_contact);
        upload=(ImageView)findViewById(R.id.upload);
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
        DrawerCreate drawer=new DrawerCreate();
        drawer.makeDrawer(this, this, mAuth, toolbar, "Jump Rope Tricktionary");

        TrickData.getTricktionary();


        scaleTitleText(title);

        scaleText(viewTricktionary);
        scaleText(viewShowmaker);
        scaleText(viewTrickTree);
        scaleText(viewSpeedData);



        if(mAuth.getCurrentUser()!=null){
            TrickData.uId=mAuth.getCurrentUser().getUid();
            TrickData.fillCompletedTricks();
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
                // ...
            }
        };

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

        AnimatorSet anim=new AnimatorSet();
        anim.play(fadeHeaderIn).with(fadeTitleIn).with(fadeViewIn).with(fadeShowIn).with(fadeSettingsIn).with(fadeTrickTreeIn).with(fadeSpeedData).with(fadeContact).with(fadeWebApp).with(fadeUpload);
        anim.start();









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
}
