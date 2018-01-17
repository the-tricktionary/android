package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import trictionary.jumproper.com.jumpropetrictionary.R;

public class SignIn extends BaseActivity {

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 4236;
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String lang;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Configure Google Sign In
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this); //new alert dialog
        //builder.setTitle("Submit reply"); //dialog title
        LayoutInflater inflater = (LayoutInflater)SignIn.this.getSystemService (Context.LAYOUT_INFLATER_SERVICE); //needed to display custom layout
        final View textBoxes=inflater.inflate(R.layout.sign_in_dialog,null); //custom layout file now a view object
        final EditText username = (EditText)textBoxes.findViewById(R.id.username);
        final EditText email = (EditText)textBoxes.findViewById(R.id.email);
        final EditText password = (EditText)textBoxes.findViewById(R.id.password);
        final CheckBox usePhone = (CheckBox)textBoxes.findViewById(R.id.use_phone_number);
        final ImageButton googleButton = (ImageButton)textBoxes.findViewById(R.id.google_sign_in);
        final Button signIn = (Button)textBoxes.findViewById(R.id.sign_in_no_google);
        usePhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    email.setHint("Phone number");
                    email.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usePhone.isChecked()){
                    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            // This callback will be invoked in two situations:
                            // 1 - Instant verification. In some cases the phone number can be instantly
                            //     verified without needing to send or enter a verification code.
                            // 2 - Auto-retrieval. On some devices Google Play services can automatically
                            //     detect the incoming verification SMS and perform verification without
                            //     user action.
                            Log.d("Auth", "onVerificationCompleted:" + phoneAuthCredential);

                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w("Auth", "onVerificationFailed", e);

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                // Invalid request
                                // ...
                            } else if (e instanceof FirebaseTooManyRequestsException) {
                                // The SMS quota for the project has been exceeded
                                // ...
                            }

                            // Show a message and update the UI
                            // ...
                        }
                        @Override
                        public void onCodeSent(String verificationId,
                                               PhoneAuthProvider.ForceResendingToken token) {
                            // The SMS verification code has been sent to the provided phone number, we
                            // now need to ask the user to enter the code and then construct a credential
                            // by combining the code with a verification ID.
                            Log.d("Auth", "onCodeSent:" + verificationId);

                            // Save verification ID and resending token so we can use them later
                            mVerificationId = verificationId;
                            mResendToken = token;

                            // ...
                        }
                    };
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            email.getText().toString(),        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            SignIn.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Auth", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignIn.this, "Signed in as "+mAuth.getCurrentUser().getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                        String name=mAuth.getCurrentUser().getDisplayName();
                                        FirebaseDatabase fb=FirebaseDatabase.getInstance();
                                        final DatabaseReference myRef=fb.getReference("users");
                                        if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
                                            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("image").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                                        }
                                        myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("0").setValue(name.substring(0,name.indexOf(' ')));
                                        myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("1").setValue(name.substring(name.indexOf(' ')+1));
                                        if(lang.equals("English"))
                                            myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("en");
                                        if(lang.equals("Deutsch"))
                                            myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("de");
                                        if(lang.equals("Svenska"))
                                            myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("sv");
                                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Auth", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                }
            }
        });
        //initialize and populate friends list
        final ListView profilelistView = (ListView)textBoxes.findViewById(R.id.profile_listview);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        builder.setView(textBoxes); //set view to custom layout
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.google_sign_in_auth_id))
                .requestIdToken(getString(R.string.google_sign_in_auth_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("Auth","Connection Failed");
                    }
                })
                .build();
        mAuth=FirebaseAuth.getInstance();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    ((GlobalData)SignIn.this.getApplication()).refreshData();
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        lang=((GlobalData)this.getApplication()).getSettings().getString(SettingsActivity.LANGUAGE_SETTING,"English");
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void signInButtonClick(View v){
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Auth", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Auth", "createUserWithEmail:success");
                            Toast.makeText(SignIn.this, "Signed in as "+mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            FirebaseDatabase fb=FirebaseDatabase.getInstance();
                            final DatabaseReference myRef=fb.getReference("users");
                            if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
                                myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("image").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                            }
                            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("0").setValue(name.substring(0,name.indexOf(' ')));
                            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("1").setValue(name.substring(name.indexOf(' ')+1));
                            if(lang.equals("English"))
                                myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("en");
                            if(lang.equals("Deutsch"))
                                myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("de");
                            if(lang.equals("Svenska"))
                                myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("sv");
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Auth", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.e("Auth","Intent: "+signInIntent.toString());
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    public void userSignOut(View v){
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("Auth","result success: "+result.isSuccess());
            Log.e("Auth","result: "+result.getStatus().toString());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Auth", "firebaseAuthWithGoogle:" + acct.getId());
        Log.d("Auth", "firebaseAuthWithGoogleToken:" + acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(task.isComplete()){
                            Toast.makeText(SignIn.this, "Signed in as "+mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            String name=mAuth.getCurrentUser().getDisplayName();
                            FirebaseDatabase fb=FirebaseDatabase.getInstance();
                            final DatabaseReference myRef=fb.getReference("users");
                            if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
                                myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("image").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                            }
                            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("0").setValue(name.substring(0,name.indexOf(' ')));
                            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("1").setValue(name.substring(name.indexOf(' ')+1));
                            if(lang.equals("English"))
                                    myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("en");
                            if(lang.equals("Deutsch"))
                                    myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("de");
                            if(lang.equals("Svenska"))
                                    myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue("sv");
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithCredential", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

}
