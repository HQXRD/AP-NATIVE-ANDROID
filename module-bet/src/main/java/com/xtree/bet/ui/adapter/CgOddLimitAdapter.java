package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.KeyboardView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseActivity;

public class CgOddLimitAdapter extends BaseAdapter<CgOddLimit> {
    private boolean flag;

    private boolean isRefresh;

    private KeyboardView keyboardView;
    private int mPosition = -1;

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

    public CgOddLimitAdapter(Context context, List<CgOddLimit> datas) {
        super(context, datas);
    }

    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_cg_item;
    }

    @Override
    protected void convert(ViewHolder holder, CgOddLimit cgOddLimit, int position) {
        /*if (getItemCount() > 1 || !TextUtils.equals("单关", cgOddLimit.getCgName())) { // 串关
            holder.setVisible(R.id.csl_cg_dan, false);
            holder.setVisible(R.id.csl_cg_cc, true);
            EditText etAmount = holder.getView(R.id.et_bt_amount_cc);
            etAmount.setHint("限制" + cgOddLimit.getCMin() + "-" + cgOddLimit.getCMax());
            etAmount.setEnabled(cgOddLimit.getCMin() > 0 && cgOddLimit.getCMax() > 0);
            *//*if(cgOddLimit.getBtAmount() > 0) {
                etAmount.setText(String.valueOf(cgOddLimit.getBtAmount()));
            }*//*
            holder.setText(R.id.iv_name, cgOddLimit.getCgName());
            holder.setText(R.id.iv_zs_amount, "x" + cgOddLimit.getBtCount());

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textChanged(etAmount, charSequence, holder, cgOddLimit, cgOddLimit.getCMin(), cgOddLimit.getCMax(), cgOddLimit.getCOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay, R.id.tv_win_cc, R.id.tv_pay_cc, R.id.csl_win_cc);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);



            if(isRefresh){
                if(mPosition == position){
                    etAmount.requestFocus();
                    etAmount.findFocus();
                    keyboardView.setEditText(etAmount);
                }
            }else{
                etAmount.setFocusable(true);
                etAmount.setFocusableInTouchMode(true);
            }

            etAmount.setOnFocusChangeListener((view, b) -> {
                keyboardView.setEditText(etAmount);
                mPosition = position;
                if(isRefresh) {
                    mKeyBoardListener.showKeyBoard(keyboardView.isShowing());
                }else{
                    mKeyBoardListener.showKeyBoard(b);
                }
                *//*if(b && !keyboardView.isShow()) {

                }*//*

            });
            *//*etAmount.setOnClickListener(view -> {
                mPosition = position;
                keyboardView.setEditText(etAmount);
                etAmount.setFocusable(true);
                etAmount.setFocusableInTouchMode(true);
                mKeyBoardListener.showKeyBoard(true);

            });*//*
        } else {
            holder.setVisible(R.id.csl_cg_dan, true);
            holder.setVisible(R.id.csl_cg_cc, false);
            EditText etAmount = holder.getView(R.id.et_bt_amount_dan);
            etAmount.setHint("限制" + cgOddLimit.getDMin() + "-" + cgOddLimit.getDMax());
            etAmount.setEnabled(cgOddLimit.getDMin() > 0 || cgOddLimit.getDMax() > 0);

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textChanged(etAmount, charSequence, holder, cgOddLimit, cgOddLimit.getDMin(), cgOddLimit.getDMax(), cgOddLimit.getDOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay, R.id.tv_win_dan, R.id.tv_pay_dan, R.id.csl_win_dan);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);

            etAmount.setOnFocusChangeListener((view, b) -> {
                mKeyBoardListener.showKeyBoard(keyboardView.isShowing());
                keyboardView.setEditText(etAmount);
            });
            etAmount.setFocusable(true);
            etAmount.setFocusableInTouchMode(true);
            etAmount.requestFocus();
            etAmount.findFocus();
            keyboardView.setEditText(etAmount);

            etAmount.setOnClickListener(view -> {
                mKeyBoardListener.showKeyBoard(true);
                keyboardView.setEditText(etAmount);
            });
        }*/
    }


    /**
     * @param etAmount
     * @param charSequence
     * @param holder
     * @param cgOddLimit
     * @param minValue       最小限额
     * @param maxValue       最大限额
     * @param odd            赔率
     * @param winResStringId 可赢金额 string id
     * @param payResStringId 投注金额 string id
     * @param winResId       可赢金额 textview id
     * @param payResId       投注金额 textview id
     * @param cslWin         单关或串关groupview id
     */
    private void textChanged(EditText etAmount, CharSequence charSequence, ViewHolder holder,
                               CgOddLimit cgOddLimit, double minValue, double maxValue, double odd,
                               int winResStringId, int payResStringId, int winResId, int payResId, int cslWin) {
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
                holder.setText(winResId, mContext.getResources().getString(winResStringId, String.valueOf(odd * amount)));
                holder.setText(payResId, mContext.getResources().getString(payResStringId, String.valueOf(amount)));
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
                            holder.setText(winResId, mContext.getResources().getString(winResStringId, String.valueOf(odd * minValue)));
                            holder.setText(payResId, mContext.getResources().getString(payResStringId, String.valueOf(minValue)));
                            flag = false;
                        });

                    });
                }

            } else if (amount > maxValue) {
                etAmount.setText(String.valueOf(maxValue));
                etAmount.setSelection(String.valueOf(maxValue).length());
                holder.setText(winResId, mContext.getResources().getString(winResStringId, String.valueOf(odd * maxValue)));
                holder.setText(payResId, mContext.getResources().getString(payResStringId, String.valueOf(maxValue)));
            } else {
                holder.setText(winResId, mContext.getResources().getString(winResStringId, String.valueOf(odd * amount)));
                holder.setText(payResId, mContext.getResources().getString(payResStringId, String.valueOf(amount)));
            }
            holder.setVisible(cslWin, true);
            cgOddLimit.setBtAmount(TextUtils.isEmpty(etAmount.getText()) ? 0 : Double.valueOf(etAmount.getText().toString()));
        } else {
            holder.setVisible(cslWin, false);
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
            } catch (Exception e) {//TODO: handle exception
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
        }
    }
}
