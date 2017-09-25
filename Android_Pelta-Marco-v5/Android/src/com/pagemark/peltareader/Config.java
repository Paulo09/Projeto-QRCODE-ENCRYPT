package com.pagemark.peltareader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Config {
	private int READ_COV = 0;
	private String ENC_KEY = "";
	private String URL = "https://www.pelta.mobi/register";
	private int PLAY_SOUND_ON_READ = 1;
	private int VIBRATE_ON_READ = 1;
	private byte[] ARR_ENC_KEY = new byte[16];
	private int FOLLOW_URL = 0;
	
	private static Config conf;
	
	public int getReadCov(){
		return READ_COV;
	}

	public void setReadCov(int READ_COV){
		this.READ_COV = READ_COV;
	}
	
	public String getEncKey(){
		return ENC_KEY;
	}
	
	public void setEncKey(String ENC_KEY){
		this.ENC_KEY = ENC_KEY;
	}
	
	public String getUrl(){
		return URL;
	}
	
	public void setUrl(String URL){
		this.URL = URL;
	}
	
	public byte[] getArrEncKey(){
		byte[] arrKey = new byte[ENC_KEY.length()]; 
		for(int i = 0, j = 0; i <= ENC_KEY.length() - 1; i+=2, j++){
			arrKey[j] = (byte)Integer.parseInt(ENC_KEY.substring(i, i + 2), 16);
		}
		return arrKey;
	}

	public void setArrEncKey(int[] ARR_ENC_KEY){
		ENC_KEY = "";
		for(int i = 0; i <= 15; i++){
			String temp = Integer.toHexString(ARR_ENC_KEY[i]);
			if(temp.length() == 1) temp = "0" + temp;
			ENC_KEY += temp;
		}
	}

	public int getPlaySoundOnRead(){
		return PLAY_SOUND_ON_READ;
	}
	
	public void setPlaySoundOnRead(int PLAY_SOUND_ON_READ){
		this.PLAY_SOUND_ON_READ = PLAY_SOUND_ON_READ;
	}

	public int getVibrateOnRead(){
		return VIBRATE_ON_READ;
	}
	
	public void setVibrateOnRead(int VIBRATE_ON_READ){
		this.VIBRATE_ON_READ = VIBRATE_ON_READ;
	}

	public int getFollowURL(){
		return FOLLOW_URL;
	}

	public void setFollowURL(int FOLLOW_URL){
		this.FOLLOW_URL = FOLLOW_URL;
	}
	
	private Config(Context context){
	    try{
	    	SQLiteDatabase peltaDB = context.openOrCreateDatabase(DBHelper.DB_NAME, Context.MODE_PRIVATE, null);
	    	Cursor cursor = peltaDB.rawQuery("SELECT name FROM sqlite_master WHERE name='" + DBHelper.CONFIG_TABLE + "'", null);
	    	if(cursor.getCount() <= 0){
	    		peltaDB.execSQL(" CREATE TABLE IF NOT EXISTS " + DBHelper.CONFIG_TABLE + 
	    				" (pkey INTEGER PRIMARY KEY, enckey VARCHAR, url VARCHAR, readcovert INTEGER," +
	    				"  playsoundonread INTEGER, vibrateonread INTEGER, followurl INTEGER) ");

	    		ContentValues cv = new ContentValues();
			    cv.put("enckey", "");
			    cv.put("url", "https://www.pelta.mobi/register");
			    cv.put("readcovert", 0);
			    cv.put("playsoundonread", 1);
			    cv.put("vibrateonread", 1);
			    cv.put("followurl", 0);
			    peltaDB.insert(DBHelper.CONFIG_TABLE, null, cv);
			    			    
	    		cursor.close();
			    peltaDB.close();
	    	} else {
	    		cursor.close();
		    	cursor = peltaDB.rawQuery("SELECT * FROM " + DBHelper.CONFIG_TABLE, null);
		    	if(cursor.getCount() > 0){
		    		cursor.moveToFirst();
		    		int colIdxEncKey = cursor.getColumnIndex("enckey");
		    		int colIdxUrl = cursor.getColumnIndex("url");
		    		int colIdxReadCov = cursor.getColumnIndex("readcovert");
		    		int colIdxSound = cursor.getColumnIndex("playsoundonread");
		    		int colIdxVib = cursor.getColumnIndex("vibrateonread");
		    		int colIdxFol = cursor.getColumnIndex("followurl");
		    		READ_COV = cursor.getInt(colIdxReadCov);
		    		ENC_KEY = cursor.getString(colIdxEncKey);
		    		URL = cursor.getString(colIdxUrl);
		    		PLAY_SOUND_ON_READ = cursor.getInt(colIdxSound);
		    		VIBRATE_ON_READ = cursor.getInt(colIdxVib);
		    		FOLLOW_URL = cursor.getInt(colIdxFol);
		    	}
		    	cursor.close();
		    	peltaDB.close();
	    	}
	    } catch (Exception ex) {
	    	Log.e("CONFIG", ex.getMessage());
	    }
	}

	public static Config getInstance(Context context){
		if(conf == null) {
			conf = new Config(context);
		}
		return conf;
	}

	public void saveConfig(Context context){
		try{
			SQLiteDatabase peltaDB = context.openOrCreateDatabase(DBHelper.DB_NAME, android.content.Context.MODE_PRIVATE, null);
			
			ContentValues cv = new ContentValues();
		    cv.put("enckey", conf.ENC_KEY);
		    cv.put("url", conf.URL);
		    cv.put("readcovert", conf.READ_COV);
		    cv.put("playsoundonread", conf.PLAY_SOUND_ON_READ);
		    cv.put("vibrateonread", conf.VIBRATE_ON_READ);
		    cv.put("followurl", conf.FOLLOW_URL);
		    peltaDB.update(DBHelper.CONFIG_TABLE, cv, null, null);
		    peltaDB.close();
		} catch (Exception ex){
			Log.e("CONFIG", ex.getMessage());
		}
	}
}