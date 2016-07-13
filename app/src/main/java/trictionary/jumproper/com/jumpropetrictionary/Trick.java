package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr_000 on 4/4/2015.
 */
public class Trick {
    //fields
    public String name;
    public String description;
    public int difficulty;
    public int index;
    public String videoCode;
    public String type;
    public String[] prereqs;
    public String fisacLevel="";



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

}
