package com.example.a45773.Db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a45773.bean.Gjcbean;

import com.example.a45773.bean.Replybean;
import com.example.a45773.bean.Setbean;


import java.util.ArrayList;
import java.util.List;


public class DBManager {
    private SqlHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new SqlHelper(context, "danmu.db", null, 1);
        db = helper.getWritableDatabase();
    }

    public void addgjc(Gjcbean gjc) {
        ContentValues value = new ContentValues();
        value.put("text", gjc.getText());
        value.put("name", "");
        db.insert("hei", null, value);
    }

    public void addgjcn(Gjcbean gjc) {
        ContentValues value = new ContentValues();
        value.put("text", "");
        value.put("name", gjc.getName());
        db.insert("hei", null, value);
    }

    public void delgjc(String text) {
        String whereClause = "text=?";
        String[] whereArgs = {text};
        db.delete("hei", whereClause, whereArgs);
    }

    public void delgjcn(String name) {
        String whereClause = "name=?";
        String[] whereArgs = {name};
        db.delete("hei", whereClause, whereArgs);
    }

    public void delallgjc() {
        String whereClause = "name=?";
        String[] whereArgs = {""};
        db.delete("hei", whereClause, whereArgs);
    }

    public void delallgjcn() {
        String whereClause = "text=?";
        String[] whereArgs = {""};
        db.delete("hei", whereClause, whereArgs);
    }

    public ArrayList<Gjcbean> queryallgjc() {
        ArrayList<Gjcbean> ad = new ArrayList<Gjcbean>();
        Cursor cursor = db.rawQuery("select text from hei where name=?", new String[]{""});
        if (cursor.moveToFirst()) {
            do {
                Gjcbean gjc = new Gjcbean();
                String text = cursor.getString(0);
                String name = cursor.getString(0);
                gjc.setText(text);
                gjc.setName(name);
                ad.add(gjc);
            } while (cursor.moveToNext());
        }
        return ad;
    }

    public ArrayList<Gjcbean> queryallgjcn() {
        ArrayList<Gjcbean> ad = new ArrayList<Gjcbean>();
        Cursor cursor = db.rawQuery("select name from  hei where text=?", new String[]{""});
        if (cursor.moveToFirst()) {
            do {
                Gjcbean gjc = new Gjcbean();
                String text = cursor.getString(0);
                String name = cursor.getString(0);
                gjc.setText(text);
                gjc.setName(name);
                ad.add(gjc);
            } while (cursor.moveToNext());
        }
        return ad;
    }

    public Setbean queryallset() {
        Setbean set = null;
        Cursor cursor = db.query("sett", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            set = new Setbean();
            int touming = cursor.getInt(1);
            int tingzhu = cursor.getInt(2);
            int gl = cursor.getInt(3);
            int langdu = cursor.getInt(4);
            int liwu = cursor.getInt(5);
            int room = cursor.getInt(6);
            int size = cursor.getInt(7);
            int glpb = cursor.getInt(8);
            int gift = cursor.getInt(9);
            String type = cursor.getString(10);
            set.setTouming(touming);
            set.setTingzhu(tingzhu);
            set.setgl(gl);
            set.setLangdu(langdu);
            set.setLiwu(liwu);
            set.setRoom(room);
            set.setSize(size);
            set.setGlpb(glpb);
            set.setGift(gift);
            set.setType(type);
        }
        return set;
    }

    public void updataset(String zhi, int x) {
        String sql = "update sett set " + zhi + "=" + x;
        db.execSQL(sql);
    }
    public void updataset(String zhi, String x) {
        String sql = "update sett set " + zhi + "='" + x+"'";
        db.execSQL(sql);
    }

    public void closeDB() {
        db.close();
    }

    public List<Replybean> queryAllReplys(){
        List<Replybean> replys = new ArrayList<Replybean>();
        Replybean reply;
        Cursor cursor = db.query("reply", null, null,null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String ask = cursor.getString(cursor.getColumnIndex("ask"));
                String r = cursor.getString(cursor.getColumnIndex("reply"));
                reply = new Replybean(ask,r);
                replys.add(reply);
            }while(cursor.moveToNext());
        }
        return replys;
    }
    public void delreply(String ask){
        db.delete("reply", "ask = ?", new String[]{ask});
    }

    public void addreply(Replybean reply){
        ContentValues values = new ContentValues();
        values.put("ask", reply.getAsk());
        values.put("reply", reply.getReply());
        db.insert("reply", null, values);
    }

    public void delAllReply(){
        db.delete("reply", null, null);
    }
}
