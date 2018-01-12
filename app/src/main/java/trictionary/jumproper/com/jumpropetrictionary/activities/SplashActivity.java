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
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

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
    private long levels;
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
        myRef.keepSynced(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                levels = dataSnapshot.getChildrenCount();
                for(int j=0;j<levels;j++){
                    tricktionary.add(new ArrayList<Trick>());
                }
                int index=0;
                String name;
                String description;
                int id0,id1;
                String type;
                String video;
                String wjrLevel;
                String fisacLevel;
                for(DataSnapshot level:dataSnapshot.getChildren()){
                    for(DataSnapshot trick:level.child("subs").getChildren()){
                        if(settings.getString(SettingsActivity.LANGUAGE_SETTING,null)!=null) {
                            SettingsActivity.language=settings.getString(SettingsActivity.LANGUAGE_SETTING,null);
                            if (SettingsActivity.language.equals("Deutsch") && trick.child("i18n").child("de").getValue() != null) {
                                try {
                                    name = trick.child("i18n").child("de").child("name").getValue().toString();
                                    description = trick.child("i18n").child("de").child("description").getValue().toString();
                                    id1 = Integer.parseInt(trick.child("id1").getValue().toString());
                                    id0 = Integer.parseInt(level.child("level").getValue().toString());
                                    type = setType(trick.child("type").getValue().toString());
                                    video = trick.child("video").getValue().toString();
                                    fisacLevel = trick.child("levels").child("irsf").child("level").getValue().toString();
                                    wjrLevel = trick.child("levels").child("wjr").child("level").getValue().toString();
                                    mTrick = new Trick(name,
                                            description,
                                            id0,
                                            index,
                                            type,
                                            video,
                                            fisacLevel,
                                            wjrLevel,
                                            id1);
                                    mTrick.setPrereqIds(trick);
                                    tricktionary.get(mTrick.getId0()).add(mTrick);
                                    index++;
                                }
                                catch(Exception e){
                                    FirebaseCrash.log("Error loading DE trick" + e.getMessage());
                                }
                            } else if (SettingsActivity.language.equals("Svenska") &&
                                    trick.child("i18n").child("sv").child("description").getValue() != null &&
                                    trick.child("i18n").child("sv").child("name").getValue() != null) {
                                try {
                                    name = trick.child("i18n").child("sv").child("name").getValue().toString();
                                    description = trick.child("i18n").child("sv").child("description").getValue().toString();
                                    id1 = Integer.parseInt(trick.child("id1").getValue().toString());
                                    id0 = Integer.parseInt(level.child("level").getValue().toString());
                                    type = setType(trick.child("type").getValue().toString());
                                    video = trick.child("video").getValue().toString();
                                    fisacLevel = trick.child("levels").child("irsf").child("level").getValue().toString();
                                    wjrLevel = trick.child("levels").child("wjr").child("level").getValue().toString();
                                    mTrick = new Trick(name,
                                            description,
                                            id0,
                                            index,
                                            type,
                                            video,
                                            fisacLevel,
                                            wjrLevel,
                                            id1);
                                    mTrick.setPrereqIds(trick);
                                    tricktionary.get(mTrick.getId0()).add(mTrick);
                                    index++;
                                } catch (Exception e) {
                                    FirebaseCrash.log("Error loading SV trick" + e.getMessage());
                                }
                            }
                            else if (SettingsActivity.language.equals("русский") || (SettingsActivity.language.equals("Russian")) &&
                                    trick.child("i18n").child("ru").child("description").getValue() != null &&
                                    trick.child("i18n").child("ru").child("name").getValue() != null) {
                                try {
                                    name = trick.child("i18n").child("ru").child("name").getValue().toString();
                                    description = trick.child("i18n").child("ru").child("description").getValue().toString();
                                    id1 = Integer.parseInt(trick.child("id1").getValue().toString());
                                    id0 = Integer.parseInt(level.child("level").getValue().toString());
                                    type = setType(trick.child("type").getValue().toString());
                                    video = trick.child("video").getValue().toString();
                                    fisacLevel = trick.child("levels").child("irsf").child("level").getValue().toString();
                                    wjrLevel = trick.child("levels").child("wjr").child("level").getValue().toString();
                                    mTrick = new Trick(name,
                                            description,
                                            id0,
                                            index,
                                            type,
                                            video,
                                            fisacLevel,
                                            wjrLevel,
                                            id1);
                                    mTrick.setPrereqIds(trick);
                                    tricktionary.get(mTrick.getId0()).add(mTrick);
                                    index++;
                                }
                                catch(Exception e){
                                    FirebaseCrash.log("Error loading RU trick" + e.getMessage());
                                }
                            }
                            else {
                                try {
                                    name = trick.child("name").getValue().toString();
                                    description = trick.child("description").getValue().toString();
                                    id1 = Integer.parseInt(trick.child("id1").getValue().toString());
                                    id0 = Integer.parseInt(level.child("level").getValue().toString());
                                    type = setType(trick.child("type").getValue().toString());
                                    video = trick.child("video").getValue().toString();
                                    fisacLevel = trick.child("levels").child("irsf").child("level").getValue().toString();
                                    wjrLevel = trick.child("levels").child("wjr").child("level").getValue().toString();
                                    mTrick = new Trick(name,
                                            description,
                                            id0,
                                            index,
                                            type,
                                            video,
                                            fisacLevel,
                                            wjrLevel,
                                            id1);
                                    mTrick.setPrereqIds(trick);
                                    tricktionary.get(mTrick.getId0()).add(mTrick);
                                    index++;
                                }
                                catch(Exception e){
                                    FirebaseCrash.log("Error loading EN trick" + e.getMessage());
                                }
                            }
                        }
                        else {
                            try {
                                name = trick.child("name").getValue().toString();
                                description = trick.child("description").getValue().toString();
                                id1 = Integer.parseInt(trick.child("id1").getValue().toString());
                                id0 = Integer.parseInt(level.child("level").getValue().toString());
                                type = setType(trick.child("type").getValue().toString());
                                video = trick.child("video").getValue().toString();
                                fisacLevel = trick.child("levels").child("irsf").child("level").getValue().toString();
                                wjrLevel = trick.child("levels").child("wjr").child("level").getValue().toString();
                                mTrick = new Trick(name,
                                        description,
                                        id0,
                                        index,
                                        type,
                                        video,
                                        fisacLevel,
                                        wjrLevel,
                                        id1);
                                mTrick.setPrereqIds(trick);
                                tricktionary.get(mTrick.getId0()).add(mTrick);
                                index++;
                            }
                            catch(Exception e){
                                FirebaseCrash.log("Error loading EN trick" + e.getMessage());
                                Log.e("Trick error", e.getMessage());
                            }
                        }
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
        if(uId.length()>0 && tricktionary.size()>0) {
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference checklist=fb.getReference("checklist").child(uId);
            for(int j=0;j<levels;j++){
                completedTricks.add(new ArrayList<Trick>());
            }
            checklist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot id0:dataSnapshot.getChildren()){
                        for(DataSnapshot id1:id0.getChildren()){
                            if(Integer.parseInt(id0.getKey())<0){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id0.getKey())>=  tricktionary.size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else if(Integer.parseInt(id1.getKey())>=  tricktionary.get(Integer.parseInt(id0.getKey())).size()){
                                Log.e("Checklist id0 :(",id0.getKey()+":"+id0.getValue());
                                Log.e("Checklist id1 :(",id1.getKey()+":"+id1.getValue());
                            }
                            else {
                                tricktionary.get(Integer.parseInt(id0.getKey()))
                                        .get(Integer.parseInt(id1.getKey()))
                                        .setCompleted(true);
                                completedTricks.get(Integer.parseInt(id0.getKey()))
                                        .add(tricktionary.get(Integer.parseInt(id0.getKey()))
                                                .get(Integer.parseInt(id1.getKey())));
                            }
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
            Intent intent = new Intent(this, Tricktionary.class);
            startActivity(intent);
            finish();
        }
    }
    public int getTotalTricks(){
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference checklist=fb.getReference("stats").child("tricks").child("total");
        checklist.addListenerForSingleValueEvent(new ValueEventListener() {
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
    public String setType(String enType){
        switch(enType){
            case("Basics"):
                return getString(R.string.basics);
            case("Multiples"):
                return getString(R.string.multiples);
            case("Power"):
                return getString(R.string.power);
            case("Releases"):
                return getString(R.string.releases);
            case("Impossible"):
                return getString(R.string.impossible);
        }
        return enType;
    }

}
