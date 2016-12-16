package trictionary.jumproper.com.jumpropetrictionary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Stats extends AppCompatActivity {
    private TextView numTricks;
    int numTricksCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numTricks=(TextView)findViewById(R.id.num_tricks);

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference myRef=fb.getReference("checklist");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ids:dataSnapshot.getChildren()){
                    for(DataSnapshot id0:ids.getChildren()){
                        for(DataSnapshot tricks:id0.getChildren()){
                            numTricksCount++;
                        }
                    }
                }
                numTricks.setText(""+numTricksCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
