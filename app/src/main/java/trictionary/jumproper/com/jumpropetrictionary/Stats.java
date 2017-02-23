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
        DatabaseReference myRef=fb.getReference("stats").child("checklist");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTricks.setText(""+dataSnapshot.child("total").getValue().toString());
                numLevel1Tricks.setText(""+dataSnapshot.child("0").getValue().toString());
                numLevel2Tricks.setText(""+dataSnapshot.child("1").getValue().toString());
                numLevel3Tricks.setText(""+dataSnapshot.child("2").getValue().toString());
                numLevel4Tricks.setText(""+dataSnapshot.child("3").getValue().toString());
                averageTricks.setText(""+(dataSnapshot.child("avg").getValue().toString()));
                maxTricks.setText(""+dataSnapshot.child("max").getValue().toString());
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
