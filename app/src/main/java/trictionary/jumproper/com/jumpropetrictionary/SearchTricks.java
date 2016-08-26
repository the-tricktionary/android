package trictionary.jumproper.com.jumpropetrictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class SearchTricks extends AppCompatActivity {


    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_toolbar_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search Tricks");
        setSupportActionBar(toolbar);


        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");
        PrimaryDrawerItem rafikiItem=new PrimaryDrawerItem().withName("Rafiki Program");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background)

                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Jump Rope Tricktionary")
                                .withIcon(getResources().getDrawable(R.drawable.icon_alpha))
                )
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(true)
                .build();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        mainMenuItem,
                        new DividerDrawerItem(),
                        tricktionaryItem,
                        new DividerDrawerItem(),
                        speedItem,
                        new DividerDrawerItem(),
                        randomTrickItem,
                        new DividerDrawerItem(),
                        showWriterItem,
                        new DividerDrawerItem(),
                        settingsItem,
                        new DividerDrawerItem(),
                        rafikiItem
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position==0){
                            return true;
                        }
                        if(position==1){
                            return true;
                        }
                        else if(position==3) {
                            Intent intent = new Intent(SearchTricks.this, Tricktionary.class);
                            startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(SearchTricks.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(SearchTricks.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(SearchTricks.this, Names.class);
                            startActivity(intent);
                        }

                        else if(position==11){
                            Intent intent = new Intent(SearchTricks.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if(position==13){
                            Intent intent = new Intent(SearchTricks.this, Rafiki.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();

        // Get the intent, verify the action and get the query
        searchBar = (EditText)findViewById(R.id.editText);
        searchBar.addTextChangedListener(searchQuery);



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
                TrickList.index = TrickList.getTrickFromName(itemValue,TrickData.getTricktionary()).getIndex();
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
        Trick[] arr = TrickData.getTricktionary();
        ArrayList<String> list = new ArrayList<>();
        for (int j=0; j < arr.length; j++){

            if (arr[j].getName().toLowerCase().contains(query.toLowerCase())) {
                list.add(arr[j].getName());

            }
        }
        return list;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }
    public void viewTricktionary(View v){
        Intent intent = new Intent(this, Tricktionary.class);
        startActivity(intent);
    }

}
