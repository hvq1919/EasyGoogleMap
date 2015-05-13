package maphelper;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapSettingMarker {
	private GoogleMap mMap;

	public enum CustomInfo {
		Default, InfoWindow, InfoContent
	};

	private CustomInfo currentCustomInfo = CustomInfo.InfoWindow;

	public interface OnSetToolTipLayout {
		/**
		 * To set call back layout of marker
		 * 
		 * @param marker
		 *            The marker you want to set
		 * @param v
		 *            Parent view
		 */
		public void onSetLayout(Marker marker, View v);
	}

	private OnSetToolTipLayout mOnSetToolTipLayout;

	public void setOnToolTipLayout(OnSetToolTipLayout onSetToolTipLayout) {
		mOnSetToolTipLayout = onSetToolTipLayout;
	}

	public GoogleMapSettingMarker(GoogleMap mMap) {
		super();
		this.mMap = mMap;
	}

	public Marker addMarker(MarkerOptions markerOptions) {
		return mMap.addMarker(markerOptions);
	}

	public void buildToolTip(BuilderToolTip toolTip) {
		mMap.setInfoWindowAdapter(toolTip);
	}

	public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
		mMap.setOnMarkerClickListener(onMarkerClickListener);
	}

	public void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
		mMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
	}

	public void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
		mMap.setOnMarkerDragListener(onMarkerDragListener);
	}

	public void setCustomInfo(CustomInfo customInfo){
		currentCustomInfo = customInfo;
	}
	
	public void clear(){
		mMap.clear();
	}
	
	/** Demonstrates customizing the info window and/or its contents. */
	public class BuilderToolTip implements InfoWindowAdapter {
		// The tooltip view that it'll be shown when clicking on marker
		private final View mTooltip;

		/**
		 * 
		 * @param window
		 *            The Tooltip view
		 */
		public BuilderToolTip(View toolTip) {
			mTooltip = toolTip;
		}

		/**
		 * 
		 * @param window
		 *            The Tooltip view
		 * @param customInfo
		 */
		public BuilderToolTip(View toolTip, CustomInfo customInfo) {
			mTooltip = toolTip;
			currentCustomInfo = customInfo;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			if (currentCustomInfo == CustomInfo.InfoWindow) {
				if (mOnSetToolTipLayout != null)
					mOnSetToolTipLayout.onSetLayout(marker, mTooltip);
				return mTooltip;
			} else
				return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			if (currentCustomInfo == CustomInfo.InfoContent) {
				if (mOnSetToolTipLayout != null)
					mOnSetToolTipLayout.onSetLayout(marker, mTooltip);
				return mTooltip;
			} else
				return null;
		}

	}

}
