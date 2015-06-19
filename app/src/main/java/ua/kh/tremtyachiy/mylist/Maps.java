package ua.kh.tremtyachiy.mylist;

import android.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 16.06.2015.
 */
public class Maps implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapFragment mMapFragment;

    public void initMap(FragmentManager fragmentManager) {
        try {
            if(mMap == null){
                mMap = ((MapFragment) fragmentManager.findFragmentById(R.id.map)).getMap();
                mMapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
            }
        } catch (NullPointerException exception){
            exception.printStackTrace();
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);
        mMapFragment.getMapAsync(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(49.994399, 36.236583), 10));
    }

    public GoogleMap getMap() {
        return mMap;
    }
}
