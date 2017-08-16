package trictionary.jumproper.com.jumpropetrictionary.speed;

import java.util.ArrayList;

/**
 * Created by jumpr_000 on 5/8/2016.
 */
public class SpeedData {
    ArrayList<Long> graphData;
    double avgJumps;
    double maxJumps;
    int misses;
    int noMissScore;
    int score;
    int time=0;
    int jumpsLost;
    String name;
    String event;


    public SpeedData(){
        graphData=new ArrayList<Long>();
        avgJumps=0;
        maxJumps=0;
        misses=0;
        noMissScore=0;
        score=0;
        time=0;
        jumpsLost=0;
    }
    public SpeedData(ArrayList<Long> mGraphData, double mAvgJumps, double mMaxJumps, int mMisses,
                     int mNoMissScore, int mScore, int mTime, int mJumpsLost){
        graphData=mGraphData;
        avgJumps=mAvgJumps;
        maxJumps=mMaxJumps;
        misses=mMisses;
        noMissScore=mNoMissScore;
        score=mScore;
        time=mTime;
        jumpsLost=mJumpsLost;
    }
    public SpeedData(ArrayList<Long> mGraphData, double mAvgJumps, double mMaxJumps, int mMisses,
                     int mNoMissScore, int mScore, int mTime, int mJumpsLost, String mName){
        graphData=mGraphData;
        avgJumps=mAvgJumps;
        maxJumps=mMaxJumps;
        misses=mMisses;
        noMissScore=mNoMissScore;
        score=mScore;
        time=mTime;
        jumpsLost=mJumpsLost;
        name=mName;
    }

    public SpeedData(ArrayList<Long> graphData, double avgJumps, double maxJumps, int misses, int noMissScore, int score, int time, int jumpsLost, String name, String event) {
        this.graphData = graphData;
        this.avgJumps = avgJumps;
        this.maxJumps = maxJumps;
        this.misses = misses;
        this.noMissScore = noMissScore;
        this.score = score;
        this.time = time;
        this.jumpsLost = jumpsLost;
        this.name = name;
        this.event = event;
    }

    public ArrayList<Long> getGraphData() {
        return graphData;
    }

    public void setGraphData(ArrayList<Long> graphData) {
        this.graphData=graphData;
    }

    public double getAvgJumps() {
        return avgJumps;
    }

    public void setAvgJumps(double avgJumps) {
        this.avgJumps = avgJumps;
    }

    public double getMaxJumps() {
        return maxJumps;
    }

    public void setMaxJumps(double maxJumps) {
        this.maxJumps = maxJumps;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public int getNoMissScore() {
        return noMissScore;
    }

    public void setNoMissScore(int noMissScore) {
        this.noMissScore = noMissScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getJumpsLost() {
        return jumpsLost;
    }

    public void setJumpsLost(int jumpsLost) {
        this.jumpsLost = jumpsLost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEventFromString(String eventName){
        if(eventName.equals("FISAC 1x30")){
            this.setEvent("srss");
        }
        if(eventName.equals("FISAC 1x180")){
            this.setEvent("srss");
        }
        if(eventName.equals("FISAC 4x45")){
            this.setEvent("ddfs");
        }
        if(eventName.equals("FISAC 2x60")){
            this.setEvent("ddfs");
        }
        if(eventName.equals("FISAC 4x30")){
            this.setEvent("srfs");
        }
        if(eventName.equals("WJR 1x30")){
            this.setEvent("srss");
        }
        if(eventName.equals("WJR 1x180")){
            this.setEvent("srss");
        }
        if(eventName.equals("WJR 2x30")){
            this.setEvent("srps");
        }
        if(eventName.equals("WJR 4x30")){
            this.setEvent("srfs");
        }
        if(eventName.equals("WJR 3x40")){
            this.setEvent("ddts");
        }
        if(eventName.equals("WJR 2x60")){
            this.setEvent("ddfs");
        }
        if(eventName.equals("USAJR 1x30")){
            this.setEvent("srss");
        }
        if(eventName.equals("USAJR Power")){
            this.setEvent("srsp");
        }
        if(eventName.equals("USAJR 1x60")){
            this.setEvent("srss");
        }
        if(eventName.equals("USAJR 1x180")){
            this.setEvent("srss");
        }
        if(eventName.equals("USAJR 4x30")){
            this.setEvent("srfs");
        }
        if(eventName.equals("USAJR 3x40")){
            this.setEvent("ddts");
        }
        if(eventName.equals("USAJR 2x60")){
            this.setEvent("ddfs");
        }
    }

    public String toString(){
        return score+" jumps - "+time+" seconds";
    }


}
