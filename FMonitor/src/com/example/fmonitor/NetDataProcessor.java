package com.example.fmonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
 
import android.util.Log;
import android.widget.Toast;

public class NetDataProcessor {

	private CRC16 mCRC16 = null;
	private boolean mHaveOnlie = false;
//	private DatagramSocket mServerSocket = null;
	private BedDocumentList mBedDocList = null;
 
	public NetDataProcessor() {
		// TODO Auto-generated constructor stub
		mCRC16 = new CRC16(); 
//		this.mServerSocket = serverSocket;
	}
 
	public void setBedDocList(BedDocumentList bedDocList) {
		mBedDocList = bedDocList;		
	}
	 
	public void processorData(DatagramPacket packet) {
		// Log.i(SystemDefine.LOG_TAG, "Net Data processor from " +
		// packet.getAddress()
		// + " with contents: " + packet.getData());
		if(null == packet){
			return;
		}
		Log.e(SystemDefine.LOG_TAG, "receive msg flag:  " + packet.getData()[9] );

		if (checkPacketErr(packet.getData())) {
			return;
		}
		switch (packet.getData()[9]) {
		case SystemDefine.PACKET_DETECT:
			processPacketDetect(packet);
			break;
		case SystemDefine.PACKET_ONLINE:
			processPacketOnline(packet);
			break;
		case SystemDefine.PACKET_OFFLINE:
			processPacketOffline(packet);
			break;
		case SystemDefine.PACKET_FETAL_DATA:
			processPacketFetalData(packet);
			break;

		default:
			break;
		}
	}

	private boolean checkPacketErr(byte[] pData) {
		// check head
		if (SystemDefine.PACKET_HEAD1 != pData[0]
				|| SystemDefine.PACKET_HEAD2 != pData[1]) {
			Log.e(SystemDefine.LOG_TAG, "Packet head err...........");
			return true;
		}
		// check tail
		int packetLen = pData[2] & 0xFF + (pData[3] & 0xFF) * 255;
		if (SystemDefine.PACKET_TAIL != pData[packetLen - 1]) {
			Log.e(SystemDefine.LOG_TAG, "Packet tail err...........");
			return true;
		}
		// check crc
		byte[] cData = new byte[packetLen];
		System.arraycopy(pData, 8, cData, 0, packetLen - 11);
		short s = mCRC16.getCrc(cData);
		byte[] bcrc = mCRC16.short2bytes(s);
//		Log.e(SystemDefine.LOG_TAG, "crc shrot is  " + (bcrc[0] & 0xFF));
		// Log.e(SystemDefine.LOG_TAG, "crc shrot is  " + bcrc[1];

		return false;
	}

	private void processPacketDetect(DatagramPacket pData) {		
		if(mHaveOnlie) {  //已经注册成功， 不再注册
			return;			
		}			
		//收到嗅探回复包， 发送注册信息
		if(0x02 != pData.getData()[8]) {
			return;
		}	
 
		byte[] data = new byte[0];
		byte[] bOnLineData = DataPacketCreate.getInstance().getDataPacket(SystemDefine.PACKET_ONLINE, (byte)0x01, data); 
		 Log.e(SystemDefine.LOG_TAG, "Send online data..........") ;		
		 sendMsgToServer(bOnLineData);
	}

	private void processPacketOnline(DatagramPacket pData) {
		 Log.e(SystemDefine.LOG_TAG, "receive online data..........") ;
		 //设置标记收到嗅探返回包后不再发起注册
		 mHaveOnlie = true;
//		mBedDocList.receiveOnLineData(pData);		
	}
	
	private void processPacketOffline(DatagramPacket pData) {
//		byte[] bBack = new byte[18];// pData.getData();
//		sendMsgToServer(bBack);
		mBedDocList.receiveBedOffLineData(pData);		
	}

	private void processPacketFetalData(DatagramPacket pData) {
	/*
		byte[] bBack = new byte[18];// pData.getData();
		System.arraycopy(pData.getData(), 0, bBack, 0, 18);
//		Log.e(SystemDefine.LOG_TAG, "receive fetal data index is " + pData.getData()[4] );
		//55AA1100AC 000000022E00D594 27BDB9394700DF07 0700040002001100 1A0036
		bBack[2] = 0x12; //len
		bBack[3] = 0x00;
		bBack[8] = 0x02;
		// todo alter crc code
		bBack[17] = SystemDefine.PACKET_TAIL;
		sendMsgToServer(bBack);
*/		
		
		Log.e(SystemDefine.LOG_TAG, "***************************receive fetal data index is ");
		byte[] data = new byte[0];
		byte[] bBack = DataPacketCreate.getInstance().getDataPacket(SystemDefine.PACKET_FETAL_DATA, (byte)0x02, data);
		sendMsgToServer(bBack);
		
		//process data 
		mBedDocList.receivePacketData(pData);		
	}
 
	private void sendMsgToServer(byte[] bData) {
		NetSocketUDP.getInstance().sendDataArray(bData);
//		DatagramPacket p = new DatagramPacket(bData, bData.length, mServerAddress, mServerPort);
//		try {
//			mServerSocket.send(p);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
 
	}


}
