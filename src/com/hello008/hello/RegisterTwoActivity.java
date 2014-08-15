package com.hello008.hello;

import com.hello008.util.SDUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class RegisterTwoActivity extends Activity {
	private ImageView photo;
	
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_two);
		photo = (ImageView) this.findViewById(R.id.faceimg);
		// 把文字控件添加监听，点击弹出自定义窗口
		photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
				startActivityForResult(new Intent(RegisterTwoActivity.this,
						SelectPicActivity.class), 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		switch (requestCode) {
		case 1:	
			if (data != null) {
				//取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				String mImageCaptureUri = data.getStringExtra(KEY_PHOTO_PATH);
				//返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				
				if (mImageCaptureUri != null) {
					Bitmap image;
					try {
						//这个方法是根据Uri获取Bitmap图片的静态方法
//						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						image = SDUtil.getImageUri(mImageCaptureUri);
						if (image != null) {
							photo.setImageBitmap(image);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						//这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
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
