package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;

public class TreeVisual extends ActionBarActivity {

    public static String currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_visual);
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("8E77428ECEC6F34EF9CE7FD2B3D942C2")
                .addTestDevice("9F2CF3A3F15E84C980EF6902242BE25C")
                .build();
        mAdView.loadAd(adRequest);
        initTrickTree();

    }
    public void initTrickTree(){
        ArrayList<String> trickNames = new ArrayList<String>();
        final String[] stateData = {"State A", "State B", "State C", "State D"};
        for(int j=0;j<TrickData.getTricktionary().length;j++) {
            trickNames.add(TrickData.getTricktionary()[j].getName());
        }
        Collections.sort(trickNames);
        ArrayList<Trick>tricks=new ArrayList<>();
        for(int j=0;j<trickNames.size();j++){
            tricks.add(Tricktionary.getTrickFromName(trickNames.get(j),TrickData.getTricktionary()));
        }
        Collections.reverse(trickNames);
        final ArrayList<String>sortedTricks=sortByLevel(tricks);

        // 0.Init the ExpandableGridView
        final ExpandableGridView trickTreeGridView = (ExpandableGridView)findViewById(R.id.trick_tree);
        ArrayAdapter<String> gridViewAdapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.grid_item, R.id.grid_item, sortedTricks){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0xFFBDBDBD; // Default
                if ((position%3==1)&&(sortedTricks.get(position).length()>0)&&(sortedTricks.get(position-1).equals("__________"))) {
                    color = 0xFF8BC5D2; // Opaque Blue
                    view.setClickable(true);
                    view.setVisibility(View.VISIBLE);
                }
                else if (sortedTricks.get(position).equals("__________")){
                    view.setVisibility(View.INVISIBLE);
                }
                else{
                    view.setClickable(false);
                    view.setVisibility(View.VISIBLE);
                }
                view.setBackgroundColor(color);

                return view;
            }
        };
        // 1.Set adapter for the grid view
        trickTreeGridView.setAdapter(gridViewAdapter);
        // 2.Add click event listener to the grid view, expand grid view when item is clicked
        trickTreeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // expand the grid view

                    trickTreeGridView.expandGridViewAtView(view, new ArrayAdapter<String>(getBaseContext(),
                            R.layout.grid_item, R.id.grid_item, TrickTree.getPrereqs(TrickList.getTrickFromName(trickTreeGridView.getItemAtPosition(position).toString(), TrickData.getTricktionary()))));

            }
        });
        // 3.Click event listener of sub GridView items
        trickTreeGridView.setOnExpandItemClickListener(new ExpandableGridView.OnExpandItemClickListener() {
            @Override
            public void onItemClick(int position, final Object clickPositionData) {
                Toast.makeText(getBaseContext(), clickPositionData.toString(), Toast.LENGTH_LONG).show();

                TrickTree.viewedTrick=Tricktionary.getTrickFromName(clickPositionData.toString(),TrickData.getTricktionary());
                Intent intent = new Intent(getApplicationContext(), TrickTree.class);
                startActivity(intent);




            }
        });
        trickTreeGridView.setSelection(indexOfPrimary(currentIndex,sortedTricks));
    }
    public ArrayList<String> sortByLevel(ArrayList<Trick> list){
        ArrayList<Trick>level1=new ArrayList<>();
        ArrayList<Trick>level2=new ArrayList<>();
        ArrayList<Trick>level3=new ArrayList<>();
        ArrayList<Trick>level4=new ArrayList<>();
        for(int j=0;j<list.size();j++){
            if (list.get(j).getDifficulty()==4){
                level4.add(list.get(j));
            }
            if (list.get(j).getDifficulty()==3){
                level3.add(list.get(j));
            }
            if (list.get(j).getDifficulty()==2){
                level2.add(list.get(j));
            }
            if (list.get(j).getDifficulty()==1){
                level1.add(list.get(j));
            }
        }
        ArrayList<String>tricks=new ArrayList<>();
        addTricksAndPrereqs(tricks,level4);
        addTricksAndPrereqs(tricks,level3);
        addTricksAndPrereqs(tricks,level2);
        addTricksAndPrereqs(tricks,level1);
        return tricks;
    }
    public void addTricksAndPrereqs(ArrayList<String> list,ArrayList<Trick>level){
        int pos=0;
        for(int j=0;j<level.size();j++){
            if (level.get(j).getPrereqs().length>0) {
                list.add("__________");
                list.add(level.get(j).getName());
                list.add("__________");

                for (int i = 0; i < level.get(j).getPrereqs().length; i++) {
                    list.add(level.get(j).getPrereqs()[i]);

                }
                pos = (list.size() - 1) % 3;
                System.out.println(pos);
                if (pos == 0) {
                    list.add("");
                    list.add("");
                    list.add("");
                    list.add("");
                    list.add("");
                }
                if (pos == 1) {
                    list.add("");
                    list.add("");
                    list.add("");
                    list.add("");
                }
                if (pos == 2) {
                    list.add("");
                    list.add("");
                    list.add("");
                }
            }
        }


    }
    public static void setIndex(String index){
        currentIndex=index;
    }
    public int indexOfPrimary(String needle,ArrayList<String>haystack){
        int index=0;
        for(int j=0;j<haystack.size();j++){
            if ((j%3==1)&&(haystack.get(j-1).equals("__________"))&&(haystack.get(j).equals(needle))){
                return j;
            }
        }
        return 0;
    }

    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }


}
