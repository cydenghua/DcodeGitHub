package com.example.fmonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class NetSocketUDP {

	private SharedPreferences sp = null;
	private InetAddress mServerAddress = null;
	private int mServerPort = -1;
	DatagramSocket mServerSocket = null;
	private NetDataProcessor mNetDataProcessor = null;
	
	private static NetSocketUDP single = null;

	// 静态工厂方法
	public static NetSocketUDP getInstance() {
		if (single == null) {
			single = new NetSocketUDP();
		}
		return single;
	}
	
	private NetSocketUDP() {  
	}

	public void setNetDataProcessor(NetDataProcessor processor){
		this.mNetDataProcessor = processor;
	}
	
	public void setPort(int serverPort) {
		this.mServerPort = serverPort;
		createSocketCon();
	}
	
	public void setAddress(String serverAddress) {
		try {
			mServerAddress = InetAddress.getByName(serverAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block.
			e.printStackTrace();
		}		
		createSocketCon();
	}
	
	private void createSocketCon() {
		if(mServerPort < 0 || null == mServerAddress) {
			return;			
		}
		if(null != mServerSocket) {
			return;
		}
		try {
			mServerSocket = new DatagramSocket(mServerPort);
			mServerSocket.setSoTimeout(1000); 			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startUDPListen();
	}
	
	private void sendDataPacket(DatagramPacket pack) {
		if(null == mServerSocket) {
			Log.e(SystemDefine.LOG_TAG, "serversocket is null, can't send data.");
			return;
		}
		try {
			mServerSocket.send(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendDataArray(byte[] dataArr) {
		DatagramPacket pack = new DatagramPacket(dataArr,
				dataArr.length, mServerAddress,
				mServerPort);
		sendDataPacket(pack);		
	}
	
	public DatagramPacket receiveDataPacket() {
		
		if(null == mServerSocket) {
			return null;
		}
 
		byte[] buffer = new byte[SystemDefine.BUF_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer,
				SystemDefine.BUF_SIZE);
		try {
			mServerSocket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packet;
	}

	private byte[] getDetectMsg() {
		byte[] detectData = new byte[0];
		// 1字节包状态 0不需要确认， 1需要确认， 2确认应答
		byte[] bDetectPacket = DataPacketCreate.getInstance().getDataPacket(SystemDefine.PACKET_DETECT, (byte)0x01, detectData);  
		return bDetectPacket;
	}

	private boolean LISTEN = true;
	private boolean mDetectMsg = true;
	private void startUDPListen() {
		Thread udpThread = new Thread(new Runnable() {
			@Override
			public void run() {
					long timeCount = new Date().getTime() / 500; 
					while (LISTEN) {
						if(mDetectMsg) {
							try {
								Thread.sleep(5);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							
							if(timeCount != (new Date().getTime() / 500)) {		
								timeCount = new Date().getTime() / 500;						  
								byte[] bDetectPacket = getDetectMsg();
								sendDataArray(bDetectPacket);
							}
						}
						
						try { 
							mNetDataProcessor.processorData(receiveDataPacket());
						} catch (Exception e) {
						}
					}  
			}
		});
		udpThread.start();
	}


}
