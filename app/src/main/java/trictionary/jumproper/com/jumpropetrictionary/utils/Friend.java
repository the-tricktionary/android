package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jumpr on 1/13/2018.
 */

public class Friend {
    private String uId;
    private String username;
    private String[] name = new String[2];
    private String imageUrl;
    private boolean approved;

    public Friend(){
        this.uId = "";
        this.name[0] = "Anon";
        this.name[1] = "";
        this.imageUrl = "";
        this.approved = false;
    }

    public Friend(String username){
        this.username = username;
        final FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fb.getReference("usernames").child(username);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setuId(dataSnapshot.getValue().toString());
                final DatabaseReference friend = fb.getReference("users").child(uId).child("profile");
                friend.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("name")){
                            String[] mName = new String[2];
                            mName[0] = dataSnapshot.child("name").child("0").getValue().toString();
                            mName[1] = dataSnapshot.child("name").child("1").getValue().toString();
                            setName(mName);
                        }
                        if (dataSnapshot.hasChild("image")){
                            setImageUrl(dataSnapshot.child("image").getValue().toString());
                            Log.d("img URL",getImageUrl());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Friend(String uId, String[] name, String imageUrl, boolean approved) {
        this.uId = uId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.approved = approved;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
