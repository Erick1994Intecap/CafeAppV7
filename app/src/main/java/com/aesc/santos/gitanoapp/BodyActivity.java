package com.aesc.santos.gitanoapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.aesc.santos.gitanoapp.Entidades.AndroidVersion;
import com.aesc.santos.gitanoapp.Fragments.NotificacionesFragment;
import com.aesc.santos.gitanoapp.Fragments.ProductoDetalleFragment;
import com.aesc.santos.gitanoapp.Fragments.ProductosFragment;
import com.aesc.santos.gitanoapp.Fragments.PromocionesFragment;
import com.aesc.santos.gitanoapp.Fragments.PuntosFragment;
import com.aesc.santos.gitanoapp.Fragments.TiendaFragment;
import com.aesc.santos.gitanoapp.Intefaces.IComunicaFragments;

import java.util.ArrayList;

public class BodyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NotificacionesFragment.OnFragmentInteractionListener,
        ProductosFragment.OnFragmentInteractionListener,
        PromocionesFragment.OnFragmentInteractionListener,
        PuntosFragment.OnFragmentInteractionListener,
        TiendaFragment.OnFragmentInteractionListener, IComunicaFragments {


    private static final String TAG = "BodyActivity";

    ProductoDetalleFragment mProductoDetalleFragment;
    PuntosFragment mPuntosFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPuntosFragment = new PuntosFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,mPuntosFragment).commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.productos) {
            miFragment = new ProductosFragment();
            fragmentSeleccionado=true;
            Log.d(TAG, "onNavigationItemSelected: ProductosFragment");
        } else if (id == R.id.tienda) {
            miFragment = new TiendaFragment();
            fragmentSeleccionado=true;
            Log.d(TAG, "onNavigationItemSelected: TiendaFragment");
        } else if (id == R.id.punto) {
            miFragment = new PuntosFragment();
            fragmentSeleccionado=true;
            Log.d(TAG, "onNavigationItemSelected: PuntoFragment");
        } else if (id == R.id.promociones) {
            miFragment = new PromocionesFragment();
            fragmentSeleccionado=true;
            Log.d(TAG, "onNavigationItemSelected: PromocinesFragment");
        } else if (id == R.id.favoritos) {
            miFragment = new NotificacionesFragment();
            fragmentSeleccionado=true;
            Log.d(TAG, "onNavigationItemSelected: NotificacionesFragment");
        } else if (id == R.id.logOut) {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.siguenos) {
            Toast.makeText(this, "Siguenos", Toast.LENGTH_SHORT).show();
        }

        if (fragmentSeleccionado==true){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void enviarProducto(int position) {
        mProductoDetalleFragment = new ProductoDetalleFragment();

        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putInt("key", position);
        mProductoDetalleFragment.setArguments(bundleEnvio);

        //Toast.makeText(this, "ver" + bundleEnvio.toString(), Toast.LENGTH_SHORT).show();
        //Cargar el fragmente en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.container,mProductoDetalleFragment).addToBackStack(null).commit();
    }
}
