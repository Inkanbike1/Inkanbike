package cl.ozcc.inkanbike.objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
/**
 * Created by angel on 19-02-16.
 */
public class Valid{
    URL url;
    final static String ROUTE  = "/inkanbike/index.php/api/";
    final static String HOST   =   "www.inkanbike.cl";
    Boolean isEnableGps, isEnableNet;
    LocationManager LocMan;
    public Boolean ValidNet(){
        try {
            return new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params){
                    try {
                        url = new URL("http",HOST,"");
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        return urlConn.getResponseCode() == HttpURLConnection.HTTP_OK;
                    }catch (Exception e){
                        return false;
                    }
                }
            }.execute().get();
        }catch (Exception e){
            return false;
        }
    }
    public Boolean ValidEmail(final String email){
        try {
            return new AsyncTask<Void, Void, Boolean>() {
                public Boolean resp = false;

                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        url = new URL("http", HOST,ROUTE+"user/validateEmail/email/"+email);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream is = urlConn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            is.close();
                                JSONObject respObj = new JSONObject(sb.toString());
                            return respObj.getInt("status") == HttpURLConnection.HTTP_OK;
                        } else {
                            resp = false;
                        }
                    } catch (Exception e) {
                        resp = false;
                    }
                    return resp;
                }
            }.execute().get();
        }catch (Exception e){
            return false;
        }
    }
    public Integer ValidLogin(final String email, final String pass, final Context ctx){
        try {
            return new AsyncTask<Void, Void, Integer>() {
                public Boolean resp = false;
                @Override
                protected Integer doInBackground(Void... params) {
                    try {
                        url = new URL("http",HOST, ROUTE+"user/validate/email/"+email+"/pass/"+pass);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream is = urlConn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            is.close();
                                JSONObject respObj = new JSONObject(sb.toString());
                                    if(respObj.getInt("status") == HttpURLConnection.HTTP_OK){
                                        return respObj.getInt("user");
                                    }else{
                                        return 0;
                                    }
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        return 0;
                    }
                }
            }.execute().get();
        }catch (Exception e){
            return 0;
        }
    }
    public void ValidDirectories(){
        File inkDir = new File(Environment.getExternalStorageDirectory() + File.separator+"InkanBike/media/images");
        if(!inkDir.exists()){
            inkDir.mkdirs();
        }
    }
    public Boolean ValidSerialNumber(final String n){
        try {
            return new AsyncTask<Void, Void, Boolean>() {
                public Boolean resp = false;

                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        url = new URL("http", HOST, ROUTE+"serial/"+n);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream is = urlConn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            is.close();
                            switch (Integer.parseInt(sb.toString())) {
                                case 200://status OK
                                    resp = true;
                                    break;
                                case 406://status No aceptable (error pass or email)
                                    resp = false;
                                    break;
                            }
                        } else {
                            resp = false;
                        }
                    } catch (Exception e) {
                        resp = false;
                    }
                    return resp;
                }
            }.execute().get();
        }catch (Exception e){
            return false;
        }
    }
    public Boolean isIntoRatio(LatLng position, int mts, Context ctx){

        LatLng userPosition = new User().getPosition(ctx);

        Double lat1 = toRadians(userPosition.latitude);
        Double lat2 = toRadians(position.latitude);
        Double lon1 = toRadians(userPosition.longitude);
        Double lon2 = toRadians(position.longitude);

        Double distance = (acos(sin(lat1) * sin(lat2) + cos(lon1 - lon2)
                * cos(lat1) * cos(lat2)) * 6371)*1000;

        Log.v("DEBUG_POSITION","CALCULE METERS  : "+round(distance));


        return mts >= round(distance);
    }

    public boolean ValidGPS(Context ctx){
        LocMan = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        isEnableGps = LocMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isEnableNet = LocMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isEnableGps){
            msg(ctx);
        }

        return false;
    }

    public void msg(Context ctx){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctx);
        dialogo.setTitle("Importante");
        dialogo.setMessage("¿Activar Gps?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo.show();
    }
}