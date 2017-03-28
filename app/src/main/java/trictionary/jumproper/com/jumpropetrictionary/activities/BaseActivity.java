package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.utils.DrawerCreate;

public class BaseActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private DrawerLayout fullView;
    private Toolbar toolbar;
    private Drawer navigationDrawer;
    private ActionBar actionBar;
    private Drawable backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mAuth= FirebaseAuth.getInstance();
        DrawerCreate drawer=new DrawerCreate();
        navigationDrawer=drawer.makeDrawer(this, this, mAuth, toolbar, "");
        actionBar=getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navigationDrawer.getActionBarDrawerToggle().isDrawerIndicatorEnabled()){
                    navigationDrawer.openDrawer();
                }
                else{
                    finish();
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        DrawerCreate drawer=new DrawerCreate();
        navigationDrawer=drawer.makeDrawer(this, this, mAuth, toolbar, "");
        actionBar=getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navigationDrawer.getActionBarDrawerToggle().isDrawerIndicatorEnabled()){
                    navigationDrawer.openDrawer();
                }
                else{
                    finish();
                }
            }
        });
    }
    public void setBackButton(){
        this.navigationDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
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
