package trictionary.jumproper.com.jumpropetrictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Stats extends AppCompatActivity {
    private TextView numTricks,numLevel1Tricks,numLevel2Tricks,numLevel3Tricks,numLevel4Tricks,averageTricks,maxTricks;
    private int numTricksCount,numLevel1Count,numLevel2Count,numLevel3Count,numLevel4Count,numUsers,maxCount;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        numTricks=(TextView)findViewById(R.id.num_tricks);
        numLevel1Tricks=(TextView)findViewById(R.id.num_level_1_tricks);
        numLevel2Tricks=(TextView)findViewById(R.id.num_level_2_tricks);
        numLevel3Tricks=(TextView)findViewById(R.id.num_level_3_tricks);
        numLevel4Tricks=(TextView)findViewById(R.id.num_level_4_tricks);
        averageTricks=(TextView)findViewById(R.id.average_tricks);
        maxTricks=(TextView)findViewById(R.id.max_tricks);


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
                numUsers=0;
                maxCount=0;
                int count=0;
                for(DataSnapshot ids:dataSnapshot.getChildren()){
                    numUsers++;
                    for(DataSnapshot id0:ids.getChildren()){
                        for(DataSnapshot tricks:id0.getChildren()){
                            count++;
                            if(Integer.parseInt(id0.getKey().toString())==0){
                                numLevel1Count++;
                            }
                            if(Integer.parseInt(id0.getKey().toString())==1){
                                numLevel2Count++;
                            }
                            if(Integer.parseInt(id0.getKey().toString())==2){
                                numLevel3Count++;
                            }
                            if(Integer.parseInt(id0.getKey().toString())==3){
                                numLevel4Count++;
                            }
                            numTricksCount++;
                        }
                        if(count>maxCount){
                            maxCount=count;
                        }
                        count=0;
                    }
                }
                numTricks.setText(""+numTricksCount);
                numLevel1Tricks.setText(""+numLevel1Count);
                numLevel2Tricks.setText(""+numLevel2Count);
                numLevel3Tricks.setText(""+numLevel3Count);
                numLevel4Tricks.setText(""+numLevel4Count);
                averageTricks.setText(""+(numTricksCount/numUsers));
                maxTricks.setText(""+maxCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void back(View v){
        finish();
    }

    public void viewProfile(View v){
        if(mAuth.getCurrentUser()==null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Profile");
            mBuilder.setMessage("You must sign in to access your profile and store trick statistics.");
            mBuilder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(Stats.this,SignIn.class);
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
