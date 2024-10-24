package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.databinding.BtFbDetailItemPlayTypeChildBinding;
import com.xtree.bet.databinding.BtFbDetailItemPlayTypeGroupBinding;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.PageHorizontalScrollView;

import java.util.List;

public class MatchDetailAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<PlayType> mDatas;
    private Match match;
    private Context mContext;
    private final boolean isResult;

    public MatchDetailAdapter(Context context, Match match, List<PlayType> datas, boolean isResult) {
        this.mDatas = datas;
        this.mContext = context;
        this.match = match;
        this.isResult = isResult;
    }

    public void setData(List<PlayType> mLeagueList) {
        this.mDatas = mLeagueList;
        notifyDataSetChanged();
    }

    private static class ChildHolder {
        TextView tvTeamNameMain;
        TextView tvTeamNameVisitor;
        TextView tvScoreMain;
        TextView tvScoreVisitor;
        TextView tvMatchTime;
        TextView tvPlayTypeCount;
        ImageView ivCourt;
        ImageView ivLive;
        XBanner xbPlayTypeGroup;
        LinearLayout llRoot;
        PageHorizontalScrollView hsvPlayTypeGroup;
        LinearLayout llPointer;

        NestedScrollView itemView;

        public ChildHolder(View view) {
            tvTeamNameMain = view.findViewById(R.id.tv_team_name_main);
            tvTeamNameVisitor = view.findViewById(R.id.tv_team_name_visitor);
            tvScoreMain = view.findViewById(R.id.tv_score_main);
            tvScoreVisitor = view.findViewById(R.id.tv_score_visitor);
            tvMatchTime = view.findViewById(R.id.tv_match_time);
            tvPlayTypeCount = view.findViewById(R.id.tv_playtype_count);
            ivCourt = view.findViewById(R.id.iv_court);
            ivLive = view.findViewById(R.id.iv_live);
            xbPlayTypeGroup = view.findViewById(R.id.play_type_banner);
            llRoot = view.findViewById(R.id.ll_root);
            hsvPlayTypeGroup = view.findViewById(R.id.hsv_play_type_group);
            llPointer = view.findViewById(R.id.ll_pointer);
            itemView = view.findViewById(R.id.cl_root);
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (mDatas.get(groupPosition) != null) {
            return mDatas.get(groupPosition).getOptionLists().size();
        }
        return 0;
    }

    @Override
    public int getGroupCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mDatas == null || mDatas.isEmpty()) {
            return null;
        }
        return mDatas.get(groupPosition).getOptionLists().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (mDatas == null || mDatas.isEmpty()) {
            return convertView;
        }

        PlayType playType = (PlayType) getGroup(groupPosition);

        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_detail_item_play_type_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        BtFbDetailItemPlayTypeGroupBinding binding = BtFbDetailItemPlayTypeGroupBinding.bind(holder.itemView);

        binding.tvPlaytypeName.setText(playType.getPlayTypeName());
        binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
        binding.rlPlayMethod.setBackgroundResource(isExpanded ? R.drawable.bt_bg_expand_group : R.drawable.bt_bg_expand_group_collapse);

        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder holder;

        OptionList optionList = ((OptionList) getChild(groupPosition, childPosition));

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_detail_item_play_type_child, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        BtFbDetailItemPlayTypeChildBinding binding = BtFbDetailItemPlayTypeChildBinding.bind(holder.itemView);
        binding.rvOptionList.setHasFixedSize(true);
        int spanCount = optionList.getOptionList().size() >= 3 ? 3 : optionList.getOptionList().size();
        binding.rvOptionList.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        if (isResult) {
            OptionResultAdapter optionAdapter = new OptionResultAdapter(mContext, optionList.getOptionList());
            binding.rvOptionList.setAdapter(optionAdapter);
        } else {
            OptionAdapter optionAdapter = new OptionAdapter(mContext, match, (PlayType) getGroup(groupPosition), optionList, optionList.getOptionList());
            binding.rvOptionList.setAdapter(optionAdapter);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupHolder {
        public GroupHolder(View view) {
            itemView = view.findViewById(R.id.cl_root);
        }

        View itemView;

    }

}
