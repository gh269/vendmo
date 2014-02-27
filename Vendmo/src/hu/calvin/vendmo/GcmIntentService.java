package hu.calvin.vendmo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.physicaloid.lib.Physicaloid;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;  
import android.widget.TextView;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    static final String TAG = "GCMDemo";
	private static Physicaloid mPhysicaloid;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	mPhysicaloid = new Physicaloid(this);
		mPhysicaloid.open();
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	handleGCM("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
            	handleGCM("Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                /*for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }*/
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                String extraString = extras.toString();
                if(extraString.length() >= 8){
                	handleGCM(extraString.substring(7, extraString.length()-1));
                	Log.i(TAG, "Received: " + extras.toString());
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void handleGCM(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DispenseActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d("service", msg);
        try {
			JSONObject jsonObject = new JSONObject(msg);
			String amount = jsonObject.getString("amount");
			JSONObject user = (jsonObject.getJSONObject("actor")).getJSONObject("user");
			String firstName = user.getString("first_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	if(msg.contains("lefta")){
    		if(!mPhysicaloid.isOpened()){
    			mPhysicaloid.open();
    		}
    		if(mPhysicaloid.isOpened()){
    			mPhysicaloid.setBaudrate(9600);
    			byte[] buf = new byte[1];
    			buf[0] = 'l';
    	        mPhysicaloid.write(buf, buf.length);
    	        mPhysicaloid.close();
    		}
    		
    		//Intent intent = new Intent(this, DispenseActivity.class);
    		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		//intent.putExtra("sentence", sentence);
    		//getApplication().startActivity(intent);
    	}
    	else if(msg.contains("righta")){
    		if(!mPhysicaloid.isOpened()){
    			mPhysicaloid.open();
    		}
    		if(mPhysicaloid.isOpened()){
    			mPhysicaloid.setBaudrate(9600);
    			byte[] buf = new byte[1];
    			buf[0] = 'r';
    	        mPhysicaloid.write(buf, buf.length);
    	        mPhysicaloid.close();
    		}
    		
    		//Intent intent = new Intent(this, DispenseActivity.class);
    		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		//getApplication().startActivity(intent);
    	}
    }
}