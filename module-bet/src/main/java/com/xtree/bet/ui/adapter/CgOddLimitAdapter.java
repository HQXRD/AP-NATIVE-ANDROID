package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.KeyboardView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseActivity;

public class CgOddLimitAdapter extends CommonAdapter<CgOddLimit> {
    private boolean flag;

    private KeyboardView keyboardView;

    private BtCarDialogFragment.KeyBoardListener listener;

    public void setKeyboardView(KeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    public void setListener(BtCarDialogFragment.KeyBoardListener listener) {
        this.listener = listener;
    }

    public CgOddLimitAdapter(Context context, int layoutId, List<CgOddLimit> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CgOddLimit cgOddLimit, int position) {
        if (getItemCount() > 1) { // 串关
            holder.setVisible(R.id.csl_cg_dan, false);
            holder.setVisible(R.id.csl_cg_cc, true);
            EditText etAmount = holder.getView(R.id.et_bt_amount_cc);
            etAmount.setHint("限制" + cgOddLimit.getCMin() + "-" + cgOddLimit.getCMax());
            holder.setText(R.id.iv_name, cgOddLimit.getCgName());
            holder.setText(R.id.iv_zs_amount, "x" + cgOddLimit.getBtCount());
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence != null && !TextUtils.isEmpty(charSequence.toString())) {
                        double amount;
                        if(charSequence.toString().startsWith(".")){
                            etAmount.setText("0");
                            amount = 0;
                        }else{
                            amount = Double.valueOf(charSequence.toString());
                        }
                        if (amount < cgOddLimit.getCMin()) {
                            holder.setText(R.id.tv_win_cc, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getCOdd() * amount)));
                            holder.setText(R.id.tv_pay_cc, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtCount() * amount)));
                            if (!flag) {
                                flag = true;
                                Disposable disposable = Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
                                    if (TextUtils.isEmpty(etAmount.getText()) || Double.valueOf(etAmount.getText().toString()) >= cgOddLimit.getCMin()) {
                                        flag = false;
                                        return;
                                    }
                                    ((BaseActivity) mContext).runOnUiThread(() -> {
                                        etAmount.setText(String.valueOf(cgOddLimit.getCMin()));
                                        etAmount.setSelection(String.valueOf(cgOddLimit.getCMin()).length());
                                        holder.setText(R.id.tv_win_cc, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getCOdd() * cgOddLimit.getCMin())));
                                        holder.setText(R.id.tv_pay_cc, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtCount() * cgOddLimit.getCMin())));
                                        flag = false;
                                    });
                                });
                            }

                        } else if (amount > cgOddLimit.getCMax()) {
                            etAmount.setText(String.valueOf(cgOddLimit.getCMax()));
                            etAmount.setSelection(String.valueOf(cgOddLimit.getCMax()).length());
                            holder.setText(R.id.tv_win_cc, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getCOdd() * cgOddLimit.getCMax())));
                            holder.setText(R.id.tv_pay_cc, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtCount() * cgOddLimit.getCMax())));
                        } else {
                            holder.setText(R.id.tv_win_cc, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getCOdd() * amount)));
                            holder.setText(R.id.tv_pay_cc, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtCount() * amount)));
                        }
                        holder.setVisible(R.id.csl_win_cc, true);
                    } else {
                        holder.setVisible(R.id.csl_win_cc, false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);
            etAmount.setOnFocusChangeListener((view, b) -> {
                listener.showKeyBoard(true);
                keyboardView.setEditText(etAmount);
            });
            etAmount.setOnClickListener(view -> {
                listener.showKeyBoard(true);
                keyboardView.setEditText(etAmount);
            });
        } else {
            holder.setVisible(R.id.csl_cg_dan, true);
            holder.setVisible(R.id.csl_cg_cc, false);
            EditText etAmount = holder.getView(R.id.et_bt_amount_dan);
            etAmount.setHint("限制" + cgOddLimit.getDMin() + "-" + cgOddLimit.getDMax());
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence != null && !TextUtils.isEmpty(charSequence.toString())) {
                        double amount;
                        if(charSequence.toString().startsWith(".")){
                            etAmount.setText("0");
                            amount = 0;
                        }else{
                            amount = Double.valueOf(charSequence.toString());
                        }

                        if (amount < cgOddLimit.getDMin()) {
                            holder.setText(R.id.tv_win_dan, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getDOdd() * amount)));
                            holder.setText(R.id.tv_pay_dan, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(amount)));
                            if (!flag) {
                                flag = true;
                                Disposable disposable = Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
                                    if (TextUtils.isEmpty(etAmount.getText()) || Double.valueOf(etAmount.getText().toString()) >= cgOddLimit.getDMin()) {
                                        flag = false;
                                        return;
                                    }
                                    ((BaseActivity) mContext).runOnUiThread(() -> {
                                        etAmount.setText(String.valueOf(cgOddLimit.getDMin()));
                                        etAmount.setSelection(String.valueOf(cgOddLimit.getDMin()).length());
                                        holder.setText(R.id.tv_win_dan, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getDOdd() * cgOddLimit.getDMin())));
                                        holder.setText(R.id.tv_pay_dan, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getDMin())));
                                        flag = false;
                                    });

                                });
                            }

                        } else if (amount > cgOddLimit.getDMax()) {
                            etAmount.setText(String.valueOf(cgOddLimit.getDMax()));
                            etAmount.setSelection(String.valueOf(cgOddLimit.getDMax()).length());
                            holder.setText(R.id.tv_win_dan, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getDOdd() * cgOddLimit.getDMax())));
                            holder.setText(R.id.tv_pay_dan, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getDMax())));
                        } else {
                            holder.setText(R.id.tv_win_dan, mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getDOdd() * amount)));
                            holder.setText(R.id.tv_pay_dan, mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(amount)));
                        }
                        holder.setVisible(R.id.csl_win_dan, true);
                    } else {
                        holder.setVisible(R.id.csl_win_dan, false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    etAmount.setSelection(TextUtils.isEmpty(etAmount.getText()) ? 0 : etAmount.getText().toString().length());
                }
            });
            disableShowInput(etAmount);
            etAmount.setFocusable(true);
            etAmount.setFocusableInTouchMode(true);
            etAmount.requestFocus();
            etAmount.findFocus();
            keyboardView.setEditText(etAmount);
            etAmount.setOnFocusChangeListener((view, b) -> {
                listener.showKeyBoard(true);
                keyboardView.setEditText(etAmount);
            });
            etAmount.setOnClickListener(view -> {
                listener.showKeyBoard(true);
                keyboardView.setEditText(etAmount);
            });
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
