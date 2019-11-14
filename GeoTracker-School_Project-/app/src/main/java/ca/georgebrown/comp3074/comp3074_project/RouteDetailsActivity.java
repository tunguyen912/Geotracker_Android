package ca.georgebrown.comp3074.comp3074_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RouteDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTE_ID = "id" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);
        RouteDetailFragment routeDetailFragment = (RouteDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_frag);
        int routeId = (int)getIntent().getExtras().get(EXTRA_ROUTE_ID);
        routeDetailFragment.setRouteId(routeId);
    }
}
