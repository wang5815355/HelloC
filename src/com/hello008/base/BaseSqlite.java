package com.hello008.base;

import java.util.ArrayList;

import com.hello008.model.Circle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseSqlite {

	private static final String DB_NAME = "hello.db";
	private static final int DB_VERSION = 1;
	
	private DbHelper dbh = null;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	
	public BaseSqlite(Context context) {
		dbh = new DbHelper(context, DB_NAME, null, DB_VERSION);
	}

	public void create (ContentValues values) {
		try {
			db = dbh.getWritableDatabase();
			db.insert(tableName(), null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public void update(ContentValues values, String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.update(tableName(), values, where, params);
		} catch (Exception e) {
			Log.w("test1===","123412341234==="+e.getMessage());
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public void delete (String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			db.delete(tableName(), where, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public ArrayList<ArrayList<String>> query (String where, String[] params) {
		Log.w("test1===","sql6");
		ArrayList<ArrayList<String>> rList = new ArrayList<ArrayList<String>>();
		try {
			db = dbh.getReadableDatabase();
			Log.w("test1===","sql7");
			cursor = db.query(tableName(),tableColumns(),where,params,null,null,null);
			ArrayList<String> rRow = null;
			while (cursor.moveToNext()) {
				rRow = new ArrayList<String>();
				String id = cursor.getString(cursor.getColumnIndex("id"));
		        String uname = cursor.getString(cursor.getColumnIndex("uname"));
		        String faceimgurl = cursor.getString(cursor.getColumnIndex("faceimage"));
		        String uphone = cursor.getString(cursor.getColumnIndex("uphone"));
		        rRow.add(0, id);
		        rRow.add(1, uname);
		        rRow.add(2, faceimgurl);
		        rRow.add(3, uphone);
				rList.add(rRow);
			}
			
		} catch (Exception e) {
			Log.w("sqllite",e.getMessage());
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return rList;
	}
	
	public ArrayList<ArrayList<String>> query_circle (String where, String[] params) {
		ArrayList<ArrayList<String>> rList = new ArrayList<ArrayList<String>>();
		Log.w("test1===","query_clecle");
		try {
			db = dbh.getReadableDatabase();
			//判断是否已存在表
			if(!tabIsExist(tableName())){
				db.execSQL(createSql());
			}
			
			Log.w("test1===","test9999");
			cursor = db.query(tableName(),tableColumns(),where,params,null,null,null);
			ArrayList<String> rRow = null;
			
			while (cursor.moveToNext()) {
				rRow = new ArrayList<String>();
				Log.w("test1===","test9998");
				String circleid = cursor.getString(cursor.getColumnIndex("circleid"));
		        String circlename = cursor.getString(cursor.getColumnIndex("circlename"));
		        String count = cursor.getString(cursor.getColumnIndex("count"));
		        String faceimg = cursor.getString(cursor.getColumnIndex("faceimg"));
		        String id = cursor.getString(cursor.getColumnIndex("id"));
		        String iscreater = cursor.getString(cursor.getColumnIndex("isCreater"));
		        String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
		        String status = cursor.getString(cursor.getColumnIndex("status"));
		        String time = cursor.getString(cursor.getColumnIndex("time"));
		        String uemail = cursor.getString(cursor.getColumnIndex("uemail"));
		        String uname = cursor.getString(cursor.getColumnIndex("uname"));
		        
		        rRow.add(0, circleid);
		        rRow.add(1, circlename);
		        rRow.add(2, count);
		        rRow.add(3, faceimg);
		        rRow.add(4, id);
		        rRow.add(5, iscreater);
		        rRow.add(6, phonenumber);
		        rRow.add(7, status);
		        rRow.add(8, time);
		        rRow.add(9, uemail);
		        rRow.add(10, uname);
				rList.add(rRow);
			}
			
		} catch (Exception e) {
			Log.w("test1===",e.getMessage());
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return rList;
	}
	
	/**
	 * 判断数据库中是否存在表
	 * @param tabName
	 * @return
	 */
	public boolean tabIsExist(String tabName){
         boolean result = false;
         if(tabName == null){
                 return false;
         }
         Cursor cursor = null;
         try {
                
                 String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
                 db = dbh.getReadableDatabase();
                 cursor = db.rawQuery(sql, null);
                 if(cursor.moveToNext()){
                         int count = cursor.getInt(0);
                         if(count>0){
                                 result = true;
                         }
                 }
                 
         } catch (Exception e) {
                 // TODO: handle exception
         }                
         return result;
 }
	
	public int count (String where, String[] params) {
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), where, params, null, null, null);
			return cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return 0;
	}
	
	public boolean exists (String where, String[] params) {
		boolean result = false;
		try {
			int count = this.count(where, params);
			if (count > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			cursor.close();
		}
		return result;
	}
	
	abstract protected String tableName ();
	abstract protected String[] tableColumns ();
	abstract protected String createSql ();
	abstract protected String upgradeSql ();
	
	protected class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				 	db.execSQL(createSql());
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(upgradeSql());
			onCreate(db);
		}
	}
}