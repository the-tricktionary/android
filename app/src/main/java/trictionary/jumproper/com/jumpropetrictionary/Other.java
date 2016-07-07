package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public class Other extends Event {
    //fields
    private String names;
    private String event;
    //constructors
    public Other(String myName, String myEvent){
        names=myName;
        event=myEvent;
    }
    //getters and setters
    public String getName(){
        return names;
    }
    public void setName(String myName){
        names=myName;
    }
    public String getEvent(){
        return event;
    }
    public void setEvent(String myEvent){
        event=myEvent;
    }
    //other methods
    public String toString(){
        return event+"; " +names;
    }
    public static ArrayList<Other> makeObjects(ArrayList<String> names){
        ArrayList<Other> objects=new ArrayList<Other>();
        for (int j=0;j<names.size();j++){
            objects.add(new Other (names.get(j).substring(names.get(j).indexOf(";")+1),names.get(j).substring(0,names.get(j).indexOf(";"))));
        }

        return objects;
    }
    public  ArrayList<String>separateNames(){
        ArrayList<String>listNames=new ArrayList<String>();
        for(String name:names.split(" ",0))
            listNames.add(name);

        return listNames;
    }
}

