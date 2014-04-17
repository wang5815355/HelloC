package com.example.model;

public class Friend {
	public final static String COL_ID = "id";
	public final static String COL_UNAME = "uname";
	public final static String COL_FACEIMAGE = "faceimage";
	public final static String COL_UPHONE = "uphone";
	
	private String id;
	private String uname;
	private String faceimage;
	private String uphone;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getFaceimage() {
		return faceimage;
	}
	public void setFaceimage(String faceimage) {
		this.faceimage = faceimage;
	}
	public String getUphone() {
		return uphone;
	}
	public void setUphone(String uphone) {
		this.uphone = uphone;
	}
	
	
}
