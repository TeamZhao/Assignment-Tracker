package com.example.assignmenttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
