package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
	
	Date selectedDueDate;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		
		// Moved database and table creation to MainActivity, as creating from individual activities was causing issues for me --Julian
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
		//Code for pop up end	
		
		
		//code to keep track of selected DatePicker date
		assDate.init(assDate.getYear(), assDate.getMonth(), assDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				selectedDueDate = new Date(year-1900, monthOfYear, dayOfMonth);		
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
					record[2] = String.valueOf(selectedDueDate);
					record[3] = String.valueOf(assProgress.getProgress());
					
					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					MainActivity.db.addRecord(values, "tbl_Assignment", fields, record);
//					MainActivity.db.close();
				}
				
				
				
			}
		});
		final Button btnCancelAss = (Button) findViewById(R.id.cancelAss);
		btnCancelAss.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

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
			}
		});
		
	}
//	protected void onDestroy() {
//	    super.onDestroy();
//	    if (MainActivity.db != null) {
//	    	MainActivity.db.close();
//	    }
//	}
	
}
