package com.example.assignmenttracker;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AssignmentAdapter2 extends BaseExpandableListAdapter{

	private Context context;
	private HashMap<String, List<String>> assignmentCourses;
	private List<String> assignmentList;
	
	public AssignmentAdapter2 (Context context, HashMap<String, List<String>> assignmentCourses, List<String> assignmentList)
	{
		this.context = context;
		this.assignmentCourses = assignmentCourses;
		this.assignmentList = assignmentList;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return assignmentCourses.get(assignmentList.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		String childTitle = (String) getChild(groupPosition, childPosition);
		if (convertView == null)
		{
			LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.list_assignments_child_assignments, parent, false);
		}
		TextView childTextView = (TextView) convertView.findViewById(R.id.assignments_child_assignments);
		childTextView.setText(childTitle);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		return assignmentCourses.get(assignmentList.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return assignmentList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {

		return assignmentList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String groupTitle = (String) getGroup(groupPosition);
		if (convertView == null) 
		{
			LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.list_assignments_parent_courses, parent, false);
		}
		TextView parentTextView = (TextView) convertView.findViewById(R.id.assignments_parent_courses);
		parentTextView.setTypeface(null, Typeface.BOLD);
		parentTextView.setText(groupTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}
	
}
