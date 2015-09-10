package com.allegion.androidtesttools;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.allegion.androidtesttools.BLeUtility.BleCentral;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements BleCentral.BleScanDeviceFoundListener, View.OnClickListener , BleCentral.BleScanDevicesListener, AdapterView.OnItemClickListener{

    ListView deviceList;
    BluetoothAdapter mBluetoothAdapter;
    BleCentral bleCentral ;
    static ArrayList<String> devicesInfo = new ArrayList<String>();
    Button stopScan ;
    Button refresh;
    public final static String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> devicesPool = new ArrayList<BluetoothDevice>();
    DeviceClickListener deviceClickListener;


public interface DeviceClickListener{
     void onDeviceClick(BluetoothDevice device);
}

    public MainActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        deviceList = (ListView) view.findViewById(R.id.deviceList);
        stopScan = (Button) view.findViewById(R.id.stopScan);
        stopScan.setOnClickListener(this);
        refresh = (Button) view.findViewById(R.id.startScan);
        refresh.setOnClickListener(this);
        bleCentral = new BleCentral();
        bleCentral.setContext(getActivity().getApplicationContext());
        bleCentral.bleScanDeviceFoundListener = this;
        displayList();
        getDeviceList();
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            deviceClickListener = (DeviceClickListener) activity;
        } catch (ClassCastException e) {
            Log.d(LOG_TAG, "Class cast exception on activity to OnLockConnectOperationListener");
        }
        setHasOptionsMenu(true);
    }
    public void getDeviceList(){
        bleCentral.startScan();
    }

    public void displayList(){
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,devicesInfo);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(this);
    }

    @Override
    public void onBleDeviceFound(BluetoothDevice device, Exception exception) {
        if(exception == null) Log.d(LOG_TAG,"exception is null");
        if(device != null ) {
            String deviceInfo = device.getName() + " " + device.getAddress() + " | " ;
            Log.d(LOG_TAG, deviceInfo);
            devicesPool.add(device);
            devicesInfo.add(deviceInfo);
            adapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.stopScan:
                bleCentral.stopScan();
                break;
            case R.id.startScan:
                devicesInfo.clear();
                adapter.notifyDataSetChanged();
                devicesPool.clear();
                deviceList.setAdapter(adapter);
                bleCentral.startScan();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBleDevicesList(ArrayList<BluetoothDevice> devices, Exception except) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice currentDevice = devicesPool.get(position);
        device_detail frag = device_detail.newInstance(currentDevice.getName(), currentDevice);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.container,frag, "detailFragment");
        transaction.commit();
        getFragmentManager().executePendingTransactions();
///        deviceClickListener.onDeviceClick(currentDevice);


    }
}
