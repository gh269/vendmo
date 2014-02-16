package hu.calvin.vendmo.hardware;

import hu.calvin.vendmo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
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
	boolean stopWorker;
	BluetoothSocket fallbackSocket;
	
	public BluetoothUtils(Context newContext){
		this.context = newContext;
		//bluetoothAdapter = ((BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getDefaultAdapter();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
    		    	if(current.getName().contains("Bluetooth_V3")){
    		    		linvor = current;
    		    		//linvor = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(current.getAddress());
    		    		break;
    		    	}
    		    }
    		    
    		    if(linvor != null){
	    		    try {
						socket = linvor.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
	    		    	//Method m = linvor.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
	    		    	//socket = (BluetoothSocket) m.invoke(linvor, 1);
	    		    	bluetoothAdapter.cancelDiscovery();
	    		    	socket.connect();
	    		    	try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//socket.connect();
						inputStream = socket.getInputStream();
						outputStream = socket.getOutputStream();
						
					        
	    		    } catch (IOException e) {
						//e.printStackTrace();
						Class<?> clazz = socket.getRemoteDevice().getClass();
						Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};

						Method m = null;
							try {
								m = clazz.getMethod("createRfcommSocket", paramTypes);
							} catch (NoSuchMethodException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Object[] params = new Object[] {Integer.valueOf(1)};
	
							if(m != null){
							try {
								fallbackSocket = (BluetoothSocket) m.invoke(socket.getRemoteDevice(), params);
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						try {
							Log.d("Bluetooth utils", "in fallback");
							fallbackSocket.connect();
							socket.connect();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}
						
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						
						e1.printStackTrace();
					}
	    		}
    		    }
    		else{
    			
    		}
    		    
    	}
	}
	
	public void startListen(){
		final Handler handler = new Handler();
		final byte delimiter = '\n';
		readBufferPosition = 0;
		stopWorker = false;
		readBuffer = new byte[1024];
	    //workerThread = new Thread(new Runnable(){
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected Void doInBackground(Void... params) {
	    		Log.d("listening", "worker beginning");
	            String totalData = "after available";
	    		while(!Thread.currentThread().isInterrupted() && !stopWorker){
		            int bytesAvailable;
					try {
						bytesAvailable = inputStream.available();
						//Log.d("In Bluetooth", "After Available");
						//Log.d("test", totalData);
						
						if(bytesAvailable > 0){
                            byte[] packetBytes = new byte[bytesAvailable];
                            int numChar = inputStream.read(packetBytes);
                            //Log.e("numChar", numChar + "");
                            
                            //totalData += new String(packetBytes, "US-ASCII");
                            
                            for(int i = 0; i < bytesAvailable; i++){
                            	byte b = packetBytes[i];
                            	if(b==delimiter){
                            		byte[] encodedBytes = new byte[readBufferPosition];
                            		System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            		final String data = new String(encodedBytes, "US-ASCII");
                            		readBufferPosition = 0;
                            		handler.post(new Runnable(){
                            			public void run(){
                            				Log.d("btUtils", "data");
                            			}
                            		});
                            	}
                            	else{
                            		readBuffer[readBufferPosition++] = b;
                            	}
                            }
                            
                        }
					} catch (IOException e) {
						e.printStackTrace();
						stopWorker = true;
					}
	            }
	    		return null;
	        }
	       }.execute(null,null,null);
		
	}
	
	public void sendData(TextView textView) throws IOException{
		String msg = textView.getText().toString();
		msg += "";
		  outputStream.write(msg.getBytes());
		  outputStream.write(msg.getBytes());
		  outputStream.flush();
		  outputStream.close();
		  //socket.close();
		}
	void closeBT() throws IOException{
		stopWorker = true;
		outputStream.close();
		inputStream.close();
		socket.close();
	} 
}
