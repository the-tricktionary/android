package trictionary.jumproper.com.jumpropetrictionary.contact;

/**
 * Created by jumpr_000 on 9/8/2016.
 */
public class Reply {

    private String name;
    private String reply;

    public Reply(){
        this.name="Anonymous";
        this.reply="Generic reply";
    }

    public Reply(String name, String reply) {
        this.name = name;
        this.reply = reply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String underline(String name){
        String line="";
        for(int j=0;j<name.length();j++){
            line=line+"_";
        }
        return line;
    }

    public String toString(){
        return this.getName()+"\n"+underline(this.getName())+"\n\n\t\t"+this.getReply();
    }
}
