package com.hello008.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hello008.base.BaseSqlite;
import com.hello008.model.Circle;
import com.hello008.model.Customer;
import com.hello008.model.Friend;

import android.content.ContentValues;
import android.content.Context;

public class CircleSqlite extends BaseSqlite {

	public CircleSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// 获取当前登录用户等登录名称
		Customer customer = Customer.getInstance();
		String uname = customer.getName();
		return "circles"+uname;
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = { "tid", Circle.COL_CIRCLEID,Circle.COL_CIRCLENAME,Circle.COL_COUNT
				,Circle.COL_FACEIMG,Circle.COL_ID,Circle.COL_ISCREATER,Circle.COL_PHONENUMBER
				,Circle.COL_STATUS,Circle.COL_TIME,Circle.COL_UEMAIL,Circle.COL_UNAME};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" + "tid"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Circle.COL_CIRCLEID
				+ " TEXT, " + Circle.COL_CIRCLENAME + " TEXT, "
				+ Circle.COL_COUNT + " TEXT, " + Circle.COL_FACEIMG
				+ " TEXT, " + Circle.COL_ID
				+ " TEXT, " + Circle.COL_ISCREATER
				+ " TEXT, " + Circle.COL_PHONENUMBER
				+ " TEXT, " + Circle.COL_STATUS
				+ " TEXT, " + Circle.COL_TIME
				+ " TEXT, " + Circle.COL_UEMAIL
				+ " TEXT, " + Circle.COL_UNAME
				+ " TEXT" + ");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public void delete() {
		String whereSql = Friend.COL_ID + "<>?";
		String[] whereParams = new String[] { "-1" };
		this.delete(whereSql, whereParams);
	}

	public boolean updateCricle(Circle circle) {
		// prepare Friend data
		ContentValues values = new ContentValues();
		values.put(Circle.COL_ID, circle.getId());
		values.put(Circle.COL_CIRCLEID, circle.getCircleid());
		values.put(Circle.COL_UNAME, circle.getUname());
		values.put(Circle.COL_CIRCLENAME, circle.getCirclename());
		values.put(Circle.COL_COUNT, circle.getCount());
		values.put(Circle.COL_FACEIMG, circle.getFaceimg());
		values.put(Circle.COL_ISCREATER, circle.getIsCreater());
		values.put(Circle.COL_PHONENUMBER, circle.getPhonenumber());
		values.put(Circle.COL_STATUS, circle.getStatus());
		values.put(Circle.COL_TIME, circle.getTime());
		values.put(Circle.COL_UEMAIL, circle.getUemail());
		
		// prepare sql
		String whereSql = Friend.COL_ID + "=?";
		String[] whereParams = new String[] { circle.getId() };
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

	public List<Map<String, Object>> getAllCircles() {
		List<Map<String, Object>> circles = null;
		try {
			ArrayList<ArrayList<String>> rList = this.query(null, null);
			circles = new ArrayList<Map<String, Object>>();
			int rCount = rList.size();
			for (int i = 0; i < rCount; i++) {
				ArrayList<String> rRow = rList.get(i);
				Map<String, Object> circleO = new HashMap<String, Object>();
				circleO.put("tid", rRow.get(0));
				circleO.put("circleid", rRow.get(1));
				circleO.put("faceimg", rRow.get(2));
				circleO.put("id", rRow.get(3));
				circleO.put("iscreater", rRow.get(3));
				circleO.put("phonenumber", rRow.get(3));
				circleO.put("status", rRow.get(3));
				circleO.put("time", rRow.get(3));
				circleO.put("uemail", rRow.get(3));
				circleO.put("uname", rRow.get(3));
				circles.add(circleO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return circles;
	}
}