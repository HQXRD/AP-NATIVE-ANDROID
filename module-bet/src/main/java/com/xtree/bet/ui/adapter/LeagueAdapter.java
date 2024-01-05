package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtFbLeagueGroupBinding;
import com.xtree.bet.databinding.BtFbMatchListBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.activity.MainActivity;
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

public class LeagueAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<League> mDatas;
    private Context mContext;

    public void setData(List<League> mLeagueList) {
        this.mDatas = mLeagueList;
        notifyDataSetChanged();
    }

    public LeagueAdapter(Context context, List<League> datas) {
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
        if (mDatas.isEmpty() || mDatas.get(groupPosition).getMatchList().isEmpty()) {
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
        BtFbLeagueGroupBinding binding = BtFbLeagueGroupBinding.bind(holder.itemView);
        if (!league.isHead()) {
            binding.llHeader.setVisibility(View.GONE);
            binding.rlLeague.setVisibility(View.VISIBLE);
            binding.tvLeagueName.setText(league.getLeagueName());
            Glide.with(mContext)
                    .load(league.getIcon())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivIcon);
        } else {
            binding.llHeader.setVisibility(View.VISIBLE);
            binding.rlLeague.setVisibility(View.GONE);
            binding.tvHeaderName.setText("未开赛");
            binding.llHeader.setOnClickListener(view -> {
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND));
            });
        }
        binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
        league.setExpand(isExpanded);
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //Log.e("test", "============getRealChildView============");

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

        BtFbMatchListBinding binding = BtFbMatchListBinding.bind(holder.itemView);

        binding.tvTeamNameMain.setText(match.getTeamMain());
        binding.tvTeamNameVisitor.setText(match.getTeamVistor());
        if (match.getScore(Constants.SCORE_TYPE_SCORE) != null && match.getScore(Constants.SCORE_TYPE_SCORE).size() > 1) {
            binding.tvScoreMain.setText(String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(0)));
            binding.tvScoreVisitor.setText(String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(1)));
        }

        binding.tvPlaytypeCount.setText(match.getPlayTypeCount() + "+>");
        // 比赛未开始
        if (match.isUnGoingon()) {
            binding.tvMatchTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
        } else {
            int sportType = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
            String sport = SportTypeContants.SPORT_IDS[sportType];
            if (sport.equals(SportTypeContants.SPORT_ID_FB) || sport.equals(SportTypeContants.SPORT_ID_BSB)) {
                binding.tvMatchTime.setText(match.getStage() + " " + match.getTime());
            } else {
                binding.tvMatchTime.setText(match.getStage());
            }
        }

        int sportPos = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        LinearLayout llTypeGroup = (LinearLayout) binding.hsvPlayTypeGroup.getChildAt(0);

        String mPlatform = ((MainActivity) mContext).getmPlatform();
        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);

        PlayGroup playGroup;

        if (TextUtils.equals(mPlatform, MainActivity.PLATFORM_FB)) {
            playGroup = new PlayGroupFb(match.getPlayTypeList());
        } else {
            playGroup = new PlayGroupPm(match.getPlayTypeList());
        }
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList();

        for (int i = 0; i < firstPagePlayType.getChildCount(); i++) {
            setPlayTypeGroup(match, parent, (LinearLayout) firstPagePlayType.getChildAt(i), playGroupList.get(0).getOriginalPlayTypeList().get(i));
        }
        binding.llPointer.removeAllViews();
        LinearLayout sencondPagePlayType = (LinearLayout) llTypeGroup.getChildAt(1);
        if (SportTypeContants.SPORT_ID_FB.equals(SportTypeContants.getSportId(sportPos))) {
            sencondPagePlayType.setVisibility(View.VISIBLE);
            List<PlayType> playTypeList = playGroupList.get(1).getOriginalPlayTypeList();
            for (int i = 0; i < sencondPagePlayType.getChildCount(); i++) {
                setPlayTypeGroup(match, parent, (LinearLayout) sencondPagePlayType.getChildAt(i), playTypeList.get(i));
            }
            initPointer(binding);
        } else {
            sencondPagePlayType.setVisibility(View.GONE);
        }

        binding.llRoot.setOnClickListener(view -> {
            BtDetailActivity.start(mContext, match);
        });

        if (convertView.getLayoutParams() == null) {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = ConvertUtils.dp2px(100);
            convertView.setLayoutParams(params);
        }

        return convertView;
    }

    /**
     * 初始化分页pointer
     *
     * @param binding
     */
    private void initPointer(BtFbMatchListBinding binding) {
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
            binding.llPointer.addView(ivPointer);
        }
        /*binding.hsvPlayTypeGroup.setOnPageSelectedListener(currentPage -> {
            for (int i = 0; i < binding.llPointer.getChildCount(); i++) {
                if (currentPage == i) {
                    binding.llPointer.getChildAt(i).setSelected(true);
                } else {
                    binding.llPointer.getChildAt(i).setSelected(false);
                }
                int width = currentPage == i ? ConvertUtils.dp2px(12) : ConvertUtils.dp2px(7);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.llPointer.getChildAt(i).getLayoutParams();
                params.width = width;
                binding.llPointer.getChildAt(i).setLayoutParams(params);

            }
        });*/
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
                            oddTextView.setOptionOdd(option);
                            BetConfirmOptionFb betConfirmOption = new BetConfirmOptionFb(match, playType, optionList, option);
                            optionView.setTag(betConfirmOption);
                            if (BtCarManager.isCg()) {
                                boolean has = BtCarManager.has(betConfirmOption);
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
            itemView = view;
            /*tvHeaderName = view.findViewById(R.id.tv_header_name);
            llHeader = view.findViewById(R.id.ll_header);
            rlLeague = view.findViewById(R.id.rl_league);
            tvLeagueName = view.findViewById(R.id.tv_league_name);
            imLeague = view.findViewById(R.id.iv_icon);*/
        }

        View itemView;

        TextView tvLeagueName;
        ImageView imLeague;
        TextView tvHeaderName;
        LinearLayout llHeader;
        RelativeLayout rlLeague;
    }

}
