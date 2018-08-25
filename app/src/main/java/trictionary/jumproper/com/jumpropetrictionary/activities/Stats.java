package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.MobileAds;


import trictionary.jumproper.com.jumpropetrictionary.R;

public class Stats extends BaseActivity {
    private TextView numTricks,numLevel1Tricks,numLevel2Tricks,numLevel3Tricks,numLevel4Tricks,numLevel5Tricks,averageTricks,maxTricks;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mAuth = FirebaseAuth.getInstance();
        numTricks = (TextView) findViewById(R.id.num_tricks);
        numLevel1Tricks = (TextView) findViewById(R.id.num_level_1_tricks);
        numLevel2Tricks = (TextView) findViewById(R.id.num_level_2_tricks);
        numLevel3Tricks = (TextView) findViewById(R.id.num_level_3_tricks);
        numLevel4Tricks = (TextView) findViewById(R.id.num_level_4_tricks);
        numLevel5Tricks = (TextView) findViewById(R.id.num_level_5_tricks);
        averageTricks = (TextView) findViewById(R.id.average_tricks);
        maxTricks = (TextView) findViewById(R.id.max_tricks);

        MobileAds.initialize(this, "ca-app-pub-2959515976305980~3811712667");
        AdView adView = findViewById(R.id.stats_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("2CC2625EB00F3EB58B6E5BC0B53C5A1D")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("Ad", "Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Ad", "Could not load error: " + errorCode);
            }
        });


        final FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fb.getReference("stats").child("checklist");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTricks.setText("" + dataSnapshot.child("total").getValue().toString());
                numLevel1Tricks.setText("" + dataSnapshot.child("0").getValue().toString());
                numLevel2Tricks.setText("" + dataSnapshot.child("1").getValue().toString());
                numLevel3Tricks.setText("" + dataSnapshot.child("2").getValue().toString());
                numLevel4Tricks.setText("" + dataSnapshot.child("3").getValue().toString());
                numLevel5Tricks.setText("" + dataSnapshot.child("4").getValue().toString());
                averageTricks.setText("" + (dataSnapshot.child("avg").getValue().toString()));
                maxTricks.setText("" + dataSnapshot.child("max").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }

    public void back(View v) {
                finish();
            }

    public void viewProfile(View v){
        if(mAuth.getCurrentUser()==null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(R.string.title_activity_profile);
            mBuilder.setMessage(R.string.stats_sign_in);
            mBuilder.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(Stats.this,SignIn.class);
                    startActivity(intent);
                }
            });
            mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
