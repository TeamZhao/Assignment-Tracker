package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ShowSemestersActivity extends ActionBarActivity {

	ListView semListView;
	String contentOfSelectedSemListItem;
	public String semesterNo;
	public String semesterDetails;
	ArrayList<String> values = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_semesters);

		semListView = (ListView) findViewById(R.id.listView_show_semesters);
		registerForContextMenu(semListView);

		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c;
		if (MainActivity.role == "Student") {
			c = db.query("tbl_Semester",
					new String[] { "semesterNo, semesterDetails" }, null, null,
					null, null, null);
		} else { // role == "Teacher"
			c = db.query("tbl_TeacherSemester",
					new String[] { "semesterNo, semesterDetails" }, null, null,
					null, null, null);
		}

		final ArrayList<String> semesterNoArray = new ArrayList<String>();
		while (c.moveToNext()) {
			semesterNoArray.add(c.getString(0));
			values.add(c.getString(1));
		}

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		semListView.setAdapter(adapter);

		semListView.setOnItemClickListener(new OnItemClickListener() {

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
				contentOfSelectedSemListItem = item;
				semesterDetails = values.get(position);
				Intent intent = new Intent(ShowSemestersActivity.this,
						ShowCoursesActivity.class);
				intent.putExtra("semesterDetails", semesterDetails);
				startActivity(intent);
			}

		});

		semListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);// value
																				// of
																				// selected
																				// list
																				// item
																				// as
																				// string
				contentOfSelectedSemListItem = item;
				semesterNo = semesterNoArray.get(position);
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_semesters, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.context_float_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.context_menu_update:
			Intent intent = new Intent(this, UpdateSemesterActivity.class);

			intent.putExtra("semesterNo", semesterNo);
			intent.putExtra("semesterDetails", contentOfSelectedSemListItem); // can
																				// you
																				// use
																				// this
																				// extra
																				// in
			// the update activity to
			// search for db record
			// containing this value?
			startActivity(intent);
			return true;
		case R.id.context_menu_delete:
			// Code for popup
			final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(
					this);
			// new AlertDialog.Builder(this);
			helpBuilder.setTitle("Confirm Delete?");
			helpBuilder
					.setMessage("Are you sure you want to delete the record?");
			helpBuilder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do nothing but close the dialog
						}
					});
			helpBuilder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									ShowSemestersActivity.this,
									DeleteSemesterConfirmationActivity.class);

							intent.putExtra("semesterNo", semesterNo);
							intent.putExtra("semesterDetails",
									contentOfSelectedSemListItem); // can you
																	// use this
																	// extra in
							// the update activity to
							// search for db record
							// containing this value?
							startActivity(intent);

						}
					});
			// Code for popup
			AlertDialog helpDialog = helpBuilder.create();
			helpDialog.show();
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
