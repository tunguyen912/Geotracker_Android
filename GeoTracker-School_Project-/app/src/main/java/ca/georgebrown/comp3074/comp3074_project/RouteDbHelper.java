package ca.georgebrown.comp3074.comp3074_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "routes";
    private static final int DB_VERSION = 8;
    public RouteDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ROUTE ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DEPARTURE TEXT,  "
                + "DESTINATION TEXT, "
                + "DATE TEXT, "
                + "NOTE TEXT, "
                + "DURATION TEXT, "
                + "RATE REAL DEFAULT 0);");
        insertRoute(db, "1470 Queen Street West", "910 King Street West",
                "10 October, 2019", "Easy", "27 min");
        insertRoute(db, "910 King Street West", "1470 Queen Street West",
                "11 October, 2019", "Easy",  "28 min");
        insertRoute(db, "2328 Sheppard Avenue West", "2500 Weston Road",
                "17 October, 2019", "Easy",  "48 min");
        insertRoute(db, "2328 Sheppard Avenue West", "2500 Weston Road",
                "21 October, 2019", "Medium",  "50 min");
        db.execSQL("CREATE TABLE PATH ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ROUTE_ID INTEGER, "
                + "LATITUDE REAL, "
                + "LONGITUDE REAL, "
                + "TIME INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ROUTE");
        db.execSQL("DROP TABLE IF EXISTS PATH");
        onCreate(db);
    }
    private static void insertRoute(
            SQLiteDatabase db, String departure, String destination,String date, String note, String duration){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DEPARTURE", departure);
        contentValues.put("DESTINATION", destination);
        contentValues.put("NOTE", note);
        contentValues.put("DATE", date);
        contentValues.put("DURATION", duration);
        db.insert("ROUTE", null, contentValues);
    }
}