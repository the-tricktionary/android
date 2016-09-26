package trictionary.jumproper.com.jumpropetrictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

/**
 * Created by jumpr_000 on 9/26/2016.
 */

public class DrawerCreate extends AppCompatActivity{
    public void makeDrawer(final Context context, final Activity activity, final FirebaseAuth mAuth, Toolbar toolbar, String title){

        new DrawerBuilder().withActivity(activity).build();
        PrimaryDrawerItem mainMenuItem=new PrimaryDrawerItem().withName("Main Menu");
        PrimaryDrawerItem tricktionaryItem=new PrimaryDrawerItem().withName("Tricktionary");
        PrimaryDrawerItem speedItem=new PrimaryDrawerItem().withName("Speed Timer");
        PrimaryDrawerItem randomTrickItem=new PrimaryDrawerItem().withName("Random Trick");
        PrimaryDrawerItem showWriterItem=new PrimaryDrawerItem().withName("Show Writer");
        PrimaryDrawerItem settingsItem=new PrimaryDrawerItem().withName("Settings");
        PrimaryDrawerItem rafikiItem=new PrimaryDrawerItem().withName("Rafiki Program");
        ProfileDrawerItem currentProfile;

        if(mAuth.getCurrentUser()!=null){
            currentProfile=new ProfileDrawerItem()
                    .withName(mAuth.getCurrentUser().getDisplayName())
                    .withIcon(mAuth.getCurrentUser().getPhotoUrl())
                    .withEnabled(true)
                    .withEmail(mAuth.getCurrentUser().getEmail());
        }
        else{
            currentProfile=new ProfileDrawerItem()
                    .withName("Sign in")
                    .withEmail("Tap the icon to sign in")
                    .withEnabled(false)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Intent intent = new Intent(context, SignIn.class);
                            activity.startActivity(intent);
                            return false;
                        }
                    });
        }


        AccountHeader headerResult = new AccountHeaderBuilder()
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
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        if(mAuth.getCurrentUser()==null) {
                            Intent intent = new Intent(context, SignIn.class);
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
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
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
                .build();
        toolbar.setTitle(title);
    }
}
