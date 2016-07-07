package trictionary.jumproper.com.jumpropetrictionary;
/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public class Individual extends Event{
    //fields
    private String names;
    private String event="Individual";
    //constructors
    public Individual(){
        names="";
    }
    public Individual(String myName){
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
    public static ArrayList<Individual> makeObjects(ArrayList<String> names){
        ArrayList<Individual> objects= new ArrayList<>();

        for (int j=0;j<names.size();j++){
            Individual temp=new Individual();
            temp.names=names.get(j);
            objects.add(temp);
        }
        return objects;
    }
    public ArrayList<String>separateNames(){
        ArrayList<String>listNames= new ArrayList<>();

        listNames.add(names);
        return listNames;
    }

}
