package trictionary.jumproper.com.jumpropetrictionary.speed;

import android.content.Context;
import android.media.MediaPlayer;

import trictionary.jumproper.com.jumpropetrictionary.R;

/**
 * Created by jumpr_000 on 8/12/2016.
 */
public class SpeedEvent {
    //fields
    private Context context;

    private int duration;
    private String name;
    private boolean wjr=false;
    private boolean usajr=false;

    private MediaPlayer time_10, time_15, time_20, time_30, time_45, time_1min, time_2min,
            time_1x30, time_1x180, time_2x30, time_4x30, time_2x60, time_3x40, time_beep,
            time_switch,

    fisac_1x30, fisac_1x180, fisac_4x45, fisac_2x60, fisac_4x30, fisac_10,
            fisac_15, fisac_20, fisac_30, fisac_45, fisac_1min, fisac_2min,
            fisac_switch, fisac_beep,

    usajr_1x30, usajr_1x30d, usajr_1x60, usajr_1x180, usajr_4x30, usajr_2x60, usajr_3x40,
            usajr_10, usajr_15, usajr_20, usajr_30, usajr_45, usajr_1min, usajr_2min,
            usajr_switch, usajr_beep;

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
        if(mName.equals("WJR 1x30")){
            wjr=true;
            duration=30;
        }
        if(mName.equals("WJR 1x180")){
            wjr=true;
            duration=180;
        }
        if(mName.equals("WJR 2x30")){
            wjr=true;
            duration=60;
        }
        if(mName.equals("WJR 4x30")){
            wjr=true;
            duration=120;
        }
        if(mName.equals("WJR 3x40")){
            wjr=true;
            duration=120;
        }
        if(mName.equals("WJR 2x60")){
            wjr=true;
            duration=120;
        }
        if(mName.equals("USAJR 1x30")){
            usajr=true;
            duration=30;
        }
        if(mName.equals("USAJR Power")){
            usajr=true;
            duration=30;
        }
        if(mName.equals("USAJR 1x60")){
            usajr=true;
            duration=60;
        }
        if(mName.equals("USAJR 1x180")){
            usajr=true;
            duration=180;
        }
        if(mName.equals("USAJR 4x30")){
            usajr=true;
            duration=120;
        }
        if(mName.equals("USAJR 3x40")){
            usajr=true;
            duration=120;
        }
        if(mName.equals("USAJR 2x60")){
            usajr=true;
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

    public boolean isUsajr() {
        return usajr;
    }

    public void setUsajr(boolean usajr) {
        this.usajr = usajr;
    }

    //other methods
    public void initTimingTracks(){
        time_10 = MediaPlayer.create(context, R.raw.time_10);
        time_15 = MediaPlayer.create(context,R.raw.time_15);
        time_20= MediaPlayer.create(context,R.raw.time_20);
        time_30= MediaPlayer.create(context,R.raw.time_30);
        time_45= MediaPlayer.create(context,R.raw.time_45);
        time_1min= MediaPlayer.create(context,R.raw.time_1min);
        time_2min = MediaPlayer.create(context,R.raw.time_2min);
        time_1x30= MediaPlayer.create(context,R.raw.time_1x30);
        time_1x180= MediaPlayer.create(context,R.raw.time_1x180);
        time_2x30= MediaPlayer.create(context,R.raw.time_2x30);
        time_4x30= MediaPlayer.create(context,R.raw.time_4x30);
        time_3x40= MediaPlayer.create(context,R.raw.time_3x40);
        time_2x60= MediaPlayer.create(context,R.raw.time_2x60);
        time_beep= MediaPlayer.create(context,R.raw.time_beep);
        time_switch= MediaPlayer.create(context,R.raw.time_switch);

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

        usajr_1x30=MediaPlayer.create(context,R.raw.usajr_1x30);
        usajr_1x30d=MediaPlayer.create(context,R.raw.usajr_1x30d);
        usajr_1x60=MediaPlayer.create(context,R.raw.usajr_1x60);
        usajr_1x180=MediaPlayer.create(context,R.raw.usajr_1x180);
        usajr_2x60=MediaPlayer.create(context,R.raw.usajr_2x60);
        usajr_3x40=MediaPlayer.create(context,R.raw.usajr_3x40);
        usajr_4x30=MediaPlayer.create(context,R.raw.usajr_4x30);
        usajr_10=MediaPlayer.create(context,R.raw.usajr_10);
        usajr_15=MediaPlayer.create(context,R.raw.usajr_15);
        usajr_20=MediaPlayer.create(context,R.raw.usajr_20);
        usajr_30=MediaPlayer.create(context,R.raw.usajr_30);
        usajr_45=MediaPlayer.create(context,R.raw.usajr_45);
        usajr_1min=MediaPlayer.create(context,R.raw.usajr_1min);
        usajr_2min=MediaPlayer.create(context,R.raw.usajr_2min);
        usajr_switch=MediaPlayer.create(context,R.raw.usajr_switch);
        usajr_beep=MediaPlayer.create(context,R.raw.usajr_beep);
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
        if (this.getName().equals("WJR 1x30")){
            this.runWjr1x30(time);
        }
        if (this.getName().equals("WJR 1x180")){
            this.runWjr1x180(time);
        }
        if (this.getName().equals("WJR 2x30")){
            this.runWjr2x30(time);
        }
        if (this.getName().equals("WJR 4x30")){
            this.runWjr4x30(time);
        }
        if (this.getName().equals("WJR 3x40")){
            this.runWjr3x40(time);
        }
        if (this.getName().equals("WJR 2x60")){
            this.runWjr2x60(time);
        }
        if (this.getName().equals("USAJR 1x30")){
            this.runUsajr1x30(time);
        }
        if (this.getName().equals("USAJR Power")){
            this.runUsajr1x30(time);
        }
        if (this.getName().equals("USAJR 1x60")){
            this.runUsajr1x60(time);
        }
        if (this.getName().equals("USAJR 1x180")){
            this.runUsajr1x180(time);
        }
        if (this.getName().equals("USAJR 4x30")){
            this.runUsajr4x30(time);
        }
        if (this.getName().equals("USAJR 3x40")){
            this.runUsajr3x40(time);
        }
        if (this.getName().equals("USAJR 2x60")){
            this.runUsajr2x60(time);
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

    public void runWjr1x30(long time){
        if(time/10==10*10){
            time_10.start();

        }
        if(time/10==20*10){
            time_20.start();
        }
        if(time/10==30*10){
            time_beep.start();
        }
    }
    public void runWjr1x180(long time){
        if(time/10==15*10){
            time_15.start();
        }
        if(time/10==30*10){
            time_30.start();
        }
        if(time/10==45*10){
            time_45.start();
        }
        if(time/10==60*10){
            time_1min.start();
        }
        if(time/10==75*10){
            time_1min.start();
            time_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_15.start();
                }
            });
        }
        if(time/10==90*10){
            time_1min.start();
            time_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_30.start();
                }
            });
        }
        if(time/10==105*10){
            time_1min.start();
            time_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_45.start();
                }
            });
        }
        if(time/10==120*10){
            time_2min.start();
        }
        if(time/10==135*10){
            time_2min.start();
            time_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_15.start();
                }
            });
        }
        if(time/10==150*10){
            time_2min.start();
            time_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_30.start();
                }
            });
        }
        if(time/10==165*10){
            time_2min.start();
            time_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    time_45.start();
                }
            });
        }
        if(time/10==180*10){
            time_beep.start();
        }
    }
    public void runWjr2x30(long time){
        if((time/10==10*10)||(time/10==40*10)){
            time_10.start();
        }
        if((time/10==20*10)||(time/10==50*10)){
            time_20.start();
        }
        if(time/10==30*10){
            time_switch.start();
        }
        if(time/10==60*10){
            time_beep.start();
        }
    }
    public void runWjr4x30(long time){
        if((time/10==10*10)||(time/10==40*10)||(time/10==70*10)||(time/10==100*10)){
            time_10.start();
        }
        if((time/10==20*10)||(time/10==50*10)||(time/10==80*10)||(time/10==110*10)){
            time_20.start();
        }
        if((time/10==30*10)||(time/10==60*10)||(time/10==90*10)){
            time_switch.start();
        }
        if(time/10==120*10){
            time_beep.start();
        }
    }
    public void runWjr3x40(long time){
        if((time/10==10*10)||(time/10==50*10)||(time/10==90*10)){
            time_10.start();
        }
        if((time/10==20*10)||(time/10==60*10)||(time/10==100*10)){
            time_20.start();
        }
        if((time/10==30*10)||(time/10==70*10)||(time/10==110*10)){
            time_30.start();
        }
        if((time/10==40*10)||(time/10==80*10)){
            time_switch.start();
        }
        if(time/10==120*10){
            time_beep.start();
        }
    }
    public void runWjr2x60(long time){
        if((time/10==15*10)||(time/10==75*10)){
            time_15.start();
        }
        if((time/10==30*10)||(time/10==90*10)){
            time_30.start();
        }
        if((time/10==45*10)||(time/10==105*10)){
            time_45.start();
        }
        if(time/10==60*10){
            time_switch.start();
        }
        if(time/10==120*10){
            time_beep.start();
        }
    }

    public void runUsajr1x30(long time){
        if(time/10==10*10){
            usajr_10.start();

        }
        if(time/10==20*10){
            usajr_20.start();
        }
        if(time/10==30*10){
            usajr_beep.start();
        }
    }
    public void runUsajr1x60(long time){
        if((time/10==15*10)||(time/10==75*10)){
            usajr_15.start();
        }
        if((time/10==30*10)||(time/10==90*10)){
            usajr_30.start();
        }
        if((time/10==45*10)||(time/10==105*10)){
            usajr_45.start();
        }
        if(time/10==60*10){
            usajr_beep.start();
        }
    }
    public void runUsajr1x180(long time){
        if(time/10==15*10){
            usajr_15.start();
        }
        if(time/10==30*10){
            usajr_30.start();
        }
        if(time/10==45*10){
            usajr_45.start();
        }
        if(time/10==60*10){
            usajr_1min.start();
        }
        if(time/10==75*10){
            usajr_1min.start();
            usajr_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_15.start();
                }
            });
        }
        if(time/10==90*10){
            usajr_1min.start();
            usajr_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_30.start();
                }
            });
        }
        if(time/10==105*10){
            usajr_1min.start();
            usajr_1min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_45.start();
                }
            });
        }
        if(time/10==120*10){
            usajr_2min.start();
        }
        if(time/10==135*10){
            usajr_2min.start();
            usajr_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_15.start();
                }
            });
        }
        if(time/10==150*10){
            usajr_2min.start();
            usajr_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_30.start();
                }
            });
        }
        if(time/10==165*10){
            usajr_2min.start();
            usajr_2min.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    usajr_45.start();
                }
            });
        }
        if(time/10==180*10){
            usajr_beep.start();
        }
    }
    public void runUsajr4x30(long time){
        if((time/10==10*10)||(time/10==40*10)||(time/10==70*10)||(time/10==100*10)){
            usajr_10.start();
        }
        if((time/10==20*10)||(time/10==50*10)||(time/10==80*10)||(time/10==110*10)){
            usajr_20.start();
        }
        if((time/10==30*10)||(time/10==60*10)||(time/10==90*10)){
            usajr_switch.start();
        }
        if(time/10==120*10){
            usajr_beep.start();
        }
    }
    public void runUsajr3x40(long time){
        if((time/10==10*10)||(time/10==50*10)||(time/10==90*10)){
            usajr_10.start();
        }
        if((time/10==20*10)||(time/10==60*10)||(time/10==100*10)){
            usajr_20.start();
        }
        if((time/10==30*10)||(time/10==70*10)||(time/10==110*10)){
            usajr_30.start();
        }
        if((time/10==40*10)||(time/10==80*10)){
            usajr_switch.start();
        }
        if(time/10==120*10){
            usajr_beep.start();
        }
    }
    public void runUsajr2x60(long time){
        if((time/10==15*10)||(time/10==75*10)){
            usajr_15.start();
        }
        if((time/10==30*10)||(time/10==90*10)){
            usajr_30.start();
        }
        if((time/10==45*10)||(time/10==105*10)){
            usajr_45.start();
        }
        if(time/10==60*10){
            usajr_switch.start();
        }
        if(time/10==120*10){
            usajr_beep.start();
        }
    }




}
