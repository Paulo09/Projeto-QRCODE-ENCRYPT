package com.pagemark.peltareader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
/*   */


public class About extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	    setContentView(R.layout.about);
		
	    TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String id = tManager.getDeviceId();

//	    Intent i = getIntent();
//	    Bundle b = i.getExtras();
//	    String newText = b.getString("BARCODE_CONTENT");

	    final TextView txtResult = (TextView) findViewById(R.id.txt_about_txt);

	    String buildDate = "";
	    try{
			PackageManager pm = getPackageManager();
//			ApplicationInfo appInfo = pm.getApplicationInfo("com.pagemark.peltareader", PackageManager.GET_META_DATA); 
			PackageInfo pkgInfo = pm.getPackageInfo("com.pagemark.peltareader", PackageManager.GET_META_DATA);
//			String appFile = appInfo.sourceDir;
//			long installed = new File(appFile).lastModified();
//			SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd/yyyy HH:mm:ss");
//			buildDate = sdf.format(new Date(installed));
			buildDate = pkgInfo.versionName;
	    }catch(Exception ex)
	    {}
		
	    txtResult.setText("iVerifyIT Version " + buildDate + " installed on phone id: " + id);

	    txtResult.setMovementMethod(new ScrollingMovementMethod());
//	    txtResult.setText(newText);

/*	    Button btnBack = (Button) findViewById(R.id.btn_bck_about);
	    btnBack.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	            Intent i = new Intent();
//	            i.putExtra("ARRIVING_FROM", "screen 1");
	            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Settings");
	            startActivity(i);
	        }
	    });
	    
	    Button btnExit = (Button) findViewById(R.id.btn_exit_about);
	    btnExit.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		finish();
	    	}
	    });
*/	    
	}

	protected void onPause() {
		super.onPause();
	}
/*	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	finish();
            Intent i = new Intent();
//            i.putExtra("ARRIVING_FROM", "screen 1");
            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Settings");
            startActivity(i);
            return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
*/
}