object Form1: TForm1
  Left = 0
  Top = 0
  Caption = 'Form1'
  ClientHeight = 360
  ClientWidth = 585
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
  object Memo1: TMemo
    Left = 0
    Top = 0
    Width = 353
    Height = 360
    Align = alLeft
    Lines.Strings = (
      'Memo1')
    TabOrder = 0
  end
  object Panel1: TPanel
    Left = 416
    Top = 0
    Width = 169
    Height = 360
    Align = alRight
    Caption = 'Panel1'
    TabOrder = 1
    object btn1: TButton
      Left = 1
      Top = 326
      Width = 167
      Height = 33
      Align = alBottom
      Caption = #21957#25506#21253
      TabOrder = 0
      OnClick = btn1Click
    end
    object Button2: TButton
      Left = 24
      Top = 16
      Width = 121
      Height = 65
      Caption = 'Start'
      TabOrder = 1
      OnClick = Button2Click
    end
    object Button1: TButton
      Left = 40
      Top = 87
      Width = 97
      Height = 41
      Caption = 'Button1'
      TabOrder = 2
      OnClick = Button1Click
    end
    object Button3: TButton
      Left = 1
      Top = 161
      Width = 167
      Height = 33
      Align = alBottom
      Caption = 'btn1'
      TabOrder = 3
    end
    object Button4: TButton
      Left = 1
      Top = 194
      Width = 167
      Height = 33
      Align = alBottom
      Caption = 'btn1'
      TabOrder = 4
    end
    object Button5: TButton
      Left = 1
      Top = 227
      Width = 167
      Height = 33
      Align = alBottom
      Caption = 'btn1'
      TabOrder = 5
    end
    object Button6: TButton
      Left = 1
      Top = 260
      Width = 167
      Height = 33
      Align = alBottom
      Caption = #26426#22120#27880#20876#21253
      TabOrder = 6
      OnClick = Button6Click
    end
    object Button7: TButton
      Left = 1
      Top = 293
      Width = 167
      Height = 33
      Align = alBottom
      Caption = 'crc '
      TabOrder = 7
      OnClick = Button7Click
    end
  end
  object IdUDPServer1: TIdUDPServer
    Bindings = <>
    DefaultPort = 0
    OnUDPRead = IdUDPServer1UDPRead
    Left = 264
    Top = 232
  end
  object Timer1: TTimer
    Enabled = False
    OnTimer = Timer1Timer
    Left = 368
    Top = 232
  end
end
