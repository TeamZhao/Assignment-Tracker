package com.example.assignmenttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class GetEmailInfoActivity extends Activity {
	String assTitle;
	String emailDestination;
	String emailSubject;
	String emailMessage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_email_info);
		Intent intent = getIntent();
		assTitle = intent.getStringExtra("assTitle");
		
		final EditText txtEmailTo = (EditText) findViewById(R.id.txt_EmailAdd);
		final EditText txtEmailSubject = (EditText) findViewById(R.id.txt_Subject);
		final EditText txtEmailMessage = (EditText) findViewById(R.id.txt_OptionalMessage);
		emailDestination = txtEmailTo.getText().toString();
		emailSubject = txtEmailSubject.getText().toString();
		emailMessage = txtEmailMessage.getText().toString();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_email_info, menu);
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
