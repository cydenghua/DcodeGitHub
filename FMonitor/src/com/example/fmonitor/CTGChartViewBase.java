package com.example.fmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;

public class CTGChartViewBase extends View {

	private CTGChartDraw mDrawDocument;
	
	public CTGChartViewBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CTGChartViewBase(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public void setDocumentDraw(CTGChartDraw drawDocument) {
		mDrawDocument = drawDocument;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (null != mDrawDocument) {
			mDrawDocument.drawPaperBase(canvas);	
		}
	}

}
