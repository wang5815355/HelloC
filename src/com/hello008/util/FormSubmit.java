package com.hello008.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.util.Log;

// 提交表单
public class FormSubmit {

	// 用一个java.util.Vector对象来存储所有的事件监听器对象。
	private Vector<FormSubmitResultListener> _resultEvents = new Vector<FormSubmitResultListener>();

	// 添加删除事件监听
	public synchronized void addSubmitListener(
			FormSubmitResultListener onFormSubmitResultListener) {
		this._resultEvents.add(onFormSubmitResultListener);
	}

	// 删除删除事件监听
	public synchronized void removeSubmitListener(
			FormSubmitResultListener onFormSubmitResultListener) {
		this._resultEvents.remove(onFormSubmitResultListener);
	}

	// 触发表单提交回传事件
	protected void getSubmitResult(boolean success, String data) {
		// 锁定，避免在触发期间有事件被订阅或移除。
		synchronized (this) {
			FormSubmitResultArgs arg = new FormSubmitResultArgs(success, data);
			// 循环触发所有的事件订阅方法。
			for (int i = 0; i < this._resultEvents.size(); i++) {
				this._resultEvents.get(i).getdata(arg);
			}
		}
	}

	private Context _context = null; // 当前上下文
	private String _url = null; // 上传表单URL地址
	private boolean _success = false; // 请求是否成功
	private Map<String, String> _params; // 表单参数
	private List<FormFile> _fileParams; // 上传文件参数
	public String Encoding = "UTF-8"; // 编码方式
	public String Enctype = "multipart/form-data"; // 表单类型
	public String Boundary = "---------123456789"; // 数据分界线
	public String _lineEnd = System.getProperty("line.separator"); // line.separator

	public FormSubmit(Context context, String url) {
		_context = context;
		_url = url;
		_params = new HashMap<String, String>();
		_fileParams = new ArrayList<FormFile>();
	}

	// 添加参数
	public void AddParams(String name, String value) {
		_params.put(name, value);
	}

	// 添加上传文件
	public void AddFile(FormFile file) {
		_fileParams.add(file);
	}

	public void AddFile(String formName, String fileName, byte[] data) {
		_fileParams.add(new FormFile(formName, fileName, data, null));
	}

	// 取得请求连线
	private HttpURLConnection getConn() throws IOException {
		URL url = new URL(this._url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setConnectTimeout(6* 1000); // 设置连接超时
		conn.setDoInput(true); // 允许输入
		conn.setDoOutput(true); // 允许输出
		conn.setUseCaches(false); // 不使用Cache
		conn.setRequestMethod("POST"); // 请求方式
		conn.setRequestProperty("Connection", "Connection"); // http头信息 -
																// 保持一段时间的连线
		conn.setRequestProperty("Charset", Encoding); // http头信息 - 编码
		conn.setRequestProperty("Content-Type", Enctype + "; boundary="
				+ Boundary); // http头信息 - 表单类型及数据分界线
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
		StringBuilder sb = new StringBuilder();
		// 设置表单参数 -构建表单字段内容
		for (Map.Entry<String, String> entry : this._params.entrySet()) {
			// 第一行
			sb.append("--");
			sb.append(this.Boundary);
			sb.append(this._lineEnd);
			// 第二行
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"");
			sb.append(this._lineEnd);
			// 第三行
			sb.append(this._lineEnd);
			// 第四行
			sb.append(entry.getValue());
			sb.append(this._lineEnd);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		// 将表单数据写入到请求中
		outStream.write(sb.toString().getBytes());

		// 设置上传文件
		for (FormFile file : this._fileParams) {
			StringBuilder split = new StringBuilder();
			// 第一行
			split.append("--");
			split.append(this.Boundary);
			split.append(this._lineEnd);
			// 第二行
			split.append("Content-Disposition: form-data; name=\""
					+ file.getFormName() + "\"" + ";filename=\""
					+ file.getFileName() + "\"");
			split.append(this._lineEnd);
			// 第三行
			split.append("Content-Type: " + file.getContentType());
			split.append(this._lineEnd);
			// 第四行
			split.append(this._lineEnd);
			outStream.write(split.toString().getBytes());
			// 第五行
			outStream.write(file.getData(), 0, file.getData().length);
			outStream.write(this._lineEnd.getBytes());
		}
		// 数据结束标志
		byte[] end_data = ("--" + this.Boundary + "--" + this._lineEnd)
				.getBytes();
		outStream.write(end_data);
		outStream.flush();
		// outStream.close();
		return conn;
	}

	// 提交表单
	public void submit() {
		this._asyn.execute();
	}

	// 定义一个异步操作
	android.os.AsyncTask<Void, Void, String> _asyn = new android.os.AsyncTask<Void, Void, String>() {
		private android.app.ProgressDialog _dialog;

		// 执行前调用[第一个执行方法 ]
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this._dialog = new android.app.ProgressDialog(_context);
			this._dialog.setTitle("与服务器连线中");
			this._dialog.show();
		}

		// 执行中[第二个执行方法]
		@Override
		protected String doInBackground(Void... params) {
			String result = "";
			try {
				HttpURLConnection conn = getConn();
				// 请求数据
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream();
					int ch;
					StringBuilder b = new StringBuilder();
					while ((ch = is.read()) != -1) {
						b.append((char) ch);
					}
					conn.disconnect();
					_success = true; // 表示请求成功
					result = b.toString();
				} else {
					Log.i("http请求失败", String.valueOf(code));
					result = String.format("http请求失败:%1$d", code);
				}
			} catch (Exception e) {
				result = String.format("提交表单失败:%1$s", e.getMessage());
			}
			return result;
		}

		// 这个函数在doInBackground调用publishProgress时触发
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
		// 这里的result就是上面doInBackground执行后的返回值
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			this._dialog.cancel();
			getSubmitResult(_success, result);
		}

		// 用户取消线程时执行
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	};
}

