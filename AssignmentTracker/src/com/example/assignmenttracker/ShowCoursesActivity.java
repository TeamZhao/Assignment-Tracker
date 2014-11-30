package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ShowCoursesActivity extends ActionBarActivity {

	SQLiteDatabase db = MainActivity.db.getReadableDatabase();
	
	ListView courseListView;
	String contentOfSelectedCourseListItem;
	int courseID;
	int cursorIterator;
	String pickSemester;
	boolean assignmentsExistForCourse;
	
	ArrayList<String> associatedAssignments = new ArrayList<String>();
	HashMap<String, List<String>> assignmentCourses;
	List<String> coursesList;
	ExpandableListView assignmentExpView;
	ShowCoursesAdapter showCoursesAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_assignments);
		
		// Update Semester Intent
		Intent semIntent = getIntent();
		pickSemester = semIntent.getStringExtra("semesterDetails");
		if (pickSemester != null) {
			pickSemester = "semesterDetails = \'"
					+ semIntent.getStringExtra("semesterDetails") + "\'";
		}
		
		// Populate Hash
		assignmentCourses = getAssignmentInfo();
		coursesList = new ArrayList<String> (assignmentCourses.keySet());
		
		assignmentExpView = (ExpandableListView) findViewById(R.id.assignments_expView);
		showCoursesAdapter = new ShowCoursesAdapter(this, assignmentCourses, coursesList);
		assignmentExpView.setAdapter(showCoursesAdapter);
		registerForContextMenu(assignmentExpView);	

		// Listen when Group Item is long clicked to display context menu.
		assignmentExpView
			.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View v, int position, long id) {
					final String item = (String) parent
							.getItemAtPosition(position);
					contentOfSelectedCourseListItem = item;
					return false;
				}
			});
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
			ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
			// Only create a context menu for child items
			if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) 
			{
				MenuInflater inflater = this.getMenuInflater();
				inflater.inflate(R.menu.context_float_menu, menu);
			}
	}

	public boolean onContextItemSelected(MenuItem item) {	
		Intent intent;
		
		if (MainActivity.role == "Student") {
			intent = new Intent(this, UpdateCourseActivity.class);
	
			ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
			int groupPos = 0, childPos = 0;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
			{
				groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
				childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			}
			
		} else {
			intent = new Intent(this, UpdateTeacherCourseActivity.class);
			
			ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
			int groupPos = 0, childPos = 0;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
			{
				groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
				childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			}
		}

		switch (item.getItemId()) {
		case R.id.context_menu_update:

			intent.putExtra("courseCode", contentOfSelectedCourseListItem);
			startActivity(intent);
			return true;
		case R.id.context_menu_delete:

			if (MainActivity.role == "Student") {
				Cursor cursorAllAssignments = db.query("tbl_Assignment",
						new String[] { "assignmentTitle", "assignmentCourse" },
						null, null, null, null, null);
				cursorIterator = 0;
				assignmentsExistForCourse = false;

				while (cursorAllAssignments.moveToNext()) {
					if (cursorAllAssignments.getString(1).equalsIgnoreCase(
							this.contentOfSelectedCourseListItem)) {
						associatedAssignments.add(cursorAllAssignments
								.getString(0));
						assignmentsExistForCourse = true;
						cursorIterator++;
					}
				}

				if (assignmentsExistForCourse) {
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
							this);
					helpBuilder.setTitle("Delete Course?");
					helpBuilder
							.setMessage("There are "
									+ cursorIterator
									+ " existing Assignments for this course. Delete the course and its assignments?");
					helpBuilder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Delete Assignment Records
									for (int i = 0; cursorIterator > i; i++) {
										db.delete(
												"tbl_Assignment",
												"assignmentTitle=\'"
														+ String.valueOf(associatedAssignments
																.get(i)) + "\'",
												null);
									} 
									db.delete("tbl_Course", "CourseCode=\'"
											+ contentOfSelectedCourseListItem
											+ "\'", null);
									Toast.makeText(
											getApplicationContext(),
											"Course "
													+ contentOfSelectedCourseListItem
													+ " Deleted",
											Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
								}
							});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
				} else {
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
							this);
					helpBuilder.setTitle("Delete Course?");
					helpBuilder
							.setMessage("Are you sure you want to delete this course?");
					helpBuilder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Delete Course Record
									db.delete("tbl_Course", "CourseCode=\'"
											+ contentOfSelectedCourseListItem
											+ "\'", null);
									Toast.makeText(
											getApplicationContext(),
											"Course "
													+ contentOfSelectedCourseListItem
													+ " Deleted",
											Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
								}
							});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
				}
			} else { // role == "Teacher"
				Cursor cursorAllAssignments = db.query("tbl_TeacherAssignment",
						new String[] { "assignmentTitle", "assignmentCourse" },
						null, null, null, null, null);
				cursorIterator = 0;
				assignmentsExistForCourse = false;

				while (cursorAllAssignments.moveToNext()) {
					if (cursorAllAssignments.getString(1).equalsIgnoreCase(
							this.contentOfSelectedCourseListItem)) {
						associatedAssignments.add(cursorAllAssignments
								.getString(0));
						assignmentsExistForCourse = true;
						cursorIterator++;
					}
				}

				if (assignmentsExistForCourse) {
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
							this);
					helpBuilder.setTitle("Delete Course?");
					helpBuilder
							.setMessage("There are "
									+ cursorIterator
									+ " existing Assignments for this course. Delete the course and its assignments?");
					helpBuilder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Delete Assignment Records
									for (int i = 0; cursorIterator > i; i++) {
										db.delete(
												"tbl_TeacherAssignment",
												"assignmentTitle=\'"
														+ String.valueOf(associatedAssignments
																.get(i)) + "\'",
												null);
									}
									db.delete(
											"tbl_TeacherCourse",
											"CourseCode=\'"
													+ contentOfSelectedCourseListItem
													+ "\'", null);
									Toast.makeText(
											getApplicationContext(),
											"Course "
													+ contentOfSelectedCourseListItem
													+ " Deleted",
											Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
								}
							});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
				} else {
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
							this);
					helpBuilder.setTitle("Delete Course?");
					helpBuilder
							.setMessage("Are you sure you want to delete this course?");
					helpBuilder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Delete Course Record
									db.delete(
											"tbl_TeacherCourse",
											"CourseCode=\'"
													+ contentOfSelectedCourseListItem
													+ "\'", null);
									Toast.makeText(
											getApplicationContext(),
											"Course "
													+ contentOfSelectedCourseListItem
													+ " Deleted",
											Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
								}
							});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
				}
			}
			this.recreate();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);

	}*/

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_courses, menu);
		return true;
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (MainActivity.role == "Student") {
			getMenuInflater().inflate(R.menu.add_menu, menu);
			return true; 
		} else {
			getMenuInflater().inflate(R.menu.add_menu_teacher, menu);
			return true;
		}
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
	
	public ArrayList<String> getAllCourseCodes() {
		ArrayList<String> courseList = new ArrayList<String>();	
		String semesterPicked;
		
		if (pickSemester != null) 
			semesterPicked = pickSemester;
		else
			semesterPicked = null;
		
		if (MainActivity.role == "Student") {
			Cursor c = db.query("tbl_Course", new String[] { "courseNo, courseCode, courseName" }, semesterPicked,
					null, null, null, null);
			while (c.moveToNext()) {
				courseList.add(c.getString(1));
			}
		}
		else if (MainActivity.role == "Teacher") {
			Cursor c = db.query("tbl_TeacherCourse", new String[] { "courseNo, courseCode, courseName" }, semesterPicked,
					null, null, null, null);
			while (c.moveToNext()) {
				courseList.add(c.getString(1));
			}
		}
		return courseList;
		
	}
	
	public HashMap<String, List<String>> getAssignmentInfo() {

		
		HashMap<String, List<String>> courseAssignments = new HashMap<String, List<String>>();
		// Write code to retrieve list of 
		ArrayList<String> courseList = getAllCourseCodes();
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		
		if (MainActivity.role == "Student") {
			Cursor cAssignments = db.query("tbl_Assignment",
					new String[] { "assignmentTitle", "assignmentCourse" },null, null, null, null, null);		
			for (String course : courseList)
			{
				ArrayList<String> groupedAssignments = new ArrayList<String>();
				while (cAssignments.moveToNext()) {
					if(cAssignments.getString(1).equalsIgnoreCase(course))
					{
						groupedAssignments.add(cAssignments.getString(0));
					}
				}	
				courseAssignments.put(course, groupedAssignments);
				cAssignments.moveToPosition(-1);
			}
		}
		else if (MainActivity.role == "Teacher") {
			Cursor cAssignments = db.query("tbl_TeacherAssignment",
					new String[] { "assignmentTitle", "assignmentCourse" },null, null, null, null, null);		
			for (String course : courseList)
			{
				ArrayList<String> groupedAssignments = new ArrayList<String>();
				while (cAssignments.moveToNext()) {
					if(cAssignments.getString(1).equalsIgnoreCase(course))
					{
						groupedAssignments.add(cAssignments.getString(0));
					}
				}	
				courseAssignments.put(course, groupedAssignments);
				cAssignments.moveToPosition(-1);
			}	
		}
		//Add Assignment pertaining to each course here in HashMap - Prep for ExpView
		//cAssignments.close();
		return courseAssignments;
	}
	
}
