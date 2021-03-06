package com.oscar.olimpia;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oscar.olimpia.Modelo.Usuario;
import com.oscar.olimpia.Utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SpinnerFragment.OnFragmentInteractionListener {

    @Bind(R.id.txt_nombre)
    EditText txtNombre;
    @Bind(R.id.txt_cedula)
    EditText txtCedula;
    @Bind(R.id.txt_dirreccion)
    EditText txtDirreccion;
    @Bind(R.id.txt_celular)
    EditText txtCelular;
    @Bind(R.id.txtPais)
    TextView txtPais;
    @Bind(R.id.txtCiudad)
    TextView txtCiudad;
    @Bind(R.id.btnSiguiente)
    Button btnSiguiente;

    private String pais;
    private String ciudad;

    private Usuario usuario;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        res = getResources();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(this, FotoActivity.class));
        } else if (id == R.id.nav_gallery) {
            usuario = new Usuario();
            Intent intent = new Intent(this, MapLocationActivity.class);
            intent.putExtra(Constants.extraActivity.pojoUsuario, usuario);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, StateWiFiBlueActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, CargarInfoActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.btnSiguiente, R.id.txtPais, R.id.txtCiudad})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSiguiente:
                fillDataUsuario();

                break;
            case R.id.txtPais:
                FragmentManager manager = getSupportFragmentManager();
                SpinnerFragment dialogSpinnerFragment = SpinnerFragment.newInstance("pais", "");
                dialogSpinnerFragment.show(manager, "dialog");
                break;
            case R.id.txtCiudad:
                FragmentManager manager1 = getSupportFragmentManager();
                SpinnerFragment dialogSpinnerFragment1 = SpinnerFragment.newInstance(pais, "");
                dialogSpinnerFragment1.show(manager1, "dialog");
                break;
        }
    }

    private void fillDataUsuario() {
        if (validateInputs()){
            usuario = new Usuario(txtNombre.getText().toString(),
                    txtCedula.getText().toString(),
                    txtDirreccion.getText().toString(),
                    txtCelular.getText().toString(),
                    txtPais.getText().toString(),
                    txtCiudad.getText().toString(),
                    "",
                    "");

            Log.e("Objeto", usuario.toString());
            Intent intent = new Intent(this,FotoActivity.class);
            intent.putExtra(Constants.extraActivity.pojoUsuario, usuario);
            startActivity(intent);
        }
    }

    private boolean validateInputs() {
        boolean validate = true;
        if (txtNombre.getText().toString().isEmpty()){
            txtNombre.setError(res.getString(R.string.message_error));
            validate = false;
        }
        if(txtCedula.getText().toString().isEmpty()){
            txtCedula.setError(res.getString(R.string.message_error));
            validate = false;
        }
        if(txtDirreccion.getText().toString().isEmpty()){
            txtDirreccion.setError(res.getString(R.string.message_error));
            validate = false;
        }
        if(txtCelular.getText().toString().isEmpty()){
            txtCelular.setError(res.getString(R.string.message_error));
            validate = false;
        }
        if(txtPais.getText().toString().isEmpty()){
            txtPais.setError(res.getString(R.string.message_error));
            validate = false;
        }
        if(txtCiudad.getText().toString().isEmpty()){
            txtCiudad.setError(res.getString(R.string.message_error));
            validate = false;
        }
        return validate;
    }


    @Override
    public void onFragmentInteraction(String cadena, boolean isPais) {
        if (isPais){
            pais = cadena;
            txtPais.setText(cadena);
        }else {
            txtCiudad.setText(cadena);
        }
    }
}
