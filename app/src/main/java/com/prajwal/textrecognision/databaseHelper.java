package com.prajwal.textrecognision;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;

public class databaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Textrecog.db";
    public static final String TABLE_NAME = "history";
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_UNAME = "name";
    public static final String HISTORY_DATA = "data";
    public databaseHelper(Context context){
        super(context,DB_NAME,null,1);
        Log.d("DBContext "+context +" db construct:", context + DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+" ("+HISTORY_ID+" integer primary key, "+HISTORY_UNAME+" text, "+HISTORY_DATA+" text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);

    }
    public boolean insertData(String uname, String data){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(HISTORY_UNAME, uname);
            contentValues.put(HISTORY_DATA, data);
            db.insert(TABLE_NAME, null, contentValues);}
        catch (Exception e){
            Log.e("DataBase Insert error:", e.getMessage());
        }
        return true;
    }
    public ArrayList<String> getData(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("Test Debug", "SELECT * from "+TABLE_NAME+" where "+HISTORY_UNAME +"=\""+uname+"\" ");
        Cursor cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" where "+HISTORY_UNAME +"=\""+uname+"\" ", null);
        ArrayList<String> array_list = new ArrayList<String>();
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            array_list.add(cursor.getString(cursor.getColumnIndex(HISTORY_DATA)));
            cursor.moveToNext();
        }
        return array_list;
    }
    public int numRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public int deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id=?", new String[]{Integer.toString(id)});
    }
}
