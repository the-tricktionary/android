package trictionary.jumproper.com.jumpropetrictionary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class ShowMainActivity extends ActionBarActivity implements Runnable {
    TextView eventName;
    Context context;
    Button makeShow;

    static ArrayList<Event> showTemp;
    TextView breaks;
    GridView showList;
    ArrayAdapter mArrayAdapter;
    static int breakEvents;
    static ArrayList<Event> sortedShow;
    String[]nameArray=new String[Names.showNames.size()];
    ArrayList<Individual> individualObjects=new ArrayList<Individual>();
    ArrayList<Pairs> pairsObjects=new ArrayList<Pairs>();
    ArrayList<DD3> dd3Objects=new ArrayList<DD3>();
    ArrayList<DD4> dd4Objects=new ArrayList<DD4>();
    ArrayList<Wheel> wheelObjects=new ArrayList<Wheel>();
    ArrayList<ThreeWheel> threeWheelObjects=new ArrayList<ThreeWheel>();
    ArrayList<Other> otherEventObjects=new ArrayList<Other>();
    String[]events={"Individuals:","Pairs:","Double Dutch Singles:","Double Dutch Pairs:","Wheels:","Three Wheels:","Others:"};
    int eventIndex=0;
    static ArrayList<Event> show;
    int numNames=0;
    String temp="";
    String tempEvent="";
    EditText otherEventName;
    Button addOtherEvent;
    Button lastEvent;
    ShareActionProvider mShareActionProvider;
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_main);
        for(int j=0;j<Names.showNames.size();j++){
            nameArray[j]=Names.showNames.get(j);
        }

        context=getApplicationContext();
        eventName=(TextView)findViewById(R.id.event_text);
        eventName.setText(events[eventIndex]);
        otherEventName=(EditText)findViewById(R.id.other_event_name);
        showList = (GridView) findViewById(R.id.nameList);
        addOtherEvent=(Button)findViewById(R.id.add_other_event);
        lastEvent=(Button)findViewById(R.id.last_event);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, nameArray);
        showList.setAdapter(adapter);
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(eventName.getText().equals("Individuals:")){
                    individualObjects.add(new Individual(nameArray[position]));
                    CharSequence text = "Added "+nameArray[position]+" to the show.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                if(eventName.getText().equals("Pairs:")){
                    if(numNames==0) {
                        temp = nameArray[position];
                        numNames++;
                    }
                    else{
                        pairsObjects.add(new Pairs(temp+" "+nameArray[position]));

                        CharSequence text = "Added "+pairsObjects.get(pairsObjects.size()-1).getName()+" to the show.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        numNames=0;
                        temp="";
                    }

                }
                if(eventName.getText().equals("Double Dutch Singles:")){
                    if(numNames<2) {
                        temp += " "+nameArray[position];
                        numNames++;
                    }
                    else{
                        dd3Objects.add(new DD3(temp+" "+nameArray[position]));

                        CharSequence text = "Added "+dd3Objects.get(dd3Objects.size()-1).getName()+" to the show.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        numNames=0;
                        temp="";
                    }

                }
                if(eventName.getText().equals("Double Dutch Pairs:")){
                    if(numNames<3) {
                        temp += " "+nameArray[position];
                        numNames++;
                    }
                    else{
                        dd4Objects.add(new DD4(temp+" "+nameArray[position]));

                        CharSequence text = "Added "+dd4Objects.get(dd4Objects.size()-1).getName()+" to the show.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        numNames=0;
                        temp="";
                    }

                }
                if(eventName.getText().equals("Wheels:")){
                    if(numNames==0) {
                        temp=nameArray[position];
                        numNames++;
                    }
                    else{
                        wheelObjects.add(new Wheel(temp+" "+nameArray[position]));

                        CharSequence text = "Added "+wheelObjects.get(wheelObjects.size()-1).getName()+" to the show.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        numNames=0;
                        temp="";
                    }

                }
                if(eventName.getText().equals("Three Wheels:")){
                    if(numNames<2) {
                        temp+=" "+nameArray[position];
                        numNames++;
                    }
                    else{
                        threeWheelObjects.add(new ThreeWheel(temp+" "+nameArray[position]));

                        CharSequence text = "Added "+threeWheelObjects.get(threeWheelObjects.size()-1).getName()+" to the show.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        numNames=0;
                        temp="";
                    }

                }
                if(eventName.getText().equals("Others:")){
                    temp+=" "+nameArray[position];

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        //MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        //if (shareItem != null) {
            //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //}

        // Create an Intent to share your content
        setShareIntent();

        return true;
    }
    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "List of names");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Arrays.toString(nameArray));

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
    public void onClick(View v){

    }
    public void run(){

        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);

      /*  individuals = getNames("*Dylan, Ridge, Drew, Sarah, Olivia, Ally,Tobin, Alex, Max, Katie, Brittany, Jeress, Adam,");
        pairs = getNames("Dylan Ridge, Drew Sarah, Olivia Ally, Chris Ryan,Nick Jeff,Brittany Alex, Adam Lindsey, Sean Singyi, Katie Jeress,");
        dd3s = getNames("Dylan Ridge Drew, Olivia Sarah Ally, Kevin Kyle Olivia,Sean Logan Singyi,Bruce Sharon Avery,Yvonne Cal Lori,Katie Stephen Jeress, Nick Robbie Brian, Tobin Alex Max,");
        dd4s = getNames("Dylan Ridge Sean Logan, Olivia Ally Sarah Singyi,Brittany Alex Jacob Katie, Tobin Alex Max Diego,");
        wheels = getNames("Sean Singyi,Dylan Ridge, Sarah Drew, Katie Jeress, Sarah Katie, Ally Olivia,");
        threeWheels = getNames("Dylan Sean Stephen,Olivia Sarah Ally, Sean Singyi Logan,");
        otherEvents=getNames(",");
*/

        int numRestarts = 0;
        int totalNumRestarts=0;
        showTemp = fillShowArray(individualObjects, pairsObjects, dd3Objects, dd4Objects, wheelObjects, threeWheelObjects, otherEventObjects); //returned if sortShow fails to find a show
        breakEvents=3;
        int firstEventIndex=0;
        for(int i=0;i<showTemp.size();i++){
            if(showTemp.get(i).toString().contains("*")){
                showTemp.get(i).setName(showTemp.get(i).getName().substring(1));
                firstEventIndex=i;
            }
        }
        while (showTemp.equals(fillShowArray(individualObjects, pairsObjects, dd3Objects, dd4Objects, wheelObjects, threeWheelObjects, otherEventObjects))) { //runs until sortShow can make a show
            if(showTemp.size()<3){
                break;
            }
            show = new ArrayList<Event>();
            show = sortShow(individualObjects, pairsObjects, dd3Objects, dd4Objects, wheelObjects, threeWheelObjects, otherEventObjects, breakEvents,context, firstEventIndex);
            showTemp=show;
            if (numRestarts > 10) { //number of times to try to make a show before reducing breakEvents
                breakEvents--;
                numRestarts = 0;
                totalNumRestarts++;
            } else {
                numRestarts++;
                totalNumRestarts++;
            }
        }
        sortedShow=show;
        CharSequence text = "It only took "+totalNumRestarts+" tries to sort this show!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


    }
    public void makeShow(View v){

        run();

        Intent intent = new Intent(this, Show.class);
        startActivity(intent);
    }
    /**
     *
     * @param input, a String consisting of names
     * @return an ArrayList<String> containing the names each in different positions
     */
    public static ArrayList<String> getNames(String input) {

        ArrayList<String> names = new ArrayList<String>();
        if (input.length() <= 1) {
            return names;
        }
        int pos = 0;
        for (int j = 0; j < input.length(); j++) {
            if (input.charAt(j) == ',') {
                if ((input.charAt(pos) == ' ') || (input.charAt(pos) == ',')) {
                    names.add(input.substring(pos + 1, j));
                } else {
                    names.add(input.substring(pos, j));
                }
                pos = j + 1;
            }
        }
        return names;
    }

    /**
     *
     * @param individuals A List of Individual objects
     * @param pairs A list of Pairs objects
     * @param dd3 A list of DD3 objects
     * @param dd4 A list of DD4 objects
     * @param wheel A list of Wheel objects
     * @param threeWheel A list of ThreeWheel objects
     * @param otherEvents A list of Other objects
     * @param breakEvents The minimum amount of break events any jumper will have in the final show
     * @return An ArrayList<Event> containing Event objects in an order so that no Event objects is within breakEvent indexes from an Event of the same type or containing the same people.
     * If a show cannot be sorted with the current breakEvents value, it will try again with a reduced breakEvents value.
     * If a show cannot be sorted, the method returns fillShowArray(individuals, pairs, dd3, dd4, wheel, threeWheel, otherEvents), which is the events in the order they were entered.
     */
    public static ArrayList<Event> sortShow(ArrayList<Individual> individuals, ArrayList<Pairs> pairs,
                                            ArrayList<DD3> dd3, ArrayList<DD4> dd4,
                                            ArrayList<Wheel> wheel, ArrayList<ThreeWheel> threeWheel, ArrayList<Other> otherEvents, int breakEvents, Context context, int firstEventIndex) {
        ArrayList<Event> show = fillShowArray(individuals, pairs, dd3, dd4, wheel, threeWheel, otherEvents);
        ArrayList<Event> showSorted = new ArrayList<Event>();
        showSorted.add(show.remove(firstEventIndex));

        Random random = new Random();
        int numBreakEvents = breakEvents;
        int size = show.size();
        long startTime = System.currentTimeMillis();
        while (showSorted.size() < size) { //while the sorted show has less events than the original list of Event objects
            if (System.currentTimeMillis() - startTime > 100) { //after that many hundredths
                System.out.println("Restarted Sort");


                return fillShowArray(individuals, pairs, dd3, dd4, wheel, threeWheel, otherEvents); //original list of Event objects
            }
            Event temp = new Individual();
            if (showSorted.size() == 0) {
                showSorted.add(show.remove(random.nextInt(show.size()))); //adds the first element of show to showSorted
            }
            temp = show.remove(random.nextInt(show.size()));
            if (isSameEvent(numBreakEvents, temp, showSorted)){ //if temp is not the same event type, and does not have the same people in it as the events before it
                showSorted.add(temp); //add it to showSorted
            } else {
                show.add(temp); //otherwise add it back to show
            }
        }
        return showSorted;
    }

    /**
     *
     * @param individuals A List of Individual objects
     * @param pairs A list of Pairs objects
     * @param dd3 A list of DD3 objects
     * @param dd4 A list of DD4 objects
     * @param wheel A list of Wheel objects
     * @param threeWheel A list of ThreeWheel objects
     * @param otherEvents A list of Other objects
     * @return A List containing all of the events in the parameter lists in the order that they appear in those lists
     */
    public static ArrayList<Event> fillShowArray(ArrayList<Individual> individuals, ArrayList<Pairs> pairs,
                                                 ArrayList<DD3> dd3, ArrayList<DD4> dd4,
                                                 ArrayList<Wheel> wheel, ArrayList<ThreeWheel> threeWheel, ArrayList<Other> otherEvents) {
        ArrayList<Event> show = new ArrayList<>();

        for (Event i : individuals) {
            show.add(i);
        }
        for (Event i : pairs) {
            show.add(i);
        }
        for (Event i : dd3) {
            show.add(i);
        }
        for (Event i : dd4) {
            show.add(i);
        }
        for (Event i : wheel) {
            show.add(i);
        }
        for (Event i : threeWheel) {
            show.add(i);
        }
        for (Event i : otherEvents) {
            show.add(i);
        }
        return show;
    }

    /**
     *
     * @param a The current event being sorted
     * @param b The event that comes before a that it will be compared to
     * @return True if Events a and b do not contain any of the same people.
     *         False if Event a and b contain any of the same people.
     */
    public static boolean haveSameNames(Event a, Event b) {
        ArrayList<String> aList = a.separateNames();
        ArrayList<String> bList = b.separateNames();
        for (int j = 0; j < aList.size(); j++) {
            for (int i = 0; i < bList.size(); i++) {
                if (aList.get(j).equals(bList.get(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param n amount of previous indexes in showSorted to compare temp to
     * @param temp current event being sorted in show into showSorted
     * @param showSorted the sorted show so far
     * @return True if temp and all of the Events checked in showSorted are different events
     *        False if temp and any of the Events checked in showSorted contain any of the same names, or are of the same event type
     */
    public static boolean isSameEvent(int n, Event temp, ArrayList<Event> showSorted) {
        int i;
        boolean acc = true;
        if (showSorted.size() < n) {
            i = showSorted.size();
        } else {
            i = n;
        }
        for (int j = 1; j <= i; j++) {
            if (((temp.getEvent().equals(showSorted.get(showSorted.size() - j).getEvent()))) || ((haveSameNames(temp, showSorted.get(showSorted.size() - j))))) {
                acc = false;
            }
        }
        return acc;
    }
    public static String getBreakEvents(){
        String breaks=""+(breakEvents);
        return breaks;
    }
    public void nextEvent(View v){
        eventName.setText(events[eventIndex+1]);
        if(eventIndex!=0){
            lastEvent.setVisibility(View.VISIBLE);
        }
        if (eventName.getText().equals("Others:")){
            otherEventName.setVisibility(View.VISIBLE);
            addOtherEvent.setVisibility(View.VISIBLE);
            findViewById(R.id.next_event).setVisibility(View.GONE);
            findViewById(R.id.review_events).setVisibility(View.VISIBLE);
        }
        eventIndex++;
    }
    public void lastEvent(View v){
        eventName.setText(events[eventIndex - 1]);
        if (eventIndex==0)
            lastEvent.setVisibility(View.GONE);

    }
    public void addOtherEventToShow(View v){
        tempEvent=otherEventName.getText().toString();
        otherEventObjects.add(new Other(temp,tempEvent));
        CharSequence text = "Added "+otherEventObjects.get(otherEventObjects.size()-1).toString()+" to the show.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        tempEvent="";
        temp="";
        otherEventName.setText("");
    }
    public void reviewEvents(View v){
        show=fillShowArray(individualObjects, pairsObjects, dd3Objects, dd4Objects, wheelObjects, threeWheelObjects, otherEventObjects);
        Intent intent = new Intent(this, UnsortedShow.class);
        startActivity(intent);
    }
}
