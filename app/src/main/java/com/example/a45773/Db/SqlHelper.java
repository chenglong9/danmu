package com.example.a45773.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 45773 on 2016-11-07.
 */

public class SqlHelper extends SQLiteOpenHelper {


    public static final String CREATE_H="create table hei ("
            +"id integer primary key autoincrement, "
            +"text text, "
            +"name text)";
    public static final String CREATE_SS="create table sett ("
            +"id integer primary key autoincrement, "
            +"touming integer , "
            +"tingzhu integer ,"
            +"guolv integer, "
            +"langdu integer, "
            +"liwu integer, "
            +"room integer,"
            +"size integer ,"
            +"glpb integer ,"
            +"gift integer ,"
            +"type text )";
    public static final String CREATE_REPLY="create table reply ("
            +"id integer primary key autoincrement, "
            +"ask text, "
            +"reply text)";



    private  Context mcontext;

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SS);
        db.execSQL(CREATE_H);
        db.execSQL(CREATE_REPLY);

        ContentValues value = new ContentValues();
        value.put("touming",0);
        value.put("tingzhu",1);
        value.put("guolv",1);
        value.put("langdu",1);
        value.put("liwu",1);
        value.put("size",10);
        value.put("room",-1111);
        value.put("glpb",0);
        value.put("gift",0);
        value.put("type","douyu");
        db.insert("sett",null,value);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
