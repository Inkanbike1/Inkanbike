package cl.ozcc.inkanbike.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cl.ozcc.inkanbike.R;
import cl.ozcc.inkanbike.objects.Bike;
import cl.ozcc.inkanbike.objects.DataHelper;

/**
 * Created by root on 14-08-15.
 */
public class BikeFragment extends Fragment {

    Context ctx;
    File imgDir;
    ImageView bikeIm;
    private Bike bike = new Bike();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.bike, container, false);
        ctx = getActivity().getApplicationContext();
        final BikesFragment bikesF = new BikesFragment();
        imgDir = new File(Environment.getExternalStorageDirectory()+File.separator+"InkanBike/media/images/");

        final RelativeLayout openCam = (RelativeLayout) view.findViewById(R.id.openCam);
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bikeIm = (ImageView) view.findViewById(R.id.bikeImg);
                try {
                    imgDir.createNewFile();
                }
                catch (IOException e)
                {
                }
                imgDir = new File(imgDir.toString()+File.separator+new DataHelper(ctx).getLastBikeId()+".jpg");

                Uri outputFileUri = Uri.fromFile(imgDir);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, 0);
            }
        });

        final EditText brandEd = (EditText) view.findViewById(R.id.idBrand);
        final EditText modelEd = (EditText) view.findViewById(R.id.idModel);
        final EditText serialNumEd = (EditText) view.findViewById(R.id.idSnumber);
        final EditText otherEd = (EditText) view.findViewById(R.id.idOthers);
        Button addBike = (Button) view.findViewById(R.id.addBike);

                addBike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bike.setBrand(brandEd.getText().toString());
                        bike.setModel(modelEd.getText().toString());
                        bike.setSerial(serialNumEd.getText().toString());
                        bike.setOther(otherEd.getText().toString());

                       if(new DataHelper(ctx).InsertBike(bike)){
                           brandEd.setText("");
                           modelEd.setText("");
                           serialNumEd.setText("");
                           otherEd.setText("");
                           FragmentManager fManager = getActivity().getSupportFragmentManager();
                           fManager.beginTransaction().replace(R.id.container, bikesF).commit();
                       }else{
                           Toast.makeText(ctx, "Error al guardar bicicleta, intenta nuevamente",
                                   Toast.LENGTH_SHORT).show();
                       }

                    }
                });
        SharedPreferences prefs = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fragment", "BikeFragment");
        editor.commit();

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode != 0) {
                try {
                    Bitmap bMap = BitmapFactory.decodeFile(imgDir.toString());
                    bikeIm.setImageBitmap(bMap);
                    bike.setPhoto(imgDir.toString());

                } catch (Exception e) {
                    Log.e("CATCH_CAMERA", e.toString());
                }

            }
        }
}