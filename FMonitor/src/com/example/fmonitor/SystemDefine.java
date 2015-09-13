package com.example.fmonitor;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class SystemDefine {

	public static final int LISTENER_PORT = 2013;
	public static final int BUF_SIZE = 1024;
	public static final String LOG_TAG = "FMonitor--->:";

	public static final int FHR_P_M = 75; // ÿ���Ӳ�����
	public static final int MAX_FILE_BUFF_LENGTH = FHR_P_M * 60 * 48; // 48Сʱ
	public static final int MAX_FHR_ABS_SUB = 30; // ��̥��������һ��̥���ʲ�ֵ����ֵ�����ֵ

	public static final byte PACKET_HEAD1 = 0x55; // ��ͷ1
	public static final byte PACKET_HEAD2 = (byte) 0xAA; // ��ͷ2
	public static final byte PACKET_TAIL = 0x05; // ��β.

	public static final byte PACKET_ONLINE = 0x12; // �豸����ע��
	public static final byte PACKET_OFFLINE = 0x14; // �豸ע��
	public static final byte PACKET_DETECT = 0x0B; // �豸����̽���
	public static final byte PACKET_FETAL_DATA = 0x2F; // ̥�� ���ݰ�

	public static final int MESSAGE = 0x00; // ��������Ϣ
	public static final int MESSAGE_ADD_BED = MESSAGE + 1; // ���Ӵ�λ
	public static final int MESSAGE_REFRESH_DATA = MESSAGE + 2; //
	public static final int MESSAGE_STOP_DETECT = MESSAGE + 3; // ֹͣ��̽

	private byte Ip1 = 0x01;
	private byte Ip2 = 0x02;
	private byte Ip3 = 0x03;
	private byte Ip4 = 0x04;

	public SystemDefine() {
		super();
	}

	private static SystemDefine single = null;

	// ��̬��������
	public static SystemDefine getInstance() {
		if (single == null) {
			single = new SystemDefine();
		}
		return single;
	}
	
	public void setMachineNo(int machineNo) {
		Ip1 = (byte)(machineNo & 0xFF);
		Ip2 = (byte)((machineNo>>8) & 0xFF);
		Ip3 = (byte)((machineNo>>16) & 0xFF);
		Ip4 = (byte)((machineNo>>24) & 0xFF);
		
		Log.e(LOG_TAG, "ip " + Ip1 + ":" + Ip2 + ":" + Ip3 + ":" + Ip4 );
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
