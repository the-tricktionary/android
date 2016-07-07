package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

public class SettingsActivity extends ActionBarActivity {

    public static final String PREFS_NAME="Settings";
    public static final String AUTO_PLAY_SETTING="AutoPlay";
    public static final String PLAYER_STYLE_SETTING="PlayerStyle";
    public static boolean autoPlay=true;
    public static String stylePref="Minimal";
    CheckBox autoPlayCheck;
    TextView playerStyle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        MainMenu.settings=getSharedPreferences(PREFS_NAME,0);
        autoPlay=MainMenu.settings.getBoolean(AUTO_PLAY_SETTING,true);
        stylePref=MainMenu.settings.getString(PLAYER_STYLE_SETTING,"Minimal");
        autoPlayCheck=(CheckBox)findViewById(R.id.auto_play);
        autoPlayCheck.setChecked(MainMenu.settings.getBoolean(AUTO_PLAY_SETTING,true));
        playerStyle=(TextView)findViewById(R.id.style);
        playerStyle.setText(stylePref);
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
    public void changePlayerStyle(View v){
        PopupMenu popupMenu = new PopupMenu(SettingsActivity.this, v);
        //Inflating the Popup using xml file


        popupMenu.getMenu().add("Minimal");
        popupMenu.getMenu().add("Chromeless");
        popupMenu.getMenu().add("Default");

        popupMenu.show();
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                playerStyle.setText(item.getTitle().toString());
                stylePref=item.getTitle().toString();
                SharedPreferences.Editor editor = MainMenu.settings.edit();
                editor.putString(PLAYER_STYLE_SETTING,item.getTitle().toString());
                editor.commit();
                return true;
            }
        });
    }
    public static String getPlayerStyle(){
        return stylePref;
    }
    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }
}
