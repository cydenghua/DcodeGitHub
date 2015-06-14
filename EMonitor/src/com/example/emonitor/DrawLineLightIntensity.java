package com.example.emonitor;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawLineLightIntensity extends View {
	
	private DrawCoordinateSystem mCoordinateSystem;
	private Paint mPaint;
	private int mCorlor;
	private int mParamOrder;
	
	public DrawLineLightIntensity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineLightIntensity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawLineLightIntensity(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initData();
	}
	
	private void initData() {
		mCorlor = Color.YELLOW;
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mCorlor);
		mPaint.setStrokeWidth(3);
	}
	
	public void setCoordinateSystem(DrawCoordinateSystem aCoordinateSystem) {
		mCoordinateSystem = aCoordinateSystem;	
		mParamOrder = ++aCoordinateSystem.mParamCount;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		 
		mPaint.setTextSize(40);
		String sText = "¡ª¡ª¹âÇ¿";
		int x = canvas.getWidth() - (int)mPaint.measureText(sText);
		int y = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);
		y = y * mParamOrder + 10;
		canvas.drawText(sText, x, y, mPaint);
		
//		canvas.drawLine(mCoordinateSystem.getZeroPoint().x, mCoordinateSystem.getZeroPoint().y, 600, 0, mPaint);
//		canvas.drawLine(0, 0, 666, 766, mPaint);
	}
}
