package com.example.fmonitor;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.Time;
import android.util.Log;

@SuppressLint("ResourceAsColor")
public class CTGChartDraw {

	private Paint mPaint;
	private int mMarginTop = 2;
	private int mMarginLeft = 40;
	private int mMarginRight = 0;
	private int mDrawX = 0;
	private int mColorBigLine = 0x00745847;
	private int mColorSmallLine = 0x00BCCADA;
	private int mPaperSpeed;
	private int mFhr2StartY = 20; // 第二胎曲线Y坐标起始偏移
	private int mFhr3StartY = -20; // 第三胎曲线Y坐标起始偏移

	private int mCTGChartStandard;
	private int mChartRange;
	private int mFhrMaxLine;
	private int mFhrMinLine;
	private int mFhrLineNum;
	private int mTocoLineNum;

	private float mLineLarge;
	private int mStartDrawPos = 0;

	private int mConfigFhrMin = 110;
	private int mConfigFhrMax = 160;
	private int mCTGFontSize = 20;
	private int mCTGValueFontSize = 90;
	private int mColorFhr1 = Color.rgb(0, 0, 255);
	private int mColorFhr2 = Color.rgb(128, 0, 128);
	private int mColorFhr3 = Color.rgb(255, 128, 0);
	private int mColorUC = Color.rgb(0, 143, 9);

	public CTGChartDraw() {
		// TODO Auto-generated constructor stub
		mPaint = new Paint();
		setCTGChartStandard(0); // 默认为美国标准
		setPaperSpeed(3); // 默认走纸速度为3厘米每分

		mColorBigLine = Color.rgb(144, 159, 180);
		mColorSmallLine = Color.rgb(221, 230, 237);
	}

	public void setCTGChartStandard(int AStandard) {
		// CTG图标准，0美国标准，1国际标准
		if (0 == AStandard) {
			mChartRange = 210 + 100 + 15; // ctg210 coto100 留空100
			mFhrMaxLine = 240;
			mFhrMinLine = 30;
			mFhrLineNum = 22; // (240-30)/10 + 1
			mTocoLineNum = 11; // 100/10 + 1
		}
		if (1 == AStandard) {
			mChartRange = 160 + 100 + 15;
			mFhrMaxLine = 210;
			mFhrMinLine = 50;
			mFhrLineNum = 17;
			mTocoLineNum = 11;
		}
		mCTGChartStandard = AStandard;
	}

	public void setPaperSpeed(int ASpeed) {
		mPaperSpeed = ASpeed;
	}

	private int mOldOffset = 0;

	public void drawPaper(Canvas canvas, BedDocument bedDoc, int offSet) {
		if (0 == offSet) {
			mStartDrawPos = 0;
			if (null != bedDoc) {
				if (bedDoc.getBedNo().length() > 0) {
					mStartDrawPos = canvas.getWidth() - mMarginLeft
							- mMarginRight - 50;
					mStartDrawPos = bedDoc.mDocWritePos - mStartDrawPos
							+ offSet;
				}
			}
		} else {
			mStartDrawPos += (offSet - mOldOffset);
		}
		mOldOffset = offSet;

		if (null != bedDoc) {
			mStartDrawPos = (mStartDrawPos > bedDoc.mDocWritePos) ? (bedDoc.mDocWritePos - 100)
					: mStartDrawPos;
		}
		mStartDrawPos = (mStartDrawPos < 0) ? 0 : mStartDrawPos;

		drawTime(canvas, bedDoc);

		if (null != bedDoc) {
			drawLine(canvas, bedDoc);
		}
	}

	public void drawPaper(Canvas canvas, BedDocument bedDoc) {
		drawPaper(canvas, bedDoc, 0);
	}

	public void drawPaperBase(Canvas canvas) {
		mLineLarge = (float) (canvas.getHeight() - 2 * mMarginTop)
				/ mChartRange;
		mMarginRight = (canvas.getWidth() - mMarginLeft) % 25;
		if (mMarginRight < 20) {
			mMarginRight += 25;
		}
		mMarginRight = (mMarginRight + mMarginLeft) / 2;
		mMarginLeft = mMarginRight;

		drawCTGGrid(canvas);
		drawTocoGrid(canvas);
	}

