package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

public class SearchTricks extends BaseActivity {

    private FirebaseAuth mAuth;
    ArrayList<Integer> trickId0=new ArrayList<>();
    ArrayList<Integer> trickId1=new ArrayList<>();
    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tricks);
        mAuth=FirebaseAuth.getInstance();

        // Get the intent, verify the action and get the query
        searchBar = (EditText)findViewById(R.id.editText);
        searchBar.addTextChangedListener(searchQuery);



    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }

    private final TextWatcher searchQuery = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            return;
        }

        public void afterTextChanged(Editable s) {
            startSearch(searchBar);
        }
    };

    public void searchButtonPressed(View v){
        searchBar.setText("");
    }
    public void startSearch(View v){

        ArrayList<String> results = doMySearch(searchBar.getText().toString());
        final ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, results);
        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                MainActivity.currentTrick= ((GlobalData) SearchTricks.this.getApplication()).getTricktionary().get(trickId0.get(position))
                        .get(trickId1.get(position));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    public ArrayList<String> doMySearch(String query){
        ArrayList<ArrayList<Trick>> tricktionary= ((GlobalData) this.getApplication()).getTricktionary();
        ArrayList<String> list = new ArrayList<>();
        trickId0.clear();
        trickId1.clear();
        for(int j=0;j<tricktionary.size();j++){
            for(Trick mTrick:tricktionary.get(j)){
                if (mTrick.getName().toLowerCase().contains(query.toLowerCase())) {
                    list.add(mTrick.getName());
                    trickId0.add(mTrick.getId0());
                    trickId1.add(mTrick.getId1());
                }
            }
        }
        return list;
    }
    public void viewTricktionary(View v){
        finish();
    }

}
