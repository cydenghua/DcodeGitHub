package com.example.emonitor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

	private DrawCoordinateSystem mDrawCoordinateSystem;
	private DrawLineTemperature mDrawLineTemperature;
	private DrawLineHumidity mDrawLineHumidity;
	private DrawLineLightIntensity mDrawLineLightIntensity;
	private DataProcess mDataProcess;

	private TCPClient mTcpClient = null;
	private ThreadReceiveData mThreadReceiveData = null;

	private Button mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏.
		setContentView(R.layout.activity_main);

		mDataProcess = new DataProcess();

		// 添加曲线视图
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// lp.addRule(RelativeLayout.BELOW, R.id.button_line);
		final RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.main_activity_layout);

		mDrawCoordinateSystem = new DrawCoordinateSystem(this);
		mDrawCoordinateSystem.setLayoutParams(lp);
		rLayout.addView(mDrawCoordinateSystem);
		mDrawCoordinateSystem.invalidate();

		mDrawLineTemperature = new DrawLineTemperature(this);
		mDrawLineTemperature.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineTemperature.setDataProcess(mDataProcess);
		mDrawLineTemperature.setLayoutParams(lp);
		rLayout.addView(mDrawLineTemperature);
		mDrawLineTemperature.invalidate();

		mDrawLineHumidity = new DrawLineHumidity(this);
		mDrawLineHumidity.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineHumidity.setDataProcess(mDataProcess);
		mDrawLineHumidity.setLayoutParams(lp);
		rLayout.addView(mDrawLineHumidity);
		mDrawLineHumidity.invalidate();

		mDrawLineLightIntensity = new DrawLineLightIntensity(this);
		mDrawLineLightIntensity.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineHumidity.setDataProcess(mDataProcess);
		mDrawLineLightIntensity.setLayoutParams(lp);
		rLayout.addView(mDrawLineLightIntensity);
		mDrawLineLightIntensity.invalidate();

		mTcpClient = null;

		mBtn = (Button) findViewById(R.id.button1);
		mBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage("Android Client msg... ");
				} else {
					mBtn.setText("NNNN");
				}
			}
		});

		new Thread(new ThreadReceiveData()).start();
		new Thread(new ThreadGetData()).start();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			mTcpClient.stopClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			 mDrawLineTemperature.invalidate();
			 mDrawLineHumidity.invalidate();
			 mDrawLineLightIntensity.invalidate();
//			Log.e("AAA", "thread receive " + msg.what);
		};
	};

	class ThreadReceiveData implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub

			Log.e("AAA", "create tcp.");
			// we create a TCPClient object and
			mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
				@Override
				// here the messageReceived method is implemented
				public void messageReceived(char[] receiveData) {
					try {
						mDataProcess.processData(receiveData);
						mHandler.obtainMessage(3).sendToTarget();
//						Log.e("AAA", "thread receive.............");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			mTcpClient.run();
			if (mTcpClient != null) {
				mTcpClient
						.sendMessage("Initial Message when connected with Socket Server.");
			}
		}
	}

	// 线程类
	class ThreadGetData implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated methd stub
			while (true) {
				try {
					// Thread.sleep(10 * 1000);
					Thread.sleep(3 * 1000);

					if (mTcpClient != null) {
//						Log.e("AAA", "thread get.............");
						MainActivity.this.mTcpClient.sendMessage("get");
					} else {
						// mBtn.setText("NNNN");
						// Log.e("AAA", "send err..." + new Date().toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("thread error...");
				}
			}
		}
	}

}
