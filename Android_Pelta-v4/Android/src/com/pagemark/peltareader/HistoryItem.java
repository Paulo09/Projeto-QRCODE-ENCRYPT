package com.pagemark.peltareader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;

public class HistoryItem {
	private int pkey;
	private String ovmessage;
	private String covmessage;
	private String dispmessage;
	private String description;

	public int getPkey() { return pkey;	}
	public void setPkey(int pkey) { this.pkey = pkey; }
	
	public String getOvMsg() { return ovmessage;	}
	public void setOvMsg(String ovmessage) { this.ovmessage = ovmessage; }
	
	public String getCovMsg() { return covmessage;	}
	public void setCovMsg(String covmessage) { this.covmessage = covmessage; }
	
	public String getDispMsg() { return dispmessage;	}
	public void setDispMsg(String dispmessage) { this.dispmessage = dispmessage; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public static ArrayList<HistoryItem> getItems(Cursor cursor) {
		ArrayList<HistoryItem> itemList = new ArrayList<HistoryItem>();
		HistoryItem item;

	   int colPkey = cursor.getColumnIndex("pkey");
	   int colOvMsg = cursor.getColumnIndex("ovmessage");
	   int colCovMsg = cursor.getColumnIndex("covmessage");
	   int colScanDate = cursor.getColumnIndex("scandate");

	   cursor.moveToFirst();
	   if(cursor.getCount() > 0){
		   do{
				String strOvMsg = cursor.getString(colOvMsg);
				String strCovMsg = cursor.getString(colCovMsg);
				String strFullMsg = strOvMsg;

				if(strCovMsg != null && strCovMsg.length() > 0){
					strFullMsg += "; " + strCovMsg;
				}

				int intPkey = cursor.getInt(colPkey);
				long intScanDate = cursor.getLong(colScanDate);

				SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd/yyyy HH:mm:ss, Z");
				String scanDate = sdf.format(new Date(intScanDate));

				item = new HistoryItem();
				item.setPkey(intPkey);

				String dispStr = strFullMsg;
				if(dispStr.length() >= 35){
					dispStr = dispStr.substring(0, 35);
				}
				item.setOvMsg(strOvMsg);
				item.setCovMsg(strCovMsg);
				item.setDispMsg(dispStr + "\n" + scanDate);
				itemList.add(item);
		   } while(cursor.moveToNext());
	   }
		return itemList;
	}
}