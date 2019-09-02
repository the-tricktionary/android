package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService  {
    private FirebaseAuth mAuth;

    @Override
    public void onNewToken(String token) {
        // Get updated InstanceID token.
        Log.d("FCM", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }

}
