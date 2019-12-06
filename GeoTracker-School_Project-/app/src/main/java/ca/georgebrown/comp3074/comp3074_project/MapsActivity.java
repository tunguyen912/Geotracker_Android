package ca.georgebrown.comp3074.comp3074_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, FetchAddressTask.OnTaskCompleted {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TRACKING_LOCATION_KEY = "tracking_key";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private List<String> tempAddress = new ArrayList<String>();
    private List<double[]> tempLatLog = new ArrayList<double[]>();

    private boolean trackingLocation;
    private ImageView startTrack;
    private String newDeparture, newDestination, newDistance, newDuration, newVia, newDate, newDifficulty;
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (savedInstanceState != null) {
            trackingLocation = savedInstanceState.getBoolean(TRACKING_LOCATION_KEY);
        }

        startTrack = findViewById(R.id.btnTrack);
        startTrack.setImageResource(R.drawable.start);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!trackingLocation) {
                    startTrackingLocation();
                }else{
                    stopTrackingLocation();
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (trackingLocation) {
                    new FetchAddressTask(MapsActivity.this, MapsActivity.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };

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
        // Add a marker in Sydney and move the camera
        //float zoom = 15;
        //LatLng gbc = new LatLng(43.676209, -79.410703);
        //mMap.addMarker(new MarkerOptions().position(gbc).title("Marker in GBC"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gbc, zoom));
    }
    private void startTrackingLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }else {
            fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, null );
        }
        trackingLocation = true;
        Toast.makeText(getApplicationContext(),"Start Tracking" , Toast.LENGTH_SHORT).show();
        startTrack.setImageResource(R.drawable.stop);
    }
    private void stopTrackingLocation(){
        if(trackingLocation){
            trackingLocation = false;
            Toast.makeText(getApplicationContext(),"Stop Tracking" , Toast.LENGTH_SHORT).show();
            startTrack.setImageResource(R.drawable.start);
            fusedLocationClient.removeLocationUpdates(locationCallback);

            newDeparture = tempAddress.get(0);
            newDestination = tempAddress.get(tempAddress.size() - 1);
            newDistance = "Test Distance";
            newDuration = "Test Duration";
            newVia = "Test Via";
            newDate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));
            newDifficulty = "Test Difficulty";
            showDialog(newDeparture, newDestination, newDistance, newDuration);

        }
    }
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startTrackingLocation();
            }else {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showDialog(String departure, String destination, String distance, String duration){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCanceledOnTouchOutside(false);

        Button btnSave, btnCancel;
        final EditText txtDeparture, txtDestination;
        EditText txtDuration, txtDistance;

        btnSave = dialog.findViewById(R.id.btnSaveDialog);
        btnCancel = dialog.findViewById(R.id.btnCancelDialog);
        txtDeparture = dialog.findViewById(R.id.txtDepartureDialog);
        txtDestination = dialog.findViewById(R.id.txtDestinationDialog);
        txtDuration = dialog.findViewById(R.id.txtDurationDialog);
        txtDistance = dialog.findViewById(R.id.txtDistanceDialog);

        txtDeparture.setText(departure);
        txtDestination.setText(destination);
        txtDistance.setText(distance);
        txtDuration.setText(duration);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDeparture = txtDeparture.getText().toString();
                newDestination = txtDestination.getText().toString();
                insertRoute(db, newDeparture, newDestination, newVia, newDate, newDuration, newDistance, newDifficulty);
                cursor = getListId(db);
                int lastRouteId = -1;
                if(cursor.moveToLast()){
                    lastRouteId = cursor.getInt(0);
                }
                Toast.makeText(MapsActivity.this, tempLatLog.size() + " " + lastRouteId , Toast.LENGTH_SHORT).show();
                cursor.close();
                for (int n = 0; n < tempLatLog.size(); n++) {
                    insertPath(db, lastRouteId, (float) tempLatLog.get(n)[0], (float) tempLatLog.get(n)[1]);
                }
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                tempAddress.clear();
                tempLatLog.clear();
                dialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempAddress.clear();
                tempLatLog.clear();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void insertRoute(SQLiteDatabase db, String newDeparture, String newDestination, String newVia,
                             String newDate, String newDuration, String newDistance, String newDifficulty){
        RouteDbHelper routeDbHelper = new RouteDbHelper(this.getApplication());
        db = routeDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DEPARTURE", newDeparture);
        contentValues.put("DESTINATION", newDestination);
        contentValues.put("VIA", newVia);
        contentValues.put("DATE", newDate);
        contentValues.put("DIFFICULTY", newDifficulty);
        contentValues.put("DURATION", newDuration);
        contentValues.put("DISTANCE", newDistance);
        db.insert("ROUTE", null, contentValues);
        db.close();
    }
    private Cursor getListId(SQLiteDatabase db){
        RouteDbHelper routeDbHelper = new RouteDbHelper(this);
        db = routeDbHelper.getReadableDatabase();
        return db.query("ROUTE",
                new String[] {"_id"},
                null, null,
                null, null, "_id ");
    }
    private void insertPath(SQLiteDatabase db, int routeId, float lat, float lng){
        RouteDbHelper routeDbHelper = new RouteDbHelper(this.getApplication());
        db = routeDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ROUTE_ID", routeId);
        contentValues.put("LATITUDE", lat);
        contentValues.put("LONGITUDE", lng);
        db.insert("PATH", null, contentValues);
        db.close();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.history:
                intent = new Intent(this, RouteListActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        tempAddress.add(result);
        //final Location[] currentLocation = new Location[1];
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    //currentLocation[0] = location;
                    /*Toast.makeText(MapsActivity.this, currentLocation[0].getLatitude()+" "+
                            currentLocation[0].getLongitude(), Toast.LENGTH_SHORT).show();
                    tempLatLog.add(new double[] {currentLocation[0].getLatitude(), currentLocation[0].getLongitude()});*/
                    Toast.makeText(getApplicationContext(), location.getLatitude() +" "+ location.getLongitude(),
                            Toast.LENGTH_SHORT).show();
                    tempLatLog.add(new double[] {location.getLatitude(), location.getLongitude()});
                    Toast.makeText(MapsActivity.this, tempLatLog.size()+"", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TRACKING_LOCATION_KEY, trackingLocation);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        if (trackingLocation) {
            stopTrackingLocation();
            trackingLocation = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (trackingLocation) {
            startTrackingLocation();
        }
        super.onResume();
    }
}