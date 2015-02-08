 package com.alamaanah.buroojquizzzics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnClickListener {
	DatabaseHandler db;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//opening transition animations
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
	    setContentView(R.layout.activity_home);
	    context = this;
	    
		
		ImageButton b1 = (ImageButton) findViewById(R.id.button1);
        ImageButton b2 = (ImageButton) findViewById(R.id.button2);
        ImageButton b3 = (ImageButton) findViewById(R.id.button3);
        ImageButton b4 = (ImageButton) findViewById(R.id.btnCompany);
        
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        
        
    	db = new DatabaseHandler(context);
	    if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dbInitialized", false)){
    		db.clearDB();
    		//db.unlockAll(); // TODO remove after testing
	    	PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("dbInitialized", true).commit();
    		
    	}
	    
	    
		
        
    }
	protected void changeFonts(ViewGroup root) {
        
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ObelixPro.ttf");
        
        for(int i = 0; i <root.getChildCount(); i++) {
                View v = root.getChildAt(i);
                if(v instanceof TextView ) {
                        ((TextView)v).setTypeface(tf);
                } else if(v instanceof Button) {
                        ((Button)v).setTypeface(tf);
                } else if(v instanceof EditText) {
                        ((EditText)v).setTypeface(tf);
                } else if(v instanceof ViewGroup) {
                        changeFonts((ViewGroup)v);
                }
        }
        
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.button1:
				Intent intent = new Intent(HomeActivity.this, TypesActivity.class);
				startActivity(intent);
				break;
			case R.id.button2:
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				}
				break;
			case R.id.button3:
				Intent intent1 = new Intent (HomeActivity.this, AboutActivity.class);
				startActivity(intent1);
				break;
			case R.id.btnCompany:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://al-amaanah.com")));
				break;
				//this.finish();
			//default: Toast.makeText(HomeActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	  protected void onPause()
	  {
	    super.onPause();
	    //closing transition animations
	    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	  }
	@Override
	protected void onDestroy()
	  {
	    //db.close();
		super.onDestroy();
	    
	  }

	

}
