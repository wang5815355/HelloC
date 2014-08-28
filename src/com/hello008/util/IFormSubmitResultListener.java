package com.hello008.util;

import java.util.EventListener;

//定义一个接口,像C#中的delegate
interface IFormSubmitResultListener extends EventListener {
	void getdata(FormSubmitResultArgs e);
}
