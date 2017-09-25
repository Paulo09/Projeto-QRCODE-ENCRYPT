package com.pagemark.peltareader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash extends Activity {
	
	
	CountDownTimer cdt;
	BroadcastReceiver rcvr = new Rcvr();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		IntentFilter iFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(rcvr, iFilter);
		
		cdt = new CountDownTimer(2000, 1000) {
			public void onTick(long millisUntilFinished) {}
			
			
			public void onFinish() {		
				
				
				
	            Intent i = new Intent();
	            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.SurfaceOverlay");
	            startActivity(i);
	            finish();
				return;
			}
		};
		cdt.start();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		cdt.cancel();
		unregisterReceiver(rcvr);
	}

	class Rcvr extends BroadcastReceiver{

		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
				cdt.cancel();
				Log.i("BROADCAST","Screen OFF");
				Intent myIntent = new Intent(Splash.this, CamStandby.class);
				Splash.this.startActivity(myIntent);
				Splash.this.finish();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				Log.i("BROADCAST","Screen ON");
			}
		}
	}

}