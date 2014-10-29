package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class AssignmentFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<Map<String, Object>> assMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// List table = MainActivity.db.getTable("tbl_Assignment");
		SQLiteDatabase db = MainActivity.db.getReadableDatabase();
		Cursor c = db
				.query("tbl_Assignment",
						new String[] { "assignmentNo, assignmentTitle, assignmentProgress" },
						null, null, null, null, null);
		while (c.moveToNext()) {
			map = new HashMap<String, Object>();
			map.put("title", c.getString(1));
			map.put("progress", c.getString(2));
			assMapList.add(map);
		}

		AssignmentAdapter adapter = new AssignmentAdapter(this.getActivity(),
				assMapList);
		this.setListAdapter(adapter);

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(getActivity(), "Your Choice : " + position,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_assignment, container,
				false);
	}
}
