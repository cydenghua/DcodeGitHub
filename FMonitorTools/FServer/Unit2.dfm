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
    object Button1: TButton
      Left = 16
      Top = 12
      Width = 129
      Height = 43
      Caption = 'SaveToFile'
      TabOrder = 0
      OnClick = Button1Click
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
