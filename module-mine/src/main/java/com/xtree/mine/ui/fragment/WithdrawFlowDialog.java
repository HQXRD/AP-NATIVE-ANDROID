package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
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
    DialogWithdrawalFlowBinding binding;
    ChooseWithdrawViewModel viewModel;
    LifecycleOwner owner;
    private AwardsRecordVo awardsRecordVo;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_withdrawal_flow;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
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
        if (!awardsRecordVo.list.isEmpty() &&awardsRecordVo.list.size() > 0) {
            tipText = String.format(getContext().getString(R.string.txt_awards_flow_title), awardsRecordVo.withdraw_dispensing_money);
            binding.tvChooseTip.setText(tipText);
            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.lvChoose.setVisibility(View.VISIBLE);
            ChooseAdapter adapter = new ChooseAdapter(getContext(), awardsRecordVo.list);
            binding.lvChoose.setAdapter(adapter);
        } /*else if (!TextUtils.isEmpty(awardsRecordVo.withdraw_dispensing_money) && awardsRecordVo.list.isEmpty()) {
            tipText = String.format(getContext().getString(R.string.txt_awards_flow_title), awardsRecordVo.withdraw_dispensing_money);
            binding.tvChooseTip.setText(tipText);
            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.tvWithdrawalFlowTip.setVisibility(View.GONE);
            binding.lvChoose.setVisibility(View.GONE);
        }*/ else if (!"0.00".equals(awardsRecordVo.withdraw_dispensing_money)) {
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

    private class ChooseAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<AwardsRecordVo.AwardsRecordInfo> awardsRecordInfoArrayList;

        public ChooseAdapter(Context context, ArrayList<AwardsRecordVo.AwardsRecordInfo> list) {
            this.context = context;
            this.awardsRecordInfoArrayList = list;
        }

        @Override
        public int getCount() {
            return this.awardsRecordInfoArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.awardsRecordInfoArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChooseAdapterViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.dialog_withdrawa_flow_item, parent, false);
                holder = new ChooseAdapterViewHolder();
                holder.showInfoName = (TextView) convertView.findViewById(R.id.tvw_title);
                holder.showBonus = (TextView) convertView.findViewById(R.id.tvw_title_bonus);
                holder.showTurnover = (TextView) convertView.findViewById(R.id.tvw_required_turnover);
                holder.showContent = (TextView) convertView.findViewById(R.id.tvw_title_content);
                convertView.setTag(holder);
            } else {
                holder = (ChooseAdapterViewHolder) convertView.getTag();
            }
            AwardsRecordVo.AwardsRecordInfo info = awardsRecordInfoArrayList.get(position);

            holder.showInfoName.setText(info.title);
            String bonusTip = String.format(getContext().getString(R.string.txt_awards_bonus_tip), info.money);
            holder.showBonus.setText(bonusTip);
            String reTurnover = String.format(getContext().getString(R.string.txt_awards_required_turnover_tip), info.dispensing_money);
            holder.showTurnover.setText(reTurnover);

            if (TextUtils.isEmpty(info.dispensing_money_left) || info.dispensing_money_left.equals("0")) {
                holder.showContent.setText(getContext().getString(R.string.txt_awards_remain_turnover_default_tip));
            } else {
                String remainTurnover = String.format(getContext().getString(R.string.txt_awards_remain_turnover_tip), info.dispensing_money_left);
                holder.showContent.setText(remainTurnover);
            }

            return convertView;
        }

        public class ChooseAdapterViewHolder {
            public TextView showInfoName;
            public TextView showBonus;//奖金
            public TextView showTurnover;//流水
            public TextView showContent;//内容
        }
    }

}
