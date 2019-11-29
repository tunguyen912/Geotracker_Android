package ca.georgebrown.comp3074.comp3074_project;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Enter Departure or Destination");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchRoute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRoute(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void searchRoute(String searchInput){
        LayoutInflater inflater = getLayoutInflater();
        try{
            RouteDbHelper routeDbHelper = new RouteDbHelper(getContext());
            db = routeDbHelper.getReadableDatabase();
            cursor = db.query("ROUTE",
                    new String[] {"_id", "DEPARTURE", "DESTINATION", "DURATION", "DISTANCE", "DATE"},
                    "DEPARTURE LIKE ? OR DESTINATION LIKE ?",
                    new String[] {"%" + searchInput + "%", "%" + searchInput + "%" },
                    null, null, null);

            if(cursor.getCount() == 0){
                Toast.makeText(getContext(), "Route not found", Toast.LENGTH_SHORT).show();
            }

            CursorAdapter adapter = new SimpleCursorAdapter(inflater.getContext(), R.layout.route_list_layout,
                    cursor, new String[] {"DEPARTURE", "DESTINATION", "DURATION", "DISTANCE", "DATE"},
                    new int[] {R.id.departure, R.id.destination, R.id.distance, R.id.duration, R.id.date}, 0);
            setListAdapter(adapter);
        }catch (SQLException e){
            Toast.makeText(getContext(), "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
