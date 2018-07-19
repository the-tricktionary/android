package trictionary.jumproper.com.jumpropetrictionary.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;


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
    public ArrayList<Integer> nextTricksId0;
    public ArrayList<Integer> nextTricksId1;


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

    public void setPrereqs(String[] prereqs) {
        this.prereqs=prereqs;
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

    public ArrayList<Integer> getNextTricksId0() {
        return nextTricksId0;
    }

    public void setNextTricksId0(ArrayList<Integer> nextTricksId0) {
        this.nextTricksId0 = nextTricksId0;
    }

    public ArrayList<Integer> getNextTricksId1() {
        return nextTricksId1;
    }

    public void setNextTricksId1(ArrayList<Integer> nextTricksId1) {
        this.nextTricksId1 = nextTricksId1;
    }
    public void setPrereqIds(DataSnapshot trick){
        ArrayList<Integer>id0s=new ArrayList<>();
        ArrayList<Integer>id1s=new ArrayList<>();
        ArrayList<String> names=new ArrayList<>();
        for(DataSnapshot prereq:trick.child("prerequisites").getChildren()){
            if(prereq.hasChild("id0")&&prereq.hasChild("id1")&&prereq.hasChild("name")) {
                id0s.add(Integer.parseInt(prereq.child("id0").getValue().toString()));
                id1s.add(Integer.parseInt(prereq.child("id1").getValue().toString()));
                names.add(prereq.child("name").getValue().toString());
            }
            else{
                names.add("None");
            }
        }
        int[] id0Arr=new int[id0s.size()];
        int[] id1Arr=new int[id1s.size()];
        String[]prereqs=new String[names.size()];
        for(int j=0;j<id0s.size();j++){
            id0Arr[j]=id0s.get(j);
        }
        for(int j=0;j<id1s.size();j++){
            id1Arr[j]=id1s.get(j);
        }
        for(int j=0;j<names.size();j++){
            prereqs[j]=names.get(j);
        }
        this.setPrereqsId0(id0Arr);
        this.setPrereqsId1(id1Arr);
        this.setPrereqs(prereqs);
    }
    public String toString(){
        return this.name;
    }
}
