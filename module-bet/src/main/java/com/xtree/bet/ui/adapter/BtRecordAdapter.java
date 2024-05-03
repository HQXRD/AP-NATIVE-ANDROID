package com.xtree.bet.ui.adapter;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM_NAME;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtRecordBeanPm;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.databinding.BtLayoutBtRecordItemBinding;
import com.xtree.bet.databinding.BtLayoutBtRecordTimeBinding;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;

import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BtRecordAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<BtRecordTime> mDatas;
    private Context mContext;
    private AdvanceSettlementCallBack mAdvanceSettlementCallBack;
    private BasePopupView baseGiftFlowView;

    public void setAdvanceSettlementCallBack(AdvanceSettlementCallBack advanceSettlementCallBack) {
        this.mAdvanceSettlementCallBack = advanceSettlementCallBack;
    }

    public BtRecordAdapter(Context context, List<BtRecordTime> datas) {
        this.mDatas = datas;
        this.mContext = context;
    }

    public void setData(List<BtRecordTime> mLeagueList) {
        this.mDatas = mLeagueList;
        notifyDataSetChanged();
    }

    private static class ChildHolder {

        ConstraintLayout itemView;

        public ChildHolder(View view) {
            itemView = view.findViewById(R.id.ll_expand);
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getBtResultList().size();
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
        return mDatas.get(groupPosition).getBtResultList().get(childPosition);
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

        BtRecordTime btRecordTime = (BtRecordTime) getGroup(groupPosition);

        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_layout_bt_record_header, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        BtLayoutBtRecordTimeBinding binding = BtLayoutBtRecordTimeBinding.bind(holder.itemView);
        binding.tvName.setText(TimeUtils.longFormatString(btRecordTime.getTime(), TimeUtils.FORMAT_YY_MM_DD));
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ChildHolder holder;

        BtResult btResult = ((BtResult) getChild(groupPosition, childPosition));

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_layout_bt_record_item, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        BtLayoutBtRecordItemBinding binding = BtLayoutBtRecordItemBinding.bind(holder.itemView);

        String cg = btResult.getBetResultOption().size() > 1 ? "串关" : "单关";
        if (btResult.getBetResultOption().size() > 1) {
            binding.tvName.setText(mContext.getResources().getString(R.string.bt_bt_result_record_cg, cg, btResult.getCgName(), SPUtils.getInstance().getString(KEY_PLATFORM_NAME)));
        } else {
            binding.tvName.setText(cg + "-" + SPUtils.getInstance().getString(KEY_PLATFORM_NAME));
        }
        binding.rvMatch.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvMatch.setAdapter(new BtResultOptionAdapter(mContext, btResult.getBetResultOption()));
        binding.tvResultId.setText(mContext.getResources().getString(R.string.bt_bt_result_id_1, btResult.getId()));
        binding.tvBtTime.setText(mContext.getResources().getString(R.string.bt_bt_result_bt_time, TimeUtils.longFormatString(btResult.getBtDate(), TimeUtils.FORMAT_YY_MM_DD_HH_MM)));
        binding.tvBtResult.setText(btResult.getStatusDesc());
        binding.tvResultId.setOnClickListener(v -> {
            StringUtils.copy(btResult.getId());
        });
        binding.tvResultStatement.setOnClickListener(v -> {
            mAdvanceSettlementCallBack.onAdvanceSettlementClick(groupPosition, childPosition, btResult, binding.tvResultSettlementOdds.isChecked(), btResult.getBetResultOption().size() > 1);
        });

        binding.tvResultStatement.setText(mContext.getResources().getString(R.string.bt_txt_btn_statement, NumberUtils.format(btResult.getAdvanceSettleAmount(), 2)));
        binding.tvResultStatement.setVisibility(btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
        binding.tvResultStatementOdds.setVisibility(btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
        binding.tvResultSettlementOdds.setVisibility(!(btResult instanceof BtRecordBeanPm) && btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
        binding.tvResultStatementOdds.setOnClickListener(v -> {
            final String title = mContext.getString(R.string.txt_kind_tips);
            String showMessage = "体育提前兑现只适用于指定赛事和盘口，如遇到赛事或盘口取消，提前兑现注单将会被收回重新结算。平台保留赛果最终解释权。";
            baseGiftFlowView = new XPopup.Builder(mContext).asCustom(new MsgDialog(mContext, title, mContext.getString(R.string.bt_txt_statement_odds), showMessage, "", "我知道了", true, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    baseGiftFlowView.dismiss();
                }

                @Override
                public void onClickRight() {
                    baseGiftFlowView.dismiss();
                }
            }));
            baseGiftFlowView.show();
        });

        if (btResult.isAdvanceSettlement()) {
            binding.clSettlement.setVisibility(View.GONE);
            binding.clAdSettlement.setVisibility(View.VISIBLE);
            binding.tvAdSettleStatus.setText(btResult.getAdvanceSettlementStatus());
            binding.tvAdSettleDate.setText(btResult.getAdvanceSettlementDate());
            binding.tvAdSettleCost.setText(mContext.getString(R.string.bt_bt_result_bt_ad_settle_cost, NumberUtils.format(btResult.getAdvanceSettlementCost(), 2)));
            binding.tvAdSettleBack.setText(mContext.getString(R.string.bt_bt_result_bt_ad_settle_back, NumberUtils.format(btResult.getAdvanceSettlementBack(), 2)));

            SpannableString spannableString = new SpannableString(mContext.getString(R.string.bt_bt_result_bt_ad_settle_result, NumberUtils.format(btResult.getAdvanceSettlementResult(), 2)));
            int startIndex = 4; // "这"的索引值
            int endIndex = spannableString.length() - 1;   // "是"的索引值 + 1
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.bt_color_car_dialog_hight_line2));
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvAdSettleResult.setText(spannableString);
        } else {
            binding.clSettlement.setVisibility(View.VISIBLE);
            binding.clAdSettlement.setVisibility(View.GONE);
            binding.tvAmount.setText(mContext.getResources().getString(R.string.bt_bt_result_bt_amount_1, String.valueOf(btResult.getBtAmount())));
            if(btResult.getStatus() == 5){ // 已结算
                binding.tvWin.setText(mContext.getResources().getString(R.string.bt_bt_result_win_2, String.valueOf(btResult.userWin())));
            }else {
                binding.tvWin.setText(mContext.getResources().getString(R.string.bt_bt_result_win_1, String.valueOf(btResult.getBtWin())));
            }
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupHolder {
        public GroupHolder(View view) {
            itemView = view.findViewById(R.id.ll_expand);
        }

        View itemView;

    }

    public interface AdvanceSettlementCallBack {
        void onAdvanceSettlementClick(int groupPosition, int childPosition, BtResult btResult, boolean acceptoddschange, boolean parlay);
    }

}
