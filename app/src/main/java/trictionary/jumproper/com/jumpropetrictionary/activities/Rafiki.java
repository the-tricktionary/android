package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import trictionary.jumproper.com.jumpropetrictionary.R;

public class Rafiki extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rafiki);
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }
    public void openFacebook(View v){
        String url = "https://www.facebook.com/Rafiki-Outreach-Program-1105234322897486/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void sendEmail(View v){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","rhodyropers@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rafiki Outreach");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
