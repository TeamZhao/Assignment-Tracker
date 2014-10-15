package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {





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
    
    private static final String tables[]={"tbl_Semester"}; 
    //
    private static final String tableCreatorString[] ={"CREATE TABLE tbl_Semester (semesterNo INTEGER PRIMARY KEY AUTOINCREMENT , semesterDetails TEXT);"};
    
 	@Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main); 
        
        final DatabaseManager db = new DatabaseManager(this); 
        //db.createDatabase(getApplicationContext());
        db.dbInitialize( tables,tableCreatorString);
        final String fields[] = {"semesterDetails"};
        final String record[] = new String[1];
        // Handle Save button
 		final Button btnSaveStudent = (Button) findViewById(R.id.button1);
 		final EditText studentName = (EditText) findViewById(R.id.semesterDetails);
 		final TextView display = (TextView) findViewById(R.id.TextViewDisplay);		
		
		//		
		btnSaveStudent.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View v) {

 				record[0]= studentName.getText().toString();
 		
 		        //Log.d("Name: ", record[1]);	       
 		        //populate the row with some values
 		        ContentValues values = new ContentValues();
 		        for (int i=0;i<record.length;i++)
 		        	values.put(fields[i],record[i]);  
 		        //add the row to the database
 		        db.addRecord(values, "tbl_Semester", fields,record);
 		        
 			}
 		});
		final Button btnShowStudent = (Button) findViewById(R.id.button2);
 		btnShowStudent.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View v) {

 				// Reading all records 
 		        List table = db.getTable("tbl_Semester");        
 		  
 		        for (Object o : table) { 
 		            ArrayList row = (ArrayList)o; 
 		                // Writing table to log
 		            String output="";
 		            for (int i=0;i<row.size();i++)
 		            {
 		            	output+= row.get(i).toString() + " ";
 		            	output+="\n";
 		            }
 		           display.setText(output);   	
 		           
 		        } 
 			}
 		});
        
    } 

}
