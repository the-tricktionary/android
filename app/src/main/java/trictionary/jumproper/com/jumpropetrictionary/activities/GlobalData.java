package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;
import trictionary.jumproper.com.jumpropetrictionary.utils.TrickData;

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
    private TrickData data;

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
        data = new TrickData();
        data.setuId(this.getuId());
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

    public TrickData getData() {
        return data;
    }

    public void setData(TrickData data) {
        this.data = data;
    }
}
