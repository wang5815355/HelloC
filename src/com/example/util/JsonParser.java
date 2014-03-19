package com.example.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.Friend;

public class JsonParser {
	//解析多条JSON数据
	public static List parseJsonList(String json){
		String s = null;
		List<Friend> friends = new ArrayList<Friend>(); //好友姓名集合
		Friend friend = null;
		
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				
				for(int i = 0; i<jsa.length();i++){
					//创建好友对象
					
					JSONObject jso = (JSONObject)jsa.opt(i);
					s = jso.getString("uname1");
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
}
