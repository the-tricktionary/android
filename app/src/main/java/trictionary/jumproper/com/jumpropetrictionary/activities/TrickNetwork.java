package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jp.kai.forcelayout.Forcelayout;
import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

public class TrickNetwork extends BaseActivity {

    private ArrayList<ArrayList<Trick>> tricktionary;
    public static Trick rootTrick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_network);

        tricktionary = ((GlobalData) this.getApplication()).getTricktionary();
        ArrayList<Pair<String, Integer>> nodes = new ArrayList<>();
        List<String> links= new ArrayList<>();

        graphSingleTrick(nodes, links, rootTrick.getId0(),  rootTrick.getId1());

        Forcelayout forcelayout = new Forcelayout(getApplicationContext());
        forcelayout.link()
                    .style(Color.argb(60,50,30,200), 2.0f);

        forcelayout.with()
                .size(50)
                .distance(750)
                .gravity(0.02)
                .friction(0.02)
                .nodes(nodes)
                .links(links)
                .start();

        FrameLayout layout = (FrameLayout)findViewById(R.id.force_layout_container);
        layout.addView(forcelayout);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }

    private void graphSingleTrick(ArrayList<Pair<String, Integer>> nodes, List<String> links, int id0, int id1){
        Trick root = tricktionary.get(id0).get(id1);
        addTrick(root, nodes);
        if (root.getPrereqsId0().length > 0 && root.getPrereqsId1().length > 0) {
            for (int i = 0; i < root.getPrereqsId0().length; i++) {
                Trick prereq = tricktionary.get(root.getPrereqsId0()[i]).get(root.getPrereqsId1()[i]);
                links.add(root.getName() + "-" + prereq.getName());
                graphSingleTrick(nodes, links, prereq.getId0(), prereq.getId1());
            }
        }
    }

    private void addTrick(Trick trick, ArrayList<Pair<String, Integer>> nodes){
        int typeRes = 0;
        switch (trick.type.toLowerCase()) {
            case ("basics"):
                typeRes = R.drawable.basics_node;
                break;
            case ("manipulation"):
                typeRes = R.drawable.manipulation_node;
                break;
            case ("power"):
                typeRes = R.drawable.power_node;
                break;
            case ("multiples"):
                typeRes = R.drawable.multiples_node;
                break;
            case ("releases"):
                typeRes = R.drawable.releases_node;
                break;
            case ("impossible"):
                typeRes = R.drawable.impossible_node;
                break;
            default:
                break;
        }
        Pair<String, Integer> nodePair = new Pair<>(trick.name, typeRes);
        if (!nodes.contains(nodePair)) {
            nodes.add(nodePair);
        }
    }

    private void graphAllTricksInLevel(ArrayList<Pair<String, Integer>> nodes, List<String> links, int maxLevel){
        int id0 = 0;
        for (ArrayList<Trick> level:tricktionary){
            if (id0 <= maxLevel) {
                for (Trick trick : level) {
                    Log.e("Trick Network", trick.name + ", " + trick.id0);
                    addTrick(trick, nodes);
                    //add prereq links
                    for (String prereq : trick.getPrereqs()) {
                        links.add(trick.getName() + "-" + prereq);
                    }

                }
            }
            id0++;
        }

    }
}
