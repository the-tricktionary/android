
package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.metrics.AddTrace;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.contact.Contact;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, AppCompatCallback {
    //declare text views
    private TextView level, type, trickName, trickDescription, fisacLevel;

    //declare image views
    private ImageView fisacExpand, wjrVerified, fisacVerified, fullScreen;

    private Spinner prereqsSpinner, nextTricksSpinner;

    //completed trick checkbox and email replies
    private CheckBox trickCompleted,emailReplies;

    //these are required for the toolbar because MainActivity already extends a class
    private AppCompatActivity appCompatActivity;
    private AppCompatDelegate mDelegate;

    //declare YouTube player objects
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //declare drawer object
    //private Drawer result;

    //current trick being viewed in MainActivity
    public static Trick currentTrick;


    //declare Trick array and index of current trick
    private ArrayList<ArrayList<Trick>> tricktionary;

    //analytic object for event logging
    private FirebaseAnalytics mFirebaseAnalytics;

    //auth object for contact dialog
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 4236;
    Button signInButton;

    //contact type
    String contactTypeName="General";
    String organization;
    EditText contactName;




    @Override
    @AddTrace(name = "mainActivityOnCreateTrace")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //required for setSupportActionBar
        appCompatActivity=new AppCompatActivity();
        setContentView(R.layout.main_activity_toolbar_layout);

        if(currentTrick==null){
            Toast.makeText(this,"Oops, something went wrong loading that trick!",Toast.LENGTH_LONG);
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }
        setupWindowAnimations();

        //declare, initialize and set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //populate the tricktionary with the most up to date data from firebase
        tricktionary= ((GlobalData) this.getApplication()).getTricktionary();

        //initialize analytic object and log an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        if(currentTrick.getName()!=null) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, currentTrick.getName());
            mFirebaseAnalytics.logEvent("view_trick", bundle);
        }
        else{
            finish();
        }

        //initialize auth object
        mAuth = FirebaseAuth.getInstance();

        //display trick name
        toolbar.setTitle(currentTrick.getName());

        //initialize YouTube view object
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        //initialize text views and set them to display trick data
        trickName = (TextView)findViewById(R.id.name);
        trickName.setText(currentTrick.getName());
        trickDescription = (TextView)findViewById(R.id.description);
        trickDescription.setText(currentTrick.getDescription());
        level=(TextView)findViewById(R.id.level_label);
        level.setText("WJR Level: " + currentTrick.getWjrLevel());
        type=(TextView)findViewById(R.id.type_label);
        type.setText(currentTrick.getType());
        fisacLevel=(TextView)findViewById(R.id.fisac_level);
        fisacExpand=(ImageView)findViewById(R.id.fisac_expand);
        fullScreen=(ImageView)findViewById(R.id.view_full_screen);
        trickCompleted=(CheckBox)findViewById(R.id.trick_completed);
        prereqsSpinner=(Spinner)findViewById(R.id.prereqs_spinner);
        nextTricksSpinner=(Spinner)findViewById(R.id.next_tricks_spinner);

        if(currentTrick==null){
            finish();
        }

        if(currentTrick.getFisacLevel().equals("")) {
            fisacLevel.setVisibility(View.INVISIBLE);
        }
        else if(currentTrick.getFisacLevel().length()>3){
            fisacLevel.setText("IRSF Level: " + currentTrick.getFisacLevel().substring(0,3)+"...");
            fisacExpand.setVisibility(View.VISIBLE);
        }
        else{
            fisacLevel.setText("IRSF Level: " + currentTrick.getFisacLevel());
        }
        if(currentTrick.isCompleted()){
            trickCompleted.setChecked(true);
        }

        setPrereqs();
        setNextTricks();
        scaleText(trickDescription,7);
        scaleText(trickName,8);
        scaleText(level,7);
        scaleText(fisacLevel,7);
        scaleText(type,7);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.google_sign_in_auth_id))
                .requestIdToken(getString(R.string.google_sign_in_auth_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("Auth","connection result: "+connectionResult.toString());
                    }
                })
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        trickCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mAuth.getCurrentUser()!=null){
                    FirebaseDatabase fb=FirebaseDatabase.getInstance();
                    final DatabaseReference myRef=fb.getReference("checklist");
                    myRef.child(mAuth.getCurrentUser().getUid())
                            .child(""+currentTrick.getId0())
                            .child(""+currentTrick.getId1())
                            .setValue(b);
                    currentTrick.setCompleted(true);
                }
                else{
                    if(mAuth.getCurrentUser()==null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //new alert dialog
                        //builder.setTitle("Submit reply"); //dialog title
                        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
                        final View textBoxes=inflater.inflate(R.layout.complete_tricks_dialog,null); //custom layout file now a view object
                        builder.setView(textBoxes); //set view to custom layout
                        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, SignIn.class);
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
                        trickCompleted.setChecked(false);
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference myRef=fb.getReference("tricks")
                .child(""+currentTrick.getId0())
                .child("subs")
                .child(""+currentTrick.getId1())
                .child("levels");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()) {
                    Log.e("Verifying", child.getKey().toString() + child.getValue().toString());
                }
                if(dataSnapshot.child("irsf").hasChild("verified")) {
                    final DataSnapshot fisacData = dataSnapshot
                            .child("irsf")
                            .child("verified");
                    if(fisacData.hasChild("verified")) {
                        if ((Boolean) fisacData.child("verified").getValue()) {
                            fisacVerified = (ImageView) findViewById(R.id.fisac_verified);
                            fisacVerified.setVisibility(View.VISIBLE);
                            fisacVerified.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String message;
                                    String verifier;
                                    if (Integer.parseInt(fisacData.child("vLevel").getValue().toString()) == 1) {
                                        verifier = "federation official";
                                    } else {
                                        verifier = "judge";
                                    }

                                    message = "This trick was verified by a "
                                            + verifier
                                            + " on "
                                            + fisacData.child("date").getValue().toString();
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                    mBuilder.setTitle("Level Verification");
                                    mBuilder.setMessage(message);
                                    mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    mBuilder.show();
                                }
                            });
                        }
                    }
                }
                if(dataSnapshot.child("wjr").hasChild("verified")) {
                    final DataSnapshot wjrData = dataSnapshot
                            .child("wjr")
                            .child("verified");
                    if(wjrData.hasChild("verified")) {
                        if ((Boolean) wjrData.child("verified").getValue()) {
                            wjrVerified = (ImageView) findViewById(R.id.wjr_verified);
                            wjrVerified.setVisibility(View.VISIBLE);
                            wjrVerified.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String message;
                                    String verifier;
                                    if (Integer.parseInt(wjrData.child("vLevel").getValue().toString()) == 1) {
                                        verifier = "federation official";
                                    } else {
                                        verifier = "judge";
                                    }

                                    message = "This trick was verified by a "
                                            + verifier
                                            + " on "
                                            + wjrData.child("date").getValue().toString();
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                    mBuilder.setTitle("Level Verification");
                                    mBuilder.setMessage(message);
                                    mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    mBuilder.show();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayer.setFullscreen(true);
                youTubePlayer.setPlayerStyle(PlayerStyle.DEFAULT);
            }
        });
    }

    private void setupWindowAnimations() {
        Fade fade = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fade = new Fade();
            fade.setDuration(25);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        youTubeView.initialize(getString(R.string.developer_key), this);
        mAuth.addAuthStateListener(mAuthListener);
        if(currentTrick.isCompleted()){
            trickCompleted.setChecked(true);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }
    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, this);
        }
        return mDelegate;
    }
    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        youTubePlayer=player;
        if(currentTrick.isCompleted()){
            trickCompleted.setChecked(true);
        }
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            if(SettingsActivity.autoPlayEnabled()) {
                player.loadVideo(currentTrick.getVideoCode());
            }
            else{
                player.cueVideo(currentTrick.getVideoCode());
            }
            // Hiding player controls
            if(SettingsActivity.getPlayerStyle().equals("Minimal")) {
                player.setPlayerStyle(PlayerStyle.MINIMAL);
            }
            else if(SettingsActivity.getPlayerStyle().equals("Chromeless")) {
                player.setPlayerStyle(PlayerStyle.CHROMELESS);
            }
            else{
                player.setPlayerStyle(PlayerStyle.DEFAULT);
            }
            player.setShowFullscreenButton (true);
        }
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                return;
            }

            @Override
            public void onLoaded(String s) {
                return;
            }

            @Override
            public void onAdStarted() {
                return;
            }

            @Override
            public void onVideoStarted() {
                return;
            }

            @Override
            public void onVideoEnded() {
                youTubePlayer.setFullscreen(false);
                if(SettingsActivity.getPlayerStyle().equals("Minimal")) {
                    youTubePlayer.setPlayerStyle(PlayerStyle.MINIMAL);
                }
                else if(SettingsActivity.getPlayerStyle().equals("Chromeless")) {
                    youTubePlayer.setPlayerStyle(PlayerStyle.CHROMELESS);
                }
                else{
                    youTubePlayer.setPlayerStyle(PlayerStyle.DEFAULT);
                }
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                return;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.developer_key), this);
        }
        else if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

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

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    public void viewTricktionary(View view){
        finish();
    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }
    public void setPrereqs(){
        try {
            ArrayList<String> prereqs=new ArrayList<>();
            prereqs.add("Prerequisites:");
            if (currentTrick.getPrereqs().length > 0) {
                for (int j = 0; j < currentTrick.getPrereqs().length; j++) {
                    prereqs.add(tricktionary.get(currentTrick.getPrereqsId0()[j]).get(currentTrick.getPrereqsId1()[j]).getName());
                }
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, prereqs);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            prereqsSpinner.setAdapter(adapter);
            prereqsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString();
                    if (item.equals("Prerequisites:")) {
                        return;
                    }
                    Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                    int pos = i-1;
                    currentTrick = tricktionary.get(currentTrick.getPrereqsId0()[pos])
                            .get(currentTrick.getPrereqsId1()[pos]);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch(Exception e){
            FirebaseCrash.log("Error viewing prereqs "+e.getMessage());
        }
    }
    public void setNextTricks(){
        ArrayList<String> nextTricks=new ArrayList<>();
        ArrayList<Integer> nextTricksId0=new ArrayList<Integer>();
        ArrayList<Integer> nextTricksId1=new ArrayList<Integer>();
        nextTricks.add("Next Tricks:");
        for(int j=currentTrick.getId0();j<tricktionary.size();j++) { //dont bother looking at levels before this level
            for (Trick mTrick : tricktionary.get(j)) {
                for (int i = 0; i < mTrick.getPrereqs().length; i++) {
                    if(i<mTrick.getPrereqsId0().length) {
                        if (mTrick.getPrereqsId0()[i] == currentTrick.getId0()) {
                            if (mTrick.getPrereqsId1()[i] == currentTrick.getId1()) {
                                if (!(mTrick.equals(currentTrick))) {
                                    nextTricks.add(mTrick.getName());
                                    nextTricksId0.add(mTrick.getId0());
                                    nextTricksId1.add(mTrick.getId1());
                                }
                            }
                        }
                    }
                }
            }
        }
        currentTrick.setNextTricksId0(nextTricksId0);
        currentTrick.setNextTricksId1(nextTricksId1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, nextTricks);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        nextTricksSpinner.setAdapter(adapter);
        nextTricksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if(item.equals("Next Tricks:")){
                    return;
                }
                Toast.makeText(MainActivity.this,item, Toast.LENGTH_SHORT).show();
                int pos=i-1;
                currentTrick = tricktionary.get(currentTrick.getNextTricksId0().get(pos))
                        .get(currentTrick.getNextTricksId1().get(pos));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public ArrayList<ArrayList<Trick>> getTricktionary(){
        return tricktionary;
    }
    public void shareTrick(View v){
        //create the send intent
        Intent shareIntent =
                new Intent(android.content.Intent.ACTION_SEND);

        //set the type
        shareIntent.setType("text/plain");

        //add a subject
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                currentTrick.getName());

        //build the body of the message to be shared
        String shareMessage = "https://the-tricktionary.com/details/"+currentTrick.getId0()+"/"+currentTrick.getId1();

        //add the message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareMessage);

        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent,
                "Share video link to this trick with:"));
    }
    public void scaleText(TextView v,float scale){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpi = metrics.densityDpi;
        v.setTextSize(width/dpi*scale);
    }



    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        return;
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
return;
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    public void expandFisacLevel(View v){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        //Inflating the Popup using xml file
        popupMenu.getMenu().add(currentTrick.getFisacLevel());
        popupMenu.show();
    }

    public void openContactDialog(View v){
        //Set up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //new alert dialog
        builder.setTitle("Submit feeback on "+currentTrick.getName()); //dialog title
        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.contact_dialog,null); //custom layout file now a view object
        builder.setView(textBoxes); //set view to custom layout
        final EditText comment = (EditText)textBoxes.findViewById(R.id.contact_comment);
        contactName = (EditText)textBoxes.findViewById(R.id.contact_name);
        final EditText correctLevel = (EditText)textBoxes.findViewById(R.id.correct_level);
        final TextView trickName=(TextView)textBoxes.findViewById(R.id.trick_name);
        final Spinner contactType=(Spinner)textBoxes.findViewById(R.id.contact_type);
        final Spinner orgSpinner=(Spinner)textBoxes.findViewById(R.id.org_spinner);
        final LinearLayout contactGeneral=(LinearLayout)textBoxes.findViewById(R.id.contact_general);
        final RelativeLayout incorrectLevel=(RelativeLayout)textBoxes.findViewById(R.id.contact_incorrect_level);
        emailReplies = (CheckBox)textBoxes.findViewById(R.id.email_replies);
        signInButton=(Button)textBoxes.findViewById(R.id.sign_in_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contact_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactType.setAdapter(adapter);
        contactType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contactTypeName=adapterView.getItemAtPosition(i).toString();
                if(contactTypeName.equals("Incorrect Level")){
                    contactGeneral.setVisibility(View.GONE);
                    incorrectLevel.setVisibility(View.VISIBLE);
                    trickName.setText(currentTrick.getName());

                }
                else{
                    contactGeneral.setVisibility(View.VISIBLE);
                    incorrectLevel.setVisibility(View.GONE);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                return;
            }
        });
        if(mAuth.getCurrentUser()!=null){
            if(mAuth.getCurrentUser().getDisplayName()!=null) {
                signInButton.setVisibility(View.GONE);
                contactName.setText(mAuth.getCurrentUser().getDisplayName());
            }
        }


        ArrayAdapter<CharSequence> orgAdapter = ArrayAdapter.createFromResource(this,
                R.array.organizations, android.R.layout.simple_spinner_item);
        orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orgSpinner.setAdapter(orgAdapter);
        orgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                organization=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        // Set up the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Anonymous auth
                if(mAuth.getCurrentUser()==null) {
                    mAuth.signInAnonymously()
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Auth", "signInAnonymously:onComplete:" + task.isSuccessful());
                                    if((contactTypeName.equals("Incorrect Level"))&&(correctLevel.getText().toString().length()>0)){
                                        FirebaseDatabase fb=FirebaseDatabase.getInstance();
                                        DatabaseReference myRef=fb.getReference("contact");
                                        Contact data;
                                        Log.d("emailReplies","Checked:"+emailReplies.isChecked());
                                        if(emailReplies.isChecked()){
                                             data=new Contact(contactName.getText().toString(),
                                                    contactTypeName,
                                                    currentTrick.getName(),
                                                    ""+currentTrick.getId1(),
                                                    ""+currentTrick.getId0(),
                                                    organization,
                                                    correctLevel.getText().toString(),
                                                    mAuth.getCurrentUser().getEmail());
                                        }
                                        else {
                                             data = new Contact(contactName.getText().toString(),
                                                    contactTypeName,
                                                    currentTrick.getName(),
                                                    ""+currentTrick.getId1(),
                                                    ""+currentTrick.getId0(),
                                                    organization,
                                                    correctLevel.getText().toString());
                                        }
                                        myRef.child(mAuth.getCurrentUser().getUid())
                                                .child(myRef.push().getKey())
                                                .setValue(data);
                                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,currentTrick.getName());
                                        mFirebaseAnalytics.logEvent("contact_submit", bundle);
                                        Toast.makeText(MainActivity.this, "Feedback on incorrect level submitted, thank you!", Toast.LENGTH_LONG).show();

                                    }
                                    else if(comment.getText().toString().length()>0){
                                        FirebaseDatabase fb=FirebaseDatabase.getInstance();
                                        DatabaseReference myRef=fb.getReference("contact");
                                        Contact data;
                                        if(emailReplies.isChecked()) {
                                             data = new Contact(contactName.getText().toString(),
                                                    contactTypeName,
                                                    currentTrick.getName() + " - " + comment.getText().toString(),
                                                    ""+currentTrick.getId1(),
                                                    ""+currentTrick.getId0(),
                                                    mAuth.getCurrentUser().getEmail());
                                        }
                                        else{

                                            data = new Contact(contactName.getText().toString(),
                                                    contactTypeName,
                                                    currentTrick.getName() + " - " + comment.getText().toString(),
                                                    ""+currentTrick.getId1(),
                                                    ""+currentTrick.getId0(),
                                                    mAuth.getCurrentUser().getEmail());
                                        }
                                        myRef.child(mAuth.getCurrentUser().getUid())
                                                .child(myRef.push().getKey())
                                                .setValue(data);
                                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,currentTrick.getName());
                                        mFirebaseAnalytics.logEvent("contact_submit", bundle);
                                        Toast.makeText(MainActivity.this, "Feedback submitted, thank you!", Toast.LENGTH_LONG).show();
                                        Log.d("Contact","UID: "+mAuth.getCurrentUser().getUid());
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "You must provide more information please.", Toast.LENGTH_LONG).show();
                                    }

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w("Auth", "signInAnonymously", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // ...
                                }
                            });
                }
                else if((contactTypeName.equals("Incorrect Level"))&&(correctLevel.getText().toString().length()>0)){
                    FirebaseDatabase fb=FirebaseDatabase.getInstance();
                    DatabaseReference myRef=fb.getReference("contact");
                    Contact data;
                    if(emailReplies.isChecked()) {
                         data = new Contact(contactName.getText().toString(),
                                contactTypeName,
                                currentTrick.getName(),
                                ""+currentTrick.getId1(),
                                ""+currentTrick.getId0(),
                                organization,
                                correctLevel.getText().toString(),
                                 mAuth.getCurrentUser().getEmail());
                    }
                    else{
                        data = new Contact(contactName.getText().toString(),
                                contactTypeName,
                                currentTrick.getName(),
                                ""+currentTrick.getId1(),
                                ""+currentTrick.getId0(),
                                organization,
                                correctLevel.getText().toString());
                    }
                    myRef.child(mAuth.getCurrentUser().getUid())
                            .child(myRef.push().getKey())
                            .setValue(data);
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,currentTrick.getName());
                    mFirebaseAnalytics.logEvent("contact_submit", bundle);
                    Toast.makeText(MainActivity.this, "Feedback on incorrect level submitted, thank you!", Toast.LENGTH_LONG).show();

                }
                else if(comment.getText().toString().length()>0){
                    FirebaseDatabase fb=FirebaseDatabase.getInstance();
                    DatabaseReference myRef=fb.getReference("contact");
                    Contact data;
                    if(emailReplies.isChecked()) {
                        data = new Contact(contactName.getText().toString(),
                                contactTypeName,
                                currentTrick.getName() + " - " + comment.getText().toString(),
                                ""+currentTrick.getId1(),
                                ""+currentTrick.getId0(),
                                mAuth.getCurrentUser().getEmail());
                    }
                    else{
                        data = new Contact(contactName.getText().toString(),
                                contactTypeName,
                                currentTrick.getName() + " - " + comment.getText().toString(),
                                ""+currentTrick.getId1(),
                                ""+currentTrick.getId0());
                    }
                    myRef.child(mAuth.getCurrentUser().getUid())
                            .child(myRef.push().getKey())
                            .setValue(data);
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,currentTrick.getName());
                    mFirebaseAnalytics.logEvent("contact_submit", bundle);
                    Toast.makeText(MainActivity.this, "Feedback submitted, thank you!", Toast.LENGTH_LONG).show();
                    Log.d("Contact","UID: "+mAuth.getCurrentUser().getUid());
                }
                else{
                    Toast.makeText(MainActivity.this, "You must provide more information please.", Toast.LENGTH_LONG).show();
                }




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
    public void signIn(View v){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
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
                            Toast.makeText(MainActivity.this, "Signed in as "+mAuth.getCurrentUser().getEmail()+", you may now submit feedback.",
                                    Toast.LENGTH_SHORT).show();
                            if(signInButton!=null) {
                                signInButton.setVisibility(View.GONE);
                            }
                            if(contactName!=null) {
                                contactName.setText(mAuth.getCurrentUser().getDisplayName());
                            }
                            if(trickCompleted.isChecked()){
                                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                                final DatabaseReference myRef=fb.getReference("checklist");
                                myRef.child(mAuth.getCurrentUser().getUid())
                                        .child(""+currentTrick.getId0())
                                        .child(""+currentTrick.getId1())
                                        .setValue(true);
                            }
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    public void signInButtonClick(View v){
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn(signInButton);
                break;
        }
    }


}