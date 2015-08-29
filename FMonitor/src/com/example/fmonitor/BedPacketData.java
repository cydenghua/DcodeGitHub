package com.example.fmonitor;

public class BedPacketData {

	private int mBedNo = 0;
	private int mFetalNum = 0;
	private int mFhr1 = 0;
	private int mFhr2 = 0;
	private int mFhr3 = 0;
	private int mUC = 0;
	private int mTD = 0;

	public BedPacketData() {
		// TODO Auto-generated constructor stub
	}

	public void setBedNo(int bedNo) {
		this.mBedNo = bedNo;
	}

	public int getBedNo() {
		return this.mBedNo;
	}
	
	public void setFetalNum(int fetalNum) {
		this.mFetalNum = fetalNum;
	}

	public int getFetalNum() {
		return this.mFetalNum;
	}

	public void setFhr1(int fhr) {
		this.mFhr1 = fhr;
	}

	public int getFhr1() {
		return this.mFhr1;
	}

	public void setFhr2(int fhr) {
		this.mFhr2 = fhr;
	}

	public int getFhr2() {
		return this.mFhr2;
	}

	public void setFhr3(int fhr) {
		this.mFhr3 = fhr;
	}

	public int getFhr3() {
		return this.mFhr3;
	}

	public void setUC(int uc) {
		this.mUC = uc;
	}

	public int getUC() {
		return this.mUC;
	}

	public void setTD(int td) {
		this.mTD = td;
	}

	public int getTD() {
		return this.mTD;
	}

}
