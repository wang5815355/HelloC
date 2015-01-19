package com.hello008.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hello008.base.BaseUi;

public class JsonParser extends BaseUi{
	
	/**
	 * 好友数据json解析
	 * @author wangkai
	 */
	public static List<Map<String, Object>> parseJsonList(String json){
		String id = null;//id
		String uname = null;//好友名称
		String uphone = null;//好友手机号码
		String faceimgurl = null;//好友头像图片URL
//		Bitmap faceimg = null;
		List<Map<String, Object>> friends = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				friends = new ArrayList<Map<String,Object>>();
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					id = jso.getString("uid");
					uname = jso.getString("uname");
					uphone = jso.getString("uphonenumber1");
					faceimgurl = jso.getString("faceimage");
					
					//异步加载好友头像图片
//					faceimg =  AppCache.getImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
					
					Map<String, Object> friend = new HashMap<String, Object>();
//					friend.put("faceimg",faceimg);
					friend.put("id",id);
					friend.put("faceimgurl",faceimgurl);
					friend.put("uname",uname);
					friend.put("uphone",uphone);
					friends.add(friend);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * 好友圈数据json解析
	 * @author wangkai
	 */
	public static List<Map<String, Object>> parseJsonListCircle(String json){
		 String circleid;
		 String circlename;
		 String count;
		 String faceimg;
		 String id;
		 String isCreater;
		 String phonenumber;
		 String status;
		 String time;
		 String uemail;
		 String uname;
		 List<Map<String, Object>> circles = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				circles = new ArrayList<Map<String,Object>>();
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					
					circleid = jso.getString("circleid");
					circlename = jso.getString("circlename");
					count = jso.getString("count");
					faceimg = jso.getString("faceimg");
					id = jso.getString("id");
					isCreater = jso.getString("isCreater");
					phonenumber = jso.getString("phonenumber");
					status = jso.getString("status");
					time = jso.getString("time");
					uemail = jso.getString("uemail");
					uname = jso.getString("uname");
					
					//异步加载好友头像图片
//					faceimg =  AppCache.getImage("http://www.hello008.com/Public/Uploads/"+faceimgurl);
					
					Map<String, Object> circle = new HashMap<String, Object>();
//					friend.put("faceimg",faceimg);
					circle.put("circleid",circleid);
					circle.put("circlename",circlename);
					circle.put("count",count);
					circle.put("faceimg",faceimg);
					circle.put("id",id);
					circle.put("isCreater",isCreater);
					circle.put("phonenumber",phonenumber);
					circle.put("status",status);
					circle.put("time",time);
					circle.put("uemail",uemail);
					circle.put("uname",uname);
					circles.add(circle);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return circles;
	}
	
}
