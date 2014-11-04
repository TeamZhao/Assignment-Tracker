package com.example.assignmenttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	public static DatabaseManager db;
	private static final String tables[] = { "tbl_Semester", "tbl_Assignment", "tbl_Course" };
	private static final String tableCreatorString[] = {
			"CREATE TABLE IF NOT EXISTS tbl_Semester (semesterNo INTEGER PRIMARY KEY AUTOINCREMENT , semesterDetails TEXT);",
			"CREATE TABLE IF NOT EXISTS tbl_Assignment (assignmentNo INTEGER PRIMARY KEY AUTOINCREMENT , assignmentTitle TEXT , assignmentCourse TEXT, assignmentDueDate DATE , assignmentProgress INT);",
			"CREATE TABLE IF NOT EXISTS tbl_Course (CourseCode TEXT , CourseName TEXT, Professor TEXT , Description TEXT);" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseManager(this);
		db.dbInitialize(tables, tableCreatorString);
		setContentView(R.layout.activity_main);

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
			adds = new Intent(this, AddCourseActivity.class);
			startActivity(adds);
			return true;
		case R.id.add_semester:
			adds = new Intent(this, AddSemesterActivity.class);
			startActivity(adds);
			return true;
		case R.id.show_semesters:
			adds = new Intent(this, ShowSemestersActivity.class);
			startActivity(adds);
			return true;
		case R.id.show_courses:
			adds = new Intent(this, ShowCoursesActivity.class);
			startActivity(adds);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
