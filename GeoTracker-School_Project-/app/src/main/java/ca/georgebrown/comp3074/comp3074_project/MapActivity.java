package ca.georgebrown.comp3074.comp3074_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MapActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    private static final int REQUEST_LOCATION_PERMISION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private boolean trackingLocation;
    private ImageView startTrack;
    private String newDeparture, newDestination, newDistance, newDuration, newVia, newDate, newDifficulty;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        startTrack = findViewById(R.id.btnTrack);

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
                    new FetchAddressTask(MapActivity.this, MapActivity.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };
    }
    private void startTrackingLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISION);
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

            newDeparture = "Test Departure";
            newDestination = "Test Destination";
            newDistance = "Test Distance";
            newDuration = "Test Duration";
            newVia = "Test Via";
            newDate = "Test Date";
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
        if(requestCode == REQUEST_LOCATION_PERMISION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startTrackingLocation();
            }else {
                Toast.makeText(this, "Permision is not granted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
