package com.upa.passwordlocker.views;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.R;

public class AboutActivity extends Activity {

	private boolean isDone;
	private boolean isBackPress;

	MaterialTextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		isDone =false;

		textView = (MaterialTextView) findViewById(R.id.textView1);
		// TODO : Add remaining text
		textView.setText(getString(R.string.about_us_string));
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

		Intent i=new Intent("com.upa.passwordlocker.SQLVIEW");
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
		// Inflate the menu, this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private boolean customStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		}
		catch (ActivityNotFoundException e) {
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
				intent.setData(Uri.parse("market://details?id="+ AboutActivity.this.getPackageName()));
				if (!customStartActivity(intent)) {

					//Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ AboutActivity.this.getPackageName()));
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
