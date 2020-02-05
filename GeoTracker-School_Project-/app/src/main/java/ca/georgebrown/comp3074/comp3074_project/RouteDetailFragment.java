package ca.georgebrown.comp3074.comp3074_project;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class RouteDetailFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private long routeId;
    private Button btnDelete, btnShare;
    private RatingBar ratingBar;
    private SQLiteDatabase db;
    private Cursor cursor;
    private AlertDialog.Builder deleteAlertDialog;
    PolylineOptions polylineOptions = new PolylineOptions();



    public RouteDetailFragment() {
        // Required empty public constructor
    }
    public void setRouteId(long id){this.routeId = id;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_detail, container, false);
        // Inflate the layout for this fragment
        if(savedInstanceState!=null){
            routeId = savedInstanceState.getLong("routeid");
        }
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view != null){
            try{
                RouteDbHelper routeDbHelper = new RouteDbHelper(getContext());
                db = routeDbHelper.getReadableDatabase();
                cursor = db.query("ROUTE",
                        new String[] {"DEPARTURE", "DESTINATION", "DATE", "NOTE", "DURATION", "RATE"},
                        "_id = ?", new String[] {Long.toString(routeId)},
                        null, null, null);
                if(cursor.moveToFirst()){
                    TextView departure = view.findViewById(R.id.textDeparture);
                    departure.setText(getString(R.string.departure , cursor.getString(0)));
                    TextView destination = view.findViewById(R.id.textDestination);
                    destination.setText(getString(R.string.destination , cursor.getString(1)));
                    TextView duration = view.findViewById(R.id.textDuration);
                    duration.setText(getString(R.string.duration , cursor.getString(4)));
                    TextView note = view.findViewById(R.id.textNote);
                    note.setText(getString(R.string.note , cursor.getString(3)));
                    TextView date = view.findViewById(R.id.textDate);
                    date.setText(getString(R.string.date, cursor.getString(2)));
                    ratingBar = view.findViewById(R.id.ratingBar);
                    ratingBar.setRating(cursor.getFloat(5));
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            ContentValues cv = new ContentValues();
                            cv.put("RATE", rating);
                            db.update("ROUTE", cv,"_id = ?", new String[] {Long.toString(routeId)});
                        }
                    });
                }

                final ArrayList<LatLng> coordList = new ArrayList<LatLng>();
                Cursor c = db.query("PATH", new String[] {"LATITUDE", "LONGITUDE"},
                        "ROUTE_ID = ?", new String[]{Long.toString(routeId)},
                        null, null, "_id");

                if(c.moveToFirst()){
                    coordList.add(new LatLng(c.getDouble(0), c.getDouble(1)));
                    while (c.moveToNext()) {
                        coordList.add(new LatLng(c.getDouble(0), c.getDouble(1)));
                    }
                }
                if(coordList.size() > 0){
                    polylineOptions.addAll(coordList);
                    polylineOptions.color(Color.RED);
                    polylineOptions.width(5);

                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap gMap) {
                            googleMap = gMap;
                            LatLng startLoc = coordList.get(0);
                            LatLng endLoc = coordList.get(coordList.size() - 1);
                            googleMap.addMarker(new MarkerOptions().position(startLoc).title("Your route starts here"));
                            googleMap.addMarker(new MarkerOptions().position(endLoc).title("Your route ends here"));
                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(startLoc).zoom(15).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            googleMap.addPolyline(polylineOptions);
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "List emty", Toast.LENGTH_SHORT).show();
                }
                c.close();
            }catch (SQLException e){
                Toast.makeText(getContext(), "Database Unavailable", Toast.LENGTH_SHORT).show();
            }
            btnDelete = view.findViewById(R.id.btnDelete);
            btnShare = view.findViewById(R.id.btnShare);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlertDialog = confirmDeleteRoute();
                    deleteAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRoute(routeId);
                            deletePath(routeId);
                            Toast.makeText(getContext(), "Successfully Deleted !!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),RouteListActivity.class);
                            startActivity(intent);
                        }
                    });
                    deleteAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    deleteAlertDialog.show();
                }
            });
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Route from " + cursor.getString(0) + " to " + cursor.getString(1);
                    String shareSub = "GEOTRACKER share";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
            });

        }
    }
    private AlertDialog.Builder confirmDeleteRoute(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        alertDialog.setTitle("Delete Confirmation");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Delete this record?");
        return alertDialog;
    }
    private void deleteRoute(long routeDeleteId){
        RouteDbHelper routeDbHelper = new RouteDbHelper(this.getContext());
        db = routeDbHelper.getWritableDatabase();
        db.delete("ROUTE", "_id = ?", new String[] {Long.toString(routeDeleteId)});
        db.close();
    }
    private void deletePath(long pathRouteId){
        RouteDbHelper routeDbHelper = new RouteDbHelper(this.getContext());
        db = routeDbHelper.getWritableDatabase();
        db.delete("PATH", "_id = ?", new String[] {Long.toString(pathRouteId)});
        db.close();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("routeId", routeId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
        mMapView.onDestroy();

    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}