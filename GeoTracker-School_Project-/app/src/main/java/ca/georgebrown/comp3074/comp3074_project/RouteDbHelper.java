package ca.georgebrown.comp3074.comp3074_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "routes";
    private static final int DB_VERSION = 3;
    public RouteDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ROUTE ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DEPARTURE TEXT,  "
                + "DESTINATION TEXT, "
                + "VIA TEXT, "
                + "DATE TEXT, "
                + "DIFFICULTY TEXT, "
                + "DURATION TEXT, "
                + "RATE REAL DEFAULT 0, "
                + "DISTANCE TEXT);");
        insertRoute(db, "1470 Queen Street West", "910 King Street West",
                "Queen Street W and Sudbury Street",
                "10 October, 2019", "Easy", "2.2 km", "27 min");
        insertRoute(db, "910 King Street West", "1470 Queen Street West",
                "Sudbury Street and Queen Street W",
                "11 October, 2019", "Easy", "2.2 km", "28 min");
        insertRoute(db, "2328 Sheppard Avenue West", "2500 Weston Road",
                "Strathburn Blvd and Weston Rd",
                "17 October, 2019", "Easy", "3.9 km", "48 min");
        insertRoute(db, "2328 Sheppard Avenue West", "2500 Weston Road",
                "Humber River Recreational Trail",
                "21 October, 2019", "Medium", "4.2 km", "50 min");
        db.execSQL("CREATE TABLE PATH ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ROUTE_ID INTEGER, "
                + "LATITUDE REAL, "
                + "LONGITUDE REAL, "
                + "TIME INTEGER);");
        //Route_id = _id in route table + 1
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ROUTE");
        onCreate(db);
    }
    private static void insertRoute(
            SQLiteDatabase db, String departure, String destination, String via,String date, String difficulty, String distance, String duration){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DEPARTURE", departure);
        contentValues.put("DESTINATION", destination);
        contentValues.put("VIA", via);
        contentValues.put("DIFFICULTY", difficulty);
        contentValues.put("DATE", date);
        contentValues.put("DURATION", duration);
        contentValues.put("DISTANCE", distance);
        db.insert("ROUTE", null, contentValues);
    }
}
