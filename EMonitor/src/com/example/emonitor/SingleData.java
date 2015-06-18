package com.example.emonitor;

import java.util.Date;

public class SingleData {

	public Date mTime;
	public int mTemperature;
	public int mHumidity;
	public int mLightIntensity;
	
	public SingleData() {
		// TODO Auto-generated constructor stub.
		mTime = new Date();
		mTemperature = SystemDefine.INVALID_DATA;
		mHumidity = SystemDefine.INVALID_DATA;;
		mLightIntensity = SystemDefine.INVALID_DATA;;
	}

}
