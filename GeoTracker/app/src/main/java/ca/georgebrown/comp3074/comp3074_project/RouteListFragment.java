package ca.georgebrown.comp3074.comp3074_project;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RouteListFragment extends ListFragment {
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
        RouteAdapter adapter = new RouteAdapter(inflater.getContext(), R.layout.route_list_layout, Route.routes);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (RouteListListener) context;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(listener !=null){
            listener.itemClicked(id);
        }
    }
}
