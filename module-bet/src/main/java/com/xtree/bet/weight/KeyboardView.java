package com.xtree.bet.weight;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.base.utils.CfLog;
import com.xtree.bet.R;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class KeyboardView extends FrameLayout implements View.OnClickListener {
    public static final String KEY_USER_BALANCE = "key_user_balance";
    private EditText editText;
    private BtCarDialogFragment.KeyBoardListener mKeyBoardListener;
    private boolean isShow;
    private NestedScrollView parent;
    private int parentY;
    private int mCurrentPos = -1;
    private double mMaxValue;
    private List<Integer> defaultAmount = new ArrayList<>();

    private InputFilter lengthFilter = (source, start, end, dest, dstart, dend) -> {
        // source:当前输入的字符
        // start:输入字符的开始位置
        // end:输入字符的结束位置
        // dest：当前已显示的内容
        // dstart:当前光标开始位置
        // dent:当前光标结束位置
        if (dest.length() == 0 && source.equals(".")) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == 2) {//输入框小数的位数
                return "";
            }
        }
        return null;
    };

    public void setEditText(EditText editText, View itemView, double maxValue) {
        this.editText = editText;
        this.mMaxValue = maxValue;
        if (editText.getFilters().length == 0) {
            editText.setFilters(new InputFilter[]{lengthFilter});
        }
        postDelayed(() -> {

            int[] outLocation = new int[2];
            itemView.getLocationInWindow(outLocation);
            int editAreaY = outLocation[1];

            int[] outLocation1 = new int[2];
            getLocationInWindow(outLocation1);
            int keyBoardY = outLocation1[1];

            int editAreaHeight = itemView.getMeasuredHeight();

            if (editAreaY + editAreaHeight > keyBoardY) {
                int y = editAreaY + editAreaHeight * 2 - keyBoardY;
                parent.scrollBy(0, y);
            }
        }, 120);

    }

    public void setKeyBoardListener(BtCarDialogFragment.KeyBoardListener mKeyBoardListener) {
        this.mKeyBoardListener = mKeyBoardListener;
    }

    public void setParent(NestedScrollView parent) {
        this.parent = parent;
        int[] outLocation = new int[2];
        ((View) parent.getParent()).getLocationInWindow(outLocation);
        parentY = outLocation[1];
    }

    public KeyboardView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_keyboard, this);

        RecyclerView rvDefaultAmount = findViewById(R.id.rv_default_amount);
        rvDefaultAmount.setLayoutManager(new GridLayoutManager(context, 5));

        initDefaultAmount();

        rvDefaultAmount.setAdapter(new CommonAdapter<Integer>(context, R.layout.bt_layout_keyboard_item, defaultAmount) {
            @Override
            protected void convert(ViewHolder holder, Integer textValue, int position) {
                holder.setText(R.id.tv_item, String.valueOf(textValue));
                holder.setTextColor(R.id.tv_item, getResources().getColor(R.color.bt_color_keyboard_quick));
                holder.getView(R.id.tv_item).setBackgroundResource(R.drawable.bt_bg_keyboard_quick_item_selector);
                if (mCurrentPos == position) {
                    holder.getView(R.id.tv_item).setSelected(true);
                } else {
                    holder.getView(R.id.tv_item).setSelected(false);
                }
                holder.itemView.setOnClickListener(view -> {
                    if (editText == null || !editText.isEnabled()) {
                        return;
                    }
                    editText.setText(String.valueOf(textValue));
                    mCurrentPos = textValue > mMaxValue ? -1 : position;
                    notifyDataSetChanged();
                });
            }

        });

        RecyclerView rvNumber = findViewById(R.id.rv_number);
        rvNumber.setLayoutManager(new GridLayoutManager(context, 3));
        String[] numbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "00"};
        rvNumber.setAdapter(new CommonAdapter<String>(context, R.layout.bt_layout_keyboard_item, Arrays.asList(numbers)) {
            @Override
            protected void convert(ViewHolder holder, String number, int position) {
                holder.setText(R.id.tv_item, number);
                holder.itemView.setOnClickListener(view -> {
                    if (editText == null || !editText.isEnabled()) {
                        return;
                    }
                    String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : "";
                    String[] splitArray = value.split("\\.");
                    if (splitArray.length > 1) {
                        String dotValue = splitArray[1];
                        if (dotValue.length() == 2) {//输入框小数的位数
                            return;
                        }
                    }
                    value += number;
                    if (isNumeric(value)) {
                        editText.setText(value);
                        mCurrentPos = -1;
                        setDefaultAmountSelected(rvDefaultAmount);
                    }
                });
            }

        });

        findViewById(R.id.ll_expand).setOnClickListener(this);
        findViewById(R.id.ll_delete).setOnClickListener(view -> {
            if (editText != null) {
                String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : " ";
                value = value.substring(0, value.length() - 1);
                editText.setText(value);
                if (TextUtils.isEmpty(value)) {
                    mCurrentPos = -1;
                    rvDefaultAmount.getAdapter().notifyDataSetChanged();
                } else if (isNumeric(value)) {
                    setDefaultAmountSelected(rvDefaultAmount);
                }
            }
        });
        findViewById(R.id.tv_max).setOnClickListener(view -> {
            if (editText == null || !editText.isEnabled()) {
                return;
            }
            CharSequence hint = editText.getHint();
            String hintStr = !TextUtils.isEmpty(hint) ? hint.toString() : "-";
            hintStr = hintStr.substring(hintStr.indexOf("-") + 1, hintStr.length());
            String balance = SPUtils.getInstance().getString(KEY_USER_BALANCE, "0");
            if (isNumeric(hintStr)) {
                editText.setText(Double.parseDouble(hintStr) > Double.parseDouble(balance) ? balance : hintStr);
                mCurrentPos = -1;
                setDefaultAmountSelected(rvDefaultAmount);
            }
        });
    }

    private void initDefaultAmount() {
        defaultAmount.add(100);
        defaultAmount.add(500);
        defaultAmount.add(1000);
        defaultAmount.add(2000);
        defaultAmount.add(5000);
    }

    public void setDefaultAmountSelected(RecyclerView rvDefaultAmount){
        for (int i = 0; i < defaultAmount.size(); i ++){
            if(Double.parseDouble(editText.getText().toString()) == defaultAmount.get(i)){
                mCurrentPos = i;
                break;
            }
        }
        rvDefaultAmount.getAdapter().notifyDataSetChanged();
    }

    public void show() {
        isShow = true;
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        isShow = false;
        this.setVisibility(GONE);
    }

    public boolean isShowing() {
        return isShow;
    }

    private boolean isNumeric(String value) {
        if (TextUtils.isEmpty(value)) {
            return true;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_expand) {
            mKeyBoardListener.showKeyBoard(false);
        }
    }
}
