package hu.calvin.vendmo;

import hu.calvin.vendmo.ProductDisplayFragment.OnFragmentInteractionListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link BluetoothFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link BluetoothFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class BluetoothFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	
	private BluetoothAdapter bluetoothAdapter;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment BluetoothFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static BluetoothFragment newInstance(String param1, String param2) {
		BluetoothFragment fragment = new BluetoothFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public BluetoothFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_bluetooth, container, false);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	String status;
    	if(bluetoothAdapter != null){
    		if (bluetoothAdapter.isEnabled()) {
    		    String mydeviceaddress = bluetoothAdapter.getAddress();
    		    String mydevicename = bluetoothAdapter.getName();
    		    status = mydevicename + " : " + mydeviceaddress;
    		    //((TextView)findViewById(R.id.fullscreen_content)).setText(status);
    		    
    		    Set<BluetoothDevice> bluetoothSet = bluetoothAdapter.getBondedDevices();
    		    
    		    Iterator<BluetoothDevice> bditer = bluetoothSet.iterator();
    		    BluetoothDevice linvor = null;
    		    while(bditer.hasNext()){
    		    	BluetoothDevice current = bditer.next();
    		    	if(current.getName().equals("linvor")){
    		    		linvor = current;
    		    		break;
    		    	}
    		    }
    		    
    		    try {
					BluetoothSocket socket = linvor.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					socket.connect();
					final InputStream linvorStream = socket.getInputStream();
					final OutputStream linvorOutputStream = socket.getOutputStream();
					Log.d("In Bluetooth", "After Connect");
					final Handler handler = new Handler();
				    Thread workerThread = new Thread(new Runnable(){
				    	public void run(){
				            String totalData = "after available";
				    		while(!Thread.currentThread().isInterrupted()){
					            int bytesAvailable;
								try {
									bytesAvailable = linvorStream.available();
									//Log.d("In Bluetooth", "After Available");
									//Log.d("test", totalData);
									
									if(bytesAvailable > 0){
										//stopRecordingVideo();
			                            byte[] packetBytes = new byte[bytesAvailable];
			                            int numChar = linvorStream.read(packetBytes);
			                            //Log.e("numChar", numChar + "");
			                            
	                                    totalData += new String(packetBytes, "US-ASCII");
	                                    
	                                    for(int i = 0; i < packetBytes.length; i++){
	                                    	if(packetBytes[i] == 10){
	                                    		//Log.e("bytes number", bytesAvailable + "");
	    	                                    Log.e("totalData", totalData);
	    	                                    /*if (totalData.equals("0\n")) {

	    	                                    } else if(totalData.equals("1\n")) {
	    	                                    	
	    	                                    }*/
	    	                                    totalData = "";
	                                    		break;
	                                    	}
	                                    }
	                                    
			                        }
								} catch (IOException e) {
									e.printStackTrace();
								}
				            }
				        }
				        });
				    
				        workerThread.start();
				        
    		    } catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		else{
    			((TextView)v.findViewById(R.id.text_btcontent)).setText("Bluetooth not enabled.");
    		}
    	}
		
		return v;
		
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

}
