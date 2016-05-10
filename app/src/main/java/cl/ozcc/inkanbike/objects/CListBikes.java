package cl.ozcc.inkanbike.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.ozcc.inkanbike.R;

/**
 * Created by root on 26-09-15.
 */
public class CListBikes extends ArrayAdapter {

    private Context context;
    private ArrayList<CItemBikes> list;


    public CListBikes(Context ctx, ArrayList lista){
        super(ctx, R.layout.card_bike, lista);
        this.context = ctx;
        this.list = lista;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater  inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.card_bike, null);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;

        ImageView imagen = (ImageView) item.findViewById(R.id.imageBike);
        Bitmap bMap = BitmapFactory.decodeFile(list.get(position).getIcon(), options);
        imagen.setImageBitmap(bMap);

        TextView nombre = (TextView) item.findViewById(R.id.idBike);
        nombre.setText(list.get(position).getText());

        return item;
    }
}