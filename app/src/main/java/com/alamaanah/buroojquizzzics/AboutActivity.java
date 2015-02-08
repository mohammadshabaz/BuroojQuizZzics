package com.alamaanah.buroojquizzzics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		    
		setContentView(R.layout.activity_about_brooj);
		TextView about = (TextView)findViewById(R.id.about);
		
		about.setText(Html.fromHtml("<div style=\"padding:0;border-width:0\" align=\"justify\"><font face=\"Skranji,cursive\" size=\"2\"><span style=\"font-size:18px\"><font color=\"#80A62D\" face=\"inherit\" size=\"4\"><span style=\"font-size:25px;background-color:transparent\">Burooj Realization</span></font>,an Islamic educational research centre was established in <font color=\"orangered\" face=\"inherit\" size=\"6\"><span style=\"font-size:50px;background-color:transparent\">2004</span></font> by a group of concerned Muslims united by their belief, with conviction inthe <font color=\"#730236\" face=\"inherit\" size=\"5\"><span style=\"font-size:35px;background-color:transparent\">Qur'an</span></font> and the <font color=\"#27C1F7\" face=\"inherit\" size=\"5\"><span style=\"font-size:35px;background-color:transparent\">Sunnah</span></font> .</span></font></div><div style=\"padding:0;border-width:0\" align=\"justify\"><font face=\"Skranji,cursive\" size=\"2\"><span style=\"font-size:18px\">Burooj has been a trailblazer in the <font color=\"#27C1F7\" face=\"inherit\" size=\"5\"><span style=\"font-size:30px;background-color:transparent\">Islamic Education</span></font> firmament catering to the much needed educational needs of children towards <font color=\"#D91E63\" face=\"inherit\" size=\"4\"><span style=\"font-size:25px;background-color:transparent\">'Deen'</span></font>. Burooj is an educational researchinstitute recognized as a <font color=\"#80A62D\" face=\"inherit\" size=\"3\"><span style=\"font-size:20px;background-color:transparent\">non-profit registered trust</span></font> with more than <font color=\"#27C1F7\" face=\"inherit\" size=\"6\"><span style=\"font-size:40px;background-color:transparent\">75</span></font> centres across India and abroad. Burooj aims in bringing about <font color=\"#D91E63\" face=\"inherit\"><span style=\"background-color:transparent\">cataclysmic</span></font> changes in the field of Islamic education by widening the educational spectrum and providing beneficialknowledge of Islamic learning.</span></font></div><div style=\"padding:0;border-width:0\" align=\"justify\"><font face=\"Skranji,cursive\" size=\"2\"><span style=\"font-size:18px\"><a href=\"http://www.burooj.org\" target=\"_blank\">www.burooj.org</a></span></font></div><div style=\"padding:0;border-width:0\" align=\"justify\"><font face=\"Skranji,cursive\" size=\"2\"><span style=\"font-size:18px\">Facebook page - buroojasia</span></font></div></span>"));
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/gabriela.ttf");
		about.setTypeface(tf);
		Button visit = (Button) findViewById(R.id.btnVisit);
		visit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = "http://www.burooj.org";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}
	@Override
	  protected void onPause()
	  {
	    super.onPause();
	    //closing transition animations
	    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_brooj, menu);
		return true;
	}

}
