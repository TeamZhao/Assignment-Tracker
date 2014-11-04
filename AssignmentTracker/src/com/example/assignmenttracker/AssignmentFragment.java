package com.example.assignmenttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
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
import android.widget.ListView;
import android.widget.Toast;

public class AssignmentFragment extends ListFragment {
	int assID;

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
						"assignmentDueDate > datetime('now','localtime')",
						null, null, null, null);
		while (c.moveToNext()) {

			map = new HashMap<String, Object>();
			map.put("id", String.valueOf(c.getInt(0)));
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
		// AdapterView.AdapterContextMenuInfo info =
		// (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		// ListView templist = this.getListView();
		// View mView = templist.getChildAt(info.position);
		// TextView mytextview = (TextView) mView.findViewById(R.id.id_ass);
		// assID = Integer.parseInt(mytextview.getText().toString());

		switch (item.getItemId()) {
		case R.id.context_menu_update:
			Intent intent = new Intent(getActivity(),
					UpdateAssignmentActivity.class);
			// intent.putExtra("assID", assID);
			startActivity(intent);
			return true;
		case R.id.context_menu_delete:
			MainActivity.db.deleteRecord("tbl_Assignment", "assignmentNo",
					String.valueOf(assID));
			getActivity().recreate();
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
}