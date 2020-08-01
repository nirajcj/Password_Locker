package com.upa.passwordlocker.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.upa.passwordlocker.R;
import com.upa.passwordlocker.utils.CommonUtils;
import com.upa.passwordlocker.utils.EncryptionHelper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChangePinActivity extends Activity implements OnClickListener{

	private static final String TAG = ChangePinActivity.class.getName();

	private static final String PREFS_NAME = "app_pref";
	private static final String pass = "password";
	private boolean isDone, isBackPress;

	TextInputEditText oldPin, newPin, reenterdPin;
	MaterialButton changePin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pin);

		isDone = false;

		oldPin = findViewById(R.id.oldPin);
		newPin = findViewById(R.id.newPin);
		reenterdPin = findViewById(R.id.reenterNewPin);

		changePin = findViewById(R.id.change);
		changePin.setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();

		if(isDone){
			Intent i = new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onBackPressed() {

		isDone = false;
		isBackPress = true;

		Intent i = new Intent("com.upa.passwordlocker.SQLVIEW");
		startActivity(i);
		finish();
	}

	@Override
	public void onPause() {

		super.onPause();

		if(!isBackPress) {
			isDone = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.change) {

			String currentPinInput = oldPin.getText().toString();
			String changePinInput = newPin.getText().toString();
			String reChangedPinInput = reenterdPin.getText().toString();

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			String currentPin = settings.getString("password", null);

			boolean matched = false;

			try {
				matched = EncryptionHelper.validatePassword(currentPinInput, currentPin);
			}
			catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				Log.e(TAG, "Error validating password", e.getCause());
			}

			if (matched) {

				if (changePinInput.equalsIgnoreCase(reChangedPinInput)) {

					String hashedNewPin = "";
					try {
						hashedNewPin = EncryptionHelper.generateStrongPasswordHash(changePinInput);
					}
					catch (Exception e) {
						Log.e(TAG, "Error generating password", e.getCause());
					}

					SharedPreferences.Editor editor;
					settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

					editor = settings.edit();
					editor.putString(pass, hashedNewPin);
					editor.apply();

					Toast.makeText(this, "PIN Changed Successfully", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(this, "New Pin doesn't Match", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(this, "Wrong Old Pin", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle presses on the action bar items
		switch (item.getItemId()) {

			case R.id.addEntry:
				Intent iAddEntry=new Intent("com.upa.passwordlocker.MAIN_ACTIVITY");
				startActivity(iAddEntry);
				finish();
				return true;

			case R.id.changePin:
				Intent iChangePin=new Intent("com.upa.passwordlocker.CHANGEPINACTIVITY");
				startActivity(iChangePin);
				finish();
				return true;

			case R.id.about:
				Intent iAbout=new Intent("com.upa.passwordlocker.ABOUT");
				startActivity(iAbout);
				finish();
				return true;

			case R.id.rate:

				Intent intent = new Intent(Intent.ACTION_VIEW);

				// Try Google play
				intent.setData(Uri.parse("market://details?id=" + ChangePinActivity.this.getPackageName()));
				if (CommonUtils.hasActivityNotStarted(getApplicationContext(), intent)) {

					// Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + ChangePinActivity.this.getPackageName()));
					if (CommonUtils.hasActivityNotStarted(getApplicationContext(), intent)) {

						// Well if this also fails, we have run out of options, inform the user.
						Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
					}
				}
				finish();

			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
