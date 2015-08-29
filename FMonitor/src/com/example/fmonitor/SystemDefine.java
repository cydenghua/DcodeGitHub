package com.example.fmonitor;

public class SystemDefine {

	public static final int LISTENER_PORT = 2013;
	public static final int BUF_SIZE = 1024;
	public static final String LOG_TAG = "FMonitor--->:";

	public static final int FHR_P_M = 75; // ÿ���Ӳ�����
	public static final int MAX_FILE_BUFF_LENGTH = FHR_P_M * 60 * 48; // 48Сʱ
	public static final int MAX_FHR_ABS_SUB = 30; // ��̥��������һ��̥���ʲ�ֵ����ֵ�����ֵ

	public static final byte PACKET_HEAD1 = 0x55; // ��ͷ1
	public static final byte PACKET_HEAD2 = (byte) 0xAA; // ��ͷ2
	public static final byte PACKET_TAIL = 0x05; // ��β

	public static final byte PACKET_ONLINE = 0x12; // �豸����ע��
	public static final byte PACKET_OFFLINE = 0x14; // �豸ע��
	public static final byte PACKET_DETECT = 0x0B; // �豸����̽���
	public static final byte PACKET_FETAL_DATA = 0x2E; // ̥�� ���ݰ�

	public static final int MESSAGE = 0x00; // ��������Ϣ
	public static final int MESSAGE_ADD_BED = MESSAGE + 1; // ���Ӵ�λ
	public static final int MESSAGE_REFRESH_DATA = MESSAGE + 2; // 
	public static final int MESSAGE_STOP_DETECT = MESSAGE + 3; // ֹͣ��̽

}
