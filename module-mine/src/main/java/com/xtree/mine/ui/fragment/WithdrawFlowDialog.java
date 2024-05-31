package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.ExpandableTextView;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogWithdrawalFlowBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.AwardsRecordVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.Utils;

/**
 * 提款流水
 */
public class WithdrawFlowDialog extends BottomPopupView {

    public interface IWithdrawFlowDialogCallBack {
        void closeWithdrawFlowDialog();
    }

    private IWithdrawFlowDialogCallBack callBack;

    private DialogWithdrawalFlowBinding binding;
    ChooseWithdrawViewModel viewModel;
    LifecycleOwner owner;
    private AwardsRecordVo awardsRecordVo;
    private FlowAdapter flowAdapter;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_withdrawal_flow;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    private WithdrawFlowDialog(@NonNull Context context) {
        super(context);
    }

    public static WithdrawFlowDialog newInstance(Context context, LifecycleOwner owner, final AwardsRecordVo awardsRecordVo, IWithdrawFlowDialogCallBack callBack) {
        WithdrawFlowDialog dialog = new WithdrawFlowDialog(context);
        dialog.owner = owner;
        dialog.awardsRecordVo = awardsRecordVo;
        dialog.callBack = callBack;

        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initData();
        initViewObservable();
        initView();
    }

    private void initView() {
        binding = DialogWithdrawalFlowBinding.bind(findViewById(R.id.ll_root_withdrawal_flow));
        binding.tvwTitle.setText(getContext().getString(R.string.txt_withdraw_flow));

        binding.ivwClose.setOnClickListener(v -> {
            callBack.closeWithdrawFlowDialog();
            dismiss();

        });
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                if (callBack != null) {
                    callBack.closeWithdrawFlowDialog();
                }
            }

            @Override
            public void onDrag(int y, float percent, boolean isScrollUp) {
            }

            @Override
            public void onOpen() {
            }
        });
        binding.llChooseTip.setVisibility(View.VISIBLE);
        String tipText = "";

        if (awardsRecordVo.list != null && !awardsRecordVo.list.isEmpty() && awardsRecordVo.list.size() > 0) {

            String listNumber = String.valueOf(awardsRecordVo.list.size());
            String showNumber = "<font color=#F35A4E>" + listNumber + "</font>";
            String format = getContext().getString(R.string.txt_awards_flow_list_title);
            String textSource = String.format(format, awardsRecordVo.withdraw_dispensing_money, showNumber, showNumber);
            binding.tvChooseTip.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));

            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.lvChoose.setVisibility(View.VISIBLE);
            flowAdapter = new FlowAdapter(awardsRecordVo.list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.lvChoose.setLayoutManager(layoutManager);
            binding.lvChoose.addItemDecoration(new FruitHorRecyclerViewAdapter.SpacesItemDecoration(10));
            binding.lvChoose.setAdapter(flowAdapter);
            binding.lvChoose.setItemAnimator(new DefaultItemAnimator());

        } else if (!"0.00".equals(awardsRecordVo.withdraw_dispensing_money)) {
            tipText = String.format(getContext().getString(R.string.txt_awards_flow_title), awardsRecordVo.withdraw_dispensing_money);
            binding.tvChooseTip.setText(tipText);
            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.tvWithdrawalFlowTip.setVisibility(View.GONE);
            binding.lvChoose.setVisibility(View.GONE);
        } else {
            tipText = getContext().getString(R.string.txt_awards_no_money_tip);
            binding.tvChooseTip.setText(tipText);
            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.tvWithdrawalFlowTip.setVisibility(View.GONE);
            binding.lvChoose.setVisibility(View.GONE);
        }

    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {

    }

    /**
     * 提款流水适配器
     */
    private class FlowAdapter extends RecyclerView.Adapter {
        private Context context;
        private ArrayList<AwardsRecordVo.AwardsRecordInfo> awardsRecordInfoArrayList;

        public FlowAdapter(ArrayList<AwardsRecordVo.AwardsRecordInfo> arrayList) {
            super();
            this.awardsRecordInfoArrayList = arrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_withdrawa_flow_item, parent, false);
            final FlowHolder viewHolder = new FlowAdapter.FlowHolder(itemView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            FlowHolder viewHolder = (FlowHolder) holder;

            AwardsRecordVo.AwardsRecordInfo info = awardsRecordInfoArrayList.get(position);
            viewHolder.showInfoName.setText(info.title);
            String bonusTip = String.format(getContext().getString(R.string.txt_awards_bonus_tip), info.money);
            viewHolder.showBonus.setText(bonusTip);
            String reTurnover = String.format(getContext().getString(R.string.txt_awards_required_turnover_tip), info.dispensing_money);
            viewHolder.showTurnover.setText(reTurnover);

            if (TextUtils.isEmpty(info.dispensing_money_left) || info.dispensing_money_left.equals("0")) {
                viewHolder.showContent.setText(getContext().getString(R.string.txt_awards_remain_turnover_default_tip));
            } else {
                String remainTurnover = String.format(getContext().getString(R.string.txt_awards_remain_turnover_tip), info.dispensing_money_left);
                viewHolder.showContent.setText(remainTurnover);
            }

            if (info.bet_source_trans != null || !TextUtils.isEmpty(info.bet_source_trans)) {
                viewHolder.showVenue.setText(info.bet_source_trans);
            } else {
                CfLog.e("********** info.bet_source_trans is null");
                viewHolder.showVenue.setVisibility(View.INVISIBLE);
            }

        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return awardsRecordInfoArrayList.size();
        }

        private class FlowHolder extends RecyclerView.ViewHolder {
            private View itemView;
            public TextView showInfoName;
            public TextView showBonus;//奖金
            public TextView showTurnover;//流水
            public TextView showContent;//内容
            public ExpandableTextView showVenue;//适用场馆

            public FlowHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                showInfoName = (TextView) this.itemView.findViewById(R.id.tvw_title);
                showBonus = (TextView) this.itemView.findViewById(R.id.tvw_title_bonus);
                showTurnover = (TextView) this.itemView.findViewById(R.id.tvw_required_turnover);
                showContent = (TextView) this.itemView.findViewById(R.id.tvw_title_content);
                showVenue = (ExpandableTextView) this.itemView.findViewById(R.id.tvw_venue);
            }

            public View getItemView() {
                return itemView;
            }
        }
    }

}
