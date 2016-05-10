package cl.ozcc.inkanbike.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;
import cl.ozcc.inkanbike.MainActivity;
import cl.ozcc.inkanbike.R;
import cl.ozcc.inkanbike.objects.DataHelper;
import cl.ozcc.inkanbike.objects.User;
import cl.ozcc.inkanbike.objects.Valid;

/**
 * Created by root on 08-09-15.
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        Log.v("DEBUG_MESSAGE","USER_ID : "+data.getString("user"));
        Log.v("DEBUG_MESSAGE","SOS_LAT : "+data.getString("lat"));
        Log.v("DEBUG_MESSAGE","SOS_LON : "+data.getString("lng"));
        Log.v("DEBUG_MESSAGE","USER_TYPE : "+data.getString("type"));
        Log.v("DEBUG_MESSAGE","USER_MSJ : "+data.getString("message"));
        Log.v("DEBUG_MESSAGE","USER_TIME : "+data.getString("time"));


        LatLng position = new User().getPosition(getApplicationContext());
        SharedPreferences pref = getApplicationContext().getSharedPreferences("broadcast", Context.MODE_PRIVATE);


        if (from.startsWith("/topics/inkan"+pref.getString("topic","null"))) {
            if(Integer.parseInt(data.getString("user")) != new DataHelper(getApplication()).getUserID()){
                if(new Valid().isIntoRatio(position, 200, getApplicationContext())){
                        sendNotification(message);
                }
            }
        } else {

        }

    }
    private void sendNotification(String message) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("SOS Inkanbike")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager.WakeLock screenOn = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
        screenOn.acquire();

        final Vibrator v = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        long[] pattern = {0, 800, 800, 800, 800, 800};
        v.vibrate(pattern, -1);

    }
}
