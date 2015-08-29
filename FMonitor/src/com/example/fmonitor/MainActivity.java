package com.example.fmonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

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
	private InetAddress mServerAddress = null;
	private int mServerPort = 2013;
	DatagramSocket mSocket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏.
		setContentView(R.layout.activity_main);

		createMsgHandler();
		sp = getSharedPreferences("SERVERINFO", Context.MODE_WORLD_READABLE);

		mBedPanelList = new BedPanelList();
		mBedPanelLinearLayout = (LinearLayout) findViewById(R.id.scrollView1LinearLayout);

		mBedDocList = new BedDocumentList(MainActivity.this);
		mBedDocList.setMainMsgHandler(mMsgHandler);

		mNetDataProcessor = new NetDataProcessor();
		mNetDataProcessor.setBedDocList(mBedDocList);

		int mServerPort = Integer.parseInt(sp.getString("SERVERPORT", "2013")); 
		try {
			mServerAddress = InetAddress.getByName(sp.getString(
					"SERVERADDRESS", "192.168.1.123"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startUDPListen();

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

		mGestureDetector = new GestureDetector(MainActivity.this,
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
		LISTEN = false;
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
				MainActivity.this, findViewById(R.id.activity_main_layout));
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
		SystemSetEdit systemSetEdit = new SystemSetEdit(MainActivity.this);
		systemSetEdit.showDialog();
	}

	private boolean LISTEN = true;
	private boolean mDetectMsg = true;
	private void startUDPListen() {
		Thread udpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// Set up the socket and packet to receive Log.i(LOG_TAG,
					// "Incoming call listener started");
					DatagramSocket mSocket = new DatagramSocket(mServerPort);
					mSocket.setSoTimeout(1000);
					byte[] buffer = new byte[SystemDefine.BUF_SIZE];
					DatagramPacket packet = new DatagramPacket(buffer,
							SystemDefine.BUF_SIZE);
					while (LISTEN) {
						// Listen for incoming call requests Log.i(LOG_TAG,
						// "Listening for incoming calls");
						if(mDetectMsg) {
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block.
								e1.printStackTrace();
							}

							byte[] bDetectPacket = getDetectMsg();
							DatagramPacket p = new DatagramPacket(bDetectPacket,
									bDetectPacket.length, mServerAddress,
									mServerPort);
							try {
								Log.e(SystemDefine.LOG_TAG,"Send detect msg.....");
								mSocket.send(p);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						try {
							mSocket.receive(packet);
							String data = new String(buffer, 0,
									packet.getLength());
							// System.out.println("udp server receive:" + data +
							// " from " + packet.getAddress().getHostAdress() +
							// " from " + packet.getAddress().getHostName() );
							// Log.i(SystemDefine.LOG_TAG,
							// "Packet received from "+ packet.getAddress()
							// +" with contents: " + data);

							mNetDataProcessor.processorData(packet);
						} catch (Exception e) {
						}
					}
					// Log.i(LOG_TAG, "Call Listener ending");
					mSocket.disconnect();
					mSocket.close();
				} catch (SocketException e) {
					// Log.e(LOG_TAG, "SocketException in listener " + e);
				}
			}
		});
		udpThread.start();
	}

	private byte[] getDetectMsg() {
		byte[] bDetectPacket = new byte[17];
		int k = 0;
		// 2个标志.
		bDetectPacket[k++] = 0x55;
		bDetectPacket[k++] = (byte) 0xAA;
		// 2字节包长
		bDetectPacket[k++] = 0x11;
		bDetectPacket[k++] = 0x00;
		// 4字节包序号
		bDetectPacket[k++] = 0x00; // mPacketIndex;
		bDetectPacket[k++] = 0x00;
		bDetectPacket[k++] = 0x00;
		bDetectPacket[k++] = 0x00;
		// 1字节包状态 0不需要确认， 1需要确认， 2确认应答
		bDetectPacket[k++] = 0x01;
		// 1字节包类型
		bDetectPacket[k++] = SystemDefine.PACKET_DETECT; // 设备 嗅探包
		// 4字节机器编号
		bDetectPacket[k++] = 0x00;
		bDetectPacket[k++] = (byte) 0xD5;
		bDetectPacket[k++] = (byte) 0x94;
		bDetectPacket[k++] = 0x27;
		// N字节数据

		// 2字节，校验码
		bDetectPacket[k++] = (byte) 0xBD;
		bDetectPacket[k++] = (byte) 0xB1;
		// 1字节，包尾
		bDetectPacket[k++] = 0x03;

		return bDetectPacket;
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
					mDetectMsg = false; 
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
			bedPanel = new BedPanel(MainActivity.this, bedNo);
			bedPanel.setBedDocument(mBedDocList.getBedDocument(bedNo));
			mBedPanelList.addBedPanel(bedPanel);
			mBedPanelLinearLayout.addView(bedPanel);

			if (null == mActiveBedDoc) {
				mActiveBedDoc = mBedDocList.getBedDocument(bedNo);
			}
		}

	}

}
