package com.example.fmonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityMain extends Activity {

	private BedDocument mActiveBedDoc = null;
	private BedDocumentList mBedDocList = null;
	private BedPanelList mBedPanelList = null;
	private LinearLayout mBedPanelLinearLayout = null;
	private NetDataProcessor mNetDataProcessor = null;
	private CTGChartDraw mDrawDocument = null;
	private CTGChartViewBase mCTGChartViewBase = null;
	private CTGChartViewValue mCTGChartViewValue = null;
	private CTGChartViewLine mCTGChartViewLine = null;

	private GestureDetector mGestureDetector;

	private Handler mMsgHandler;
	private SharedPreferences sp = null;
//	private InetAddress mServerAddress = null;
//	private int mServerPort = 2013;
//	DatagramSocket mServerSocket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏.
		setContentView(R.layout.activity_main);

		createMsgHandler();
		setMachineNo();
		sp = getSharedPreferences("SERVERINFO", Context.MODE_WORLD_READABLE);
		
		int serverPort = Integer.parseInt(sp.getString("SERVERPORT", "2013")); 
		String serverAddress = sp.getString("SERVERADDRESS", "192.168.1.123");
		NetSocketUDP.getInstance().setPort(serverPort);
		NetSocketUDP.getInstance().setAddress(serverAddress);
		 
		mBedPanelList = new BedPanelList();
		mBedPanelLinearLayout = (LinearLayout) findViewById(R.id.scrollView1LinearLayout);

		mBedDocList = new BedDocumentList(ActivityMain.this);
		mBedDocList.setMainMsgHandler(mMsgHandler);

		mNetDataProcessor = new NetDataProcessor();
		mNetDataProcessor.setBedDocList(mBedDocList); 
		NetSocketUDP.getInstance().setNetDataProcessor(mNetDataProcessor);
		
//		startUDPListen();

		mDrawDocument = new CTGChartDraw();

		// 添加CTG图曲线视图
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// lp.addRule(RelativeLayout.RIGHT_OF, R.id.activity_main_layout_left);
		final RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.activity_main_layout_right);

		mCTGChartViewBase = new CTGChartViewBase(this);
		mCTGChartViewBase.setDocumentDraw(mDrawDocument);
		mCTGChartViewBase.setLayoutParams(lp);
		rLayout.addView(mCTGChartViewBase);
		mCTGChartViewBase.invalidate();

		mCTGChartViewValue = new CTGChartViewValue(this);
		mCTGChartViewValue.setDocumentDraw(mDrawDocument);
		mCTGChartViewValue.setLayoutParams(lp);
		rLayout.addView(mCTGChartViewValue);

		mCTGChartViewLine = new CTGChartViewLine(this);
		mCTGChartViewLine.setDocumentDraw(mDrawDocument);
		mCTGChartViewLine.setLayoutParams(lp);
		rLayout.addView(mCTGChartViewLine);

		mGestureDetector = new GestureDetector(ActivityMain.this,
				onGestureListener);

		mCTGChartViewLine.setLongClickable(true);
		// findViewById(R.id.activity_main_layout_right).setLongClickable(true);
		mCTGChartViewLine.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		LISTEN = false;
		Log.e(SystemDefine.LOG_TAG, " this destroy....");
		mBedDocList = null;
		mBedPanelList = null;
		mBedPanelLinearLayout = null;
		mNetDataProcessor = null;
		mDrawDocument = null;
		mCTGChartViewBase = null;

		super.onDestroy();
	}

	private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			float x = e2.getX() - e1.getX();
			float y = e2.getY() - e1.getY();

			if (Math.abs(x) > Math.abs(y)) {
				// 左右滑动 if(y>0) {/*向右滑动 */}else { /*向左滑动*/ }
				mCTGChartViewLine.onFlingRefresh((int) y);
			} else {
				// 上下滑动
				if (x > 0) { // 向上滑动时弹出菜单
					displayMenuButton();
					// if (mCTGChartViewLine.getBedNo() > 0) {
					// displayMenuButton();
					// }
				}
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

	};

	private void displayMenuButton() {
		final PopupMenuButtom popupMenu = new PopupMenuButtom(
				ActivityMain.this, findViewById(R.id.activity_main_layout));
		popupMenu.setMenuClickCallBack(new PopupMenuClickInterface() {
			@Override
			public void menuClick(int itemFlag) {

				// TODO Auto-generated method stub
				if (popupMenu.MENU_SET == itemFlag) {
					clickSystemSet();
				}
			}
		});
		popupMenu.showPopupMenu();
	}

	private void clickSystemSet() {
		SystemSetEdit systemSetEdit = new SystemSetEdit(ActivityMain.this);
		systemSetEdit.showDialog();
	}
 
	private void createMsgHandler() {
		mMsgHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				if (SystemDefine.MESSAGE_ADD_BED == msg.what) {
					addBedPanel((String) msg.obj);
				}
				if (SystemDefine.MESSAGE_REFRESH_DATA == msg.what) {
					refreshBedPanel((String) msg.obj);
				}
				if (SystemDefine.MESSAGE_STOP_DETECT == msg.what) {
//					mDetectMsg = false; 
					 Log.e(SystemDefine.LOG_TAG, "set detect off ******" );
				}				
			}
		};
	}

	protected void refreshBedPanel(String bedNo) {
		// TODO Auto-generated method stub
		mBedPanelList.getBedPanel(bedNo).updateDisplay();
		// Log.e(SystemDefine.LOG_TAG, "bed no " + bedNo);
		// Log.e(SystemDefine.LOG_TAG, "active bed no " +
		// mActiveBedDoc.getBedNo());
		if (bedNo.equals(mActiveBedDoc.getBedNo())) {
			mCTGChartViewValue.drawBedDocValue(mActiveBedDoc);
			mCTGChartViewLine.drawBedDoc(mActiveBedDoc);
		}
	}

	private void addBedPanel(String bedNo) {
		// Log.e(SystemDefine.LOG_TAG, "add bed panel " + bedNo);
		BedPanel bedPanel = mBedPanelList.getBedPanel(bedNo);
		if (null == bedPanel) {
			bedPanel = new BedPanel(ActivityMain.this, bedNo);
			bedPanel.setBedDocument(mBedDocList.getBedDocument(bedNo));
			mBedPanelList.addBedPanel(bedPanel);
			mBedPanelLinearLayout.addView(bedPanel);

			if (null == mActiveBedDoc) {
				mActiveBedDoc = mBedDocList.getBedDocument(bedNo);
			}
		}
	}

	private void setMachineNo() {
		  //获取wifi服务  
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();
		SystemDefine.getInstance().setMachineNo(ipAddress);
	}
}
