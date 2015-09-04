//---------------------------------------------------------------------------

#ifndef Unit2H
#define Unit2H
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

#define PACKET_HEAD1 0x55; // ��ͷ1
#define PACKET_HEAD2 0xAA; // ��ͷ2
#define PACKET_TAIL  0x05; // ��β

const Byte PACKET_ONLINE = 0x12; // �豸����ע��
const Byte PACKET_OFFLINE = 0x14; // �豸ע��
const Byte PACKET_DETECT = 0x0B; // �豸����̽���
const Byte PACKET_FETAL_DATA = 0x2F; // ̥�� ���ݰ�

class TForm2 : public TForm
{
__published:	// IDE-managed Components
	TIdUDPServer *IdUDPServer1;
	TPanel *Panel1;
	TButton *Button1;
	TEdit *Edit1;
	TEdit *Edit2;
	TLabel *Label1;
	TLabel *Label2;
	TMemo *Memo1;
	TTimer *Timer1;
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall IdUDPServer1UDPRead(TIdUDPListenerThread *AThread, TBytes AData,
          TIdSocketHandle *ABinding);
	void __fastcall Timer1Timer(TObject *Sender);
private:	// User declarations
	int mLogCount;
	int mPacketIndex;
	TIdSocketHandle *mUDPBinding;

	void LogMsg(String AMsg);
	void SendUDPData(Sysutils::TBytes ABuffer, TIdSocketHandle *ABinding);
	void ProcessReceiveData(TBytes AData, TIdSocketHandle *ABinding);
	void DoDetectMsg(TBytes AData, TIdSocketHandle *ABinding);
	void DoOnlineMsg(TBytes AData, TIdSocketHandle *ABinding);
	void StartSendFetalData(TIdSocketHandle *ABinding);

public:		// User declarations
	__fastcall TForm2(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TForm2 *Form2;
//---------------------------------------------------------------------------
#endif