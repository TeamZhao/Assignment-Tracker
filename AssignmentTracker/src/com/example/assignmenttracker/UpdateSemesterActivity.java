package com.example.assignmenttracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UpdateSemesterActivity extends ActionBarActivity {

	String passedFromShowSemesterListSelection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_semester);

		// gets list item content passed in from ShowSemesterActivity -- in this
		// case, the courseCode
		// use passedFromShowSemesterListSelection as the search string to search for the database record
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			passedFromShowSemesterListSelection = extras
					.getString("semesterDetails");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_semester, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}