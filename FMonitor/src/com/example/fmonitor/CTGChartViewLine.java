package com.example.fmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CTGChartViewLine extends View {

	private BedDocument mBedDoc;
	private CTGChartDraw mDrawDocument;

	private final int mDragCountMAX = 30;
	private int mDragXDown;
	private int mDragXUp;
	private int mDragOffset = 0;
	private int mDragCount = mDragCountMAX;

	public CTGChartViewLine(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public void setDocumentDraw(CTGChartDraw drawDocument) {
		mDrawDocument = drawDocument;
	}

	public String getBedNo() {
		if (null != mBedDoc) {
			return mBedDoc.getBedNo();
		}
		return "";
	}

	public void drawBedDoc(BedDocument bedDoc) {
		mDragOffset = 0;
		mDragCount = 0;
		mBedDoc = bedDoc;
		this.refreshData();
	}

	public void refreshData() {
		// Log.e(SRDefine.LOG_TAG, "repaint ctg ........." + getBedNo());
		if (mDragCount > 0) { // 发送了拖动， dragcount个周期内不实时画点.
			mDragCount--;
		} else {
//			this.invalidate();
			mDragOffset = 0;
		}

		this.invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			mDragXDown = (int) event.getX(0);
		}
		if (MotionEvent.ACTION_UP == event.getAction()) {
			mDragXUp = (int) event.getX(0);
			mDragOffset += mDragXDown - mDragXUp;
			mDragCount = mDragCountMAX;
			invalidate();
		}
		return true;
	}
	
	public void onFlingRefresh(int offset) {
		mDragOffset += offset;
		mDragCount = mDragCountMAX;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		if (null != mDrawDocument) {
			if (mDragOffset != 0) {
				mDrawDocument.drawPaper(canvas, mBedDoc, mDragOffset);
			} else {
				mDrawDocument.drawPaper(canvas, mBedDoc);
			}			
		}

	}
}
