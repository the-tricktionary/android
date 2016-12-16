package trictionary.jumproper.com.jumpropetrictionary;

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

    public String toString(){
        return score+" jumps - "+time+" seconds";
    }


}
