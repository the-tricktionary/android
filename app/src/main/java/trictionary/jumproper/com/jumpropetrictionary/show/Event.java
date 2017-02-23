package trictionary.jumproper.com.jumpropetrictionary.show;

/**
 * Created by jumpr_000 on 6/6/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author jumpr_000
 */
public abstract class Event {

    //other methods
    public abstract String toString();
    public abstract String getName();
    public abstract ArrayList<String>separateNames();
    public abstract void setEvent(String myEvent);
    public abstract String getEvent();
    public abstract void setName(String myName);


}

