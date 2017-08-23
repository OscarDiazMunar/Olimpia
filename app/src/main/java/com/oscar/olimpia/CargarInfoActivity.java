package com.oscar.olimpia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

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
    private Usuario usuario;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_info);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra(Constants.extraActivity.pojoUsuario);
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
        AppController.getInstance().addToRequestQueue(VolleyManager.makeRequestJsonPOST(BuildConfig.host_guardar, construirJsonObject(usuario)));
    }


    @Subscribe
    public void onEventMainThread(ErrorData event) {
        switch (event.getType()){
            case Constants.errorType.ERROR_DATA_LOAD:
                Log.e("Sesion 1 error", event.getMessage());
                break;
            default:
        }
    }


    @Subscribe
    public void onEventMainThread(SuccessData event) {
        switch (event.getType()){
            case Constants.successType.DATA_LOAD:
                Log.e("Sesion 1 success", event.getMessage());
                break;

            default:
        }
    }
}
