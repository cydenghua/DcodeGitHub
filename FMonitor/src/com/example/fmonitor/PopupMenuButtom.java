package com.example.fmonitor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopupMenuButtom {

	public static final int MENU_SET = 1;
//	public static final int MENU_START = 1;
//	public static final int MENU_STOP = 2;
//	public static final int MENU_EDIT = 3;
//	public static final int MENU_EVENT = 4;
//	public static final int MENU_WARN = 5;

	private PopupMenuClickInterface menuClickCallBack;

	private Activity mParentActivity;
	private View mParentView;
//	private BedDocument mBedDoc;
	private PopupWindow mPopup; // ����popupwindow
	private PopupMenuAdapter mMenuAdapter; // ����������
	private List<PopupMenuInfo> mMenuLists; // �˵����б�
	private GridView menuGridView; // ����gridview

	public PopupMenuButtom(Activity aActivity, View aView) {
		// TODO Auto-generated constructor stub
		mParentActivity = aActivity;
		mParentView = aView;
//		mBedDoc = bedDoc;
		menuClickCallBack = null;
		initPopupMenu();
	}

	public void setMenuClickCallBack(PopupMenuClickInterface clickBack) {
		menuClickCallBack = clickBack;
	}

	public void showPopupMenu() {
		mMenuLists = getMenuList();// MenuUtils.getMenuList();
		mMenuAdapter = new PopupMenuAdapter(mParentActivity, mMenuLists);
		menuGridView.setAdapter(mMenuAdapter);

		mPopup.setWidth(mParentView.getWidth());
		mPopup.showAtLocation(mParentView, Gravity.NO_GRAVITY,
				// mParentView.getLeft(),
				mParentActivity.findViewById(R.id.activity_main_layout_right)
						.getLeft(),
				mParentView.getHeight() - mPopup.getHeight());

	}

	private void initPopupMenu() {
		// Toast.makeText(mParentActivity, "show pop", 1).show();
		// ��ʼ��gridview
		menuGridView = (GridView) View.inflate(mParentActivity,
				R.layout.gridview_menu, null);
		// ��ʼ��PopupWindow,LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT������ʾ
		mPopup = new PopupWindow(menuGridView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// ����menu�˵�����
		mPopup.setBackgroundDrawable(mParentActivity.getResources()
				.getDrawable(R.drawable.menu_background));

		// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
		mPopup.setFocusable(true);
		// ������ʾ�����صĶ���
		mPopup.setAnimationStyle(R.style.menushow);
		mPopup.update();
		// ���ô�����ȡ����
		menuGridView.setFocusableInTouchMode(true);

		// ���ü����¼�,������²˵��������ز˵�
		menuGridView.setOnKeyListener(new android.view.View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (mPopup.isShowing())) {
					mPopup.dismiss();
					return true;
				}
				return false;
			}
		});

		// ��Ӳ˵���ť�¼�
		menuGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				PopupMenuInfo mInfo = mMenuLists.get(arg2);
				mPopup.dismiss();

				if (null != menuClickCallBack) {
					menuClickCallBack.menuClick(mInfo.menuId);
				}
			}
		});

	}

	private List<PopupMenuInfo> initMenu() {
		List<PopupMenuInfo> list = new ArrayList<PopupMenuInfo>();
		list.add(0, new PopupMenuInfo(MENU_SET, "ϵͳ����",
		R.drawable.menu_ic_set, false));
//		list.add(0, new PopupMenuInfo(MENU_WARN, "������¼.",
//		R.drawable.menu_ic_alarm, false));
//		list.add(0, new PopupMenuInfo(MENU_EVENT, "�¼�����",
//				R.drawable.menu_ic_event, false));
//		list.add(0, new PopupMenuInfo(MENU_EDIT, "�༭����",
//				R.drawable.menu_ic_edit, false));
//		if (mBedDoc.mStart) {
//			list.add(0, new PopupMenuInfo(MENU_STOP, "ֹͣ�໤",
//					R.drawable.menu_ic_stop, false));
//		} else {
//			list.add(0, new PopupMenuInfo(MENU_START, "��ʼ�໤",
//					R.drawable.menu_ic_start, false));
//		}

		return list;
	}

	// ��ȡ��ǰ�˵��б�
	public List<PopupMenuInfo> getMenuList() {
		List<PopupMenuInfo> list = initMenu();
		return list;
	}

}
