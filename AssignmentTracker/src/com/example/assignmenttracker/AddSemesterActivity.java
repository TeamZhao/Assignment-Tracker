package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
	
	// Moved database and table creation to MainActivity, as creating from individual activities was causing issues for me -- Julian
	/**
	private static final String tables[] = { "tbl_Semester" };
	//
	private static final String tableCreatorString[] = { "CREATE TABLE tbl_Semester (semesterNo INTEGER PRIMARY KEY AUTOINCREMENT , semesterDetails TEXT);" };
	**/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_semester);

		// Moved database and table creation to MainActivity, as creating from individual activities was causing issues for me -- Julian
		/** 
		final DatabaseManager db = new DatabaseManager(this);
		// db.createDatabase(getApplicationContext());
		MainActivity.db.dbInitialize(tables, tableCreatorString);
		**/
		final String fields[] = { "semesterDetails" };
		final String record[] = new String[1];
		// Handle Save button
		final Button btnSaveStudent = (Button) findViewById(R.id.btn_addSemester);
		final EditText semesterName = (EditText) findViewById(R.id.semesterDetails);
		final TextView display = (TextView) findViewById(R.id.TextViewDisplay);
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
		//
		btnSaveStudent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (semesterName.getText().toString().matches("")) {
		 		      // Show the popup
		 		      AlertDialog helpDialog = helpBuilder.create();
		 		      helpDialog.show();
				    return;
				}else{
					record[0] = semesterName.getText().toString();

					// Log.d("Name: ", record[1]);
					// populate the row with some values
					ContentValues values = new ContentValues();
					for (int i = 0; i < record.length; i++)
						values.put(fields[i], record[i]);
					// add the row to the database
					MainActivity.db.addRecord(values, "tbl_Semester", fields, record);
				}

		

			}
		});
		final Button btnShowStudent = (Button) findViewById(R.id.btn_cancelSemester);
		btnShowStudent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Reading all records
				List table = MainActivity.db.getTable("tbl_Semester");

				for (Object o : table) {
					ArrayList row = (ArrayList) o;
					// Writing table to log
					String output = "";
					for (int i = 0; i < row.size(); i++) {
						output += row.get(i).toString() + " ";
						output += "\n";
					}
					display.setText(output);

				}
			}
		});

	}

}
