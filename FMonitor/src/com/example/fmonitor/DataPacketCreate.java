package com.example.fmonitor;

public class DataPacketCreate { 

	private static DataPacketCreate single = null;

	// ��̬��������
	public static DataPacketCreate getInstance() {
		if (single == null) {
			single = new DataPacketCreate();
		}
		return single;
	}

	public DataPacketCreate() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public byte[] getDataPacket(byte dataType, byte dataStatus, byte[] dataArr) {
		int len = dataArr.length;
		//2��ͷ+2����+4�����+1��״̬+2������+4�������+N����+2CRC+1��β
		len += 18;

		byte[] packetData = new byte[len];

		// 2����ͷ
		packetData[0] = SystemDefine.PACKET_HEAD1;
		packetData[1] = SystemDefine.PACKET_HEAD2;
		// 2�ֽڰ���
		packetData[2] = (byte)len;
		packetData[3] = 0x00;
		// 4�ֽڰ����
		packetData[4] = 0x00; // mPacketIndex;
		packetData[5] = 0x00;
		packetData[6] = 0x00;
		packetData[7] = 0x00;
		// 1�ֽڰ�״̬   0����Ҫȷ�ϣ� 1��Ҫȷ�ϣ� 2ȷ��Ӧ��
		packetData[8] = dataStatus;
		// 2�ֽڰ�����
		packetData[9] = dataType;
		packetData[10] = 0x00;
		// 4�ֽڻ������
		packetData[11] = SystemDefine.getInstance().getIp1();
		packetData[12] = SystemDefine.getInstance().getIp2();
		packetData[13] = SystemDefine.getInstance().getIp3();
		packetData[14] = SystemDefine.getInstance().getIp4();
		// N�ֽ�����
		if(dataArr.length > 0) {
			System.arraycopy(dataArr, 0, packetData, 15, dataArr.length);			
		}

		// 2�ֽڣ�У����
		packetData[len-3] = 0x00;
		packetData[len-2] = 0x00;
		// 1�ֽڣ���β
		packetData[len-1] = SystemDefine.PACKET_TAIL;

		return packetData;
	}

}
