package com.oscar.olimpia;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oscar.olimpia.Managers.VolleyManager;
import com.oscar.olimpia.Modelo.ErrorData;
import com.oscar.olimpia.Modelo.SuccessData;
import com.oscar.olimpia.Modelo.Usuario;
import com.oscar.olimpia.Utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CargarInfoActivity extends AppCompatActivity {

    @Bind(R.id.btnGuardar)
    Button btnGuardar;
    @Bind(R.id.checkBoxSMS)
    CheckBox checkBoxSMS;
    @Bind(R.id.txtcelular_sms)
    EditText txtcelularSms;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layout)
    ConstraintLayout layout;
    private Usuario usuario;
    private EventBus eventBus = EventBus.getDefault();
    private boolean checkedSMS;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_info);
        ButterKnife.bind(this);
        res = getResources();
        usuario = (Usuario) getIntent().getSerializableExtra(Constants.extraActivity.pojoUsuario);
        txtcelularSms.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    private JSONObject construirJsonObject(Usuario usuario) {
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("nombre", usuario.getNombre());
            jsonData.put("cedula", usuario.getCedula());
            jsonData.put("dirreccion", usuario.getDirreccion());
            jsonData.put("celular", usuario.getCelular());
            jsonData.put("pais", usuario.getPais());
            jsonData.put("ciudad", usuario.getCiudad());
            jsonData.put("foto", usuario.getFoto());
            jsonData.put("coordenadas", usuario.getCoordenadas());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("eldatajson", jsonData.toString());
        return jsonData;

    }

    @OnClick(R.id.btnGuardar)
    public void onViewClicked() {
        progressBar.setVisibility(View.VISIBLE);
        btnGuardar.setEnabled(false);
        if (checkedSMS) {
            if (!txtcelularSms.getText().toString().isEmpty()) {
                enviarSMS(txtcelularSms.getText().toString(), "Los datos han sido enviados a la base de datos");
            } else {
                txtcelularSms.setError(res.getString(R.string.message_error));
            }

        }
        AppController.getInstance().addToRequestQueue(VolleyManager.makeRequestJsonPOST(BuildConfig.host_guardar, construirJsonObject(usuario)));
    }


    @Subscribe
    public void onEventMainThread(ErrorData event) {
        switch (event.getType()) {
            case Constants.errorType.ERROR_DATA_LOAD:
                Log.e("Sesion 1 error", event.getMessage());
                progressBar.setVisibility(View.GONE);
                //crearCuadroAlerta("Carga exitosa", "Los datos fueron gardos cone exito");
                Snackbar.make(layout,"Los datos fueron guardados con exito", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), getResources().getString(R.string.mensaje_datosOK), Toast.LENGTH_LONG).show();
                btnGuardar.setEnabled(true);
                break;
            default:
        }
    }


    @Subscribe
    public void onEventMainThread(SuccessData event) {
        switch (event.getType()) {
            case Constants.successType.DATA_LOAD:
                Log.e("Sesion 1 success", event.getMessage());
                progressBar.setVisibility(View.GONE);
                crearCuadroAlerta("Carga exitosa", "Los datos fueron gardos cone exito");
                btnGuardar.setEnabled(true);
                break;

            default:
        }
    }

    public void enviarSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_menEnviado), Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_fallo_envio), Toast.LENGTH_LONG).show();

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_noServicio), Toast.LENGTH_LONG).show();

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_noPdu), Toast.LENGTH_LONG).show();

                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        //Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_radioApagado),Toast.LENGTH_LONG).show();

                        break;
                }
            }
        }, new IntentFilter(SENT));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_menEntregadoOpe), Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mensajeToast_menNoEntregadoOpe), Toast.LENGTH_LONG).show();

                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 3);
            return;
        }
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        //CuadroProgreso tareaCuadroProgreso = new CuadroProgreso();
        //tareaCuadroProgreso.execute();
    }


    public void crearCuadroAlerta(String titulo, String mensaje) {
        AlertDialog.Builder cuadroAlerta = new AlertDialog.Builder(getApplicationContext());
        cuadroAlerta.setTitle(titulo);
        cuadroAlerta.setMessage(mensaje).setCancelable(false).setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog envioOK = cuadroAlerta.create();

        envioOK.show();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checkedSMS?
        checkedSMS = checkBoxSMS.isChecked();
        if (checkedSMS) {
            txtcelularSms.setEnabled(true);
        } else {
            txtcelularSms.setEnabled(false);
        }
    }
}
