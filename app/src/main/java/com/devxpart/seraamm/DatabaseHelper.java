package com.devxpart.seraamm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String Database_name = "SeraAmm.db";
    private static final String FirstTable = "Request_1";
    private static final String SecondTable = "Request_2";
    private static final String ThirdTable = "Request_3";
    private static final String FourthTable = "Request_4";
    private static final String FifthTable = "Request_5";
    private static final String COLUMN_NAME = "JSONDATA";
    Context context;


    public DatabaseHelper(Context context) {
        super(context, Database_name, null, 1);

        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {




        String Time ="create table RequestTime (ID INTEGER PRIMARY KEY AUTOINCREMENT,JSONDATA TEXT );";
        String createTable1 = "create table "+FirstTable+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,JSONDATA TEXT );";
        String createTable2 = "create table "+SecondTable+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,JSONDATA TEXT );";
        String createTable3 = "create table "+ThirdTable+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,JSONDATA TEXT);";
        String createTable4 = "create table "+FourthTable+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,JSONDATA TEXT);";
        String createTable5 = "create table "+FifthTable+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,JSONDATA TEXT);";
        db.execSQL(Time);
        db.execSQL(createTable1);
        db.execSQL(createTable2);
        db.execSQL(createTable3);
        db.execSQL(createTable4);
        db.execSQL(createTable5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableupdate = "DROP TABLE IF EXISTS FirstTable";
        db.execSQL(tableupdate);
        onCreate(db);
    }

    boolean insartData(String tablename, String jsonobject){
        boolean b;
        SQLiteDatabase sd=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,jsonobject);
        long bl = sd.insert(tablename,null,contentValues);

        if(bl==-1){
            Toast.makeText(context,"Failed To Save Data!",Toast.LENGTH_SHORT).show();
            b=false;
        }else {
            Toast.makeText(context,"Data Saved Successfully!",Toast.LENGTH_SHORT).show();
            b=true;
        }
        return b;
    }

    boolean insartData(String tablename, String Name,String jsonobject){
        boolean b;
        SQLiteDatabase sd=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",Name);
        contentValues.put(COLUMN_NAME,jsonobject);
        long bl = sd.insert(tablename,null,contentValues);

        if(bl==-1){
            Toast.makeText(context,"Failed To Save Data!",Toast.LENGTH_SHORT).show();
            b=false;
        }else {
            Toast.makeText(context,"Data Saved Successfully!",Toast.LENGTH_SHORT).show();
            b=true;
        }
        return b;
    }


    public Cursor getData(String tablename){
        SQLiteDatabase sd=this.getWritableDatabase();

        return sd.rawQuery("select JSONDATA from "+tablename,null);
    }

    public void removeALlData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM RequestTime");
        sqLiteDatabase.execSQL("VACUUM");
        sqLiteDatabase.execSQL("DELETE FROM "+ FirstTable);
        sqLiteDatabase.execSQL("VACUUM");
        sqLiteDatabase.execSQL("DELETE FROM "+ SecondTable);
        sqLiteDatabase.execSQL("VACUUM");
        sqLiteDatabase.execSQL("DELETE FROM "+ ThirdTable);
        sqLiteDatabase.execSQL("VACUUM");
        sqLiteDatabase.execSQL("DELETE FROM "+ FourthTable);
        sqLiteDatabase.execSQL("VACUUM");
        sqLiteDatabase.execSQL("DELETE FROM "+ FifthTable);
        sqLiteDatabase.execSQL("VACUUM");
    }


    public Cursor getSingleRaw(String tablename,String name) {

        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT JSONDATA FROM "+tablename+" WHERE NAME=?", new String[] {name + ""});

       return cursor;
    }
}
