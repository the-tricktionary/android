package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import java.util.Arrays;


public class TrickList extends ActionBarActivity {

    public static int level=-1;
    public static boolean alphabet;
    public static String type="all";
    public static int index=0;
    TextView levelLabel, typeLabel, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_list);

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");

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
                        settingsItem
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
                            Intent intent = new Intent(TrickList.this, Tricktionary.class);
                            startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(TrickList.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(TrickList.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(TrickList.this, Names.class);
                            startActivity(intent);
                        }
                        else if(position==11){
                            Intent intent = new Intent(TrickList.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();



        final ListView listView = (ListView) findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();
        ArrayList<Trick> tricksInList=new ArrayList<Trick>();
        String[] trickNames=new String[TrickData.getTricktionary().length];
        title = (TextView) findViewById(R.id.title);

        for (int i = 0; i < trickNames.length; i++) {
            trickNames[i] = TrickData.getTricktionary()[i].getName();
        }
        if (alphabet) {


            Arrays.sort(trickNames);


        }
        if ((level==-1)&&(type.equals("all"))){
            title.setText("All Tricks");
            for (int i = 0; i < TrickData.getTricktionary().length; i++) {

                list.add(trickNames[i]);
                    tricksInList.add(getTrickFromName(trickNames[i],TrickData.getTricktionary()));

            }
        }
        else if(level==-1){
            title.setText(type);
            for (int i = 0; i < TrickData.getTricktionary().length; i++) {
                if (TrickData.getTricktionary()[i].getType().equals(type)) {
                    list.add(trickNames[i]);
                    tricksInList.add(getTrickFromName(trickNames[i],TrickData.getTricktionary()));
                }
            }
        }
        else if(type.equals("all")){
            title.setText("Level "+level+" Tricks");
            for (int i = 0; i < TrickData.getTricktionary().length; i++) {
                if (TrickData.getTricktionary()[i].getDifficulty() == level) {
                    list.add(trickNames[i]);
                    tricksInList.add(getTrickFromName(trickNames[i],TrickData.getTricktionary()));
                }
            }
        }

        else {
            title.setText("Level "+level+" "+type);
            for (int i = 0; i < TrickData.getTricktionary().length; ++i) {
                if ((TrickData.getTricktionary()[i].getDifficulty() == level) && (TrickData.getTricktionary()[i].getType().equals(type))) {
                    list.add(trickNames[i]);
                    tricksInList.add(getTrickFromName(trickNames[i],TrickData.getTricktionary()));
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
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
                index = getTrickFromName(itemValue,TrickData.getTricktionary()).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tricks_by_level, menu);
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
    public static void setLevel(int n){
        level=n;
    }
    public static void setType(String str){
        type=str;
    }
    //public void itemClicked(View v){
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    //}
    public static Trick getTrickFromName(String name, Trick[]arr){

        for(int j=0;j<arr.length;j++){
            if(arr[j].getName().equals(name)){
                return arr[j];
            }
        }
        return arr[0];
    }
    public void chooseSorting(View v){
        PopupMenu popupMenu = new PopupMenu(TrickList.this, title);
        //Inflating the Popup using xml file
        if(level==-1){popupMenu.getMenu().add("Level");}
        popupMenu.getMenu().add("Alphabetical");
        if(type.equals("all")){popupMenu.getMenu().add("Type");}


        popupMenu.show();
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(TrickList.this, "Sorted by: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getTitle().equals("Type")) {

                } else if (item.getTitle().equals("Level")) {

                }
                else if (item.getTitle().equals("Alphabetical")){

                }
                return true;
            }
        });

    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }
}
