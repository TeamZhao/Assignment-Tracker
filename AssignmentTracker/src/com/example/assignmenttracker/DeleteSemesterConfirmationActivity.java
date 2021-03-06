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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeleteSemesterConfirmationActivity extends ActionBarActivity {

	String passedFromShowSemesterListSelection;
	// public String record[] = new String[2];
	// final String fields[] = {"semesterNo", "semesterDetails"};
	public String semesterNo;
	ArrayList<String> semCourseCodes = new ArrayList<String>();
	ArrayList<String> semCourseNames = new ArrayList<String>();
	ArrayList<String> semCourseAssignments = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_semester_confirmation);

		// gets list item content passed in from ShowSemesterActivity -- in this
		// case, the courseCode
		// use passedFromShowSemesterListSelection as the search string to
		// search for the database record
		Bundle extras = getIntent().getExtras();
		//
		if (extras != null) {
			passedFromShowSemesterListSelection = extras
					.getString("semesterDetails");
			semesterNo = extras.getString("semesterNo");

			// show semester name in top row (different text format)
			final TextView tv_displaySemTitle = (TextView) findViewById(R.id.textView_DisplaySemTitle);
			final TextView tv_displaySemContents = (TextView) findViewById(R.id.textView_DisplaySemContents);
			final TextView tv_SemInfoNotice = (TextView) findViewById(R.id.textView_SemInfoNotice);
			final TextView tv_ContinueNotice = (TextView) findViewById(R.id.textView_ContinueNotice);

			tv_SemInfoNotice
					.setText("You are about to delete the following semester with its related courses and assignments as listed.");
			tv_displaySemTitle.setText("Semester: "
					+ passedFromShowSemesterListSelection);
			tv_ContinueNotice.setText("Are you sure you want to continue?");

			SQLiteDatabase db = MainActivity.db.getReadableDatabase();
			if (MainActivity.role == "Student") {
				Cursor c = db.query("tbl_Course",
						new String[] { "CourseCode, CourseName" },
						"semesterDetails ='"
								+ passedFromShowSemesterListSelection + "'",
						null, null, null, null);
				while (c.moveToNext()) {
					semCourseCodes.add(c.getString(0));
					semCourseNames.add(c.getString(1));

					// print courseCode and courseName on one line
					tv_displaySemContents.append("\t" + c.getString(0) + ": "
							+ c.getString(1) + "\n");
					Cursor c2 = db.query("tbl_Assignment",
							new String[] { "assignmentTitle" },
							"assignmentCourse ='" + c.getString(1) + "'", null,
							null, null, null);
					// print assignments under one course below the
					// courseCode/Name
					// line
					while (c2.moveToNext()) {
						semCourseAssignments.add(c2.getString(0));
						tv_displaySemContents.append("\t\t" + c2.getString(0)
								+ "\n");
					}
					tv_displaySemContents.append("\n");
				}
			} else { // role == "Teacher"
				Cursor c = db.query("tbl_TeacherCourse",
						new String[] { "CourseCode, CourseName" },
						"semesterDetails ='"
								+ passedFromShowSemesterListSelection + "'",
						null, null, null, null);
				while (c.moveToNext()) {
					semCourseCodes.add(c.getString(0));
					semCourseNames.add(c.getString(1));

					// print courseCode and courseName on one line
					tv_displaySemContents.append("\t" + c.getString(0) + ": "
							+ c.getString(1) + "\n");
					Cursor c2 = db.query("tbl_TeacherAssignment",
							new String[] { "assignmentTitle" },
							"assignmentCourse ='" + c.getString(1) + "'", null,
							null, null, null);
					// print assignments under one course below the
					// courseCode/Name
					// line
					while (c2.moveToNext()) {
						semCourseAssignments.add(c2.getString(0));
						tv_displaySemContents.append("\t\t" + c2.getString(0)
								+ "\n");
					}
					tv_displaySemContents.append("\n");
				}
			}

			// for (int line = 0; line < semCourseCodes.size(); line++){
			// tv_displaySemContents.append(semCourseCodes.get(line)+": "+semCourseNames.get(line)+"\n");
			// }

			// //Code for popup
			final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
					this);
			// new AlertDialog.Builder(this);
			helpBuilder.setTitle("SUCCESS!");
			helpBuilder.setMessage("Semester deleted");
			helpBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do nothing but close the dialog
							Intent intent = new Intent(
									DeleteSemesterConfirmationActivity.this,
									ShowSemestersActivity.class);
							startActivity(intent);
						}
					});
			// //Code for pop up end

			final Button btnCancelDeleteSemester = (Button) findViewById(R.id.btn_cancelDeleteSem);
			final Button btnConfirmDeleteSemester = (Button) findViewById(R.id.btn_deleteSem);

			btnCancelDeleteSemester
					.setOnClickListener(new View.OnClickListener() {

						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(
									DeleteSemesterConfirmationActivity.this,
									ShowSemestersActivity.class);
							startActivity(intent);
						}

					});

			btnConfirmDeleteSemester
					.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							// db.deleteRecord("tbl_Semester", "semesterNo" ,
							// semesterNo);
							DatabaseManager db = new DatabaseManager(
									DeleteSemesterConfirmationActivity.this);
							if (MainActivity.role == "Student") {

								// delete all assignments from selected semester
								for (int recordNum = 0; recordNum < semCourseNames
										.size(); recordNum++) {
									db.deleteRecord("tbl_Assignment",
											"assignmentCourse",
											semCourseNames.get(recordNum));
								}

								// delete all courses from selected semester
								for (int recordNum = 0; recordNum < semCourseNames
										.size(); recordNum++) {
									db.deleteRecord("tbl_Course",
											"semesterDetails",
											passedFromShowSemesterListSelection);
								}
								// delete semester
								db.deleteRecord("tbl_Semester", "semesterNo",
										semesterNo);
							} else { // role == "Teacher"
								// delete all assignments from selected semester
								for (int recordNum = 0; recordNum < semCourseNames
										.size(); recordNum++) {
									db.deleteRecord("tbl_TeacherAssignment",
											"assignmentCourse",
											semCourseNames.get(recordNum));
								}

								// delete all courses from selected semester
								for (int recordNum = 0; recordNum < semCourseNames
										.size(); recordNum++) {
									db.deleteRecord("tbl_TeacherCourse",
											"semesterDetails",
											passedFromShowSemesterListSelection);
								}
								// delete semester
								db.deleteRecord("tbl_TeacherSemester",
										"semesterNo", semesterNo);
							}

							AlertDialog helpDialog = helpBuilder.create();
							helpDialog.show();
						}

					});
			// final Button btnUpdateSemester = (Button)
			// findViewById(R.id.btn_updateSemester);
			// btnUpdateSemester.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// ContentValues cv = new ContentValues();
			//
			// cv.put("semesterNo", semesterNo);
			// cv.put("semesterDetails", txtsemesterName.getText().toString());
			//
			// record[0] = semesterNo;
			// record[1] = txtsemesterName.getText().toString();
			// //Toast.makeText(UpdateSemesterActivity.this, "Your Choice : \'"
			// + record[0] + "\';",
			// // Toast.LENGTH_SHORT).show();
			// db.updateRecord(cv, "tbl_Semester", fields, record);
			// AlertDialog helpDialog = helpBuilder.create();
			// helpDialog.show();
			// }
			// });
			// final Button btnCancelSemester = (Button)
			// findViewById(R.id.btn_cancelSemester);
			// btnCancelSemester.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// onBackPressed();
			// }
			// });
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_semester_confirmation, menu);
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
