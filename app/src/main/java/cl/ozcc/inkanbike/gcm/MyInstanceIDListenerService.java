package cl.ozcc.inkanbike.gcm;
/**
 * Created by root on 08-09-15.
 */

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {

        Log.v("DEBUG_TOKEN","REFRESH_TOKEN");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}