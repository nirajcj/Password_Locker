package com.upa.passwordlocker.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.R;
import com.upa.passwordlocker.utils.EncryptionHelper;
import com.upa.passwordlocker.utils.FileUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;


public class PasswordActivity extends Activity implements OnClickListener {

	private static final String TAG = PasswordActivity.class.getName();

	public static final String PREFS_NAME = "app_pref";
	public static final String pass = "password";

	private String s = "",  storedPass = "", value = "", question_value =  "", n_hashed;
	private int attempts=0;

	private ToneGenerator mp;

	MaterialButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13;
	MaterialTextView pinInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		FileUtils.writeToFile(getApplicationContext(), "go", "waitstate");
		FileUtils.writeToFile(getApplicationContext(), "123", "timestamp");

		Typeface heading = Typeface.createFromAsset(getAssets(), "fonts/yoyo.otf");
		Typeface all_ages = Typeface.createFromAsset(getAssets(), "fonts/babylove.ttf");

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		storedPass = settings.getString("password", null); //  password stored
		value = settings.getString("question", null); // security question answer
		question_value = settings.getString("question_value",null); // security question chosen

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);

		mp = new ToneGenerator(AudioManager.STREAM_DTMF,70);

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
		button13 = findViewById(R.id.button13);

		pinInput = findViewById(R.id.textView2);

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
		button13.setOnClickListener(this);

		button1.setTypeface(all_ages);
		button2.setTypeface(all_ages);
		button3.setTypeface(all_ages);
		button4.setTypeface(all_ages);
		button5.setTypeface(all_ages);
		button6.setTypeface(all_ages);
		button7.setTypeface(all_ages);
		button8.setTypeface(all_ages);
		button9.setTypeface(all_ages);
		button10.setTypeface(all_ages);
		button11.setTypeface(all_ages);
		button12.setTypeface(all_ages);

		MaterialTextView myTextView = (MaterialTextView)findViewById(R.id.textView1);
		myTextView.setTypeface(heading);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View v) {

		final View vi = getLayoutInflater().inflate(R.layout.answer_dialog, null);

		switch(v.getId()) {

			case R.id.button1:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="1";
				pinInput.setText(s);
				break;

			case R.id.button2:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="2";
				pinInput.setText(s);
				break;

			case R.id.button3:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="3";
				pinInput.setText(s);
				break;

			case R.id.button4:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="4";
				pinInput.setText(s);
				break;

			case R.id.button5:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="5";
				pinInput.setText(s);
				break;

			case R.id.button6:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="6";
				pinInput.setText(s);
				break;
			case R.id.button7:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="7";
				pinInput.setText(s);
				break;

			case R.id.button8:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="8";
				pinInput.setText(s);
				break;

			case R.id.button9:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				s+="9";
				pinInput.setText(s);
				break;

			case R.id.button10:
				mp.startTone(ToneGenerator.TONE_DTMF_1, 120);
				if(s.length()>0){
					s=s.substring(0,s.length()-1);
					pinInput.setText(s);}
				break;

			case R.id.button11:
				mp.startTone(ToneGenerator.TONE_DTMF_0, 120);
				s+="0";
				pinInput.setText(s);
				break;

			case R.id.button12:

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				storedPass = settings.getString("password", null); //  password stored

				try {

					String waitState= FileUtils.readFromFile(getApplicationContext(), "waitstate");
					java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
					Time now = new Time();
					if(waitState.equalsIgnoreCase("wait")) {

						now.setToNow();

						Date d1 = df.parse(FileUtils.readFromFile(getApplicationContext(), "timestamp"));
						Date d2 = df.parse(now.format("%H:%M:%S"));

						if(((d2.getTime()-d1.getTime()) / 1000) > 30) {

							FileUtils.writeToFile(getApplicationContext(), "0", "timestamp");
							FileUtils.writeToFile(getApplicationContext(), "go", "waitstate");

							attempts=0;

							if(EncryptionHelper.validatePassword(s, storedPass)) {

								attempts=0;

								Intent i=new Intent("com.upa.passwordlocker.SQLVIEW");
								startActivity(i);
								finish();
							}
						}
						else {

							TextView tv=new TextView(this);
							tv.setText(String.format("%d seconds", 30 - ((d2.getTime() - d1.getTime()) / 1000)));

							Dialog d=new Dialog(this);
							d.setTitle("Wait");
							d.setContentView(tv);
							d.show();

							s="";
							pinInput.setText(s);
						}
					}

					else {

						boolean matched = false;
						try {
							matched = EncryptionHelper.validatePassword(s, storedPass);
						}
						catch(NoSuchAlgorithmException | InvalidKeySpecException e){
							Log.e(TAG, "Error validating password", e.getCause());
						}

						if(matched) {

							attempts=0;

							Intent i=new Intent("com.upa.passwordlocker.SQLVIEW");
							startActivity(i);
							finish();
						}
						else {

							s="";
							pinInput.setText(s);
							attempts++;

							if (attempts>=3) {

								now.setToNow();

								FileUtils.writeToFile(getApplicationContext(), "wait", "waitstate");
								FileUtils.writeToFile(getApplicationContext(), now.format("%H:%M:%S"), "timestamp");

								Toast.makeText(PasswordActivity.this,"Three wrong attempts !!\nWait for 30 secs then try again", Toast.LENGTH_LONG).show();
							}
							else {
								Toast.makeText(PasswordActivity.this, "Invalid Password !!\nAttempts= " + attempts, Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
				catch(ParseException | NoSuchAlgorithmException | InvalidKeySpecException e){
					Log.e(TAG, "Error validating password", e.getCause());
				}
				break;

			case R.id.button13:

				Toast.makeText(this,"This will reset your password.",Toast.LENGTH_LONG).show();

				AlertDialog.Builder dialog = new AlertDialog.Builder(this)
						.setTitle("Answer Your Security Question").setMessage(question_value).setView(vi)
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {}
						})
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							TextInputEditText answerTextInput;

							@Override
							public void onClick(DialogInterface dialog, int which) {

								answerTextInput = vi.findViewById(R.id.editTexty);
								s = answerTextInput.getText().toString();

								try {

									if(EncryptionHelper.validatePassword(s, value)) {

										Random rnd = new Random();
										int n = 1000 + rnd.nextInt(9000);

										Toast.makeText(PasswordActivity.this, "Your New Password is : " + n, Toast.LENGTH_LONG).show();

										n_hashed = String.valueOf(n);
										n_hashed = EncryptionHelper.generateStrongPasswordHash(n_hashed);

										SharedPreferences settings;
										SharedPreferences.Editor editor;
										settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
										editor = settings.edit();
										editor.putString(pass, n_hashed);
										editor.apply();

										s = "";
										pinInput.setText(s);
										attempts=0;
									}
									else {

										Toast.makeText(PasswordActivity.this, "Wrong Answer", Toast.LENGTH_LONG).show();

										s = "";
										pinInput.setText(s);
									}
								}
								catch(NoSuchAlgorithmException | InvalidKeySpecException e){
									Log.e(TAG, "Error validating password", e.getCause());
								}
							}
						});

				dialog.create().show();

				s = "";
				pinInput.setText(s);

				break;
		}

	}
}
