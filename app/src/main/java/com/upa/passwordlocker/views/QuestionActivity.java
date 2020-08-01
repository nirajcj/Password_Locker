package com.upa.passwordlocker.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.utils.EncryptionHelper;
import com.upa.passwordlocker.R;

public class QuestionActivity extends Activity implements OnClickListener {

	private static final String TAG = QuestionActivity.class.getName();

	private static final String PREFS_NAME = "app_pref";
	private static final String PREFS_NAME1 = "MyPrefsFile";

	public static final String pass1 = "question";
	public static final String pass2 = "launch";
	public static final String pass3 = "question_value";

	private String s = "", buttonText = "";
	private String s_hashed = "";

	MaterialButton button1, button2, button3, button4, button5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestPass2();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		button3 = findViewById(R.id.button3);
		button4 = findViewById(R.id.button4);
		button5 = findViewById(R.id.button5);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);

		Typeface heading = Typeface.createFromAsset(getAssets(), "fonts/yoyo.otf");
		MaterialTextView myTextView = findViewById(R.id.textView1);
		myTextView.setTypeface(heading);
	}


	@Override
	public void onClick(final View v) {

		final View view = getLayoutInflater().inflate(R.layout.question_dialog, null);

		switch(v.getId()) {

			case R.id.button1:

				MaterialButton b1 = (MaterialButton) v;
				buttonText = b1.getText().toString();

				onButtonClick("Answer1", view);

				break;

			case R.id.button2:

				MaterialButton b2 = (MaterialButton) v;
				buttonText = b2.getText().toString();

				onButtonClick("Answer2", view);

				break;

			case R.id.button3:

				MaterialButton b3 = (MaterialButton) v;
				buttonText = b3.getText().toString();

				onButtonClick("Answer3", view);

				break;

			case R.id.button4:

				MaterialButton b4 = (MaterialButton) v;
				buttonText = b4.getText().toString();

				onButtonClick("Answer4", view);

				break;

			case R.id.button5:

				MaterialButton b5 = (MaterialButton) v;
				buttonText = b5.getText().toString();

				onButtonClick("Answer5", view);

				break;
		}

		SharedPreferences settings1;
		SharedPreferences.Editor editor1;
		settings1 = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		editor1 = settings1.edit();
		editor1.putString(pass3, buttonText);
		editor1.apply();

		requestPass2();
	}

	private void onButtonClick(String dialogTitle, final View view) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this)
				.setTitle(dialogTitle).setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					TextInputEditText answerTextInput;

					@Override
					public void onClick(DialogInterface dialog, int which) {

						answerTextInput = (TextInputEditText)view.findViewById(R.id.editText1);
						s = answerTextInput.getText().toString();

						try {

							s_hashed=EncryptionHelper.generateStrongPasswordHash(s);

							SharedPreferences settings;
							SharedPreferences.Editor editor;
							settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

							editor = settings.edit();
							editor.putString(pass1, s_hashed);
							editor.apply();
						}
						catch(Exception e) {
							Log.e(TAG, "Error generating password", e.getCause());
						}

						requestPass();

						Intent i=new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
						startActivity(i);
						finish();
					}
				});

		dialog.create().show();
	}

	private void requestPass() {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);

		if(settings.getBoolean(pass2, true)) {
			settings.edit().putBoolean(pass2, false).apply();
			return;
		}

		Intent i=new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
		startActivity(i);
		finish();
	}

	private void requestPass2() {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME1, MODE_PRIVATE);

		if(settings.getBoolean(pass2, true)) {
			return;
		}

		Intent i=new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
		startActivity(i);
		finish();
	}
}
