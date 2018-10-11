package com.example.emonitor;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawLineLightIntensity extends DrawLineBase {
	 
	public DrawLineLightIntensity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_LIGHTINTENSITY);
	}

	public DrawLineLightIntensity(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_LIGHTINTENSITY);
	}

	public DrawLineLightIntensity(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		super.setDataType(SystemDefine.DATA_LIGHTINTENSITY);
	}
	}
