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
	private int mFhr2StartY = 20; // �ڶ�̥����Y������ʼƫ��
	private int mFhr3StartY = -20; // ����̥����Y������ʼƫ��

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
		setCTGChartStandard(0); // Ĭ��Ϊ������׼
		setPaperSpeed(3); // Ĭ����ֽ�ٶ�Ϊ3����ÿ��

		mColorBigLine = Color.rgb(144, 159, 180);
		mColorSmallLine = Color.rgb(221, 230, 237);
	}

	public void setCTGChartStandard(int AStandard) {
		// CTGͼ��׼��0������׼��1���ʱ�׼
		if (0 == AStandard) {
			mChartRange = 210 + 100 + 15; // ctg210 coto100 ����100
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
		int bigLineDistance = 0; // һ��������ĸ߶�

		mMarginRight = (canvas.getWidth() - mMarginLeft) % 40;
		if (mMarginRight < 20) {
			mMarginRight += 40;
		}
		mMarginRight = (mMarginRight + mMarginLeft) / 2;
		mMarginLeft = mMarginRight;
		mDrawX = mMarginLeft;

		mPaint.setTextSize(mCTGFontSize);
		// ������������Χ ������ΧΪ120-160
		mPaint.setColor(Color.rgb(211, 223, 232));
		canvas.drawColor(Color.WHITE);
		canvas.drawRect(mMarginLeft, mLineLarge * 0 + mMarginTop,
				canvas.getWidth() - mMarginRight, mLineLarge
						* (mFhrMaxLine - mConfigFhrMax) + mMarginTop, mPaint);
		canvas.drawRect(mMarginLeft, mLineLarge * (mFhrMaxLine - mConfigFhrMin)
				+ mMarginTop, canvas.getWidth() - mMarginRight, mLineLarge
				* (mFhrMaxLine - mFhrMinLine) + mMarginTop, mPaint);

		for (int i = 0; i < mFhrLineNum; i++) {
			// ���û�����ɫ
			if (1 == mCTGChartStandard) {// ���ʱ�׼
				if ((i + 1) % 2 == 0) {
					mPaint.setColor(mColorBigLine);
				} else {
					mPaint.setColor(mColorSmallLine);
				}
				bigLineDistance = (int) mLineLarge * 2 * 10;
				k = 210 - i * 10;
			} else { // ������׼
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

			// ��һ���ߺ����һ���߲�д����
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

		// ��̥�������ߣ���д��������ֵ
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) {
			if ((i % 3) == 0) {
				mPaint.setColor(mColorBigLine);
			} else {
				mPaint.setColor(mColorSmallLine);
			}
			// ��̥��������
			canvas.drawLine(i * 40 + mMarginLeft, mMarginTop, i * 40
					+ mMarginLeft, mMarginTop + (mFhrMaxLine - mFhrMinLine)
					* mLineLarge, mPaint);
		}
	}

	private void drawTime(Canvas canvas, BedDocument bedDoc) {
		mPaint.setTextSize(mCTGFontSize);
		mPaint.setColor(mColorFhr3);
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) {
			// дʱ������.
			String sStr = "";
			Time t = new Time();
			t.setToNow();
			if (null != bedDoc) {
				t.set(bedDoc.mTimeStart);
			}
			if ((i % 6) == 3) { // ��ʱ��
				DecimalFormat fFormat = new DecimalFormat(".0");
				float iSeconds = (i / mPaperSpeed + mStartDrawPos / 120.0f) * 60;
				if (3 == (i % 12)) { // ����Ϊ����ʱ��ģʽ����ÿ��12�У����������ʱ��
					t.set(t.toMillis(true) + (int) iSeconds * 1000);
					fFormat.applyPattern("00");
					sStr = fFormat.format(t.hour) + ":"
							+ fFormat.format(t.minute);
				} else {// �������������ʱ��
					fFormat.applyPattern(".0");
					sStr = fFormat.format(iSeconds / 60) + "��";
				}
				canvas.drawText(sStr, i * 40 + mMarginLeft,
						(mFhrMaxLine - mFhrMinLine) * mLineLarge + 20, mPaint);
			}
		}

	}

	private void drawTocoGrid(Canvas canvas) {
		int k;

		// ���������ֵĺ���.
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

			// д����
			k = i * 10;
			if (i != 0 && i < (mTocoLineNum - 1)
					&& (mPaint.getColor() != mColorSmallLine)) { // ��һ���ߺ����һ���߲�д����
				mPaint.setColor(mColorFhr3);
				canvas.drawText("" + k, mMarginLeft - 20, canvas.getHeight()
						- mMarginTop - mLineLarge * i * 10 - 5, mPaint);
				canvas.drawText("" + k, canvas.getWidth() - mMarginRight + 5,
						canvas.getHeight() - mMarginTop - mLineLarge * i * 10
								- 5, mPaint);
			}
		}

		// ���������ֵ����ߺ���������ֵ��ÿ�� 25 �����ػ�һ������.
		for (int i = 0; i <= ((canvas.getWidth() - mMarginLeft - mMarginRight) / 40); i++) { // �м�ࣺ25������
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
		int iPrevPos = aPostion - (4 - mPaperSpeed); // ��һ�ε���ֵ��λ��

		iPrevPos = (iPrevPos < 0) ? (0) : (iPrevPos);
		int iCurrPosValue, iPrevPosValue;
		// if(!CanPutDot(iCurrPos)) { return; }

		int prevDrawX = mDrawX - 1;
		if (prevDrawX < mMarginLeft) {
			prevDrawX = mMarginLeft;
		}

		// ��һ̥����
		if (0 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			iPrevPosValue = bedDoc.mFHR1[iPrevPos] & 0xFF;
			iCurrPosValue = bedDoc.mFHR1[iCurrPos] & 0xFF;
			if (IsValidFhrValue(iPrevPosValue, iCurrPosValue)) {// ̥������ֵ��Ч
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
		// �ڶ�̥����
		if (1 == bedDoc.mFetalModal || 2 == bedDoc.mFetalModal) {
			iPrevPosValue = bedDoc.mFHR2[iPrevPos] & 0xFF;
			iCurrPosValue = bedDoc.mFHR2[iCurrPos] & 0xFF;
			if (IsValidFhrValue(iPrevPosValue, iCurrPosValue)) {// ̥������ֵ��Ч
				if (iCurrPosValue < mFhrMinLine + mFhr2StartY
						|| iPrevPosValue < mFhrMinLine + mFhr2StartY) { // ����������Χ��������

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

		// ������
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

		// ��̥�������ֶ�̥�������Զ�̥��ʱ��̥����
		// ��ֽ�ٶ�Ϊ3ʱ���жϵ�ǰʱ�������̥����
		// ��ֽ�ٶ�Ϊ2ʱ���ж�ǰ2������ߵ�ǰ������̥����
		// ��ֽ�ٶ�Ϊ1ʱ���ж�ǰ3��ʱ�������̥����
		iCurrPosValue = bedDoc.mTD[iCurrPos]; // ��ǰ����̥��
		iPrevPosValue = bedDoc.mTD[iPrevPos]; // ��һ��̥��
		int iPrevPrevPosValue = (iCurrPos >= 2) ? (bedDoc.mTD[iCurrPos - 2])
				: (0); // ���ϴ�̥��

		// ���ֶ�̥��
		// boolean haveMuTD = false; // �Ƿ����ֶ�̥��
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
		// // ���Զ�̥��
		// boolean haveAuTD = false; // �Ƿ����Զ�̥��
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
				&& Math.abs(PrevFhrValue - CurrFhrValue) < SystemDefine.MAX_FHR_ABS_SUB) // ̥������ֵ��Ч
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
