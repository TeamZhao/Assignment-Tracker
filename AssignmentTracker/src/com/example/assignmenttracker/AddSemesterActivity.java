package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddSemesterActivity extends ActionBarActivity {

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
		setContentView(R.layout.activity_add_semester);

		// Moved database and table creation to MainActivity, as creating from
		// individual activities was causing issues for me -- Julian
		/**
		 * final DatabaseManager db = new DatabaseManager(this); //
		 * db.createDatabase(getApplicationContext());
		 * MainActivity.db.dbInitialize(tables, tableCreatorString);
		 **/
		final String fields[] = { "semesterDetails" };
		final String record[] = new String[1];
		// Handle Save button
		final Button btnSaveStudent = (Button) findViewById(R.id.btn_updateSemester);
		final EditText semesterName = (EditText) findViewById(R.id.semesterDetails);
		final TextView display = (TextView) findViewById(R.id.TextViewDisplay);
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
		helpBuilder1.setTitle("SUCCESS");
		helpBuilder1.setMessage("Semester Added");
		helpBuilder1.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Close the dialog then go back
						dialog.dismiss();
						onBackPressed();
					}
				});
		// Code for pop up end
		//
		btnSaveStudent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (semesterName.getText().toString().matches("")) {
					// Show the popup
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					return;
				} else {
					record[0] = semesterName.getText().toString();

					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					if (MainActivity.role == "Student") {
						MainActivity.db.addRecord(values, "tbl_Semester",
								fields, record);
					} else { // role == "Teacher"
						MainActivity.db.addRecord(values,
								"tbl_TeacherSemester", fields, record);
					}
					AlertDialog helpDialog = helpBuilder1.create();
					helpDialog.show();
					
				}

			}
		});
		final Button btnShowStudent = (Button) findViewById(R.id.btn_cancelSemester);
		btnShowStudent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

}
