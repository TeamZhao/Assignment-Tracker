package com.example.assignmenttracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateAssignmentActivity extends ActionBarActivity {

	String assignmentNo;
	String assignmentTitle;
	String assignmentCourse;
	String assignmentDueDate;
	int assignmentProgress;
	String assTitle;
	public String record[] = new String[5];
	final String fields[] = { "assignmentNo", "assignmentTitle",
			"assignmentCourse", "assignmentDueDate", "assignmentProgress" };
	String formatedDate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_assignment);
		Intent intent = getIntent();
		assTitle = intent.getStringExtra("assTitle");
		// Code for popup
		final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder.setTitle("SUCCESS!");
		helpBuilder.setMessage("Assignment updated");
		helpBuilder.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
						Intent intent = new Intent(
								UpdateAssignmentActivity.this,
								MainActivity.class);
						startActivity(intent);
					}
				});
		// Code for pop up end
		//

		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c;
		if (MainActivity.role == "Student") {
			c = db.query(
					"tbl_Assignment",
					new String[] { "assignmentNo, assignmentTitle, assignmentCourse, assignmentDueDate, assignmentProgress" },
					"assignmentTitle = \"" + assTitle + "\";", null, null,
					null, null);
		} else { // role == "Teacher"
			c = db.query(
					"tbl_TeacherAssignment",
					new String[] { "assignmentNo, assignmentTitle, assignmentCourse, assignmentDueDate, assignmentProgress" },
					"assignmentTitle = \"" + assTitle + "\";", null, null,
					null, null);
		}
		while (c.moveToNext()) {
			assignmentNo = c.getString(0);
			assignmentTitle = c.getString(1);
			assignmentCourse = c.getString(2);
			assignmentDueDate = c.getString(3);
			assignmentProgress = c.getInt(4);
		}

		final EditText txtassignmentTitle = (EditText) findViewById(R.id.assTitle_update);
		final DatePicker dpassignmentDueDate = (DatePicker) findViewById(R.id.assDatePicker);
		final SeekBar seekBarassignmentProgress = (SeekBar) findViewById(R.id.assProgressSeekBar_update);

		txtassignmentTitle.setText(assignmentTitle);
		seekBarassignmentProgress.setProgress(assignmentProgress);
		String[] parts = assignmentDueDate.split("-");
		String[] dayOfMonth = parts[2].split(" ");
		dpassignmentDueDate.init(Integer.parseInt(parts[0]),
				Integer.parseInt(parts[1]), Integer.parseInt(dayOfMonth[0]),
				null);
		// code to keep track of selected DatePicker date

		final Calendar selectedDueDate = Calendar.getInstance();
		final SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		selectedDueDate.set(dpassignmentDueDate.getYear(),
				dpassignmentDueDate.getMonth(),
				dpassignmentDueDate.getDayOfMonth(), 23, 59, 59); // set to end
																	// of day
		formatedDate = format.format(selectedDueDate.getTime()); // initialize
																	// value to
																	// default
																	// value in
																	// datepicker
		dpassignmentDueDate.init(dpassignmentDueDate.getYear(),
				dpassignmentDueDate.getMonth(),
				dpassignmentDueDate.getDayOfMonth(),
				new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						selectedDueDate.set(year, monthOfYear - 1, dayOfMonth,
								23, 59, 59); // set to end of the selected day
						formatedDate = format.format(selectedDueDate.getTime());
						// insertCalTime.set(year, monthOfYear, dayOfMonth);
					}
				});
		// CODE FOR PUTTING VALUES IN SPINNER ITEM FROM SPINNER TABLE
		final Spinner courses = (Spinner) findViewById(R.id.assCourseSpinner_update);
		Cursor course;
		if (MainActivity.role == "Student") {
			course = db.query("tbl_Course", new String[] { "CourseName" },
					null, null, null, null, null);
		} else { // role == "Teacher"
			course = db
					.query("tbl_TeacherCourse", new String[] { "CourseName" },
							null, null, null, null, null);
		}
		String[] array_spinner = new String[course.getCount()];

		int counter = 0;
		while (course.moveToNext()) {
			array_spinner[counter] = course.getString(0);
			counter++;
		}

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, array_spinner);
		courses.setAdapter(adapter);
		courses.setSelection(adapter.getPosition(assignmentCourse));

		final DatabaseManager db1 = new DatabaseManager(this);

		final Button btnUpdateGame = (Button) findViewById(R.id.updateAss_update);
		btnUpdateGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
				// Toast.makeText(UpdateAssignmentActivity.this,
				// "Your Choice : \'" + seekBarassignmentProgress.getProgress()
				// + "\';",
				// Toast.LENGTH_SHORT).show();

				cv.put("assignmentNo", assignmentNo);
				cv.put("assignmentTitle", txtassignmentTitle.getText()
						.toString());
				cv.put("assignmentCourse", courses.getSelectedItem().toString());
				cv.put("assignmentDueDate", String.valueOf(formatedDate));
				cv.put("assignmentProgress",
						String.valueOf(seekBarassignmentProgress.getProgress()));

				record[0] = assignmentNo;
				record[1] = txtassignmentTitle.getText().toString();
				record[2] = courses.getSelectedItem().toString();
				record[3] = String.valueOf(formatedDate);
				record[4] = String.valueOf(seekBarassignmentProgress
						.getProgress());
				if (MainActivity.role == "Student") {
					db1.updateRecord(cv, "tbl_Assignment", fields, record);
				} else { // role == "Teacher"
					db1.updateRecord(cv, "tbl_TeacherAssignment", fields,
							record);
				}
				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();

			}
		});
		final Button btnCancelGame = (Button) findViewById(R.id.cancelAss_update);
		btnCancelGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UpdateAssignmentActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_assignment, menu);
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
