package trictionary.jumproper.com.jumpropetrictionary;

/**
 * Created by jumpr on 9/6/2016.
 */
public class Contact {
    private String name;
    private String type;
    private String desc;
    private Reply[]replies;
    private String id1;
    private String id0;
    private String org;
    private String level;
    private String email;

    public Contact(){
        this.name="Test";
        this.type="General";
        this.desc="This is a test";
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
    }
    public Contact(String name, String type, String desc, String id1, String id0) {
        if(name.length()>0) {
            this.name = name;
        }
        else{
            this.name="Anonymous";
        }
        this.type = type;
        this.desc = desc;
        this.id1=id1;
        this.id0=id0;
    }
    public Contact(String name, String type, String desc, String id1, String id0,String email) {
        if(name.length()>0) {
            this.name = name;
        }
        else{
            this.name="Anonymous";
        }
        this.type = type;
        this.desc = desc;
        this.id1=id1;
        this.id0=id0;
        this.email=email;
    }
    public Contact(String name, String type, String desc, String id1, String id0, String org, String level) {
        if(name.length()>0) {
            this.name = name;
        }
        else{
            this.name="Anonymous";
        }
        this.type = type;
        this.desc = desc;
        this.id1=id1;
        this.id0=id0;
        this.org=org;
        this.level=level;
    }
    public Contact(String name, String type, String desc, String id1, String id0, String org, String level,String email) {
        if(name.length()>0) {
            this.name = name;
        }
        else{
            this.name="Anonymous";
        }
        this.type = type;
        this.desc = desc;
        this.id1=id1;
        this.id0=id0;
        this.org=org;
        this.level=level;
        this.email=email;
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

    public Reply[] getReplies() {
        return replies;
    }

    public void setReplies(Reply[] replies) {
        this.replies = replies;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId0() {
        return id0;
    }

    public void setId0(String id0) {
        this.id0 = id0;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString(){
        return this.name+this.id0+this.id1;
    }
}
