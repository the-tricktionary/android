package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr on 9/6/2016.
 */
public class Contact {
    private String name;
    private String type;
    private String desc;
    private String[]replies;

    public Contact(){
        this.name="Test";
        this.type="General";
        this.desc="This is a test";
        replies=new String[0];
    }

    public Contact(String name, String type, String desc) {
        if(name.length()>0) {
            this.name = name;
        }
        else{
            this.name="Anonymous";
        }
        this.type = type;
        this.desc = desc;
        replies=new String[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
