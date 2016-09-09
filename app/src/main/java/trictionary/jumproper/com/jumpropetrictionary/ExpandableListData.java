package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr_000 on 9/8/2016.
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableListData extends ContactActivity {

    //auth object for contact dialog
    private static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private static ArrayList<String> replies;

    public static HashMap<String, List<String>> getData() {

        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference myRef=fb.getReference("contact").child(mAuth.getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot contact:dataSnapshot.getChildren()){
                    GenericTypeIndicator<Reply> reply=new GenericTypeIndicator<Reply>(){};
                    replies = new ArrayList<String>();
                    for(DataSnapshot r:contact.child("replies").getChildren()){
                        replies.add(r.getValue(reply).toString());
                        Log.d("Replies",r.getValue(reply).toString());

                    }

                    expandableListDetail.put(contact.child("type").getValue().toString()+"\n\n\t"
                                            +contact.child("desc").getValue().toString(),
                                            replies);

                    ContactActivity.contactIds.put(contact.child("type").getValue().toString()+"\n\n\t"
                                            +contact.child("desc").getValue().toString(),
                                            contact.getKey());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return expandableListDetail;
    }


}
