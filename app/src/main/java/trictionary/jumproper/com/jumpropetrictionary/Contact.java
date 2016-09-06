package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr on 9/6/2016.
 */
public class Contact {
    private String name;
    private String type;
    private String desc;

    public Contact(){
        this.name="Test";
        this.type="General";
        this.desc="This is a test";
    }

    public Contact(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }
}
