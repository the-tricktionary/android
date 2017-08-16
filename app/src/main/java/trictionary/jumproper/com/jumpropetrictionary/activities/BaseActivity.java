package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.DrawerCreate;

public class BaseActivity extends AppCompatActivity {
    private DrawerLayout fullView;
    private Toolbar toolbar;
    private Drawer navigationDrawer;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = ((GlobalData) this.getApplication()).getmAuth();
    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public void setContentView(int layoutResID){
        fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        fullView.setDrawerElevation(4);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        DrawerCreate drawer=new DrawerCreate();
        navigationDrawer=drawer.makeDrawer(this, this, mAuth, toolbar, "");
        actionBar=getSupportActionBar();
    }
    @Override
    public void onResume(){
        super.onResume();
        DrawerCreate drawer=new DrawerCreate();
        navigationDrawer=drawer.makeDrawer(this, this, mAuth, toolbar, "");
        actionBar=getSupportActionBar();
    }
    public void setBackButton(){
        navigationDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setHamburger(){
        this.actionBar.setDisplayHomeAsUpEnabled(false);
        this.navigationDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
    public void setGlobalActionBarTitle(String title){
        this.toolbar.setTitle(title);
        this.actionBar.setTitle(title);
        this.fullView.setDrawerTitle(Gravity.LEFT,title);
    }


}
