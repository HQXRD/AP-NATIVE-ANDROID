package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtree.mine.R;
import com.xtree.mine.vo.WithdrawVo.WithdrawalBankInfoVo;

import java.util.ArrayList;

/**
 * 银行卡提款固额 金额适配器
 */
public class BankAmountRecyclerViewAdapter extends BaseAdapter {
    public interface IBankAmountRecyclerCallback {
        public void callbackWithAmount(WithdrawalBankInfoVo.WithdrawalAmountVo amountVo);
    }
    private Context context;
    public ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> arrayList;
    private IBankAmountRecyclerCallback callback;
    public BankAmountRecyclerViewAdapter(Context context,ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> list, IBankAmountRecyclerCallback callback) {
        super();
        this.context = context;
        this.arrayList = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      HolderView holderView = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_withdrawal_amount_child, parent, false);
            holderView = new HolderView();
            holderView.textView = (TextView) view.findViewById(R.id.tv_top_child);
            holderView.linear = (LinearLayout) view.findViewById(R.id.cl_title);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        HolderView finalHolderView = holderView;

        if (arrayList.get(position).flag) {
            finalHolderView.linear.setBackgroundResource(R.drawable.bg_dialog_amount_selected);
        } else {
            finalHolderView.linear.setBackgroundResource(R.drawable.bg_dialog_top_bank_amount_noselected);
        }
        holderView.setShowAmount(arrayList.get(position).amount);


        holderView.getTextView().setOnClickListener(v -> {
            if (callback != null) {
                callback.callbackWithAmount(arrayList.get(position));
            }

            referArray(arrayList.get(position), arrayList);

        });
        holderView.linear.setOnClickListener(v -> {
            if (callback != null) {
                callback.callbackWithAmount(arrayList.get(position));
               /* finalHolderView.linear.setBackgroundResource(R.drawable.bg_dialog_amount_selected);
                arrayList.get(position).flag = true;*/
                referArray(arrayList.get(position), arrayList);
            }
        });
        return view;
    }

    private void referArray(WithdrawalBankInfoVo.WithdrawalAmountVo amountVo, ArrayList<WithdrawalBankInfoVo.WithdrawalAmountVo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).amount.equals(amountVo.amount)) {
                arrayList.get(i).flag = true;
            } else {
                arrayList.get(i).flag = false;
            }

        }
    }
    private class HolderView {
        private String showAmount;

        public void setShowAmount(String showAmount) {
            this.showAmount = showAmount;
            this.textView.setText(showAmount);
        }

        public String getShowAmount() {
            return showAmount;
        }

        private TextView textView;
        private LinearLayout linear;

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
