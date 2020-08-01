package com.upa.passwordlocker.views;

import com.google.android.material.textview.MaterialTextView;
import com.upa.passwordlocker.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class SearchableActivity extends Activity {
	
	private MaterialTextView txtQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable);

		ActionBar actionBar = getActionBar();
		 
        // Enabling Back navigation on Action Bar icon
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
 
        txtQuery = (MaterialTextView) findViewById(R.id.textView2);
 
        handleIntent(getIntent());
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
 
    private void handleIntent(Intent intent) {

	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
 
           // Use this query to display search results like
            txtQuery.setText(String.format("%s", query));
        }
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchable, menu);
		return true;
	}

}
