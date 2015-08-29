// ---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit1.h"
// ---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma link "IdBaseComponent"
#pragma link "IdComponent"
#pragma link "IdUDPBase"
#pragma link "IdUDPServer"
#pragma resource "*.dfm"
TForm1 *Form1;

// ---------------------------------------------------------------------------
__fastcall TForm1::TForm1(TComponent* Owner) : TForm(Owner) {
}

// ---------------------------------------------------------------------------
void __fastcall TForm1::FormCreate(TObject *Sender) {
	mPacketIndex = 0;

	IdUDPServer1->Bindings->Add();
	// IdUDPServer1->Bindings[0]->IP = "";
	// IdUDPServer1->Bindings->Items[0]->IP = "127.0.0.1";
	// IdUDPServer1->Bindings->Items[0]->IP = "192.168.5.117";
	IdUDPServer1->Bindings->Items[0]->Port = 2013;
	IdUDPServer1->Active = true;

	mServerPort = 2011;
//	mServerIP = "192.168.1.182";
	mServerIP = "192.168.1.100";
	// Timer1->Enabled = true;
}

// ---------------------------------------------------------------------------
void __fastcall TForm1::IdUDPServer1UDPRead(TIdUDPListenerThread *AThread,
	TBytes AData, TIdSocketHandle *ABinding) {
	Memo1->Lines->Add("msg size = " + IntToStr(AData.Length));
	String s = "";
	for (int i = 0; i < AData.Length; i++) {
		s += IntToHex(AData[i], 2);
		s += "-";
	}
	Memo1->Lines->Add(s);



	ProcessBackData(AData, ABinding);
}
// ---------------------------------------------------------------------------

void TForm1::ProcessBackData(TBytes AData, TIdSocketHandle *ABinding) {
	if (0x0A == AData[9]) {
		if (0x02 == AData[8]) { // 收到探测应答
			Memo1->Lines->Add("收到探测应答，fa song zhu ce bao");
			Button6Click(this);
		}
	}


	if (0x0B == AData[9]) {
		if (0x01 == AData[8]) { // 收到探测应答
			Memo1->Lines->Add("收到探测包， 应答2222222222");
			SendDetectBackData(ABinding);
		}
	}


	if (0x12 == AData[9]) { // 收到注册应答
		Memo1->Lines->Add("收到注册");
		// start send fetal data
		SendFetalData();
	}

	if (0x2E == AData[9]) { // 收到注册应答
		if (0x02 == AData[8]) { // 收到探测应答
			Memo1->Lines->Add("收到数据应答包， 等待500ms， 发送下一包.");
			Sleep(500);
			SendFetalData();
		}
	}
}

// ---------------------------------------------------------------------------
void __fastcall TForm1::Button1Click(TObject *Sender) {
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(30);
	for (int i = 0; i < 30; i++) {
		ABuffer[i] = i;
	}
	IdUDPServer1->SendBuffer("192.168.5.73", 10000, ABuffer);

}

// ---------------------------------------------------------------------------
void __fastcall TForm1::Timer1Timer(TObject *Sender) {

	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	ABuffer[k++] = 0x55;
	ABuffer[k++] = 0xAA;
	// 2字节包长
	ABuffer[k++] = 0x11;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0x01;
	// 1字节包类型
	ABuffer[k++] = 0x2E; // 设备 嗅探包
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;
	// N字节数据

	ABuffer[k++] = 0xBD; // 胎心1
	ABuffer[k++] = 0x00; // 胎心2
	ABuffer[k++] = 0x00; // toco
	ABuffer[k++] = 0x00; // afm  自动胎动
	ABuffer[k++] = 0x00; // 胎监模式
	ABuffer[k++] = 0x00; // 胎动计算模式
	ABuffer[k++] = 0x00; // 胎心报警高限32-240
	ABuffer[k++] = 0x00; // 胎心报警低限
	ABuffer[k++] = 0x00; // 信号质量
	ABuffer[k++] = 0xA0; // 医生标记事件
	ABuffer[k++] = 0x78; // 错误代码

	// 2字节，校验码
	ABuffer[k++] = 0xBD;
	ABuffer[k++] = 0xB1;
	// 1字节，包尾
	ABuffer[k++] = 0x03;

	ABuffer.set_length(k);
	ABuffer[2] = k;

	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);
	// Sysutils::TBytes ABuffer; // = new TBytes;
	// ABuffer.set_length(100);
	// // for (int i = 0; i < 30; i++) {
	// // ABuffer[i] = i;
	// // }
	//
	// int k = 0;
	// // 2个标志
	// ABuffer[k++] = 0xA5;
	// ABuffer[k++] = 0x5A;
	// // 2字节包长
	// ABuffer[k++] = 18 + 0;
	// ABuffer[k++] = 0x00;
	// // 4字节包序号
	// ABuffer[k++] = mPacketIndex;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// // 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	// ABuffer[k++] = 0;
	// // 2字节包类型
	// ABuffer[k++] = 0x2E; // 胎监数据包
	// ABuffer[k++] = 0;
	// // 4字节机器编号
	// ABuffer[k++] = 11;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// // N字节数据
	// ABuffer[k++] = 130; // 胎心1
	// ABuffer[k++] = 130; // 胎心2
	// ABuffer[k++] = 30; // toco
	// ABuffer[k++] = 30; // afm  自动胎动
	// ABuffer[k++] = 3; // 胎监模式
	// ABuffer[k++] = 3; // 胎动计算模式
	// ABuffer[k++] = 160; // 胎心报警高限32-240
	// ABuffer[k++] = 120; // 胎心报警低限
	// ABuffer[k++] = 0; // 信号质量
	// ABuffer[k++] = 0; // 医生标记事件
	// ABuffer[k++] = 0; // 错误代码
	// // 2字节，校验码
	// ABuffer[k++] = 0;
	// ABuffer[k++] = 0;
	// // 1字节，包尾
	// ABuffer[k++] = 0x05;
	//
	// ABuffer[2] = k;
	// ABuffer.set_length(k);
	//
	// IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);

	mPacketIndex = mPacketIndex % 100;

}

