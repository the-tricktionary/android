package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Friend;

public class Friends extends BaseActivity {
    private ListView friendsList;
    private int friendCount=0;
    private ArrayList<Friend> friends;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mAuth = FirebaseAuth.getInstance();
        friendsList = (ListView)findViewById(R.id.profile_listview);
        friends = new ArrayList<>();
        if(mAuth.getCurrentUser()!=null) {
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fb.getReference("users").child(mAuth.getCurrentUser().getUid()).child("friends");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        friendCount = (int) dataSnapshot.getChildrenCount();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addNewFriend(View v){
        if(mAuth.getCurrentUser()!=null) {
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fb.getReference("users").child(mAuth.getCurrentUser().getUid()).child("friends");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
