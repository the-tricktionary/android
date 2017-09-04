package trictionary.jumproper.com.jumpropetrictionary.show;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.activities.BaseActivity;


public class Show extends BaseActivity {

    ListView showList;
    ArrayAdapter mArrayAdapter;
    TextView yourShow;
    TextView breakEvents;
    ArrayList<Event>sortedShow2=new ArrayList<Event>();
    ShareActionProvider mShareActionProvider;
    int pos;
    boolean firstTap=false;
    Context context;
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        showList = (ListView) findViewById(R.id.sorted_show);
        yourShow=(TextView)findViewById(R.id.events);
        sortedShow2=new ArrayList<Event>();
        breakEvents=(TextView)findViewById(R.id.break_events);
        breakEvents.setText("Each jumper has a minimum of " + ShowMainActivity.getBreakEvents() + " breaks between events.");
        for(int j=0;j<ShowMainActivity.sortedShow.size();j++){
            sortedShow2.add(ShowMainActivity.sortedShow.get(j));
            //sortedShow2.get(j).setEvent((j+1)+" " +sortedShow2.get(j).getEvent());
        }
        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                ShowMainActivity.sortedShow);
        showList.setAdapter(mArrayAdapter);
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (breakEvents.getText().equals("Which event should this be after?\n")) {
                    ShowMainActivity.sortedShow.add(position+1, ShowMainActivity.sortedShow.remove(pos));
                    firstTap = false;
                    breakEvents.setText("Each jumper has a minimum of " + ShowMainActivity.getBreakEvents() + " breaks between events.");
                    mArrayAdapter.notifyDataSetChanged();
                    showList.refreshDrawableState();
                } else {
                    breakEvents.setText("Which event should this be after?\n");
                    pos = position;
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        //MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        //if (shareItem != null) {
            //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //}

        // Create an Intent to share your content
        setShareIntent();

        return true;
    }
    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sorted Show");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareShow(sortedShow2));

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    public String shareShow(ArrayList<Event> show){
        String acc="";
        for(int j=0;j<show.size();j++){
            acc+=show.get(j)+"\n";
        }
        return acc;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void backToWriter(View v){
        finish();
    }
}
