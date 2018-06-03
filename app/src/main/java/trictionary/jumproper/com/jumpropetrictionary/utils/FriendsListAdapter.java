package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    public FriendsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FriendsListAdapter(Context context, int resource, List<Friend> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friend_list_item, null);
        }

        final Friend p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.friend_name);
            ImageView img = (ImageView) v.findViewById(R.id.friend_profile_image);

            if (name != null) {
                name.setText(p.getUsername());
                //name.setText(p.getName()[0]+" "+p.getName()[1]);
            }

            if (img != null) {
                final DownloadImageTask downloadImage = new DownloadImageTask(img);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    if(p.getImageUrl()!=null) {
                        downloadImage.execute(p.getImageUrl());
                    }
                    else {
                        final FirebaseDatabase fb = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = fb.getReference("usernames").child(p.getUsername());
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String uId = (dataSnapshot.getValue().toString());
                                final DatabaseReference friend = fb.getReference("users").child(uId).child("profile");
                                friend.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("image")) {
                                            String imgUrl = dataSnapshot.child("image").getValue().toString();
                                            Log.d("img URL", imgUrl);
                                            downloadImage.execute(imgUrl);
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
                }
            }
        }

        return v;
    }

    public void approveRequest(Friend friend){

    }

}