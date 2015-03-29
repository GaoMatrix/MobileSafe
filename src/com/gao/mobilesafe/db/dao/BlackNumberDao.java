
package com.gao.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.gao.mobilesafe.db.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 黑名单数据库的增删改查业务类
 *
 * @author gaochengquan
 */
public class BlackNumberDao {
    private BlackNumberDBOpenHelper mHelper;

    public BlackNumberDao(Context context) {
        mHelper = new BlackNumberDBOpenHelper(context);
    }

    public boolean find(String number) {
        boolean result = false;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber where number=?", new String[] {
            number
        });
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询黑名单号码的拦截模式
     * @param number
     * @return
     */
    public String findMode(String number) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber where number=?", new String[] {
            number
        });
        if (cursor.moveToNext()) {
            result = cursor.getString(2);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询部分的黑名单号码
     * @param offset从哪个位置开始获取数据
     * @param maxnumber 一次最多获取多少条记录
     * @return
     */
    public List<BlackNumberInfo> findPart(int offset,int maxnumber){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //imit ? offset ?必须写在查询语句的最后
        Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc  limit ? offset ? ", 
                    new String[]{String.valueOf(maxnumber),String.valueOf(offset)});
        while(cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setMode(mode);
            info.setNumber(number);
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询全部黑名单号码
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber", null);
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(1);
            String mode = cursor.getString(2);
            info.setNumber(number);
            info.setMode(mode);
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 添加黑名单号码
     *
     * @param number
     * @param mode 拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
     */
    public void add(String number, String mode) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    /**
     * 修改黑名单号码的拦截模式
     *
     * @param number
     * @param mode 拦截模式
     */
    public void update(String number, String newMode) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newMode);
        db.update("blacknumber", values, "number=?", new String[] {
            number
        });
        db.close();
    }

    /**
     * 删除黑名单号码
     *
     * @param number
     * @param mode 拦截模式
     */
    public void delete(String number) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("blacknumber", "number=?", new String[] {
            number
        });
        db.close();
    }

}
