package cl.ozcc.inkanbike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

import cl.ozcc.inkanbike.objects.Bike;
import cl.ozcc.inkanbike.objects.DataHelper;

public class EditBike extends AppCompatActivity {

    File imgDir = new File(Environment.getExternalStorageDirectory()+File.separator+"InkanBike/media/images/");
    ImageView bikeIm;
    Bitmap bMap;
    Bike miBike;
    DataHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike);

        Bundle params = getIntent().getExtras();
        db = new DataHelper(getApplicationContext());

        miBike = db.findBikeById(params.getString("id"));

        EditText brandEd = (EditText) findViewById(R.id.idBrand);
        EditText modelEd = (EditText) findViewById(R.id.idModel);
        EditText serialNumEd = (EditText) findViewById(R.id.idSnumber);
        EditText otherEd = (EditText) findViewById(R.id.idOthers);

        brandEd.setText(miBike.getBrand());
        modelEd.setText(miBike.getModel());
        serialNumEd.setText(miBike.getSerial());
        otherEd.setText(miBike.getOther());

        Log.v("DEBUG_ID_BIKE", "PHOTO PATH:" + miBike.getPhoto());

        try {
            imgDir = new File(miBike.getPhoto());
            bMap = BitmapFactory.decodeFile(imgDir.toString());
            bikeIm = (ImageView) findViewById(R.id.bikeImg);
            bikeIm.setImageBitmap(bMap);
        }catch(Exception e){

        }
        final RelativeLayout openCam = (RelativeLayout) findViewById(R.id.openCam);
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bikeIm = (ImageView) findViewById(R.id.bikeImg);
                Uri outputFileUri = Uri.fromFile(imgDir);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, 0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != 0) {
            try {
                bMap = BitmapFactory.decodeFile(imgDir.toString());
                bikeIm.setImageBitmap(bMap);
                miBike.setPhoto(imgDir.toString());

            } catch (Exception e) {
                Log.e("CATCH_CAMERA", e.toString());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_bike_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_delete_bike:
                    try{
                        imgDir.delete();
                        db.DeleteBike(new String[]{String.valueOf(miBike.getId())});
                        onBackPressed();
                    }catch (Exception ex){

                    }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}