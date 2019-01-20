package com.forjapps.gerardo.pruebas.instantAppFeature;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GERODebug";
    private static final int REQUEST_ENABLE_BT = 1000;
    private static final int REQUEST_DISCOVERABLE_BT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: works!");


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
        if (id == R.id.activarBT) {
            manejadorBT();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG,"SI BT");
                verDispositivos();
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG,"CANCELADO BT");
            }
        }
        if(requestCode == REQUEST_DISCOVERABLE_BT) {

        }
    }

    private void manejadorBT() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Log.d(TAG,"No hay bluetooth disponible");
        }else{
            Log.d(TAG,"Si hay bluetooth");
            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            }else{
                mBluetoothAdapter.disable();
                Log.d(TAG,"Se deshabilito");
            }
        }
    }

    public void toggleBT(View view) {
       manejadorBT();
    }

    public void verDispositivos() {
        Log.d(TAG,"Ver dispositivos");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,R.layout.lista_dispositivos,R.id.textoDevice);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, String.valueOf(pairedDevices));
        if(pairedDevices.size() > 0) {
            Log.d(TAG,"Hay dispositivos.");
            for (BluetoothDevice device : pairedDevices) {
                mArrayAdapter.add(device.getName()+ "\n" +device.getAddress());
                Log.d(TAG,device.getName()+ "\n" +device.getAddress());

            }
           if(!mBluetoothAdapter.isDiscovering()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVERABLE_BT );
           }


        }else{
            Log.d(TAG,"No hay dispositivos.");
            Toast.makeText(this, "No hay dispositivos.", Toast.LENGTH_SHORT).show();
        }

    }

}
