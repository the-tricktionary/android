package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.show.Names;

public class BaseActivity extends AppCompatActivity {
    private DrawerLayout fullView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private ImageView avatar;
    private TextView profileName,profileDescription;
    private Locale mCurrentLocale;
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        mAuth = ((GlobalData) this.getApplication()).getmAuth();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = getLocale(this);
        ((GlobalData) getApplication()).setLocale();
        if (!locale.equals(mCurrentLocale)) {

            mCurrentLocale = locale;
            recreate();
        }
    }
    public static Locale getLocale(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsActivity.PREFS_NAME,0);
        Locale locale;
        String lang = sharedPreferences.getString(SettingsActivity.LANGUAGE_SETTING, "English");
        Log.e("Locale","sharedPrefs: "+lang);
        switch (lang) {
            case "Dansk":
                lang = "da";
                break;
            case "Deutsch":
                lang = "de";
                break;
            case "Español":
                lang = "es";
                break;
            case "русский":
                lang = "ru";
                break;
            case "Svenska":
                lang = "sv";
                break;
        }
        locale = new Locale(lang);
        return locale;
    }
    private void selectItemFromDrawer(int position) {
        mDrawerList.setItemChecked(position, true);
        Log.e("Classes",mNavItems.get(position).mClass.getCanonicalName()+" : "+getClass().getCanonicalName());
        if(!mNavItems.get(position).mClass.getCanonicalName().equals(getClass().getCanonicalName())) {
            if(mNavItems.get(position).mTitle.equals(getString(R.string.title_activity_contact)) && mAuth.getCurrentUser()==null){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle(getString(R.string.title_activity_profile));
                mBuilder.setMessage(getString(R.string.contact_sign_in_prompt));
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
            }
            else {
                Intent intent = new Intent(this, mNavItems.get(position).mClass);
                startActivity(intent);
            }
        }
        else if(mNavItems.get(position).mTitle.equals(getString(R.string.instagram))){
            Uri uri = Uri.parse("https://www.instagram.com/p/BSrBPgEDoqT/");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                this.startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                this.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.instagram.com/jumpropetricktionary/")));
            }
        }
        else if(mNavItems.get(position).mTitle.equals(getString(R.string.web))){
            String url = "https://the-tricktionary.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if(mNavItems.get(position).mTitle.equals(getString(R.string.sign_out))){
            mAuth.signOut();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
    @Override
    public void setContentView(int layoutResID) {
        //super.setContentView(layoutResID);
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        avatar = (ImageView) mDrawerLayout.findViewById(R.id.avatar);
        profileName = (TextView) mDrawerLayout.findViewById(R.id.userName);
        profileDescription = (TextView) mDrawerLayout.findViewById(R.id.desc);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(mDrawerLayout);
        mDrawerLayout.setDrawerElevation(4);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        initToolbar();
    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void setUpNav() {
        mDrawerToggle = new ActionBarDrawerToggle(BaseActivity.this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mNavItems.add(new NavItem(getString(R.string.title_activity_tricktionary), getString(R.string.home_page), R.drawable.ic_video_library_black_24dp,Tricktionary.class));
        mNavItems.add(new NavItem(getString(R.string.speed_timer), getString(R.string.speed_drawer_description), R.drawable.ic_timer_black_24dp,Speed.class));
        mNavItems.add(new NavItem(getString(R.string.title_activity_speed_graph), getString(R.string.speed_data_drawer_description), R.drawable.ic_assessment_black_24dp,SpeedGraph.class));
        mNavItems.add(new NavItem(getString(R.string.title_activity_submit), getString(R.string.submit_drawer_description), R.drawable.ic_file_upload_black_24dp,Submit.class));
        mNavItems.add(new NavItem(getString(R.string.instagram), "@jumpropetricktionary", R.drawable.icon_alpha_small,Tricktionary.class));
        mNavItems.add(new NavItem(getString(R.string.web), "the-tricktionary.com", R.drawable.ic_public_black_24dp,Tricktionary.class));
        mNavItems.add(new NavItem(getString(R.string.title_activity_contact),getString(R.string.contact_drawer_description),R.drawable.ic_message_black_24dp,ContactActivity.class));
        mNavItems.add(new NavItem(getString(R.string.title_activity_show), getString(R.string.show_drawer_description), R.drawable.ic_assignment_black_24dp, Names.class));
        mNavItems.add(new NavItem(getString(R.string.title_activity_settings), getString(R.string.settings_drawer_description), R.drawable.ic_settings_black_48dp, SettingsActivity.class));
        if(mAuth.getCurrentUser()!=null) {
            mNavItems.add(new NavItem(getString(R.string.sign_out), "", R.drawable.ic_account_circle_black_24dp, Tricktionary.class));
        }

        mDrawerPane = (RelativeLayout) mDrawerLayout.findViewById(R.id.drawerPane);
        mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
        mDrawerToggle.syncState();
        if(mAuth.getCurrentUser()!=null){
            trictionary.jumproper.com.jumpropetrictionary.utils.DownloadImageTask downloadImage=new trictionary.jumproper.com.jumpropetrictionary.utils.DownloadImageTask(mAuth,avatar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
                    downloadImage.execute(mAuth.getCurrentUser().getPhotoUrl().toString());
                }
            }
            profileName.setText(mAuth.getCurrentUser().getDisplayName());
            profileDescription.setText("View Profile");
        }
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        actionBar=getSupportActionBar();
    }
    public void setBackButton(){
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setHamburger(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void setGlobalActionBarTitle(String title){
        this.toolbar.setTitle(title);
        this.actionBar.setTitle(title);
        this.mDrawerLayout.setDrawerTitle(Gravity.LEFT,title);
    }
    public void profileImageClicked(View v){
        if(mAuth.getCurrentUser()==null){
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }
    }
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;
        Class mClass;

        public NavItem(String title, String subtitle, int icon, Class activeClass) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
            mClass = activeClass;
        }
    }
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_list_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}
