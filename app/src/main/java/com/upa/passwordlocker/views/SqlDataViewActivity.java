package com.upa.passwordlocker.views;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.models.Group;
import com.upa.passwordlocker.R;
import com.upa.passwordlocker.adapter.MyExpandableListAdapter;
import com.upa.passwordlocker.utils.CustomDbHelper;
import com.upa.passwordlocker.utils.AESHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class SqlDataViewActivity extends Activity{

	private boolean isDone;
	private boolean isBackPress;
	private String s1 = "", s2 = "", s3 = "";
	private int pos;

	// more efficient than HashMap for mapping integers to objects
	private SparseArray<Group> groups = new SparseArray<>();

	private CustomDbHelper entry = new CustomDbHelper(SqlDataViewActivity.this);

	private MyExpandableListAdapter adapter;

	ExpandableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		isDone =false;
	}

	@Override
	protected void onResume() {

		super.onResume();

		if(isDone){
			Intent i=new Intent("com.upa.passwordlocker.PASSWORD_ACTIVITY");
			startActivity(i);
			finish();
		}

		createData();

		int layoutId;

		if (groups.size()==0) {
			layoutId = R.layout.viewsqlempty;
		}
		else {
			layoutId = R.layout.viewsql;
		}

		setContentView(layoutId);

		if (!(groups.size()==0)) {

			adapter = new MyExpandableListAdapter(this, groups);

			listView = findViewById(R.id.listView);
			listView.setAdapter(adapter);

			registerForContextMenu(listView);
		}
		else {

			Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/kbzipadeedoodah.ttf");

			MaterialTextView myTextView = (MaterialTextView)findViewById(R.id.textView1);
			myTextView.setTypeface(myTypeface);
		}
	}

	@Override
	public void onBackPressed() {

		isDone =false;
		isBackPress =true;

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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.listView) {
			menu.add(Menu.NONE, 0, 0, "Edit");
			menu.add(Menu.NONE, 0, 1, "Delete");
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		pos = ExpandableListView.getPackedPositionGroup(info.packedPosition);

		if (item.getTitle().toString().equals("Edit")) {

			final View v = getLayoutInflater().inflate(R.layout.edit_dialog, null);

			AlertDialog.Builder mydialog = new AlertDialog.Builder(this)
					.setTitle("Edit")
					.setView(v)
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {}
					})
					.setPositiveButton("Update", new DialogInterface.OnClickListener() {

						TextInputEditText websiteTextInput, usernameTextInput, passwordTextInput;

						@Override
						public void onClick(DialogInterface dialog, int which) {

							websiteTextInput =(TextInputEditText)v.findViewById(R.id.editText1);
							usernameTextInput =(TextInputEditText)v.findViewById(R.id.editText2);
							passwordTextInput =(TextInputEditText)v.findViewById(R.id.editText3);

							s1= websiteTextInput.getText().toString();
							s2= usernameTextInput.getText().toString();
							s3= passwordTextInput.getText().toString();

							if(containsWhiteSpace(s1)) {
								Toast.makeText(SqlDataViewActivity.this, "Website field cannot have spaces", Toast.LENGTH_SHORT).show();
							}
							else if(containsWhiteSpace(s2)) {
								Toast.makeText(SqlDataViewActivity.this, "Username field cannot have spaces", Toast.LENGTH_SHORT).show();
							}
							else if(containsWhiteSpace(s3)) {
								Toast.makeText(SqlDataViewActivity.this, "Password field cannot have spaces", Toast.LENGTH_SHORT).show();
							}
							else if(s1.equalsIgnoreCase("")) {
								Toast.makeText(SqlDataViewActivity.this, "Website field cannot be empty", Toast.LENGTH_SHORT).show();
							}
							else if((s2.equalsIgnoreCase(""))&&(s3.equalsIgnoreCase(""))) {
								Toast.makeText(SqlDataViewActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();}
							else {

								if(s2.equalsIgnoreCase("")) {
									s2 = "No_username_entered";
								}
								if(s3.equalsIgnoreCase("")) {
									s3 = "No_password_entered";
								}

								try {

									String s2_encr = AESHelper.encrypt(s2);
									String s3_encr = AESHelper.encrypt(s3);

									entry.open();
									entry.editTable(pos+1,s1,s2_encr,s3_encr);
									entry.close();

									createData();
								}
								catch (Exception e) {
								}
								adapter.notifyDataSetChanged();
							}
						}
					});

			mydialog.create().show();
		}
		else if(item.getTitle().toString().equals("Delete")) {

			try {

				entry.open();
				entry.deleteEntry(pos+1);
				entry.close();

				createData();

				adapter.notifyDataSetChanged();
			}
			catch(Exception e) {

				setContentView(R.layout.viewsqlempty);

				Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/kbzipadeedoodah.ttf");
				TextView myTextView = (TextView)findViewById(R.id.textView1);

				myTextView.setTypeface(myTypeface);}
		}
		return super.onContextItemSelected(item);
	}



	public void createData() {

		try {

			int k = 0;
			groups.clear();

			CustomDbHelper info = new CustomDbHelper(this);

			info.open();
			String data = info.getData();
			info.close();

			String username_decr;
			String password_decr;

			String[] arr = data.split("\n");

			for (int j = 0; j < arr.length; j = j + 3) {

				Group group = new Group(arr[j]);

				try {

					username_decr = AESHelper.decrypt(arr[j + 1]);
					password_decr = AESHelper.decrypt(arr[j + 2]);

					group.children.add("Username - " + username_decr);
					group.children.add("Password - " + password_decr);

					groups.append(k++, group);
				}
				catch (Exception e) {}
			}
		}
		catch (SQLException e) {}

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public static boolean containsWhiteSpace(final String testCode) {

		if(testCode == null) {
			return false;
		}

		for(int i = 0; i < testCode.length(); i++){
			if(Character.isWhitespace(testCode.charAt(i))){
				return true;
			}
		}

		return false;
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
				intent.setData(Uri.parse("market://details?id="+ SqlDataViewActivity.this.getPackageName()));
				if (!customStartActivity(intent)) {

					// Market (Google play) app seems not installed, let's try to open a web browser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ SqlDataViewActivity.this.getPackageName()));
					if (!customStartActivity(intent)) {

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
