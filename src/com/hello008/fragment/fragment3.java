package com.hello008.fragment;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hello008.base.C;
import com.hello008.hello.R;
import com.hello008.model.Customer;
import com.hello008.util.AppCache;


public class fragment3 extends Fragment{
	private View mMainView;
	private TextView tv;
	private Button btn;
	private ImageView sweepIV;
	private ImageView myface;
	private static Customer customer;
	private TextView uname;
	private TextView phonenumber;
	
	private int QR_WIDTH = 295;
	private int QR_HEIGHT = 295;
	
	public static void  setCustomer(Customer cs){
		customer = cs;
	}
	
	public void createQRImage(String url){
	        try
	        {
	            //判断URL合法性
	            if (url == null || "".equals(url) || url.length() < 1)
	            {
	                return;
	            }
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            //图像数据转换，使用了矩阵转换
	            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            //下面这里按照二维码的算法，逐个生成二维码的图片，
	            //两个for循环是图片横列扫描的结果
	            for (int y = 0; y < QR_HEIGHT; y++)
	            {
	                for (int x = 0; x < QR_WIDTH; x++)
	                {
	                    if (bitMatrix.get(x, y))
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    }
	                    else
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }
	                }
	            }
	            //生成二维码图片的格式，使用ARGB_8888
	            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            //显示到一个ImageView上面
	            sweepIV = (ImageView)mMainView.findViewById(R.id.qrcode);
	            sweepIV.setImageBitmap(bitmap);
	        }catch (WriterException e){
	            e.printStackTrace();
	        }
	}
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("huahua", "fragment3-->onCreate()");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.frag3, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		this.createQRImage("http://www.baidu.com");
		
		Bitmap faceimgbit = AppCache.getImageBydir("http://www.hello008.com/Public/Uploads/"+customer.getFace(),C.dir.facesoriginal);
		myface = (ImageView)mMainView.findViewById(R.id.myface);
		myface.setImageBitmap(faceimgbit);
		uname = (TextView)mMainView.findViewById(R.id.uname);
		phonenumber = (TextView)mMainView.findViewById(R.id.uphone);
		uname.setText(customer.getName());
		phonenumber.setText(customer.getPhonenumber());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("huahua", "fragment3-->onCreateView()");
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.v("huahua", "fragment3-->移除已存在的View");
        } 
		
		return mMainView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("huahua", "fragment3-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("huahua", "fragment3-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("huahua", "fragment3-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("huahua", "fragment3-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("huahua", "fragment3-->onStop()");
	}

}