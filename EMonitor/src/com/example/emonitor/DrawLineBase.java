package com.example.emonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawLineBase extends View {

	protected int mDataType;
	private String mCaption;
	private String mUnitText;
	
	private DrawCoordinateSystem mCoordinateSystem;
	private Paint mPaint;
	private int mParamOrder;

	private DataProcess mDataProcess;

	// private long[][] mDrawData;

	public DrawLineBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineBase(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initData();
	}

	private void initData() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(3);

	}
	
	protected void setDataType(int aType) {
		mDataType = aType;
		if(SystemDefine.DATA_TEMPERATURE == mDataType) {
			mCaption = "！！梁業";
			mUnitText = "≧";
			mPaint.setColor(Color.RED);
		}
		if(SystemDefine.DATA_HUMIDITY == mDataType) {
			mCaption = "！！物業";
			mUnitText = "%";
			mPaint.setColor( Color.rgb(99, 66, 00));
		}
		if(SystemDefine.DATA_LIGHTINTENSITY == mDataType) {
			mCaption = "！！高孚";
			mUnitText = "Lx";
			mPaint.setColor( Color.YELLOW);
		}
	}

	public void setCoordinateSystem(DrawCoordinateSystem aCoordinateSystem) {
		mCoordinateSystem = aCoordinateSystem;
		mParamOrder = ++aCoordinateSystem.mParamCount;
	}

	public void setDataProcess(DataProcess aDataProcess) {
		mDataProcess = aDataProcess;
	}
 

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		mPaint.setTextSize(40);
		
//		String sText = "！！梁業";
		int x2 = 0;
		int y2 = 0;
		int x1 = canvas.getWidth() - (int) mPaint.measureText(mCaption);
		int y1 = (int) (mPaint.getFontMetrics().descent - mPaint
				.getFontMetrics().ascent);
		y1 = y1 * mParamOrder + 10;
		canvas.drawText(mCaption, x1, y1, mPaint);

		if (null == mDataProcess || mDataProcess.mDataList.isEmpty()) {
			return;
		}

		int k = mDataProcess.mDataList.size() - 1;
		int aVal = getDataVal(k);//((SingleData) mDataProcess.mDataList.get(k)).mTemperature;
		Date aTime = ((SingleData) mDataProcess.mDataList.get(k)).mTime; 

		String sText = Integer.toString(aVal) + mUnitText;
		mPaint.setTextSize(120);

		x1 = 50 + (int) mPaint.measureText("0000")*(mParamOrder-1);
		y1 = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);
		canvas.drawText(sText, x1, y1, mPaint);

		x1 = getDrawX(aTime);
		y1 = getDrawY(aVal);
//		doreset
		if (x1 > canvas.getWidth()) {// 
			mCoordinateSystem.resetBeginTime();	
			this.invalidate();
			return;
		}

//		Log.e(SystemDefine.LOG_TAG, "Begin draw ***********************************");
		for (int i = k; i >= 0; i--) {

			aVal = getDataVal(i);// ((SingleData) mDataProcess.mDataList.get(i)).mTemperature;
			aTime = ((SingleData) mDataProcess.mDataList.get(i)).mTime;

			x2 = getDrawX(aTime);
			y2 = getDrawY(aVal);			 

			canvas.drawLine(x1, y1, x2, y2, mPaint);

//			Log.e(SystemDefine.LOG_TAG, "draw time. is " + aTime);
//			Log.e(SystemDefine.LOG_TAG, "draw x1 = " + x1 + " y1 = " + y1 + " x2 =" +x2 + " y2="+y2);

			x1 = x2; 
			y1 = y2;
			
			if(x1<mCoordinateSystem.getZeroPoint().x) {
//				Log.e(SystemDefine.LOG_TAG, "draw break*************************" );
				break;
			}
		} 
	}
	
	protected int getDrawX(Date aTime) {
		int x = (int) (aTime.getTime() - mCoordinateSystem
				.getBeginTime().getTime());
		x = x / 1000;
		x = (int) (x * mCoordinateSystem.getStepX());
		x = x + mCoordinateSystem.getZeroPoint().x;
		return x;
	}
	
	protected int getDrawY(int aVal) {

		int y = (int) (aVal * mCoordinateSystem
				.getStepY());
		y = mCoordinateSystem.getZeroPoint().y - y;
		return y;		
	}
	
	private int getDataVal(int postion) {
		int val = 0;
		
		if(SystemDefine.DATA_TEMPERATURE == mDataType) {
			val = ((SingleData) mDataProcess.mDataList.get(postion)).mTemperature;			
		}
		if(SystemDefine.DATA_HUMIDITY == mDataType) {
			val = ((SingleData) mDataProcess.mDataList.get(postion)).mHumidity;
		}
		if(SystemDefine.DATA_LIGHTINTENSITY == mDataType) {
			val = ((SingleData) mDataProcess.mDataList.get(postion)).mLightIntensity;
		}		
		return val;
	}
}
