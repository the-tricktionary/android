package trictionary.jumproper.com.jumpropetrictionary.utils;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jumpr_000 on 4/4/2015.
 */
public class Trick extends FirebaseInstanceIdService {
    //fields
    public String name;
    public String description;
    public int difficulty;
    public int index;
    public String videoCode;
    public String type;
    public String[] prereqs;
    public int[] prereqsId0;
    public int[] prereqsId1;
    public int[] nextTricksId0;
    public int[] nextTricksId1;
    public String fisacLevel="";
    public String wjrLevel="";
    public int id1;
    public int id0;
    public String deName;
    public String deDescription;
    public String svName;
    public String svDescription;
    public boolean completed=false;
    public boolean checklist=false;


    //constructors
    public Trick(){

        name="";
        description="";
        difficulty=0;
        index=-1;
        type="";
    }
    public Trick(String myName){
        name=myName;
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        prereqs=new String[0];
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, String[]myPrereqs){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        prereqs=myPrereqs;
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, String[]myPrereqs, String mFisacLevel){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        prereqs=myPrereqs;
        fisacLevel=mFisacLevel;
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, String[]myPrereqs, String mFisacLevel, String mWjrLevel,int mId1){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        prereqs=myPrereqs;
        fisacLevel=mFisacLevel;
        wjrLevel=mWjrLevel;
        id1=mId1;
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, String mFisacLevel, String mWjrLevel,int mId1){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        fisacLevel=mFisacLevel;
        wjrLevel=mWjrLevel;
        id1=mId1;
    }
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, Trick[]myPrereqs, String mFisacLevel, String mWjrLevel,int mId1){
        name=myName;
        description=myDescription;
        difficulty=myDifficulty;
        index=myIndex;
        type=myType;
        videoCode=myVideoCode;
        prereqs=new String[myPrereqs.length];
        prereqsId0=new int[myPrereqs.length];
        prereqsId1=new int[myPrereqs.length];
        for(int j=0;j<myPrereqs.length;j++){
            prereqs[j]=myPrereqs[j].getName();
            prereqsId0[j]=myPrereqs[j].getId0();
            prereqsId1[j]=myPrereqs[j].getId1();
        }
        fisacLevel=mFisacLevel;
        wjrLevel=mWjrLevel;
        id1=mId1;
    }

    //getters and setters
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getType(){
        return type;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public int getIndex(){
        return index;
    }
    public String getVideoCode(){
        return videoCode;
    }
    public void setName(String myName){
        name=myName;
    }
    public void setType(String myType){
        type=myType;
    }
    public String[] getPrereqs(){
        return prereqs;
    }

    public void setPrereqs(Trick[] prereqs) {
        this.prereqs=new String[prereqs.length];
        for(int j=0;j<prereqs.length;j++) {
            this.prereqs[j] = prereqs[j].getName();
        }
    }

    public void setDescription(String myDescription){
        description=myDescription;
    }
    public void setDifficulty(int myDifficulty){
        difficulty=myDifficulty;
    }
    public void setIndex(int myIndex){
        index=myIndex;
    }
    public String getFisacLevel() {
        return fisacLevel;
    }
    public void setFisacLevel(String fisacLevel) {
        this.fisacLevel = fisacLevel;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId0(){
        return difficulty-1;
    }

    public void setId0(int id0){ this.id0 = id0; }

    public String getWjrLevel() {
        return wjrLevel;
    }

    public void setWjrLevel(String wjrLevel) {
        this.wjrLevel = wjrLevel;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isChecklist() {
        return checklist;
    }

    public void setChecklist(boolean checklist) {
        this.checklist = checklist;
    }

    public int[] getPrereqsId0() {
        return prereqsId0;
    }

    public void setPrereqsId0(int[] prereqsId0) {
        this.prereqsId0 = prereqsId0;
    }

    public int[] getPrereqsId1() {
        return prereqsId1;
    }

    public void setPrereqsId1(int[] prereqsId1) {
        this.prereqsId1 = prereqsId1;
    }

    public int[] getNextTricksId0() {
        return nextTricksId0;
    }

    public void setNextTricksId0(int[] nextTricksId0) {
        this.nextTricksId0 = nextTricksId0;
    }

    public int[] getNextTricksId1() {
        return nextTricksId1;
    }

    public void setNextTricksId1(int[] nextTricksId1) {
        this.nextTricksId1 = nextTricksId1;
    }
}
