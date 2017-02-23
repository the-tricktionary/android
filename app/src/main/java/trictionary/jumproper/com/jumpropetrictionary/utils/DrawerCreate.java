package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.InputStream;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.activities.MainActivity;
import trictionary.jumproper.com.jumpropetrictionary.activities.MainMenu;
import trictionary.jumproper.com.jumpropetrictionary.activities.Profile;
import trictionary.jumproper.com.jumpropetrictionary.activities.Rafiki;
import trictionary.jumproper.com.jumpropetrictionary.activities.SettingsActivity;
import trictionary.jumproper.com.jumpropetrictionary.activities.SignIn;
import trictionary.jumproper.com.jumpropetrictionary.activities.Speed;
import trictionary.jumproper.com.jumpropetrictionary.activities.Tricktionary;
import trictionary.jumproper.com.jumpropetrictionary.show.Names;

/**
 * Created by jumpr_000 on 9/26/2016.
 */

public class DrawerCreate extends AppCompatActivity{
    ProfileDrawerItem currentProfile;
    FirebaseAuth mAuthCopy;
    AccountHeader headerResult;
    public void makeDrawer(final Context context, final Activity activity, final FirebaseAuth mAuth, Toolbar toolbar, String title){

        mAuthCopy=mAuth;
        new DrawerBuilder().withActivity(activity).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");
        PrimaryDrawerItem rafikiItem=new PrimaryDrawerItem().withName("Rafiki Program");


        if(mAuth.getCurrentUser()!=null){
            DownloadImageTask downloadImage=new DrawerCreate.DownloadImageTask(currentProfile);
            downloadImage.execute(mAuth.getCurrentUser().getPhotoUrl().toString());
            currentProfile=new ProfileDrawerItem()
                    .withName(mAuth.getCurrentUser().getDisplayName())
                    .withEnabled(true)
                    .withEmail(mAuth.getCurrentUser().getEmail());
        }
        else{
            currentProfile=new ProfileDrawerItem()
                    .withName("Sign in")
                    .withEmail("Tap the icon to sign in")
                    .withEnabled(false);

        }


        headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.background)
                .addProfiles(
                        currentProfile
                )
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if(mAuth.getCurrentUser()==null) {
                            Intent intent = new Intent(context, SignIn.class);
                            activity.startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(context, Profile.class);
                            activity.startActivity(intent);
                        }
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        if(mAuth.getCurrentUser()==null) {
                            Intent intent = new Intent(context, SignIn.class);
                            activity.startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(context, Profile.class);
                            activity.startActivity(intent);
                        }
                        return false;
                    }
                })
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(true)
                .build();


        Drawer result = new DrawerBuilder()
                .withActivity(activity)
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
                            Intent intent = new Intent(context, MainMenu.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        if(position==1){
                            Intent intent = new Intent(context, MainMenu.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        else if(position==3) {
                            Intent intent = new Intent(context, Tricktionary.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(context, Speed.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        else if(position==7){
                            MainMenu.index=((int)(Math.random()* MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(context, MainActivity.class);
                            activity.startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(context, Names.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        else if(position==11){
                            Intent intent = new Intent(context, SettingsActivity.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        else if(position==13){
                            Intent intent = new Intent(context, Rafiki.class);
                            if(context.getClass().getName().equals(intent.getComponent().getClassName()))
                                return true;
                            activity.startActivity(intent);
                        }
                        return true;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if((mAuth.getCurrentUser()!=null)&&(currentProfile.getName().toString().equals("Sign in"))){
                            headerResult.setActiveProfile(new ProfileDrawerItem()
                                                            .withEmail(mAuth.getCurrentUser().getEmail())
                                                            .withName(mAuth.getCurrentUser().getDisplayName())
                                                            .withIcon(mAuth.getCurrentUser().getPhotoUrl()));
                        }
                        else if((mAuth.getCurrentUser()==null)&&!(currentProfile.getName().toString().equals("Sign in"))){
                            headerResult.setActiveProfile(new ProfileDrawerItem()
                                    .withName("Sign in")
                                    .withEmail("Tap the icon to sign in")
                                    .withEnabled(false));
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        return;
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        return;
                    }
                })
                .build();
        toolbar.setTitle(title);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, ProfileDrawerItem> {
        ProfileDrawerItem bmImage;

        public DownloadImageTask(ProfileDrawerItem bmImage) {
            this.bmImage = bmImage;
        }

        protected ProfileDrawerItem doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {

                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                currentProfile=new ProfileDrawerItem()
                        .withName(mAuthCopy.getCurrentUser().getDisplayName())
                        .withEnabled(true)
                        .withEmail(mAuthCopy.getCurrentUser().getEmail())
                        .withIcon(mIcon11);
            } catch (Exception e) {
                currentProfile=new ProfileDrawerItem()
                        .withName(mAuthCopy.getCurrentUser().getDisplayName())
                        .withEnabled(true)
                        .withEmail(mAuthCopy.getCurrentUser().getEmail());
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return currentProfile;
        }

        protected void onPostExecute(ProfileDrawerItem current) {
            headerResult.setActiveProfile(current);
        }
    }
}
