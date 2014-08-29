package com.hello008.hello;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.hello008.base.BaseUi;
import com.hello008.util.FormSubmit;
import com.hello008.util.FormSubmitResultArgs;
import com.hello008.util.FormSubmitResultListener;
import com.hello008.util.SDUtil;

public class RegisterTwoActivity extends BaseUi {
	private ImageView photo;
	private Button logbutton;
	String fileName = "test";
	String url = "http://www.hello008.com/Public/registerIn_phone_2";
	String imguri;
	Bitmap bmp; 

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
		
		// 把文字控件添加监听，点击弹出自定义窗口
		photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
				startActivityForResult(new Intent(RegisterTwoActivity.this,
						SelectPicActivity.class), 1);
			}
		});
		
		// 添加注册按钮响应事件监听
		logbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.w("imguri",imguri);
				
				// 創建表單提交對象
				FormSubmit submit = new FormSubmit(RegisterTwoActivity.this, url);
				// 添加表單參數
				submit.AddParams("name", fileName);
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
						  if(e.getSuccess()) { 
						   try {
								new AlertDialog.Builder(RegisterTwoActivity.this)
								   .setTitle("请求成功")
								   .setMessage(jo.getString("status"))
								   .setNegativeButton("确定", null)
								   .show();
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						  } else {
						   new AlertDialog.Builder(RegisterTwoActivity.this)
						   .setTitle("请求失败")
						   .setMessage(e.getData())
						   .setNegativeButton("确定", null)
						   .show();
						  }
					 }
				
				});
				
				// 提交表單
				submit.submit();
		}
	 });
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
