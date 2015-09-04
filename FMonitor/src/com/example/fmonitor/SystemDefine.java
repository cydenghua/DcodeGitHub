package com.example.fmonitor;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class SystemDefine {

	public static final int LISTENER_PORT = 2013;
	public static final int BUF_SIZE = 1024;
	public static final String LOG_TAG = "FMonitor--->:";

	public static final int FHR_P_M = 75; // 每分钟采样率
	public static final int MAX_FILE_BUFF_LENGTH = FHR_P_M * 60 * 48; // 48小时
	public static final int MAX_FHR_ABS_SUB = 30; // 旧胎心率与新一次胎心率差值绝对值的最大值

	public static final byte PACKET_HEAD1 = 0x55; // 包头1
	public static final byte PACKET_HEAD2 = (byte) 0xAA; // 包头2
	public static final byte PACKET_TAIL = 0x05; // 包尾

	public static final byte PACKET_ONLINE = 0x12; // 设备上线注册
	public static final byte PACKET_OFFLINE = 0x14; // 设备注销
	public static final byte PACKET_DETECT = 0x0B; // 设备网络探测包
	public static final byte PACKET_FETAL_DATA = 0x2F; // 胎监 数据包

	public static final int MESSAGE = 0x00; // 主界面消息
	public static final int MESSAGE_ADD_BED = MESSAGE + 1; // 增加床位
	public static final int MESSAGE_REFRESH_DATA = MESSAGE + 2; //
	public static final int MESSAGE_STOP_DETECT = MESSAGE + 3; // 停止嗅探

	private byte Ip1 = 0x01;
	private byte Ip2 = 0x02;
	private byte Ip3 = 0x03;
	private byte Ip4 = 0x04;

	public SystemDefine() {
		super();
		// TODO Auto-generated constructor stub
		   //获取wifi服务  
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
//        String ip = intToIp(ipAddress); 
	}

	private static SystemDefine single = null;

	// 静态工厂方法
	public static SystemDefine getInstance() {
		if (single == null) {
			single = new SystemDefine();
		}
		return single;
	}

	public byte getIp1() {
		return Ip1;
	}

	public byte getIp2() {
		return Ip2;
	}

	public byte getIp3() {
		return Ip3;
	}

	public byte getIp4() {
		return Ip4;
	}

}
