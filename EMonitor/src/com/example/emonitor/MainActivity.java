package com.example.emonitor;

import java.util.Date;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class MainActivity extends ActionBarActivity {

	private DrawCoordinateSystem mDrawCoordinateSystem;
	private DrawLineTemperature mDrawLineTemperature;
	private DrawLineHumidity mDrawLineHumidity;
	private DrawLineLightIntensity mDrawLineLightIntensity;
	private DataProcess mDataProcess;

	private TCPClient mTcpClient = null;
	private connectTask conctTask = null;
	
	private int mCount = 0;

	private Button mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏.
		setContentView(R.layout.activity_main);

		// 添加曲线视图
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW, R.id.button_line);
		final RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.main_activity_layout);

		mDrawCoordinateSystem = new DrawCoordinateSystem(this);
		mDrawCoordinateSystem.setLayoutParams(lp);
		rLayout.addView(mDrawCoordinateSystem);
		mDrawCoordinateSystem.invalidate();

		mDrawLineTemperature = new DrawLineTemperature(this);
		mDrawLineTemperature.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineTemperature.setLayoutParams(lp);
		rLayout.addView(mDrawLineTemperature);
		mDrawLineTemperature.invalidate();

		mDrawLineHumidity = new DrawLineHumidity(this);
		mDrawLineHumidity.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineHumidity.setLayoutParams(lp);
		rLayout.addView(mDrawLineHumidity);
		mDrawLineHumidity.invalidate();

		mDrawLineLightIntensity = new DrawLineLightIntensity(this);
		mDrawLineLightIntensity.setCoordinateSystem(mDrawCoordinateSystem);
		mDrawLineLightIntensity.setLayoutParams(lp);
		rLayout.addView(mDrawLineLightIntensity);
		mDrawLineLightIntensity.invalidate();

		mDataProcess = new DataProcess();
		mDataProcess.setDrawTemperature(mDrawLineTemperature);
		mDataProcess.setDrawLight(mDrawLineLightIntensity);
		mDataProcess.setDrawHumidity(mDrawLineHumidity);

		mTcpClient = null;
		// connect to the server.
		conctTask = new connectTask();
		conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

		new Thread(new ThreadShow()).start();
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
			conctTask.cancel(true);
			conctTask = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	/**
	 * @author Prashant Adesara receive the message from server with asyncTask
	 * */
	public class connectTask extends AsyncTask<char[], char[], TCPClient> {

		@Override
		protected TCPClient doInBackground(char[]... receiveData) {
			Log.e("AAA", "create tcp.");
			// we create a TCPClient object and
			mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
				@Override
				// here the messageReceived method is implemented
				public void messageReceived(char[] receiveData) {
					try {
						// this method calls the onProgressUpdate
						publishProgress(receiveData);
						// if(message!=null)
						// {
						// System.out.println("Return Message from Socket::::: >>>>> "+message);
						// }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			mTcpClient.run();
			if (mTcpClient != null) {
				mTcpClient
						.sendMessage("Initial Message when connected with Socket Server");
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(char[]... receiveData) {
			super.onProgressUpdate(receiveData);

			Log.e("AAA", "receive msg, len = " + receiveData.length);
			for (int i = 0; i < receiveData.length; i++) {
				// String s = new String(receiveData[i]);
				// Log.e("AAA", "receive, " + s);
				if(0==mCount%6) {
					mDataProcess.processData(receiveData[i]);
					mCount=0;

					 Log.e("AAA", "receive and proccess********************* " );
				}
				mCount++;
				// mTextView.setText(s);

			}

			// in the arrayList we add the messaged received from server.
			// arrayList.add(values[0]);

			// notify the adapter that the data set has changed. This means that
			// new message received
			// from server was added to the list
			// mAdapter.notifyDataSetChanged();
		}
	}

	// 线程类
	class ThreadShow implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated methd stub
			while (true) {
				try {
//					Thread.sleep(10 * 1000);
					Thread.sleep(300);
					

					if (mTcpClient != null) {
						// Log.e("AAA", "thread get............." );
						MainActivity.this.mTcpClient.sendMessage("get");
					} else {
						// mBtn.setText("NNNN");
						Log.e("AAA", "send err..." + new Date().toString());
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
