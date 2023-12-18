package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtree.bet.R;
import com.xtree.bet.weight.AnimatedExpandableListView;

import java.util.List;

public class MyExpandableListViewAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private List<String> groupData;
    private List<List<String>> childrenData;

    private Context context;

    public MyExpandableListViewAdapter(List<String> groupData, List<List<String>> childrenData){
        this.groupData = groupData;
        this.childrenData = childrenData;
    }

    @Override
    public Object getGroup(int i) {
        return groupData.get(i);
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public Object getChild(int groupPostion, int childPosition) {
        return childrenData.get(groupPostion).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.bt_fb_test_item, parent, false);
        TextView textView = view.findViewById(R.id.tv_league_name);
        textView.setText(getGroup(i).toString());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return childrenData.size();
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.bt_fb_test_item, null, false);
        TextView textView = convertView.findViewById(R.id.tv_league_name);
        textView.setText(getChild(groupPosition, childPosition).toString());
        return convertView;
    }

}
