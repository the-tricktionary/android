package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeedDataSelect extends AppCompatActivity {
    public static String mUid;
    ArrayList<String> events;
    ArrayList<String> scores;
    ArrayList<String> dates;
    int pos;
    TextView tryRefresh;

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    ListView eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_data_select);
        eventList=(ListView)findViewById(R.id.event_list);
        events=new ArrayList<>();
        scores=new ArrayList<>();
        dates=new ArrayList<>();
        tryRefresh=(TextView)findViewById(R.id.try_refresh);
        getEvents();

        SimpleAdapter eventAdapter = new SimpleAdapter(this,list,
                                                        android.R.layout.simple_list_item_2,
                                                        new String[] {"event","date"},
                                                        new int[] {android.R.id.text1,
                                                                    android.R.id.text2});

        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos=i;
                FirebaseDatabase fb = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = fb.getReference("speed").child("scores").child(mUid);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot date:dataSnapshot.getChildren()) {
                                if(date.child("name").getValue().toString().equals(events.get(pos))){
                                    GenericTypeIndicator<SpeedData> sd = new GenericTypeIndicator<SpeedData>() {
                                    };
                                    SpeedGraph.data=date.getValue(sd);
                                    Intent intent = new Intent(SpeedDataSelect.this, SpeedGraph.class);
                                    finish();
                                    startActivity(intent);
                                }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });
            }
        });
    }
    public void getEvents() {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fb.getReference("speed").child("scores").child(mUid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("snapshot children " + dataSnapshot.getValue());
                for(DataSnapshot date:dataSnapshot.getChildren()){
                        Map<String, String> data = new HashMap<String, String>(2);
                        events.add(date.child("name").getValue().toString());
                        data.put("event",date.child("name").getValue().toString()+ "  (" + date.child("score").getValue().toString()+")");
                        data.put("date",formatEpoch(date.getKey()));
                        list.add(data);
                }

                return;
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                return;
            }
        });
        eventList.refreshDrawableState();

    }

    public String formatEpoch(String str){
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date (Long.parseLong(str)*1000));
        return date;
    }
    public String formatDuration(int secs){
        String duration="";
        duration+=""+secs/60;
        duration+=":"+String.format("%02d", secs%60);
        return duration;
    }
    public void refreshItems(View v){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
    public void backToGraph(View v){
        SpeedGraph.data=null;
        SpeedGraph.loadingData=true;
        Intent intent = new Intent(this, SpeedGraph.class);
        finish();
        startActivity(intent);
    }
}
