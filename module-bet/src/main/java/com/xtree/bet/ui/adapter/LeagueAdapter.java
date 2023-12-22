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

import com.bumptech.glide.Glide;
import com.stx.xhb.androidx.XBanner;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.AnimatedExpandableListView;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.DiscolourTextView;
import com.xtree.bet.weight.PageHorizontalScrollView;

import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LeagueAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<League> mDatas;
    private Context mContext;

    public LeagueAdapter(Context context, List<League> datas) {
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
        PageHorizontalScrollView hsvPlayTypeGroup;
        LinearLayout llPointer;

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
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if(mDatas.isEmpty() || mDatas.get(groupPosition).getMatchList().isEmpty()){
            return 0;
        }
        return mDatas.get(groupPosition).getMatchList().size();
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
        if (mDatas == null || mDatas.isEmpty()) {
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
        if (!league.isHead()) {
            holder.llHeader.setVisibility(View.GONE);
            holder.rlLeague.setVisibility(View.VISIBLE);
            holder.tvLeagueName.setText(league.getLeagueName());
            Glide.with(mContext)
                    .load(league.getIcon())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(holder.imLeague);
        } else {
            holder.llHeader.setVisibility(View.VISIBLE);
            holder.rlLeague.setVisibility(View.GONE);
            holder.tvHeaderName.setText("未开赛");
            holder.llHeader.setOnClickListener(view -> {
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND));
            });
        }
        if (isExpanded) {
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, 0);
        } else {
            convertView.setPadding(0, ConvertUtils.dp2px(5), 0, ConvertUtils.dp2px(5));

        }
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Log.e("test", "============getRealChildView============");

        ChildHolder holder;
        Match match = (Match) getChild(groupPosition, childPosition);
        if (match == null) {
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

        holder.tvPlayTypeCount.setText(match.getPlayTypeCount() + "+>");
        // 比赛未开始
        if(match.isUnGoingon()){
            holder.tvMatchTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
        }else {
            int sportType = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
            String sport = SportTypeContants.SPORT_IDS[sportType];
            if (sport.equals(SportTypeContants.SPORT_ID_FB) || sport.equals(SportTypeContants.SPORT_ID_BSB)) {
                holder.tvMatchTime.setText(match.getStage() + " " + match.getTime());
            } else {
                holder.tvMatchTime.setText(match.getStage());
            }
        }

        int sportPos = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        boolean isFb = SPUtils.getInstance().getBoolean(SPKey.BT_SPORT_IF_FB, true);
        LinearLayout llTypeGroup = (LinearLayout) holder.hsvPlayTypeGroup.getChildAt(0);

        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);
        PlayGroup playGroup = new PlayGroup(match.getPlayTypeList());
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList();

        for (int i = 0; i < firstPagePlayType.getChildCount(); i++) {
            setPlayTypeGroup(match, parent, (LinearLayout) firstPagePlayType.getChildAt(i), playGroupList.get(0).getPlayTypeList().get(i));
        }
        holder.llPointer.removeAllViews();
        LinearLayout sencondPagePlayType = (LinearLayout) llTypeGroup.getChildAt(1);
        if (SportTypeContants.SPORT_ID_FB.equals(SportTypeContants.getSportId(sportPos, isFb))) {
            sencondPagePlayType.setVisibility(View.VISIBLE);
            List<PlayType> playTypeList = playGroupList.get(1).getPlayTypeList();
            for (int i = 0; i < sencondPagePlayType.getChildCount(); i++) {
                setPlayTypeGroup(match, parent, (LinearLayout) sencondPagePlayType.getChildAt(i), playTypeList.get(i));
            }
            initPointer(holder);
        } else {
            sencondPagePlayType.setVisibility(View.GONE);
        }

        holder.llRoot.setOnClickListener(view -> {
            BtDetailActivity.start(mContext, match);
        });
        return convertView;
    }

    /**
     * 初始化分页pointer
     *
     * @param holder
     */
    private void initPointer(ChildHolder holder) {
        for (int i = 0; i < 2; i++) {
            ImageView ivPointer = new ImageView(mContext);
            ivPointer.setBackgroundResource(R.drawable.bt_bg_play_type_group_pointer_selected);
            int width = i == 0 ? ConvertUtils.dp2px(12) : ConvertUtils.dp2px(7);
            int height = ConvertUtils.dp2px(2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i == 0) {
                params.rightMargin = ConvertUtils.dp2px(2);
                ivPointer.setSelected(true);
            }
            ivPointer.setLayoutParams(params);
            holder.llPointer.addView(ivPointer);
        }
        holder.hsvPlayTypeGroup.setOnPageSelectedListener(currentPage -> {
            for (int i = 0; i < holder.llPointer.getChildCount(); i++) {
                if (currentPage == i) {
                    holder.llPointer.getChildAt(i).setSelected(true);
                } else {
                    holder.llPointer.getChildAt(i).setSelected(false);
                }
                int width = currentPage == i ? ConvertUtils.dp2px(12) : ConvertUtils.dp2px(7);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.llPointer.getChildAt(i).getLayoutParams();
                params.width = width;
                holder.llPointer.getChildAt(i).setLayoutParams(params);

            }
        });
    }

    private void setPlayTypeGroup(Match match, ViewGroup parent, LinearLayout rootPlayType, PlayType playType) {
        ViewGroup.LayoutParams params = rootPlayType.getLayoutParams();
        params.width = parent.getWidth() / 2 / 3;
        rootPlayType.setLayoutParams(params);

        for (int j = 0; j < rootPlayType.getChildCount(); j++) {
            View view = rootPlayType.getChildAt(j);
            if (view instanceof TextView) {
                ((TextView) view).setText(playType.getPlayTypeName());
            } else {

                LinearLayout optionView = (LinearLayout) view;
                List<Option> options = playType.getOptionList();
                if (j - 1 == playType.getOptionList().size()) {
                    optionView.setVisibility(View.GONE);
                } else {
                    optionView.setVisibility(View.VISIBLE);
                    Option option = options.get(j - 1);
                    BetConfirmOption betConfirmOption = (BetConfirmOption) optionView.getTag();

                    TextView uavailableTextView = (TextView) optionView.getChildAt(0);
                    TextView nameTextView = (TextView) optionView.getChildAt(1);
                    DiscolourTextView oddTextView = (DiscolourTextView) optionView.getChildAt(2);

                    if (option == null) {
                        uavailableTextView.setVisibility(View.VISIBLE);
                        oddTextView.setVisibility(View.GONE);
                        nameTextView.setVisibility(View.GONE);
                        uavailableTextView.setText("-");
                        uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        optionView.setOnClickListener(view1 -> {
                        });
                    } else {
                        OptionList optionList = playType.getOptionLists().get(0);
                        if (!optionList.isOpen()) {
                            uavailableTextView.setVisibility(View.VISIBLE);
                            oddTextView.setVisibility(View.GONE);
                            nameTextView.setVisibility(View.GONE);
                            uavailableTextView.setText("");
                            uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.bt_icon_option_locked), null, null, null);
                            optionView.setOnClickListener(view1 -> {
                            });
                        } else {
                            uavailableTextView.setVisibility(View.GONE);
                            oddTextView.setVisibility(View.VISIBLE);
                            nameTextView.setVisibility(View.VISIBLE);
                            nameTextView.setText(option.getSortName());
                            oddTextView.setText(String.valueOf(option.getOdd()));

                            if (betConfirmOption == null) {
                                betConfirmOption = new BetConfirmOptionFb(match, playType, optionList, option);
                                optionView.setTag(betConfirmOption);
                            } else {
                                betConfirmOption.setData(match, playType, optionList, option);
                            }
                            if (BtCarManager.isCg()) {
                                boolean has = BtCarManager.has(betConfirmOption);
                                if (has) {
                                    Log.e("test", option.getName() + "=======" + has);
                                }

                                optionView.setSelected(has);
                                oddTextView.setSelected(has);
                                nameTextView.setSelected(has);
                            } else {
                                if (optionView.isSelected()) {
                                    optionView.setSelected(false);
                                    oddTextView.setSelected(false);
                                    nameTextView.setSelected(false);
                                }
                            }
                            setOptionClickListener(optionView, optionList, option);
                        }
                    }
                }
            }
        }
    }

    private void setOptionClickListener(LinearLayout llOption, OptionList optionList, Option option) {
        llOption.setOnClickListener(view1 -> {
            if (!optionList.isOpen() || option == null) {
                return;
            }
            BetConfirmOption betConfirmOption = (BetConfirmOption) view1.getTag();
            if ((BtCarManager.isCg() && !BtCarManager.isEmpty())) { // 如果是串关，往购物车增加
                if (!optionList.isAllowCrossover() && !BtCarManager.has(betConfirmOption)) {
                    ToastUtils.showShort(mContext.getResources().getText(R.string.bt_bt_is_not_allow_crossover));
                    return;
                }
                if (!BtCarManager.has(betConfirmOption)) {
                    BtCarManager.addBtCar(betConfirmOption);
                } else {
                    BtCarManager.removeBtCar(betConfirmOption);
                }
                //option.setSelected(BtCarManager.has(betConfirmOption));
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_CG));
            } else {
                BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BtCarDialogFragment.KEY_BT_OPTION, (BetConfirmOption) view1.getTag());
                btCarDialogFragment.setArguments(bundle);
                if (mContext instanceof BaseActivity) {
                    btCarDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "btCarDialogFragment");
                }
            }
        });
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
