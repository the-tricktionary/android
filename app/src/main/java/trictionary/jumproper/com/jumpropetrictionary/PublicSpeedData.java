package trictionary.jumproper.com.jumpropetrictionary;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class PublicSpeedData extends AppCompatActivity {
    LineChart chart;
    ArrayList<Long> scrubbedTimes;
    SpeedData data;
    ArrayList<SpeedData> allData;
    AlertDialog dateSelect;
    String finalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_speed_data);
        allData=new ArrayList<>();
        //getData();

    }
    public void drawSlopeGraph(LineChart chart,ArrayList<Long> scrubbedTimes){
        ArrayList<String> jumpLabels=getXAxisValues(scrubbedTimes.size(),data.getTime());
        ArrayList<Entry> values=new ArrayList<>();

        for(int j=1;j<scrubbedTimes.size();j++){
            values.add(new Entry(100*(1/((float)scrubbedTimes.get(j)-(float)scrubbedTimes.get(j-1))), j));
        }

        LineDataSet set=new LineDataSet(values, "Jumps per second");
        set.setDrawCubic(true);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setCircleRadius(0);
        set.setColor(Color.RED);
        set.setFillColor(Color.RED);
        set.setFillAlpha(75);

        LineData totalJumps= new LineData(jumpLabels, set);
        totalJumps.setDrawValues(false);

        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);

        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(6);


        chart.setDescription("");
        chart.setData(totalJumps);

        chart.invalidate();
    }
    public void drawGraph(LineChart chart,ArrayList<Long> scrubbedTimes){
        ArrayList<String> jumpLabels=new ArrayList<>();//getXAxisValues(scrubbedTimes.size(),data.getTime());
        for(int j=0;j<scrubbedTimes.size();j++){
            jumpLabels.add(""+scrubbedTimes.get(j)/100);
        }
        ArrayList<Entry> values=new ArrayList<>();

        for(int j=1;j<scrubbedTimes.size();j++){
            values.add(new Entry(scrubbedTimes.get(j)/100,j));
        }

        LineDataSet set=new LineDataSet(values, "Total Jumps");
        set.setDrawCubic(true);
        set.setDrawFilled(true);
        set.setDrawValues(true);
        set.setCircleRadius(1);
        set.setColor(Color.RED);
        set.setFillColor(Color.RED);
        set.setFillAlpha(75);

        LineData totalJumps= new LineData(jumpLabels, set);
        totalJumps.setDrawValues(false);

        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);


        //leftAxis.setAxisMinValue(0);
        //leftAxis.setAxisMaxValue(data.getScore()/2);



        chart.setDescription("");
        chart.setData(totalJumps);

        chart.invalidate();
    }
    public ArrayList<String> getXAxisValues(int size,int eventLength){
        double interval=(double)(eventLength)/size;
        double currentValue=0;
        ArrayList<String> values=new ArrayList<>();
        for(int j=0;j<size;j++){
            values.add(":" + String.format("%02d", (int)currentValue));
            currentValue+=interval;
        }
        values.remove(values.size()-1);
        values.add(":"+String.format("%02d", eventLength));
        return values;
    }
    /*public void getData(){
        final Firebase myFirebaseRef = new Firebase("https://jump-rope-scores.firebaseio.com/");
        //myFirebaseRef.child("scores").child("Dylan Plummer").child(finalDate).addValueEventListener(new ValueEventListener() {
        myFirebaseRef.child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Children of "+dataSnapshot.getValue());
                GenericTypeIndicator<SpeedData> sd = new GenericTypeIndicator<SpeedData>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                };
                for(DataSnapshot name:dataSnapshot.getChildren()){
                    System.out.println(name.getKey()+" "+name.getValue());
                    for(DataSnapshot date:name.getChildren()){
                        System.out.println(date.getKey()+" "+date.getValue());
                        data=date.getValue(sd);
                        allData.add(date.getValue(sd));
                    }
                }


                //data=dataSnapshot.getValue(sd);
                drawSlopeGraph((LineChart)findViewById(R.id.chart),averageAllData(allData));
                drawGraph((LineChart)findViewById(R.id.chart2),averageAllData(allData));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });


    }
    public void selectDate(String name){

        final Firebase myFirebaseRef = new Firebase("https://jump-rope-scores.firebaseio.com/");
        final ArrayList<String> datesList=new ArrayList<>();
        System.out.println("name "+name);
        myFirebaseRef.child("scores").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("dates pls work "+dataSnapshot.getChildrenCount());
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    datesList.add(snapshot.getKey());
                    System.out.println("date added! "+snapshot.getKey());
                }
                return;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
        LayoutInflater inflater = (LayoutInflater)PublicSpeedData.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View textBoxes=inflater.inflate(R.layout.select_date,null);


        final ListView dates=(ListView)textBoxes.findViewById(R.id.dates_list);
        System.out.println("dates to use "+datesList.toString());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(PublicSpeedData.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, datesList);
        // Assign adapter to ListView
        dates.setAdapter(adapter);

        dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // ListView Clicked item value
                finalDate= (String) dates.getItemAtPosition(position);
                System.out.println(finalDate);
                getData();
                dateSelect.dismiss();



            }

        });
        AlertDialog.Builder builder = new AlertDialog.Builder(PublicSpeedData.this);
        builder.setTitle("Select Date");



        builder.setView(textBoxes);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create();
        dateSelect=builder.show();
        dates.refreshDrawableState();
        textBoxes.refreshDrawableState();
    }
    public ArrayList<Long> riemannSum(ArrayList<Long> speed){
        ArrayList<Long> sum=new ArrayList<>();
        long area=0;
        for(int j=0;j<speed.size();j++){
            area+=speed.get(j);
            sum.add(area);
        }
        return sum;
    }
    public ArrayList<Long> averageAllData(ArrayList<SpeedData> list){
        ArrayList<Long> avg=new ArrayList<>();
        for(int j=0;j<list.size();j++){
            if(list.get(j).getTime()==30) {
                avg.add((long) list.get(j).getScore());
            }
        }

        System.out.println("Avg Graph= "+avg.toString());
        return avg;
    }
    public int averageScore(ArrayList<SpeedData> list){
        int avg=0;
        for(int j=0;j<list.size();j++){
            avg+=list.get(j).getScore();
        }
        return avg/list.size();
    }*/
}