// ---------------------------------------------------------------------------
void __fastcall TForm1::Button2Click(TObject *Sender) {

	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	ABuffer[k++] = 0xA5;
	ABuffer[k++] = 0x5A;
	// 2字节包长
	ABuffer[k++] = 18 + 0;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0;
	// 2字节包类型
	ABuffer[k++] = 0x0A; // 设备上线
	ABuffer[k++] = 0;
	// 4字节机器编号
	ABuffer[k++] = 11;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// N字节数据

	// 2字节，校验码
	ABuffer[k++] = 0;
	ABuffer[k++] = 0;
	// 1字节，包尾
	ABuffer[k++] = 0x05;

	ABuffer[2] = k;
	ABuffer.set_length(k);

	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);

	Timer1->Enabled = true;

}
// ---------------------------------------------------------------------------

void __fastcall TForm1::btn1Click(TObject *Sender) {

	// 55 AA 11 00 00 00 00 00 01 0A 00 D5 94 27 BD B1 03
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	ABuffer[k++] = 0x55;
	ABuffer[k++] = 0xAA;
	// 2字节包长
	ABuffer[k++] = 0x11;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = 0x00; // mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0x01;
	// 1字节包类型
	ABuffer[k++] = 0x0A; // 设备 嗅探包
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;
	// N字节数据

	// 2字节，校验码
	ABuffer[k++] = 0xBD;
	ABuffer[k++] = 0xB1;
	// 1字节，包尾
	ABuffer[k++] = 0x03;

	ABuffer.set_length(k);

	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);
}
// ---------------------------------------------------------------------------

void __fastcall TForm1::Button7Click(TObject *Sender) {

	// 55 AA 11 00 00 00 00 00 01 0A 00 D5 94 27 BD B1 03
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	// ABuffer[k++] = 0x55;
	// ABuffer[k++] = 0xAA;
	// 2字节包长
	// ABuffer[k++] = 0x11;
	// ABuffer[k++] = 0x00;
	// 4字节包序号
	// ABuffer[k++] = 0x00; // mPacketIndex;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	// ABuffer[k++] = 0x01;
	// 1字节包类型
	// ABuffer[k++] = 0x0A; // 设备 嗅探包
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;
	// N字节数据

	// 2字节，校验码
	// ABuffer[k++] = 0xBD;
	// ABuffer[k++] = 0xB1;
	// 1字节，包尾
	// ABuffer[k++] = 0x03;

	ABuffer.set_length(k);

	Word w = GetCRC16DByte(0, ABuffer);

	Memo1->Lines->Add("crr = " + IntToHex(w, 2));
	// GetCRC16DByte(Word crc, Sysutils::TBytes ABuffer)
	// Word TForm1::GetCRC16(Word crc, Byte* buffer, int len)

}
// ---------------------------------------------------------------------------

