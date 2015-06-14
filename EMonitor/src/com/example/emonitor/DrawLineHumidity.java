package com.example.emonitor;

import java.util.Date;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawLineHumidity extends View {

	private DrawCoordinateSystem mCoordinateSystem;
	private Paint mPaint;
	private int mCorlor;
	private int mParamOrder;

	private int mDataLen = 0;
	private int[][] mDrawData;
	
	public DrawLineHumidity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineHumidity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineHumidity(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initData();
	}
	
	protected void initData() {
		mCorlor = Color.rgb(99, 66, 00);//Color.LTGRAY;
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mCorlor);
		mPaint.setStrokeWidth(3);

		mDrawData = new int[1000][2];
	}
	
	public void setCoordinateSystem(DrawCoordinateSystem aCoordinateSystem) {
		mCoordinateSystem = aCoordinateSystem;	
		mParamOrder = ++aCoordinateSystem.mParamCount;
	}

	public void addData(Date aTime, int aVal) {
		int t = (int)(aTime.getTime() - mCoordinateSystem.getBeginTime().getTime());
		t = t/1000;
		t = (int)(t*mCoordinateSystem.getStepX());
		t = t + mCoordinateSystem.getZeroPoint().x;
		mDrawData[mDataLen][0] = t;		
		mDrawData[mDataLen][1] = aVal;
		
		mDataLen++;
		
		this.invalidate();
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		 
		mPaint.setTextSize(40);
		String sText = "！！物業";
		int x2, y2;
		int x = canvas.getWidth() - (int)mPaint.measureText(sText);
		int y = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);
		y = y * mParamOrder + 10;
		canvas.drawText(sText, x, y, mPaint);
		
//		canvas.drawLine(mCoordinateSystem.getZeroPoint().x, mCoordinateSystem.getZeroPoint().y, 990, 0, mPaint);
//		canvas.drawLine(0, 0, 666, 966, mPaint);

		if(mDataLen<1) {
			return;
		}
		
		x = mDrawData[0][0];		
		y = mDrawData[0][1];
		y = (int)(y * mCoordinateSystem.getStepY());
		y = mCoordinateSystem.getZeroPoint().y - y;
		for (int i = 0; i < mDataLen; i++) {
			x2 = mDrawData[i][0];
			y2 = mDrawData[i][1];
			y2 = (int)(y2 * mCoordinateSystem.getStepY());
			y2 = mCoordinateSystem.getZeroPoint().y - y2;
			canvas.drawLine(x, y, x2, y2, mPaint);
			x = x2;
			y = y2;
		}
		
		
		y = mDrawData[mDataLen-1][1];
		sText = Integer.toString(y) +  "%";
		mPaint.setTextSize(160);

		x = 150 + (int)mPaint.measureText(sText);
		y = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);
		canvas.drawText(sText, x, y, mPaint);
		
		Log.e("AAAA.", "Draw Humidity  " + mDataLen);
//		Log.e("AAAA.", "width =" + canvas.getWidth() + "h="+ canvas.getHeight());

	}
}
