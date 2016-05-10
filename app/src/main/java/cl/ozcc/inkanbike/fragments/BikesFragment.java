package cl.ozcc.inkanbike.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cl.ozcc.inkanbike.EditBike;
import cl.ozcc.inkanbike.R;
import cl.ozcc.inkanbike.objects.CItemBikes;
import cl.ozcc.inkanbike.objects.CListBikes;
import cl.ozcc.inkanbike.objects.DataHelper;

/**
 * Created by root on 14-08-15.
 */
public class BikesFragment extends Fragment {

    Context ctx;
    BikeFragment bkF = new BikeFragment();
    ListView miBikes;
    CListBikes adapterC;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
          view = inflater.inflate(R.layout.add_bike, container, false);
        ctx = getActivity().getApplicationContext();
        final FragmentManager fManager = getActivity().getSupportFragmentManager();

         miBikes = (ListView) view.findViewById(R.id.list_my_bike);

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.addnewBikeFB);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fManager.beginTransaction().replace(R.id.container, bkF).commit();
                }
            });
        miBikes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewL, int position, long id) {
                CItemBikes obj = (CItemBikes) miBikes.getAdapter().getItem(position);

                Intent editBike = new Intent(ctx, EditBike.class);
                    editBike.putExtra("id",obj.getText());
                    view.getContext().startActivity(editBike);

            }
        });
        SharedPreferences prefs = ctx.getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fragment", "BikesFragment");
        editor.commit();
        return view;
    }
    @Override
    public void onStart() {
        adapterC = new CListBikes(
                getActivity().getApplicationContext(),
                new DataHelper(ctx).GetBikesFromList());

        miBikes.setAdapter(adapterC);
        super.onStart();
    }
}