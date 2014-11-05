package com.example.assignmenttracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UpdateCourseActivity extends ActionBarActivity {

	String passedFromShowCourseListSelection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_course);
		
		//gets list item content passed in from ShowCoursesActivity -- in this case, the courseCode
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			passedFromShowCourseListSelection = extras.getString("courseCode");
		}
		
		//textview for debugging purposes -- can delete
		TextView displayBox = (TextView) findViewById(R.id.textView_listSelectionCourse);
		displayBox.setText(passedFromShowCourseListSelection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_course, menu);
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