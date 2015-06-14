package com.example.emonitor;

import java.util.Date;

import android.util.Log;

public class DataProcess {

	private DrawLineTemperature mDrawTemperature;
	private DrawLineLightIntensity mDrawLight;
	private DrawLineHumidity mDrawHumidity;
	
	public DataProcess() {
		// TODO Auto-generated constructor stub
	}

	public void setDrawTemperature(DrawLineTemperature aDrawTemperature) {
		mDrawTemperature = aDrawTemperature;
	}
	public void setDrawLight(DrawLineLightIntensity aDrawLight) {
		mDrawLight = aDrawLight;
	}
	public void setDrawHumidity(DrawLineHumidity aDrawHumidity) {
		mDrawHumidity = aDrawHumidity;
	}
	
	public void processData(char[] receiveData) {
		
//		Log.e("AAA", "len = "+ receiveData.length +"process  " + new String(receiveData) );
//receive, fdfdfdfd 13 01 02 00 00 01 02 01 02 00 21 02 02 00 3b 0d
		//check title fdfdfdfd  todo..
		
		int iData[] = new int[100];
		for (int i = 0; i < receiveData.length; i+=2) {
			String sData = new String(receiveData, i, 2);
			iData[i/2] = Integer.parseInt(sData, 16);
			Log.e("AAA", " data i =" + i/2 +" "+ sData + " int val = " + iData[i/2]);		
		}
		
		int k = 10;
		int pType;
		int pLen;
		int pVal, pVal1, pVal2;
		int iParamCount = iData[k++];
		for (int i = 0; i < iParamCount; i++) {
			pType = iData[k++];  //参数类型
			pLen = iData[k++];  //参数长度
			pVal1 = iData[k++]*256;  //参数val1
			pVal2 = iData[k++];  //参数val2
			pVal = pVal1*256 + pVal2;

			//temperature
			if(0x01 == pType) {
				Log.e("AAA", "temperature is " + pVal);	
				mDrawTemperature.addData(new Date(), pVal);
			}
			//humidity
			if(0x02 == pType) {
				Log.e("AAA", "humidity is " + pVal);	
				mDrawHumidity.addData(new Date(), pVal);
			}
			//light
			if(0x03 == pType) {
				Log.e("AAA", "light is " + pVal);				
			}			
		}
	}

}
