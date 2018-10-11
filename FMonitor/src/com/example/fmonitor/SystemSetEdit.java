package com.example.fmonitor;
 
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SystemSetEdit {

	private Activity mParentActivity;
	private EditText mEditTextServerAddress;
	private EditText mEditTextServerPort;
	private Button mBtnSave;
	private Button mBtnExit;
	
	private String mServerAddress; 
	private String mServerPort;

    private SharedPreferences sp = null;  
	// private PopupWindow mPopup; // 定义popupwindow
	private Dialog mDialog;
	
	public SystemSetEdit(Activity aActivity) {
		// TODO Auto-generated constructor stub
		mParentActivity = aActivity;        
	}
	
	public void showDialog() {

		mDialog = new Dialog(mParentActivity, R.style.dialog_style);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.system_set_edit);

		mEditTextServerAddress = (EditText) mDialog.findViewById(R.id.editText_server_address);
		mEditTextServerPort = (EditText) mDialog.findViewById(R.id.editText_server_port); 
		mBtnSave = (Button) mDialog.findViewById(R.id.button_save); 
		mBtnExit = (Button) mDialog.findViewById(R.id.button_exit);

		mBtnExit.setOnClickListener(btnExitClick);
		mBtnSave.setOnClickListener(btnSaveClick);
		
        sp = mParentActivity.getSharedPreferences("SERVERINFO", Context.MODE_WORLD_READABLE);
        mServerAddress = sp.getString("SERVERADDRESS", "192.168.1.123");
        mServerPort = sp.getString("SERVERPORT", "2013"); 
        mEditTextServerAddress.setText(mServerAddress);  
        mEditTextServerPort.setText(mServerPort);
		
		mDialog.setCancelable(false);
		mDialog.show();
	}
	
	private OnClickListener btnSaveClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {			
	       if(!mServerAddress.equalsIgnoreCase(mEditTextServerAddress.getText().toString()) 
	    		   || !mServerPort.equalsIgnoreCase(mEditTextServerPort.getText().toString())) {

	            Editor editor = sp.edit();  
	            editor.putString("SERVERADDRESS", mEditTextServerAddress.getText().toString());  
	            editor.putString("SERVERPORT",mEditTextServerPort.getText().toString());  
	            editor.commit();	            
	            Toast.makeText(mParentActivity, "系统设置发生变化， 请重新启动系统！！！", 1).show();
	        }
	        
			mDialog.dismiss();
		}
	};
	
	private OnClickListener btnExitClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
		}
	};

}
