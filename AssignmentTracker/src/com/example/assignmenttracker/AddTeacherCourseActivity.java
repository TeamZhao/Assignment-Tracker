package com.example.assignmenttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddTeacherCourseActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_teacher_course);

		final String fields[] = { "CourseCode", "CourseName", "semesterDetails" , "Description" };
		final String record[] = new String[4];
		// Handle all elements on page
		final Button btnAddCou = (Button) findViewById(R.id.btn_addTeacherCourse);
		final Button btnCancelCou = (Button) findViewById(R.id.btn_cancelAddTeacherCourse);
		final EditText couCode = (EditText) findViewById(R.id.txt_teacherCourseCode);
		final EditText couName = (EditText) findViewById(R.id.txt_teacherCourseName);
		final EditText couDescription = (EditText) findViewById(R.id.txt_teacherCourseDescription);
		final Spinner semesters = (Spinner) findViewById(R.id.teacherCourseSemesterSpinner);
		
		

		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db
				.query("tbl_TeacherSemester", new String[]{"semesterDetails"},
						null, null, null, null, null);
		String[] array_spinner = new String[c.getCount()];
		
		int counter=0;
		while (c.moveToNext()) {		
			array_spinner[counter] = c.getString(0);
			counter++;
		}
		
		ArrayAdapter adapter = new ArrayAdapter(this,
		        android.R.layout.simple_spinner_item, array_spinner);
		semesters.setAdapter(adapter);
		

		// Code for popup
		final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder.setTitle("ERROR!");
		helpBuilder.setMessage("Field cannot be empty, Try again");
		helpBuilder.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Close the dialog 
						dialog.dismiss();
					}
				});
		// Code for pop up end

		// Code for popup
		final AlertDialog.Builder helpBuilder1 = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder1.setTitle("Success");
		helpBuilder1.setMessage("Course added successfully.");
		helpBuilder1.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Close the dialog then go back
						Intent b = new Intent(AddTeacherCourseActivity.this, ShowCoursesActivity.class);
						startActivity(b);
					}
				});
		// Code for pop up end

		btnAddCou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (couCode.getText().toString().matches("")
						|| couName.getText().toString().matches("")
						|| couDescription.getText().toString().matches("")) {
					// Show the popup
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					return;
				} else {
					record[0] = couCode.getText().toString();
					record[1] = couName.getText().toString();
					record[2] = semesters.getSelectedItem().toString();
					record[3] = couDescription.getText().toString();

					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					MainActivity.db.addRecord(values, "tbl_TeacherCourse", fields,
							record);
					// MainActivity.db.close();

					couCode.setText("");
					couName.setText("");
					couDescription.setText("");
					// Show the popup
					AlertDialog helpDialog = helpBuilder1.create();
					helpDialog.show();

				}

			}
		});

		btnCancelCou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Intent myIntent = new Intent(AddCourseActivity.this,
				// MainActivity.class);
				Intent b = new Intent(AddTeacherCourseActivity.this, ShowCoursesActivity.class);
				startActivity(b);
			}
		});
	}
}
