package trictionary.jumproper.com.jumpropetrictionary;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jumpr_000 on 6/9/2016.
 */
public class TrickData extends Trick{
    public static Trick[]tricktionary;
    public static String uId="";
    public static ArrayList<Trick>tempList;
    public static Trick mTrick;
    private static final int LEVEL_1=1;
    private static final int LEVEL_2=2;
    private static final int LEVEL_3=3;
    private static final int LEVEL_4=4;
    private static final int LEVEL_5=5;
    private static final String BASICS="Basics";
    private static final String MANIPULATION="Manipulation";
    private static final String MULTIPLES="Multiples";
    private static final String POWER="Power";
    private static final String RELEASES="Releases";
    private static boolean offline=true;

    public static Trick[]getTricktionaryData(){

        tempList=new ArrayList<>();
        if(Tricktionary.completedTricks==null) {
            Tricktionary.completedTricks = new ArrayList<>();
        }

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        if(offline) {
            fb.setPersistenceEnabled(true);
            offline=false;
        }

        DatabaseReference myRef=fb.getReference("tricks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempList.clear();
                int index=0;
                for(DataSnapshot level:dataSnapshot.getChildren()){
                    for(DataSnapshot trick:level.child("subs").getChildren()){
                        mTrick=new Trick(trick.child("name").getValue().toString(),
                                trick.child("description").getValue().toString(),
                                Integer.parseInt(level.child("level").getValue().toString()),
                                index,
                                trick.child("type").getValue().toString(),
                                trick.child("video").getValue().toString(),
                                getPrereqs(trick),trick.child("irsf").getValue().toString(),
                                trick.child("wjr").getValue().toString(),
                                trick.child("id1").getValue().toString());
                        Log.i("id1",mTrick.getId1());



                        tempList.add(mTrick);
                        index++;

                    }
                }

                tricktionary=new Trick[tempList.size()];
                for(int j=0;j<tempList.size();j++){
                    tricktionary[j]=tempList.get(j);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("checklist",databaseError.getMessage().toString()+ " : "+databaseError.getDetails());
                tricktionary=getTricktionaryOffline();
            }
        });


