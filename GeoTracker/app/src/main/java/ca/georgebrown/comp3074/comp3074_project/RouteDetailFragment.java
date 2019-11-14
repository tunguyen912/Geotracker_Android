package ca.georgebrown.comp3074.comp3074_project;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



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
            Route route = Route.routes[(int) routeId];
            TextView departure = view.findViewById(R.id.textDeparture);
            departure.setText(getString(R.string.departure , route.getStart()));
            TextView via = view.findViewById(R.id.textVia);
            via.setText(getString(R.string.via , route.getVia()));
            TextView destination = view.findViewById(R.id.textDestination);
            destination.setText(getString(R.string.destination , route.getStop()));
            TextView distance = view.findViewById(R.id.textDistance);
            distance.setText(getString(R.string.distance , route.getDistance()));
            TextView duration = view.findViewById(R.id.textDuration);
            duration.setText(getString(R.string.duration , route.getDuration()));
            TextView difficulty = view.findViewById(R.id.textDifficulty);
            difficulty.setText(getString(R.string.difficulty , route.getDifficulty()));
            TextView date = view.findViewById(R.id.textDate);
            date.setText(getString(R.string.date, route.getDate()));
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("workoutId", routeId);
    }
}
