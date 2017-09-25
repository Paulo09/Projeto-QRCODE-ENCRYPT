package com.pagemark.peltareader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings.Global;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ShowResults extends Activity {
	
	private long pkey;
	private String caller;
	String texto = null;
	int check1=0;int check2=0;int position=0;
	
	           
	
	public String replace(String str, int index, char replace){      
	    if(str==null){ 
	        return str; 
	    }else if(index<0 || index>=str.length()){ 
	        return str; 
	    } 
	    char[] chars = str.toCharArray(); 
	    chars[index] = replace; 
	    return String.valueOf(chars);        
	} 
	
	public String FixUpURL(String inputurl)
	{
		URL url = null;
    	
    	
    	try {  url = new URL(inputurl); } 
    	catch (MalformedURLException e) 
    	{
    	
    	/* invalid URL */ 
    	}
    	if(url != null)
    	{
    	
    		String host = url.getHost();
    	
    		// Android has problems with upper case links so
    		// convert hostname and HTTP: to lowercase
    		int index = inputurl.indexOf("HTTP:");
    		if(index >= 0)
    		{
    		for(int ii = 0; ii < 4; ii++)
    		{
    			char c = inputurl.charAt(ii+index);
    		
    			char lower = Character.toLowerCase(c);
    			inputurl = replace(inputurl,ii+index,lower);
    			 
    		}
    		}
    		index = inputurl.indexOf(host);
    		if(index >= 0)
    		{
    		for(int ii = 0; ii < host.length(); ii++)
    		{
    			char c = inputurl.charAt(ii+index);
    		
    			char lower = Character.toLowerCase(c);
    			inputurl = replace(inputurl,ii+index,lower);
    			 
    		}
    		}
    	
    	}
    	return inputurl;	
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

	    setContentView(R.layout.show_results);

	    Intent i = getIntent();
	    Bundle b = i.getExtras();
	    String ovText = b.getString("BARCODE_CONTENT_OV");
	    String covText = b.getString("BARCODE_CONTENT_COV");
	    covText = covText.trim();
	    
	    final boolean save2DB = Boolean.parseBoolean(b.getString("SAVE_TO_DB"));
	    pkey = Long.parseLong(b.getString("PKEY"));
	    caller = b.getString("CALLER");

	    final Config conf = Config.getInstance(this);
	    final TextView txtScannedOv = (TextView) findViewById(R.id.txt_scanned_ov);   
	  

	    if( conf.getFollowURL() == 1){    	
	    	
	    	ovText = FixUpURL(ovText);
	    		    		
	    	txtScannedOv.setAutoLinkMask(Linkify.WEB_URLS);
	    	txtScannedOv.setClickable(true); 

	    }
	    
	    txtScannedOv.setText("\n\n\n" + ovText.replaceAll("[^\\w]000003","").replaceAll("[^\\w]000004","").replaceAll("[^\\w]000005","").replaceAll("[^\\w]000006",""));//.replaceAll("[^\\w]000003",""));
	        
	    ///Paulo Castro - 29/11/2016
	    // Verificar
	    
	    Log.v("Linha Log:133","Valor:"+ovText.replaceAll("[^\\w]000003","").replaceAll("[^\\w]000004","").replaceAll("[^\\w]000005","").replaceAll("[^\\w]000006",""));//.replaceAll("[^\\w]000003",""));
	    
	    if(txtScannedOv != null)
	    {
	    	 Context contexto = getApplicationContext();
	    	 int duracao = Toast.LENGTH_SHORT;
	        
	    	check1=ovText.length();
	 		check2=ovText.replaceAll("[^\\w]","").replaceAll("0","").length();
	         
	 	    String text  = "XXXXXX"+ovText.replaceAll("[^\\w]","");
			String match ="000003";
			position = text.indexOf(match);
			
	         if(position>0){	        	 	        	 
	        	 texto = "SELO AUTÊNTICO ";}
	         else{
	        	 texto="SELO FALSIFICADO";}	         
	         
        	 Toast toast = Toast.makeText(contexto, texto,duracao);
	         toast.show();
	    }
	    //////////////////////////
	    //if(covText != null && covText.length() > 0){ //Paulo Castro
		
	    if((check1 != check2) && (position > 0)){
	    	
	    	TextView txtLblCov = (TextView) findViewById(R.id.txt_covert_label);
	    	txtLblCov.setVisibility(TextView.VISIBLE);

		    TextView txtScannedCov = (TextView) findViewById(R.id.txt_scanned_cov);
		    txtScannedCov.setVisibility(TextView.VISIBLE);
		    if( conf.getFollowURL() == 1){
		    	covText = FixUpURL(covText);
		    	txtScannedCov.setAutoLinkMask(Linkify.WEB_URLS);
		    	txtScannedCov.setClickable(true);
		    } else
		    {
		    	 txtScannedCov.setVisibility(TextView.VISIBLE);

		    }
		    covText=ovText.replaceAll("[^\\w]","Z").replaceAll("0","X").replaceAll("1","H").replaceAll("2","D").replaceAll("3","T");
			txtScannedCov.setText(covText);
	    }
	    else
	    {   
	    	/* Criação: Paulo Castro - Data: 30/11/2016
	    	 * Celo QRCODE - FALSIFICADO - Emite um sinal Sonoro. 
	    	 */
	    	covText="SELO FALSIFICADO";
	    	TextView txtScannedCov = (TextView) findViewById(R.id.txt_scanned_cov);
		    txtScannedCov.setVisibility(TextView.VISIBLE);
		    txtScannedCov.setText(covText);	    	
	    	
	    	if(conf.getPlaySoundOnRead() == 1){
		    	AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
		    	float vol = am.getStreamVolume(AudioManager.STREAM_RING);
		    	vol = vol / am.getStreamMaxVolume(AudioManager.STREAM_RING);		    	
		    	MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
		    	mp.setVolume(vol, vol);		    	
		    	mp.start();	
	    	}
	    	
	    }

	    if(save2DB){
	    	
	    	//play sound and vibrate when barcode is read successfully
	    	//Essa parte do código é responsável por acionar - O som e a vibração quando o APP consegue ler o QRCODE.
	    	//Paulo Castro - 30/11/2016
	    	
	    	if(conf.getVibrateOnRead() == 1)
	    		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);  	
	    	

	    	//enter the results to the database only if it is freshly read
		    Date now = Calendar.getInstance().getTime();
		    SQLiteDatabase peltaDB = this.openOrCreateDatabase(DBHelper.DB_NAME, MODE_PRIVATE, null);
		    peltaDB.execSQL(" CREATE TABLE IF NOT EXISTS " + DBHelper.HISTORY_TABLE + " (pkey INTEGER PRIMARY KEY, ovmessage VARCHAR, covmessage VARCHAR, scandate NUMERIC); ");
		    ContentValues cv = new ContentValues();
		    cv.put("ovmessage", ovText);
		    cv.put("covmessage", covText);
		    cv.put("scandate", now.getTime());
		    pkey = peltaDB.insert(DBHelper.HISTORY_TABLE, null, cv);
		    peltaDB.close();
	    }

//	    stopService(this.getIntent());
/*
	    Button btnHist = (Button) findViewById(R.id.btn_hist_showresult);
	    btnHist.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            Intent i = new Intent();
//	            i.putExtra("ARRIVING_FROM", "screen 1");
	            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.History");
	            startActivity(i);
	            finish();
	        }
	    });

	    Button btnMenu = (Button) findViewById(R.id.btn_menu_showresult);
	    btnMenu.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		Intent i = new Intent();
//	            i.putExtra("ARRIVING_FROM", "screen 1");
	    		i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Settings");
	    		startActivity(i);
	    		finish();
	    	}
	    });
*/
	    Button btnDel = (Button) findViewById(R.id.btn_del_showres);
	    btnDel.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    	    SQLiteDatabase peltaDB = openOrCreateDatabase(DBHelper.DB_NAME, MODE_PRIVATE, null);
	    	    peltaDB.delete(DBHelper.HISTORY_TABLE," pkey = " + pkey, null);
//	    	    if(save2DB){
//			    	Intent myIntent = new Intent(ShowResults.this, SurfaceOverlay.class);
//			    	ShowResults.this.startActivity(myIntent);
//			    	finish();
//	    	    } else {
//			    	Intent myIntent = new Intent(ShowResults.this, History.class);
//			    	ShowResults.this.startActivity(myIntent);
//			    	finish();
//	    	    }
	    	    peltaDB.close();
	    	    finish();
	    	}
	    });
	    
//	    TableLayout tblLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
//	    TableRow tblRow	 = new TableRow(this);
//	    Button btn = new Button(this);
//	    btn.setText("Open 3rd party app");
//	    TextView tv = new TextView(this);
//	    tblRow.addView(tv);
//	    tblRow.addView(btn);
//	    tblLayout1.addView(tblRow);

	}

	protected void onPause() {
		super.onPause();
	}

/*	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	finish();
            Intent i = new Intent();
//            i.putExtra("ARRIVING_FROM", "screen 1");
            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader." + caller);
            startActivity(i);
            return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
*/
}