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

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

import cl.ozcc.inkanbike.R;
import cl.ozcc.inkanbike.SosActivity;
import cl.ozcc.inkanbike.objects.DataHelper;
import cl.ozcc.inkanbike.objects.Sos;
import cl.ozcc.inkanbike.objects.User;
import cl.ozcc.inkanbike.objects.Valid;

/**
 * Created by root on 08-09-15.
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MyGcmListenerService";
    Sos sos = null;
    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        LatLng position = new User().getPosition(getApplicationContext());
        SharedPreferences pref = getApplicationContext().getSharedPreferences("broadcast", Context.MODE_PRIVATE);

        if (from.startsWith("/topics/inkan"+pref.getString("topic","null"))) {
            if(Integer.parseInt(data.getString("user")) != new DataHelper(getApplication()).getUserID()){
                if(new Valid().isIntoRatio(position, 200, getApplicationContext())){

                    sos = new Sos(data.getString("alert_id"),
                            data.getString("user"),
                            data.getString("lat"),
                            data.getString("lng"),
                            data.getString("type"),
                            data.getString("message"),
                            data.getString("time"),
                            "200");

                        sendNotification(message);
                }
            }
        } else {
        }
    }
    private void sendNotification(String message) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        Intent sosIntent = new Intent(this, SosActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, sosIntent, 0);

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
