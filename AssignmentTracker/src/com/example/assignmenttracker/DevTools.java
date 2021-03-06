package com.example.assignmenttracker;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DevTools extends ActionBarActivity implements OnClickListener {

	DatabaseManager db = MainActivity.db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dev_tools);
		Button btnDevCleanDatabase = (Button) findViewById(R.id.devCleanDatabase);
		btnDevCleanDatabase.setOnClickListener(this);
		Button btnDevPopulateDatabase = (Button) findViewById(R.id.devPopulateDatabase);
		btnDevPopulateDatabase.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dev_tools, menu);
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.devCleanDatabase) {
			// Delete
			db.getReadableDatabase().close();
			db.deleteDatabase(this);
			// Recreate
			db.dbInitialize(MainActivity.getTables(),
					MainActivity.getTableString());
		} else if (v.getId() == R.id.devPopulateDatabase) {
			// Populate
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			// fetch current increment
			int counter = preferences.getInt("Counter", 0);
			int counter2 = preferences.getInt("Counter", 0);
			// Create Semester
			ContentValues valuesSemester = new ContentValues();
			valuesSemester.put("semesterDetails", "DummySemester" + counter);
			db.addRecord(valuesSemester, "tbl_Semester",
					new String[] { "semesterDetails" },
					new String[] { valuesSemester
							.getAsString("semesterDetails") });

			// for teacher table
			ContentValues valuesTeacherSemester = new ContentValues();
			valuesTeacherSemester.put("semesterDetails", "DummyTeacherSemester"
					+ counter2);
			db.addRecord(valuesTeacherSemester, "tbl_TeacherSemester",
					new String[] { "semesterDetails" },
					new String[] { valuesTeacherSemester
							.getAsString("semesterDetails") });

			// Create Courses and Assignments
			ContentValues valuesCourses = new ContentValues();
			ContentValues valuesAssignments = new ContentValues();
			final String courseFields[] = { "CourseCode", "CourseName",
					"semesterDetails", "Professor", "Description" };
			final String courseRecords[] = new String[5];
			final String asmtFields[] = { "assignmentTitle",
					"assignmentCourse", "assignmentDueDate",
					"assignmentProgress" };
			final String asmtRecords[] = new String[4];

			// Adds 4 Courses
			for (int x = 0; x < 4; x++) {
				courseRecords[0] = "devCOMP10" + counter;
				courseRecords[1] = "devCOMP10" + counter;
				courseRecords[2] = valuesSemester
						.getAsString("semesterDetails");
				courseRecords[3] = "devProfessor";
				courseRecords[4] = "devDesc";
				asmtRecords[0] = "devAssignment" + counter;
				asmtRecords[1] = courseRecords[1];
				asmtRecords[2] = "2015-05-01 10:00:00";
				asmtRecords[3] = String.valueOf(50);

				for (int i = 0; i < 5; i++) {
					valuesCourses.put(courseFields[i], courseRecords[i]);
				}

				for (int y = 0; y < 4; y++) {
					valuesAssignments.put(asmtFields[y], asmtRecords[y]);
				}
				db.addRecord(valuesCourses, "tbl_Course", courseFields,
						courseRecords);
				db.addRecord(valuesAssignments, "tbl_Assignment", asmtFields,
						asmtRecords);
				counter++;
			}

			// For teacher tables
			ContentValues valuesTeacherCourses = new ContentValues();
			ContentValues valuesTeacherAssignments = new ContentValues();
			final String teacherCourseFields[] = { "CourseCode", "CourseName",
					"semesterDetails", "Description" };
			final String teacherCourseRecords[] = new String[4];
			final String teacherAsmtFields[] = { "assignmentTitle",
					"assignmentCourse", "assignmentDueDate",
					"assignmentProgress" };
			final String teacherAsmtRecords[] = new String[4];

			// Adds 4 Courses
			for (int a = 0; a < 4; a++) {
				teacherCourseRecords[0] = "devTeacherCOMP10" + counter2;
				teacherCourseRecords[1] = "devTeacherCOMP10" + counter2;
				teacherCourseRecords[2] = valuesTeacherSemester
						.getAsString("semesterDetails");
				teacherCourseRecords[3] = "devTeacherDesc";
				teacherAsmtRecords[0] = "devTeacherAssignment" + counter2;
				teacherAsmtRecords[1] = teacherCourseRecords[1];
				teacherAsmtRecords[2] = "2015-05-01 10:00:00";
				teacherAsmtRecords[3] = String.valueOf(50);

				for (int j = 0; j < 4; j++) {
					valuesTeacherCourses.put(teacherCourseFields[j],
							teacherCourseRecords[j]);
				}

				for (int b = 0; b < 4; b++) {
					valuesTeacherAssignments.put(teacherAsmtFields[b],
							teacherAsmtRecords[b]);
				}
				db.addRecord(valuesTeacherCourses, "tbl_TeacherCourse",
						teacherCourseFields, teacherCourseRecords);
				db.addRecord(valuesTeacherAssignments, "tbl_TeacherAssignment",
						teacherAsmtFields, teacherAsmtRecords);
				counter2++;
			}

			// set current increment
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("Counter", counter);
			editor.apply();
			
			SharedPreferences.Editor editor2 = preferences.edit();
			editor2.putInt("Counter", counter2);
			editor2.apply();
			

		}
	}
}