package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentFragment extends ListFragment  {
	int assID;
	String contentOfSelectedCourseListItem;
	SQLiteDatabase db = MainActivity.db.getReadableDatabase();
	int cursorIterator;
	boolean assignmentsExistForCourse;
	ArrayList<String> associatedAssignments = new ArrayList<String>();
	String clickedAssignmentTitle;
	
	HashMap<String, List<String>> assignmentCourses;
	List<String> coursesList;
	ExpandableListView assignmentExpView;
	AssignmentAdapter2 assignmentAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// List table = MainActivity.db.getTable("tbl_Assignment");
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		
		if (MainActivity.role == "Student") {
			
			// Initialize view in OnCreateView
			
			// Populate Hash
			assignmentCourses = getAssignmentInfo();
			coursesList = new ArrayList<String> (assignmentCourses.keySet());

		} else if (MainActivity.role == "Teacher") {

			List<Map<String, Object>> assMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			Cursor c = db.query("tbl_TeacherCourse",
					new String[] { "courseNo, courseCode, courseName" }, null,
					null, null, null, null); // change to tbl_TeacherCourse
			ArrayList<String> courseValues = new ArrayList<String>();
			while (c.moveToNext()) {
				courseValues.add(c.getString(1));
			}
			ArrayAdapter adapter2 = new ArrayAdapter(this.getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1,
					courseValues);
			this.setListAdapter(adapter2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (MainActivity.role == "Student") {		
			View expView = inflater.inflate(R.layout.list_assignments, container, false);
			return expView;
		}
		else if (MainActivity.role == "Teacher") 
		{
			View list = inflater.inflate(R.layout.fragment_show_assignment, container, false);
			return list;
		}	
		return null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		if (MainActivity.role == "Student") {		
			assignmentExpView = (ExpandableListView) view.findViewById(R.id.assignments_expView);
			assignmentAdapter = new AssignmentAdapter2(this.getActivity(), assignmentCourses, coursesList);
			assignmentExpView.setAdapter(assignmentAdapter);
			registerForContextMenu(assignmentExpView);	
		}
		else if (MainActivity.role == "Teacher") 
		{
			registerForContextMenu(getListView());
		}				
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	
		if (MainActivity.role == "Student") {	
		
			ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
			// Only create a context menu for child items
			if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
			{
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.context_float_menu, menu);
			}
		}
		else if (MainActivity.role == "Teacher") 
		{
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.context_float_menu, menu);
		}    
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if (MainActivity.role == "Student") {
			
			ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
			int groupPos = 0, childPos = 0;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
			{
				groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
				childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
				clickedAssignmentTitle = assignmentCourses.get(coursesList.get(groupPos)).get(childPos);
			}

			Intent intent;
			intent = new Intent(getActivity(), UpdateAssignmentActivity.class);			

			switch (item.getItemId()) {
			case R.id.context_menu_update:	
				Log.v("Clicked:", clickedAssignmentTitle);
				intent.putExtra("assTitle", clickedAssignmentTitle);
				startActivity(intent);
				return true;
			case R.id.context_menu_delete:
				// Confirm Deletion
				new AlertDialog.Builder(getActivity())
						.setTitle("Confirm Deletion")
						.setMessage(
								"Do you really want to delete this Assignment?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Delete Record
										db.delete(
												"tbl_Assignment",
												"assignmentTitle=\'"
														+ clickedAssignmentTitle
														+ "\'", null);
										Toast.makeText(
												getActivity(),
												"Assignment"
														+ clickedAssignmentTitle
														+ "Deleted",
												Toast.LENGTH_SHORT).show();
										getActivity().recreate();
									}
								}).setNegativeButton(android.R.string.no, null)
						.show();
				// Do Nothing
				return true;
			default:
				return super.onContextItemSelected(item);
			}
		} else { // role == "Teacher"
			
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			ListView templist = this.getListView();
			View mView = templist.getChildAt(info.position);
			// String val =info.getItemAtPosition(info.position);
			Intent intent;
			
			intent = new Intent(getActivity(),
					UpdateTeacherCourseActivity.class);

			switch (item.getItemId()) {
			case R.id.context_menu_update:
				intent.putExtra("courseCode", contentOfSelectedCourseListItem);
				startActivity(intent);
				return true;
			case R.id.context_menu_delete:
				// Confirm Deletion
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

					new AlertDialog.Builder(getActivity())
							.setTitle("Delete Course?")
							.setMessage(
									"There are "
											+ cursorIterator
											+ " existing Assignments for this course. Delete the course and its assignments?")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// Delete Assignment Records
											for (int i = 0; cursorIterator > i; i++) {
												db.delete(
														"tbl_TeacherAssignment",
														"assignmentTitle=\'"
																+ String.valueOf(associatedAssignments
																		.get(i))
																+ "\'", null);
											}
											db.delete(
													"tbl_TeacherCourse",
													"CourseCode=\'"
															+ contentOfSelectedCourseListItem
															+ "\'", null);
											Toast.makeText(
													getActivity(),
													"Course "
															+ contentOfSelectedCourseListItem
															+ " Deleted",
													Toast.LENGTH_SHORT).show();
										}
									})
							.setNegativeButton(android.R.string.no, null)
							.show();
					// Do Nothing
					getActivity().recreate();
					return true;
				}
			default:
				return super.onContextItemSelected(item);
			}
		}

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		if (MainActivity.role == "Student") {
			Toast.makeText(getActivity(), "Your Choice : " + position,
					Toast.LENGTH_SHORT).show();
		} else {

			Toast.makeText(getActivity(),
					"Your Choice : " + parent.getItemAtPosition(position),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onListItemLongClick(ListView parent, View v, int position,
			long id) {
		String item = (String) parent.getItemAtPosition(position);
		contentOfSelectedCourseListItem = item;
		if (MainActivity.role == "Student") {
			Toast.makeText(getActivity(), "Your Choice : " + position,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "Your Choice : " + item,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public ArrayList<String> getAllCourseCodes() {
		ArrayList<String> courseList = new ArrayList<String>();
		Cursor c = db.query("tbl_Course", new String[] { "courseNo, courseCode, courseName" }, null,
				null, null, null, null); // change to tbl_TeacherCourse		
		while (c.moveToNext()) {
			courseList.add(c.getString(1));
		}
		return courseList;
	}
	
	public HashMap<String, List<String>> getAssignmentInfo() {

		HashMap<String, List<String>> courseAssignments = new HashMap<String, List<String>>();
		// Write code to retrieve list of 
		ArrayList<String> courseList = getAllCourseCodes();
		Cursor cAssignments = db.query("tbl_Assignment",
				new String[] { "assignmentTitle", "assignmentCourse" }, null, null, null, null, null);		

		//Add Assignment pertaining to each course here in HashMap - Prep for ExpView
		for (String course : courseList)
		{
			int j = 0;
			ArrayList<String> groupedAssignments = new ArrayList<String>();
			while (cAssignments.moveToNext()) {
				if(cAssignments.getString(1).equalsIgnoreCase(course))
				{
					groupedAssignments.add(cAssignments.getString(0));
				}
				j++;
			}	
			courseAssignments.put(course, groupedAssignments);
			cAssignments.moveToPosition(-1);
		}
		cAssignments.close();
		return courseAssignments;
	}
}
