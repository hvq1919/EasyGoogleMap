package com.example.easygooglemap;

import maphelper.GoogleMapHelper;
import maphelper.GoogleMapHelper.OnSettingMarkerReady;
import maphelper.GoogleMapSettingMarker;
import maphelper.GoogleMapSettingMarker.CustomInfo;
import maphelper.GoogleMapSettingMarker.OnSetToolTipLayout;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnSettingMarkerReady, OnMarkerClickListener,
		OnInfoWindowClickListener, OnMarkerDragListener {

	// Data test
	private Marker mP1;
	private Marker mP2;
	private Marker mP3;

	private static final LatLng P1 = new LatLng(10.794810, 106.651325);
	private static final LatLng P2 = new LatLng(10.894710, 106.651315);
	private static final LatLng P3 = new LatLng(10.694610, 106.651335);

	/* Use to init map, move camera , get location ,etc... */
	private GoogleMapHelper mGoogleMapHelper;

	/* Use to set layout and action for marker */
	private GoogleMapSettingMarker mGoogleMapSettingMarker;

	@Override
	protected void onResume() {
		super.onResume();
		mGoogleMapHelper.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGoogleMapHelper.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		/* Start map and move camera to current location */
		mGoogleMapHelper = new GoogleMapHelper(this, mapFragment);

		/* Add the line below If you want to work with marker */
		mGoogleMapHelper.setOnMarkerReady(this);

		// TODO test
		findViewById(R.id.btnChangeLayoutToolTip).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* You can change layout of tooltip */
				mGoogleMapSettingMarker.setCustomInfo(CustomInfo.InfoContent);
			}
		});
	}

	@Override
	public void onSettingMarkerReady(GoogleMapSettingMarker mapSettingMarker) {
		mGoogleMapSettingMarker = mapSettingMarker;

		/* You must add marker after this method's ran */
		addMarker();

		/* Custom tooltip's layout for marker */
		customTooltipLayout();

	}

	/**
	 * 
	 * @param mapSettingMarker
	 */
	private void addMarker() {
		mP1 = mGoogleMapSettingMarker.addMarker(new MarkerOptions().position(P1).title("P1").snippet("snippet: P1")
				.draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

		mP2 = mGoogleMapSettingMarker.addMarker(new MarkerOptions().position(P2).title("P2").snippet("snippet: P1")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

		mP3 = mGoogleMapSettingMarker.addMarker(new MarkerOptions().position(P3).title("P3").snippet("snippet: P1")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

	}

	/**
	 * Use to set the layout of tooltip after clicking on marker
	 * 
	 * @param mapSettingMarker
	 */
	private void customTooltipLayout() {
		View toolTip = getLayoutInflater().inflate(R.layout.tooltip_layout, null);

		mGoogleMapSettingMarker
				.buildToolTip(mGoogleMapSettingMarker.new BuilderToolTip(toolTip, CustomInfo.InfoWindow));

		mGoogleMapSettingMarker.setOnMarkerClickListener(this);
		mGoogleMapSettingMarker.setOnInfoWindowClickListener(this);
		/* Notes: You must set draggable(true) for marker options */
		mGoogleMapSettingMarker.setOnMarkerDragListener(this);

		mGoogleMapSettingMarker.setOnToolTipLayout(new OnSetToolTipLayout() {
			@Override
			public void onSetLayout(Marker marker, View v) {
				ImageView imgToolTip = ((ImageView) v.findViewById(R.id.imgToolTip));
				TextView textToolTip = ((TextView) v.findViewById(R.id.textToolTip));

				textToolTip.setText(marker.getTitle());

				if (marker.equals(mP2))
					imgToolTip.setImageResource(R.drawable.arrow);
				else
					imgToolTip.setImageResource(R.drawable.ic_launcher);
			}
		});

	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		Toast.makeText(this, "onMarkerDragEnd: " + marker.getPosition().toString(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		Toast.makeText(this, " onMarkerClick ", Toast.LENGTH_SHORT).show();

		// TODO test some actions with maker when clicking on it
		if (marker.equals(mP1)) {
			// This causes the marker at mP1 to bounce into position when it
			// is clicked.
			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			final long duration = 1500;

			final Interpolator interpolator = new BounceInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
					marker.setAnchor(0.5f, 1.0f + 2 * t);

					if (t > 0.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		}

		return false;
	}

}