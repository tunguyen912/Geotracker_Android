package ca.georgebrown.comp3074.comp3074_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class RouteListActivity extends AppCompatActivity implements RouteListFragment.RouteListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
    }

    @Override
    public void itemClicked(long id) {
        View fragment_container = findViewById(R.id.fragment_container);
        if(fragment_container!=null){
            RouteDetailFragment details =new RouteDetailFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setRouteId(id);
            ft.replace(R.id.fragment_container, details);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }else{
            Intent intent = new Intent(this, RouteDetailsActivity.class );
            intent.putExtra(RouteDetailsActivity.EXTRA_ROUTE_ID, (int) id);
            startActivity(intent);
        }
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
}
