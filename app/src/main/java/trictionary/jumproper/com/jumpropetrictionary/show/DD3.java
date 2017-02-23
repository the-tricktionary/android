package trictionary.jumproper.com.jumpropetrictionary.show;

/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public class DD3 extends Event {
    //fields
    private String names;
    private String event="Double Dutch Singles";
    //constructors
    public DD3(String myName){
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
    public static ArrayList<DD3> makeObjects(ArrayList<String> names){
        ArrayList<DD3> objects= new ArrayList<>();
        for (int j=0;j<names.size();j++){
            objects.add(new DD3 (names.get(j)));
        }
        return objects;
    }
    public  ArrayList<String>separateNames(){
        ArrayList<String>listNames= new ArrayList<>();
        for(String name:names.split(" ",0))
            listNames.add(name);
        return listNames;
    }
}
