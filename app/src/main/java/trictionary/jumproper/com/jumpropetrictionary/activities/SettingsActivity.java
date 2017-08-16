package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import trictionary.jumproper.com.jumpropetrictionary.R;

public class SettingsActivity extends BaseActivity {

    public static final String PREFS_NAME="Settings";
    public static final String AUTO_PLAY_SETTING="AutoPlay";
    public static final String PLAYER_STYLE_SETTING="PlayerStyle";
    public static final String LANGUAGE_SETTING="Language";
    public static boolean autoPlay=true;
    public static String stylePref="Minimal";
    public static String language;
    private CheckBox autoPlayCheck;
    private Spinner playerStyleSpinner;
    private Spinner languageSpinner;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();

        MainMenu.settings=getSharedPreferences(PREFS_NAME,0);
        autoPlay=MainMenu.settings.getBoolean(AUTO_PLAY_SETTING,true);
        stylePref=MainMenu.settings.getString(PLAYER_STYLE_SETTING,"Minimal");
        language=MainMenu.settings.getString(LANGUAGE_SETTING,"English");
        Log.e("Language",language);
        autoPlayCheck=(CheckBox)findViewById(R.id.auto_play);
        autoPlayCheck.setChecked(MainMenu.settings.getBoolean(AUTO_PLAY_SETTING,true));
        playerStyleSpinner=(Spinner)findViewById(R.id.video_player_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_styles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerStyleSpinner.setAdapter(adapter);
        playerStyleSpinner.setSelection(adapter.getPosition(stylePref));
        playerStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stylePref=adapterView.getItemAtPosition(i).toString();
                SharedPreferences.Editor editor = MainMenu.settings.edit();
                editor.putString(PLAYER_STYLE_SETTING,stylePref);
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }
    public void changeAutoPlay(View v){
        if (autoPlayCheck.isChecked()){
            autoPlayCheck.setChecked(true);
            SharedPreferences.Editor editor = MainMenu.settings.edit();
            editor.putBoolean(AUTO_PLAY_SETTING,true);
            autoPlay=true;
            editor.commit();
            return;
        }
        else{
            autoPlayCheck.setChecked(false);
            SharedPreferences.Editor editor = MainMenu.settings.edit();
            editor.putBoolean(AUTO_PLAY_SETTING,false);
            autoPlay=false;
            editor.commit();
        }
    }
    public static boolean autoPlayEnabled(){
        return autoPlay;
    }
    public static void setLanguage(String mLanguage){
        SharedPreferences.Editor editor = MainMenu.settings.edit();
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
