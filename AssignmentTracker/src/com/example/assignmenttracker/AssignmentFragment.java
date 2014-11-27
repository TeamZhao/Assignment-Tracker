package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentFragment extends ListFragment {
	int assID;
	String contentOfSelectedCourseListItem;
	SQLiteDatabase db = MainActivity.db.getReadableDatabase();
	int cursorIterator;
	boolean assignmentsExistForCourse;
	ArrayList<String> associatedAssignments = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<Map<String, Object>> assMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// List table = MainActivity.db.getTable("tbl_Assignment");
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		if (MainActivity.role == "Student") {
			Cursor c = db
					.query("tbl_Assignment",
							new String[] { "assignmentNo, assignmentTitle, assignmentProgress" },
							"assignmentDueDate > datetime('now','localtime')",
							null, null, null, null);
			while (c.moveToNext()) {

				map = new HashMap<String, Object>();
				map.put("id", String.valueOf(c.getInt(0)));
				map.put("title", c.getString(1));
				map.put("progress", c.getString(2));
				assMapList.add(map);
			}

			AssignmentAdapter adapter = new AssignmentAdapter(
					this.getActivity(), assMapList);
			this.setListAdapter(adapter);

		} else if (MainActivity.role == "Teacher") {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View list = inflater.inflate(R.layout.fragment_show_assignment,
				container, false);
		// registerForContextMenu(list);
		return list;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_float_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		ListView templist = this.getListView();
		View mView = templist.getChildAt(info.position);
		// String val =info.getItemAtPosition(info.position);
		Intent intent;
		if (MainActivity.role == "Student") {
			intent = new Intent(getActivity(), UpdateAssignmentActivity.class);

			TextView mytextview = (TextView) mView.findViewById(R.id.id_ass);
			assID = Integer.parseInt(mytextview.getText().toString());
			final String str;
			String selectedFromList = (templist
					.getItemAtPosition((int) info.position).toString());
			String lines[] = selectedFromList.split("title=");
			str = lines[1].substring(0, lines[1].length() - 1);
			switch (item.getItemId()) {
			case R.id.context_menu_update:

				// Toast.makeText(getActivity().getApplicationContext(),
				// str,Toast.LENGTH_LONG).show();
				intent.putExtra("assTitle", String.valueOf(str));
				startActivity(intent);
				return true;
			case R.id.context_menu_delete:
				// Confirm Deletion
				Toast.makeText(getActivity().getApplicationContext(), str,
						Toast.LENGTH_LONG).show();
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
														+ String.valueOf(str)
														+ "\'", null);
										Toast.makeText(
												getActivity(),
												"Assignment"
														+ String.valueOf(str)
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
}
