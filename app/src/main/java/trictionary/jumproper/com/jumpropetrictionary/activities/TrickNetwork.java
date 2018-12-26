package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import com.otaliastudios.zoom.ZoomLayout;

import java.util.ArrayList;
import java.util.List;

import jp.kai.forcelayout.Forcelayout;
import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

public class TrickNetwork extends AppCompatActivity {

    private ArrayList<ArrayList<Trick>> tricktionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_trick_network2);

        tricktionary = ((GlobalData) this.getApplication()).getTricktionary();
        int id0 = 0;
        ArrayList<Pair<String, Integer>> nodes = new ArrayList<>();
        List<String> links= new ArrayList<>();
        for (ArrayList<Trick> level:tricktionary){
            if (id0 <= 1) {
                for (Trick trick : level) {
                    Log.e("Trick Network", trick.name + ", " + trick.id0);
                    switch (trick.type.toLowerCase()) {
                        case ("basics"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.basics_node));
                            break;
                        case ("manipulation"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.manipulation_node));
                            break;
                        case ("power"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.power_node));
                            break;
                        case ("multiples"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.multiples_node));
                            break;
                        case ("releases"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.releases_node));
                            break;
                        case ("impossible"):
                            nodes.add(new Pair<String, Integer>(trick.name, R.drawable.impossible_node));
                            break;
                        default:
                            break;
                    }
                    //add prereq links
                    for (String prereq : trick.getPrereqs()) {
                        links.add(trick.getName() + "-" + prereq);
                    }

                }
            }
            id0++;
        }

        Forcelayout forcelayout = new Forcelayout(getApplicationContext());
        forcelayout.link()
                    .style(Color.argb(60,50,30,200), 2.0f);

        forcelayout.with()
                .size(50)
                .distance(750)
                .gravity(0.1)
                .friction(0.04)
                .nodes(nodes)
                .links(links)
                .start();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(forcelayout);
        setContentView(layout);
    }
}
