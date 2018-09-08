package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import trictionary.jumproper.com.jumpropetrictionary.R;

public class SettingsActivity extends BaseActivity {

    public static final String PREFS_NAME="Settings";
    public static final String AUTO_PLAY_SETTING="AutoPlay";
    public static final String PLAYER_STYLE_SETTING="PlayerStyle";
    public static final String LANGUAGE_SETTING="Language";
    public static final String PUBLIC_PROFILE_SETTING="Profile";
    public static final String PUBLIC_SPEED_SETTING="Speed";
    public static final String PUBLIC_TRICK_SETTING="Checklist";
    public static boolean autoPlay=true;
    public static String stylePref="Minimal";
    public static String language;
    public static boolean publicProfile;
    public static boolean publicSpeed;
    public static boolean publicTricks;
    private CheckBox autoPlayCheck,publicProfileCheck,publicSpeedCheck,publicTricksCheck, adsCheck;
    private Spinner playerStyleSpinner;
    private Spinner languageSpinner;
    private FirebaseAuth mAuth;
    private boolean languageChanged=false;
    private static SharedPreferences settings;
    private String[] langs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();

        settings = ((GlobalData) this.getApplication()).getSettings();
        langs = getResources().getStringArray(R.array.language_abbreviations);
        settings=getSharedPreferences(PREFS_NAME,0);
        autoPlay=settings.getBoolean(AUTO_PLAY_SETTING,true);
        stylePref=settings.getString(PLAYER_STYLE_SETTING,getString(R.string.youtube_style_minimal));
        language=settings.getString(LANGUAGE_SETTING,"English");
        publicProfile=settings.getBoolean(PUBLIC_PROFILE_SETTING,false);
        publicSpeed=settings.getBoolean(PUBLIC_SPEED_SETTING,false);
        publicTricks=settings.getBoolean(PUBLIC_TRICK_SETTING,false);
        autoPlayCheck=(CheckBox)findViewById(R.id.auto_play);
        autoPlayCheck.setChecked(autoPlay);
        publicProfileCheck=(CheckBox)findViewById(R.id.public_profile_checkbox);
        publicSpeedCheck=(CheckBox)findViewById(R.id.public_speed);
        publicSpeedCheck.setChecked(publicSpeed);
        publicTricksCheck=(CheckBox)findViewById(R.id.public_tricks);
        publicSpeedCheck.setChecked(publicTricks);
        adsCheck = (CheckBox)findViewById(R.id.ads_checkbox);
        boolean ads = true;
        try {
            ads = ((GlobalData) this.getApplication()).getAds();
        } catch (Exception e){
            ads = true;
        }
        adsCheck.setChecked(ads);
        playerStyleSpinner=(Spinner)findViewById(R.id.video_player_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_styles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerStyleSpinner.setAdapter(adapter);
        playerStyleSpinner.setSelection(adapter.getPosition(stylePref));

        MobileAds.initialize(this, "ca-app-pub-2959515976305980~3811712667");
        AdView adView = findViewById(R.id.settings_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("2CC2625EB00F3EB58B6E5BC0B53C5A1D")
                .build();
        if (ads) {
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.e("Ad", "Loaded");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("Ad", "Could not load error: " + errorCode);
                }
            });
        }


        if(mAuth.getCurrentUser()!=null) {
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fb.getReference("users").child(mAuth.getCurrentUser().getUid()).child("profile");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("public")) {
                        if ((Boolean) dataSnapshot.child("public").getValue()) {
                            publicProfileCheck.setChecked(true);
                            publicProfile = true;
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(PUBLIC_PROFILE_SETTING, publicProfile);
                            editor.commit();
                            if (dataSnapshot.hasChild("speed")) {
                                if ((Boolean) dataSnapshot.child("speed").getValue()) {
                                    publicSpeedCheck.setChecked(true);
                                    publicSpeed = true;
                                    editor.putBoolean(PUBLIC_SPEED_SETTING, publicSpeed);
                                    editor.commit();
                                }
                            }
                            if (dataSnapshot.hasChild("checklist")) {
                                if ((Boolean) dataSnapshot.child("checklist").getValue()) {
                                    publicTricksCheck.setChecked(true);
                                    publicTricks = true;
                                    editor.putBoolean(PUBLIC_TRICK_SETTING, publicTricks);
                                    editor.commit();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        playerStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        stylePref = adapterView.getItemAtPosition(i).toString();

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PLAYER_STYLE_SETTING, stylePref);
                        editor.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
        });
        languageSpinner=(Spinner)findViewById(R.id.language_settings_spinner);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(languageAdapter.getPosition(language));
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SettingsActivity.setLanguage(adapterView.getItemAtPosition(i).toString());
                ((GlobalData) getApplication()).setLocale();
                if(languageChanged) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    languageChanged=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        adsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((GlobalData) getApplication()).setAds(b);
            }
        });
        publicProfileCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mAuth.getCurrentUser()==null){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                    mBuilder.setTitle(R.string.public_profile_title);
                    mBuilder.setMessage(R.string.profile_sign_in);
                    mBuilder.setPositiveButton(getString(R.string.sign_in), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(getApplicationContext(),SignIn.class);
                            startActivity(intent);
                        }
                    });
                    mBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    mBuilder.show();
                    return;
                }
                if(b){
                    publicSpeedCheck.setVisibility(View.VISIBLE);
                    publicTricksCheck.setVisibility(View.VISIBLE);
                }
                else{
                    publicSpeedCheck.setVisibility(View.INVISIBLE);
                    publicTricksCheck.setVisibility(View.INVISIBLE);
                }
                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                final DatabaseReference myRef=fb.getReference("users")
                        .child(mAuth.getCurrentUser().getUid())
                        .child("profile")
                        .child("public");
                myRef.setValue(b);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(PUBLIC_PROFILE_SETTING,b);
                publicProfile=b;
                editor.commit();
            }
        });
        publicSpeedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                final DatabaseReference myRef=fb.getReference("users")
                        .child(mAuth.getCurrentUser().getUid())
                        .child("profile")
                        .child("speed");
                myRef.setValue(b);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(PUBLIC_SPEED_SETTING,b);
                publicSpeed=b;
                editor.commit();
            }
        });
        publicTricksCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                final DatabaseReference myRef=fb.getReference("users")
                        .child(mAuth.getCurrentUser().getUid())
                        .child("profile")
                        .child("checklist");
                myRef.setValue(b);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(PUBLIC_TRICK_SETTING,b);
                publicTricks=b;
                editor.commit();
            }
        });

    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }
    public void changeAutoPlay(View v){
        if (autoPlayCheck.isChecked()){
            autoPlayCheck.setChecked(true);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(AUTO_PLAY_SETTING,true);
            autoPlay=true;
            editor.commit();
            return;
        }
        else{
            autoPlayCheck.setChecked(false);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(AUTO_PLAY_SETTING,false);
            autoPlay=false;
            editor.commit();
        }
    }
    public static boolean autoPlayEnabled(){
        return autoPlay;
    }
    public static void setLanguage(String mLanguage){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LANGUAGE_SETTING,mLanguage);
        editor.commit();
        language=mLanguage;
    }
    public static String getPlayerStyle(){
        return stylePref;
    }
    public void mainMenu(View v){
        finish();
    }
}
