package trictionary.jumproper.com.jumpropetrictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    FirebaseAuth mAuth;
    public static HashMap<String, String> contactIds;
    String currentId;
    int replyIndex=0;
    int expandIndex=-1;

    int delay = 100; //milliseconds
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_toolbar);

        mAuth=FirebaseAuth.getInstance();
        contactIds=new HashMap<>();
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
        expandableListDetail = ExpandableListData.getData();
        h = new Handler();
        h.postDelayed(r, delay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact us");
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
                                .withNameShown(false)
                                .withEnabled(true)

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
                            Intent intent = new Intent(ContactActivity.this, MainMenu.class);
                            startActivity(intent);
                        }
                        else if(position==3) {
                            Intent intent = new Intent(ContactActivity.this, Tricktionary.class);
                            startActivity(intent);
                        }
                        else if(position==5){
                            Intent intent = new Intent(ContactActivity.this, Speed.class);
                            startActivity(intent);
                        }
                        else if(position==7){
                            TrickList.index=((int)(Math.random()*MainActivity.getTricktionaryLength()));
                            Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(position==9){
                            Intent intent = new Intent(ContactActivity.this, Names.class);
                            startActivity(intent);
                        }
                        else if(position==11){
                            Intent intent = new Intent(ContactActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if(position==13){
                            Intent intent = new Intent(ContactActivity.this, Rafiki.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();


    }
    public Runnable r=new Runnable() {
        @Override
        public void run() {
            //do something
            if(expandableListDetail.isEmpty()){
                Log.i("Contact","List is empty");
                expandableListDetail=ExpandableListData.getData();
            }
            else{
                Log.i("Contact","List is full!");
                populateData();
                expandableListView.refreshDrawableState();
                delay=60000; //change update time to 1 minute
                h.removeCallbacks(r);
            }
            h.postDelayed(this, delay);
        }
    };

    public void populateData(){

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                currentId=contactIds.get(expandableListTitle.get(groupPosition));
                replyIndex=expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
                expandIndex=groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.d("Contact","groupPosition="+groupPosition+" childPosition="+childPosition);
                return false;
            }
        });


    }
    public void newReply(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this); //new alert dialog
        builder.setTitle("Submit reply"); //dialog title
        LayoutInflater inflater = (LayoutInflater)ContactActivity.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.new_reply,null); //custom layout file now a view object
        builder.setView(textBoxes); //set view to custom layout
        // Set up the buttons
        final EditText name = (EditText)textBoxes.findViewById(R.id.name_text);
        final EditText reply = (EditText)textBoxes.findViewById(R.id.reply_text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //prompt user for name

                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                DatabaseReference myRef=fb.getReference("contact").child(mAuth.getCurrentUser().getUid());
                Log.d("Contact","currentId="+currentId+" replyIndex="+replyIndex);
                myRef.child(currentId)
                        .child("replies")
                        .child(""+replyIndex)
                        .setValue(new Reply(name.getText().toString(),
                                  reply.getText().toString()));
                Toast.makeText(ContactActivity.this, "Thanks for the reply!",
                        Toast.LENGTH_SHORT).show();
                populateData();




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void viewMainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }


}
