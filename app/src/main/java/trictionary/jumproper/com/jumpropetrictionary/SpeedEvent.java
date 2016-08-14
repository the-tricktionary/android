package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by jumpr_000 on 8/12/2016.
 */
public class SpeedEvent {
    //fields
    private Context context;

    private int duration;
    private String name;
    private boolean wjr;

    private MediaPlayer time_10, time_15, time_20, time_30, time_45, time_1min, time_2min,
            time_1x30, time_1x180, time_beep,

    fisac_1x30, fisac_1x180, fisac_4x45, fisac_2x60, fisac_4x30, fisac_10,
            fisac_15, fisac_20, fisac_30, fisac_45, fisac_1min, fisac_2min,
            fisac_switch, fisac_beep;

    //constructors
    public SpeedEvent(Context context) {
        this.context=context;
        this.name="";
        this.duration=0;
        this.wjr=true;
    }
    public SpeedEvent(Context context, String mName) {
        this.context=context;
        this.name=mName;
        if(mName.equals("FISAC 1x30")){
            wjr=false;
            duration=30;
        }
        if(mName.equals("FISAC 1x180")){
            wjr=false;
            duration=180;
        }
        if(mName.equals("FISAC 4x45")){
            wjr=false;
            duration=180;
        }
        if(mName.equals("FISAC 2x60")){
            wjr=false;
            duration=120;
        }
        if(mName.equals("FISAC 4x30")){
            wjr=false;
            duration=120;
        }
        initTimingTracks();
    }

    //getters and setters
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWjr() {
        return wjr;
    }

    public void setWjr(boolean wjr) {
        this.wjr = wjr;
    }

    //other methods
    public void initTimingTracks(){
        time_10 = MediaPlayer.create(context,R.raw.time_10);
        time_15 = MediaPlayer.create(context,R.raw.time_15);
        time_20= MediaPlayer.create(context,R.raw.time_20);
        time_30= MediaPlayer.create(context,R.raw.time_30);
        time_45= MediaPlayer.create(context,R.raw.time_45);
        time_1min= MediaPlayer.create(context,R.raw.time_1min);
        time_2min = MediaPlayer.create(context,R.raw.time_2min);
        time_1x30= MediaPlayer.create(context,R.raw.time_1x30);
        time_1x180= MediaPlayer.create(context,R.raw.time_1x180);
        time_beep= MediaPlayer.create(context,R.raw.time_beep);

        fisac_1x30=MediaPlayer.create(context,R.raw.fisac_1x30);
        fisac_1x180=MediaPlayer.create(context,R.raw.fisac_1x180);
        fisac_4x45=MediaPlayer.create(context,R.raw.fisac_4x45);
        fisac_2x60=MediaPlayer.create(context,R.raw.fisac_2x60);
        fisac_4x30=MediaPlayer.create(context,R.raw.fisac_4x30);
        fisac_10=MediaPlayer.create(context,R.raw.fisac_10);
        fisac_15=MediaPlayer.create(context,R.raw.fisac_15);
        fisac_20=MediaPlayer.create(context,R.raw.fisac_20);
        fisac_30=MediaPlayer.create(context,R.raw.fisac_30);
        fisac_45=MediaPlayer.create(context,R.raw.fisac_45);
        fisac_1min=MediaPlayer.create(context,R.raw.fisac_1min);
        fisac_2min=MediaPlayer.create(context,R.raw.fisac_2min);
        fisac_switch=MediaPlayer.create(context,R.raw.fisac_switch);
        fisac_beep=MediaPlayer.create(context,R.raw.fisac_beep);
    }

    public void runCurrentEvent(long time){
        if (this.getName().equals("FISAC 1x30")){
            this.runFisac1x30(time);
        }
        if (this.getName().equals("FISAC 1x180")){
            this.runFisac1x180(time);
        }
        if (this.getName().equals("FISAC 4x45")){
            this.runFisac4x45(time);
        }
        if (this.getName().equals("FISAC 2x60")){
            this.runFisac2x60(time);
        }
        if (this.getName().equals("FISAC 4x30")){
            this.runFisac4x30(time);
        }
    }

    public void runFisac1x30(long time){
        if(time/10==10*10){
            fisac_10.start();

        }
        if(time/10==20*10){
            fisac_20.start();
        }
        if(time/10==30*10){
            fisac_beep.start();
        }
    }
    public void runFisac1x180(long time){
        if(time/10==30*10){
            fisac_30.start();
        }
        if(time/10==60*10){
            fisac_1min.start();
        }
        if(time/10==90*10){
            fisac_30.start();
        }
        if(time/10==120*10){
            fisac_2min.start();
        }
        if(time/10==135*10){
            fisac_15.start();
        }
        if(time/10==150*10){
            fisac_30.start();
        }
        if(time/10==165*10){
            fisac_45.start();
        }
        if(time/10==180*10){
            fisac_beep.start();
        }
    }
    public void runFisac4x45(long time){
        if((time/10==15*10)||(time/10==60*10)||(time/10==105*10)||(time/10==150*10)){
            fisac_15.start();
        }
        if((time/10==30*10)||(time/10==75*10)||(time/10==120*10)||(time/10==165*10)){
            fisac_30.start();
        }
        if((time/10==45*10)||(time/10==90*10)||(time/10==135*10)){
            fisac_switch.start();
        }
        if(time/10==180*10){
            fisac_beep.start();
        }
    }
    public void runFisac2x60(long time){
        if((time/10==15*10)||(time/10==75*10)){
            fisac_15.start();
        }
        if((time/10==30*10)||(time/10==90*10)){
            fisac_30.start();
        }
        if((time/10==45*10)||(time/10==105*10)){
            fisac_45.start();
        }
        if(time/10==60*10){
            fisac_switch.start();
        }
        if(time/10==120*10){
            fisac_beep.start();
        }
    }
    public void runFisac4x30(long time){
        if((time/10==10*10)||(time/10==40*10)||(time/10==70*10)||(time/10==100*10)){
            fisac_10.start();
        }
        if((time/10==20*10)||(time/10==50*10)||(time/10==80*10)||(time/10==110*10)){
            fisac_20.start();
        }
        if((time/10==30*10)||(time/10==60*10)||(time/10==90*10)){
            fisac_switch.start();
        }
        if(time/10==120*10){
            fisac_beep.start();
        }
    }



}
