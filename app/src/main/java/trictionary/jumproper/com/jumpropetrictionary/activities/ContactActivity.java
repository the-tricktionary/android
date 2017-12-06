package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trictionary.jumproper.com.jumpropetrictionary.customviews.CustomExpandableListAdapter;
import trictionary.jumproper.com.jumpropetrictionary.customviews.ExpandableListData;
import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.contact.Reply;

public class ContactActivity extends BaseActivity {

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
        setContentView(R.layout.activity_contact);
        mAuth=FirebaseAuth.getInstance();
        contactIds=new HashMap<>();
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
        expandableListDetail = ExpandableListData.getData();
        h = new Handler();
        h.postDelayed(r, delay);
        setGlobalActionBarTitle(getString(R.string.title_activity_contact));

    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }
    public Runnable r=new Runnable() {
        @Override
        public void run() {
            //do something
            if(expandableListDetail.isEmpty()){
                expandableListDetail=ExpandableListData.getData();
            }
            else{
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
        builder.setTitle(R.string.contact_submit_reply); //dialog title
        LayoutInflater inflater = (LayoutInflater)ContactActivity.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.new_reply,null); //custom layout file now a view object
        builder.setView(textBoxes); //set view to custom layout
        // Set up the buttons
        final EditText name = (EditText)textBoxes.findViewById(R.id.name_text);
        final EditText reply = (EditText)textBoxes.findViewById(R.id.reply_text);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //prompt user for name

                FirebaseDatabase fb=FirebaseDatabase.getInstance();
                DatabaseReference myRef=fb.getReference("contact").child(mAuth.getCurrentUser().getUid());
                myRef.child(currentId)
                        .child("replies")
                        .child(""+replyIndex)
                        .setValue(new Reply(name.getText().toString(),
                                  reply.getText().toString()));
                Toast.makeText(ContactActivity.this, R.string.contact_reply_toast,
                        Toast.LENGTH_SHORT).show();
                populateData();




            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void viewMainMenu(View v){
        finish();
    }




}