Word TForm1::GetCRC16Word(Word crcData, Byte bData) {
	Word const crc16_table[256] = {
		0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241, 0xC601,
		0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440, 0xCC01, 0x0CC0,
		0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40, 0x0A00, 0xCAC1, 0xCB81,
		0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841, 0xD801, 0x18C0, 0x1980, 0xD941,
		0x1B00, 0xDBC1, 0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01,
		0x1DC0, 0x1C80, 0xDC41, 0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0,
		0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081,
		0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
		0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441, 0x3C00,
		0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41, 0xFA01, 0x3AC0,
		0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981,
		0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41,
		0x2D00, 0xEDC1, 0xEC81, 0x2C40, 0xE401, 0x24C0, 0x2580, 0xE541, 0x2700,
		0xE7C1, 0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0,
		0x2080, 0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281,
		0x6240, 0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
		0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41, 0xAA01,
		0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840, 0x7800, 0xB8C1,
		0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80,
		0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40, 0xB401, 0x74C0, 0x7580, 0xB541,
		0x7700, 0xB7C1, 0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101,
		0x71C0, 0x7080, 0xB041, 0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0,
		0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481,
		0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
		0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841, 0x8801,
		0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40, 0x4E00, 0x8EC1,
		0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581,
		0x4540, 0x8701, 0x47C0, 0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341,
		0x4100, 0x81C1, 0x8081, 0x4040
	};
	return(crcData >> 8) ^ crc16_table[(crcData ^ bData) & 0xff];
}

Word TForm1::GetCRC16(Word crc, Byte* buffer, int len) {
	while (len--)
		crc = GetCRC16Word(crc, *buffer++);

	return crc;
}

Word TForm1::GetCRC16DByte(Word crc, Sysutils::TBytes ABuffer) {
	int k = ABuffer.Length;
	for (int i = 0; i < k; i++) {
		crc = GetCRC16Word(crc, ABuffer[i]);
	}
	return crc;

}

void __fastcall TForm1::Button6Click(TObject *Sender) {
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	ABuffer[k++] = 0x55;
	ABuffer[k++] = 0xAA;
	// 2字节包长
	ABuffer[k++] = 0x11;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = 0x00; // mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0x00;
	// 1字节包类型
	ABuffer[k++] = 0x10; // 设备 注册
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;
	// N字节数据

	// 2字节，校验码
	ABuffer[k++] = 0xBD;
	ABuffer[k++] = 0xB1;
	// 1字节，包尾
	ABuffer[k++] = 0x03;

	ABuffer.set_length(k);

	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);
}

// ---------------------------------------------------------------------------
void TForm1::SendFetalData(void) {
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

//55 AA
//1D 00
//98 00 00 00
//00
//2E
//00 D5 94 27
//BD 00 00 00 00 00 00 00 A0 78 80 00 00 F5 03
	int k = 0;
	// 2个标志
	ABuffer[k++] = 0x55;
	ABuffer[k++] = 0xAA;
	// 2字节包长
	ABuffer[k++] = 0x1D;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0x00;
	// 1字节包类型
	ABuffer[k++] = 0x2E; //  10
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;     //14
	// N字节数据
	ABuffer[k++] = 0xBD - mPacketIndex%30;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00 + mPacketIndex%50;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xA0;
	ABuffer[k++] = 0x78;
	ABuffer[k++] = 0x80;
	ABuffer[k++] = 0x00;

	// 2字节，校验码
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xF5;
	// 1字节，包尾
	ABuffer[k++] = 0x03;

	ABuffer.set_length(k);

	Memo1->Lines->Add("send fetal data index = " + IntToStr(mPacketIndex));
	if (mPacketIndex++ > 100) {
		mPacketIndex = 1;
	}

	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);
}

// ---------------------------------------------------------------------------
void TForm1::SendDetectBackData(TIdSocketHandle *ABinding) {


	// 55 AA 11 00 00 00 00 00 01 0A 00 D5 94 27 BD B1 03
	Sysutils::TBytes ABuffer; // = new TBytes;
	ABuffer.set_length(100);

	int k = 0;
	// 2个标志
	ABuffer[k++] = 0x55;
	ABuffer[k++] = 0xAA;
	// 2字节包长
	ABuffer[k++] = 0x11;
	ABuffer[k++] = 0x00;
	// 4字节包序号
	ABuffer[k++] = 0x00; // mPacketIndex;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0x00;
	// 1字节包状态   0不需要确认， 1需要确认， 2确认应答
	ABuffer[k++] = 0x02;
	// 1字节包类型
	ABuffer[k++] = 0x0B; // 设备 嗅探包
	// 4字节机器编号
	ABuffer[k++] = 0x00;
	ABuffer[k++] = 0xD5;
	ABuffer[k++] = 0x94;
	ABuffer[k++] = 0x27;
	// N字节数据

	// 2字节，校验码
	ABuffer[k++] = 0xBD;
	ABuffer[k++] = 0xB1;
	// 1字节，包尾
	ABuffer[k++] = 0x03;

	ABuffer.set_length(k);
//                                   ABinding->

//IdUDPServer1->SendBuffer(ABinding->IP, ABinding->Port, ABuffer);
//	IdUDPServer1->SendBuffer(mServerIP, mServerPort, ABuffer);
	IdUDPServer1->SendBuffer("192.168.1.104", 2013, ABuffer);

}

