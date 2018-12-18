/*
package com.ULock.App_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

*/
/**
 * Created by nazer on 7/20/2016.
 *//*

public class App_Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appp_data.db";
    private static final String TABLE_NAME = "app_table";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "ID";
    private static final String APP_NAME = "APP_NAME";
    private static final String STARTDATE = "STARTDATE";
    private static final String ENDDATE = "ENDDATE";
    private static final String STARTTIME = "STARTTIME";
    private static final String ENDTIME = "ENDTIME";
    private static final String DAYS = "DAYS";
    private SQLiteDatabase db;
    private Context con;

    public App_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.con = context;
        Log.d("constructor", "Const");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Oncreate", "oncreate");
        db.execSQL("create table "
                + TABLE_NAME
                + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, APP_NAME TEXT, STARTDATE TEXT,ENDDATE TEXT, STARTTIME TEXT, ENDTIME TEXT, DAYS TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertdata(String Name,String Image, String Rate, String Qty)
    {
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(APP_NAME,Name);
        contentValues.put(STARTDATE,Image);
        contentValues.put(ENDDATE,Image);
        contentValues.put(STARTTIME,Image);
        contentValues.put(ENDTIME,Rate);
        contentValues.put(DAYS,Qty);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            Log.e("Pass data","ok");
        return true;
    }
    public Cursor getallData(){
        db=this.getWritableDatabase();
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME, null);

        return rs;

    }
//    public void delete_byID(int id){
//
//        db.delete(TABLE_NAME, ID+"="+id, null);
//    }

    public void delete_byID(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=" + id, null);


    }

}
*/
