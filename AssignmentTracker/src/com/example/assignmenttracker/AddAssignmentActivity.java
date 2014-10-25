package com.example.assignmenttracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AddAssignmentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_assignment, menu);
		return true;
	}

	private static final String tables[] = { "tbl_Assignment" };
	//
	private static final String tableCreatorString[] = { "CREATE TABLE tbl_Assignment (assignmentNo INTEGER PRIMARY KEY AUTOINCREMENT , assignmentTitle TEXT, assignmentCourse TEXT, assignmentDueDate DATE, assignmentProgress INT );" };

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
