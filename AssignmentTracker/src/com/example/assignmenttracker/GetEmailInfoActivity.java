package com.example.assignmenttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetEmailInfoActivity extends Activity {
	String assTitle = "asdsad", emailDestination = "emailDest",
			emailSubject = "emailSub", emailMessage = "emailMs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_email_info);
		Intent intent = getIntent();
		assTitle = intent.getStringExtra("assTitle");

		final EditText emailDestBox = (EditText) findViewById(R.id.txtEmailAdd);
		final EditText txtEmailSubject = (EditText) findViewById(R.id.txtSubject);
		final EditText txtEmailMessage = (EditText) findViewById(R.id.txt_OptionalMessage);

		// Code for popup
		final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder.setTitle("ERROR!");
		helpBuilder.setMessage("Please enter an email address");
		helpBuilder.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
					}
				});

		final Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				emailDestination = emailDestBox.getText().toString();
				emailSubject = txtEmailSubject.getText().toString();
				emailMessage = txtEmailMessage.getText().toString();
				if (emailDestination.matches("")) {
					// Show the popup
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					return;
				} else {
					
					Intent emailContentIntent = new Intent(
							GetEmailInfoActivity.this,
							EmailContentToSendActivity.class);
					emailContentIntent.putExtra("assTitle", assTitle);
					emailContentIntent.putExtra("emailTo", emailDestination);
					emailContentIntent.putExtra("emailSubject", emailSubject);
					emailContentIntent.putExtra("emailMsg", emailMessage);
					startActivity(emailContentIntent);

					finish();
				}
			}
		});

		final Button btnCancelEmail1 = (Button) findViewById(R.id.btnCancelEmail1);
		btnCancelEmail1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (MainActivity.role == "Student") {
					// go back to main
					Intent cancelIntent = new Intent(GetEmailInfoActivity.this,
							MainActivity.class);
					startActivity(cancelIntent);
				} else { // role == "Teacher"
					// go back to show assignments
					Intent cancelIntent = new Intent(GetEmailInfoActivity.this,
							ShowTeacherAssignmentsActivity.class);
					startActivity(cancelIntent);
				}
				finish();
			}
		});
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
			if (MainActivity.role == "Student") {
				adds = new Intent(this, AddCourseActivity.class);
				startActivity(adds);
			} else if (MainActivity.role == "Teacher") {
				adds = new Intent(this, AddTeacherCourseActivity.class);
				startActivity(adds);
			}
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
		case R.id.show_teacher_assignments:
			adds = new Intent(this, ShowTeacherAssignmentsActivity.class);
			startActivity(adds);
			return true;
		case R.id.show_progress_report:
			adds = new Intent(this, AssignmentProgressReportActivity.class);
			startActivity(adds);
			return true;
		case R.id.show_devtools:
			adds = new Intent(this, DevTools.class);
			startActivity(adds);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (MainActivity.role == "Student") {
			getMenuInflater().inflate(R.menu.add_menu, menu);
			return true;
		} else {
			getMenuInflater().inflate(R.menu.add_menu_teacher, menu);
			return true;
		}
	}

}
