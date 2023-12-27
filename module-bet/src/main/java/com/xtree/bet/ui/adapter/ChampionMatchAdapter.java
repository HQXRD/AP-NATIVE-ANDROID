package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtFbLeagueGroupBinding;
import com.xtree.bet.databinding.BtFbMatchGroupBinding;
import com.xtree.bet.databinding.BtFbMatchListBinding;
import com.xtree.bet.databinding.BtFbPlayTypeGroupListBinding;
import com.xtree.bet.databinding.BtFbPlaytypeListBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.DiscolourTextView;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

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
        if (mDatas.isEmpty() || mDatas.get(groupPosition).getPlayTypeList().isEmpty()) {
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
        binding.tvLeagueName.setText(match.getChampionMatchName());
        Glide.with(mContext)
                .load(match.getLeague().getIcon())
                //.apply(new RequestOptions().placeholder(placeholderRes))
                .into(binding.ivIcon);
        binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
        if (isExpanded) {
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, 0);
        } else {
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, ConvertUtils.dp2px(5));

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

        List<Option> optionList = new ArrayList<>();
        optionList.addAll(playType.getChampionOptionList());
        binding.rvOptionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        binding.rvOptionList.setAdapter(new OptionChampionAdapter(mContext, (Match) getGroup(groupPosition), playType, playType.getChampionOptionList()));

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
