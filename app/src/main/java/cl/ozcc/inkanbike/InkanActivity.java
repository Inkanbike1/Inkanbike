package cl.ozcc.inkanbike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cl.ozcc.inkanbike.fragments.BikesFragment;
import cl.ozcc.inkanbike.fragments.IndexFragment;
import cl.ozcc.inkanbike.fragments.NewGarageFragment;
import cl.ozcc.inkanbike.gcm.RegistrationIntentService;
import cl.ozcc.inkanbike.objects.DataHelper;
import cl.ozcc.inkanbike.objects.Valid;

public class InkanActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fManager = getSupportFragmentManager();
    IndexFragment indF = new IndexFragment();
    NewGarageFragment garF = new NewGarageFragment();
    BikesFragment bikeF = new BikesFragment();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inkan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getApplicationContext().getSharedPreferences("broadcast", Context.MODE_PRIVATE);
        new Valid().ValidDirectories();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fManager.beginTransaction().replace(R.id.container, indF).commit();

        Intent registerGcM = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(registerGcM);


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (!prefs.getString("fragment","null").equals("IndexFragment")) {
                fManager.beginTransaction().replace(R.id.container, indF).commit();
            }else{
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inkan, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_index_sos:
                fManager.beginTransaction().replace(R.id.container, indF).commit();
                break;
            case R.id.nav_upload_garage:
                fManager.beginTransaction().replace(R.id.container, garF).commit();
                break;
            case R.id.nav_mi_bike:
                fManager.beginTransaction().replace(R.id.container, bikeF).commit();
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_log_out:
               SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("login", false);
                editor.commit();
                Intent index = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(index);
                new DataHelper(getApplicationContext()).CloseSession();
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
