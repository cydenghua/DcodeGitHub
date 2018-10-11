package com.example.fmonitor;

import java.net.DatagramPacket;
import java.util.HashMap;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BedDocumentList {
	private Activity sParent = null;
	private Handler mMainMsgHandler = null;
	private HashMap<String, BedDocument> mBedDocs;

	public BedDocumentList(Activity parentActivity) {
		sParent = parentActivity;
		mBedDocs = new HashMap<String, BedDocument>();
	}
	
	public void setMainMsgHandler(Handler handler) {
		mMainMsgHandler = handler;
	}

	public BedDocument getBedDocument(String bedNo) {
		addBedDocuemnt(bedNo);
		return mBedDocs.get(bedNo);
	}

	public void addBedDocuemnt(String bedNo) {
		if (null != mBedDocs.get(bedNo)) {
			return;
		}
//		Log.e(SystemDefine.LOG_TAG, "add bed doc " + bedNo);
		BedDocument bedDoc = new BedDocument(bedNo);
		mBedDocs.put(bedNo, bedDoc);
		if( null != mMainMsgHandler ){
			Message msg = mMainMsgHandler.obtainMessage(SystemDefine.MESSAGE_ADD_BED);
			msg.obj = bedNo;
			mMainMsgHandler.sendMessage(msg);
		}
	}

	private String getPacketBedNo(DatagramPacket packet) {
		// 读取4个字节的机器编号
		String bedNoStr = "";
//		bedNoStr = bedNoStr + Integer.toHexString(packet.getData()[10]&0xFF);
//		bedNoStr = bedNoStr + Integer.toHexString(packet.getData()[11]&0xFF);
		bedNoStr = bedNoStr + Integer.toHexString(packet.getData()[12]&0xFF);
		bedNoStr = bedNoStr + Integer.toHexString(packet.getData()[13]&0xFF);
		return bedNoStr;		
	}
	
	public void receivePacketData(DatagramPacket packet) {

		if (SystemDefine.PACKET_FETAL_DATA == packet.getData()[9]) {
			// 读取4个字节的机器编号
			String bedNoStr = getPacketBedNo(packet);
			getBedDocument(bedNoStr).receivePacketData(packet);
			
//			Log.e(SystemDefine.LOG_TAG, "add bed doc " + bedNoStr);
//			Log.e(SystemDefine.LOG_TAG, "add bed doc " + Integer.toHexString(packet.getData()[10])+ Integer.toHexString(packet.getData()[11])+ Integer.toHexString(packet.getData()[12])+ Integer.toHexString(packet.getData()[13])  );
			if( null != mMainMsgHandler ){
				Message msg = mMainMsgHandler.obtainMessage(SystemDefine.MESSAGE_REFRESH_DATA);
				msg.obj = bedNoStr;
				mMainMsgHandler.sendMessage(msg);
			}
		}
	}

//	public void receiveBedOnLineData(DatagramPacket packet) {
//		String bedNoStr = getPacketBedNo(packet);
//		getBedDocument(bedNoStr).setBedOnline(true);
//	}

	public void receiveOnLineData(DatagramPacket packet) {
		//stop detect 
		if( null != mMainMsgHandler ){
			Message msg = mMainMsgHandler.obtainMessage(SystemDefine.MESSAGE_STOP_DETECT);			
			mMainMsgHandler.sendMessage(msg);
		}
	}
	
	public void receiveBedOffLineData(DatagramPacket packet) {
//		String bedNoStr = getPacketBedNo(packet);
//		getBedDocument(bedNoStr).setBedOnline(false);
	}
	


}

// String sLog = "";
// int k;
// for (int i = 0; i < bData.length; i++) {
// k = bData[i] & 0xFF;
// sLog += Integer.toHexString(k) + "-";
// }
// Log.e(SRDefine.LOG_TAG, sLog);