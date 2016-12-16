package trictionary.jumproper.com.jumpropetrictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpeedGraph extends AppCompatActivity {
    AlertDialog editDialog;
    AlertDialog.Builder editDialogBuilder;
    ArrayList<Long> scrubbedTimes;
    TextView eventName,duration,score,avgJumps,maxJumps,numMisses,estimatedScore,jumpsLost,currentUser;
    ImageView editScore;
    RelativeLayout deleteDialog;
    LineChart chart;
    double avgJumpsPerSec,maxJumpsPerSec;
    int time,jumps,misses,scoreNoMisses,jumpDeficit;
    String jumperName;
    public static String finalDate;
    public static boolean loadingData=false;
    public static SpeedData data;
    Button saveData,authButton;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 4236;
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //analytic object for event logging
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_graph);

        // Configure Google Sign In
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);

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
        mAuth=FirebaseAuth.getInstance();

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

        eventName=(TextView)findViewById(R.id.event_name);
        duration = (TextView) findViewById(R.id.event_duration);
        score = (TextView) findViewById(R.id.score);
        avgJumps = (TextView) findViewById(R.id.avg_jumps_per_second);
        maxJumps = (TextView) findViewById(R.id.max_jumps_per_second);
        numMisses = (TextView) findViewById(R.id.num_misses);
        estimatedScore = (TextView) findViewById(R.id.score_no_misses);
        jumpsLost = (TextView) findViewById(R.id.jumps_lost);
        currentUser = (TextView)findViewById(R.id.current_user);
        saveData = (Button)findViewById(R.id.save_data);
        editScore = (ImageView)findViewById(R.id.edit_score);
        authButton=(Button)findViewById(R.id.auth_button);
        if((mAuth.getCurrentUser()==null)||(mAuth.getCurrentUser().getEmail()==null)){
            currentUser.setVisibility(View.INVISIBLE);
            authButton.setText("Sign In");
        }
        else{
            currentUser.setVisibility(View.VISIBLE);
            currentUser.setText(mAuth.getCurrentUser().getEmail().toString());
            authButton.setText("Sign Out");
        }

        if(loadingData){
            saveData.setVisibility(View.INVISIBLE);
            editScore.setVisibility(View.INVISIBLE);
            eventName.setVisibility(View.INVISIBLE);
            loadingData=false;
        }
        else if(data!=null){
            if(data.getName()==null)
                data.setName("");
            eventName.setVisibility(View.VISIBLE);
            eventName.setText(data.getName());
            saveData.setVisibility(View.INVISIBLE);
            editScore.setVisibility(View.VISIBLE);
            duration.setText(""+formatDuration(data.getTime()));
            score.setText(""+data.getScore());
            avgJumps.setText(""+data.getAvgJumps());
            maxJumps.setText(""+data.getMaxJumps());
            numMisses.setText(""+data.getMisses());
            estimatedScore.setText(""+data.getNoMissScore());
            jumpsLost.setText(""+data.getJumpsLost());
            drawGraph();
            //data=null;
        }
        else {
            chart = (LineChart) findViewById(R.id.chart);
            scrubbedTimes = scrubData(Speed.times);
            time=Speed.eventLength;
            jumps=Speed.numJumps;
            avgJumpsPerSec = averageJumpsPerSecond(scrubbedTimes);
            maxJumpsPerSec = maxJumpsPerSecond(scrubbedTimes);
            misses = getMisses(Speed.times);
            scoreNoMisses = (Speed.numJumps + ((int) (avgJumpsPerSec * misses) + 1));
            if(misses==0){
                scoreNoMisses=jumps;
                jumpDeficit=0;
            }
            else{
                jumpDeficit=(scoreNoMisses - Speed.numJumps);
            }

            data=new SpeedData(scrubbedTimes,avgJumpsPerSec,maxJumpsPerSec,misses,scoreNoMisses,
                    jumps,time,jumpDeficit,jumperName); //jumperName, maybe?

            duration.setText(formatDuration(data.getTime()));
            score.setText("" + data.getScore());
            avgJumps.setText("" + data.getAvgJumps());
            maxJumps.setText("" + data.getMaxJumps());
            numMisses.setText("" + data.getMisses());
            estimatedScore.setText("" + data.getNoMissScore());
            jumpsLost.setText("" + data.getJumpsLost());
            editScore.setVisibility(View.INVISIBLE);
            eventName.setVisibility(View.INVISIBLE);
            drawGraph();
            //initialize analytic object and log an event
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCORE,""+data.getScore());
            bundle.putString("duration",""+data.getTime());
            mFirebaseAnalytics.logEvent("score_clicked", bundle);
        }
        if (mAuth.getCurrentUser()!=null){
            SpeedDataSelect.mUid=mAuth.getCurrentUser().getUid().toString();
        }
    }



    public void signInButtonClick(View v){
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
        Log.e("Auth","Intent data: "+data.toString());
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
                            SpeedDataSelect.mUid=mAuth.getCurrentUser().getUid().toString();
                            currentUser.setVisibility(View.VISIBLE);
                            currentUser.setText(mAuth.getCurrentUser().getEmail().toString());
                            authButton.setText("Sign Out");
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithCredential", task.getException());
                            Toast.makeText(SpeedGraph.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    public void drawGraph(){
        chart = (LineChart) findViewById(R.id.chart);
        scrubbedTimes=data.getGraphData();
        if(scrubbedTimes.size()<1 || scrubbedTimes==null){
            return;//
        }
        else {
            ArrayList<String> jumpLabels = getXAxisValues(scrubbedTimes.size() - 1, data.getTime());
            ArrayList<Entry> values = new ArrayList<>();

            for (int j = 1; j < scrubbedTimes.size(); j++) {
                values.add(new Entry(100 * (1 / ((float) scrubbedTimes.get(j) - (float) scrubbedTimes.get(j - 1))), j - 1));
            }

            LineDataSet set = new LineDataSet(values, "Jumps per second");
            set.setDrawCubic(true);
            set.setDrawFilled(true);
            set.setDrawValues(false);
            set.setCircleRadius(0);

            LineData totalJumps = new LineData(jumpLabels, set);
            totalJumps.setDrawValues(false);

            XAxis bottomAxis = chart.getXAxis();
            bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            bottomAxis.setAvoidFirstLastClipping(true);

            YAxis leftAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);

            leftAxis.setAxisMinValue(0);
            leftAxis.setAxisMaxValue(6);


            chart.setDescription("");
            chart.setData(totalJumps);

            chart.invalidate();
        }
    }

    public ArrayList<Long> scrubData(ArrayList<Long> list){
        ArrayList<Long> scrubbed=new ArrayList<>();

            for (int j = 1; j < list.size(); j += 2) {


                scrubbed.add((list.get(j) + list.get(j - 1)) / 2);

            }


        return scrubbed;
    }

    public ArrayList<String> getXAxisValues(int size,int eventLength){
        double interval=(double)(eventLength)/size;
        double currentValue=0;
        ArrayList<String> values=new ArrayList<>();
        for(int j=0;j<size;j++){
            values.add(":" + String.format("%02d", (int)currentValue));
            currentValue+=interval;
        }
        values.remove(values.size()-1);
        values.add(":"+String.format("%02d", eventLength));
        return values;
    }

    public double averageJumpsPerSecond(ArrayList<Long> list){
        double avg=0;
        for(int j=1;j<list.size();j++){
            avg+=100*(1/((double)list.get(j)-(double)list.get(j-1)));
        }
        avg/=list.size();
        avg = Math.round(avg * 100);
        return avg/100;
    }
    public double maxJumpsPerSecond(ArrayList<Long> list){
        double max=0;
        double jumpsPer=0;
        for(int j=1;j<list.size();j++){
            jumpsPer=100*(1/((double)list.get(j)-(double)list.get(j-1)));
            if(jumpsPer>max){
                max=jumpsPer;
            }
        }
        max = Math.round(max * 100);

        return max/100;
    }

    public int getMisses(ArrayList<Long> list){
        int misses=0;
        double currentJumpsPerSec=0;
        for(int j=1;j<list.size();j++){
            currentJumpsPerSec=100*(1/((double)list.get(j)-(double)list.get(j-1)));
            System.out.println("Average / Current "+avgJumpsPerSec + " / " + currentJumpsPerSec + "from "+list.get(j));
            if (avgJumpsPerSec/currentJumpsPerSec > 1.5){
                misses++;
            }
        }
        return misses;

    }

    public String formatDuration(int secs){
        String duration="";
        duration+=""+secs/60;
        duration+=":"+String.format("%02d", secs%60);
        return duration;
    }

    public void viewSpeed(View v){
        data=null;
        finish();
    }

    public void viewPublicData(View v){
        Intent intent = new Intent(this, PublicSpeedData.class);
        finish();
        startActivity(intent);
    }

    public void saveData(View v){
        if(mAuth.getCurrentUser()==null){
            Log.e("Auth","Current user null");
            signInButtonClick(signInButton);
            mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(mAuth.getCurrentUser()!=null) {
                        Toast.makeText(getApplicationContext(),
                                "Signed in as " + mAuth.getCurrentUser().getEmail() + ".  Now you can save your data for access later.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            return;
        }

        else{
            Log.e("Auth","Current user: "+mAuth.getCurrentUser().getEmail().toString());
        }


        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=fb.getReference("speed").child("scores");


                myRef.child(mAuth.getCurrentUser().getUid().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(SpeedGraph.this); //new alert dialog
        builder.setTitle("Save Score"); //dialog title
        LayoutInflater inflater = (LayoutInflater)SpeedGraph.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.score_info_dialog,null); //custom layout file now a view object
        builder.setView(textBoxes); //set view to custom layout
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //prompt user for name
                EditText name = (EditText)textBoxes.findViewById(R.id.enter_jumper_name);

                setJumperName(name.getText().toString()+" "+Speed.getEventName());
                data=new SpeedData(scrubbedTimes,avgJumpsPerSec,maxJumpsPerSec,misses,scoreNoMisses,
                        jumps,time,jumpDeficit,jumperName); //jumperName, maybe?
                formatData();
                Toast.makeText(getApplicationContext(),
                        "Saved Data for "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(SpeedGraph.this, SpeedDataSelect.class);
                finish();
                startActivity(intent);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        saveData.setVisibility(View.INVISIBLE);






    }
    public void setJumperName(String name){
        jumperName=name;
    }


    public void formatData(){
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=fb.getReference("speed").child("scores");
        String date=""+System.currentTimeMillis()/1000;
        myRef.child(mAuth.getCurrentUser().getUid().toString()).child(date).setValue(data);
        data=null;
        Intent intent = new Intent(this, SpeedDataSelect.class);
        finish();
        startActivity(intent);
        return;
    }



    public void readData(View v){

        if(mAuth.getCurrentUser()==null){
            Log.e("Auth","Current user null");
            signInButtonClick(signInButton);
            return;

        }

        else {
            SpeedDataSelect.mUid=mAuth.getCurrentUser().getUid().toString();
            Intent intent = new Intent(this, SpeedDataSelect.class);
            finish();
            startActivity(intent);
        }
    }



    public void updateData(){
        saveData.setVisibility(View.INVISIBLE);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=fb.getReference("speed").child("scores");
        myRef.child(mAuth.getCurrentUser().getUid().toString()).child(finalDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Children of "+dataSnapshot.getValue());

                GenericTypeIndicator<SpeedData> sd = new GenericTypeIndicator<SpeedData>() {

                };
                System.out.println(dataSnapshot.getKey());
                data=dataSnapshot.getValue(sd);
                //System.out.println(data.toString());
                duration.setText(""+formatDuration(data.getTime()));
                score.setText(""+data.getScore());
                avgJumps.setText(""+data.getAvgJumps());
                maxJumps.setText(""+data.getMaxJumps());
                numMisses.setText(""+data.getMisses());
                estimatedScore.setText(""+data.getNoMissScore());
                jumpsLost.setText(""+data.getJumpsLost());
                drawGraph();
                return;
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                return;
            }
        });
    }

    public String formatEpoch(String str){
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (Long.parseLong(str)*1000));
        return date;
    }

    public void editData(View v){
        editDialogBuilder = new AlertDialog.Builder(SpeedGraph.this); //new alert dialog
        editDialogBuilder.setTitle("Edit Score"); //dialog title
        LayoutInflater inflater = (LayoutInflater)SpeedGraph.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.edit_score_dialog,null); //custom layout file now a view object
        final EditText name = (EditText)textBoxes.findViewById(R.id.edit_score_name);
        deleteDialog=(RelativeLayout)textBoxes.findViewById(R.id.delete_dialog);
        name.setText(data.getName());
        editDialogBuilder.setView(textBoxes); //set view to custom layout
        // Set up the buttons
        editDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //prompt user for name
                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                final DatabaseReference myRef=fb.getReference("speed").child("scores");
                myRef.child(mAuth.getCurrentUser().getUid().toString()).child(finalDate).child("name").setValue(name.getText().toString());

            }
        });
        editDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        editDialogBuilder.create();
        editDialog=editDialogBuilder.show();

    }

    public void deleteScore(View v){
        deleteDialog.setVisibility(View.VISIBLE);
    }
    public void yesDelete(View v){
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=fb.getReference("speed").child("scores");
        myRef.child(mAuth.getCurrentUser().getUid().toString()).child(finalDate).removeValue();
        deleteDialog.setVisibility(View.GONE);
        editDialog.cancel();
        Intent intent = new Intent(this, SpeedDataSelect.class);
        finish();
        startActivity(intent);
        Toast.makeText(getApplicationContext(),
                "Deleted score", Toast.LENGTH_SHORT)
                .show();
    }
    public void noDontDelete(View v){
        deleteDialog.setVisibility(View.GONE);
        editDialog.cancel();

    }
    public void beginAuth(View v){
        if(mAuth.getCurrentUser()==null){
            signInButtonClick(signInButton);
            authButton.setText("Sign Out");

        }
        else{
            userSignOut(v);
            authButton.setText("Sign In");
            currentUser.setVisibility(View.INVISIBLE);
        }
    }







}
