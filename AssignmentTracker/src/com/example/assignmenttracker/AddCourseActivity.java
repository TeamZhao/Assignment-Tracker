package com.example.assignmenttracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class AddCourseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_course, menu);
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
	
	final String courseFields[] = { "Code", "Name", "Professor", "Desc" };
	final EditText courseCode = (EditText) findViewById(R.id.courseCodeInput);
	final EditText courseName = (EditText) findViewById(R.id.courseNameInput);
	final EditText courseProfessor = (EditText) findViewById(R.id.courseProfessorInput);
	final EditText courseDesc = (EditText) findViewById(R.id.courseDescriptionInput);

	final Button btnAddCourse = (Button) findViewById(R.id.btn_addCourse);
	final Button btnCancelCourse = (Button) findViewById(R.id.btn_cancelCourse);
	
}
