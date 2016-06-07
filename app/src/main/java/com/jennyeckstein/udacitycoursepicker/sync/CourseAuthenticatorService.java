package com.jennyeckstein.udacitycoursepicker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Jenny on 5/23/2016.
 */
public class CourseAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private CourseAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new CourseAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
