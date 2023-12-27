package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.weight.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;

public class LeagueAdapter1 extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private List<League> mDatas;
    private Context mContext;

    public LeagueAdapter1(Context context, List<League> datas) {
        this.mDatas = datas;
        this.mContext = context;
    }

    public void setData(List<League> mLeagueList) {
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
        HorizontalScrollView hsvPlayTypeGroup;

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
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getMatchList().size();
    }

    @Override
    public int getGroupCount() {
        if(mDatas == null){
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
        if(mDatas == null || mDatas.isEmpty()){
            return null;
        }
        return mDatas.get(groupPosition).getMatchList().get(childPosition);
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
        if(mDatas == null || mDatas.isEmpty()){
            return convertView;
        }
        League league = mDatas.get(groupPosition);

        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_league_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if(!league.isHead()) {
            holder.llHeader.setVisibility(View.GONE);
            holder.rlLeague.setVisibility(View.VISIBLE);
            holder.tvLeagueName.setText(league.getLeagueName());
            Glide.with(mContext)
                    .load(league.getIcon())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(holder.imLeague);
        }else {
            holder.llHeader.setVisibility(View.VISIBLE);
            holder.rlLeague.setVisibility(View.GONE);
            holder.tvHeaderName.setText("未开赛");
            holder.llHeader.setOnClickListener(view -> {
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND));
            });
        }
        /*if(isExpanded) {
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, 0);
        }else{
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, ConvertUtils.dp2px(5));

        }*/
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        Match match = (Match) getChild(groupPosition, childPosition);
        if(match == null){
            return convertView;
        }

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_match_list, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.tvTeamNameMain.setText(match.getTeamMain());
        holder.tvTeamNameVisitor.setText(match.getTeamVistor());
        if (match.getScore(Constants.SCORE_TYPE_SCORE) != null && match.getScore(Constants.SCORE_TYPE_SCORE).size() > 1) {
            holder.tvScoreMain.setText(String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(0)));
            holder.tvScoreVisitor.setText(String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(1)));
        }
        holder.tvMatchTime.setText(match.getStage() + " " + match.getTime());
        holder.tvPlayTypeCount.setText(match.getPlayTypeCount() + "+>");

        /*holder.ivCourt.setVisibility(match.hasAs() ? View.VISIBLE : View.GONE);
        holder.ivLive.setVisibility(match.hasVideo() ? View.VISIBLE : View.GONE);*/

        /*holder.xbPlayTypeGroup.setBannerData(R.layout.bt_fb_list_item_play_type, playGroupList);
        holder.xbPlayTypeGroup.loadImage(new PlayGroupAdapter());
        holder.xbPlayTypeGroup.getViewPager().setOverScrollMode(View.OVER_SCROLL_NEVER);*/


        int sportPos = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        boolean isFb = SPUtils.getInstance().getBoolean(SPKey.BT_SPORT_IF_FB, true);
        LinearLayout llTypeGroup = (LinearLayout)holder.hsvPlayTypeGroup.getChildAt(0);

        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);
        PlayGroup playGroup = new PlayGroup(match.getPlayTypeList());
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList();
        ViewGroup.LayoutParams params = llTypeGroup.getLayoutParams();

        llTypeGroup.setLayoutParams(params);

        List<PlayType> playTypeList = new ArrayList<>();
        playTypeList.addAll(playGroupList.get(0).getPlayTypeList());
        playTypeList.addAll(playGroupList.get(1).getPlayTypeList());
        for(int i = 0; i < ((LinearLayout)llTypeGroup.getChildAt(0)).getChildCount(); i ++){
            setPlayTypeGroup(parent, (LinearLayout) ((LinearLayout)llTypeGroup.getChildAt(0)).getChildAt(i), playTypeList, i);
        }

        /*LinearLayout sencondPagePlayType = (LinearLayout)llTypeGroup.getChildAt(1);
        if(SportTypeContants.SPORT_ID_FB.equals(SportTypeContants.getSportId(sportPos, isFb))){
            sencondPagePlayType.setVisibility(View.VISIBLE);
            List<PlayType> playTypeList = playGroupList.get(1).getPlayTypeList();
            for(int i = 0; i < sencondPagePlayType.getChildCount(); i ++){
                setPlayTypeGroup(parent, sencondPagePlayType, playTypeList, i);
            }
        }else{
            sencondPagePlayType.setVisibility(View.GONE);
        }*/



        holder.llRoot.setOnClickListener(view -> {
            BtDetailActivity.start(mContext, match);
        });

        convertView.setPadding(0, 0, 0, 0);
        return convertView;
    }

    private void setPlayTypeGroup(ViewGroup parent, LinearLayout pagePlayType, List<PlayType> playTypeList, int i) {
        View view;
        //LinearLayout child = (LinearLayout) pagePlayType.getChildAt(i);
        ViewGroup.LayoutParams params = pagePlayType.getLayoutParams();
        params.width = parent.getWidth() / 2 / 3;
        pagePlayType.setLayoutParams(params);
        for(int j = 0; j < pagePlayType.getChildCount(); j ++){
            view = pagePlayType.getChildAt(j);
            if(view instanceof TextView){
                ((TextView) view).setText(playTypeList.get(i).getPlayTypeName());
            }else {
                LinearLayout linearLayout = (LinearLayout) view;
                    if(j - 1 == playTypeList.get(i).getOptionList().size()){
                        return;
                    }
                    Option option = playTypeList.get(i).getOptionList().get(j - 1);
                    if (option == null) {
                        linearLayout.getChildAt(0).setVisibility(View.VISIBLE);
                        linearLayout.getChildAt(1).setVisibility(View.GONE);
                        linearLayout.getChildAt(2).setVisibility(View.GONE);
                    } else {
                        linearLayout.getChildAt(0).setVisibility(View.GONE);
                        linearLayout.getChildAt(1).setVisibility(View.VISIBLE);
                        linearLayout.getChildAt(2).setVisibility(View.VISIBLE);
                        ((TextView) linearLayout.getChildAt(1)).setText(option.getSortName());
                        ((TextView) linearLayout.getChildAt(2)).setText(String.valueOf(option.getOdd()));
                    }
                }

        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupHolder {
        public GroupHolder(View view) {
            tvHeaderName = view.findViewById(R.id.tv_header_name);
            llHeader = view.findViewById(R.id.ll_header);
            rlLeague = view.findViewById(R.id.rl_league);
            tvLeagueName = view.findViewById(R.id.tv_league_name);
            imLeague = view.findViewById(R.id.iv_icon);
        }

        TextView tvLeagueName;
        ImageView imLeague;
        TextView tvHeaderName;
        LinearLayout llHeader;
        RelativeLayout rlLeague;
    }

}
