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

public class DrawLineTemperature extends DrawLineBase {
 
	// private long[][] mDrawData;

	public DrawLineTemperature(Context context) {
		super(context);
		// TODO Auto-generated constructor stub 
		super.setDataType(SystemDefine.DATA_TEMPERATURE);
	}

	public DrawLineTemperature(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub 
		super.setDataType(SystemDefine.DATA_TEMPERATURE);
	}

	public DrawLineTemperature(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub 
		super.setDataType(SystemDefine.DATA_TEMPERATURE);
	}
 
 }
