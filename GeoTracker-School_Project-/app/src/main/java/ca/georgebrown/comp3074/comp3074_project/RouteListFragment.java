package ca.georgebrown.comp3074.comp3074_project;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RouteListFragment extends ListFragment {

    private Cursor cursor;
    private SQLiteDatabase db;
    static interface RouteListListener{
        void itemClicked(long id);
    }
    private RouteListListener listener;

    public RouteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //RouteAdapter adapter = new RouteAdapter(inflater.getContext(), R.layout.route_list_layout, Route.routes);
        try{
        RouteDbHelper routeDbHelper = new RouteDbHelper(getContext());
        db = routeDbHelper.getReadableDatabase();
        cursor = db.query("ROUTE",
                new String[] {"_id", "DEPARTURE", "DESTINATION", "DURATION", "DISTANCE", "DATE"},
                null, null ,null, null, null);
        CursorAdapter adapter = new SimpleCursorAdapter(inflater.getContext(), R.layout.route_list_layout,
                cursor, new String[] {"DEPARTURE", "DESTINATION", "DURATION", "DISTANCE", "DATE"},
                new int[] {R.id.departure, R.id.destination, R.id.distance, R.id.duration, R.id.date}, 0);
        setListAdapter(adapter);
        }catch (SQLException e){
            Toast.makeText(getContext(), "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (RouteListListener) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(listener !=null){
            listener.itemClicked(id);
        }
    }
}
