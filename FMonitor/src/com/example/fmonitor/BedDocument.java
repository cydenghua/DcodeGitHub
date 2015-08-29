package com.example.fmonitor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

public class BedDocument {

	private String mBedNo;
	private boolean mOnline = false;
	
	public boolean mStart;
	public Time mTimeStart;
	public int mFetalModal;
	public int mFHR1_SINGLE;
	public int mFHR2_SINGLE;
	public int mFHR3_SINGLE;
	public int mUC_SINGLE;
	public int mTD_SINGLE;

	public int mDocWritePos;
	public byte[] mFHR1;
	public byte[] mFHR2;
	public byte[] mFHR3;
	public byte[] mUC;
	public byte[] mTD;


	public BedDocument(String bedNo) {
		mBedNo = bedNo;
		mFHR1 = new byte[SystemDefine.MAX_FILE_BUFF_LENGTH];
		mFHR2 = new byte[SystemDefine.MAX_FILE_BUFF_LENGTH];
		mFHR3 = new byte[SystemDefine.MAX_FILE_BUFF_LENGTH];
		mUC = new byte[SystemDefine.MAX_FILE_BUFF_LENGTH];
		mTD = new byte[SystemDefine.MAX_FILE_BUFF_LENGTH];
		mTimeStart = new Time();
		mTimeStart.setToNow();
	}

	public String getBedNo() {
		return mBedNo;
	}
	
	public void setBedOnline(boolean onLine) {
		mOnline = onLine;		
	}
	
	public void receivePacketData(DatagramPacket packet){
//		if(!mOnline) {
//			return;		//未注册，不处理	
//		}

		if (!mStart) {
			this.startRecord();
		}

		zeroSingleData();
		
		byte[] pData = packet.getData();
		mFHR1_SINGLE = pData[14]&0xFF;//fhr1
		mFHR2_SINGLE = 0xFF&pData[15];//fhr2
		mUC_SINGLE = 0xFF&pData[16];//toco宫缩压 (有效数据0-100)
//		pData[17];//afm 自动胎动曲线 (有效数据0-40)  
//		pData[18];//fm胎动次数 (有效数据0-255)   
		mFetalModal = pData[19];//胎监模式 (0:单胎监FHR1 1:单胎监FHR2 3:双胎监FHR1&FHR2)     
//		pData[20];//fm_count_mode;	//胎监计数模式 (自动&手动、手动)   
//		pData[21]; //胎心率报警高限(32-240) 
//		pData[22]; //胎心率报警低限(30-238)
//		pData[23]; //信号质量(bit0-1:FHR1 bit2-3:FHR2,3:信号质量好 2:信号质量较好 1-0:信号质量差)   
//		pData[24];  //医生标记事件
//		pData[25]; //错误代码

		saveDataToArray(); 

	}
 
	private void saveDataToArray() {
		mFHR1[mDocWritePos] = (byte) mFHR1_SINGLE;
		mFHR2[mDocWritePos] = (byte) mFHR2_SINGLE;
		mFHR3[mDocWritePos] = (byte) mFHR3_SINGLE;
		mUC[mDocWritePos] = (byte) mUC_SINGLE;
		mTD[mDocWritePos] = (byte) mTD_SINGLE;
		if (mDocWritePos < SystemDefine.MAX_FILE_BUFF_LENGTH) {
			mDocWritePos++;
		}
	}

	private void zeroSingleData() {
		mFHR1_SINGLE = 0;
		mFHR2_SINGLE = 0;
		mFHR3_SINGLE = 0;
		mUC_SINGLE = 0;
		mTD_SINGLE = 0;
	}

	public void startRecord() {
		mStart = true;
		mDocWritePos = 0;
		mTimeStart.setToNow();
	}

	public void overRecord() {
		mStart = false;
	}


}
