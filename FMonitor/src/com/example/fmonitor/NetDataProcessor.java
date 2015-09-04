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
	private DatagramSocket mServerSocket = null;
	private BedDocumentList mBedDocList = null;

	private InetAddress mServerAddress = null;
	private int mServerPort = 2013;
	
	public NetDataProcessor(DatagramSocket serverSocket) {
		// TODO Auto-generated constructor stub
		mCRC16 = new CRC16(); 
		this.mServerSocket = serverSocket;
	}

	public void setmServerAddress(InetAddress serverAddress) {
		this.mServerAddress = serverAddress;
	}

	public void setmServerPort(int serverPort) {
		this.mServerPort = serverPort;
	}
	
	public void setBedDocList(BedDocumentList bedDocList) {
		mBedDocList = bedDocList;		
	}
	 
	public void processorData(DatagramPacket packet) {

		// Log.i(SystemDefine.LOG_TAG, "Net Data processor from " +
		// packet.getAddress()
		// + " with contents: " + packet.getData());
		

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
		// TODO Auto-generated method stub
		//收到嗅探回复包， 发送注册信息
		if(0x02 != pData.getData()[8]) {
			return;
		}	

		int k = 0;
		byte[] bOnLineData = new byte[18];
		// 2个标志
		bOnLineData[k++] = SystemDefine.PACKET_HEAD1;// 0x55;
		bOnLineData[k++] = SystemDefine.PACKET_HEAD2;// (byte)0xAA;
		// 2字节包长
		bOnLineData[k++] = 0x12;
		bOnLineData[k++] = 0x00;
		// 4字节包序号
		bOnLineData[k++] = 0x00; // mPacketIndex;
		bOnLineData[k++] = 0x00;
		bOnLineData[k++] = 0x00;
		bOnLineData[k++] = 0x00;
		// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
		bOnLineData[k++] = 0x01;
		// 1字节包类型
		bOnLineData[k++] = SystemDefine.PACKET_ONLINE; // 设备 注册
		bOnLineData[k++] = 0x00; // 设备 注册
		// 4字节机器编号
		bOnLineData[k++] = 0x01;
		bOnLineData[k++] = 0x01;
		bOnLineData[k++] = 0x01;
		bOnLineData[k++] = 0x01;
		// N字节数据

		// 2字节，校验码
		bOnLineData[k++] = 0x01;
		bOnLineData[k++] = 0x01;
		// 1字节，包尾
		bOnLineData[k++] = SystemDefine.PACKET_TAIL;


		 Log.e(SystemDefine.LOG_TAG, "Send online data..........") ;
		
//		sendMsgBack(pData.getAddress(), pData.getPort(), bOnLineData);		
		 sendMsgToServer(bOnLineData);
	}

	private void processPacketOnline(DatagramPacket pData) {
		// TODO Auto-generated method stub
//		mBedDocList.receiveBedOnLineData(pData);

		 Log.e(SystemDefine.LOG_TAG, "receive online data..........") ;
		mBedDocList.receiveOnLineData(pData);
		
	}
	
	private void processPacketOffline(DatagramPacket pData) {
		// TODO Auto-generated method stub

		byte[] bBack = new byte[18];// pData.getData();
		System.arraycopy(pData.getData(), 0, bBack, 0, 18);

		bBack[2] = 0x12; //len
		bBack[3] = 0x00;
		
		bBack[8] = 0x02; 
		// todo alter crc code
//		sendMsgBack(pData.getAddress(), pData.getPort(), bBack);
		sendMsgToServer(bBack);

		mBedDocList.receiveBedOffLineData(pData);		
	}

	private void processPacketFetalData(DatagramPacket pData) {
		// TODO Auto-generated method stub
		byte[] bBack = new byte[18];// pData.getData();
		System.arraycopy(pData.getData(), 0, bBack, 0, 18);

//		Log.e(SystemDefine.LOG_TAG, "receive fetal data index is " + pData.getData()[4] );
		//55AA1100AC 000000022E00D594 27BDB9394700DF07 0700040002001100 1A0036
		bBack[2] = 0x12; //len
		bBack[3] = 0x00;

		bBack[8] = 0x02; 
		 
		// todo alter crc code
		bBack[17] = SystemDefine.PACKET_TAIL;
//		sendMsgBack(pData.getAddress(), pData.getPort(), bBack);
		sendMsgToServer(bBack);
		
		//process data 
		mBedDocList.receivePacketData(pData);		
	}

//	private void sendMsgBack(InetAddress aAddr, int aPort, byte[] bData) {
	private void sendMsgToServer(byte[] bData) {
		DatagramPacket p = new DatagramPacket(bData, bData.length, mServerAddress, mServerPort);
		try {
			mServerSocket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Log.e(SystemDefine.LOG_TAG, "send msg back  " + mServerPort );
	}


}
