package cl.ozcc.inkanbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.ozcc.inkanbike.objects.MessageUI;
import cl.ozcc.inkanbike.objects.Sos;
import cl.ozcc.inkanbike.objects.User;

public class SosActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    final Handler handler = new Handler();
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment SupportMap;
    GoogleMap Gmap;
    Sos sos = new Sos();
    Boolean send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        SupportMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSos);
        SupportMap.getMapAsync(this);
        Gmap = SupportMap.getMap();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        TextView type = (TextView) findViewById(R.id.sosTypeRec);
        TextView distance = (TextView) findViewById(R.id.sosDistanRec);
        TextView message = (TextView) findViewById(R.id.sosMsjRec);

        String[] sosName = getResources().getStringArray(R.array.SOS_ARRAY);

        type.setText(sosName[sos.getType()]);
        distance.setText(sos.getDistance() + " Mts.");
        message.setText(sos.getMessage());

        FloatingActionButton respSos = (FloatingActionButton) findViewById(R.id.respSos);
        respSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alert = new MessageUI(SosActivity.this)
                        .GetAlertDialog(
                                getApplicationContext().getResources().getString(R.string.SEND_SOS_USERS),
                                getApplicationContext().getResources().getString(R.string.SOS_RESP_OK)
                        );
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        send = true;
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        send = false;
                    }
                });
                alert.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                        }
                        if (send) {
                            new User().responseSos(sos.getSosId());
                        }
                    }
                }, 3000);

            }
        });
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(sos.getLat(), sos.getLon())).zoom(15).bearing(0).tilt(90).build();
            CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
            Gmap.moveCamera(camUpd);
            Gmap.setMyLocationEnabled(true);
            Gmap.addMarker(new MarkerOptions()
                            .position(new LatLng(sos.getLat(), sos.getLon()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_handsup_black_36dp))
            );
        } catch (SecurityException se) {

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

}
