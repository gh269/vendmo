package hu.calvin.vendmo.hardware;

import hu.calvin.vendmo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class BluetoothUtils {
	private BluetoothAdapter bluetoothAdapter;
	private Context context;
	BluetoothSocket socket;
	BluetoothDevice device;
	OutputStream outputStream;
	InputStream inputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	
	public BluetoothUtils(Context newContext){
		this.context = newContext;
		bluetoothAdapter = (BluetoothAdapter) context.getSystemService(Context.BLUETOOTH_SERVICE);
		
	}
	
	public void connect(){
    	String status;
    	if(bluetoothAdapter != null){
    		if (bluetoothAdapter.isEnabled()) {
    		    String mydeviceaddress = bluetoothAdapter.getAddress();
    		    String mydevicename = bluetoothAdapter.getName();
    		    status = mydevicename + " : " + mydeviceaddress;
    		    
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
					socket = linvor.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					socket.connect();
					inputStream = socket.getInputStream();
					outputStream = socket.getOutputStream();
					
				        
    		    } catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		else{
    			
    		}
    	}
	}
	
	private void startListen(){
		final Handler handler = new Handler();
		final byte delimiter = 10;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
	    Thread workerThread = new Thread(new Runnable(){
	    	public void run(){
	            String totalData = "after available";
	    		while(!Thread.currentThread().isInterrupted()){
		            int bytesAvailable;
					try {
						bytesAvailable = inputStream.available();
						//Log.d("In Bluetooth", "After Available");
						//Log.d("test", totalData);
						
						if(bytesAvailable > 0){
							//stopRecordingVideo();
                            byte[] packetBytes = new byte[bytesAvailable];
                            int numChar = inputStream.read(packetBytes);
                            //Log.e("numChar", numChar + "");
                            
                            totalData += new String(packetBytes, "US-ASCII");
                            
                            for(int i = 0; i < packetBytes.length; i++){
                            	if(packetBytes[i] == 10){
                            		//Log.e("bytes number", bytesAvailable + "");
                                    Log.e("totalData", totalData);
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
		
	}
	
	private class AcceptThread extends Thread {
	    private final BluetoothServerSocket mmServerSocket;
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                manageConnectedSocket(socket);
	                mmServerSocket.close();
	                break;
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
}
