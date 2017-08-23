package com.oscar.olimpia;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.oscar.olimpia.Modelo.Usuario;
import com.oscar.olimpia.Utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StateWiFiBlueActivity extends AppCompatActivity {
    public static int REQUEST_BLUETOOTH = 1;
    @Bind(R.id.imgWifi)
    ImageView imgWifi;
    @Bind(R.id.imgBluetooth)
    ImageView imgBluetooth;
    @Bind(R.id.btnSiguiente)
    Button btnSiguiente;
    private BluetoothAdapter bluetoothAdapter;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wi_fi_blue);
        ButterKnife.bind(this);
        usuario = (Usuario) getIntent().getSerializableExtra(Constants.extraActivity.pojoUsuario);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(bluetoothReceiver, filter);
        activarBluetooth();

    }

    private void activarBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            imgBluetooth.setImageResource(R.drawable.bluetooth_off);
        }else {
            imgBluetooth.setImageResource(R.drawable.bluetooth_on);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("en pause","aqui");
    }

    @OnClick(R.id.btnSiguiente)
    public void onViewClicked() {
        Intent intent = new Intent(this, CargarInfoActivity.class);
        intent.putExtra(Constants.extraActivity.pojoUsuario, usuario);
        startActivity(intent);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        imgBluetooth.setImageResource(R.drawable.bluetooth_off);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        imgBluetooth.setImageResource(R.drawable.bluetooth_on);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (wifiState){
                    case WifiManager.WIFI_STATE_DISABLED:
                        imgWifi.setImageResource(R.drawable.wifi_off);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        imgWifi.setImageResource(R.drawable.wifi_on);
                        break;
                }
            }
        }
    };
}
