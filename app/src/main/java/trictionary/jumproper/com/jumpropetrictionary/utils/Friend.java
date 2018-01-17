package trictionary.jumproper.com.jumpropetrictionary.utils;

/**
 * Created by jumpr on 1/13/2018.
 */

public class Friend {
    private String uId;
    private String[] name;
    private String imageUrl;
    private boolean approved;

    public Friend(){
        this.uId = "";
        this.name[0] = "Guest";
        this.name[1] = "";
        this.imageUrl = "";
        this.approved = false;
    }

    public Friend(String uId, String[] name, String imageUrl, boolean approved) {
        this.uId = uId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.approved = approved;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
