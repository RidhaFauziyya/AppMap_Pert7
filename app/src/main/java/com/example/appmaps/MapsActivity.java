package com.example.appmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.appmaps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Membuat marker default
        LatLng vokasiUgm = new LatLng(-7.774786, 110.374559);
        LatLng kos = new LatLng(-7.777276, 110.360068);
        LatLng tugu = new LatLng(-7.782964, 110.367136);

        //Memberikan title pada marker
        mMap.addMarker(new MarkerOptions().position(vokasiUgm).title("Marker in Vocational School UGM"));
        mMap.addMarker(new MarkerOptions().position(kos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title("Marker in Kos Putri Orange"));
        mMap.addMarker(new MarkerOptions().position(tugu).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).title("Marker in Vocational School UGM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vokasiUgm));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vokasiUgm, 13));

        //Memanggil fungsi yang sudah dibuat
        setMapStyle();
        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.satelite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMapLongClick(final GoogleMap mMap) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                String text = String.format(Locale.getDefault(),
                        "Lat : %1$.5f, Long : %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Dropped pin")
                        .snippet(text));
            }
        });
    }

    private void setPoiClick(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(pointOfInterest.latLng)
                        .title(pointOfInterest.name));
                poiMarker.showInfoWindow();
            }
        });
    }

    //Mengecek permisi
    private void enableMyLocation(){
        //Kalau uda set locasi di map
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        //Kalau belum request permission
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Menyesuasikan request code di fungsi enableMyLocation
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                    enableMyLocation();
                    break;
                }
        }
    }

    public void setMapStyle(){
        boolean result = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style));
        if (result){
            Log.e("Map", "Error set style map");
        }
    }
}