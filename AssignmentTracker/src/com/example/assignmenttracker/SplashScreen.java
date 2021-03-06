package com.example.assignmenttracker;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		
		final Button btnTeacher = (Button) findViewById(R.id.btn_teacher);
		final Button btnStudent = (Button) findViewById(R.id.btn_student);
		
		btnTeacher.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MainActivity.role = "Teacher";
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		btnStudent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	
				MainActivity.role = "Student";
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
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
