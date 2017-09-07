package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import trictionary.jumproper.com.jumpropetrictionary.R;

import static trictionary.jumproper.com.jumpropetrictionary.activities.Tricktionary.DASHES;

/**
 * Created by jumpr on 3/11/2017.
 */

public class TrickListAdapter extends ArrayAdapter<Trick> {
    private final String[]ignoredStrings={"Multiples","Power","Manipulation","Releases",DASHES};
    private int color;
    private boolean completedColor=true;

    public TrickListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TrickListAdapter(Context context, int resource, List<Trick> items) {
        super(context, resource, items);
    }

    public TrickListAdapter(Context context, int resource, List<Trick> items, boolean completedColor){
        super(context,resource,items);
        this.completedColor=completedColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.trick_list_layout, null);
        }

        Trick mTrick = getItem(position);
        ((TextView) v).setText(mTrick.getName());
        for(int j = 0;j<ignoredStrings.length;j++){
            if(mTrick.getName().equals(ignoredStrings[j])){
                color = getContext().getResources().getColor(R.color.colorPrimary); // Material Red
                v.setBackgroundColor(color);
                v.setClickable(false);
                ((TextView) v).setTextColor(Color.WHITE);
            }
        }
        if (mTrick.isCompleted() && completedColor) {
            color = getContext().getResources().getColor(R.color.colorAccent); // Material Yellow
            v.setBackgroundColor(color);
            //((TextView) v).setTextColor(Color.WHITE);
        }
        v.setClickable(false);
        return v;
    }

}
