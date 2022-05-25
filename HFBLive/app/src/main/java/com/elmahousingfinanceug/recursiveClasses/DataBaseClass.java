package com.elmahousingfinanceug.recursiveClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseClass extends SQLiteOpenHelper {

    private static final String
            DATABASE_NAME = "HFBDB.db",
            LOG_TABLE_NAME = "ActivityLogTable";

    public static final String
            LOG_ID_NUMBER = "Id",
            LOG_DATE = "date",
            LOG_LABEL = "Label";

    private static final int DATABASE_VERSION = 1;

    public DataBaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists "
                + LOG_TABLE_NAME +
                "("+ LOG_ID_NUMBER +       " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LOG_DATE +                 " TEXT," +
                LOG_LABEL +                " TEXT)"
        );
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}

    void InsertLog(
            String Date,
            String Label
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOG_DATE, Date);
        contentValues.put(LOG_LABEL, Label);
        db.insert(LOG_TABLE_NAME, null, contentValues);
    }

    public Cursor retrieveAllLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + LOG_TABLE_NAME, null);
        cur.moveToFirst();
        return cur;
    }

    public void deleteOneLog(Integer id) {
        String[] IDval = {String.valueOf(id)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOG_TABLE_NAME, LOG_ID_NUMBER + "= ?", IDval);
    }

    public void deleteAllLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + LOG_TABLE_NAME);
        db.execSQL("delete from sqlite_sequence where name = '" + LOG_TABLE_NAME + "'");
    }

}