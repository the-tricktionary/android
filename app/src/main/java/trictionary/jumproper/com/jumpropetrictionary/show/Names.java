package trictionary.jumproper.com.jumpropetrictionary.show;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.activities.BaseActivity;
import trictionary.jumproper.com.jumpropetrictionary.activities.ShowMainActivity;


public class Names extends BaseActivity {
    EditText nameInput;
    public static ArrayList<String>showNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        nameInput=(EditText)findViewById(R.id.name_input);
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.setBackButton();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void selectNames(View v){
        showNames = getNames(nameInput.getText().toString()+","); //create list of names
        Collections.sort(showNames);
        Intent intent = new Intent(this, ShowMainActivity.class);
        startActivity(intent);
    }
    public static ArrayList<String> getNames(String input) {

        ArrayList<String> names = new ArrayList<String>();
        if (input.length() <= 1) {
            return names;
        }
        int pos = 0;
        for (int j = 0; j < input.length(); j++) {
            if (input.charAt(j) == ',') {
                if ((input.charAt(pos) == ' ') || (input.charAt(pos) == ',')) {
                    if(pos<input.length()) {
                        names.add(input.substring(pos + 1, j));
                    }
                } else {
                    names.add(input.substring(pos, j));
                }
                pos = j + 1;
            }
        }
        for(int j=0;j<names.size();j++){
            if (names.get(j).charAt(0)==' '){
                names.set(j,names.get(j).substring(1));
            }
            if (names.get(j).charAt(names.get(j).length()-1)==' '){
                names.set(j,names.get(j).substring(0,names.get(j).length()-1));
            }
        }
        return names;
    }

    public void back(View v){
        finish();
    }

}
