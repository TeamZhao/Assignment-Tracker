package com.example.assignmenttracker;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowCoursesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_semesters);
		
		ListView semListView = (ListView) findViewById(R.id.listView_show_semesters);
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db.query("tbl_Course", new String[] { "CourseCode, CourseName" },
						null,
						null, null, null, null);
		
		ArrayList<String> values = new ArrayList<String>();
		while (c.moveToNext()){
			values.add(c.getString(0)+" : "+c.getString(1));
		}

		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		semListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_courses, menu);
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
}
