package com.allegion.androidtesttools.BLeUtility;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.allegion.androidtesttools.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by kimbui on 9/8/15.
 */
public class BleCentral extends Service implements BluetoothAdapter.LeScanCallback{
    public static BluetoothDevice bleDevice;
    public static Context mContext;
    public static BluetoothAdapter mBluetoothAdapter;
    public Map<String, BluetoothDevice> allDevices = new HashMap<String, BluetoothDevice>();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    static ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    public final static String LOG_TAG = BleCentral.class.getSimpleName();



    public static List<String> swordfish;
    public static List<String> gateway;




    public interface BleScanDevicesListener{
        void onBleDevicesList(ArrayList<BluetoothDevice> devices, Exception except );
    }

    public interface BleScanDeviceFoundListener{
        void onBleDeviceFound ( BluetoothDevice device, Exception exception);
    }

    public BleScanDevicesListener bleScanDevicesListener;
    public BleScanDeviceFoundListener bleScanDeviceFoundListener;


    public BleCentral(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setContext(Context context){
        mContext = context;
        BluetoothManager mBluetoothManager = (BluetoothManager) context.getSystemService(context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        swordfish = Arrays.asList(mContext.getResources().getStringArray(R.array.nde));

    }


    public void startScan(){
        Log.d(LOG_TAG,"Start scanning");
//        if(scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor.isShutdown()|| scheduledThreadPoolExecutor.isTerminated()){
//            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
//            scheduledThreadPoolExecutor.scheduleAtFixedRate(mStartRunnable,0,20, TimeUnit.SECONDS);
//            scheduledThreadPoolExecutor.scheduleAtFixedRate(mStopRunnable,10,20,TimeUnit.SECONDS);
        mBluetoothAdapter.startLeScan(this);

       // }
    }

    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            mBluetoothAdapter.startLeScan(BleCentral.this);
        }
    };

    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            mBluetoothAdapter.stopLeScan(BleCentral.this);
            bleScanDevicesListener.onBleDevicesList(devices, null);
        }
    };


    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        //ParcelUuid[] uui = device.getUuids();
        if(device !=null) {
            String deviceName = device.getName() == null ? "" : device.getName();
            String deviceAddress = device.getAddress() == null ? "" : device.getAddress();
            Log.d(LOG_TAG, " Information about the device " + " | " + deviceName + " | " + deviceAddress);
            bleScanDeviceFoundListener.onBleDeviceFound(device, null);
            devices.add(device);

        }

    }

    public void stopScan(){
        Log.d(LOG_TAG,"stop scanning");
        if(scheduledThreadPoolExecutor != null)
            scheduledThreadPoolExecutor.shutdown();

        mBluetoothAdapter.stopLeScan(this);

    }

}
