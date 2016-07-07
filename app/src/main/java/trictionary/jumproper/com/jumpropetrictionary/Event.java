package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public abstract class Event {


    public final String INDIVIDUAL="Individual";
    public final String PAIRS="Pairs";
    public final String DDS="Double Dutch Singles";
    public final String DDP="Double Dutch Pairs";
    public final String BDD="Big Double Dutch";
    //other methods
    public abstract String toString();
    public abstract String getName();
    public abstract ArrayList<String>separateNames();
    public abstract void setEvent(String myEvent);
    public abstract String getEvent();
    public abstract void setName(String myName);


}

