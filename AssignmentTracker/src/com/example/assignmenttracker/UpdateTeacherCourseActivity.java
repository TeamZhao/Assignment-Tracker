package com.example.assignmenttracker;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

public class UpdateTeacherCourseActivity extends ActionBarActivity {

	String code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_teacher_course);
		
		//gets list item content passed in from ShowCoursesActivity -- in this case, the courseCode
		// use passedFromShowCourseListSelection as the search string for the database record
		Bundle extras = getIntent().getExtras();
		code = extras.getString("courseCode");
		
		
		final String fields[] = { "CourseCode", "CourseName", "semesterDetails", "Description" };
		String courseName="" ,description="", semester="";
		
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db
				.query("tbl_TeacherCourse", fields,
						"CourseCode = \"" + code + "\";",
						null, null, null, null);
		while (c.moveToNext()) {		
			 courseName = c.getString(1);
			 semester = c.getString(2);
			 description = c.getString(3);
		}
		
		
		final String record[] = new String[4];
		// Handle all elements on page
		final Button btnAddCou = (Button) findViewById(R.id.btn_updateTeacherCourse);
		final Button btnCancelCou = (Button) findViewById(R.id.btn_cancelUpdateTeacherCourse);
		final EditText couCode = (EditText) findViewById(R.id.txt_teacherUpdateCourseCode);
		final EditText couName = (EditText) findViewById(R.id.txt_teacherUpdateCourseName);
		final EditText couDescription = (EditText) findViewById(R.id.txt_teacherUpdateCourseDescription);
		final Spinner semesters = (Spinner) findViewById(R.id.teacherUpdateCourseSemesterSpinner);
		
		couCode.setText(code);
		couName.setText(courseName);
		couDescription.setText(description);
		
		
		Cursor s = db
				.query("tbl_TeacherSemester", new String[]{"semesterDetails"},
						null, null, null, null, null); // change this to 
		ArrayList<String> values = new ArrayList<String>();
		
		while (s.moveToNext()) {		
			values.add(s.getString(0));
		}
		
		ArrayAdapter adapter = new ArrayAdapter(this,
		        android.R.layout.simple_spinner_item, values);
		semesters.setAdapter(adapter);
        semesters.setSelection(adapter.getPosition(semester));
		

			// Code for popup
			final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
			// new AlertDialog.Builder(this);
			helpBuilder.setTitle("ERROR!");
			helpBuilder.setMessage("Field cannot be empty, Try again");
			helpBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog
			}
		});
			//Code for pop up end

		// Code for popup
		final AlertDialog.Builder helpBuilder1 = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder1.setTitle("Success");
		helpBuilder1.setMessage("Course updated successfully.");
		helpBuilder1.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog
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
			MainActivity.db.updateRecord(values, "tbl_TeacherCourse", fields,
					record);
			// MainActivity.db.close();
//
//			couCode.setText("");
//			couName.setText("");
//			couDescription.setText("");
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

		onBackPressed();
	}
});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_teacher_course, menu);
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