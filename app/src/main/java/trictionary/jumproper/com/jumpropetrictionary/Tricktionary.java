package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
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
import java.util.Collections;


public class Tricktionary extends ActionBarActivity {
    TextView sortBy, level1, level2, level3, level4, level5, viewTricks;
    ScrollView type, levels;

    public static Trick[] tricktionary;

    public static final String DASHES="  ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricktionary_toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TrickList.level=-1;
        TrickList.alphabet=true;
        TrickList.type="all";
        tricktionary=TrickData.getTricktionary();


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
                        settingsItem
                )
                .withSelectedItem(-1)

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position==0){
                            Intent intent = new Intent(Tricktionary.this, MainMenu.class);
                            startActivity(intent);
                        }
                        if(position==1){
                            Intent intent = new Intent(Tricktionary.this, MainMenu.class);
                            startActivity(intent);
                        }
                        else if(position==3) {
                            return true;
                        }
                        else if(position==5){
                            Intent intent = new Intent(Tricktionary.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(Tricktionary.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(Tricktionary.this, Names.class);
                            startActivity(intent);
                        }
                        else if(position==11){
                            Intent intent = new Intent(Tricktionary.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();









        final MyGridView basicsGridView = (MyGridView) findViewById(R.id.basics_grid_view);
        final ArrayList<String> basicsList = new ArrayList<String>();

        final MyGridView level1GridView = (MyGridView) findViewById(R.id.level_1_grid_view);
        final ArrayList<String> level1List = new ArrayList<String>();

        final MyGridView level2GridView = (MyGridView) findViewById(R.id.level_2_grid_view);
        final ArrayList<String> level2List = new ArrayList<String>();

        final MyGridView level3GridView = (MyGridView) findViewById(R.id.level_3_grid_view);
        final ArrayList<String> level3List = new ArrayList<String>();

        final MyGridView level4GridView = (MyGridView) findViewById(R.id.level_4_grid_view);
        final ArrayList<String> level4List = new ArrayList<String>();


        for(int j=0;j<tricktionary.length;j++){
            if(tricktionary[j].getType().equals("Basics")){
                basicsList.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==1){
                level1List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==2){
                level2List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==3){
                level3List.add(tricktionary[j].getName());
            }
            else if(tricktionary[j].getDifficulty()==4){
                level4List.add(tricktionary[j].getName());
            }
        }
        Collections.sort(basicsList);
        final ArrayList<String>level1Sorted=addWhiteSpace(sortTrickList(level1List, 1));
        final ArrayList<String>level2Sorted=addWhiteSpace(sortTrickList(level2List, 2));
        final ArrayList<String>level3Sorted=addWhiteSpace(sortTrickList(level3List, 3));
        final ArrayList<String>level4Sorted=addWhiteSpace(sortTrickList(level4List, 4));

        final String[]ignoredStrings={"Multiples","Power","Manipulation","Releases",DASHES};

        ArrayAdapter<String> basicsAdapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, basicsList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setClickable(false);
                return view;
            }
        };
        ArrayAdapter<String> level1Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level1Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level1Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Material Red
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };

        ArrayAdapter<String> level2Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level2Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level2Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        ArrayAdapter<String> level3Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level3Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level3Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        ArrayAdapter<String> level4Adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, android.R.id.text1, level4Sorted){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = getResources().getColor(R.color.materialRed); // Default
                for(int j=0;j<ignoredStrings.length;j++) {
                    if (level4Sorted.get(position).equals(ignoredStrings[j])) {
                        color = getResources().getColor(R.color.materialRed); // Opaque Blue
                        view.setBackgroundColor(color);
                        view.setClickable(true);
                        view.setVisibility(View.VISIBLE);
                        ((TextView) view).setTextColor(Color.WHITE);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        ((TextView) view).setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    }
                    else{
                        view.setClickable(false);
                        view.setVisibility(View.VISIBLE);
                    }
                }




                return view;
            }
        };
        // Assign basicsAdapter to ListView
        basicsGridView.setAdapter(basicsAdapter);
        level1GridView.setAdapter(level1Adapter);
        level2GridView.setAdapter(level2Adapter);
        level3GridView.setAdapter(level3Adapter);
        level4GridView.setAdapter(level4Adapter);

        // ListView Item Click Listener
        basicsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) basicsGridView.getItemAtPosition(position);
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

        level1GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level1GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level1GridView.getItemAtPosition(position);
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level2GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level2GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level2GridView.getItemAtPosition(position);
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level3GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for(int j=0;j<ignoredStrings.length;j++){
                    if(level3GridView.getItemAtPosition(position).equals(ignoredStrings[j]))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level3GridView.getItemAtPosition(position);
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        level4GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                for (String ignoredString : ignoredStrings) {
                    if (level4GridView.getItemAtPosition(position).equals(ignoredString))
                        return;
                }
                // ListView Clicked item value
                String itemValue = (String) level4GridView.getItemAtPosition(position);
                TrickList.index = getTrickFromName(itemValue, tricktionary).getIndex();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }

    public static ArrayList<String> sortTrickList(ArrayList<String> list, int level){
        ArrayList<String>acc= new ArrayList<>();
        int multiples=0;
        int power=1;
        int manipulation=2;
        int releases=3;
        Collections.sort(list);
        Collections.reverse(list);

        acc.add(0, "Releases");
        acc.add(0, "Manipulation");
        acc.add(0, "Power");
        acc.add(0,"Multiples");
        for(int j=0;j<list.size();j++){

            if(getTrickFromName(list.get(j), tricktionary).getType().equals("Multiples")){
                acc.add(multiples + 1, list.get(j));
                power++;
                manipulation++;
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Power")){
                acc.add(power+1,list.get(j));
                manipulation++;
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Manipulation")){
                acc.add(manipulation+1,list.get(j));
                releases++;
            }
            else if(getTrickFromName(list.get(j), tricktionary).getType().equals("Releases")){
                acc.add(releases+1,list.get(j));
            }

        }



        return acc;
    }
    public ArrayList<String> addWhiteSpace(ArrayList<String> list){
        int index;
        ArrayList<String> acc= new ArrayList<>();
        for(int j=0;j<list.size();j++){
            acc.add(list.get(j));
            if(list.get(j).equals("Multiples")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);


            }
            if(list.get(j).equals("Power")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }
            if(list.get(j).equals("Manipulation")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }
            if(list.get(j).equals("Releases")){
                index=acc.size()-1;
                while(index%3>0){
                    acc.add(index,"");
                    index++;
                }
                acc.add(index,DASHES);
                acc.add(index+2,DASHES);

            }

        }
        return acc;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tricktionary, menu);


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
    public static Trick getTrickFromName(String name, Trick[]arr){

        for (Trick anArr : arr) {
            if (anArr.getName().equals(name)) {
                return anArr;
            }
        }
        return arr[0];
    }


    public void viewCurrentSettings(View v){
        TrickList.alphabet=true;
        TrickList.type="all";
        TrickList.level=-1;
        Intent intent = new Intent(this, TrickList.class);
        startActivity(intent);




    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }

    public void startSearch(View v){
        Intent intent = new Intent(this, SearchTricks.class);
        startActivity(intent);
    }
}

