package com.example.assignmenttracker;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
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
			// Add code to pull affiliated assignments
			// Add code to prompt deletion of all pertaining assignments (if any)
			// Add confirmation Message Code				

			Cursor cursorAllAssignments = db.query("tbl_Assignment", new String[] {"assignmentTitle", "assignmentCourse" }, null, null, null, null, null);
			cursorIterator = 0;
			// Stores all found associated assignments
			ArrayList<String> associatedAssignments = new ArrayList<String>();
			assignmentsExistForCourse = false;
			while (cursorAllAssignments.moveToNext()) {
				Toast.makeText(getApplicationContext(),cursorAllAssignments.getString(1),Toast.LENGTH_LONG).show();
				if (cursorAllAssignments.getString(1).equalsIgnoreCase(this.contentOfSelectedCourseListItem)) {
					associatedAssignments.add(cursorAllAssignments.getString(1));
					assignmentsExistForCourse = true;
				}
				cursorIterator++;
			}			
			//MainActivity.db.deleteRecord("tbl_Course", "CourseCode",
			//		contentOfSelectedCourseListItem);
			//this.recreate();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
// Temporary Reference integrity logic 	
/*	 AdapterView.AdapterContextMenuInfo info =
	 (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	 
	 ListView templist = this.getListView();
	 View mView = templist.getChildAt(info.position);
	 //String val =info.getItemAtPosition(info.position);
	 Intent intent = new Intent(getActivity(), UpdateAssignmentActivity.class);
	 TextView mytextview = (TextView) mView.findViewById(R.id.id_ass);
	 assID = Integer.parseInt(mytextview.getText().toString());
	 final String str;
	 String selectedFromList = (templist.getItemAtPosition((int) info.position).toString());
	 String lines[] = selectedFromList.split("title=");
	 str = lines[1].substring(0, lines[1].length()-1);
	switch (item.getItemId()) {
	case R.id.context_menu_update:

        //Toast.makeText(getActivity().getApplicationContext(), str,Toast.LENGTH_LONG).show();
		intent.putExtra("assTitle",String.valueOf(str));
		startActivity(intent);
		return true;
	case R.id.context_menu_delete:*/
	

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
