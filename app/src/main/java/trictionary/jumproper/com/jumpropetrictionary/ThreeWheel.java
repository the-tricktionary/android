package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public class ThreeWheel extends Event{
    //fields
    private String names;
    private String event="Three Wheel";
    //constructors
    public ThreeWheel(String myName){
        names=myName;
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
    public static ArrayList<ThreeWheel> makeObjects(ArrayList<String> names){
        ArrayList<ThreeWheel> objects=new ArrayList<ThreeWheel>();
        for (int j=0;j<names.size();j++){
            objects.add(new ThreeWheel (names.get(j)));
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

