package com.hello008.hello;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hello008.base.BaseUi;
import com.hello008.base.C.action.edittext;
import com.hello008.model.Customer;
import com.hello008.util.FormSubmit;
import com.hello008.util.FormSubmitResultArgs;
import com.hello008.util.FormSubmitResultListener;
import com.hello008.util.SDUtil;

public class RegisterTwoActivity extends BaseUi {
	private ImageView photo;
	private Button logbutton;
	private EditText uname;
	String fileName = "test";
	String unameString ;
	String url = "http://www.hello008.com/Public/registerIn_phone_info";
	String imguri;
	Bitmap bmp; 
	//创建遮罩dialog
	Dialog dialog;

	/**
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_two);
		photo = (ImageView) this.findViewById(R.id.faceimg);
		logbutton = (Button) this.findViewById(R.id.logbutton);
		dialog = new Dialog(RegisterTwoActivity.this, R.style.mydialog);
		uname = (EditText)this.findViewById(R.id.uname);
		
		// 把文字控件添加监听，点击弹出自定义窗口
		photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
				startActivityForResult(new Intent(RegisterTwoActivity.this,
						SelectPicActivity.class), 1);
			}
		});
		
		//当session为空时
		Customer customer = Customer.getInstance();
		String sid = customer.getSid();
		if(sid == null){
			RegisterTwoActivity.this.forward(MainActivity.class);
		}
		
		// 添加注册按钮响应事件监听
		logbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				unameString = uname.getText().toString();
				
				if(bmp == null){
					Toast.makeText(RegisterTwoActivity.this,"请点击图片选择真实头像上传",Toast.LENGTH_LONG).show();
					return;
				}else if(unameString == null){
					Toast.makeText(RegisterTwoActivity.this,"请填写真实中文姓名",Toast.LENGTH_LONG).show();
					return;
				}
				
				// 創建表單提交對象
				FormSubmit submit = new FormSubmit(RegisterTwoActivity.this, url);
				// 添加表單參數
				Log.w("uname=====================","123123");
				submit.AddParams("uname",unameString);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.JPEG, 30, baos);
				
				// 添加圖片文件
				submit.AddFile("photo1", "1408102736210.jpeg", baos.toByteArray());
				
				submit.addSubmitListener(new FormSubmitResultListener(){
					 // 取得表單數據
					 public void getdata(FormSubmitResultArgs e) {
						 JSONObject jo = null;
					   	 JSONTokener jsonParser;
					   	 
						try {
							jsonParser = new JSONTokener(e.getData());  
							jo = (JSONObject) jsonParser.nextValue();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						 
						  // 是否成功取得請求數據
						  try {
							if(jo.getString("status").equals("1")) { 
								dialog.cancel();
								
								SharedPreferences setting = getPreferences(Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = setting.edit();
								editor.putString("regstep","2");//注册步骤 1，提交资料，2 提交头像姓名
								editor.commit();
								
								RegisterTwoActivity.this.forward(IndexActivity.class);//跳转到提交头像姓名资料页面
							 } else {
								dialog.cancel();
								Toast.makeText(RegisterTwoActivity.this,jo.getString("info"),Toast.LENGTH_SHORT).show();
							 }
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					 }
				
				});
				
				dialog.setContentView(R.layout.main_dialog);  
				LayoutParams lay = dialog.getWindow().getAttributes();  
				setParams(lay);//设置遮罩参数  
				dialog.show();
				
				// 提交表單
				submit.submit();
		}
	 });
	}
	
	/**
     * 设置遮罩dialog样式
     * @author wangkai
     * */
    private void setParams(LayoutParams lay) {  
    	 DisplayMetrics dm = new DisplayMetrics();  
    	 getWindowManager().getDefaultDisplay().getMetrics(dm);  
    	 Rect rect = new Rect();  
    	 View view = getWindow().getDecorView();  
    	 view.getWindowVisibleDisplayFrame(rect);  
    	 lay.height = dm.heightPixels - rect.top;  
    	 lay.width = dm.widthPixels;  
     }  

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			if (data != null) {
				// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				String mImageCaptureUri = data.getStringExtra(KEY_PHOTO_PATH);
				this.imguri = mImageCaptureUri;

				// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null) {
					Bitmap image;
					try {
						// 这个方法是根据Uri获取Bitmap图片的静态方法
						// image =
						// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
						// mImageCaptureUri);
						image = SDUtil.getImageUri(mImageCaptureUri);
						if (image != null) {
							photo.setImageBitmap(image);
							this.bmp = image;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						Bitmap image = extras.getParcelable("data");
						if (image != null) {
							photo.setImageBitmap(image);
						}
					}
				}

			}
			break;
		default:
			break;

		}
	}

}
