package maphelper;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class GoogleMapHelper implements OnMapReadyCallback, LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener {
	private static final String TAG = "TAG";
	private final float ZOOM = 10;
	private static final int TIME_Interval_REQUEST = 5000; // 5s

	private Context mContext;
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	private GoogleApiClient mGoogleApiClient;

	private GoogleMapSettingMarker mapSettingMarker;

	public interface OnSettingMarkerReady {
		public void onSettingMarkerReady(GoogleMapSettingMarker mapSettingMarker);
	}

	private OnSettingMarkerReady mOnSettingMarkerReady;

	public void setOnMarkerReady(OnSettingMarkerReady onSettingMarkerReady) {
		mOnSettingMarkerReady = onSettingMarkerReady;
	}

	// These settings are the same as the settings for the map. They will in
	// fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(TIME_Interval_REQUEST)
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public GoogleMapHelper(Context context, SupportMapFragment supportMapFragment) {
		mContext = context;
		supportMapFragment.getMapAsync(this);
		init();
	}

	public GoogleMapHelper(Context context, MapFragment mapFragment) {
		mContext = context;
		mapFragment.getMapAsync(this);
		init();
	}

	private void init() {
		mGoogleApiClient = new GoogleApiClient.Builder(mContext).addApi(LocationServices.API)
				.addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

	}

	public void moveCamera(LatLng latLng, int zoom) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
	}

	public void moveCamera(LatLng latLng) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
	}

	public void animateCamera(LatLng latLng) {
		mMap.animateCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).zoom(ZOOM).build()),
				null);
	}

	public void animateCamera(LatLng latLng, int zoom) {
		mMap.animateCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).zoom(zoom).build()),
				null);
	}

	public void setZoomButtonsEnabled(boolean shouldEnable) {
		mUiSettings.setZoomControlsEnabled(shouldEnable);
	}

	public void setMyLocationButtonEnabled(boolean shouldEnable) {
		mUiSettings.setMyLocationButtonEnabled(shouldEnable);
	}

	public void onResume() {
		mGoogleApiClient.connect();
	}

	public void onPause() {
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;

		mMap.setMyLocationEnabled(true);
		mUiSettings = mMap.getUiSettings();

		mapSettingMarker = new GoogleMapSettingMarker(map);
		if (mOnSettingMarkerReady != null)
			mOnSettingMarkerReady.onSettingMarkerReady(mapSettingMarker);
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged:" + location.getLatitude());
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, REQUEST, this); // LocationListener
		if (mGoogleApiClient.isConnected()) {
			Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			animateCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
			Log.d(TAG, "onConnected:" + currentLocation.toString());

		}

	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnectionSuspended(int cause) {

	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}
}