	private void drawCTGGrid(Canvas canvas) {
		mLineLarge = (float) (canvas.getHeight() - 2 * mMarginTop)
				/ mChartRange;
		int k;
		int bigLineDistance = 0; // 一个大网格的高度

		mMarginRight = (canvas.getWidth() - mMarginLeft) % 40;
		if (mMarginRight < 20) {
			mMarginRight += 40;
		}
		mMarginRight = (mMarginRight + mMarginLeft) / 2;
		mMarginLeft = mMarginRight;
		mDrawX = mMarginLeft;

		mPaint.setTextSize(mCTGFontSize);
		// 画心率正常范围 正常范围为120-160
		mPaint.setColor(Color.rgb(211, 223, 232));
		canvas.drawColor(Color.WHITE);
		canvas.drawRect(mMarginLeft, mLineLarge * 0 + mMarginTop,
				canvas.getWidth() - mMarginRight, mLineLarge
						* (mFhrMaxLine - mConfigFhrMax) + mMarginTop, mPaint);
		canvas.drawRect(mMarginLeft, mLineLarge * (mFhrMaxLine - mConfigFhrMin)
				+ mMarginTop, canvas.getWidth() - mMarginRight, mLineLarge
				* (mFhrMaxLine - mFhrMinLine) + mMarginTop, mPaint);

		for (int i = 0; i < mFhrLineNum; i++) {
			// 设置画笔颜色
			if (1 == mCTGChartStandard) {// 国际标准
				if ((i + 1) % 2 == 0) {
					mPaint.setColor(mColorBigLine);
				} else {
					mPaint.setColor(mColorSmallLine);
				}
				bigLineDistance = (int) mLineLarge * 2 * 10;
				k = 210 - i * 10;
			} else { // 美国标准
				if ((i % 3) == 0) {
					mPaint.setColor(mColorBigLine);
				} else {
					mPaint.setColor(mColorSmallLine);
				}
				bigLineDistance = (int) mLineLarge * 3 * 10;
				k = 240 - i * 10;
			}

			canvas.drawLine(mMarginLeft, mLineLarge * 10 * i + mMarginTop,
					canvas.getWidth() - mMarginRight, mLineLarge * 10 * i
							+ mMarginTop, mPaint);

			// 第一天线和最后一条线不写数字
			if (i != 0 && i < (mFhrLineNum - 1)
					&& (mPaint.getColor() != mColorSmallLine)) {
				String sNum = Integer.toString(k);
				mPaint.setColor(mColorFhr3);
				canvas.drawText(sNum, 0, mLineLarge * 10 * i + mMarginTop - 5,
						mPaint);
				canvas.drawText(sNum, canvas.getWidth() - mMarginRight,
						mLineLarge * 10 * i + mMarginTop - 5, mPaint);
			}
		}

		// 画胎心率竖线，并写纵坐标数值
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) {
			if ((i % 3) == 0) {
				mPaint.setColor(mColorBigLine);
			} else {
				mPaint.setColor(mColorSmallLine);
			}
			// 画胎心率竖线
			canvas.drawLine(i * 40 + mMarginLeft, mMarginTop, i * 40
					+ mMarginLeft, mMarginTop + (mFhrMaxLine - mFhrMinLine)
					* mLineLarge, mPaint);
		}
	}

	private void drawTime(Canvas canvas, BedDocument bedDoc) {
		mPaint.setTextSize(mCTGFontSize);
		mPaint.setColor(mColorFhr3);
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) {
			// 写时间座标.
			String sStr = "";
			Time t = new Time();
			t.setToNow();
			if (null != bedDoc) {
				t.set(bedDoc.mTimeStart);
			}
			if ((i % 6) == 3) { // 绘时间
				DecimalFormat fFormat = new DecimalFormat(".0");
				float iSeconds = (i / mPaperSpeed + mStartDrawPos / 120.0f) * 60;
				if (3 == (i % 12)) { // 设置为绝对时间模式或者每隔12列，则输出绝对时间
					t.set(t.toMillis(true) + (int) iSeconds * 1000);
					fFormat.applyPattern("00");
					sStr = fFormat.format(t.hour) + ":"
							+ fFormat.format(t.minute);
				} else {// 其他情况输出相对时间
					fFormat.applyPattern(".0");
					sStr = fFormat.format(iSeconds / 60) + "分";
				}
				canvas.drawText(sStr, i * 40 + mMarginLeft,
						(mFhrMaxLine - mFhrMinLine) * mLineLarge + 20, mPaint);
			}
		}

	}

	private void drawTocoGrid(Canvas canvas) {
		int k;

		// 画宫缩部分的横线.
		for (int i = 0; i < mTocoLineNum; i++) {
			if ((i % 2) == 0) {
				mPaint.setColor(mColorBigLine);
			} else {
				mPaint.setColor(mColorSmallLine);
			}
			canvas.drawLine(mMarginLeft, canvas.getHeight() - mMarginTop
					- mLineLarge * i * 10, canvas.getWidth() - mMarginRight,
					canvas.getHeight() - mMarginTop - mLineLarge * i * 10,
					mPaint);

			// 写数字
			k = i * 10;
			if (i != 0 && i < (mTocoLineNum - 1)
					&& (mPaint.getColor() != mColorSmallLine)) { // 第一天线和最后一条线不写数字
				mPaint.setColor(mColorFhr3);
				canvas.drawText("" + k, mMarginLeft - 20, canvas.getHeight()
						- mMarginTop - mLineLarge * i * 10 - 5, mPaint);
				canvas.drawText("" + k, canvas.getWidth() - mMarginRight + 5,
						canvas.getHeight() - mMarginTop - mLineLarge * i * 10
								- 5, mPaint);
			}
		}

		// 画宫缩部分的竖线和纵坐标数值，每隔 25 个像素画一条竖线.
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) { // 列间距：25个像素
			if ((i % 3) == 0) {
				mPaint.setColor(mColorBigLine);
			} else {
				mPaint.setColor(mColorSmallLine);
			}
			canvas.drawLine(i * 40 + mMarginLeft, canvas.getHeight()
					- mMarginTop - mLineLarge * 100, i * 40 + mMarginLeft,
					canvas.getHeight() - mMarginTop, mPaint);
		}
	}

	private void drawLine(Canvas canvas, BedDocument bedDoc) {
		mPaint.setStrokeWidth(2);
		mDrawX = mMarginLeft;
		for (int i = mStartDrawPos; i < bedDoc.mDocWritePos; i++) {
			drawDot(canvas, bedDoc, i);
			if (mDrawX > canvas.getWidth() - mMarginRight) {
				break;
			}
		}
		mPaint.setStrokeWidth(1);

	}

	private void drawDot(Canvas canvas, BedDocument bedDoc, int aPostion) {
		int k, x1, y1, x2, y2;
		int iCurrPos = aPostion;
		int iPrevPos = aPostion - (4 - mPaperSpeed); // 上一次档案值的位置

		iPrevPos = (iPrevPos < 0) ? (0) : (iPrevPos);
		int iCurrPosValue, iPrevPosValue;
		// if(!CanPutDot(iCurrPos)) { return; }

		int prevDrawX = mDrawX - 1;
		if (prevDrawX < mMarginLeft) {
			prevDrawX = mMarginLeft;
		}

		// 第一胎心率
		if (0 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			iPrevPosValue = bedDoc.mFHR1[iPrevPos] & 0xFF;
			iCurrPosValue = bedDoc.mFHR1[iCurrPos] & 0xFF;
			if (IsValidFhrValue(iPrevPosValue, iCurrPosValue)) {// 胎心率数值有效
				mPaint.setColor(mColorFhr1);
				x1 = prevDrawX;
				y1 = (int) ((mFhrMaxLine - iPrevPosValue) * mLineLarge)
						+ mMarginTop;
				x2 = mDrawX;
				y2 = (int) ((mFhrMaxLine - iCurrPosValue) * mLineLarge)
						+ mMarginTop;
				canvas.drawLine(x1, y1, x2, y2, mPaint);
			}
		}
		// 第二胎心率
		if (1 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			iPrevPosValue = bedDoc.mFHR2[iPrevPos] & 0xFF;
			iCurrPosValue = bedDoc.mFHR2[iCurrPos] & 0xFF;
			if (IsValidFhrValue(iPrevPosValue, iCurrPosValue)) {// 胎心率数值有效
				if (iCurrPosValue < mFhrMinLine + mFhr2StartY
						|| iPrevPosValue < mFhrMinLine + mFhr2StartY) { // 超出画布范围，不画点

				} else {
					mPaint.setColor(mColorFhr2);
					x1 = prevDrawX;
					y1 = mFhr2StartY
							+ (int) ((mFhrMaxLine - iPrevPosValue) * mLineLarge)
							+ mMarginTop;
					x2 = mDrawX;
					y2 = mFhr2StartY
							+ (int) ((mFhrMaxLine - iCurrPosValue) * mLineLarge)
							+ mMarginTop;
					canvas.drawLine(x1, y1, x2, y2, mPaint);
				}
			}
		}

		// 画宫缩
		iPrevPosValue = bedDoc.mUC[iPrevPos];
		iCurrPosValue = bedDoc.mUC[iCurrPos];
		if (iPrevPosValue > 100) {
			iPrevPosValue = 100;
		}
		if (iCurrPosValue > 100) {
			iCurrPosValue = 100;
		}
		mPaint.setColor(mColorUC);
		canvas.drawLine(prevDrawX, canvas.getHeight() - mMarginTop
				- iPrevPosValue * mLineLarge, mDrawX, canvas.getHeight()
				- mMarginTop - iCurrPosValue * mLineLarge, mPaint);

		// 画胎动。有手动胎动或者自动胎动时画胎动。
		// 走纸速度为3时，判断当前时间点有无胎动；
		// 走纸速度为2时，判断前2个点或者当前点有无胎动；
		// 走纸速度为1时，判断前3个时间点有无胎动；
		iCurrPosValue = bedDoc.mTD[iCurrPos]; // 当前本次胎动
		iPrevPosValue = bedDoc.mTD[iPrevPos]; // 上一次胎动
		int iPrevPrevPosValue = (iCurrPos >= 2) ? (bedDoc.mTD[iCurrPos - 2])
				: (0); // 上上次胎动

		// 画手动胎动
		// boolean haveMuTD = false; // 是否有手动胎动
		// switch (mPaperSpeed) {
		// case 3:
		// if ((iCurrPosValue & SRDefine.MUTD) > 0) {
		// haveMuTD = true;
		// }
		// break;
		// case 2:
		// if (((iCurrPos % 3 == 0) && ((iCurrPosValue & SystemDefine.MUTD) >
		// 0))
		// || ((iCurrPos % 3 == 2) && ((iCurrPosValue & SystemDefine.MUTD) > 0
		// || (iPrevPosValue & SRDefine.MUTD) > 0))) {
		// haveMuTD = true;
		// }
		// break;
		// case 1:
		// if ((iCurrPosValue & SystemDefine.MUTD) > 0
		// || (iPrevPosValue & SystemDefine.MUTD) > 0
		// || (iPrevPrevPosValue & SystemDefine.MUTD) > 0) {
		// haveMuTD = true;
		// }
		// break;
		// default:
		// break;
		// }
		// if (haveMuTD) {
		// canvas.drawLine(mDrawX, canvas.getHeight() - mMarginTop - 110
		// * mLineLarge, mDrawX, canvas.getHeight() - mMarginTop - 105
		// * mLineLarge, mPaint);
		// }
		// // 画自动胎动
		// boolean haveAuTD = false; // 是否有自动胎动
		// switch (mPaperSpeed) {
		// case 3:
		// if ((iCurrPosValue & SRDefine.AUTD) > 0) {
		// haveAuTD = true;
		// }
		// break;
		// case 2:
		// if (((iCurrPos % 3 == 0) && ((iCurrPosValue & SRDefine.AUTD) > 0))
		// || ((iCurrPos % 3 == 2) && ((iCurrPosValue & SRDefine.AUTD) > 0 ||
		// (iPrevPosValue & SRDefine.AUTD) > 0))) {
		// haveAuTD = true;
		// }
		// break;
		// case 1:
		// if ((iCurrPosValue & SRDefine.AUTD) > 0
		// || (iPrevPosValue & SRDefine.AUTD) > 0
		// || (iPrevPrevPosValue & SRDefine.AUTD) > 0) {
		// haveAuTD = true;
		// }
		// break;
		// default:
		// break;
		// }
		// if (haveAuTD) {
		// canvas.drawLine(mDrawX, canvas.getHeight() - mMarginTop - 116
		// * mLineLarge, mDrawX, canvas.getHeight() - mMarginTop - 111
		// * mLineLarge, mPaint);
		// }

		mDrawX++;

	}

	private boolean IsValidFhrValue(int PrevFhrValue, int CurrFhrValue) {
		if (CurrFhrValue >= mFhrMinLine
				&& CurrFhrValue <= mFhrMaxLine
				&& PrevFhrValue >= mFhrMinLine
				&& PrevFhrValue <= mFhrMaxLine
				&& Math.abs(PrevFhrValue - CurrFhrValue) < SystemDefine.MAX_FHR_ABS_SUB) // 胎心率数值有效
		{
			return true;
		}
		return false;
	}

	public void drawCTGValue(Canvas canvas, BedDocument bedDoc) {
		if (null == bedDoc) {
			return;
		}
		if (!bedDoc.mStart) {
			return;
		}
		String sValue = "";
		mPaint.setTextSize(mCTGValueFontSize);
		int iWidth = (int) mPaint.measureText("0000");

		if (0 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			sValue = Integer.toString(bedDoc.mFHR1_SINGLE);
			mPaint.setColor(mColorFhr1);

			canvas.drawText(sValue, mMarginLeft + iWidth * 0,
					mCTGValueFontSize, mPaint);
		}
		if (1 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			sValue = Integer.toString(bedDoc.mFHR2_SINGLE);
			mPaint.setColor(mColorFhr2);
			canvas.drawText(sValue, mMarginLeft + iWidth * 1,
					mCTGValueFontSize, mPaint);
		}

	}

}
