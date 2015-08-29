package com.example.fmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BedPanelList {
	
	private HashMap<String, BedPanel> mBedPanelMap;
	
	public BedPanelList() {
		// TODO Auto-generated constructor stub
		mBedPanelMap = new HashMap<String, BedPanel>();
	}
	
	public void addBedPanel(BedPanel bedPanel) {
		mBedPanelMap.put(bedPanel.getBedNo(), bedPanel);//.add(bedPanel);		
	}
	
	public BedPanel getBedPanel(String bedNo){
		return mBedPanelMap.get(bedNo);		
	}

}
