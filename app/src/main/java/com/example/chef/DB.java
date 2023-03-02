package com.example.chef;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

public class DB {
    private Context context = null;
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    private String dbName = "";

    public DB(Context context,String dbName){
        this.context = context;
        this.dbName = dbName;
    }
    //взаимодействие с базой данных
    private void request(String request){
        db = context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        db.execSQL(request);
        db.close();
        db = null;
    }
    public void INSERT(String tableName,String Values,boolean id){
        String request = "INSERT OR IGNORE INTO "+tableName+" VALUES ("+Values+"";
        if(id)
            request += ",'"+(UUID.randomUUID()).toString()+"'";
        request += ");";
        request(request);
    }
    public void UPDATE(String tableName,String Values){
        String request = "UPDATE "+tableName+" SET "+Values+";";
        request(request);
    }
    public void DELETE(String tableName,String Values){
        String request = "DELETE FROM "+tableName+" WHERE "+Values+";";
        request(request);
    }
    public void CREATE_TABLE(String tableName,String Values){
        String request = "CREATE TABLE IF NOT EXISTS "+tableName+" ("+Values+")";
        request(request);
    }
    public Cursor SELECT(String tableName,String Values){
        String request ="SELECT * FROM "+tableName+Values+";";
        db = context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        cursor = db.rawQuery(request, null);
        return cursor;
    }
    public void STOP(){
        db.close();
        db = null;
    }
}
