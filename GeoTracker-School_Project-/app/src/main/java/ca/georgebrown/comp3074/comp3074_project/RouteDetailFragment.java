package ca.georgebrown.comp3074.comp3074_project;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class RouteDetailFragment extends Fragment {


    private long routeId;
    public RouteDetailFragment() {
        // Required empty public constructor
    }
    public void setRouteId(long id){this.routeId = id;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState!=null){
            routeId = savedInstanceState.getLong("routeid");
        }
        return inflater.inflate(R.layout.fragment_route_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view != null){
            try{
                RouteDbHelper routeDbHelper = new RouteDbHelper(getContext());
                SQLiteDatabase db = routeDbHelper.getReadableDatabase();
                Cursor cursor = db.query("ROUTE",
                        new String[] {"DEPARTURE", "DESTINATION", "VIA", "DATE", "DIFFICULTY", "DURATION", "DISTANCE"},
                        "_id = ?", new String[] {Long.toString(routeId)},
                        null, null, null);
                if(cursor.moveToFirst()){
                    TextView departure = view.findViewById(R.id.textDeparture);
                    departure.setText(getString(R.string.departure , cursor.getString(0)));
                    TextView via = view.findViewById(R.id.textVia);
                    via.setText(getString(R.string.via , cursor.getString(2)));
                    TextView destination = view.findViewById(R.id.textDestination);
                    destination.setText(getString(R.string.destination , cursor.getString(1)));
                    TextView distance = view.findViewById(R.id.textDistance);
                    distance.setText(getString(R.string.distance , cursor.getString(6)));
                    TextView duration = view.findViewById(R.id.textDuration);
                    duration.setText(getString(R.string.duration , cursor.getString(5)));
                    TextView difficulty = view.findViewById(R.id.textDifficulty);
                    difficulty.setText(getString(R.string.difficulty , cursor.getString(4)));
                    TextView date = view.findViewById(R.id.textDate);
                    date.setText(getString(R.string.date, cursor.getString(3)));
                }
                cursor.close();
                db.close();
            }catch (SQLException e){
                Toast.makeText(getContext(), "Database Unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("routeId", routeId);
    }
}
