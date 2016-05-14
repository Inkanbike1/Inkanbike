package cl.ozcc.inkanbike.objects;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 07-09-15.
 */
public class Garage {
    final static String ROUTE = "/inkanbike/index.php/api/";
    final static String HOST = "www.inkanbike.cl";
    URL url;
    private int id;
    private String Name;
    private String Dir;
    private Double Lat;
    private Double Lng;

    public Garage(int id, String name, String dir, String lt, String ln) {
        this.id = id;
        this.Name = name;
        this.Dir = dir;
        try{
            this.Lat = Double.parseDouble(lt);
            this.Lng = Double.parseDouble(ln);
        }catch(Exception e){
            this.Lat = -0.0;
            this.Lng = 0.0;
        }
    }
    public String getName(){
        return this.Name;
    }
    public void setName(String n){
        this.Name = n;
    }
    public String getDir(){
        return this.Dir;
    }
    public void setDir(String d){
        this.Dir = d;
    }
    public Double getLat(){
        return this.Lat;
    }
    public void setLat(Double lt){
        this.Lat = lt;
    }
    public Double getLng(){return this.Lng;}
    public void setLng(Double ln){this.Lng = ln;}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void SaveGarage(){
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        String name = Garage.this.getName().replace(" ","%20");
                        String dir  = Garage.this.getDir().replace(" ","%20");
                        url = new URL("http", HOST, ROUTE+"garage/newGarage/name/"+name+
                                                            "/dir/"+dir+
                                                            "/lat/"+Garage.this.getLat()+
                                                            "/lng/"+Garage.this.getLng());

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
                                        Garage.this.setId(respObj.getInt("ibGara_id"));
                                    return null;
                                }else{
                                    return null;
                                }
                        } else {
                            return null;
                        }
                    }catch(Exception e){
                        return null;
                    }
                }
            }.execute();
        }catch(Exception e){
        }
    }
}