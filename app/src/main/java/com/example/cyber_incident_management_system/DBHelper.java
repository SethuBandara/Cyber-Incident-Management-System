package com.example.cyber_incident_management_system;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "CyberIncidentDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE incidents (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "incident_type TEXT," +
                "severity TEXT," +
                "status TEXT," +
                "reported_date TEXT," +
                "affected_asset TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS incidents");
        onCreate(db);
    }

    // CREATE
    public boolean insertIncident(String title, String description,
                                  String type, String severity,
                                  String status, String date,
                                  String asset) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("description", description);
        values.put("incident_type", type);
        values.put("severity", severity);
        values.put("status", status);
        values.put("reported_date", date);
        values.put("affected_asset", asset);

        long result = db.insert("incidents", null, values);

        return result != -1;
    }

    // READ
    public Cursor getAllIncidents() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM incidents ORDER BY id DESC",
                null);
    }

    // READ ONE INCIDENT
    public Cursor getIncidentById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM incidents WHERE id = ?",
                new String[]{String.valueOf(id)});
    }

    // UPDATE
    public boolean updateIncident(int id, String title,
                                  String description, String type,
                                  String severity, String status,
                                  String date, String asset) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("description", description);
        values.put("incident_type", type);
        values.put("severity", severity);
        values.put("status", status);
        values.put("reported_date", date);
        values.put("affected_asset", asset);

        int result = db.update("incidents", values,
                "id = ?", new String[]{String.valueOf(id)});

        return result > 0;
    }

    // DELETE
    public boolean deleteIncident(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete("incidents",
                "id = ?", new String[]{String.valueOf(id)});

        return result > 0;
    }
}
