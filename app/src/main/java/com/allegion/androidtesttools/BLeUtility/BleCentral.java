package com.allegion.androidtesttools.BLeUtility;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
    private BluetoothGatt gatt;
    private static  long connectStartTime, connectTime, disconnectStartTime, disconnectTime;
    private static final String LOG_TAG = BleCentral.class.getSimpleName();
    private static Context mcontext;




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
        if(gatt != null ){gatt.close();}

    }


    public void startScan(){
        Log.d(LOG_TAG,"Start scanning");
        devices.clear();
        mBluetoothAdapter.startLeScan(BleCentral.this);
//        if(scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor.isShutdown()|| scheduledThreadPoolExecutor.isTerminated()) {
//            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
//            scheduledThreadPoolExecutor.scheduleAtFixedRate(mStartRunnable,0,20, TimeUnit.SECONDS);
//         scheduledThreadPoolExecutor.scheduleWithFixedDelay(mStopRunnable, 10, 20, TimeUnit.SECONDS);
            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG,"stop scan runnable is called");
                    mBluetoothAdapter.stopLeScan(BleCentral.this);
                }
            },10000);
        //}
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
            //bleScanDevicesListener.onBleDevicesList(devices, null);
        }
    };


    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        //ParcelUuid[] uui = device.getUuids();
        if(device !=null) {
            String deviceName = device.getName() == null ? "" : device.getName();
            String deviceAddress = device.getAddress() == null ? "" : device.getAddress();
            Advertisement advert = AdvertisementParser.parseUUIDs(scanRecord);
            if(!isAllegionDevice(advert))
                return;
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

    private boolean isAllegionDevice(Advertisement advertisement) {
        if (!advertisement.isAllegion())
            return false;

//        for (UUID service : advertisement.getUuids())
//            for (UUID lockService : serviceUuids)
//                if (service.compareTo(lockService) == 0)
//                    return true;
//
//        Log.d(LOG_TAG, "Failed UUID check");
//
//        return false;
        return true;
    }

    public void connect(Context context,BluetoothDevice device){
        Log.d(LOG_TAG,"connect is called");
        bleDevice = device;
        mcontext = context;
        gatt = device.connectGatt(context, false, mGattCallback);
        gatt.connect();
        connectStartTime = System.currentTimeMillis()/1000;

    }

    public void disconnect(){
        Log.d(LOG_TAG, "diconnected is called");
        gatt.disconnect();

        gatt = bleDevice.connectGatt(mcontext,false,mGattCallback);
        disconnectStartTime = System.currentTimeMillis()/1000;
        gatt.connect();
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothProfile.STATE_CONNECTED){
                connectTime = System.currentTimeMillis()/1000 - connectStartTime;
                Log.d("TIME", "connect Time " + +connectTime);
                disconnect();

            }
            else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                disconnectTime = System.currentTimeMillis()/1000 - disconnectStartTime;
                Log.d("TIME", "disconnect Time : " + disconnectTime);
                connect(mcontext,bleDevice);
            }
            else if(newState == BluetoothProfile.STATE_CONNECTING){
                Log.d(LOG_TAG,"Connecting");
            }
            else if( newState == BluetoothProfile.STATE_DISCONNECTING){
                Log.d(LOG_TAG,"Disconnecting");
            }
        }
    };

    public void forceDisconnect(){
        gatt.disconnect();
        gatt.close();
        gatt = null;
    }




}
