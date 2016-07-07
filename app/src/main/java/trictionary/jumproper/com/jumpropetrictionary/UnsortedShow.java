package trictionary.jumproper.com.jumpropetrictionary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class UnsortedShow extends ActionBarActivity {

    ListView showList;
    ArrayAdapter mArrayAdapter;


    ArrayList<Event>sortedShow2=new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsorted_show);

        showList = (ListView) findViewById(R.id.sorted_show);
        for (int j = 0; j < ShowMainActivity.show.size(); j++) {
            sortedShow2.add(ShowMainActivity.show.get(j));
        }


        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                sortedShow2);
        showList.setAdapter(mArrayAdapter);
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                sortedShow2.get(position).setName("*" + sortedShow2.get(position).getName());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unsorted_show, menu);
        return true;
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
}
