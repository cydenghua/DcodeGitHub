package com.example.fmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CTGChartViewValue extends View {

	private BedDocument mBedDoc;
	private CTGChartDraw mDrawDocument;
	
	public CTGChartViewValue(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewValue(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewValue(Context context, AttributeSet attrs,
			int defStyleAttr) {
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

	public void drawBedDocValue(BedDocument bedDoc) {
		mBedDoc = bedDoc;
		this.invalidate();
	}

	public void refreshData() {
		this.invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(null != mDrawDocument) {
			mDrawDocument.drawCTGValue(canvas, mBedDoc);
		}
	}

}
