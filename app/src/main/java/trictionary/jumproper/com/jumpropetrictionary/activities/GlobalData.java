package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

/**
 * Created by jumpr_000 on 8/15/2017.
 */

public class GlobalData extends Application {
    private FirebaseAuth mAuth;
    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private String uId = "";
    private SharedPreferences settings;
    private int totalTricks;
    public Comparator<Trick> compareName;

    @Override
    public void onCreate() {
        super.onCreate();
        this.setmAuth(FirebaseAuth.getInstance());
        if(this.getmAuth().getCurrentUser()!=null) {
            this.setuId(this.getmAuth().getCurrentUser().getUid());
        }
    }

    public ArrayList<ArrayList<Trick>> getTricktionary() {
        return tricktionary;
    }

    public void setTricktionary(ArrayList<ArrayList<Trick>> tricktionary) {
        this.tricktionary = tricktionary;
    }

    public void refreshData(){
        this.setmAuth(FirebaseAuth.getInstance());
        if(this.getmAuth().getCurrentUser()!=null) {
            this.setuId(this.getmAuth().getCurrentUser().getUid());
        }
    }

    public ArrayList<ArrayList<Trick>> getCompletedTricks() {
        return completedTricks;
    }

    public void setCompletedTricks(ArrayList<ArrayList<Trick>> completedTricks) {
        this.completedTricks = completedTricks;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    public void setSettings(SharedPreferences settings) {
        this.settings = settings;
    }

    public int getTotalTricks() {
        return totalTricks;
    }

    public void setTotalTricks(int totalTricks) {
        this.totalTricks = totalTricks;
    }

    public Comparator<Trick> getCompareName() {
        return new Comparator<Trick>() {
            @Override
            public int compare(Trick trick, Trick t1) {
                return trick.getName().compareTo(t1.getName());
            }
        };
    }

    public void setCompareName(Comparator<Trick> compareName) {
        this.compareName = compareName;
    }

    @SuppressWarnings("deprecation")
    public void setLocale(Locale locale){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
            getApplicationContext().createConfigurationContext(configuration);
        }
        else{
            configuration.locale=locale;
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
}
