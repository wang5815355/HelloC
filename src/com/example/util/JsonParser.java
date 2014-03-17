package com.example.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	//解析多条JSON数据
	public static String parseJsonList(String json){
		String s = null;
		try {
				JSONArray jsa = new JSONObject(json).getJSONArray("info");
				for(int i = 0; i<jsa.length();i++){
					JSONObject jso = (JSONObject)jsa.opt(i);
					s = jso.getString("uname1");
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}
}
