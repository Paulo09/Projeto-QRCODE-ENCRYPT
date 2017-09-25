package com.pagemark.peltareader;

import org.w3c.dom.Text;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.graphics.*;

public class Settings extends Activity
{
	 	
    private void onAboutClick(final View view) {
        final Intent intent = new Intent();
        intent.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.About");
        this.startActivity(intent);
    }
    
    private void onHelpClick(final View view) {
        final Intent intent = new Intent();
        intent.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Help");
        this.startActivity(intent);
    }
    
    private void onHistoryClick(final View view) {
        final Intent intent = new Intent();
        intent.putExtra("SHOWCROSS", false);
        intent.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.History");
        this.startActivity(intent);
    }
    
    private void onPeltaAdvSettingsClick(final View view) {
        final Intent intent = new Intent();
        intent.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.PeltaConfig");
        this.startActivity(intent);
    }
    
    private void onScanClick(final View view) {
        this.finish();
    }
    
    private void onUpdateClick(final View view) {
        final Intent intent = new Intent();
        intent.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.Updates");
        this.startActivity(intent);
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setFlags(1024, 1024);
        this.requestWindowFeature(1);
        this.setContentView(2130903049);
        final ImageButton imageButton = (ImageButton)this.findViewById(2131165229);
        imageButton.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onScanClick(view);
            }
        });
        final ImageButton imageButton2 = (ImageButton)this.findViewById(2131165230);
        imageButton2.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onPeltaAdvSettingsClick(view);
            }
        });
        final ImageButton imageButton3 = (ImageButton)this.findViewById(2131165234);
        imageButton3.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onHistoryClick(view);
            }
        });
        final ImageButton imageButton4 = (ImageButton)this.findViewById(2131165235);
        imageButton4.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onAboutClick(view);
            }
        });
        final TextView textView = (TextView)this.findViewById(2131165232);
        textView.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onScanClick(view);
            }
        });
        final TextView textView2 = (TextView)this.findViewById(2131165233);
        textView2.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onPeltaAdvSettingsClick(view);
            }
        });
        final TextView textView3 = (TextView)this.findViewById(2131165236);
        textView3.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onHistoryClick(view);
            }
        });
        final TextView textView4 = (TextView)this.findViewById(2131165237);
        textView4.setOnClickListener((OnClickListener)new OnClickListener() {
            public void onClick(final View view) {
                Settings.this.onAboutClick(view);
            }
        });
        
        /*
        imageButton.setOnTouchListener((OnClickListener)new OnClickListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    textView.setTypeface((Typeface)null, 1);
                }
                else if (motionEvent.getAction() == 1) {
                    textView.setTypeface((Typeface)null, 0);
                }
                else if (motionEvent.getAction() == 4) {
                    textView.setTypeface((Typeface)null, 0);
                }
                return false;
            }
        });
       
        imageButton2.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    textView2.setTypeface((Typeface)null, 1);
                }
                else if (motionEvent.getAction() == 1) {
                    textView2.setTypeface((Typeface)null, 0);
                }
                else if (motionEvent.getAction() == 4) {
                    textView2.setTypeface((Typeface)null, 0);
                }
                return false;
            }
        });
        imageButton3.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    textView3.setTypeface((Typeface)null, 1);
                }
                else if (motionEvent.getAction() == 1) {
                    textView3.setTypeface((Typeface)null, 0);
                }
                else if (motionEvent.getAction() == 4) {
                    textView3.setTypeface((Typeface)null, 0);
                }
                return false;
            }
        });
        imageButton4.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    textView4.setTypeface((Typeface)null, 1);
                }
                else if (motionEvent.getAction() == 1) {
                    textView4.setTypeface((Typeface)null, 0);
                }
                else if (motionEvent.getAction() == 4) {
                    textView4.setTypeface((Typeface)null, 0);
                }
                return false;
            }
        });*/
    }
    
    protected void onPause() {
        super.onPause();
    }
}
