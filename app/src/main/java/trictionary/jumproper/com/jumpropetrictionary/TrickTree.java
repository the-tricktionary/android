package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class TrickTree extends ActionBarActivity {

    ListView prereqs,nextTricks;
    TextView currentTrick;

    public static Trick viewedTrick;
    public static ArrayList<String>prereqsList;
    public static ArrayList<String>nextTricksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_tree);

        currentTrick=(TextView)findViewById(R.id.current_trick);
        prereqs=(ListView)findViewById(R.id.prereqs);
        nextTricks=(ListView)findViewById(R.id.next_tricks);
        currentTrick.setText(viewedTrick.getName());
        fillLists();



    }
    public void fillLists(){
        viewedTrick=TrickList.getTrickFromName(currentTrick.getText().toString(),TrickData.getTricktionary());
        prereqsList=getPrereqs(viewedTrick);
        nextTricksList=getNextTricks(viewedTrick);
        Collections.sort(prereqsList);
        Collections.sort(nextTricksList);

        ArrayAdapter<String> adapterPrerqs = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, prereqsList);
        prereqs.setAdapter(adapterPrerqs);
        ArrayAdapter<String> adapterNextTricks = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nextTricksList);
        nextTricks.setAdapter(adapterNextTricks);

        prereqs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) prereqs.getItemAtPosition(position);
                currentTrick.setText(itemValue);
                fillLists();
            }

        });
        nextTricks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) nextTricks.getItemAtPosition(position);
                currentTrick.setText(itemValue);
                fillLists();
            }

        });
    }

    public static ArrayList<String> getPrereqs(Trick currentTrick){
        ArrayList<String> prereqs=new ArrayList<>();
        for(int j=0;j<currentTrick.getPrereqs().length;j++){
            prereqs.add(currentTrick.getPrereqs()[j]);
        }
        return prereqs;
    }

    public ArrayList<String> getNextTricks(Trick currentTrick){
        ArrayList<String>nextTricks=new ArrayList<>();

        Trick[] tricktionary=TrickData.getTricktionary();
        for(int j=0;j<tricktionary.length;j++){
            for (int i=0;i<tricktionary[j].getPrereqs().length;i++){
                if (tricktionary[j].getPrereqs()[i].equals(currentTrick.getName())){
                    if (!(tricktionary[j].getPrereqs()[i].equals(currentTrick.getName())))
                        nextTricks.add(tricktionary[j].getName());
                }
            }
        }
        return nextTricks;
    }

    public void viewTrick(View v){
        TrickList.index=viewedTrick.getIndex();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void viewTree(View v){
        TreeVisual.setIndex(viewedTrick.getName());
        Intent intent = new Intent(getApplicationContext(), TreeVisual.class);
        startActivity(intent);
    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }

}
