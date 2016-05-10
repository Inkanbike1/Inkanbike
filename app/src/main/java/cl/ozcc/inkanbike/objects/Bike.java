package cl.ozcc.inkanbike.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 23-03-16.
 */
public class Bike implements Parcelable{
    private int id;
    private String brand;
    private String model;
    private String serial;
    private String other;
    private String photo;

    public Bike(int id, String brand, String other, String serial, String model) {
        this.id = id;
        this.brand = brand;
        this.other = other;
        this.serial = serial;
        this.model = model;
    }
    public Bike(String brand, String other, String serial, String model) {
        this.brand = brand;
        this.other = other;
        this.serial = serial;
        this.model = model;
    }
    public Bike(){

    }
    public Bike (Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.brand    = data[0];
        this.model   = data[1];
        this.serial    = data[2];
        this.other   = data[3];
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.brand,
                this.model,
                this.serial,
                this.other,});
    }
    public static final Creator CREATOR = new Creator() {
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getSerial() {
        return serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}