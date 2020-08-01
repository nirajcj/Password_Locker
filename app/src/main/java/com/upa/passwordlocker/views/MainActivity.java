package com.upa.passwordlocker.views;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.upa.passwordlocker.R;
import com.upa.passwordlocker.utils.CustomDbHelper;
import com.upa.passwordlocker.utils.AESHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	MaterialButton updateButton;
	TextInputEditText websiteTextInput, usernameTextInput, passwordTextInput;

	private boolean isDone, isBackPress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		isDone = false;

		updateButton = (MaterialButton) findViewById(R.id.button1);

		usernameTextInput = (TextInputEditText) findViewById(R.id.editText1);
		passwordTextInput = (TextInputEditText) findViewById(R.id.editText2);
		websiteTextInput = (TextInputEditText) findViewById(R.id.editText3);

		updateButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();

		if(isDone){
			Intent i=new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onBackPressed() {

		isDone =false;
		isBackPress =true;

		Intent i = new Intent("com.upa.passwordlocker.SQLVIEW");
		startActivity(i);
		finish();
	}

	@Override
	public void onPause() {

		super.onPause();

		if(!isBackPress)
			isDone =true;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.button1) {

			boolean worked = true;

			String username = usernameTextInput.getText().toString();
			String password = passwordTextInput.getText().toString();
			String website = websiteTextInput.getText().toString();

			if (containsWhiteSpace(website)) {
				Toast.makeText(MainActivity.this, "Website field cannot have spaces", Toast.LENGTH_SHORT).show();
			}
			else if (containsWhiteSpace(username)) {
				Toast.makeText(MainActivity.this, "Username field cannot have spaces", Toast.LENGTH_SHORT).show();
			}
			else if (containsWhiteSpace(password)) {
				Toast.makeText(MainActivity.this, "Password field cannot have spaces", Toast.LENGTH_SHORT).show();
			}
			else if (website.equalsIgnoreCase("")) {
				Toast.makeText(MainActivity.this, "Website field cannot be empty", Toast.LENGTH_SHORT).show();
			}
			else if ((username.equalsIgnoreCase("")) && (password.equalsIgnoreCase(""))) {
				Toast.makeText(MainActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();
			}
			else {

				if (username.equalsIgnoreCase("")) {
					username = "No_username_entered";
				}
				if (password.equalsIgnoreCase("")) {
					password = "No_password_entered";
				}

				// pass the context of the current class
				try {

					String username_encrypted = AESHelper.encrypt(username);
					String password_encrypted = AESHelper.encrypt(password);

					CustomDbHelper entry = new CustomDbHelper(MainActivity.this);

					entry.open();
					entry.createEntry(website, username_encrypted, password_encrypted);
					entry.close();

					usernameTextInput.setText(null);
					passwordTextInput.setText(null);
					websiteTextInput.setText(null);
				}
				catch (Exception e) {
					worked = false;
				}
				finally {

					if (worked) {

						TextView tv = new TextView(this);
						tv.setText(R.string.creation_success_string);

						Dialog d = new Dialog(this);
						d.setTitle("Done");
						d.setContentView(tv);
						d.show();
					}
				}
			}
		}
	}

	private boolean containsWhiteSpace(final String testCode) {

		if(testCode == null) {
			return false;
		}

		for(int i = 0; i < testCode.length(); i++) {
			if(Character.isWhitespace(testCode.charAt(i))){
				return true;
			}
		}

		return false;
	}

	private boolean customStartActivity(Intent aIntent) {
		try
		{
			startActivity(aIntent);
			return true;
		}
		catch (ActivityNotFoundException e)
		{
			return false;
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

				//Try Google play
				intent.setData(Uri.parse("market://details?id="+ MainActivity.this.getPackageName()));

				if (!customStartActivity(intent)) {

					//Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+MainActivity.this.getPackageName()));
					if (!customStartActivity(intent)) {

						//Well if this also fails, we have run out of options, inform the user.
						Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
					}
				}
				finish();

			default:
				return super.onOptionsItemSelected(item);
		}
	}

}