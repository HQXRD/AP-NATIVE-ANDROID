package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtree.base.utils.NumberUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.databinding.BtLayoutCarCgItemBinding;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.CgOddLimitView;
import com.xtree.bet.weight.KeyboardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.StringUtils;

public class CgOddLimitSecAdapter extends CgOddLimitView.Adapter<CgOddLimit> {
    private boolean flag;

    private boolean isRefresh;

    private KeyboardView keyboardView;

    private List<BtLayoutCarCgItemBinding> bindingList = new ArrayList<>();

    private BtCarDialogFragment.KeyBoardListener mKeyBoardListener;

    public void setKeyboardView(KeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    public void setKeyBoardListener(BtCarDialogFragment.KeyBoardListener keyBoardListener) {
        this.mKeyBoardListener = keyBoardListener;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public CgOddLimitSecAdapter(Context context, List<CgOddLimit> datas) {
        super(context, datas);
    }

    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_cg_item;
    }

    @Override
    protected void convert(View itemView, CgOddLimit cgOddLimit, int position) {

        if (getItemCount() > 1 || !TextUtils.equals("单关", cgOddLimit.getCgName())) { // 串关

            itemView.findViewById(R.id.csl_cg_dan).setVisibility(View.GONE);
            itemView.findViewById(R.id.csl_cg_cc).setVisibility(View.VISIBLE);
            EditText etAmount = itemView.findViewById(R.id.et_bt_amount_cc);
            etAmount.setHint("限制" + cgOddLimit.getCMin() + "-" + cgOddLimit.getCMax());
            etAmount.setEnabled(cgOddLimit.getCMin() > 0 && cgOddLimit.getCMax() > 0);
            if(sizeChange) {
                itemView.findViewById(R.id.csl_win_cc).setVisibility(View.GONE);
                etAmount.setText("");
            }
            ((TextView)itemView.findViewById(R.id.iv_name)).setText(cgOddLimit.getCgName());
            ((TextView)itemView.findViewById(R.id.iv_zs_amount)).setText("x" + cgOddLimit.getBtCount());

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textChanged(etAmount, charSequence, cgOddLimit, cgOddLimit.getCMin(), cgOddLimit.getCMax(), cgOddLimit.getCOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay, itemView.findViewById(R.id.tv_win_cc), itemView.findViewById(R.id.tv_pay_cc), itemView.findViewById(R.id.csl_win_cc));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);

            etAmount.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus) {
                    keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_cc));
                    itemView.findViewById(R.id.csl_win_cc).setVisibility(View.VISIBLE);
                }else{
                    itemView.findViewById(R.id.csl_win_cc).setVisibility(View.GONE);
                }
                if(hasFocus && !keyboardView.isShowing()) {
                    mKeyBoardListener.showKeyBoard(true);
                }

            });

            etAmount.setOnClickListener(view -> {
                if(!etAmount.hasFocus()) {
                    keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_cc));
                }
                itemView.findViewById(R.id.csl_win_cc).setVisibility(View.VISIBLE);
                if(!keyboardView.isShowing()) {
                    mKeyBoardListener.showKeyBoard(true);
                }
            });
        } else {
            itemView.findViewById(R.id.csl_cg_dan).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.csl_cg_cc).setVisibility(View.GONE);
            EditText etAmount = itemView.findViewById(R.id.et_bt_amount_dan);
            etAmount.setHint("限制" + cgOddLimit.getDMin() + "-" + cgOddLimit.getDMax());
            etAmount.setEnabled(cgOddLimit.getDMin() > 0 || cgOddLimit.getDMax() > 0);

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textChanged(etAmount, charSequence, cgOddLimit, cgOddLimit.getDMin(), cgOddLimit.getDMax(), cgOddLimit.getDOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay, itemView.findViewById(R.id.tv_win_dan), itemView.findViewById(R.id.tv_pay_dan), itemView.findViewById(R.id.csl_win_dan));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);

            etAmount.setOnFocusChangeListener((view, b) -> {
                mKeyBoardListener.showKeyBoard(keyboardView.isShowing());
                keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan));
            });
            etAmount.setFocusable(true);
            etAmount.setFocusableInTouchMode(true);
            etAmount.requestFocus();
            etAmount.findFocus();
            keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan));

            etAmount.setOnClickListener(view -> {
                mKeyBoardListener.showKeyBoard(true);
                keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan));
            });
        }
    }

    /**
     * @param etAmount
     * @param charSequence
     * @param cgOddLimit
     * @param minValue       最小限额
     * @param maxValue       最大限额
     * @param odd            赔率
     * @param winResStringId 可赢金额 string id
     * @param payResStringId 投注金额 string id
     * @param tvWin       可赢金额 textview
     * @param tvPay       投注金额 textview
     * @param cslWin         单关或串关groupview
     */
    private void textChanged(EditText etAmount, CharSequence charSequence,
                             CgOddLimit cgOddLimit, double minValue, double maxValue, double odd,
                             int winResStringId, int payResStringId, TextView tvWin, TextView tvPay, View cslWin) {
        if (!etAmount.isEnabled()) {
            return;
        }

        if (charSequence != null && !TextUtils.isEmpty(charSequence.toString())) {
            double amount;
            if (charSequence.toString().startsWith(".")) {
                etAmount.setText("0");
                amount = 0;
            } else {
                amount = Double.valueOf(charSequence.toString());
            }

            if (amount < minValue) {
                tvWin.setText(mContext.getResources().getString(winResStringId, String.valueOf(odd * amount)));
                tvPay.setText(mContext.getResources().getString(payResStringId, String.valueOf(amount * cgOddLimit.getBtCount())));
                if (!flag) {
                    flag = true;
                    Disposable disposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(aLong -> {
                        if (TextUtils.isEmpty(etAmount.getText()) || Double.valueOf(etAmount.getText().toString()) >= minValue) {
                            flag = false;
                            return;
                        }
                        ((BaseActivity) mContext).runOnUiThread(() -> {
                            etAmount.setText(String.valueOf(minValue));
                            etAmount.setSelection(String.valueOf(minValue).length());

                            tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * minValue, 2)));
                            tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(minValue * cgOddLimit.getBtCount(), 2)));
                            flag = false;
                        });

                    });
                }

            } else if (amount > maxValue) {
                etAmount.setText(String.valueOf(maxValue));
                etAmount.setSelection(String.valueOf(maxValue).length());
                tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * maxValue, 2)));
                tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(maxValue * cgOddLimit.getBtCount(), 2)));
            } else {
                tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * amount, 2)));
                tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(amount * cgOddLimit.getBtCount(), 2)));
            }
            cslWin.setVisibility(View.VISIBLE);
            cgOddLimit.setBtAmount(TextUtils.isEmpty(etAmount.getText()) ? 0 : Double.valueOf(etAmount.getText().toString()));
        } else {
            if(!sizeChange) {
                cslWin.setVisibility(View.VISIBLE);
            }
            cgOddLimit.setBtAmount(0);
            tvWin.setText(mContext.getResources().getString(winResStringId, "0"));
            tvPay.setText(mContext.getResources().getString(payResStringId, "0"));
        }
    }


    public void disableShowInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }
}
