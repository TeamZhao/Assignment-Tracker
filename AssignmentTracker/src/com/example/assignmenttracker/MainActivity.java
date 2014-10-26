package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static DatabaseManager db;
	private static final String tables[] = { "tbl_Semester", "tbl_Assignment" };
	private static final String tableCreatorString[] = {
			"CREATE TABLE IF NOT EXIST tbl_Semester (semesterNo INTEGER PRIMARY KEY AUTOINCREMENT , semesterDetails TEXT);",
			"CREATE TABLE IF NOT EXIST tbl_Assignment (assignmentNo INTEGER PRIMARY KEY AUTOINCREMENT , assignmentTitle TEXT , assignmentCourse TEXT , assignmentDueDate TEXT , assignmentProgress TEXT);" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseManager(this);
		db.dbInitialize(tables, tableCreatorString);

		List table = db.getTable("tbl_Assignment");
		if (table != null) {
			String output = "";
			for (Object o : table) {
				ArrayList row = (ArrayList) o;
				// Writing table to screen
				for (int i = 0; i < row.size(); i++) {
					output += row.get(i).toString() + " ";
					output += "\n";
				}
			}
			TextView txt = (TextView) findViewById(R.id.instruction);
			txt.setText(output);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_menu, menu);
		return true;
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

			return true;
		case R.id.add_semester:
			adds = new Intent(this, AddSemesterActivity.class);
			startActivity(adds);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
