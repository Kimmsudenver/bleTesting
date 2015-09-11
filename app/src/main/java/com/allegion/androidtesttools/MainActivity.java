package com.allegion.androidtesttools;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements MainActivityFragment.DeviceClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main activity", "onCreate is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityFragment main = new MainActivityFragment();
       getFragmentManager().beginTransaction().add(R.id.fragment,main,null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onDeviceClick(BluetoothDevice device) {
        device_detail frag = device_detail.newInstance(device.getName(), device);
       //setContentView(R.layout.fragment_device_detail);
//        android.support.v4.app.FragmentManager manager = MainActivity.this.getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.container, frag);
//        transaction.commit();
    }
}
