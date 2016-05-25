package cl.ozcc.inkanbike.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by angel on 23-05-16.
 */
public class Sos implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public Sos createFromParcel(Parcel in) {
            return new Sos(in);
        }

        public Sos[] newArray(int size) {
            return new Sos[size];
        }
    };
    static int sosId;
    static int userId;
    static int Distance;
    static Double lat;
    static Double lon;
    static int Type;
    static String Message;
    static String Hours;

    public Sos() {
    }

    public Sos(String sId, String uId, String latitude, String longitude, String type, String msj, String hours, String dist) {
        Distance = Integer.parseInt(dist);
        sosId = Integer.parseInt(sId);
        userId = Integer.parseInt(uId);
        lat = Double.parseDouble(latitude);
        lon = Double.parseDouble(longitude);
        Type = Integer.parseInt(type);
        Message = msj;
        Hours = hours;
    }

    public Sos(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        sosId = Integer.parseInt(data[0]);
        userId = Integer.parseInt(data[1]);
        lat = Double.parseDouble(data[2]);
        lon = Double.parseDouble(data[3]);
        Type = Integer.parseInt(data[4]);
        Message = data[5];
        Hours = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                "" + sosId,
                "" + userId,
                "" + lat,
                "" + lon,
                "" + Type,
                Message,
                Hours});
    }

    public int getSosId() {
        return sosId;
    }

    public void setSosId(int sosId) {
        Sos.sosId = sosId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        Sos.userId = userId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        Sos.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        Sos.lon = lon;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }


}
