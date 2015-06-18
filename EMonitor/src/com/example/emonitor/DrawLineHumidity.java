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

public class DrawLineHumidity extends DrawLineBase {
	
	public DrawLineHumidity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_HUMIDITY);
	}

	public DrawLineHumidity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_HUMIDITY);
	}

	public DrawLineHumidity(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_HUMIDITY);
	}

}
