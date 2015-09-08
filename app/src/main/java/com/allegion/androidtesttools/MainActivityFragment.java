package com.allegion.androidtesttools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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



    public void getDeviceList(){
        bleCentral.startScan();
    }

    public void displayList(){
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,devicesInfo);
        deviceList.setAdapter(adapter);
    }

    @Override
    public void onBleDeviceFound(BluetoothDevice device, Exception exception) {
        if(exception == null) Log.d(LOG_TAG,"exception is null");
        if(device != null ) {
            String deviceInfo = device.getName() + " " + device.getAddress() + " | " ;
            Log.d(LOG_TAG, deviceInfo);
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

    }
}
