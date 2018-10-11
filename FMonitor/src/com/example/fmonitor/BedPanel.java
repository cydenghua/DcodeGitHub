package com.example.fmonitor;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BedPanel extends LinearLayout {

	private String mBedNo = "";
	private TextView mTvBedNo;
	private TextView mTvFhrValue;
	private TextView mTvUC;
	private TextView mTvPatientName;
	private BedDocument mBedDoc = null;

	public BedPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.bedpanel_layout, this);
		initData();
	}

	public BedPanel(Context context, String bedNo) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.bedpanel_layout, this);
		initData();
		setBedNo(bedNo);
	}

	public BedPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.bedpanel_layout, this);
		initData();
	}

	private void initData() {
		mTvBedNo = (TextView) findViewById(R.id.textView2);
		mTvFhrValue = (TextView) findViewById(R.id.textView4);
		mTvUC = (TextView) findViewById(R.id.textView6);
		mTvPatientName = (TextView) findViewById(R.id.textView3);
		updateBedPatient("---");
	}

	public void setBedDocument(BedDocument bedDoc) {
		mBedDoc = bedDoc;
	}

	public void setBedNo(String bedNo) {
		mBedNo = bedNo;
		mTvBedNo.setText(bedNo);
	}

	public String getBedNo() {
		return mBedNo;
	}

	public void updateDisplay() {
		String tmpStr = "";
		int postion = mBedDoc.mDocWritePos-1;
		if (0 == mBedDoc.mFetalModal) {
			tmpStr = tmpStr + (mBedDoc.mFHR1[postion]&0xFF);
		}
		if (1 == mBedDoc.mFetalModal) {
			tmpStr = tmpStr + (mBedDoc.mFHR2[postion]&0xFF);
		}
		if (2 == mBedDoc.mFetalModal) {
			tmpStr = tmpStr + (mBedDoc.mFHR1[postion]&0xFF) + " "
					+ (mBedDoc.mFHR2[postion]&0xFF);
		}
		mTvFhrValue.setText(tmpStr);
		
		mTvUC.setText(" " + (mBedDoc.mUC[postion]&0xFF));
		
//		 Log.e(SystemDefine.LOG_TAG, "update bedpanel fhr modal" + mBedDoc.mFetalModal);
//		 Log.e(SystemDefine.LOG_TAG, "update bedpanel fhr1 single " + mBedDoc.mFHR1_SINGLE);
//		 Log.e(SystemDefine.LOG_TAG, "update bedpanel display" + tmpStr);

	}

	public void updateBedData(BedPacketData bedData) {
		String sTmp = "";
		if (bedData.getFetalNum() > 0) {
			sTmp += bedData.getFhr1();
			if (bedData.getFetalNum() > 1) {
				sTmp += " " + bedData.getFhr2();
			}
			if (bedData.getFetalNum() > 2) {
				sTmp += " " + bedData.getFhr3();
			}
			mTvFhrValue.setText(sTmp);
			mTvUC.setText(" " + bedData.getUC());
		} else {
			mTvFhrValue.setText(sTmp);
			mTvUC.setText(sTmp);
			mTvPatientName.setText("---");
		}
	}

	public void updateBedPatient(String patientName) {
		mTvPatientName.setText(patientName);
	}

	private int mWarnColor = 0;

	public void updateWarnColor(int warnLevel) {
		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.bli_rl_up);
		mWarnColor++;
		if (mWarnColor > 7) {
			mWarnColor = 0;
		}

		if (3 == warnLevel) {
			if (0 == mWarnColor || 2 == mWarnColor || 4 == mWarnColor
					|| 6 == mWarnColor) {
				rl.setBackgroundColor(Color.RED);
			} else {
				rl.setBackgroundColor(Color.WHITE);
			}
		}

		if (2 == warnLevel) {
			if (0 == mWarnColor) {
				rl.setBackgroundColor(Color.YELLOW);
			}
			if (4 == mWarnColor) {
				rl.setBackgroundColor(Color.WHITE);
			}
		}

		if (1 == warnLevel) {
			rl.setBackgroundColor(Color.YELLOW);
		}

		if (0 == warnLevel) {
			rl.setBackgroundColor(Color.WHITE);
		}

	}

}
