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
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
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

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
	
	// Moved database and table creation to MainActivity, as creating from individual activities was causing issues for me --Julian
	/**
	private static final String tablesAssignment[] = { "tbl_Assignment" };
	//
	private static final String tableCreatorStringAss[] = { "CREATE TABLE tbl_Assignment (assignmentNo INTEGER PRIMARY KEY AUTOINCREMENT , assignmentTitle TEXT , assignmentCourse TEXT , assignmentDueDate TEXT , assignmentProgress TEXT);" };
	**/
	private static final int DAY_IN_MILLISECS = 86400000;
	
	//Date selectedDueDate = new Date();
	String formatedDate;
	Calendar selectedDueDate = Calendar.getInstance();
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		

		/**
		final DatabaseManager db = new DatabaseManager(this);
		// db.createDatabase(getApplicationContext());
		MainActivity.db.dbInitialize(tablesAssignment, tableCreatorStringAss);
		**/
		final String fields[] = { "assignmentTitle", "assignmentCourse", "assignmentDueDate", "assignmentProgress" };
		final String record[] = new String[4];
		// Handle all elements on page
		final Button btnAddAss = (Button) findViewById(R.id.addAss);
		final EditText assTitle = (EditText) findViewById(R.id.assTitle);
		final Spinner assCourse = (Spinner) findViewById(R.id.assCourseSpinner);
		final DatePicker assDate = (DatePicker) findViewById(R.id.assDatePicker);
		final SeekBar assProgress = (SeekBar) findViewById(R.id.assProgressSeekBar);
		
		final TextView display = (TextView) findViewById(R.id.textView_Test);
		
		//stub//
		//
		//fill course list with fake entries
		String courseEntries[] = { "Course 1","Course 2","Course 3","Course 4"};
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courseEntries);
		assCourse.setAdapter(adapter);
		//
		//endstub//
		
        //Code for popup
        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this); 
				   //new AlertDialog.Builder(this);
		helpBuilder.setTitle("ERROR!");
		helpBuilder.setMessage("Field cannot be empty, Try again");
		helpBuilder.setNegativeButton("Ok",
		  new DialogInterface.OnClickListener() {
		
		   public void onClick(DialogInterface dialog, int which) {
		    // Do nothing but close the dialog
		   }
		  });
		//Code for pop up end	 for calender
		
        //Code for popup
        final AlertDialog.Builder helpBuilder1 = new AlertDialog.Builder(this); 
				   //new AlertDialog.Builder(this);
        helpBuilder1.setTitle("Add to Calender?");
        helpBuilder1.setMessage("Do you want to add this assignment to your google calender?");
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
		//Code for pop up end
		
		
		//code to keep track of selected DatePicker date
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        selectedDueDate.set(assDate.getYear(), assDate.getMonth(), assDate.getDayOfMonth(), 23, 59, 59); //set to end of day
        formatedDate = format.format(selectedDueDate.getTime()); // initialize value to default value in datepicker
        assDate.init(assDate.getYear(), assDate.getMonth(), assDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				selectedDueDate.set(year, monthOfYear, dayOfMonth, 23,59,59);  //set to end of the selected day
				formatedDate = format.format(selectedDueDate.getTime());
				//insertCalTime.set(year, monthOfYear, dayOfMonth);		
			}
		});
		
		/** 
		//test code
		btnAddAss.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				display.setText(String.valueOf(assProgress.getProgress()));
			}
		});
		**/
		
		
		//	
		btnAddAss.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				if (assTitle.getText().toString().matches("")){
		 		      // Show the popup
		 		      AlertDialog helpDialog = helpBuilder.create();
		 		      helpDialog.show();
				    return;
				}else{
					record[0] = assTitle.getText().toString();
					record[1] = assCourse.getSelectedItem().toString();
					record[2] = String.valueOf(formatedDate);
					record[3] = String.valueOf(assProgress.getProgress());
					
					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					MainActivity.db.addRecord(values, "tbl_Assignment", fields, record);
//					MainActivity.db.close();
					// NOTIFICATIONS CODE
				    Calendar calendar = selectedDueDate;
				    //calendar.DAY_OF_MONTH = selectedDueDate.DAY_OF_MONTH - 1;
				      
				    //calendar.set(Calendar.MONTH, 11);
				    //calendar.set(Calendar.YEAR, 2014);
				    calendar.set(Calendar.DAY_OF_MONTH, selectedDueDate.DAY_OF_MONTH - 1);
				 
				    calendar.set(Calendar.HOUR_OF_DAY, 14);
				    calendar.set(Calendar.MINUTE, 33);
				    calendar.set(Calendar.SECOND, 0);
				    String title = assTitle.getText().toString() + " due";
				    String content = String.valueOf(selectedDueDate.get(Calendar.MONTH)) + "/" + String.valueOf(selectedDueDate.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(selectedDueDate.get(Calendar.YEAR));
					Notification notification = getNotification(content, title);
			        Intent notificationIntent = new Intent(AddAssignmentActivity.this, MainActivity.class);
			        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
			        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
			        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAssignmentActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			 
			        long futureInMillis = SystemClock.elapsedRealtime() + 10000;
			        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
			        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
					AlertDialog helpDialog1 = helpBuilder1.create();
					helpDialog1.show();
				}
				
				
				
			}
		});
		final Button btnCancelAss = (Button) findViewById(R.id.cancelAss);
		btnCancelAss.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed(); //go back to previous page on Cancel
/*
//test code to display database records
 				// Reading all records
				List table = MainActivity.db.getTable("tbl_Assignment");

				for (Object o : table) {
					ArrayList row = (ArrayList) o;
					// Writing table to log
					String output = "";
					for (int i = 0; i < row.size(); i++) {
						output += row.get(i).toString() + " ";
						output += "\n";
					}
					display.setText(output);
//					MainActivity.db.close();

				}
*/
			}
		});
		
	}
//	protected void onDestroy() {
//	    super.onDestroy();
//	    if (MainActivity.db != null) {
//	    	MainActivity.db.close();
//	    }
//	}
	
	private void insertNewEventIntoCalendar(EditText assTitle) {
		    /**
		     *  Inserting a new calendar event using an Intent
		     */
		    // Create a new insertion Intent.
		    Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
		  
		    // Add the calendar event details
		    intent.putExtra(CalendarContract.Events.TITLE, assTitle.getText().toString());
		    intent.putExtra(CalendarContract.Events.DESCRIPTION, 
		                    "Professional Android 4 " +
		                    "Application Development release!");
		    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
		  
		    //Calendar startTime = Calendar.getInstance();
		    //startTime.set(2012, 2, 13, 0, 30);
		    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, selectedDueDate.getTimeInMillis()-DAY_IN_MILLISECS );
		   
		    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);    
		  
		     // Use the Calendar app to add the new event.
		    startActivity(intent);
		  }
	
    private Notification getNotification(String content, String title) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }
	
}
