package com.allegion.androidtesttools;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.allegion.androidtesttools.BLeUtility.BleCentral;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link device_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link device_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class device_detail extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    // TODO: Rename and change types of parameters
    private String name;
    private String address;
     TextView nameField;
    private TextView addressField;
    private TextView testTimes, successTimes, failTimes;
    private Button startTesting, stopTesting;
    private BleCentral bleCentral;

//    private OnFragmentInteractionListener mListener;
    private static BluetoothDevice mBluetoothDevice;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment device_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static device_detail newInstance(String name, BluetoothDevice device) {
        device_detail fragment = new device_detail();
        Bundle args = new Bundle();
        mBluetoothDevice = device;
        args.putString(NAME, name);
        args.putString(ADDRESS, device.getAddress());
        fragment.setArguments(args);
        return fragment;
    }

    public device_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_detail, container, false);
        bleCentral = new BleCentral();
        bleCentral.setContext(getActivity().getApplicationContext());
        bleCentral.connect(getActivity().getApplicationContext(),mBluetoothDevice);
        Bundle bundle = this.getArguments();
        if (bundle.containsKey(NAME)) {
            name = getArguments().getString(NAME);
        }
        if(bundle.containsKey(ADDRESS)){
            address = getArguments().getString(ADDRESS);
        }
        populateView(view);
        configureView(view);
        return view;
    }

    public void populateView(View view){
        nameField = (TextView)view.findViewById(R.id.deviceName);
       stopTesting = (Button) view.findViewById(R.id.stopTest);
        stopTesting.setOnClickListener(this);
        addressField = (TextView) view.findViewById(R.id.deviceAddress);
        testTimes = (TextView) view.findViewById(R.id.testTimes);
        successTimes = (TextView) view.findViewById(R.id.success);
        failTimes = (TextView) view.findViewById(R.id.fail);
        startTesting = (Button) view.findViewById(R.id.startTesting);
        startTesting.setOnClickListener(this);

    }

    public void configureView(View view){
        if(name != null && address !=null){
            nameField.setText(name);
            addressField.setText(address);
        }

    }

    public void startTest(){
        bleCentral.connect(getActivity().getApplicationContext(),mBluetoothDevice);
    }

    public void stopTest(){
        bleCentral.forceDisconnect();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // activity.setContentView(R.layout.fragment_device_detail);

//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.startTesting:
                startTest();
                break;
            case R.id.stopTest:
                stopTest();
                break;
            default:
                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
