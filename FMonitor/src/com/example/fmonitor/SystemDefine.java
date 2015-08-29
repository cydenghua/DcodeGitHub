package com.example.fmonitor;

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
	public static final byte PACKET_FETAL_DATA = 0x2E; // 胎监 数据包

	public static final int MESSAGE = 0x00; // 主界面消息
	public static final int MESSAGE_ADD_BED = MESSAGE + 1; // 增加床位
	public static final int MESSAGE_REFRESH_DATA = MESSAGE + 2; // 
	public static final int MESSAGE_STOP_DETECT = MESSAGE + 3; // 停止嗅探

}
