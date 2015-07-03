package com.hello008.hello;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hello008.base.BaseFragmentHandler;
import com.hello008.base.BaseFragmentUi;
import com.hello008.base.BaseTask;
import com.hello008.base.C;
import com.hello008.fragment.fragment1;
import com.hello008.fragment.fragment2;
import com.hello008.fragment.fragment3;
import com.hello008.list.FriendList;
import com.hello008.model.Circle;
import com.hello008.model.Customer;
import com.hello008.model.Friend;
import com.hello008.service.PollingService;
import com.hello008.service.UpdatePollingService;
import com.hello008.sqlite.CircleSqlite;
import com.hello008.sqlite.FriendSqlite;
import com.hello008.util.AppCache;
import com.hello008.util.AppClient;
import com.hello008.util.HttpUtil;
import com.hello008.util.JsonParser;
import com.hello008.util.PollingUtils;

public class testActivity extends BaseFragmentUi {
	public static testActivity ts = null;

	private ViewPager m_vp;
	private fragment1 mfragment1;
	private fragment2 mfragment2;
	private fragment3 mfragment3;
	ImageButton img1, img2, img3;
	private int currIndex = -1;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();

	private String friendResult = null;// 返回好友信息
	private String circleResult = null;// 返回圈子信息
	private String customerResult = null;// 返回个人信息
	// private SimpleAdapter sadapter;
	private FriendList friendList = null;
	private FriendSqlite friendSqlite = null;
	private CircleSqlite circlesqlite = null;
	Dialog dialogLoad = null;

