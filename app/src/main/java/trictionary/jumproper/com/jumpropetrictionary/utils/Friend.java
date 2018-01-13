package trictionary.jumproper.com.jumpropetrictionary.utils;

/**
 * Created by jumpr on 1/13/2018.
 */

public class Friend {
    private String uId;
    private String name;

    public Friend(){
        this.uId = "";
        this.name = "Guest";
    }

    public Friend(String uId, String name) {
        this.uId = uId;
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
