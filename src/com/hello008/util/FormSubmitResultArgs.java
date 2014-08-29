package com.hello008.util;

import java.util.EventObject;

//表单提交返回结果参数
@SuppressWarnings("serial")
public class FormSubmitResultArgs extends EventObject {
	private String _data;
	private boolean _success;

	public FormSubmitResultArgs(boolean success, String data) {
		super(data);
		this._success = success;
		this._data = data;
	}

	public String getData() {
		return this._data;
	}

	public boolean getSuccess() {
		return this._success;
	}
}