        return tricktionary;
    }

    public static void fillCompletedTricks(){
        if(uId.length()>0 && Tricktionary.completedTricks.size()==0) {
            Tricktionary.completedTricks.clear();
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference checklist=fb.getReference("checklist");
            checklist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(int j=0;j<tricktionary.length;j++) {
                        if (dataSnapshot.child(uId).hasChild(tricktionary[j].getId0())) {
                            if (dataSnapshot.child(uId).child(tricktionary[j].getId0()).hasChild(tricktionary[j].getId1())) {
                                if (dataSnapshot.child(uId)
                                        .child(tricktionary[j].getId0())
                                        .child(tricktionary[j].getId1())
                                        .getValue().toString().equals("true")) {
                                    tricktionary[j].setCompleted(true);
                                    Tricktionary.completedTricks.add(tricktionary[j]);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("checklist", databaseError.getMessage().toString() + " : " + databaseError.getDetails());
                }
            });
        }
    }

    public static Trick[]getTricktionary(){
        if(tricktionary==null){
            return getTricktionaryData();
        }
        else {
            Tricktionary.completedTricks=getCompletedTricks();
            return tricktionary;
        }
    }

    public static ArrayList<Trick>getCompletedTricks(){
        return Tricktionary.completedTricks;
    }

    public static String[] getPrereqs(DataSnapshot trick){
        ArrayList<String>list=new ArrayList<String>();
        for(DataSnapshot prereq:trick.child("prerequisites").getChildren()){
            Log.i("prereqs",prereq.child("name").toString()+":"+prereq.child("name").getValue().toString());
            list.add(""+prereq.child("name").getValue().toString());
        }
        String[] arr=new String[list.size()];
        for(int j=0;j<list.size();j++){
            arr[j]=list.get(j);
        }
        return arr;
    }
    public static int getLen(){
        if(tricktionary!=null) {
            return tricktionary.length;
        }
        else{
            return getTricktionaryData().length;
        }
    }

    public static Trick[] getTricktionaryOffline(){
        Trick doubleBounce=new Trick("Double Bounce","The jumper jumps off of the ground two times for every one turn of the rope.",LEVEL_1,0,BASICS, "ybwRcnfQtIM" );
        Trick singleBounce=new Trick("Single Bounce","The jumper jumps off of the ground one time for every one turn of the rope.", LEVEL_1,1,BASICS, "k_lHyi85OkM");
        Trick sideSwing=new Trick("Side Swing","The jumper brings his or her hands together and swings the rope to either the right or the left.",LEVEL_1,2,BASICS, "20L8nROw-ds");
        String[] crossPrereqs={"Single Bounce"};
        Trick crissCross=new Trick("Criss Cross","The jumper crosses his or her arms over each other while jumping the rope.",LEVEL_1,3,BASICS, "KABMzJ-U-U4",crossPrereqs);
        Trick skier=new Trick("Skier","The jumper does a single bounce while jumping from side to side.",LEVEL_1,4,BASICS, "k2m4hOZ4yog",crossPrereqs);
        Trick bell=new Trick("Bell","The jumper does a single bounce while jumping from front to back.",LEVEL_1,5,BASICS, "8ioiMPfUSNk",crossPrereqs);
        String[] sideSwingJumpPrereqs={"Side Swing","Single Bounce"};
        Trick sideSwingJump=new Trick("Side Swing Jump", "The jumper does a side swing followed by a single bounce.",LEVEL_1,6,BASICS,"mku8GBT6ZeA",sideSwingJumpPrereqs);
        Trick joggingStep=new Trick("Jogging Step","The jumper alternates feet with each jump.",LEVEL_1,7,BASICS,"DhLUZSUs1nw",crossPrereqs);
        String[] sideCrossPrereqs={"Side Swing Jump","Criss Cross"};
        Trick sideSwingCross=new Trick("Side Swing Cross","The jumper does a side swing followed by a cross.",LEVEL_1,8,BASICS,"03D0wOfCVy4",sideCrossPrereqs);
        Trick straddleJump=new Trick("Straddle Jump","The jumper does a single bounce while putting his or her feet apart, then together.",LEVEL_1,9,BASICS,"AkNV6SnNxEw",crossPrereqs);
        Trick xMotion=new Trick("X-Motion", "The jumper does a straddle jump but crosses his or her legs instead of bringing them together.",LEVEL_1,10,BASICS,"7XC7eD4XexA",crossPrereqs);
        String[] toadPrereqs={"Criss Cross"};
        Trick toad=new Trick("Toad","The jumper does a cross with the bottom arm under the inside of his or her opposite leg.",LEVEL_1,11,MANIPULATION,"If-NmjCDYiQ",toadPrereqs);
        Trick as=new Trick("AS","The jumper does a cross with both arms behind his or her knees.",LEVEL_1,12,MANIPULATION,"otzG5Zq7Ybs",toadPrereqs);
        Trick ts=new Trick("TS","The jumper does a cross with both arms behind his or her back.",LEVEL_1,13,MANIPULATION,"zU2U4NPoXMA",toadPrereqs);
        Trick cl=new Trick("CL","The jumper does a cross with one arm behind his or her legs and one arm behind his or her back.",LEVEL_1,14,MANIPULATION,"pXkhg5GegiQ",toadPrereqs);
        String[] awesomeAnniePrereqs={"Crooger","Toad"};
        Trick awesomeAnnie=new Trick("Awesome Annie","The jumper alternates between a crooger and a toad, similar to a regular open cross.",LEVEL_1,15,MANIPULATION,"PmetnIF4a3s",awesomeAnniePrereqs);
        Trick doubleUnder=new Trick("Double Under","The rope swings around the jumper two times for every one jump.",LEVEL_1,16,MULTIPLES,"AHGuLozb-Cs",crossPrereqs);
        Trick openCrossDoubleUnder=new Trick("Double Under Open Cross","The jumper does a double under where the second swing is a cross.",LEVEL_1,17,MULTIPLES,"YuhOH6dFoPo",toadPrereqs);
        Trick crossOpenDoubleUnder=new Trick("Double Under Cross Open","The jumper does a double under where the first swing is a cross.",LEVEL_1,18,MULTIPLES,"TBfOw2PySuI",toadPrereqs);
        Trick crossCrossDoubleUnder=new Trick("Double Under Cross Cross","The jumper does a double under where both swings are crosses.",LEVEL_2,19,MULTIPLES,"KDEtGzW81P4",toadPrereqs);
        String[] doubleSwitchCrossPrereqs={"Switch Cross", "Double Under"};
        Trick switchCrossDoubleUnder=new Trick("Double Under Switch Cross","The jumper does a double under where both swings are crosses with the opposite arm on top for each.",LEVEL_2,20,MULTIPLES,"LGB2S33GmMU",doubleSwitchCrossPrereqs);
        String[] sideOpenDoublePrereqs={"Side Swing Jump","Double Under"};
        Trick sideOpenDoubleUnder=new Trick("Double Under Side Open","The jumper does a double under where the first swing is a side swing.",LEVEL_2,21,MULTIPLES,"zmdcvOabQhQ",sideOpenDoublePrereqs);
        String[] sideCrossDoublePrereqs={"Side Swing Cross","Double Under"};
        Trick sideCrossDoubleUnder=new Trick("Double Under Side Cross","The jumper does a double under where the first swing is a side swing, and the second is a cross.",LEVEL_2,22,MULTIPLES,"LPOBDj3fvvo",sideCrossDoublePrereqs);
        String[] ebDoublePrereqs={"EB","Double Under"};
        Trick ebDoubleUnder=new Trick("Double Under EB","The jumper does an E.B as a double under in one jump.",LEVEL_2,23,MULTIPLES,"FqWwwf_kHA4",ebDoublePrereqs);
        Trick halfTurnForward=new Trick("180 Forward","The jumper brings his or her arms to the side, turns halfways around, then jumps backwards.",LEVEL_1,24,BASICS,"aVw4B14COd0",crossPrereqs);
        Trick halfTurnBackward=new Trick("180 Backward","The jumper brings his or her arms up, turns halfway around, then brings the rope down to jump forwards.",LEVEL_1,25,BASICS,"dnZ5XSjgbuo",crossPrereqs);
        String[] threeSixtyPrereqs={"180 Forward","180 Backward"};
        Trick threeSixty=new Trick("360","The jumper combines the 180 forward and 180 backward to spin all the way around.",LEVEL_1,26,BASICS,"EbqgKGZK_vE",threeSixtyPrereqs);
        Trick fullTurn=new Trick("Full Turn","The jumper brings his or her arms to the side, spins all the way around with the rope in front, then jumps forwards.",LEVEL_1,27,BASICS,"3bOH6E6q0FQ",crossPrereqs);
        String[]fullTwistPrereqs={"Full Turn"};
        Trick fullTwist=new Trick("Full Twist","The jumper does a full turn as a double under in one jump.",LEVEL_2,28,MULTIPLES,"alhKF8nkHd8",fullTwistPrereqs);
        Trick twister=new Trick("Twister","The jumper does a single bounce while rotating his or her hips.",LEVEL_1,29,BASICS,"wOYpU-_Hw50");
        Trick heelToHeel=new Trick("Heel-to-Heel","The jumper does a single bounce while putting his or her heel in front of the other foot.",LEVEL_1, 30, BASICS,"TeHjRTv10Hg");
        Trick toeToToe=new Trick("Toe-to-Toe","The jumper does a single bounce while putting his or her toe behind the other foot.",LEVEL_1,31,BASICS,"FGY084y4z44");
        Trick heelToToe=new Trick("Heel-to-Toe","The jumper does a single bounce while alternating between heel and toe.",LEVEL_1,32,BASICS,"Zs4q4-asvDs");
        Trick kneeCrossover=new Trick("Knee Crossover","The jumper brings his or her leg up, crosses it over the other leg, back up, and back down.",LEVEL_1,33,BASICS,"hOFyc_qlHOs");
        Trick crooger=new Trick("Crooger","The jumper takes a jump with one arm under the inside of his or her leg, then does a cross.",LEVEL_1,34,MANIPULATION,"YMZ0-tMOZEI");
        String[] bcPrereqs={"360"};
        Trick bcTurn=new Trick("BC Turn","The jumper does a 360 with one arm under the outside of his or her leg.",LEVEL_2,35,MANIPULATION,"FY6PD6zCDks",bcPrereqs);
        String[] frogPrereqs={"Frog Fake"};
        Trick frog=new Trick("Frog/Donkey Kick","The jumper kicks into a handstand and jumps the rope as his or her legs snap down.",LEVEL_2,36,POWER,"9JXv35xSmko",frogPrereqs);
        String[]pushupPrereqs={"Pushup Fake"};
        Trick pushup=new Trick("Pushup","The jumper jumps out into a push-up position then jumps the rope while coming up.",LEVEL_2,37,POWER,"lECGyOMead0",pushupPrereqs);
        String[] triplePrereqs={"Double Under"};
        Trick tripleUnder=new Trick("Triple Under","The rope swings around the jumper three times for every one jump.",LEVEL_2,38,MULTIPLES,"NbeTn6WcB1U",triplePrereqs);
        String[] otherTriplePrereqs={"Triple Under","Double Under Side Open","Double Under Side Cross"};
        Trick sideSwingTripleUnder=new Trick("Triple Under Side Side Open","The jumper does a triple under where the first two swings are side swings.",LEVEL_2,39,MULTIPLES,"nbMTOfyYqHk",otherTriplePrereqs);
        Trick sideOpenOpenTripleUnder=new Trick("Triple Under Side Open Open","The jumper does a triple under where the first swing is a side swing.",LEVEL_2,40,MULTIPLES,"uKJkm_Cabds",otherTriplePrereqs);
        Trick sideCrossOpenTripleUnder=new Trick("Triple Under Side Cross Open","The jumper does a triple under where the first swing is a side swing and the second swing is a cross.",LEVEL_3,41,MULTIPLES,"GMON4IZ10xk",otherTriplePrereqs);
        Trick sideOpenCrossTripleUnder=new Trick("Triple Under Side Open Cross","The jumper does a triple under where the first swing is a side swing and the third swing is a cross.",LEVEL_3,42,MULTIPLES,"QVk_r53VILk",otherTriplePrereqs);
        Trick sideCrossCrossTripleUnder=new Trick("Triple Under Side Cross Cross","The jumper does a triple under where the first swing is a side swing and the second and third swings are crosses.",LEVEL_3,43,MULTIPLES,"n4m-b6HB2aU",otherTriplePrereqs);
        String[]snakeAboveHeadPrereqs={"Snake 180"};
        Trick snakeAboveHead=new Trick("Snake Above Head","The jumper does a snake release and spins the rope twice above his or her head.",LEVEL_3,44,RELEASES,"JFzQ1g2UlVY",snakeAboveHeadPrereqs);
        String[]snakeHalfTurnPrereqs={"Snake"};
        Trick snakeHalfTurn=new Trick("Snake 180","The jumper does a snake release and spins halfway around instead of catching the handle.", LEVEL_2,45,RELEASES,"a5XDno6OU-I",snakeHalfTurnPrereqs);
        String[] snakePrereqs={"One Handle Release"};
        Trick snake=new Trick("Snake","The jumper swings the rope around his or her body, releasing one handle, spinning it on the ground, then catching it.",LEVEL_2,46,RELEASES,"Lj07wtx4gnA",snakePrereqs);
        String[] oneHandleHalfTurnPrereqs={"One Handle Release"};
        Trick oneHandleReleaseHalfTurn=new Trick("One Handle Release 180","The jumper does a one handle release from a forward jump with a half turn.",LEVEL_2,47,RELEASES,"T9BrfGZiJBY",oneHandleHalfTurnPrereqs);
        Trick oneHandleRelease=new Trick("One Handle Release","The jumper releases one handle behind his or her feet, then swings it back up and catches it.",LEVEL_1,48,RELEASES,"rweowUw16DQ");
        Trick woundedDuck=new Trick("Wounded Duck","The jumper does a single bounce while turning his or her toes to the inside and outside.",LEVEL_1,49,BASICS,"Z8l8uKJ_CT0");
        String[] doubleSideSwingPrereqs={"Side Swing"};
        Trick doubleSideSwing=new Trick("Double Side Swing","The jumper alternates between an \"open\" and a \"cross\" side swing.",LEVEL_1,50,BASICS,"_coW8Bk1-_w",doubleSideSwingPrereqs);
        String[] switchCrossPrereqs={"Criss Cross"};
        Trick switchCross=new Trick("Switch Cross","The jumper does two criss crosses in a row, switching which arm is on top between jumps.",LEVEL_1,51,MANIPULATION,"IYwNGa92kNc",switchCrossPrereqs);
        String[] fullTwistCrossPrereqs={"Full Twist"};
        Trick fullTwistCross=new Trick("Full Twist Cross","The jumper does a full twist and crosses at the end of the twist.",LEVEL_2,52,MULTIPLES,"iS0QsDcFRHI",fullTwistCrossPrereqs);
        String[] asToClPrereqs={"AS","CL"};
        Trick asToCl=new Trick("AS to CL","A switch cross where the first cross is an AS, and the second a CL.",LEVEL_2,53,MANIPULATION,"qL93G-mxgJY",asToClPrereqs);
        String[]marleyPrereqs={"Full Turn"};
        Trick marley=new Trick("Marley","The jumper does a full turn under his or her leg into a forward side swing toad.",LEVEL_2,54,MANIPULATION,"5H08KxQKv7s",marleyPrereqs);
        String[] knPrereqs={"Toad"};
        Trick kn=new Trick("KN","The jumper does a cross with one arm under the inside of his or her leg, and the other behind his or her head.",LEVEL_2,55,MANIPULATION,"BO9wHbwU2ws",knPrereqs);
        String[]ebPrereqs={"Criss Cross","Side Swing Cross"};
        Trick eb=new Trick("EB","The jumper does a cross with one arm in front of and the other arm behind his or her back.",LEVEL_1,56,MANIPULATION,"eL5eO8SssTE",ebPrereqs);
        String[]caboosePrereqs={"Criss Cross"};
        Trick caboose=new Trick("Caboose","The jumper does a cross between his or her legs, then brings the rope backward.",LEVEL_2,57,MANIPULATION,"e7zeQb7csAE",caboosePrereqs);
        String[]legWrapReleasePrereqs={"One Handle Release"};
        Trick legWrapRelease=new Trick("Leg Wrap Release","The jumper wraps the rope around his or her legs, then releases the front handle, swings it around, up, and catches it.",LEVEL_2,58,RELEASES,"2-PgNR-_xsA",legWrapReleasePrereqs);
        String[] mick360Prereqs={"Mick", "Mick Switch Sides"};
        Trick mick360=new Trick("Mick 360","The jumper does a mick release while doing a 360.",LEVEL_3,59,RELEASES,"-9sqvG4osaU",mick360Prereqs);
        String[] mickSwitchSidesPrereqs={"Mick"};
        Trick mickSwitchSides=new Trick("Mick Switch Sides","The jumper does a mick release and swings the rope to both sides of his or her body before catching the handle.",LEVEL_3,60,RELEASES,"N8dRngmXJbo",mickSwitchSidesPrereqs);
        String[] twoHandleReleasePrereqs={"One Handle Release"};
        Trick twoHandleRelease=new Trick("Two Handle Release","The jumper tosses both handles into the air, letting the rope rotate once before catching both handles.",LEVEL_3,61,RELEASES,"WjPlDVqaWQg",twoHandleReleasePrereqs);
        String[] mickPrereqs={"Snake"};
        Trick mick=new Trick("Mick","The jumper swings the rope to one side, releases one handle, swings the rope two or three times, then catches the handle.",LEVEL_3,62,RELEASES,"jim55moLp0E",mickPrereqs);
        String[] johmmyPrereqs={"Triple Under Side Open Cross","Crooger"};
        Trick johmmy=new Trick("Johmmy","A triple under side swing, crooger, then cross.",LEVEL_3,63,MULTIPLES,"qp7uVwkcpyM",johmmyPrereqs);
        String[] johmmyInversePrereqs={"Johmmy", "Crooger Inverse"};
        Trick johmmyInverse=new Trick("Johmmy Inverse","A triple under side swing, inverse crooger, then cross.", LEVEL_3,64,MULTIPLES,"5C0sii5Vfnk",johmmyInversePrereqs);
        String[] ekPrereqs={"360","Full Twist","Triple Under"};
        Trick ek=new Trick("EK Fulltwist", "A triple under 360 with a jump in the back, and the front.",LEVEL_3,65,MULTIPLES,"3B1lOdyW-ng",ekPrereqs);
        String[] ekBackwardPrereqs={"EK Fulltwist"};
        Trick ekBackward=new Trick("EK Backward","An EK where the first swing is a texas style turn, then a 180, then a jump backwards.",LEVEL_3,66,MULTIPLES,"WUvdFdzdG6M",ekBackwardPrereqs);
        String[] bcFullTwistPrereqs={"BC Turn", "EK FullTwist"};
        Trick bcFullTwist=new Trick("BC FullTwist","An EK where the jumper puts one arm under the outside of his or her leg during the backward jump.",LEVEL_3,67,MULTIPLES,"Z67-4V9tGjE",bcFullTwistPrereqs);
        String[] oneAndAHalfPrereqs={"EK Fulltwist"};
        Trick oneAndAHalf=new Trick("540 (One and a Half)","A fulltwist and a half turn performed all together as a triple under.",LEVEL_3,68,MULTIPLES,"eCertQqG5Ng",oneAndAHalfPrereqs);
        String[]tjPrereqs={"Toad","Triple Under Side Cross Open"};
        Trick tj=new Trick("TJ", "A triple under side swing, then toad, then open.",LEVEL_3,69,MULTIPLES,"6Jwi4UWuRTU",tjPrereqs);
        String[] ebTjPrereqs={"Triple Under EB","TJ"};
        Trick ebTj=new Trick("EB TJ","A triple under EB to Toad, then open.",LEVEL_3,70,MULTIPLES,"E7d1vDH-wQc",ebTjPrereqs);
        String[]ekCrossPrereqs={"EK Fulltwist"};
        Trick ekCross=new Trick("EK Cross","An EK with a cross either in the back, or on the final jump in the front.",LEVEL_3,71,MULTIPLES,"JTRkjzFKP_s",ekCrossPrereqs);
        String[] ekCrossCrossPrereqs={"EK Cross"};
        Trick ekCrossCross=new Trick("EK Cross Cross", "An EK with crosses in both the back and the final jump in the front.",LEVEL_3,72,MULTIPLES,"hrh8TQnctdc",ekCrossCrossPrereqs);
        String[] belchPrereqs={"Frog/Donkey Kick", "Pushup"};
        Trick belch=new Trick("Belch", "The jumper does a frog landing in a pushup position.",LEVEL_3, 73, POWER,"vtPLdqkDzAY",belchPrereqs);
        String[] doubleASPrereqs={"AS","Double Under"};
        Trick doubleAS=new Trick("Double Under AS", "The jumper does an A.S. cross open as a double under.",LEVEL_2,74,MULTIPLES,"5oWcJO7uY_s",doubleASPrereqs);
        String[]duFrogPrereqs={"Two Footed Frog"};
        Trick doubleUnderFrog=new Trick("Double Under Frog","The jumper does a two footed frog and swings the rope one time around before landing on his or her hands.",LEVEL_4,75,POWER,"xhw-yRK1Pb8",duFrogPrereqs);
        String[] ebTjInversePrereqs={"EB TJ","TJ Inverse"};
        Trick ebtjInverse=new Trick("EB TJ Inverse","The jumper does an EB TJ with the front arm going under the outside of his or her leg.",LEVEL_3,76,MULTIPLES,"FXqZmxrdFW4",ebTjInversePrereqs);
        String[] twoFootedFrogPrereqs={"Frog/Donkey Kick"};
        Trick twoFootedFrog=new Trick("Two Footed Frog","The jumper rebounds off of both feet into a frog.",LEVEL_3,77,POWER,"cioB48iYxQo",twoFootedFrogPrereqs);
        String[]kamikazePrereqs={"Pushup","Double Under Pushup"};
        Trick kamikaze=new Trick("Kamikaze","The jumper does a pushup landing back in a pushup.",LEVEL_4,78,POWER,"rKhzsAj5TYw",kamikazePrereqs);
        String[]otherQuadPrereqs={"Quadruple Under"};
        Trick sideCrossEB=new Trick("Quad Side Cross EB","The jumper does a side cross and an EB on opposite sides as a quad.",LEVEL_4,79,MULTIPLES,"pvTO5QLa1io",otherQuadPrereqs);
        Trick sideCrossSideCross=new Trick("Quad Side Cross Side Cross","The jumper does two side crosses on opposite sides as a quad.",LEVEL_4,80,MULTIPLES,"qvfD-72S_QI",otherQuadPrereqs);
        Trick sideOpenSideOpen=new Trick("Quad Side Open Side Open","The jumper does two side opens on opposite sides as a quad.",LEVEL_4,81,MULTIPLES,"Wk8OjOYLewg",otherQuadPrereqs);
        Trick sideSideOpenCross=new Trick("Quad Side Side Open Cross","The jumper does two side swings then open cross as a quad.",LEVEL_4,82,MULTIPLES,"mZ8FU4BhWRY",otherQuadPrereqs);
        Trick sideSideOpenOpen=new Trick("Quad Side Side Open Open","The jumper does two side swings then two opens as a quad.",LEVEL_4,83,MULTIPLES,"oJTVClWyzOc",otherQuadPrereqs);
        String[]quadPrereqs={"Triple Under"};
        Trick quad=new Trick("Quadruple Under","The jumper swings the rope around 4 times in one jump.",LEVEL_4,84,MULTIPLES,"h-VFiCfFnSo",quadPrereqs);
        String[] sunnyDPrereqs={"Frog/Donkey Kick"};
        Trick sunnyD=new Trick("Sunny-D","The jumper does a frog but swings the rope down backwards.",LEVEL_4,85,POWER,"FON7KEeUsJU",sunnyDPrereqs);
        String[] tjInversePrereqs={"TJ","Toad Inverse"};
        Trick tjInverse=new Trick("TJ Inverse","The jumper does a TJ but with an inverse toad.",LEVEL_3,86,MULTIPLES,"ehNh537ZZs4",tjInversePrereqs);
        String[] tjQuadPrereqs={"TJ", "Quadruple Under"};
        Trick tjQuad=new Trick("TJ Quad","The jumper does a TJ with one extra open swing at the end.",LEVEL_4,87,MULTIPLES,"WQY6dYB6fzs",tjQuadPrereqs);
        String[]tripleASPrereqs={"Double Under AS","Triple Under Side Cross Open"};
        Trick tripleAS=new Trick("Triple Under AS","The jumper does a side swing then AS Open as a triple under.",LEVEL_3,88,MULTIPLES,"prZwqUB826k",tripleASPrereqs);
        String[] tripleCLPrereqs={"CL","Triple Under EB"};
        Trick tripleCL=new Trick("Triple Under CL","The jumper does an EB to CL as a triple under.",LEVEL_3,89,MULTIPLES,"qLiWuaC9vZM",tripleCLPrereqs);
        String[] tummytuckPrereqs={"Triple Under Side Cross Open"};
        Trick tummytuck=new Trick("TummyTuck","The jumper does a triple under with one arm behind his or her legs and one arm under that arm.",LEVEL_3,90,MULTIPLES,"_TvmIkzH4i8",tummytuckPrereqs);
        String[]croogerWrapPrereqs={"Crooger","Toad"};
        Trick croogerWrap=new Trick("Crooger Wrap","The jumper jumps a crooger twice, wrapping the rope on his or her ankle and swinging into a toad on the other leg.",LEVEL_4,91,MANIPULATION,"jj4yOpmR-6c",croogerWrapPrereqs);
        String[]croogerWrapInversePrereqs={"Crooger Wrap","Crooger Inverse","T-Toad"};
        Trick croogerWrapInverse=new Trick("Crooger Wrap Inverse","The jumper does a crooger wrap with an inverse crooger, and swings into a t-toad.",LEVEL_4,92,MANIPULATION,"RQEA1HYAcAc",croogerWrapInversePrereqs);
        String[]crossToCrossPrereqs={"Criss Cross","One Handle Release"};
        Trick crossToCross=new Trick("Cross to Cross Floater","The jumper does a cross, releases the bottom handle, then catches it back in a cross.",LEVEL_2,93,RELEASES,"oCN7n-xUUiA",crossToCrossPrereqs);
        String[] ebToCrossPrereqs={"Cross to Cross Floater","E.B."};
        Trick ebToCrossFloater=new Trick("EB to Cross Floater","The jumper does an EB, releases the back handle, then catches it in a cross.",LEVEL_2,94,RELEASES,"CV3RyptvPkI",ebToCrossPrereqs);
        String[] hummingbirdPrereqs={"Quad Wrap Cross"};
        Trick hummingbird=new Trick("Hummingbird","The jumper does an EB wrap with a half turn as a quad.",LEVEL_4,95,MULTIPLES,"1uJjSZbJIvk",hummingbirdPrereqs);
        String[]marteenCaboosePrereqs={"Caboose"};
        Trick marteenCaboose=new Trick("Marteen Caboose","The jumper does a caboose jumping both the cross and the backwards swing in one jump.",LEVEL_3,96,MANIPULATION,"lYyfXZs5Z4w",marteenCaboosePrereqs);
        String[]quadMickPrereqs={"Triple Under Mick"};
        Trick quadMick=new Trick("Quad Mick","The jumper does a mick with three spins then jumps the rope as a quad.",LEVEL_4,97,MULTIPLES,"-iHsqSStuKY",quadMickPrereqs);
        String[]tjEbPrereqs={"TJ","Quad Side Cross EB"};
        Trick tjEb=new Trick("TJ EB","The jumper does a side swing toad and an EB together as a quad.",LEVEL_4,98,MULTIPLES,"-swBl5_N18c",tjEbPrereqs);
        String[]toadWrapPrereqs={"Crooger Wrap","Toad"};
        Trick toadWrap=new Trick("Toad Wrap","The jumper jumps a toad twice, wrapping the rope on his or her ankle and swinging into a crooger on the other leg.",LEVEL_4,99,MANIPULATION,"N2qHIoxzmKQ",toadWrapPrereqs);
        String[] tripleMickPrereqs={"Mick","Triple Under"};
        Trick tripleMick=new Trick("Triple Under Mick","The jumper does a mick with two spins then jump the rope as a triple under.",LEVEL_3,100,MULTIPLES,"ai8mnmuvUNU",tripleMickPrereqs);
        String[] mickUnderLegPrereqs={"Mick"};
        Trick mickUnderLeg=new Trick("Mick Under Leg","The jumper swings the rope under his or her leg and lets go of one handle into a mick.",LEVEL_3,101,RELEASES,"CpcsYUY2gq4",mickUnderLegPrereqs);
        String[] croogerInversePrereqs={"Crooger"};
        Trick croogerInverse=new Trick("Crooger Inverse","The jumper does an open jump with one arm over the outside of his or her leg, and crosses out of it.",LEVEL_2,102,MANIPULATION,"WfRp51wK7Vc",croogerInversePrereqs);
        String[] doubleUnderPushupPrereqs={"Pushup"};
        Trick doubleUnderPushup=new Trick("Double Under Pushup","The jumper does one jump and lands in a push-up position.",LEVEL_3, 103,POWER,"E0nDa8GnnWE",doubleUnderPushupPrereqs);
        String[] elephantPrereqs={"Toad","Toad Inverse"};
        Trick elephant=new Trick("Elephant","The jumper does a cross with both arms crossed under one of his or her legs.",LEVEL_2,104,MANIPULATION,"w7UMwAnzQ48",elephantPrereqs);
        Trick fakePushup=new Trick("Pushup Fake","The jumper jumps out into a push-up position and comes up before jumping the rope.",LEVEL_1,105,POWER,"V5L43EmkW1o");
        String[]toadInversePrereqs={"Toad"};
        Trick toadInverse=new Trick("Toad Inverse","The jumper does a cross with one arm under the outside of one of his or her legs.",LEVEL_2,106,MANIPULATION,"7fWZhrcpaH0",toadInversePrereqs);
        String[]ebTripleUnderPrereqs={"Double Under EB","Triple Under"};
        Trick tripleEB=new Trick("Triple Under EB","The jumper does an EB then an open jump as a triple under.",LEVEL_3,107,MULTIPLES,"3-9AhG4Ziho",ebTripleUnderPrereqs);
        Trick tripleFakeEB=new Trick("Triple Under Fake EB","The jumper does a fake EB then an open jump as a triple under.",LEVEL_3,108,MULTIPLES,"QHLBj4f3gNs",ebTripleUnderPrereqs);
        String[] backwardsDoubleUnderPrereqs={"Backwards Jump","Double Under"};
        Trick backwardsDoubleUnder=new Trick("Backwards Double Under","The jumper swings the rope two times backward in one jump.",LEVEL_2,109,MULTIPLES,"2jfYYCC14cs",backwardsDoubleUnderPrereqs);
        String[] backwardsEbPrereqs={"EB","Backwards Cross"};
        Trick backwardsEb=new Trick("Backwards EB","The jumper does a backwards cross with one arm behind his or her back.",LEVEL_2,110,MANIPULATION,"9h6Z0poPtCY",backwardsEbPrereqs);
        String[] backwardsJumpPrereqs={"Single Bounce"};
        Trick backwardsJump=new Trick("Backwards Jump","The jumper does a single bounce with the rope swinging backwards.",LEVEL_1,111,BASICS,"1kDglhDiaB8",backwardsJumpPrereqs);
        String[] backwardsCrossPrereqs={"Backwards Jump","Criss Cross"};
        Trick backwardsCross=new Trick("Backwards Cross","The jumper does an open cross with the rope swinging backwards.",LEVEL_1,112,MANIPULATION,"_t0RozsY2aQ",backwardsCrossPrereqs);
        String[] backwardsSideCrossPrereqs={"Backwards Side Swing","Backwards Cross","Side Swing Cross"};
        Trick backwardsSideCross=new Trick("Backwards Side Swing Cross","The jumper does a side swing into a cross with the rope swinging backwards.",LEVEL_1,113,MANIPULATION,"fdHNM7hjS6s",backwardsSideCrossPrereqs);
        String[] backwardsSideOpenPrereqs={"Backwards Side Swing","Backwards Jump","Side Swing Jump"};
        Trick backwardsSideOpen=new Trick("Backwards Side Swing Open","The jumper does a side swing into a jump with the rope swinging backwards.",LEVEL_1,114,BASICS,"FaBts6r7_rI",backwardsSideOpenPrereqs);
        String[] backwardsSideSwingPrereqs={"Side Swing"};
        Trick backwardsSideSwing=new Trick("Backwards Side Swing","The jumper swings the rope from side to side with the rope swinging backwards.",LEVEL_1,115,BASICS,"dVrCt_n8WSE",backwardsSideSwingPrereqs);
        Trick fakeFrog=new Trick("Frog Fake","The jumper kicks up into a handstand, snaps down, then jumps the rope.",LEVEL_1,116,POWER,"HjmCr4dwicA");
        String[] frogDoubleUnderPrereqs={"Frog/Donkey Kick"};
        Trick frogDoubleUnder=new Trick("Frog Double Under","The jumper does a frog and swings the rope two times around on the way down.",LEVEL_4,117,POWER,"r_nlicVxhfI",frogDoubleUnderPrereqs);
        String[] mickCatchOnLegPrereqs={"Mick"};
        Trick mickCatchOnLeg=new Trick("Mick Catch on Leg","The jumper does a mick, catches it on his or her leg, and swings it off into another mick.",LEVEL_4,118,RELEASES,"Y6JK44BLXLI",mickCatchOnLegPrereqs);
        String[] pushupDoubleUnderPrereqs={"Pushup"};
        Trick pushupDoubleUnder=new Trick("Pushup Double Under","The jumper does a pushup and swings the rope two times around on the way up.",LEVEL_4,119,POWER,"1epKBV__Spo",pushupDoubleUnderPrereqs);
        Trick roundoffBacktuck=new Trick("Round Off Backtuck","The jumper does a round-off backtuck and swings the rope around more than once.",LEVEL_4,120,POWER,"jLk11mxUzOw");
        String[] standingBacktuckPrereqs={"Round Off Backtuck"};
        Trick standingBacktuck=new Trick("Standing Backtuck","The jumper does a standing backtuck and swings the rope around more than once.",LEVEL_4,121,POWER,"gYtxFsWyUng",standingBacktuckPrereqs);
        String[] tjAsPrereqs={"TJ Quad","AS Quad"};
        Trick tjAs=new Trick("TJ AS","A side swing toad into an AS cross as a quad.",LEVEL_4,122,MULTIPLES,"kPJf7lBzdkc",tjAsPrereqs);
        String[]tToadInversePrereqs={"T Toad","Toad Inverse"};
        Trick tToadInverse=new Trick("T Toad Inverse","The jumper does a cross with the top arm under the outside of his or her leg.",LEVEL_2,123,MANIPULATION,"Yesr_ujlejc",tToadInversePrereqs);
        String[] tToadPrereqs={"Toad"};
        Trick tToad=new Trick("T Toad","The jumper does a cross with the top arm under the inside of his or her leg.",LEVEL_2,124,MANIPULATION,"sbe33OChR3M",tToadPrereqs);

        Trick[] tricktionary={doubleBounce,singleBounce,sideSwing,crissCross,skier,bell,sideSwingJump,joggingStep,sideSwingCross,straddleJump,xMotion,toad,as,ts,cl,awesomeAnnie,
                doubleUnder,openCrossDoubleUnder,crossOpenDoubleUnder,crossCrossDoubleUnder,switchCrossDoubleUnder,sideOpenDoubleUnder,sideCrossDoubleUnder,ebDoubleUnder,
                halfTurnForward,halfTurnBackward,threeSixty,fullTurn,fullTwist,twister,heelToHeel,toeToToe,heelToToe,kneeCrossover,crooger,bcTurn,frog,pushup,tripleUnder,
                sideSwingTripleUnder,sideOpenOpenTripleUnder,sideCrossOpenTripleUnder,sideOpenCrossTripleUnder,sideCrossCrossTripleUnder,snakeAboveHead,snakeHalfTurn,snake,
                oneHandleReleaseHalfTurn,oneHandleRelease,woundedDuck,doubleSideSwing,switchCross,fullTwistCross,asToCl,marley,kn,eb,caboose,legWrapRelease,mick360,mickSwitchSides,
                twoHandleRelease,mick, johmmy,johmmyInverse,ek,ekBackward,bcFullTwist,oneAndAHalf,tj,ebTj,ekCross,ekCrossCross,belch,doubleAS,doubleUnderFrog,ebtjInverse,
                twoFootedFrog,kamikaze,sideCrossEB,sideCrossSideCross,sideOpenSideOpen,sideSideOpenCross,sideSideOpenOpen,quad,sunnyD,tjInverse,tjQuad,tripleAS,tripleCL,
                tummytuck,croogerWrap,croogerWrapInverse,crossToCross,ebToCrossFloater,hummingbird,marteenCaboose,quadMick,tjEb,toadWrap,tripleMick,mickUnderLeg,croogerInverse,
                doubleUnderPushup,elephant,fakePushup,toadInverse,tripleEB,tripleFakeEB,backwardsDoubleUnder,backwardsEb,backwardsJump,backwardsCross,backwardsSideCross,
                backwardsSideOpen,backwardsSideSwing,fakeFrog,frogDoubleUnder,mickCatchOnLeg,pushupDoubleUnder,roundoffBacktuck,standingBacktuck,tjAs,tToadInverse,tToad};

        return tricktionary;

    }
}


