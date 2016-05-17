package cl.ozcc.inkanbike.objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cl.ozcc.inkanbike.R;

public class User implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    final  static String ROUTE  = "/inkanbike/index.php/api/";
    final  static String HOST   =   "www.inkanbike.cl";
    public static URL url;
    public static int id;
    public static String name;
    public static String email;
    public static String token;
    public static String pass;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    static Location mLastLocation;
    static GoogleApiClient mGoogleApiClient;
    private static Boolean isPosition = false;

    public User() {
    }

    public User(int id, String name, String email, String token, String pass) {
        User.id = id;
        User.name = name;
        User.email = email;
        User.token = token;
        User.pass = pass;
    }

    public static ArrayList<Garage> getGarageFromServer(final LatLng latLng, Context ctx) {
        final int ID = new DataHelper(ctx).getUserID();

        final ArrayList<Garage> garages = new ArrayList<Garage>();
        try {
            return new AsyncTask<Void, Void, ArrayList<Garage>>() {
                @Override
                protected ArrayList<Garage> doInBackground(Void... params) {
                    try {
                        url = new URL("http", HOST, ROUTE + "garage/workshops/lat/" + latLng.latitude +
                                "/lon/" + latLng.longitude + "/user/" + ID);

                        Log.v("DEBUG_GARAGE", "URL GET GARAGE :" + url.toString());

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
                            JSONArray respArray = new JSONArray(sb.toString());
                            for (int i = 0; i < respArray.length(); i++) {
                                JSONObject object = respArray.getJSONObject(i);
                                Log.v("DEBUG_GARAGE", "ID :" + object.getInt("ibGara_id"));
                                Log.v("DEBUG_GARAGE", "NAME :" + object.getString("ibGara_name"));
                                Log.v("DEBUG_GARAGE", "DIR :" + object.getString("ibGara_address"));
                                Log.v("DEBUG_GARAGE", "LAT :" + object.getString("ibGara_lat"));
                                Log.v("DEBUG_GARAGE", "LON :" + object.getString("ibGara_lon"));
                                garages.add(new Garage(
                                        object.getInt("ibGara_id"),
                                        object.getString("ibGara_name"),
                                        object.getString("ibGara_address"),
                                        object.getString("ibGara_lat"),
                                        object.getString("ibGara_lon")));
                            }
                            return garages;
                            //Implements JSON ARRAY Garage

                        } else {
                            return garages;
                        }
                    } catch (Exception e) {
                        Log.v("DEBUG_GARAGE", "EXCEPTION :" + e.toString());
                        return garages;
                    }
                }
            }.execute().get();
        } catch (Exception e) {
            Log.v("DEBUG_GARAGE", "EXCEPTION :" + e.toString());
            return garages;
        }
    }

    public Boolean Register(){
        try {
            return new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        name = name.replace(" ","%20");
                        url = new URL("http",HOST,ROUTE+"user/register/email/"+email+"/pass/"+pass+"/token/"+token+"/name/"+name);
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
                        }else{
                            return false;
                        }
                    }catch (Exception e){
                        return false;
                    }
                }
            }.execute().get();
        }catch (Exception e){
            return false;
        }
    }//Fin Registered

    public LatLng getPosition(Context ctx){
        try{
            mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

            while (!isPosition);
            return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }catch (Exception e){
            Log.v("DEBUG_POSITION","ERROR GET POSITION  : " +e.toString());
            return new LatLng(0.0,0.0);
        }
    }

    public User findUserFromServer(final int id){
        try {
            return new AsyncTask<Void, Void, User>() {
                @Override
                protected User doInBackground(Void... params) {
                    try {
                        url = new URL("http",HOST,ROUTE+"user/user/id/"+id);
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
                                return new User(id,respObj.getString("ibUser_name"),
                                        respObj.getString("ibUser_email"),
                                        respObj.getString("ibUser_token"),
                                        respObj.getString("ibUser_pass"));
                        } else {
                            return null;
                        }
                    }catch (Exception e){
                        return null;
                    }
                }
            }.execute().get();
        }catch(Exception e){
            return  null;
        }
    }//Fin findUserFromServer
    public void save(Context ctx){
        new DataHelper(ctx).InsertUser(this);
    }
    public boolean sendToken(final int idUpd, final String tokenUpd){
        try {
            return new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                        try{
                            url = new URL("http",HOST,ROUTE+"user/updateToken/id/"+idUpd+"/token/"+tokenUpd);
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
                            } else return false;
                        }catch(Exception e){
                        }
                    return null;
                }
            }.execute().get();
        }catch (Exception e){
            return false;
        }
    }
    public void sendSos(final int selected, final Context ctx){
        String message = "";
        switch (selected){
            case 0:message = ctx.getString(R.string.SOS_FLAT_TIRE);break;
            case 1:message = ctx.getString(R.string.SOS_TIRE);break;
            case 2:message = ctx.getString(R.string.SOS_BRAKE);break;
            case 3:message = ctx.getString(R.string.SOS_CHAIN);break;
            case 4:message = ctx.getString(R.string.SOS_OTHER);break;
            case 5:message = ctx.getString(R.string.SOS_THIEFT);break;
        }
       final AlertDialog alert = new MessageUI(ctx).GetAlertDialog(ctx.getString(R.string.SEND_SOS_USERS),message);
        alert.show();
        try {
            AsyncTask<Integer, Void, Boolean> execute = new AsyncTask<Integer, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Integer... params) {
                    LatLng position = User.this.getPosition(ctx);
                    pref = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
                    try {
                        Calendar c = Calendar.getInstance();
                            int hour = c.get(Calendar.HOUR);
                            int min = c.get(Calendar.MINUTE);
                            String time = ""+hour+"_"+min;
                        url = new URL("http", HOST, ROUTE + "user/sendAlert/" +
                                "user/" + new DataHelper(ctx).getUserID() + "/" +
                                "lat/" + position.latitude + "/" +
                                "lon/" + position.longitude + "/" +
                                "type/" + params[0] + "/" +
                                "topic/" + pref.getString("topic", "null")+ "/" +
                                "time/"+time);
                        Log.v("DEBUG_MESSAGE","SENDING SOS : "+url.toString());
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
                        } else return false;
                    } catch (Exception e) {
                        Log.v("DEBUG_MESSAGE","SENDING SOS ERROR: "+e.toString());
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    if (alert.isShowing()) {
                        alert.dismiss();
                    }
                }
            }.execute(selected);
        }catch(Exception ex){
        }
    }
    public void subscribeTopics(final Context ctx, final String token){
        pref = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        editor = pref.edit();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    LatLng cords = User.this.getPosition(ctx);
                    double lat = cords.latitude;
                    double lng = cords.longitude;
                    Geocoder gcd = new Geocoder(ctx, Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                    String topic = cleanString(addresses.get(0).getAdminArea());
                    String topicS = pref.getString("topic", "null");

                    Log.v("DEBUG_TOKEN", "TOPIC "+topic);
                    Log.v("DEBUG_TOKEN", "TOPICS " + topicS);

                    GcmPubSub pubSub = GcmPubSub.getInstance(ctx);
                    if(topicS.equals("null")){
                        pubSub.subscribe(token, "/topics/inkan"+topic, null);
                        editor.putString("topic", topic);
                        Log.v("DEBUG_TOKEN", "SUSCRIBE_TOKEN "+topic);
                        editor.commit();
                    }else if(!topicS.equals(topic)){
                        pubSub.unsubscribe(token, "/topics/inkan"+topicS);
                        pubSub.subscribe(token, "/topics/inkan" + topic, null);
                        editor.putString("topic", topic);
                        Log.v("DEBUG_TOKEN", "SUSCRIBE_TOKEN " + topic);
                        editor.commit();
                    }

                }catch (IOException ioe){
                    Log.v("DEBUG_TOKEN", "SUSCRIBE_TOKEN IOException : "+ioe.toString());
                }
                return null;
            }
        }.execute();
    }
    private String cleanString(String string){
        string = string.replace("á","a");
        string = string.replace("é","e");
        string = string.replace("í","i");
        string = string.replace("ó", "o");
        string = string.replace("ú", "u");
        string = string.replace(" ","");
        return string;
    }
    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            isPosition = true;
        }catch(SecurityException se){
            isPosition = true;
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        isPosition = true;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        isPosition = true;
    }


    public void HOLANTONIO() {
        
    }
}