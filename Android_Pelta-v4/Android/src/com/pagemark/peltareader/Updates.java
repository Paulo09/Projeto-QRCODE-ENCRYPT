package com.pagemark.peltareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Updates extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	    setContentView(R.layout.updates);

//	    Intent i = getIntent();
//	    Bundle b = i.getExtras();
//	    String newText = b.getString("BARCODE_CONTENT");
//
//	    final TextView txtResult = (TextView) findViewById(R.id.txt_scanned_results);
//	    txtResult.setMovementMethod(new ScrollingMovementMethod());
//	    txtResult.setText(newText);
/*
	    Button btnBack = (Button) findViewById(R.id.btn_bck_update);
	    btnBack.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	            Intent i = new Intent();
//	            i.putExtra("ARRIVING_FROM", "screen 1");
	            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Settings");
	            startActivity(i);
	        }
	    });
	    
	    Button btnExit = (Button) findViewById(R.id.btn_exit_update);
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

/*	public boolean onKeyDown(int keyCode, KeyEvent event)  {
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