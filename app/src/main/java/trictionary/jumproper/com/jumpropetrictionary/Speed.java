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
    TextView counter, time, duration, instructions, eventSelect; //textviews
    ImageView plus,dropDownArrow,backArrow,eventDropDown; //imageviews
    Button startButton;  //bottom of screen start button
    SpeedEvent currentEvent;//speed event object used for timing tracks
    public static int numJumps;  //score of event being clicked
    //timer
    long starttime = 0L; //start time in seconds
    long timeInHundredths = 0L; //current time in hundredths
    long timeSwapBuff = 0L; //buffer between time updates
    long updatedtime = 0L; //new time after update
    int t = 1; //has timer been run yet?
    int secs = 0; //timer seconds
    int mins = 0; //timer minutes
    int hundredths = 0; //first digit of timeInHundredths for timer display
    boolean firstTap=true; //will first click start the timer?
    public static int eventLength=30; //current event length in seconds
    Handler handler = new Handler(); //handler for the timer
    public static ArrayList<Integer> jumps=new ArrayList<>(); //list of jumps to update score
    public static ArrayList<Long> times=new ArrayList<>(); //list of times of jumps in ms for graph
    //declaring all timing tracks
    private MediaPlayer time_10, time_15, time_20, time_30, time_45, time_1min, time_2min,
                        time_1x30, time_1x180, time_2x30, time_4x30, time_2x60, time_3x40, time_beep,

                        fisac_1x30, fisac_1x180, fisac_4x45, fisac_2x60, fisac_4x30, fisac_10,
                        fisac_15, fisac_20, fisac_30, fisac_45, fisac_1min, fisac_2min,
                        fisac_switch, fisac_beep;
    Vibrator vibrator; //no not that kind

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speed);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //dont let screen sleep
        numJumps=0; //always reset score
        counter=(TextView)findViewById(R.id.counter); //current score display
        time=(TextView)findViewById(R.id.timer); //timer display
        duration=(TextView)findViewById(R.id.duration); //duration selection
        plus=(ImageView)findViewById(R.id.plus); //middle of screen cicker
        dropDownArrow=(ImageView)findViewById(R.id.dropdown_arrow); //duration dropdown arrow
        backArrow=(ImageView)findViewById(R.id.back_arrow); //return to main menu
        eventDropDown=(ImageView)findViewById(R.id.event_dropdown); //event dropdown arrow
        startButton=(Button)findViewById(R.id.start_button); //bottom of screen start button
        vibrator=(Vibrator) Speed.this.getSystemService(Context.VIBRATOR_SERVICE); //requesting vibrator services
        instructions=(TextView)findViewById(R.id.instructions); //tiny text below timer
        eventSelect=(TextView)findViewById(R.id.event_select); //event selection
        currentEvent=new SpeedEvent(this); //intitialize empty event in case of no timing track

        //wjr timing tracks
        time_10 = MediaPlayer.create(Speed.this,R.raw.time_10);
        time_15 = MediaPlayer.create(Speed.this,R.raw.time_15);
        time_20= MediaPlayer.create(this,R.raw.time_20);
        time_30= MediaPlayer.create(this,R.raw.time_30);
        time_45= MediaPlayer.create(this,R.raw.time_45);
        time_1min= MediaPlayer.create(this,R.raw.time_1min);
        time_2min = MediaPlayer.create(this,R.raw.time_2min);
        time_1x30= MediaPlayer.create(this,R.raw.time_1x30);
        time_1x180= MediaPlayer.create(this,R.raw.time_1x180);
        time_2x30= MediaPlayer.create(this,R.raw.time_2x30);
        time_4x30= MediaPlayer.create(this,R.raw.time_4x30);
        time_3x40= MediaPlayer.create(this,R.raw.time_3x40);
        time_2x60= MediaPlayer.create(this,R.raw.time_2x60);
        time_beep= MediaPlayer.create(this,R.raw.time_beep);
        //fisac timing tracks
        fisac_1x30=MediaPlayer.create(Speed.this,R.raw.fisac_1x30);
        fisac_1x180=MediaPlayer.create(Speed.this,R.raw.fisac_1x180);
        fisac_4x45=MediaPlayer.create(Speed.this,R.raw.fisac_4x45);
        fisac_2x60=MediaPlayer.create(Speed.this,R.raw.fisac_2x60);
        fisac_4x30=MediaPlayer.create(Speed.this,R.raw.fisac_4x30);
        fisac_10=MediaPlayer.create(Speed.this,R.raw.fisac_10);
        fisac_15=MediaPlayer.create(Speed.this,R.raw.fisac_15);
        fisac_20=MediaPlayer.create(Speed.this,R.raw.fisac_20);
        fisac_30=MediaPlayer.create(Speed.this,R.raw.fisac_30);
        fisac_45=MediaPlayer.create(Speed.this,R.raw.fisac_45);
        fisac_1min=MediaPlayer.create(Speed.this,R.raw.fisac_1min);
        fisac_2min=MediaPlayer.create(Speed.this,R.raw.fisac_2min);
        fisac_switch=MediaPlayer.create(Speed.this,R.raw.fisac_switch);
        fisac_beep=MediaPlayer.create(Speed.this,R.raw.fisac_beep);

        //on touch listener for only tap down because it is much faster
        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //only down tap
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    if(firstTap){ //start timer if it is the first click
                        startTimer(startButton);
                        firstTap=false;
                    }
                    vibrator.vibrate(75); //75ms vibration
                    addJump(plus); //increment score and animate clicker
                }
                return true;
            }
        });

    }
    /**whenever the clicker is tapped animate a button press of the clicker and increment the
     * score.  Only works if the timer is running or can be activated by a non-timing track
     * first tap.**/
    public void addJump(View v){
        //check if timer is running
        if(startButton.getText().equals("Start")){
            return;
        }
        plus.setAlpha(.75f); //don't need to animate the fade out
        //maybe one day if latency isn't such a huge issue
        //playClick();
        //new DownloadFilesTask().execute();

        ValueAnimator fadeIn = ObjectAnimator.ofFloat(plus, "alpha", .75f, 1f); //fade back to full
        fadeIn.setDuration(25); //25ms
        fadeIn.start();

        numJumps++; //increase score
        counter.setText(""+numJumps); //display new score
        jumps.add(numJumps); //add score to list of jumps
        times.add(timeInHundredths); //add current time is ms to list of times

    }


    public void startTimer(View v){
        firstTap=false;
        counter.setText(""+0);
        if (t == 1) {

            MediaPlayer eventTrack=time_1x30;
            if (currentEvent.getName().length()>0){
                if (currentEvent.getName().equals("FISAC 1x30")){
                    eventTrack=fisac_1x30;
                    eventTrack.start();

                }
                if (currentEvent.getName().equals("FISAC 1x180")){
                    eventTrack=fisac_1x180;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("FISAC 4x45")){
                    eventTrack=fisac_4x45;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("FISAC 2x60")){
                    eventTrack=fisac_2x60;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("FISAC 4x30")){
                    eventTrack=fisac_4x30;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 1x30")){
                    eventTrack=time_1x30;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 1x180")){
                    eventTrack=time_1x180;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 2x30")){
                    eventTrack=time_2x30;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 4x30")){
                    eventTrack=time_4x30;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 3x40")){
                    eventTrack=time_3x40;
                    eventTrack.start();
                }
                if (currentEvent.getName().equals("WJR 2x60")){
                    eventTrack=time_2x60;
                    eventTrack.start();
                }

                eventTrack.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(currentEvent.isWjr()){
                            time_beep.start();
                        }
                        else{
                            fisac_beep.start();
                        }
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
            eventSelect.setVisibility(View.VISIBLE);
            eventDropDown.setVisibility(View.VISIBLE);

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
        eventSelect.setVisibility(View.INVISIBLE);
        eventDropDown.setVisibility(View.INVISIBLE);
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

            currentEvent.runCurrentEvent(timeInHundredths);


            if (timeInHundredths/10==eventLength*10){
                handler.removeCallbacks(updateTimer);
                //time_beep.start();
                if (numJumps!=0) {
                    mins = secs / 60;
                    secs = secs % 60;
                    final TextView time= timeRef.get();
                    time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                            + "0");
                    Intent intent = new Intent(Speed.this, SpeedGraph.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    mins = secs / 60;
                    secs = secs % 60;
                    final TextView time= timeRef.get();
                    time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                            + "0");
                    startTimer(startButton);
                }
                return;
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
    public String formatDuration(int secs){
        String duration="";
        duration+=""+secs/60;
        duration+=":"+String.format("%02d", secs%60);
        return duration;
    }

    public void selectEvent(View v){
        PopupMenu popupMenu = new PopupMenu(Speed.this, v);
        //Inflating the Popup using xml file


        popupMenu.getMenu().add("WJR 1x30");
        popupMenu.getMenu().add("WJR 1x180");
        popupMenu.getMenu().add("WJR 2x30");
        popupMenu.getMenu().add("WJR 4x30");
        popupMenu.getMenu().add("WJR 3x40");
        popupMenu.getMenu().add("WJR 2x60");
        popupMenu.getMenu().add("FISAC 1x30");
        popupMenu.getMenu().add("FISAC 1x180");
        popupMenu.getMenu().add("FISAC 4x30");
        popupMenu.getMenu().add("FISAC 2x60");
        popupMenu.getMenu().add("FISAC 4x45");



        popupMenu.show();
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                currentEvent=new SpeedEvent(Speed.this,  menuItem.getTitle().toString());
                duration.setText(""+formatDuration(currentEvent.getDuration()));
                eventSelect.setText(menuItem.getTitle().toString());
                eventLength=currentEvent.getDuration();
                return false;
            }
        });
    }





}
