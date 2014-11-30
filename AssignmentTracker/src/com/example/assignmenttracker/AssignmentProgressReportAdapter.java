package com.example.assignmenttracker;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AssignmentProgressReportAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater layoutInflater;

	private List<Map<String, Object>> list;

	public AssignmentProgressReportAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;

		layoutInflater = LayoutInflater.from(context);

		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list != null ? this.list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_show_progress_reports, null);
		}

		TextView assignmentTitle = (TextView) convertView.findViewById(R.id.assignment_progress_report_assignment_name);
		TextView assignmentDueDate = (TextView) convertView.findViewById(R.id.assignment_progress_report_assignment_due_on);
		TextView assignmentCourseName = (TextView) convertView.findViewById(R.id.assignment_progress_report_assignment_course_name);
		ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.assignment_progress_report_assignment_progress_bar);
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_ass);
		
		assignmentTitle.setText(list.get(position).get("title").toString());
		pb.setProgress(Integer.parseInt(list.get(position).get("progress").toString()));
		assignmentDueDate.setText(list.get(position).get("duedate").toString());
		assignmentCourseName.setText(list.get(position).get("course").toString());
		
		return convertView;
	}
}
