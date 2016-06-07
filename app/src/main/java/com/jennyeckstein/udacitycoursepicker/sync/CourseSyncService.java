package com.jennyeckstein.udacitycoursepicker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jenny on 5/23/2016.
 */
public class CourseSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static CourseSyncAdapter sSunshineSyncAdapter = null;


    @Override
    public void onCreate() {
        Log.d("CourseSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new CourseSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}
