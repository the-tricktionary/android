package trictionary.jumproper.com.jumpropetrictionary;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Submit extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView myVideoView;
    EditText trickName,trickDescription,trickLevel;
    private Uri videoUri;
    Spinner orgSpinner;
    String org;
    String name;
    String description;
    String level;
    private StorageReference storageRef;
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=36;
    private FirebaseAuth mAuth;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int notifyId = 1;
    private long fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        //make sure device has a camera
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(this,"Sorry, you need a camera to submit tricks!", Toast.LENGTH_SHORT).show();
            finish();
        }
        mAuth=FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance(); //get the storage instance
        storageRef = storage.getReferenceFromUrl("gs://project-5641153190345267944.appspot.com/"); // Create a storage reference from our app
        getPermission();

        myVideoView = (VideoView) findViewById(R.id.video_view);
        trickName = (EditText) findViewById(R.id.trick_name);
        trickDescription = (EditText) findViewById(R.id.trick_description);
        trickLevel = (EditText) findViewById(R.id.trick_level);
        orgSpinner=(Spinner)findViewById(R.id.trick_org);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.organizations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orgSpinner.setAdapter(adapter);
        orgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                org=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                org="";
                return;
            }
        });

        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Uploading Trick")
                .setContentText("Upload in progress...")
                .setSmallIcon(R.drawable.icon_notify);
    }
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            myVideoView.setVideoURI(videoUri);
            myVideoView.requestFocus();
            //we also set an setOnPreparedListener in order to know when the video file is ready for playback
            myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    myVideoView.seekTo((int)(Math.random()*myVideoView.getDuration()));
                    myVideoView.start();


                }

            });
        }
    }
    public void startUpload(View v){
        v.setVisibility(View.INVISIBLE);
        name=trickName.getText().toString();
        description=trickDescription.getText().toString();
        level=trickLevel.getText().toString();
        Log.e("Upload","URI "+videoUri.getPath()+" "+videoUri.getAuthority());
        StorageReference uploadRef=storageRef.child("submit").child("trick_"+System.currentTimeMillis());
        Log.e("Upload","Path "+uploadRef.getPath());
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(getContentResolver().getType(videoUri))
                .setCustomMetadata("user", mAuth.getCurrentUser().getEmail())
                .setCustomMetadata("name", name)
                .setCustomMetadata("description", description)
                .setCustomMetadata("level", level)
                .setCustomMetadata("organization", org)
                .build();
        UploadTask uploadTask=uploadRef.putFile(videoUri,metadata);
        fileSize=uploadTask.getSnapshot().getTotalByteCount();
        mBuilder.setProgress((int)fileSize, 0, false);
        mNotifyManager.notify(notifyId, mBuilder.build()); //display notification
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Submit.this,"Sorry your video could not be uploaded at this time.",Toast.LENGTH_SHORT).show();
                Log.e("Upload",e.toString()+" "+e.getCause().toString());
                Log.e("Upload","Auth: "+mAuth.getCurrentUser().getEmail());
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                fileSize=taskSnapshot.getTotalByteCount();
                mBuilder.setProgress((int)fileSize, (int)taskSnapshot.getBytesTransferred(), false);
                mNotifyManager.notify(notifyId, mBuilder.build()); //display notification
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mBuilder.setProgress(0,0,false);
                mBuilder.setContentText("Upload complete, thank you!");
                mNotifyManager.notify(notifyId, mBuilder.build()); //display notification
                Toast.makeText(Submit.this,"Thank you for your submission! We will review it as soon as possible!",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void getPermission(){
        // Here, thisActivity is the current activity
        if ((ContextCompat.checkSelfPermission(Submit.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(Submit.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)){

                ActivityCompat.requestPermissions(Submit.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                     android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else{
            dispatchTakeVideoIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dispatchTakeVideoIntent();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}