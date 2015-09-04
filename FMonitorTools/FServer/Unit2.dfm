object Form2: TForm2
  Left = 0
  Top = 0
  Caption = 'Form2'
  ClientHeight = 451
  ClientWidth = 778
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 778
    Height = 65
    Align = alTop
    TabOrder = 0
    Visible = False
    object Label1: TLabel
      Left = 24
      Top = 13
      Width = 31
      Height = 13
      Caption = 'Label1'
    end
    object Label2: TLabel
      Left = 24
      Top = 32
      Width = 31
      Height = 13
      Caption = 'Label2'
    end
    object Button1: TButton
      Left = 392
      Top = 8
      Width = 75
      Height = 25
      Caption = 'Button1'
      TabOrder = 0
    end
    object Edit1: TEdit
      Left = 61
      Top = 5
      Width = 121
      Height = 21
      TabOrder = 1
      Text = 'Edit1'
    end
    object Edit2: TEdit
      Left = 61
      Top = 33
      Width = 121
      Height = 21
      TabOrder = 2
      Text = 'Edit2'
    end
  end
  object Memo1: TMemo
    Left = 0
    Top = 65
    Width = 778
    Height = 386
    Align = alClient
    Lines.Strings = (
      #22312#25163#26426#31243#24207#35774#32622#22320#22336#20026#26412#30005#33041'IP'#22320#22336
      #31471#21475#21495#20026'2013')
    TabOrder = 1
  end
  object IdUDPServer1: TIdUDPServer
    Bindings = <>
    DefaultPort = 2013
    OnUDPRead = IdUDPServer1UDPRead
    Left = 56
    Top = 104
  end
  object Timer1: TTimer
    Enabled = False
    OnTimer = Timer1Timer
    Left = 56
    Top = 168
  end
end
