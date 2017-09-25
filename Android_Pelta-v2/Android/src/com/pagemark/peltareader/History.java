package com.pagemark.peltareader;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class History extends Activity implements OnClickListener {

	private ListView listview;
	private ArrayList<HistoryItem> mListItem;
	private ArrayList<String> checkedItems = new ArrayList<String>(); // holds primary key for items marked for deletion
	private boolean itemChecked[];	// remembers the checkbox positions which are marked for deletion, required to overcome view recycling prob
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hide the window title.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.history);
		listview = (ListView) findViewById(R.id.listview_history);

		Button btnDel = (Button) findViewById(R.id.btn_del_history);
		btnDel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String clause = "";
				if(checkedItems.size() == 1){
					clause = " = " + checkedItems.get(0);
				}
				else
				{
					clause = " IN (" + checkedItems.get(0);
					for(int i = 1; i <= checkedItems.size() - 1; i++){
						clause += "," + checkedItems.get(i);
					}
					clause += ") ";
				}
				SQLiteDatabase peltaDB = openOrCreateDatabase(DBHelper.DB_NAME, MODE_PRIVATE, null);
				peltaDB.delete(DBHelper.HISTORY_TABLE," pkey " + clause, null);
				
				finish();
				Intent myIntent = new Intent(History.this, History.class);
				History.this.startActivity(myIntent);
			}
		});

/*	    Button btnExit = (Button) findViewById(R.id.btn_exit_history);
	    btnExit.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		finish();
	    	}
	    });
	    
	    Button btnMenu = (Button) findViewById(R.id.btn_menu_history);
	    btnMenu.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		Intent myIntent = new Intent(History.this, Settings.class);
	    		History.this.startActivity(myIntent);
	    		finish();
	    	}
	    });
*/	
	}

	public void onResume() {
		super.onResume();

		SQLiteDatabase peltaDB = this.openOrCreateDatabase(DBHelper.DB_NAME, MODE_PRIVATE, null);
	    peltaDB.execSQL(" CREATE TABLE IF NOT EXISTS " + DBHelper.HISTORY_TABLE + " (pkey INTEGER PRIMARY KEY, ovmessage VARCHAR, covmessage VARCHAR, scandate NUMERIC); ");
	    Cursor cursor = peltaDB.rawQuery("SELECT * FROM " + DBHelper.HISTORY_TABLE + " ORDER BY scandate DESC ", null);
		itemChecked = new boolean[cursor.getCount()];
		mListItem = HistoryItem.getItems(cursor);
		listview.setAdapter(new ListAdapter(History.this, R.id.listview_history, mListItem));

		cursor.close();
		peltaDB.close();
	}

	public void onClick(View v) {

	}

	private class ListAdapter extends ArrayAdapter<HistoryItem> { 
		private ArrayList<HistoryItem> mList;
		
		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<HistoryItem> list) {
			super(context, textViewResourceId, list);
			this.mList = list;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final Button btnDel = (Button) findViewById(R.id.btn_del_history);
			
			try {
				if (view == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = vi.inflate(R.layout.history_item, null); 
				}
				final HistoryItem listItem = mList.get(position);
				if (listItem != null) {
					TextView tv = (TextView) view.findViewById(R.id.tv_msg); 
					tv.setText(listItem.getDispMsg());
					tv.setOnClickListener(new OnClickListener() {
						public void onClick(View arg0) {
							Intent myIntent = new Intent(History.this, ShowResults.class);
//							myIntent.putExtra("BARCODE_CONTENT", listItem.getFullMsg());
							myIntent.putExtra("BARCODE_CONTENT_OV", listItem.getOvMsg());
							myIntent.putExtra("BARCODE_CONTENT_COV", listItem.getCovMsg());
							myIntent.putExtra("SAVE_TO_DB", "false");
							myIntent.putExtra("PKEY","" + listItem.getPkey());
							myIntent.putExtra("CALLER", "History");
							startActivity(myIntent);
						}
					});

					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_hist_item);
					cb.setTag("pkey" + listItem.getPkey());
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							itemChecked[getPosition(listItem)] = isChecked;
							if(isChecked){
								checkedItems.add("" + listItem.getPkey());
								btnDel.setEnabled(true);
							} else {
								checkedItems.remove("" + listItem.getPkey());
								if(checkedItems.size() <= 0){
									btnDel.setEnabled(false);
								}
							}
						}
					});
					cb.setChecked(itemChecked[position]);
				}
			} catch (Exception e) {
//				Log.i(History.ListAdapter.class.toString(), e.getMessage());
			}
			return view;
		}
	}
}