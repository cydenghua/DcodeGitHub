package com.example.emonitor;

import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawCoordinateSystem extends View {

	public static final int MODAL_MIN = 0;
	public static final int MODAL_DAY = 1;
	public static final int MODAL_WEEK = 2;
	public static final int MODAL_MONTH = 3;

	public int mParamCount = 0;

	private Paint mPaint;
	private Paint mPaintDotLine;

	private int mModal; // day or week or month

	private int mMarginLeft = 30;
	private int mMarginRight = 10;
	private int mMarginTop = 10;
	private int mMarginBottom = 30;

	private int mZeroX; // 原点x坐标
	private int mZeroY; // 原点y坐标
	private double mStepX; // x方向步长
	private double mStepY; // y方向步长

	private Date mBeginTime;

	public DrawCoordinateSystem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawCoordinateSystem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initData();
	}

	public DrawCoordinateSystem(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-gnerated constructor stub
		initData();
	}

	private void initData() {
		mBeginTime = new Date();	 
		
		setModal(MODAL_MIN);
//		setModal(MODAL_DAY);
//		setModal(MODAL_WEEK);
//		setModal(MODAL_MONTH); 
		
		initBeginTime();
 		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(20);

		mPaintDotLine = new Paint();
		mPaintDotLine.setAntiAlias(true);
		mPaintDotLine.setStyle(Paint.Style.STROKE);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		mPaintDotLine.setPathEffect(effects);

	}

	public void setModal(int aModal) {
		mModal = aModal;
		if (mModal < MODAL_MIN || mModal > MODAL_MONTH) {
			mModal = MODAL_DAY;
		}		
	}

	private void initBeginTime() {

		Date t = new Date();
		if(MODAL_MIN == mModal) {
			mBeginTime.setTime(t.getTime() - 0 * 1000);			
		}
		if(MODAL_DAY == mModal) { 
			mBeginTime.setTime(t.getTime() - 2 * 60 * 60 * 1000);
			mBeginTime.setMinutes(0);
			mBeginTime.setSeconds(0);			
		}
		if(MODAL_WEEK == mModal) {
			
		}
		if(MODAL_MONTH == mModal) {
			
		}
	}
	
	public void resetBeginTime() {
		Date t = new Date();
		if(MODAL_MIN == mModal) {
			mBeginTime.setTime(t.getTime() - 60 * 1000);			
		}
		if(MODAL_DAY == mModal) { 
			mBeginTime.setTime(t.getTime() - 20 * 60 * 60 * 1000);
			mBeginTime.setMinutes(0);
			mBeginTime.setSeconds(0);			
		}
		if(MODAL_WEEK == mModal) {
			
		}
		if(MODAL_MONTH == mModal) {
			
		}
		invalidate();
	}
	
	private void setStep(Canvas canvas) {

		double second = 0;
		if (mModal == MODAL_MIN) {
			second = 60*2;
		}
		if (mModal == MODAL_DAY) {
			second = 24 * 60 * 60;
		}
		if (mModal == MODAL_WEEK) {
			second = 7 * 24 * 60 * 60;
		}
		if (mModal == MODAL_MONTH) {
			second = 31 * 24 * 60 * 60;
		}

		mStepX = (canvas.getWidth() - mMarginLeft - mMarginRight) / second;
		mStepY = (canvas.getHeight() - mMarginBottom - mMarginTop) / 100.0;
	}

	private void drawCoordinateSystem(Canvas canvas) {

		String sTmp = "";
		int k = 0, t = 0, m = 0;
		int x1, y1, x2, y2;
		mZeroX = mMarginLeft;
		mZeroY = canvas.getHeight() - mMarginBottom;

		mPaint.setStrokeWidth((float) 3.0); // 设置线宽

		// draw x
		x1 = canvas.getWidth() - mMarginRight;
		y1 = mZeroY;
		canvas.drawLine(mZeroX, mZeroY, x1, y1, mPaint);

		x2 = x1 - 10;
		y2 = y1 + 5;
		canvas.drawLine(x1, y1, x2, y2, mPaint);

		x2 = x1 - 10;
		y2 = y1 - 5;
		canvas.drawLine(x1, y1, x2, y2, mPaint);

		// draw y
		x1 = mZeroX;
		y1 = mMarginTop;
		canvas.drawLine(mZeroX, mZeroY, x1, y1, mPaint);

		x2 = x1 - 5;
		y2 = y1 + 10;
		canvas.drawLine(x1, y1, x2, y2, mPaint);

		x2 = x1 + 5;
		y2 = y1 + 10;
		canvas.drawLine(x1, y1, x2, y2, mPaint);

		mPaint.setStrokeWidth((float) 1.0); // 设置线宽

		// y 0-100 --> 20,40,60,80
		for (int i = 1; i < 5; i++) {
			k = 20 * i;
			x1 = mZeroX;
			y1 = mZeroY - (int) mStepY * k;
			canvas.drawLine(x1, y1, x1 + 15, y1, mPaint);

			// draw text
			sTmp = Integer.toString(k);
			t = (int) mPaint.measureText(sTmp);
			m = (int) (mPaint.getFontMetrics().descent - mPaint
					.getFontMetrics().ascent);
			m /= 2;
			canvas.drawText(sTmp, x1 - t, y1 + m, mPaint);

			// draw dot line
			Path path = new Path();
			path.moveTo(x1, y1);
			path.lineTo(canvas.getWidth() - mMarginRight, y1);
			canvas.drawPath(path, mPaintDotLine);
		}

		if (MODAL_MIN == mModal) {
			k = 20; // 1 step hour 20 sec
			t = 6*k; // total 120 sec
		}
		if (MODAL_DAY == mModal) {
			k = 3*60*60;  // 1 step sec
			t = k*8;
		}
		if (MODAL_WEEK == mModal) {
			k = 24*60*60;
			t = k*7;
		}
		if (MODAL_MONTH == mModal) {
			k = 3*24*60*60;
			t = k*10; 
		}
		
		for (int j = 1; j < t/k; j++) {
			x1 = mZeroX + (int) (mStepX * k*j );
			y1 = mZeroY;
			canvas.drawLine(x1, y1, x1, y1 - 15, mPaint);

			if (MODAL_MIN == mModal) {
				sTmp = k*j + "秒";
			}
			if (MODAL_DAY == mModal) {
				Date dt = new Date(mBeginTime.getTime() + 1000*j*k);
				sTmp =	dt.getDate()+"日"  + dt.getHours() + "点"; //sText =	(dt.getMonth() + 1) + "月" + dt.getDate()+"日"  + dt.getHours() + "点";				 
			}
			if(MODAL_WEEK == mModal) {
			//todo.					
			}
			if(MODAL_MONTH == mModal) {
			//todo.					
			}
						
			// draw text
			x2 = x1 - (int) mPaint.measureText(sTmp);// mCanvas->TextWidth(sTmp)/2;
			y2 = y1
					+ (int) (mPaint.getFontMetrics().descent - mPaint
							.getFontMetrics().ascent);
			canvas.drawText(sTmp, x2, y2, mPaint);
			 			
			// draw dot line
			Path path = new Path();
			path.moveTo(x1, y1);
			path.lineTo(x1, mMarginTop);
			canvas.drawPath(path, mPaintDotLine);
			
		}
	

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		setStep(canvas);
		drawCoordinateSystem(canvas);
		// Paint paint = new Paint();
		// paint.setStyle(Paint.Style.STROKE);
		// // paint.setColor(Color.BLUE);
		// Path path = new Path();
		// path.moveTo(0, 10);
		// path.lineTo(480, 10);
		// PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
		// 1);
		// paint.setPathEffect(effects);
		// canvas.drawPath(path, paint);
		
//		canvas.drawLine(500, 500, 800, 800, mPaint);

	}

	public Point getZeroPoint() {
		return new Point(mZeroX, mZeroY);
	}

	public double getStepX() {
		return mStepX;
	}

	public double getStepY() {
		return mStepY;
	}

	public Date getBeginTime() {
		return mBeginTime;
	}

}
