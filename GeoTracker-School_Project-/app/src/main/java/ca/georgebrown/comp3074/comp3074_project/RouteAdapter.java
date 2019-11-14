/*package ca.georgebrown.comp3074.comp3074_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RouteAdapter extends ArrayAdapter<Route> {
    private int layout;
    public RouteAdapter(@NonNull Context context, int resource, @NonNull Route[] objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView ==null){
            LayoutInflater inflater =LayoutInflater.from(getContext());
            convertView =inflater.inflate(layout, null);
        }
        Route route = getItem(position);
        TextView date = convertView.findViewById(R.id.date);
        TextView departure = convertView.findViewById(R.id.departure);
        TextView destination = convertView.findViewById(R.id.destination);
        TextView distance = convertView.findViewById(R.id.distance);
        TextView duration = convertView.findViewById(R.id.duration);
        departure.setText(route.getStart());
        destination.setText(route.getStop());
        date.setText(route.getDate());
        distance.setText(route.getDistance());
        duration.setText(route.getDuration());
        return convertView;
    }
}*/
