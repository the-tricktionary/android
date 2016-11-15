package trictionary.jumproper.com.jumpropetrictionary;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
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
    public String fisacLevel="";
    public String wjrLevel="";
    public String id1;
    public String id0;
    public boolean completed;


    //constructors
    public Trick(){

        name="";
        description="";
        difficulty=0;
        index=-1;
        type="";
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
    public Trick(String myName,String myDescription,int myDifficulty,int myIndex,String myType, String myVideoCode, String[]myPrereqs, String mFisacLevel, String mWjrLevel,String mId1){
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
        //getMessageToken();
        return prereqs;
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

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId0(){
        return ""+(difficulty-1);
    }

    public void setId0(String id0){ this.id0 = id0; }

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

/**
    public void getMessageToken(){
        Log.e("Token",FirebaseInstanceId.getInstance().getToken());
    }
     **/

}
