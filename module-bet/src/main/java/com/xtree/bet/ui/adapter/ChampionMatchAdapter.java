package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.databinding.BtFbMatchGroupBinding;
import com.xtree.bet.databinding.BtFbPlaytypeListBinding;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;

import java.util.ArrayList;
import java.util.List;

public class ChampionMatchAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<Match> mDatas;
    private Context mContext;

    public void setData(List<Match> mLeagueList) {
        this.mDatas = mLeagueList;
        notifyDataSetChanged();
    }

    public ChampionMatchAdapter(Context context, List<Match> datas) {
        this.mDatas = datas;
        this.mContext = context;
    }

    private static class ChildHolder {
        View itemView;

        public ChildHolder(View view) {
            itemView = view;
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (mDatas.isEmpty() || mDatas.get(groupPosition).isHead() || mDatas.get(groupPosition).getPlayTypeList().isEmpty()) {
            return 0;
        }
        return mDatas.get(groupPosition).getPlayTypeList().size();
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
        return mDatas.get(groupPosition).getPlayTypeList().get(childPosition);
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
        Match match = mDatas.get(groupPosition);


        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_match_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        BtFbMatchGroupBinding binding = BtFbMatchGroupBinding.bind(holder.itemView);
        if (match.isHead()) {
            binding.llHeader.setVisibility(View.VISIBLE);
            binding.rlLeague.setVisibility(View.GONE);
            binding.ivExpand.setSelected(match.isExpand());
            binding.vSpace.setVisibility(View.VISIBLE);
        } else {
            binding.llHeader.setVisibility(View.GONE);
            binding.rlLeague.setVisibility(View.VISIBLE);
            binding.tvLeagueName.setText(match.getChampionMatchName());
            Glide.with(mContext)
                    .load(match.getLeague().getIcon())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivIcon);
            binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
            match.setExpand(isExpanded);
            binding.vSpace.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            binding.rlLeague.setBackgroundResource(isExpanded ? R.drawable.bt_bg_league_top : R.drawable.bt_bg_league_top_collapse);
        }
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //Log.e("test", "============getRealChildView============");

        ChildHolder holder;
        PlayType playType = (PlayType) getChild(groupPosition, childPosition);
        if (playType == null) {
            return convertView;
        }

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_playtype_list, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        BtFbPlaytypeListBinding binding = BtFbPlaytypeListBinding.bind(holder.itemView);

        binding.tvTitle.setText(playType.getPlayTypeName());
        binding.tvDeadline.setText(mContext.getResources().getString(R.string.bt_bt_champion_match_deadline, playType.getMatchDeadLine()));

        List<Option> optionList = new ArrayList<>();
        optionList.addAll(playType.getChampionOptionList());
        binding.rvOptionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        binding.rvOptionList.setAdapter(new OptionChampionAdapter(mContext, (Match) getGroup(groupPosition), playType, playType.getChampionOptionList()));
        binding.vSpace.setVisibility(childPosition == getRealChildrenCount(groupPosition) - 1 ? View.VISIBLE : View.GONE);
        binding.clOptionRoot.setBackgroundResource(childPosition == getRealChildrenCount(groupPosition) - 1 ? R.drawable.bt_bg_match_item_bottom : R.drawable.bt_bg_match_item);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupHolder {
        public GroupHolder(View view) {
            itemView = view;
        }

        View itemView;
    }

}
