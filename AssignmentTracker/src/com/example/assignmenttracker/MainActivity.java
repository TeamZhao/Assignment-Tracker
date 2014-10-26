package com.example.assignmenttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	public static DatabaseManager db;
	private static final String tables[] = { "tbl_Semester", "tbl_Assignment" };
	private static final String tableCreatorString[] = { "CREATE TABLE tbl_Semester (semesterNo INTEGER PRIMARY KEY AUTOINCREMENT , semesterDetails TEXT);"
		, "CREATE TABLE tbl_Assignment (assignmentNo INTEGER PRIMARY KEY AUTOINCREMENT , assignmentTitle TEXT , assignmentCourse TEXT , assignmentDueDate TEXT , assignmentProgress TEXT);" };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DatabaseManager(this);
		db.dbInitialize(tables, tableCreatorString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent adds;
		switch (item.getItemId()) {
		case R.id.add_assignment:
			adds = new Intent(this, AddAssignmentActivity.class);
			startActivity(adds);
			return true;
		case R.id.add_course:

			return true;
		case R.id.add_semester:
			adds = new Intent(this, AddSemesterActivity.class);
			startActivity(adds);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
