package com.example.fmonitor;

public class PopupMenuInfo {
	//����	 
	public String title;
	public int imgsrc;
	//�Ƿ�����
	public boolean ishide;
	//menuId
	public int menuId;
	
	public PopupMenuInfo(int menuId, String title,int imgsrc,Boolean ishide){
		this.menuId=menuId;
		this.title=title;
		this.imgsrc=imgsrc;
		this.ishide=ishide;
	}
}
