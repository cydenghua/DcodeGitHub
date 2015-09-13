package com.example.fmonitor;

public class DataPacketCreate { 

	private static DataPacketCreate single = null;

	// 静态工厂方法
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
		//2包头+2包长+4包序号+1包状态+2包类型+4机器编号+N数据+2CRC+1包尾
		len += 18;

		byte[] packetData = new byte[len];

		// 2个包头
		packetData[0] = SystemDefine.PACKET_HEAD1;
		packetData[1] = SystemDefine.PACKET_HEAD2;
		// 2字节包长
		packetData[2] = (byte)len;
		packetData[3] = 0x00;
		// 4字节包序号
		packetData[4] = 0x00; // mPacketIndex;
		packetData[5] = 0x00;
		packetData[6] = 0x00;
		packetData[7] = 0x00;
		// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
		packetData[8] = dataStatus;
		// 2字节包类型
		packetData[9] = dataType;
		packetData[10] = 0x00;
		// 4字节机器编号
		packetData[11] = SystemDefine.getInstance().getIp1();
		packetData[12] = SystemDefine.getInstance().getIp2();
		packetData[13] = SystemDefine.getInstance().getIp3();
		packetData[14] = SystemDefine.getInstance().getIp4();
		// N字节数据
		if(dataArr.length > 0) {
			System.arraycopy(dataArr, 0, packetData, 15, dataArr.length);			
		}

		// 2字节，校验码
		packetData[len-3] = 0x00;
		packetData[len-2] = 0x00;
		// 1字节，包尾
		packetData[len-1] = SystemDefine.PACKET_TAIL;

		return packetData;
	}

}
