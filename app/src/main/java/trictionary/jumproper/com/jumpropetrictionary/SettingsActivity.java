package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class SettingsActivity extends AppCompatActivity {

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
        setContentView(R.layout.settings_toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");
        PrimaryDrawerItem rafikiItem=new PrimaryDrawerItem().withName("Rafiki Program");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background)

                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Jump Rope Tricktionary")
                                .withIcon(getResources().getDrawable(R.drawable.icon_alpha))
                )
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(true)
                .build();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        mainMenuItem,
                        new DividerDrawerItem(),
                        tricktionaryItem,
                        new DividerDrawerItem(),
                        speedItem,
                        new DividerDrawerItem(),
                        randomTrickItem,
                        new DividerDrawerItem(),
                        showWriterItem,
                        new DividerDrawerItem(),
                        settingsItem,
                        new DividerDrawerItem(),
                        rafikiItem
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position==0){
                            return true;
                        }
                        if(position==1){
                            Intent intent = new Intent(SettingsActivity.this, MainMenu.class);
                            startActivity(intent);
                        }
                        else if(position==3) {
                            Intent intent = new Intent(SettingsActivity.this, Tricktionary.class);
                            startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(SettingsActivity.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(SettingsActivity.this, Names.class);
                            startActivity(intent);
                        }
                        else if(position==11){
                            return true;
                        }
                        else if(position==13){
                            Intent intent = new Intent(SettingsActivity.this, Rafiki.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();


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
        finish();
    }
}
