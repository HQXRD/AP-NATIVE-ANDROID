package com.xtree.bet.ui.adapter;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM_NAME;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.databinding.BtLayoutBtRecordItemBinding;
import com.xtree.bet.databinding.BtLayoutBtRecordTimeBinding;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.fragment.BtAdvanceSettlementFragment;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;

import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class BtRecordAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<BtRecordTime> mDatas;
    private Context mContext;
    private AdvanceSettlementCallBack mAdvanceSettlementCallBack;

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
        binding.tvName.setText(TimeUtils.longFormatString(btRecordTime.getTime(), TimeUtils.FORMAT_MM_DD_1));
        /*if(groupPosition == 0){
            convertView.setVisibility(View.GONE);
            binding.vSpace.getLayoutParams().height = 0;
        }else {
            binding.vSpace.getLayoutParams().height = ConvertUtils.dp2px(10);
            convertView.setVisibility(View.VISIBLE);
            binding.tvName.setText(TimeUtils.longFormatString(btRecordTime.getTime(), TimeUtils.FORMAT_MM_DD_1));
        }*/
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

        /*if(groupPosition == 0 && childPosition == 0){
            binding.vSpace.getLayoutParams().height = 0;
        }else {
            binding.vSpace.getLayoutParams().height = ConvertUtils.dp2px(10);
        }*/

        String cg = btResult.getBetResultOption().size() > 1 ? "串关" : "单关";
        if (btResult.getBetResultOption().size() > 1) {
            binding.tvName.setText(mContext.getResources().getString(R.string.bt_bt_result_record_cg, cg, btResult.getCgName(), SPUtils.getInstance().getString(KEY_PLATFORM_NAME)));
        } else {
            binding.tvName.setText(cg);
        }
        binding.rvMatch.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvMatch.setAdapter(new BtResultOptionAdapter(mContext, btResult.getBetResultOption()));
        binding.tvAmount.setText(mContext.getResources().getString(R.string.bt_bt_result_bt_amount_1, String.valueOf(btResult.getBtAmount())));
        binding.tvWin.setText(mContext.getResources().getString(R.string.bt_bt_result_win_1, String.valueOf(btResult.getBtWin())));
        binding.tvResultId.setText(mContext.getResources().getString(R.string.bt_bt_result_id_1, btResult.getId()));
        binding.tvBtTime.setText(mContext.getResources().getString(R.string.bt_bt_result_bt_time, TimeUtils.longFormatString(btResult.getBtDate(), TimeUtils.FORMAT_YY_MM_DD_HH_MM)));
        binding.tvBtResult.setText(btResult.getStatusDesc());
        binding.tvResultId.setOnClickListener(v -> {
            StringUtils.copy(btResult.getId());
        });
        binding.tvResultStatement.setOnClickListener(v -> {
            mAdvanceSettlementCallBack.onAdvanceSettlementClick(groupPosition, childPosition, btResult);
        });
        binding.tvResultStatement.setText(mContext.getResources().getString(R.string.bt_txt_btn_statement, String.valueOf(btResult.getAdvanceSettleAmount())));
        binding.tvResultStatement.setVisibility(btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
        binding.tvResultStatementOdds.setVisibility(btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
        binding.tvResultSettlementOdds.setVisibility(btResult.canAdvanceSettle() ? View.VISIBLE : View.GONE);
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

    public interface AdvanceSettlementCallBack{
        void onAdvanceSettlementClick(int groupPosition, int childPosition, BtResult btResult);
    }

}
