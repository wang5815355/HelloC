package com.hello008.fragment;

import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hello008.base.C;
import com.hello008.hello.R;
import com.hello008.util.AppCache;


public class fragment1 extends ListFragment{
	  
//    private AnimalListAdapter adapter = null;  
	List<Map<String, Object>> friends;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
    }  
  
	public List<Map<String, Object>> getFriends() {
		return friends;
	}

	public void setFriends(List<Map<String, Object>> friends) {
		this.friends = friends;
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View v = inflater.inflate(R.layout.frag1, container, false);  
        return v;  
    }  
      
    @Override  
    public void onListItemClick(ListView l, View v, final int position, long id) {  
        super.onListItemClick(l, v, position, id);  
        
    	TextView indexDiaPhone = null;
		TextView indexDiaName = null;
		ImageView faceImgView = null;
		
		//创建遮罩dialog
		Dialog dialog  = new Dialog(getActivity(), R.style.mydialog);
		dialog.setContentView(R.layout.indexitem_dialog);  
		indexDiaPhone = (TextView) dialog.findViewById(R.id.index_item_dialog_phone); 
		indexDiaName = (TextView) dialog.findViewById(R.id.index_item_dialog_name);
		faceImgView = (ImageView) dialog.findViewById(R.id.index_item_dialog_faceimg);
		Button callBtn = (Button)dialog.findViewById(R.id.callbutton);
		
		indexDiaPhone.setText((String)friends.get((int)position).get("uphone"));
		indexDiaName.setText((String)friends.get((int)position).get("uname"));
		String faceimgurl = (String)friends.get((int)position).get("faceimgurl");
		Bitmap faceimgbit = AppCache.getImageBydir("http://www.hello008.com/Public/Uploads/"+faceimgurl,C.dir.facesoriginal);
		faceImgView.setImageBitmap(faceimgbit);
		
		//设置拨号按钮事件
		callBtn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//调用系统的拨号服务实现电话拨打功能
	            //封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
	            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+(String)friends.get((int)position).get("uphone")));
	            getActivity().startActivity(intent);//内部类
			}
		});
		
		LayoutParams lay = dialog.getWindow().getAttributes();  
		setParams(lay);//设置遮罩参数  
		dialog.show();
    }
    
    /**
     * 设置遮罩dialog样式
     * @author wangkai
     **/
    private void setParams(LayoutParams lay) {  
    	 DisplayMetrics dm = new DisplayMetrics();  
    	 getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);  
    	 Rect rect = new Rect();  
    	 View view = getActivity().getWindow().getDecorView();  
    	 view.getWindowVisibleDisplayFrame(rect);  
    	 lay.height = dm.heightPixels - rect.top;  
    	 lay.width = dm.widthPixels;  
     }  

}