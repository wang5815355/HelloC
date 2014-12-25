package com.hello008.base;

import com.hello008.util.AppUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BaseFragmentHandler extends Handler {
	
	protected BaseFragmentUi fui;
	
	public BaseFragmentHandler (BaseFragmentUi fui) {
		this.fui = fui;
	}
	
	public BaseFragmentHandler (Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
			switch (msg.what) {
				case BaseTask.TASK_COMPLETE:
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					if (result != null) {
					} else if (!AppUtil.isEmptyInt(taskId)) {
					} else {
					}
					break;
				case BaseTask.NETWORK_ERROR:
					taskId = msg.getData().getInt("task");
					break;
				case BaseTask.SHOW_LOADBAR:
					break;
				case BaseTask.HIDE_LOADBAR:
					break;
				case BaseTask.SHOW_TOAST:
					result = msg.getData().getString("data");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}