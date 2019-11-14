package ca.georgebrown.comp3074.comp3074_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class MapActivity extends AppCompatActivity {

    private boolean clicked = false;
    private boolean finished = false;
    private ImageView startTrack;
    private String newDeparture, newDestination, newDistance, newDuration, newVia, newDate, newDifficulty;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        startTrack = findViewById(R.id.btnTrack);

        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = !clicked;
                finished = false;
                if(clicked && finished == false)
                {
                    Toast.makeText(getApplicationContext(),"START" , Toast.LENGTH_SHORT).show();
                    startTrack.setImageResource(R.drawable.stop);
                    finished=false;
                }
                else if(clicked == false && finished == false)
                {
                    Toast.makeText(getApplicationContext(),"STOP" , Toast.LENGTH_SHORT).show();
                    startTrack.setImageResource(R.drawable.start);
                    finished=false;

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
        });
    }
    private void showDialog(String departure, String destination, String distance, String duration){
        final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCanceledOnTouchOutside(false);

        Button btnSave, btnCancel;
        EditText txtDeparture, txtDestination, txtDuration, txtDistance;

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
}
