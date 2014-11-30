package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.assignmenttracker.R.id;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AssignmentProgressReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_progress_reports_main);
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		List<Map<String, Object>> assMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		ListView progressListView =(ListView)findViewById(R.id.listView_show_progress_reports);
		
		// Return all assignments
		if (MainActivity.role == "Student") {
			Cursor c = db
					.query("tbl_Assignment",
							new String[] { "assignmentNo, assignmentTitle, assignmentProgress, assignmentDueDate, assignmentCourse" }, null, null, null, null, null);
			while (c.moveToNext()) {
				map = new HashMap<String, Object>();
				map.put("id", String.valueOf(c.getInt(0)));
				map.put("title", c.getString(1));
				map.put("progress", c.getString(2));
				map.put("duedate", c.getString(3));
				map.put("course", c.getString(4));
				assMapList.add(map);
			}
			
			AssignmentProgressReportAdapter adapter = new AssignmentProgressReportAdapter(
			this, assMapList);
			progressListView.setAdapter(adapter);
			
		} else if (MainActivity.role == "Teacher") {

			Cursor c = db
					.query("tbl_TeacherAssignment",
							new String[] { "assignmentNo, assignmentTitle, assignmentProgress, assignmentDueDate, assignmentCourse" },
							null, null, null, null, null); 
			while (c.moveToNext()) {

				map = new HashMap<String, Object>();
				map.put("id", String.valueOf(c.getInt(0)));
				map.put("title", c.getString(1));
				map.put("progress", c.getString(2));
				map.put("duedate", c.getString(3));
				map.put("course", c.getString(4));
				assMapList.add(map);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

}
