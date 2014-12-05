package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.text.method.DateTimeKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class AddAssignmentActivity extends ActionBarActivity {

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

	public static final int TIME_TO_END_OF_DAY = 86340000; // 23 hrs, 59 mins
	public static final int DAY_IN_MILLISECS = 86400000;
	public static int alarmID;
	//public static NotificationManager mNotifyMgr;

	// Date selectedDueDate = new Date();
	String formatedDate, selectedCourse;
	Calendar selectedDueDate = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);

		// Moved database and table creation to MainActivity, as creating from
		// individual activities was causing issues for me --Julian
		/**
		 * final DatabaseManager db = new DatabaseManager(this); //
		 * db.createDatabase(getApplicationContext());
		 * MainActivity.db.dbInitialize(tablesAssignment,
		 * tableCreatorStringAss);
		 **/
		final String fields[] = { "assignmentTitle", "assignmentCourse",
				"assignmentDueDate", "assignmentProgress" };
		final String record[] = new String[4];
		// Handle all elements on page
		final Button btnAddAss = (Button) findViewById(R.id.addAss);
		final EditText assTitle = (EditText) findViewById(R.id.assTitle);
		final DatePicker assDate = (DatePicker) findViewById(R.id.assDatePicker);
		final SeekBar assProgress = (SeekBar) findViewById(R.id.assProgressSeekBar);

		final TextView display = (TextView) findViewById(R.id.textView_Test);
		final Spinner courses = (Spinner) findViewById(R.id.assCourseSpinner);

		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c;
		if (MainActivity.role == "Student") {
			c = db.query("tbl_Course", new String[] { "CourseName" }, null,
					null, null, null, null);
		} else { // role == "Teacher"
			c = db.query("tbl_TeacherCourse", new String[] { "CourseName" },
					null, null, null, null, null);
		}

		String[] array_spinner = new String[c.getCount()];
		if(c.getCount()==0)
		{
			// Code for popup
			final AlertDialog.Builder helpBuilder2 = new AlertDialog.Builder(this);
			// new AlertDialog.Builder(this);
			helpBuilder2.setTitle("ERROR!");
			helpBuilder2.setMessage("Please create a course before creating a course");
			helpBuilder2.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do nothing but close the dialog
							onBackPressed();
						}
					});
			// Code for pop up end
			AlertDialog helpDialog = helpBuilder2.create();
			helpDialog.show();
		}
		int counter = 0;
		while (c.moveToNext()) {
			array_spinner[counter] = c.getString(0);
			counter++;
		}

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, array_spinner);
		courses.setAdapter(adapter);

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
		// Code for pop up end for calender

		// Code for popup
		final AlertDialog.Builder helpBuilder1 = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this);
		helpBuilder1.setTitle("Add to Calender?");
		helpBuilder1
				.setMessage("Do you want to add this assignment to your google calender?");
		helpBuilder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Code for calender
						insertNewEventIntoCalendar(assTitle);
					}
				});
		helpBuilder1.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
					}
				});
		// Code for pop up end

		// code to keep track of selected DatePicker date
		final SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		selectedDueDate.set(assDate.getYear(), assDate.getMonth(),
				assDate.getDayOfMonth(), 23, 59, 59); // set to end of day
		formatedDate = format.format(selectedDueDate.getTime()); // initialize
																	// value to
																	// default
																	// value in
																	// datepicker
		assDate.init(assDate.getYear(), assDate.getMonth(),
				assDate.getDayOfMonth(),
				new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						selectedDueDate.set(year, monthOfYear, dayOfMonth, 23,
								59, 59); // set to end of the selected day
						formatedDate = format.format(selectedDueDate.getTime());
						// insertCalTime.set(year, monthOfYear, dayOfMonth);
					}
				});

		/**
		 * //test code btnAddAss.setOnClickListener(new View.OnClickListener() {
		 * public void onClick(View v) {
		 * display.setText(String.valueOf(assProgress.getProgress())); } });
		 **/

		//
		btnAddAss.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (assTitle.getText().toString().matches("")) {
					// Show the popup
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					return;
				} else {
					selectedCourse = courses.getSelectedItem().toString();
					record[0] = assTitle.getText().toString();
					record[1] = selectedCourse;
					record[2] = String.valueOf(formatedDate);
					record[3] = String.valueOf(assProgress.getProgress());

					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					if (MainActivity.role == "Student") {
						MainActivity.db.addRecord(values, "tbl_Assignment",
								fields, record);
					} else { // role == "Teacher"
						MainActivity.db.addRecord(values,
								"tbl_TeacherAssignment", fields, record);
					}
					// MainActivity.db.close();

					// NEW CODE FOR NOTIFICATION
					String title;
					String content = String.valueOf(selectedDueDate
							.get(Calendar.MONTH))
							+ "/"
							+ String.valueOf(selectedDueDate
									.get(Calendar.DAY_OF_MONTH))
							+ "/"
							+ String.valueOf(selectedDueDate.get(Calendar.YEAR));
					
					long selectedDueDateInMillis = selectedDueDate.getTimeInMillis() - TIME_TO_END_OF_DAY;
					
					long dayBeforeInMillis = selectedDueDateInMillis-DAY_IN_MILLISECS;
					long twoDaysBeforeInMillis = dayBeforeInMillis - DAY_IN_MILLISECS;
					long threeDaysBeforeInMillis = twoDaysBeforeInMillis - DAY_IN_MILLISECS;
					if (MainActivity.role == "Student") {
						title = assTitle.getText().toString() + " is due";
										
					
						//Generating notifications for teach after the due date
						
						//createNotificationAlarm(1000, title, content, " tomorrow");
						createNotificationAlarm(selectedDueDateInMillis, title, content, " today! Remember to submit!");
						createNotificationAlarm(dayBeforeInMillis, title, content, " tomorrow");
						createNotificationAlarm(twoDaysBeforeInMillis, title, content, " in 2 days");
						createNotificationAlarm(threeDaysBeforeInMillis, title, content, " in 3 days");
					} else { // (MainActivity.role == "Teacher")
						dayBeforeInMillis = selectedDueDateInMillis+DAY_IN_MILLISECS;
						twoDaysBeforeInMillis = dayBeforeInMillis + DAY_IN_MILLISECS;
						threeDaysBeforeInMillis = twoDaysBeforeInMillis + DAY_IN_MILLISECS;
						
						title = assTitle.getText().toString();
												
						//Generating notifications for teach after the due date
						
						//createNotificationAlarm(1000, title, content, " tomorrow");
						createNotificationAlarm(selectedDueDateInMillis , title, content, " is due today, please begin grading!");
						createNotificationAlarm(dayBeforeInMillis, title, content, " was due yesterday, lets get grading!");
						createNotificationAlarm(twoDaysBeforeInMillis, title, content, " was due 2 days ago, how's the grading coming along?");
						createNotificationAlarm(threeDaysBeforeInMillis, title, content, " was due 3 days ago, get those grades in!");
					}
					// NOTIFICATION CODE END

					AlertDialog helpDialog1 = helpBuilder1.create();
					helpDialog1.show();
				}

			}
		});

		final Button btnCancelAss = (Button) findViewById(R.id.cancelAss);
		btnCancelAss.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goback = new Intent(AddAssignmentActivity.this,
						MainActivity.class);
				startActivity(goback); // go back to previous page on Cancel
				/*
				 * //test code to display database records // Reading all
				 * records List table =
				 * MainActivity.db.getTable("tbl_Assignment");
				 * 
				 * for (Object o : table) { ArrayList row = (ArrayList) o; //
				 * Writing table to log String output = ""; for (int i = 0; i <
				 * row.size(); i++) { output += row.get(i).toString() + " ";
				 * output += "\n"; } display.setText(output); //
				 * MainActivity.db.close();
				 * 
				 * }
				 */
			}
		});

	}

	// protected void onDestroy() {
	// super.onDestroy();
	// if (MainActivity.db != null) {
	// MainActivity.db.close();
	// }
	// }

	private void insertNewEventIntoCalendar(EditText assTitle) {
		/**
		 * Inserting a new calendar event using an Intent
		 */
		// Create a new insertion Intent.
		Intent intent = new Intent(Intent.ACTION_INSERT,
				CalendarContract.Events.CONTENT_URI);

		// Add the calendar event details
		intent.putExtra(CalendarContract.Events.TITLE, assTitle.getText()
				.toString());
		intent.putExtra(CalendarContract.Events.DESCRIPTION, "Assignment: "
				+ assTitle.getText().toString() + " due for course: "
				+ selectedCourse);
		intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");

		// Calendar startTime = Calendar.getInstance();
		// startTime.set(2012, 2, 13, 0, 30);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				selectedDueDate.getTimeInMillis() - TIME_TO_END_OF_DAY);

		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// Use the Calendar app to add the new event.
		startActivity(intent);
	}

	/*
	 * Method not used with notification fix
	 * 
	 * @SuppressLint("NewApi") // ignore API level 16 (builder.build requires
	 * 16) call because min sdk set // to 14 private Notification
	 * getNotification(String content, String title) { Notification.Builder
	 * builder = new Notification.Builder(this); builder.setContentTitle(title);
	 * builder.setContentText(content);
	 * builder.setSmallIcon(R.drawable.ic_launcher); return builder.build();
	 * 
	 * // Notification builder = new Notification.Builder(this) //
	 * .setContentTitle(title) // .setContentText(content) //
	 * .setSmallIcon(R.drawable.ic_launcher) // .build(); // // return builder;
	 * 
	 * }
	 */
	
	private void createNotificationAlarm(long timeInMillis, String title, String content, String daysLeft){
		Intent notificationIntent3days = new Intent(
				getBaseContext(),
				NotificationPublisher.class);
		notificationIntent3days.putExtra("Title", title+daysLeft);
		notificationIntent3days.putExtra("Content", content);
		
		PendingIntent resultPendingIntent3days = PendingIntent
				.getBroadcast(AddAssignmentActivity.this, alarmID,
						notificationIntent3days,
						PendingIntent.FLAG_UPDATE_CURRENT);

		// create notification for 3 days before
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, timeInMillis,
				resultPendingIntent3days);
		
		alarmID++;
	}

}
