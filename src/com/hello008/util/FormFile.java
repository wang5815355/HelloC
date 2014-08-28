package com.hello008.util;

// 表单文件
public class FormFile {
	// 上传文件数据
	private byte[] _data;
	// 上伟文件名称
	private String _fileName;
	// 表单栏位名称
	private String _formName;
	// 上传文件内容类型
	private String _contentType = "application/octet-stream";

	public FormFile(String formName, String fileName, byte[] data,
			String contentType) {
		this._data = data;
		this._fileName = fileName;
		this._formName = formName;
		if (contentType != null && contentType.equals(""))
			this._contentType = contentType;
	}

	public byte[] getData() {
		return this._data;
	}

	public void setData(byte[] data) {
		this._data = data;
	}

	public String getFileName() {
		return this._fileName;
	}

	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	public String getFormName() {
		return this._formName;
	}

	public void setFormName(String formName) {
		this._formName = formName;
	}

	public String getContentType() {
		return this._contentType;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
	}

}