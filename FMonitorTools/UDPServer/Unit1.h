//---------------------------------------------------------------------------

#ifndef Unit1H
#define Unit1H
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include "IdBaseComponent.hpp"
#include "IdComponent.hpp"
#include "IdUDPBase.hpp"
#include "IdUDPServer.hpp"
#include <ExtCtrls.hpp>
//---------------------------------------------------------------------------
class TForm1 : public TForm
{
__published:	// IDE-managed Components
	TIdUDPServer *IdUDPServer1;
	TMemo *Memo1;
	TTimer *Timer1;
	TPanel *Panel1;
	TButton *btn1;
	TButton *Button2;
	TButton *Button1;
	TButton *Button3;
	TButton *Button4;
	TButton *Button5;
	TButton *Button6;
	TButton *Button7;
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall IdUDPServer1UDPRead(TIdUDPListenerThread *AThread, TBytes AData,
          TIdSocketHandle *ABinding);
	void __fastcall Button1Click(TObject *Sender);
	void __fastcall Timer1Timer(TObject *Sender);
	void __fastcall Button2Click(TObject *Sender);
	void __fastcall btn1Click(TObject *Sender);
	void __fastcall Button7Click(TObject *Sender);
	void __fastcall Button6Click(TObject *Sender);
private:	// User declarations
	int mPacketIndex;
	int mServerPort;
	String mServerIP;
	Word GetCRC16Word(Word crcData, Byte bData);
	Word GetCRC16(Word crc, Byte* buffer, int len);
	Word GetCRC16DByte(Word crc, Sysutils::TBytes ABuffer);
	void ProcessBackData(TBytes AData,  TIdSocketHandle *ABinding);
	void SendFetalData(void);
	void SendDetectBackData(TIdSocketHandle *ABinding);


public:		// User declarations
	__fastcall TForm1(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TForm1 *Form1;
//---------------------------------------------------------------------------
#endif
