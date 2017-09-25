package com.pagemark.peltareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class CamStandby extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cam_standby);
		
		((Button)findViewById(R.id.btn_camstandby_resume)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(CamStandby.this, PreviewTransit.class);
				CamStandby.this.startActivity(myIntent);
				finish();
			}
		});
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		Intent myIntent = new Intent(CamStandby.this, PreviewTransit.class);
		CamStandby.this.startActivity(myIntent);
		finish();
		return super.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent myIntent = new Intent(CamStandby.this, PreviewTransit.class);
			CamStandby.this.startActivity(myIntent);
			finish();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}