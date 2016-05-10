package cl.ozcc.inkanbike.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import cl.ozcc.inkanbike.R;
import cl.ozcc.inkanbike.objects.DataHelper;
import cl.ozcc.inkanbike.objects.Garage;

public class NewGarageFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient;
    SupportMapFragment SupportMap;
    GoogleMap Gmap;
    String[] ltln = new String[2];
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,final Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.new_garage_fragment, container, false);
        ctx = getActivity().getApplicationContext();
        SupportMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsFull);
        SupportMap.getMapAsync(this);
        Gmap = SupportMap.getMap();
        mGoogleApiClient =  new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        final FloatingActionButton btnAlert = (FloatingActionButton) view.findViewById(R.id.sendGarage);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(getString(R.string.nav_upload_garage));
                final View nG = getLayoutInflater(savedInstanceState).inflate(R.layout.add_new_garage, null);
                final EditText name = (EditText) nG.findViewById(R.id.nmAlert);
                final EditText dirA = (EditText) nG.findViewById(R.id.drAlert);
                alert.setView(nG);
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nom = name.getText().toString();
                        String dir = dirA.getText().toString();
                        if (nom.isEmpty() || dir.isEmpty()) {
                            String text = "Lat :" + ltln[0] + " Lon :" + ltln[1];
                            Toast.makeText(getContext(), "¡ERROR!"+text, Toast.LENGTH_SHORT).show();
                        } else {
                            Garage grg = new Garage(nom, dir, ltln[0], ltln[1]);
                            grg.SaveGarage();
                               if(new DataHelper(ctx).SaveGarage(grg)){
                                Toast.makeText(ctx,ctx.getString(R.string.GARAGE_OK),Toast.LENGTH_LONG).show();
                               }
                        }
                    }
                });
                alert.show();
            }
        });
        SharedPreferences prefs = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fragment", "NewGarageFragment");
        editor.commit();
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                ltln[0] = "" + cameraPosition.target.latitude;
                ltln[1] = "" + cameraPosition.target.longitude;
            }
        });
    }
    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            centerMap(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    public void centerMap(Double lat, Double lon){
        LatLng mPosition = new LatLng(lat, lon);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(mPosition).zoom(18).bearing(0).tilt(0).build();
        CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        Gmap.moveCamera(camUpd);
    }
}