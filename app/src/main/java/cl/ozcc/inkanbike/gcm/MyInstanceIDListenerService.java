package cl.ozcc.inkanbike.gcm;
/**
 * Created by root on 08-09-15.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    @Override
    public void onTokenRefresh() {


        pref = getApplicationContext().getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        editor = pref.edit();

        Log.v("DEBUG_TOKEN","REFRESH_TOKEN");

        editor.putString("topic", "null");
        editor.commit();

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}