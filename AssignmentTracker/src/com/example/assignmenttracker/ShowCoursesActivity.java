package com.example.assignmenttracker;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCoursesActivity extends ActionBarActivity {

	ListView courseListView;
	String contentOfSelectedCourseListItem;
	int courseID;
	int cursorIterator;
	boolean assignmentsExistForCourse;
	SQLiteDatabase db = MainActivity.db.getReadableDatabase();
	ArrayList<String> associatedAssignments = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_courses);

		courseListView = (ListView) findViewById(R.id.listView_show_courses);
		registerForContextMenu(courseListView);
		
		Cursor c = db.query("tbl_Course",
				new String[] { "CourseCode, CourseName" }, null, null, null,
				null, null);

		ArrayList<String> values = new ArrayList<String>();
		while (c.moveToNext()) {
			values.add(c.getString(0));
		}

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		courseListView.setAdapter(adapter);

		courseListView
				.setOnItemLongClickListener(new OnItemLongClickListener() { //set value of selected list item on longpress

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						final String item = (String) parent.getItemAtPosition(position);// value
																						// of
																						// selected
																						// list
																						// item
																						// as
																						// string
						contentOfSelectedCourseListItem = item;
						return false;
					}
				});

		courseListView.setOnItemClickListener(new OnItemClickListener() { //shows toast on single tap

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);// value
																				// of
																				// selected
																				// list
																				// item
																				// as
																				// string
				Toast.makeText(getApplicationContext(),
						"Your Choice : " + item, Toast.LENGTH_SHORT).show();
				contentOfSelectedCourseListItem = item;

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_courses, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.context_float_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		ListView coursesList = (ListView)findViewById(R.id.listView_show_courses);
		View coursesView = coursesList.getChildAt(info.position);
		Intent intent = new Intent(this, UpdateCourseActivity.class);
		courseID = coursesList.getId();
		//Toast.makeText(getApplicationContext(), "COURSEID +" + courseID,Toast.LENGTH_LONG).show();
		// Add code to fetch realID of selected course
		// Current behaviour deletes courses with existing assignments successfully, but defaults to another existing course - add logic to fix
		
		switch (item.getItemId()) {
		case R.id.context_menu_update:
			
			intent.putExtra("courseCode", contentOfSelectedCourseListItem); 
				// can you use this extra in the update activity to search for db record containing this value?
			startActivity(intent);
			return true;
		case R.id.context_menu_delete:
			
			Cursor cursorAllAssignments = db.query("tbl_Assignment", new String[] {"assignmentTitle", "assignmentCourse" }, null, null, null, null, null);
			cursorIterator = 0;
			assignmentsExistForCourse = false;

			while (cursorAllAssignments.moveToNext()) {
				if (cursorAllAssignments.getString(1).equalsIgnoreCase(this.contentOfSelectedCourseListItem)) {
					associatedAssignments.add(cursorAllAssignments.getString(0));
					assignmentsExistForCourse = true;
					cursorIterator++;
				}
			}
			
			if (assignmentsExistForCourse) {
								// Delete Assignment Records
								for (int i = 0; cursorIterator > i; i++) {
									db.delete("tbl_Assignment","assignmentTitle=\'" + String.valueOf(associatedAssignments.get(i))+"\'", null);
								}
								db.delete("tbl_Course","CourseCode=\'" + contentOfSelectedCourseListItem + "\'", null);
								Toast.makeText(getApplicationContext(), "Course " + contentOfSelectedCourseListItem + " Deleted", Toast.LENGTH_SHORT).show();
							}
			else {
								// Delete Course Record
								db.delete("tbl_Course","CourseCode=\'" + contentOfSelectedCourseListItem + "\'", null);
								Toast.makeText(getApplicationContext(), "Course " + contentOfSelectedCourseListItem + " Deleted", Toast.LENGTH_SHORT).show();	
				}
			this.recreate();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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
