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
}
