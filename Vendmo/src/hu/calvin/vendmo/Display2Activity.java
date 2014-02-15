package hu.calvin.vendmo;

import hu.calvin.vendmo.ProductDisplayFragment.OnFragmentInteractionListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Display2Activity extends FragmentActivity implements
		ActionBar.OnNavigationListener, OnFragmentInteractionListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private LocationManager locationManager;
	private Location currentLocation;
	private SupportMapFragment mapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display2);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_products),
								getString(R.string.title_location),
								getString(R.string.title_call), }), this);
		
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		currentLocation = null;
		
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			private boolean setupDone = false;
			
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		      Log.d("Location info", location.toString());
		      if(mapFragment != null && mapFragment.getMap() != null && !setupDone){
		    	  setupDone = true;
		    	  currentLocation = location;
		    	  setupMap(mapFragment);
		      }
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display2, menu);
		return true;
	}

	
	public void setupMap(SupportMapFragment mapFragment){
		if(mapFragment != null){
			GoogleMap map = mapFragment.getMap();
			if(currentLocation != null && map != null){
				Log.d("currentLocation not null", "true");
				map.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Vendmo"));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
				map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null); 
			}
		}
		//map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}
	
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		//Fragment fragment = new DummySectionFragment();
		//Bundle args = new Bundle();
		//args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		//fragment.setArguments(args);
		///getSupportFragmentManager().beginTransaction()
				//.replace(R.id.container, fragment).commit();

		switch(position){
		case 0:
			ProductDisplayFragment pFragment = new ProductDisplayFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, pFragment).commit();
			break;
		case 1:
			mapFragment = new SupportMapFragment();
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, mapFragment).commit();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

}
