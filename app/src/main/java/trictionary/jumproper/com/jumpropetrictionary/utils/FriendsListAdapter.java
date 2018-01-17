package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
            Button approve = (Button) v.findViewById(R.id.friend_approve_request);

            if (name != null) {
                name.setText(p.getName()[0]+" "+p.getName()[1]);
            }

            if (img != null) {
                DownloadImageTask downloadImage = new DownloadImageTask(img);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    downloadImage.execute(p.getImageUrl());
                }
            }

            if (approve != null) {
                if(!p.isApproved()){
                    approve.setVisibility(View.VISIBLE);
                    approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            approveRequest(p);
                        }
                    });
                }
            }
        }

        return v;
    }

    public void approveRequest(Friend friend){

    }

}