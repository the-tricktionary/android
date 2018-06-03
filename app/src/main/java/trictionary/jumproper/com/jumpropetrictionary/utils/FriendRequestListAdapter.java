package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import trictionary.jumproper.com.jumpropetrictionary.R;

/**
 * Created by jumpr_000 on 1/15/2018.
 */

public class FriendRequestListAdapter extends ArrayAdapter<String> {
    private DatabaseReference friends;

    public FriendRequestListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FriendRequestListAdapter(Context context, int resource, List<String> items, DatabaseReference friends) {
        super(context, resource, items);
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friend_request_list_item, null);
        }

        final String p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.friend_username);
            ImageView confirm = (ImageView) v.findViewById(R.id.confirm_friend_request);
            ImageView deny = (ImageView) v.findViewById(R.id.deny_friend_request);

            name.setText(p);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friends.child("friends").child(p).child("mutual").setValue(false);
                    Toast.makeText(getContext(),
                            "Accepted request from "+p, Toast.LENGTH_LONG)
                            .show();
                }
            });
            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase fb = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = fb.getReference("usernames").child(p);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            friends.child("friendrequests").child(dataSnapshot.getValue().toString()).removeValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        return v;
    }

    public void approveRequest(Friend friend){

    }

    

}