package trictionary.jumproper.com.jumpropetrictionary;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Speed extends ActionBarActivity {

    TextView counter, time, duration, instructions;
    ImageView plus,dropDownArrow,backArrow;
    Button startButton;
    CheckBox timingTrack;
    public static int numJumps;
    long starttime = 0L;
    long timeInHundredths = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int hundredths = 0;
    boolean firstTap=true;
    public static int eventLength=30;
    Handler handler = new Handler();
    public static ArrayList<Integer> jumps=new ArrayList<>();
    public static ArrayList<Long> times=new ArrayList<>();
    private MediaPlayer time_10, time_15, time_20, time_30, time_45, time_1min, time_2min, time_1x30, time_1x180, time_beep;
    private MediaPlayer click;
    MediaPlayer[] timingTracks={time_10, time_15, time_20, time_30, time_45, time_1min, time_2min, time_1x30, time_1x180, time_beep};
    Vibrator vibrator;
    float x;
    float y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speed);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        numJumps=0;
        counter=(TextView)findViewById(R.id.counter);
        time=(TextView)findViewById(R.id.timer);
        duration=(TextView)findViewById(R.id.duration);
        plus=(ImageView)findViewById(R.id.plus);
        dropDownArrow=(ImageView)findViewById(R.id.dropdown_arrow);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        startButton=(Button)findViewById(R.id.start_button);
        timingTrack=(CheckBox)findViewById(R.id.timing_track);
        vibrator=(Vibrator) Speed.this.getSystemService(Context.VIBRATOR_SERVICE);
        instructions=(TextView)findViewById(R.id.instructions);

        time_10 = MediaPlayer.create(Speed.this,R.raw.time_10);
        time_15 = MediaPlayer.create(Speed.this,R.raw.time_15);
        time_20= MediaPlayer.create(this,R.raw.time_20);
        time_30= MediaPlayer.create(this,R.raw.time_30);
        time_45= MediaPlayer.create(this,R.raw.time_45);
        time_1min= MediaPlayer.create(this,R.raw.time_1min);
        time_2min = MediaPlayer.create(this,R.raw.time_2min);
        time_1x30= MediaPlayer.create(this,R.raw.time_1x30);
        time_1x180= MediaPlayer.create(this,R.raw.time_1x180);
        time_beep= MediaPlayer.create(this,R.raw.time_beep);

        click = MediaPlayer.create(this,R.raw.click);

        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    if(firstTap){
                        startTimer(startButton);
                        firstTap=false;
                    }

                    //if(click.isPlaying()){
                        //click.stop();
                    //}
                    //click.start();
                    vibrator.vibrate(75);
                    addJump(plus);
                }


                return true;
            }
        });








    }
    public void addJump(View v){

        if(startButton.getText().equals("Start")){
            return;
        }
        plus.setAlpha(.75f);
        //playClick();
        //new DownloadFilesTask().execute();

        ValueAnimator fadeIn = ObjectAnimator.ofFloat(plus, "alpha", .75f, 1f);
        fadeIn.setDuration(25);
        fadeIn.start();

        numJumps++;
        counter.setText(""+numJumps);
        jumps.add(numJumps);
        //System.out.println("current time= "+times.toString());
        times.add(timeInHundredths);

    }


    public void startTimer(View v){
        firstTap=false;
        counter.setText(""+0);
        if (t == 1) {

            MediaPlayer eventTrack=time_1x30;
            if (timingTrack.isChecked()){
                if (eventLength==30){
                    eventTrack=time_1x30;
                    eventTrack.start();

                }
                if (eventLength==180){
                    eventTrack=time_1x180;
                    eventTrack.start();
                }
                eventTrack.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        time_beep.start();
                        startButton.setText("Stop");
                        runTimer();
                    }
                });

            }
            else {
                startButton.setText("Stop");
                runTimer();
            }
        } else {
            jumps.clear();
            times.clear();
            time.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
            dropDownArrow.setVisibility(View.VISIBLE);
            instructions.setVisibility(View.VISIBLE);
            duration.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            timingTrack.setVisibility(View.VISIBLE);
            starttime = 0L;
            timeInHundredths = 0L;
            timeSwapBuff = 0L;
            updatedtime = 0L;
            t = 1;
            secs = 0;
            mins = 0;
            hundredths = 0;
            startButton.setText("Start");
            firstTap=true;
            numJumps=0;
            timeSwapBuff += timeInHundredths;
            handler.removeCallbacks(updateTimer);

        }

    }
    public void runTimer(){
        numJumps=0;
        jumps.clear();
        times.clear();
        time.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
        instructions.setVisibility(View.INVISIBLE);
        dropDownArrow.setVisibility(View.INVISIBLE);
        duration.setVisibility(View.INVISIBLE);
        backArrow.setVisibility(View.INVISIBLE);
        timingTrack.setVisibility(View.INVISIBLE);
        startButton.setText("Stop");
        starttime = SystemClock.uptimeMillis();
        t = 0;
        handler.postDelayed(updateTimer, 100);

    }
    public Runnable updateTimer = new Runnable() {
        public void run() {

            final WeakReference<TextView> timeRef;
            timeRef= new WeakReference<TextView>(time);

            timeInHundredths = (SystemClock.uptimeMillis() - starttime)/10;
            updatedtime = timeSwapBuff + timeInHundredths;
            secs = (int) (updatedtime / 100);
            if (secs==eventLength){
                handler.removeCallbacks(updateTimer);
                time_beep.start();
                if (numJumps!=0) {
                    Intent intent = new Intent(Speed.this, SpeedGraph.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    startTimer(startButton);
                }
                return;
            }
            if((eventLength==30)&&(timingTrack.isChecked())){
                if(timeInHundredths/10==10*10){
                    time_10.start();

                }
                if(timeInHundredths/10==20*10){
                    time_20.start();
                    time_10.stop();
                }

            }
            else if(eventLength>15){
                if(timeInHundredths/10==15*10){
                    time_15.start();
                }
                if(timeInHundredths/10==30*10){
                    time_30.start();
                }
                if(timeInHundredths/10==45*10){
                    time_45.start();
                }
                if(timeInHundredths/10==60*10){
                    time_1min.start();
                }
                if(timeInHundredths/10==75*10){
                    time_1min.start();
                    time_1min.setNextMediaPlayer(time_15);
                }
                if(timeInHundredths/10==90*10){
                    time_1min.setNextMediaPlayer(time_30);
                    time_1min.start();
                }
                if(timeInHundredths/10==105*10){
                    time_1min.setNextMediaPlayer(time_45);
                    time_1min.start();
                }
                if(timeInHundredths/10==120*10){
                    time_2min.start();
                }
                if(timeInHundredths/10==135*10){
                    time_2min.setNextMediaPlayer(time_15);
                    time_2min.start();
                }
                if(timeInHundredths/10==150*10){
                    time_2min.setNextMediaPlayer(time_30);
                    time_2min.start();
                }
                if(timeInHundredths/10==165*10){
                    time_2min.setNextMediaPlayer(time_45);
                    time_2min.start();
                }

            }

            mins = secs / 60;
            secs = secs % 60;
            hundredths = (int) (updatedtime % 100);
            final TextView time= timeRef.get();
            time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + hundredths/10);

            handler.postDelayed(this,100);

        }};



    public void chooseDuration(View v){
        PopupMenu popupMenu = new PopupMenu(Speed.this, v);
        //Inflating the Popup using xml file

        popupMenu.getMenu().add("0:30");
        popupMenu.getMenu().add("1:00");
        popupMenu.getMenu().add("2:00");
        popupMenu.getMenu().add("3:00");
        popupMenu.getMenu().add("Custom");



        popupMenu.show();
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.toString().equals("0:30")){
                    duration.setText("0:30");
                    eventLength=30;
                }
                else if(item.toString().equals("1:00")){
                    duration.setText("1:00");
                    eventLength=60;
                }
                else if(item.toString().equals("2:00")){
                    duration.setText("2:00");
                    eventLength=120;
                }
                else if(item.toString().equals("3:00")){
                    duration.setText("3:00");
                    eventLength=180;
                }
                else{
                    duration.setText("Custom");

                    AlertDialog.Builder builder = new AlertDialog.Builder(Speed.this);
                    builder.setTitle("Custom Time");
                    LayoutInflater inflater = (LayoutInflater)Speed.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                    final View textBoxes=inflater.inflate(R.layout.custom_time_picker,null);
                    builder.setView(textBoxes);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText seconds = (EditText)textBoxes.findViewById(R.id.custom_time_seconds);
                            EditText minutes = (EditText)textBoxes.findViewById(R.id.custom_time_minutes);
                            if(minutes.getText().toString().equals("")){
                                minutes.setText("0");
                            }
                            if(seconds.getText().toString().equals("")){
                                seconds.setText("0");
                            }
                            eventLength=Integer.parseInt(minutes.getText().toString()) * 60 + Integer.parseInt(seconds.getText().toString());
                            duration.setText(minutes.getText().toString()+":"+String.format("%02d",Integer.parseInt(seconds.getText().toString())));
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
                return true;
            }
        });
    }

    public void mainMenu(View v){
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }





}
