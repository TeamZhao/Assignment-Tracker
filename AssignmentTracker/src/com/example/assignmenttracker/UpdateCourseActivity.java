package com.example.assignmenttracker;

import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCourseActivity extends ActionBarActivity {

	String code;
	String courseNo, courseName, professor, description, semester;
	String record[] = new String[6];
	String fields[] = { "courseNo", "CourseCode", "CourseName",
			"semesterDetails", "Professor", "Description" };
	private DatabaseManager dbCourse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_course);
		dbCourse = new DatabaseManager(this);
		// gets list item content passed in from ShowCoursesActivity -- in this
		// case, the courseCode
		// use passedFromShowCourseListSelection as the search string for the
		// database record
		Bundle extras = getIntent().getExtras();
		code = extras.getString("courseCode");

		final SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db.query("tbl_Course", fields, "CourseCode = \"" + code
				+ "\";", null, null, null, null);
		while (c.moveToNext()) {
			courseNo = c.getString(0);
			courseName = c.getString(1);
			semester = c.getString(2);
			professor = c.getString(3);
			description = c.getString(4);
		}

		// Handle all elements on page
		final Button btnUpdateCou = (Button) findViewById(R.id.btn_updateCourse);
		final Button btnCancelCou = (Button) findViewById(R.id.btn_cancelUpdateCourse);
		final EditText couCode = (EditText) findViewById(R.id.txt_courseCodeUpdate);
		final EditText couName = (EditText) findViewById(R.id.txt_courseNameUpdate);
		final EditText couProfessor = (EditText) findViewById(R.id.txt_professorUpdate);
		final EditText couDescription = (EditText) findViewById(R.id.txt_descriptionUpdate);
		final Spinner semesters = (Spinner) findViewById(R.id.courseSemesterSpinnerUpdate);

		couCode.setText(code);
		couName.setText(courseName);
		couProfessor.setText(professor);
		couDescription.setText(description);

		Cursor s = db.query("tbl_Semester", new String[] { "semesterDetails" },
				null, null, null, null, null);
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
		// Code for pop up end

		// Code for popup
		final AlertDialog.Builder helpBuilder1 = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder1.setTitle("Success");
		helpBuilder1.setMessage("Course updated successfully.");
		helpBuilder1.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(UpdateCourseActivity.this,
								ShowCoursesActivity.class);
						startActivity(intent);
					}
				});
		// Code for pop up end
		
		// Code for popup
		final AlertDialog.Builder helpBuilder2 = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder2.setTitle("ERROR!");
		helpBuilder2.setMessage("One or more associated assignments could not be updated");
		helpBuilder2.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
					}
				});
		// Code for pop up end

		btnUpdateCou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (couCode.getText().toString().matches("")
						|| couName.getText().toString().matches("")
						|| couDescription.getText().toString().matches("")) {
					// Show the popup
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					return;
				} else {
					// update the course table
					ContentValues cv = new ContentValues();
					cv.put("courseNo", courseNo);
					cv.put("CourseCode", couCode.getText().toString());
					cv.put("CourseName", couName.getText().toString());
					cv.put("semesterDetails", semesters.getSelectedItem()
							.toString());
					cv.put("Professor", couProfessor.getText().toString());
					cv.put("Description", couDescription.getText().toString());

					record[0] = courseNo;
					record[1] = couCode.getText().toString();
					record[2] = couName.getText().toString();
					record[3] = semesters.getSelectedItem().toString();
					record[4] = couProfessor.getText().toString();
					record[5] = couDescription.getText().toString();

					dbCourse.updateRecord(cv, "tbl_Course", fields, record);
					// end update the course table
					
					
					// update all assignments under affected course
					String assFields[] = { "assignmentNo", "assignmentTitle",
							"assignmentCourse", "assignmentDueDate",
							"assignmentProgress" };
					String assRecord[] = new String[5];
					String assignmentNo = "", assignmentTitle = "", assignmentDueDate = "";
					int assignmentProgress = 0;
					ArrayList<String> associatedAssTitles = new ArrayList<String>();
					Cursor assCursor = db.query("tbl_Assignment",
							assFields, "assignmentCourse = \"" + code + "\";", null,
							null, null, null);
					while (assCursor.moveToNext()) {
						associatedAssTitles.add(assCursor.getString(1));
					}
					// iterate through the list of associated ass titles and
					// update each course
					for (int assIterator = 0; assIterator < associatedAssTitles
							.size(); assIterator++) {
						Cursor assCursor2 = db.query("tbl_Assignment",
								assFields, "assignmentTitle = \""
										+ associatedAssTitles.get(assIterator)
										+ "\";", null, null, null, null);
						while (assCursor2.moveToNext()) {
							assignmentNo = assCursor2.getString(0);
							assignmentTitle = assCursor2.getString(1);
							assignmentDueDate = assCursor2.getString(3);
							assignmentProgress = assCursor2.getInt(4);
						}
						if (assignmentNo.matches("")
								|| assignmentTitle.matches("")
								|| assignmentDueDate.matches("")
								|| assignmentNo.matches("")
								|| String.valueOf(assignmentProgress).matches(
										"0")) {
							// Show the popup
							AlertDialog helpDialog = helpBuilder2.create();
							helpDialog.show();
							return;
						} else {
							assRecord[0] = assignmentNo;
							assRecord[1] = assignmentTitle;
							assRecord[2] = record[1];
							assRecord[3] = assignmentDueDate;
							assRecord[4] = String.valueOf(assignmentProgress);
							// populate the row with some values
							ContentValues assValues = new ContentValues();
							for (int av = 0; av < assRecord.length; av++)
								assValues.put(assFields[av], assRecord[av]);
							// add the row to the database
							dbCourse.updateRecord(assValues, "tbl_Assignment", assFields, assRecord);
						}
					}
					// end update all assignments under affected course
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
