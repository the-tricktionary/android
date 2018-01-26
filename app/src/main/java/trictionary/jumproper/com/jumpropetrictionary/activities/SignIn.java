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
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private boolean haveAccount = false;
    private boolean usernameTaken = false;
    private String[] name= new String[2];
    private String userEmail, uId;
    private EditText username, firstName, lastName, email, password, verify;
    private TextView orText;
    private CheckBox usePhone;
    private ImageButton googleButton;
    private Button signIn, haveAccountButton;
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
        username = (EditText)textBoxes.findViewById(R.id.username);
        firstName = (EditText)textBoxes.findViewById(R.id.first_name);
        lastName = (EditText)textBoxes.findViewById(R.id.last_name);
        email = (EditText)textBoxes.findViewById(R.id.email);
        password = (EditText)textBoxes.findViewById(R.id.password);
        verify = (EditText)textBoxes.findViewById(R.id.verify_code);
        usePhone = (CheckBox)textBoxes.findViewById(R.id.use_phone_number);
        googleButton = (ImageButton)textBoxes.findViewById(R.id.google_sign_in);
        signIn = (Button)textBoxes.findViewById(R.id.sign_in_no_google);
        haveAccountButton = (Button)textBoxes.findViewById(R.id.already_have_account);
        orText = (TextView)textBoxes.findViewById(R.id.or_sign_in);
        usePhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    email.setHint("Phone number");
                    email.setInputType(InputType.TYPE_CLASS_PHONE);
                }
                else{
                    email.setHint("Email");
                    email.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveAccount){
                    haveAccount = false;
                    firstName.setVisibility(View.VISIBLE);
                    lastName.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    haveAccountButton.setText(R.string.already_have_account);
                    signIn.setText(R.string.sign_up);
                }
                else {
                    haveAccount = true;
                    firstName.setVisibility(View.GONE);
                    lastName.setVisibility(View.GONE);
                    username.setVisibility(View.GONE);
                    haveAccountButton.setText(R.string.create_account);
                    signIn.setText(R.string.sign_in);
                }
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                usePhone.setVisibility(View.GONE);
                googleButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);
                haveAccountButton.setVisibility(View.GONE);
                signIn.setText(R.string.google_sign_in);
                signIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signIn();
                    }
                });

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!haveAccount) {
                    if (email.getText().toString().length() == 0 ||
                            username.getText().toString().length() == 0 ||
                            password.getText().toString().length() == 0 ||
                            firstName.getText().toString().length() == 0 ||
                            lastName.getText().toString().length() == 0) {

                        Toast.makeText(getApplicationContext(), R.string.blank_fields_toast, Toast.LENGTH_SHORT).show();
                    }
                    else if (usePhone.isChecked()) {
                        checkUsername(username.getText().toString());
                        name[0] = firstName.getText().toString();
                        name[1] = lastName.getText().toString();
                        if(!usernameTaken) {
                            checkPhoneNumber();
                        }
                    } else {
                        checkUsername(username.getText().toString());
                        if(!usernameTaken) {
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(SignIn.this, getString(R.string.verification_email) + task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                finishSignIn();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("Auth", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(SignIn.this, "Authentication failed." + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }
                                    });
                        }
                    }
                }
                else {
                    if(email.getText().toString().length()==0 ||
                            password.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(), R.string.blank_fields_toast, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(usePhone.isChecked()){
                            checkPhoneNumber();
                        }
                        else {
                            FirebaseDatabase fb = FirebaseDatabase.getInstance();
                            userEmail = email.getText().toString();
                            mAuth.signInWithEmailAndPassword(userEmail, password.getText().toString())
                                    .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("Auth", "signInWithEmail:success");
                                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("Auth", "signInWithEmail:failure", task.getException());
                                                Toast.makeText(SignIn.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                            // ...
                                        }
                                    });
                        }
                    }
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
    public void checkUsername(final String username){
        if(username.length()>20){
            Toast.makeText(getApplicationContext(), "Sorry usernames must be less than 20 characters.",Toast.LENGTH_SHORT);
            usernameTaken = true;
        }
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference usernamesRef = fb.getReference("usernames");
        usernamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username)){
                    Toast.makeText(getApplicationContext(), "Sorry that username is taken.",Toast.LENGTH_SHORT);
                    usernameTaken = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.username_error,Toast.LENGTH_LONG);
                usernameTaken = true;
            }
        });
    }
    public void checkPhoneNumber(){
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        username.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        orText.setVisibility(View.GONE);
        usePhone.setVisibility(View.GONE);
        signIn.setText(R.string.verify);
        haveAccountButton.setVisibility(View.GONE);
        verify.setVisibility(View.VISIBLE);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verify.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
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
                Toast.makeText(getApplicationContext(), R.string.could_not_verify, Toast.LENGTH_SHORT).show();

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
                            finishSignIn();
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
    public void finishSignIn(){
        // Sign in success, update UI with the signed-in user's information
        Log.d("Auth", "signInWithCredential:success");

        // Sign in success, update UI with the signed-in user's information
        Log.d("Auth", "createUserWithEmail:success");
        Toast.makeText(SignIn.this, R.string.signed_in_as + username.getText().toString(),
                Toast.LENGTH_SHORT).show();

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=fb.getReference("users");
        if(mAuth.getCurrentUser().getPhotoUrl()!=null) {
            myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("image").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
        }
        myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("0").setValue(name[0]);
        myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("name").child("1").setValue(name[1]);
        myRef.child(mAuth.getCurrentUser().getUid()).child("profile").child("username").setValue(username.getText().toString().toLowerCase());

        lang = "en"; //by default
        switch (lang) {
            case "Dansk":
                lang = "da";
                break;
            case "Deutsch":
                lang = "de";
                break;
            case "Español":
                lang = "es";
                break;
            case "русский":
                lang = "ru";
                break;
            case "Svenska":
                lang = "sv";
                break;
        }
        myRef.child(mAuth.getCurrentUser().getUid()).child("lang").setValue(lang);
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(intent);
        finish();
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
                            finishSignIn();
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
