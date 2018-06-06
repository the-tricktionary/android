package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

import static trictionary.jumproper.com.jumpropetrictionary.activities.BaseActivity.getLocale;

/**
 * Created by jumpr_000 on 8/15/2017.
 */

public class GlobalData extends Application {
    private FirebaseAuth mAuth;
    private ArrayList<ArrayList<Trick>> tricktionary;
    private ArrayList<ArrayList<Trick>> completedTricks;
    private String uId = "";
    private String username;
    private int levels;
    private SharedPreferences settings;
    private int totalTricks;
    private Locale mLocale;
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

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public Comparator<Trick> getCompareName() {
        return new Comparator<Trick>() {
            @Override
            public int compare(Trick trick, Trick t1) {
                return trick.getName().compareTo(t1.getName());
            }
        };
    }
    public Locale getmLocale() {
        return mLocale;
    }

    public void setmLocale(Locale mLocale) {
        this.mLocale = mLocale;
    }

    public String getUsername() {
        Log.e("Getting username",username);
        return username;
    }

    public void setUsername(String username) {
        Log.e("username",username);
        this.username = username;
    }

    public void setCompareName(Comparator<Trick> compareName) {
        this.compareName = compareName;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    @SuppressWarnings("deprecation")
    public void setLocale(){
        final Resources resources = getResources();
        final Configuration configuration = resources.getConfiguration();
        final Locale locale = getLocale(this);
        Log.e("Locale",locale.getDisplayLanguage());
        if (!configuration.locale.equals(locale)) {
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, null);
        }
    }
}
