package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueArea;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtDialogLeagueBinding;
import com.xtree.bet.databinding.BtDialogLeagueGroupBinding;
import com.xtree.bet.databinding.BtFbLeagueChildBinding;
import com.xtree.bet.databinding.BtFbMatchGroupBinding;
import com.xtree.bet.databinding.BtFbPlaytypeListBinding;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;

public class SettingLeagueAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<LeagueArea> mDatas;
    private Context mContext;

    public void setData(List<LeagueArea> leagueAreaList) {
        this.mDatas = leagueAreaList;
        notifyDataSetChanged();
    }

    public SettingLeagueAdapter(Context context, List<LeagueArea> datas) {
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
    public int getChildrenCount(int groupPosition) {
        return getRealChildrenCount(groupPosition);
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (mDatas.isEmpty() || mDatas.get(groupPosition).getLeagueList().isEmpty()) {
            return 0;
        }
        return mDatas.get(groupPosition).getLeagueList().size();
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
        return mDatas.get(groupPosition).getLeagueList().get(childPosition);
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
        LeagueArea leagueArea = mDatas.get(groupPosition);


        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_dialog_league_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        BtDialogLeagueGroupBinding binding = BtDialogLeagueGroupBinding.bind(holder.itemView);
        binding.tvLeagueName.setText(leagueArea.getName());
        binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_white_expand : R.mipmap.bt_icon_white_unexpand);

        binding.cbChoise.setOnClickListener(v -> {
            leagueArea.setSelected(!leagueArea.isSelected());
            int childrenCount = getRealChildrenCount(groupPosition);
            for (int i = 0; i < childrenCount; i++) {
                League league = (League)getChild(groupPosition, i);
                league.setSelected(leagueArea.isSelected());
            }
            notifyDataSetChanged();
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_CHECK_ALL_CHECK));
        });
        binding.cbChoise.setChecked(leagueArea.isSelected());
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder holder;
        League league = (League) getChild(groupPosition, childPosition);
        if (league == null) {
            return convertView;
        }

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_league_child, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        BtFbLeagueChildBinding binding = BtFbLeagueChildBinding.bind(holder.itemView);
        Glide.with(mContext)
                .load(league.getIcon())
                //.apply(new RequestOptions().placeholder(placeholderRes))
                .into(binding.ivIcon);
        binding.tvLeagueName.setText(league.getLeagueName());
        binding.tvSaleCount.setText("(" + league.getSaleCount() + ")");
        binding.cbChoise.setChecked(league.isSelected());


        binding.cbChoise.setOnClickListener(v -> {
            league.setSelected(!league.isSelected());
            boolean isAllChecked = true;
            for (int i = 0; i < getRealChildrenCount(groupPosition); i ++){
                League child = (League) getChild(groupPosition, i);
                if(!child.isSelected()){
                    isAllChecked = false;
                    break;
                }
            }
            LeagueArea leagueArea = (LeagueArea) getGroup(groupPosition);
            if(isAllChecked != leagueArea.isSelected()) {
                leagueArea.setSelected(isAllChecked);
            }

            notifyDataSetChanged();
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_CHECK_ALL_CHECK));
        });


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
