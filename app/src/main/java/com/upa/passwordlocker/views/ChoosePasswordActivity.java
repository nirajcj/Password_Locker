package com.upa.passwordlocker.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.utils.EncryptionHelper;
import com.upa.passwordlocker.R;

public class ChoosePasswordActivity extends Activity implements OnClickListener {

	private static final String PREFS_NAME = "app_pref";
	private static final String pass = "password";
	private static final String PREFS_NAME1 = "MyPrefsFile";

	private String s = "";

	private ToneGenerator toneGenerator;

	MaterialButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12;
	MaterialTextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if(!prefs.getBoolean("firstTime", false)) {

			if(!prefs.getBoolean("version", false)) {

				PackageInfo packageInfo;

				try {

					packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					String versionName = packageInfo.versionName;

					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("version", true);
					editor.putString("version", versionName);
					editor.apply();
				}
				catch (Exception e) {}
			}

			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("firstTime", true);
			editor.apply();

			try {

				SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

				String securityAnswer = preferences.getString("question", null);
				String answer_hashed = EncryptionHelper.generateStrongPasswordHash(securityAnswer);

				String storedPass = preferences.getString("password", null);
				String pass_hashed = EncryptionHelper.generateStrongPasswordHash(storedPass);

				editor = preferences.edit();
				editor.putString(pass, pass_hashed);
				editor.putString("question", answer_hashed);
				editor.commit();

			}
			catch (Exception e) {}
		}

		requestPass();

		super.onCreate(savedInstanceState);

		Typeface heading = Typeface.createFromAsset(getAssets(), "fonts/yoyo.otf");
		Typeface allages = Typeface.createFromAsset(getAssets(), "fonts/babylove.ttf");

		setContentView(R.layout.activity_password_choose);

		toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF,70);

		button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		button3 = findViewById(R.id.button3);
		button4 = findViewById(R.id.button4);
		button5 = findViewById(R.id.button5);
		button6 = findViewById(R.id.button6);
		button7 = findViewById(R.id.button7);
		button8 = findViewById(R.id.button8);
		button9 = findViewById(R.id.button9);
		button10 = findViewById(R.id.button10);
		button11 = findViewById(R.id.button11);
		button12 = findViewById(R.id.button12);

		textView = findViewById(R.id.textView2);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		button10.setOnClickListener(this);
		button11.setOnClickListener(this);
		button12.setOnClickListener(this);

		MaterialTextView myTextView = findViewById(R.id.textView1);
		myTextView.setTypeface(heading);

		button1.setTypeface(allages);
		button2.setTypeface(allages);
		button3.setTypeface(allages);
		button4.setTypeface(allages);
		button5.setTypeface(allages);
		button6.setTypeface(allages);
		button7.setTypeface(allages);
		button8.setTypeface(allages);
		button9.setTypeface(allages);
		button11.setTypeface(allages);
		button10.setTypeface(allages);
		button12.setTypeface(allages);
	}

	private void requestPass() {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);

		if(settings.getBoolean(pass, true)) {
			settings.edit().putBoolean(pass, false).apply();
			return;
		}

		Intent i = new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
		startActivity(i);
		finish();
	}

	private void requestPass2() {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);

		if(settings.getBoolean(pass, true)) {
			return;
		}

		Intent i = new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
		startActivity(i);
		finish();
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {

			case R.id.button1:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "1";
				textView.setText(s);
				break;

			case R.id.button2:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "2";
				textView.setText(s);
				break;

			case R.id.button3:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "3";
				textView.setText(s);
				break;

			case R.id.button4:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "4";
				textView.setText(s);
				break;

			case R.id.button5:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "5";
				textView.setText(s);
				break;
			case R.id.button6:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "6";
				textView.setText(s);
				break;
			case R.id.button7:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "7";
				textView.setText(s);
				break;

			case R.id.button8:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "8";
				textView.setText(s);
				break;

			case R.id.button9:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "9";
				textView.setText(s);
				break;

			case R.id.button10:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				if(s.length()>0){
					s = s.substring(0, s.length() - 1);
					textView.setText(s);
				}
				break;

			case R.id.button11:
				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s += "0";
				textView.setText(s);
				break;

			case R.id.button12:

				toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 120);

				try {

					String s_hashed = EncryptionHelper.generateStrongPasswordHash(s);

					SharedPreferences settings;
					SharedPreferences.Editor editor;
					settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

					editor = settings.edit();
					editor.putString(pass, s_hashed);
					editor.apply();
				}
				catch(Exception e) {}

				requestPass2();

				Intent i=new Intent("com.upa.passwordlocker.QUESTIONACTIVITY");
				startActivity(i);
				finish();
				break;

		}

	}

}
