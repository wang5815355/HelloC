package com.example.sqlite;

import java.util.ArrayList;

import com.example.base.BaseSqlite;
import com.example.model.Friend;

import android.content.ContentValues;
import android.content.Context;


public class FriendSqlite extends BaseSqlite {

	public FriendSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "Friends";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			Friend.COL_ID , 
			Friend.COL_UNAME ,
			Friend.COL_FACEIMAGE ,
			Friend.COL_UPHONE
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" +
			Friend.COL_ID + " INTEGER PRIMARY KEY, " +
			Friend.COL_UNAME + " TEXT, " +
			Friend.COL_FACEIMAGE + " TEXT, " +
			Friend.COL_UPHONE + " TEXT, " +
			");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
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

	public ArrayList<Friend> getAllFriends () {
		ArrayList<Friend> friendList = new ArrayList<Friend>();
		try {
			ArrayList<ArrayList<String>> rList = this.query(null, null);
			int rCount = rList.size();
			for (int i = 0; i < rCount; i++) {
				ArrayList<String> rRow = rList.get(i);
				Friend friend = new Friend();
				friend.setId(rRow.get(0));
				friend.setUname(rRow.get(1));
				friend.setFaceimage(rRow.get(2));
				friend.setUphone(rRow.get(3));
				friendList.add(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendList;
	}
}