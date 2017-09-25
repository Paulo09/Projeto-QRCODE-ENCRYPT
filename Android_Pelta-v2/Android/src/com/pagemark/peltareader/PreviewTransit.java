package com.pagemark.peltareader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class PreviewTransit extends Activity {
	CountDownTimer cdt;
	BroadcastReceiver rcvr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.prev_transit);
		cdt = new CountDownTimer(500, 500) {
			public void onTick(long millisUntilFinished) {}
			
			public void onFinish() {
				Intent myIntent = new Intent(PreviewTransit.this, SurfaceOverlay.class);
				PreviewTransit.this.startActivity(myIntent);
				finish();
				return;
			}
		}.start();
	}

	protected void onResume(){
		super.onResume();

//		IntentFilter iFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//		iFilter.addAction(Intent.ACTION_USER_PRESENT);
//		rcvr = new Rcvr();

//		try{
//			registerReceiver(rcvr, iFilter);
//		}catch(Exception ex){}

//		cdt.start();
	}
	
	protected void onPause(){
		super.onPause();
//		cdt.cancel();
	}

	protected void onDestroy() {
		super.onDestroy();
		cdt.cancel();
	}
	
	
//	class Rcvr extends BroadcastReceiver{
//
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
//				try{
//					unregisterReceiver(rcvr);
//				}catch(Exception ex){}
//				cdt.start();
//			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
////				cdt.start();
//				Log.i("PREVIEWTRANSIT","Screen ON");
//			} else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
//				Log.i("PREVIEWTRANSIT","Screen OFF");
//			} 
//		}
//	}
}