	// 接收广播 当用户数据更新时
	private BroadcastReceiver br = new BroadcastReceiver() {
		List<Map<String, Object>> friends;

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.w("polling", "接收广播");
			friendSqlite = new FriendSqlite(testActivity.this);
			friends = friendSqlite.getAllFriends();
			// 加载好友头像
			for (Map<String, Object> friend : friends) {
				loadImage("http://www.hello008.com/Public/Uploads/"
						+ (String) friend.get("faceimgurl"));
			}
			if (friendList != null) {
				friendList.setFriendList(friends);
				friendList.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		ts = this;

		img1 = (ImageButton) findViewById(R.id.cursor1);
		img2 = (ImageButton) findViewById(R.id.cursor2);
		img3 = (ImageButton) findViewById(R.id.cursor3);
		m_vp = (ViewPager) findViewById(R.id.viewpager);

		img1.setOnClickListener(new MyOnClickListener(0));
		img2.setOnClickListener(new MyOnClickListener(1));
		img3.setOnClickListener(new MyOnClickListener(2));

		// pagerTitleStrip=(PagerTitleStrip) findViewById(R.id.pagertab);
		// //设置背景的颜色
		// pagerTitleStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

		mfragment1 = new fragment1();
		mfragment2 = new fragment2();
		mfragment3 = new fragment3();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(mfragment1);
		fragmentList.add(mfragment2);
		fragmentList.add(mfragment3);

		titleList.add("第一页 ");
		titleList.add("第二页");
		titleList.add("第三页 ");

		m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		m_vp.setOnPageChangeListener(new MyOnPageChangeListener());

		// 创建加载dialog
		dialogLoad = new Dialog(testActivity.this, R.style.mydialog);
		dialogLoad.setContentView(R.layout.index_load);
		LayoutParams layLoad = dialogLoad.getWindow().getAttributes();
		setParams(layLoad);// 设置遮罩参数
//		dialogLoad.show();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", "1");// 手机客户端 请求全部好友
		AnsyTry anysFragment1 = new AnsyTry(map, dialogLoad, mfragment1,1);
		anysFragment1.execute();
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		AnsyTry anysFragment2 = new AnsyTry(map2, dialogLoad, mfragment1,2);
		anysFragment2.execute();
		
		HashMap<String, String> map3 = new HashMap<String, String>();
		AnsyTry anysFragment3 = new AnsyTry(map3, dialogLoad, mfragment1,3);
		anysFragment3.execute();
		
		// 广播接收器的动态注册，Action必须与Service中的Action一致
		registerReceiver(br, new IntentFilter("ACTION_MY"));
		// 启动检测版本更新service轮询
		PollingUtils.startPollingService(testActivity.this, 60 * 60 * 24 * 2,
				UpdatePollingService.class, UpdatePollingService.ACTION_UPDATE);
		
	}

	/**
	 * 变更listview
	 * 
	 * @author wangkai
	 */
	protected void changeListView(final List<Map<String, Object>> friends) {
		// 自定义adapter
		friendList = new FriendList(testActivity.this, friends);
		ListView list = (ListView) findViewById(R.id.friendlist);
		list.setAdapter(friendList);
		// Toast.makeText(testActivity.this,friendResult,Toast.LENGTH_LONG).show();
		this.setHandler(new IndexHandler(this, friendList));

		// 设置listviewitem监听器
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView indexDiaPhone = null;
				TextView indexDiaName = null;
				ImageView faceImgView = null;

				// 创建遮罩dialog
				Dialog dialog = new Dialog(testActivity.this, R.style.mydialog);
				dialog.setContentView(R.layout.indexitem_dialog);
				indexDiaPhone = (TextView) dialog
						.findViewById(R.id.index_item_dialog_phone);
				indexDiaName = (TextView) dialog
						.findViewById(R.id.index_item_dialog_name);
				faceImgView = (ImageView) dialog
						.findViewById(R.id.index_item_dialog_faceimg);

				indexDiaPhone.setText((String) friends.get((int) arg3).get(
						"uphone"));
				indexDiaName.setText((String) friends.get((int) arg3).get(
						"uname"));
				String faceimgurl = (String) friends.get((int) arg3).get(
						"faceimgurl");

				Bitmap faceimgbit = AppCache
						.getImage("http://www.hello008.com/Public/Uploads/"
								+ faceimgurl);
				faceImgView.setImageBitmap(faceimgbit);

				LayoutParams lay = dialog.getWindow().getAttributes();
				setParams(lay);// 设置遮罩参数
				dialog.show();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 关闭service轮询
		PollingUtils.stopPollingService(this, PollingService.class,
				PollingService.ACTION);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("pagenum","1");
		// AnsyTry anys=new AnsyTry(map,dialogLoad);
		// anys.execute();

		// 启动轮询service
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 启动轮询service
				// PollingUtils.startPollingService(testActivity.this, 15,
				// PollingService.class, PollingService.ACTION);
			}
		}, 500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);// true对任何Activity都适用
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 设置遮罩dialog样式
	 * 
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

	class AnsyTry extends AsyncTask<String, HashMap<String, String>, List<Map<String, Object>>> {
		JSONObject jo;
		JSONTokener jsonParser;
		SharedPreferences setting;
		HashMap<String, String> map;
		Dialog dialogLoad;
		int newVerCode;
		String newVerName;
		ProgressDialog pBar = null;
		fragment1 mfragment1;
		int tag;
		Customer customer;//用户个人信息

		public AnsyTry(HashMap<String, String> map, Dialog dialogLoad,
				fragment1 mfragment1,int tag) {
			super();
			this.map = map;
			this.dialogLoad = dialogLoad;
			this.mfragment1 = mfragment1;
			this.tag = tag;//1：执行fragment1内容异步  2：fragment2 3：fragment3
		} 

		@Override
		/**
		 * 网络耗时操作
		 */
		protected List<Map<String, Object>> doInBackground(String... params) {
			if(tag == 1){//fragment 1
				List<Map<String, Object>> friends = null;
				Friend friendO = null;
				AppClient client = new AppClient("/Index/queryMyFriend2");// 客户端初始化
				friendSqlite = new FriendSqlite(testActivity.this);
				
				// 判断网络连接状态
				Integer netType = HttpUtil.getNetType(testActivity.this);
				if (netType != HttpUtil.NONET_INT) {// 网络连接正常
					// updateApp(); 检测是否有版本更新

					try {
						friends = friendSqlite.getAllFriends();
						Log.w("test1===", "sql2");
						if (friends.size() == 0) {
							// 网络请求
							friendResult = client.post(map);
							Log.w("test1===",map.toString()+friendResult.toString());
							// JSON 解析
							friends = JsonParser.parseJsonList(friendResult);
							
							for (Map<String, Object> friend : friends) {
								friendO = new Friend();
								friendO.setFaceimage((String) friend
										.get("faceimgurl"));
								friendO.setId((String) friend.get("id"));
								friendO.setUname((String) friend.get("uname"));
								friendO.setUphone((String) friend.get("uphone"));
								// 将数据存数sqllit中
								friendSqlite.updateFriend(friendO);
								Log.w("friends", friendO.toString());
								loadImage("http://www.hello008.com/Public/Uploads/"
										+ (String) friend.get("faceimgurl"));
							}
							
						} else {
							for (Map<String, Object> friend : friends) {
								loadImage("http://www.hello008.com/Public/Uploads/"
										+ (String) friend.get("faceimgurl"));
							}
						}
						

					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					// 网络连接断开时从sqllite中取出信息
					friends = friendSqlite.getAllFriends();
					for (Map<String, Object> friend : friends) {
						loadImage("http://www.hello008.com/Public/Uploads/"
								+ (String) friend.get("faceimgurl"));
					}
					
				}
			
				return friends;
			}else if(tag == 2){//fragment2
				List<Map<String, Object>> circles = null;
				Circle circleO = null;
				AppClient client = new AppClient("/Index/myCircle");// 客户端初始化
				circlesqlite = new CircleSqlite(testActivity.this);
				// 判断网络连接状态
				Integer netType = HttpUtil.getNetType(testActivity.this);
				if (netType != HttpUtil.NONET_INT) {// 网络连接正常
					try {
						circles = circlesqlite.getAllCircles();
						if (circles.size() == 0) {
							// 网络请求
							circleResult = client.post(map);
							Log.w("customermsg", circleResult.toString());
							// JSON 解析
							circles = JsonParser.parseJsonListCircle(circleResult);

							for (Map<String, Object> circle : circles) {
								circleO = new Circle();
								//将数据存储入sqlight
								circleO.setCircleid((String) circle.get("circleid"));
								circleO.setCirclename((String) circle.get("circlename"));
								circleO.setCount((String) circle.get("count"));
								circleO.setFaceimg((String) circle.get("faceimg"));
								circleO.setId((String) circle.get("id"));
								circleO.setIsCreater((String) circle.get("isCreater"));
								circleO.setPhonenumber((String) circle.get("phonenumber"));
								circleO.setStatus((String) circle.get("status"));
								circleO.setTime((String) circle.get("time"));
								circleO.setUemail((String) circle.get("uemail"));
								circleO.setUname((String) circle.get("uname"));
								
								circlesqlite.updateCricle(circleO);
							}
						} else {
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					// 网络连接断开时从sqllite中取出信息
					circles = circlesqlite.getAllCircles();
				}
				return circles;
			}else if(tag == 3){//fragment3 
				AppClient client = new AppClient("/Index/requestMyInfoPhone");// 客户端初始化
				// 判断网络连接状态
				Integer netType = HttpUtil.getNetType(testActivity.this);
				if (netType != HttpUtil.NONET_INT) {// 网络连接正常
					try {
						customerResult = client.post(map);
						Log.w("customermsg", "123");
						Log.w("customermsg", customerResult.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					customer = JsonParser.parseJsonCustomer(customerResult);
					setting = getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = setting.edit();
					editor.putString("phonenumber",customer.getPhonenumber());
					editor.putString("faceimg",customer.getFace());
					editor.putString("name",customer.getName());
					editor.commit();
				}else{
					// 网络连接断开时
					setting = getPreferences(Context.MODE_APPEND);
					String name = setting.getString("name","");
					String faceimg = setting.getString("faceimg","");
					String phonenumber = setting.getString("phonenumber","");
					customer.setName(name);
					customer.setFace(faceimg);
					customer.setPhonenumber(phonenumber);
				}
				loadImage("http://www.hello008.com/Public/Uploads/"
						+ customer.getFace());
				return null;
			}
				
			return null;
		}

		@Override
		/**
		 * 执行ui变更操作
		 */
		protected void onPostExecute(final List<Map<String, Object>> areas) {
			if(tag == 1){
				// 自定义adapter
				friendList = new FriendList(testActivity.this, areas);
				// ListView list = (ListView) findViewById(R.id.friendlist);
				// BounceListView list = new BounceListView(testActivity.this);
				mfragment1.setFriends(areas);
				mfragment1.setListAdapter(friendList);
				// Toast.makeText(testActivity.this,friendResult,Toast.LENGTH_LONG).show();
				testActivity.this.setHandler(new IndexHandler(testActivity.this,
						friendList));
				testActivity.this.friendList = friendList;

				new Handler().postDelayed(new Runnable() {
					public void run() {
						dialogLoad.dismiss();
					}
				}, 1500);
				
			}else if(tag == 2){
				fragment2.setCircles(areas,1);
				//发送广播
				Intent intent = new Intent("ACTION_FRAGMENT2");
				intent.putExtra("areas", (Serializable) areas);
				LocalBroadcastManager.getInstance(testActivity.this).sendBroadcast(intent);
			}else if(tag == 3){
				fragment3.setCustomer(customer);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onProgressUpdate(Integer... values) {

		}

	}

	/**
	 * 图片异步下砸成功后更新ui
	 */
	private class IndexHandler extends BaseFragmentHandler {
		// SimpleAdapter sadapter;
		private FriendList friendList;
		private ProgressDialog pBar;

		public IndexHandler(BaseFragmentUi fui, FriendList friendList) {
			super(fui);
			this.friendList = friendList;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case BaseTask.LOAD_IMAGE:
					friendList.notifyDataSetChanged();
					break;
				case BaseTask.UPDATE_APP:
					AlertDialog alert = null;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							testActivity.this);
					builder.setMessage("软件更新")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// pBar = new
											// ProgressDialog(testActivity.this);
											// pBar.setTitle("正在下载");
											// pBar.setMessage("请稍候...");
											// pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											// pBar.show();
											// downApp("http://www.hello008.com/Public/App/Hello.apk",pBar);
											// Uri uri =
											// Uri.parse("http://www.hello008.com/Public/App/Hello.apk");
											// Intent it = new
											// Intent(Intent.ACTION_VIEW, uri);
											// startActivity(it);
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					alert = builder.create();
					alert.show();
					break;
				}
			} catch (Exception e) {
				Toast.makeText(testActivity.this, e.toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation anim = null;
			switch (arg0) {
			case 0:
				img1.setBackgroundResource(R.color.blue2);
				img1.setImageResource(R.drawable.main);

				img2.setBackgroundResource(R.color.gray_tap);
				img2.setImageResource(R.drawable.circle_blue);

				img3.setBackgroundResource(R.color.gray_tap);
				img3.setImageResource(R.drawable.user_blue);
				break;
			case 1:
				img1.setBackgroundResource(R.color.gray_tap);
				img1.setImageResource(R.drawable.main_blue);

				img2.setBackgroundResource(R.color.blue2);
				img2.setImageResource(R.drawable.circle);

				img3.setBackgroundResource(R.color.gray_tap);
				img3.setImageResource(R.drawable.user_blue);
				break;
			case 2:
				img1.setBackgroundResource(R.color.gray_tap);
				img1.setImageResource(R.drawable.main_blue);

				img2.setBackgroundResource(R.color.gray_tap);
				img2.setImageResource(R.drawable.circle_blue);

				img3.setBackgroundResource(R.color.blue2);
				img3.setImageResource(R.drawable.user);
				break;
			default:
				break;
			}

		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			m_vp.setCurrentItem(index);
		}
	};

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}

	}

}