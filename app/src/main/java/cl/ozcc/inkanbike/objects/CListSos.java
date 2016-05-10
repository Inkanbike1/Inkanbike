package cl.ozcc.inkanbike.objects;

import android.content.Context;
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
public class CListSos extends ArrayAdapter {

    private Context context;
    private ArrayList<CItemSos> list;


    public CListSos(Context ctx, ArrayList lista){
        super(ctx, R.layout.list_item, lista);
        this.context = ctx;
        this.list = lista;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater  inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.list_item, null);

        ImageView imagen = (ImageView) item.findViewById(R.id.imageCustomItem);
        imagen.setImageResource(list.get(position).getIcon());

        TextView nombre = (TextView) item.findViewById(R.id.textCustomItem);
        nombre.setText(list.get(position).getText());

        return item;
    }













}