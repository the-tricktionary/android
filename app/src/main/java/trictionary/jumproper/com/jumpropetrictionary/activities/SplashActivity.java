package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

/**
 * Created by jumpr on 8/15/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private String uId="";
    private Trick mTrick;
    private int totalTricks;
    private FirebaseAuth mAuth;
    private static boolean offline=true;
    private SharedPreferences settings;
    public static Comparator<Trick> compareName=new Comparator<Trick>() {
        @Override
        public int compare(Trick trick, Trick t1) {
            return trick.getName().compareTo(t1.getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Fade fade = new Fade();
            fade.setDuration(100);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
            getWindow().setExitTransition(fade);
        }
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        settings=getSharedPreferences(SettingsActivity.PREFS_NAME,0);
        tricktionary = getTricktionaryData();
        int totalTricks = getTotalTricks();
        if(mAuth.getCurrentUser()!=null) {
            uId = mAuth.getCurrentUser().getUid();
        }
        ((GlobalData) this.getApplication()).setTotalTricks(totalTricks);
        ((GlobalData) this.getApplication()).setSettings(settings);
    }
    public ArrayList<ArrayList<Trick>> getTricktionaryData(){
        tricktionary =new ArrayList<>();
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        if(offline) {
            fb.setPersistenceEnabled(true);
            offline=false;
        }
        DatabaseReference myRef=fb.getReference("tricks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int loaded = 0;
                for(int j=0;j<4;j++){
                    tricktionary.add(new ArrayList<Trick>());
                }
                int index=0;
                for(DataSnapshot level:dataSnapshot.getChildren()){
                    for(DataSnapshot trick:level.child("subs").getChildren()){
                        if(settings.getString(SettingsActivity.LANGUAGE_SETTING,null)!=null) {
                            SettingsActivity.language=settings.getString(SettingsActivity.LANGUAGE_SETTING,null);
                            if (SettingsActivity.language.equals("Deutsch") && trick.child("i18n").child("de").getValue() != null) {
                                mTrick = new Trick(trick.child("i18n").child("de").child("name").getValue().toString(),
                                        trick.child("i18n").child("de").child("description").getValue().toString(),
                                        Integer.parseInt(level.child("level").getValue().toString()),
                                        index,
                                        trick.child("type").getValue().toString(),
                                        trick.child("video").getValue().toString(),
                                        trick.child("irsf").getValue().toString(),
                                        trick.child("wjr").getValue().toString(),
                                        Integer.parseInt(trick.child("id1").getValue().toString()));
                                mTrick.setPrereqIds(trick);
                                tricktionary.get(mTrick.getId0()).add(mTrick);
                                index++;
                            } else if (SettingsActivity.language.equals("Svenska") &&
                                    trick.child("i18n").child("sv").child("description").getValue() != null &&
                                    trick.child("i18n").child("sv").child("name").getValue() != null) {
                                mTrick = new Trick(trick.child("i18n").child("sv").child("name").getValue().toString(),
                                        trick.child("i18n").child("sv").child("description").getValue().toString(),
                                        Integer.parseInt(level.child("level").getValue().toString()),
                                        index,
                                        trick.child("type").getValue().toString(),
                                        trick.child("video").getValue().toString(),
                                        trick.child("irsf").getValue().toString(),
                                        trick.child("wjr").getValue().toString(),
                                        Integer.parseInt(trick.child("id1").getValue().toString()));
                                mTrick.setPrereqIds(trick);
                                tricktionary.get(mTrick.getId0()).add(mTrick);
                                index++;
                            } else {
                                mTrick = new Trick(trick.child("name").getValue().toString(),
                                        trick.child("description").getValue().toString(),
                                        Integer.parseInt(level.child("level").getValue().toString()),
                                        index,
                                        trick.child("type").getValue().toString(),
                                        trick.child("video").getValue().toString(),
                                        trick.child("irsf").getValue().toString(),
                                        trick.child("wjr").getValue().toString(),
                                        Integer.parseInt(trick.child("id1").getValue().toString()));
                                mTrick.setPrereqIds(trick);
                                tricktionary.get(mTrick.getId0()).add(mTrick);
                                index++;
                                Log.e("Tricks",mTrick.getName());
                            }
                        }
                        else {
                            mTrick = new Trick(trick.child("name").getValue().toString(),
                                    trick.child("description").getValue().toString(),
                                    Integer.parseInt(level.child("level").getValue().toString()),
                                    index,
                                    trick.child("type").getValue().toString(),
                                    trick.child("video").getValue().toString(),
                                    trick.child("irsf").getValue().toString(),
                                    trick.child("wjr").getValue().toString(),
                                    Integer.parseInt(trick.child("id1").getValue().toString()));
                            mTrick.setPrereqIds(trick);
                            tricktionary.get(mTrick.getId0()).add(mTrick);
                            index++;
                            Log.e("Tricks",mTrick.getName());
                        }
                        loaded++;
                    }
                }
                completedTricks=new ArrayList<>();
                fillCompletedTricks();
                ((GlobalData) SplashActivity.this.getApplication()).setTricktionary(tricktionary);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("checklist",databaseError.getMessage().toString()+ " : "+databaseError.getDetails());
            }
        });
        return tricktionary;
    }
    public void fillCompletedTricks(){
        Log.e("checklist", uId);
        if(uId.length()>0) {
            for(int j=0;j<4;j++){
                completedTricks.add(new ArrayList<Trick>());
            }
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference checklist=fb.getReference("checklist").child(uId);
            checklist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot id0:dataSnapshot.getChildren()){
                        for(DataSnapshot id1:id0.getChildren()){
                            tricktionary.get(Integer.parseInt(id0.getKey()))
                                    .get(Integer.parseInt(id1.getKey()))
                                    .setCompleted(true);
                            completedTricks.get(Integer.parseInt(id0.getKey()))
                                    .add(tricktionary.get(Integer.parseInt(id0.getKey()))
                                            .get(Integer.parseInt(id1.getKey())));
                        }
                    }
                    ((GlobalData) SplashActivity.this.getApplication()).setCompletedTricks(completedTricks);
                    Intent intent = new Intent(SplashActivity.this, Tricktionary.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("checklist", databaseError.getMessage().toString() + " : " + databaseError.getDetails());
                }
            });
        }
        else{
            Log.e("Nooo","no uid");
            Log.e("Nooo",tricktionary.get(0).toString());
            Intent intent = new Intent(this, Tricktionary.class);
            startActivity(intent);
            finish();
        }
    }
    public int getTotalTricks(){
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference checklist=fb.getReference("stats").child("tricks").child("total");
        checklist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Totals",dataSnapshot.getKey().toString()+"  "+dataSnapshot.getValue().toString());
                totalTricks = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
        return totalTricks;
    }

}
