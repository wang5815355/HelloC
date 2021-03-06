package com.hello008.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hello008.base.BaseSqlite;
import com.hello008.model.Customer;
import com.hello008.model.Friend;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


public class FriendSqlite extends BaseSqlite {

	public FriendSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		//获取当前登录用户等登录名称
		return "friends";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			"tid",
			Friend.COL_ID , 
			Friend.COL_UNAME ,
			Friend.COL_FACEIMAGE ,
			Friend.COL_UPHONE
		};
		return columns;
	}

	@Override
	protected String createSql() {
		Log.w("test1===","12348");
		return "CREATE TABLE " + tableName() + " (" +
			"tid" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			Friend.COL_ID + " TEXT, " +
			Friend.COL_UNAME + " TEXT, " +
			Friend.COL_FACEIMAGE + " TEXT, " +
			Friend.COL_UPHONE + " TEXT" +
			");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public void delete(){
		String whereSql = Friend.COL_ID + "<>?";
		String[] whereParams = new String[]{"-1"};
		this.delete(whereSql,whereParams);
	}
	
	public boolean updateFriend(Friend friend) {
		// prepare Friend   data
		ContentValues values = new ContentValues();
		values.put(Friend.COL_ID, friend.getId());
		values.put(Friend.COL_UNAME, friend.getUname());
		values.put(Friend.COL_FACEIMAGE, friend.getFaceimage());
		values.put(Friend.COL_UPHONE, friend.getUphone());
		// prepare sql
		String whereSql = Friend.COL_ID + "=?";
		String[] whereParams = new String[]{friend.getId()};
		// create or update
		try {
			if (this.exists(whereSql, whereParams)) {
				this.update(values, whereSql, whereParams);
			} else {
				this.create(values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public List<Map<String, Object>> getAllFriends () {
		Log.w("test1===","sql3");
		List<Map<String, Object>> friends = null;
		try {
				ArrayList<ArrayList<String>> rList = this.query(null, null);
				Log.w("test1===","sql5");
				friends = new ArrayList<Map<String,Object>>();
				int rCount = rList.size();
				for (int i = 0; i < rCount; i++) {
					ArrayList<String> rRow = rList.get(i);
					Map<String, Object> friendO = new HashMap<String, Object>();
					friendO.put("id",rRow.get(0));
					friendO.put("uname",rRow.get(1));
					friendO.put("faceimgurl",rRow.get(2));
					friendO.put("uphone",rRow.get(3));
					friends.add(friendO);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.w("test1===","sql4");
		return friends;
	}
}