package cl.ozcc.inkanbike.objects;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public DataHelper(Context ctx){
        super(ctx, "INKANBIKE", null, 1);
        prefs = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreate = "CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY ,"+
                "user_name TEXT, user_email TEXT, user_pass TEXT, user_token TEXT);";

        String queryTalleres = "CREATE TABLE IF NOT EXISTS talleres (tal_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "tal_name TEXT, tal_dir TEXT, tal_lat TEXT, tal_lng TEXT);";

        String queryBike ="CREATE TABLE IF NOT EXISTS bikes(bike_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " brand TEXT, model TEXT, serial TEXT,other TEXT, photo TEXT);";

        db.execSQL(queryCreate);
        db.execSQL(queryTalleres);
        db.execSQL(queryBike);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Boolean InsertUser(User user){
        try {
            ContentValues values = new ContentValues();
            values.put("user_id", User.id);
            values.put("user_name", User.name);
            values.put("user_email", User.email);
            values.put("user_pass", User.pass);
            values.put("user_token", User.token);
            this.getWritableDatabase().insert("users", null, values);

            return true;
        }catch(Exception e){
            return false;
        }
    }
    public Boolean ValidLogin(){
        return prefs.getBoolean("login",false);
    }
    public void setLogin(){
        editor.putBoolean("login", true);
        editor.commit();
    }
    public Boolean InsertBike(Bike bike){
        try{
            ContentValues content = new ContentValues();
                content.put("brand",bike.getBrand());
                content.put("model",bike.getModel());
                content.put("serial",bike.getSerial());
                content.put("other", bike.getOther());
                content.put("photo", bike.getPhoto());
                this.getWritableDatabase().insert("bikes", null, content);

                return true;
        }catch (Exception ex){
            return false;
        }
    }
    public User getUser(){
        String columnas[]= {"user_id","user_name","user_email","user_pass","user_token"};
        Cursor c = this.getReadableDatabase().query("users", columnas, null, null,null, null, null);
        try{
            int id,in,ie,ip,it;
            id = c.getColumnIndex("user_id");
            in = c.getColumnIndex("user_name");
            ie = c.getColumnIndex("user_email");
            ip = c.getColumnIndex("user_pass");
            it = c.getColumnIndex("user_token");
            c.moveToFirst();
            return new User(c.getInt(id),
                    c.getString(in),
                    c.getString(ie),
                    c.getString(ip),
                    c.getString(it));
        }catch(Exception e){
            return null;
        }
    }
    public int getUserID(){
        String columnas[]= {"user_id"};
        Cursor c = this.getReadableDatabase().query("users", columnas, null, null,null, null, null);
        try{
            int id;
            id = c.getColumnIndex("user_id");
            c.moveToFirst();
            this.close();
            return c.getInt(id);
        }catch(Exception e){
            return 0;
        }
    }
    public Boolean updateToken(String token){
        try{
            int id = getUserID();
            if(id > 0){
                this.getWritableDatabase().execSQL("UPDATE users SET user_token='"+token+"' WHERE user_id="+id);
                return true;
            }else return false;
        }catch(Exception e){return false;}
    }
    public String getUserTOKEN(){
        String column[]= {"user_token"};
        Cursor c = this.getReadableDatabase().query("users", column, null, null,null, null, null);
        try{
            int tk;
            tk = c.getColumnIndex("user_token");
            c.moveToFirst();
            this.close();
            return c.getString(tk);
        }catch(Exception e){
            return "";
        }
    }
    public ArrayList<Bike> GetBikes(){
        ArrayList<Bike> res = new ArrayList<>();
        String columnas[]= {"bike_id","brand","model","serial","other"};
        Cursor c = this.getReadableDatabase().query("bikes", columnas, null, null,null, null, null);
        try{
            int id,ib,im,is,io;
            id = c.getColumnIndex("bike_id");
            ib = c.getColumnIndex("brand");
            im = c.getColumnIndex("model");
            is = c.getColumnIndex("serial");
            io = c.getColumnIndex("other");
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
            {
                res.add(new Bike( c.getInt(id),
                        c.getString(ib),
                        c.getString(im),
                        c.getString(is),
                        c.getString(io)));
            }
        }catch(Exception e){

        }
        return  res;
    }
    public ArrayList<CItemBikes> GetBikesFromList(){
        ArrayList<CItemBikes> res = new ArrayList<>();
        String columnas[]= {"bike_id","other","photo"};
        Cursor c = this.getReadableDatabase().query("bikes", columnas, null, null,null, null, null);
        try{
            int id,io,ip;
            id = c.getColumnIndex("bike_id");
            io = c.getColumnIndex("other");
            ip = c.getColumnIndex("photo");
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
            {
                res.add(new CItemBikes( c.getString(id),
                        c.getString(ip)));
            }
        }catch(Exception e){

        }
        return  res;
    }
    public Boolean DeleteBike(String[] id){
        this.getWritableDatabase().delete("bikes","bike_id=?",id);
        return true;
    }
    public Integer getLastBikeId(){
        Cursor c = this.getReadableDatabase().rawQuery("SELECT bike_id FROM bikes ORDER BY bike_id DESC LIMIT 1", null);
        int id = c.getColumnIndex("bike_id");
        if(c.moveToFirst()){
             int idBike = c.getInt(id)+1;
            return idBike;
        }else{
            return 1;
        }
    }
    public Bike findBikeById(String id){
        Bike res = new Bike();
        String columnas[]= {"bike_id","brand","model","serial","other","photo"};
        Cursor c = this.getReadableDatabase().query("bikes", columnas, "bike_id=?", new String[] { id },null, null, null);
        try{
            c.moveToFirst();
            int ii,ib,im,is,io,ip;
            ii = c.getColumnIndex("bike_id");
            ib = c.getColumnIndex("brand");
            im = c.getColumnIndex("model");
            is = c.getColumnIndex("serial");
            io = c.getColumnIndex("other");
            ip = c.getColumnIndex("photo");

            res.setId(c.getInt(ii));
            res.setBrand(c.getString(ib));
            res.setModel(c.getString(im));
            res.setSerial(c.getString(is));
            res.setOther(c.getString(io));
            res.setPhoto(c.getString(ip));

            return res;
        }catch(Exception e){
            return res;
        }
    }
    public void CloseSession(){
        SQLiteDatabase db = this.getWritableDatabase();
            String tbUsers      = "DROP TABLE users";
                db.execSQL(tbUsers);
                onCreate(db);
    }
    public Boolean SaveGarage(Garage garage){
        try{
            ContentValues values = new ContentValues();
                values.put("tal_id",garage.getId());
                values.put("tal_name",garage.getName());
                values.put("tal_dir", garage.getDir());
                values.put("tal_lat", garage.getLat());
                values.put("tal_lng", garage.getLng());
            this.getWritableDatabase().insert("talleres", null, values);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}