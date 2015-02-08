package com.alamaanah.buroojquizzzics;

import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class QuizActivity extends Activity implements OnClickListener {

	//intent variables
	String type;
	int level;
	//score related variables
	final Handler handler = new Handler();
	int timerDelayInMillis = 1000;
	int penalty, penaltyPerMistake = 3;
	float score;
	int timeLimit;
	
	//UI Variables
	TextView txtScore;
	//TextView txtPenalty;
	TextView txtQuestion;
	TextView txtLevel, txtType;
	Button op1, op2, op3, op4;
	ProgressBar scoreBar;
	int currentColor = 0;
	
	
	//questionRelated
	int currentQuestion;
	int currentAnswer = 1;
	boolean isComplete = false;
	Question qList[];
	
	// Database
	DatabaseHandler dbHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//opening transition animations
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
	    
		setContentView(R.layout.activity_quiz);
		
		level = getIntent().getIntExtra("level", 1);
		type = getIntent().getStringExtra("type");
		timeLimit = Integer.parseInt(getResources().getString(R.string.time_limit));
		
		dbHandler = new DatabaseHandler(this);
		qList = dbHandler.getQuestions(type, level);
		
		getUiElements();
		
		score = 0;
		
		showInstruction();
		
		currentQuestion = 0;
		
		changeQuestion();
		
		
		
	}

	private void showInstruction() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if(preferences.getBoolean("show_instructions", true)){
			//Open Dialog Box
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
			
				// set title
			alertDialogBuilder.setTitle("Instructions");
	 
				// set dialog message
			alertDialogBuilder
				.setMessage(Html.fromHtml("You must complete the quiz in less than <b>" + timeLimit + "</b> seconds to win this level.<br><br>" +
						"For every wrong answer, <b>"+penaltyPerMistake+ " seconds</b> will be added to your timer.<br /><br />" +
								"All the best!"))
				.setCancelable(true);
			
			alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int id) {
					startTimer();
					dialog.dismiss();
				}
			  });
			alertDialogBuilder.setNegativeButton("Do not show this again", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int id) {
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
				  SharedPreferences.Editor editor = preferences.edit();
				  editor.putBoolean("show_instructions", false);
				  editor.commit();
					dialog.dismiss();
					startTimer();
				}
			  });
			alertDialogBuilder.create().show();
		}
		else{
			startTimer();
		}
		
	}
	public String arabify(String option){
		Resources r = getResources();
		option = option.replace("(SAW)", r.getString(R.string.SAW));
		option = option.replace("(RA)", r.getString(R.string.RA));
		option = option.replace("(AS)", r.getString(R.string.AS));
		option = option.replace("(RAA)", r.getString(R.string.RAA));
		return option;
	}
	
	private void changeQuestion() {
		currentQuestion++;
		if(currentQuestion > qList.length){
			endOfLevel();
		}
		else{
			//revert colors of buttons
			op1.setTextColor(Color.BLACK);
			op2.setTextColor(Color.BLACK);
			op3.setTextColor(Color.BLACK);
			op4.setTextColor(Color.BLACK);
			
			op1.setBackgroundResource(R.drawable.main_button);
			op2.setBackgroundResource(R.drawable.main_button);
			op3.setBackgroundResource(R.drawable.main_button);
			op4.setBackgroundResource(R.drawable.main_button);
			
			
			int questionIndex = currentQuestion - 1;
			String questionText = arabify(qList[questionIndex].qText);
			txtQuestion.setText("Q"+ currentQuestion + ". " + questionText);
			
			//resize question based on length of question
			if(questionText.length() < 100)
				txtQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
			else if(questionText.length() < 150)
				txtQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			else 
				txtQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			
			String options[] = {
				qList[questionIndex].op1,
				qList[questionIndex].op2,
				qList[questionIndex].op3,
				qList[questionIndex].op4
			};
			
			currentAnswer = randomizeOptions(options, qList[questionIndex].answerIndex - 1);
			
			int i = 0;
			op1.setText(arabify(options[i++]));
			op2.setText(arabify(options[i++]));
			op3.setText(arabify(options[i++]));
			op4.setText(arabify(options[i++]));
			
			op1.setOnClickListener(this);
			op2.setOnClickListener(this);
			op3.setOnClickListener(this);
			op4.setOnClickListener(this);
		}
	}
	
	public int randomizeOptions(String options[], int answerIndex){
		String correctAnswer = options[answerIndex];
		shuffleArray(options);
		answerIndex = Arrays.asList(options).indexOf(correctAnswer);
		return answerIndex + 1; //because its 1 indexed
	}
	
	// Implementing Fisher–Yates shuffle
	  static void shuffleArray(String[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      String a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
	@Override
	protected void onPause()
	{
		super.onPause();
	    //closing transition animations
	    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}
	private class ImageGetter implements Html.ImageGetter {

	    public Drawable getDrawable(String source) {
	        int id;
	        
	    	
	    	id = R.drawable.logo;
	        Drawable d = getResources().getDrawable(id);
	        d.setBounds(0,0,100,70);
	        return d;
	    }
	};
	private void endOfLevel() {
		isComplete = true;
		float finalScore = score;
		//float realTime = score - penalty;
		int timeLimit = Integer.parseInt(getResources().getString(R.string.time_limit));
		int scoreResult = dbHandler.addScore(type, level, finalScore);
		
		//show score
		String message;
		boolean goToNextLevel = true;
		if(finalScore > timeLimit){
			message = "You failed to complete the quiz in less than " + timeLimit + " seconds :(\n\n" +
					"Give it another try :)";
			goToNextLevel = false;
		}
		else if(scoreResult == 3 || scoreResult == 2){ //scoreResult = Unlock + beat or just new insert + Unlock
			//inserted a new record < timeLimit
			message = "Alhamdulillaah ... You unlocked the next level.";
		}
		else{ //Beat old unlocked record
			message = "Mashaa Allaah... You beat your previous record!";
		}
		
		
		String finalScoreMessage = "You final score is <b>" + Math.round(finalScore) + "</b>";
		if(penalty > 0)
			finalScoreMessage += " after a penalty of <b>" + penalty + "s</b>";
		
		//check if nextLevel exists
		int maxLevel = dbHandler.getMaxLevelForType(type);
		boolean nextLevelExists = true;
		if(level == maxLevel && goToNextLevel){
			nextLevelExists = false;
			message = "SubhaanAllaah... You've completed all the levels for this difficulty level.";
		}
		
		
		//Open Dialog Box
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
			alertDialogBuilder.setTitle("Quiz Complete");
 
			// set dialog message
			alertDialogBuilder
				.setMessage(Html.fromHtml(finalScoreMessage + "<br /><br />" + message))
				.setCancelable(false);
			
			if(goToNextLevel && nextLevelExists){	
				alertDialogBuilder.setPositiveButton("Go To Next Level",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
						intent.putExtra("type", type);
						intent.putExtra("level", level + 1);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
					}
				  });
			}
			else if(goToNextLevel && !nextLevelExists){
				alertDialogBuilder.setPositiveButton("Back To Difficulty Menu",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(QuizActivity.this, TypesActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
					}
				  });

			}
			
			if(!goToNextLevel){
				alertDialogBuilder.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
						intent.putExtra("type", type);
						intent.putExtra("level", level);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						
					}
				  });
				
			  alertDialogBuilder.setNegativeButton("Back To Difficulty Menu",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(QuizActivity.this, TypesActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});
			  
			}
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		
	}

	public void getUiElements(){
		txtScore = (TextView) findViewById(R.id.txtScore);
		//txtPenalty = (TextView) findViewById(R.id.txtPenalty);
		txtQuestion = (TextView) findViewById(R.id.txtQuestion);
		txtLevel = (TextView) findViewById(R.id.tvLevel);
		txtType = (TextView) findViewById(R.id.tvType);
		scoreBar = (ProgressBar) findViewById(R.id.scoreBar);
		
		
		op1 = (Button) findViewById(R.id.op1);
		op2 = (Button) findViewById(R.id.op2);
		op3 = (Button) findViewById(R.id.op3);
		op4 = (Button) findViewById(R.id.op4);
		
		//Defaults
		//txtPenalty.setText("Penalty: 0s");
		txtLevel.setText("LEVEL " + level);
		txtType.setText(type);
		scoreBar.setMax(Integer.parseInt(getResources().getString(R.string.time_limit)));
		
		Typeface fontObelix = Typeface.createFromAsset(getAssets(), "fonts/ObelixPro.ttf");
		txtLevel.setTypeface(fontObelix);
		txtType.setTypeface(fontObelix);
		txtScore.setTypeface(fontObelix);
		
		Typeface fontGabriela = Typeface.createFromAsset(getAssets(), "fonts/Exo.ttf");
		txtQuestion.setTypeface(fontGabriela);
		
		Typeface fontAcme = Typeface.createFromAsset(getAssets(), "fonts/Exo.ttf");
		
		
		op1.setTypeface(fontAcme);
		op2.setTypeface(fontAcme);
		op3.setTypeface(fontAcme);
		op4.setTypeface(fontAcme);
		
	}
	
	@Override
	public void onBackPressed(){
		//Open Dialog Box
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
		//alertDialogBuilder.setTitle("Quit Quiz?");
 
			// set dialog message
		alertDialogBuilder
			.setMessage("Are you sure you want to quit this level?")
			.setCancelable(true);
		
		alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int id) {
				Intent intent = new Intent(QuizActivity.this, LevelsActivity.class);
				intent.putExtra("type", type);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		  });
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int id) {
				dialog.dismiss();
			}
		  });
		alertDialogBuilder.create().show();
		return;
	}
	
	public void startTimer(){
		handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				 if(!isComplete){
					 score += timerDelayInMillis/1000.0f;
					 int percentage = Math.round(score/timeLimit * 100);
					 txtScore.setText("Time: " + Math.round(score));
					 scoreBar.setProgress(Math.round(score));
					  
					 if(currentColor == 0 && percentage > 40){
						 currentColor++;
						 scoreBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_orange));
					 }
					 else if(currentColor == 1 && percentage > 80){
							 currentColor++;
							 scoreBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_red));
					 }
					 handler.postDelayed(this, timerDelayInMillis);
				 }
		  }
		}, timerDelayInMillis);
	}

	@Override
	public void onClick(View v) {
		int selectedOption = 1;
		Button b = (Button) findViewById(v.getId());
		switch(v.getId()){
			case R.id.op1: selectedOption = 1; break;
			case R.id.op2: selectedOption = 2; break;
			case R.id.op3: selectedOption = 3; break;
			case R.id.op4: selectedOption = 4; break;
		}
		if(selectedOption != currentAnswer){
			b.setTextColor(Color.WHITE);
			b.setBackgroundResource(R.drawable.button_red);
			penalty += penaltyPerMistake;
			score += penaltyPerMistake;
			//txtPenalty.setText("Penalty: " + penalty + "s");
		}
		else{
			b.setBackgroundResource(R.drawable.button_green);
			handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
					 changeQuestion();
			  }
			}, 500);
		}
		
	}
	
}
