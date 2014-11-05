package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_semesters);
		
		semListView = (ListView) findViewById(R.id.listView_show_semesters);
		registerForContextMenu(semListView);
		
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db.query("tbl_Semester", new String[] { "semesterNo, semesterDetails" },
						null,
						null, null, null, null);
		
		ArrayList<String> values = new ArrayList<String>();
		while (c.moveToNext()){
			values.add(c.getString(1));
		}

		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		semListView.setAdapter(adapter);
		
		semListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				  final String item = (String) parent.getItemAtPosition(position);//value of selected list item as string
				Toast.makeText(getApplicationContext(), "Your Choice : " + item, Toast.LENGTH_SHORT)
				.show();
				contentOfSelectedSemListItem = item;
				
			}
		  
		});
		
		semListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				 final String item = (String) parent.getItemAtPosition(position);//value of selected list item as string
					contentOfSelectedSemListItem = item;
				return false;
			}
		});
		
		semListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				final String item = (String) parent.getItemAtPosition(position);//value of selected list item as string
				Toast.makeText(getApplicationContext(), "Your Choice : " + item, Toast.LENGTH_SHORT)
				.show();				 
				
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
			intent.putExtra("semesterDetails", contentOfSelectedSemListItem); // can you use this extra in
													// the update activity to
													// search for db record
													// containing this value?
			startActivity(intent);
			return true;
		case R.id.context_menu_delete:
			MainActivity.db.deleteRecord("tbl_Semester", "assignmentNo",
					contentOfSelectedSemListItem);
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
