package com.example.assignmenttracker;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateSemesterActivity extends ActionBarActivity {

	String passedFromShowSemesterListSelection;
	public String record[] = new String[2];
	final String fields[] = { "semesterNo", "semesterDetails" };
	public static DatabaseManager db;
	public String semesterNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_semester);
		db = new DatabaseManager(this);
		// gets list item content passed in from ShowSemesterActivity -- in this
		// case, the courseCode
		// use passedFromShowSemesterListSelection as the search string to
		// search for the database record
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			passedFromShowSemesterListSelection = extras
					.getString("semesterDetails");
			semesterNo = extras.getString("semesterNo");
			// Toast.makeText(UpdateSemesterActivity.this, "Your Choice :" +
			// semesterNo,
			// Toast.LENGTH_SHORT).show();
			final EditText txtsemesterName = (EditText) findViewById(R.id.semesterDetails);
			txtsemesterName.setText(passedFromShowSemesterListSelection);

			// Code for popup
			final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
					this);
			// new AlertDialog.Builder(this);
			helpBuilder.setTitle("SUCCESS!");
			helpBuilder.setMessage("Assignment updated");
			helpBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do nothing but close the dialog
							Intent intent = new Intent(
									UpdateSemesterActivity.this,
									ShowSemestersActivity.class);
							startActivity(intent);
						}
					});
			// Code for pop up end

			final Button btnUpdateSemester = (Button) findViewById(R.id.btn_updateSemester);
			btnUpdateSemester.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					ContentValues cv = new ContentValues();

					cv.put("semesterNo", semesterNo);
					cv.put("semesterDetails", txtsemesterName.getText()
							.toString());

					record[0] = semesterNo;
					record[1] = txtsemesterName.getText().toString();
					// Toast.makeText(UpdateSemesterActivity.this,
					// "Your Choice : \'" + record[0] + "\';",
					// Toast.LENGTH_SHORT).show();
					if (MainActivity.role == "Student") {
						db.updateRecord(cv, "tbl_Semester", fields, record);
					} else { // role == "Teacher"
						db.updateRecord(cv, "tbl_TeacherSemester", fields,
								record);
					}
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
				}
			});
			final Button btnCancelSemester = (Button) findViewById(R.id.btn_cancelSemester);
			btnCancelSemester.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_semester, menu);
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