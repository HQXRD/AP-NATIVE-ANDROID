package com.xtree.bet.weight;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;

public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private EditText editText;
    private BtCarDialogFragment.KeyBoardListener mKeyBoardListener;
    private boolean isShow;
    private NestedScrollView parent;
    private int parentY;
    private View itemView;
    private int currentPos = -1;
    private double maxValue;

    public void setEditText(EditText editText, View itemView, double maxValue) {
        this.editText = editText;
        this.itemView = itemView;
        this.maxValue = maxValue;
        postDelayed(() -> {

            int[] outLocation = new int[2];
            itemView.getLocationInWindow(outLocation);
            int editAreaY = outLocation[1];

            int[] outLocation1 = new int[2];
            getLocationInWindow(outLocation1);
            int keyBoardY = outLocation1[1];

            int editAreaHeight = itemView.getMeasuredHeight();

            if(editAreaY + editAreaHeight > keyBoardY){
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
        ((View)parent.getParent()).getLocationInWindow(outLocation);
        parentY = outLocation[1];
    }

    public KeyboardView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_keyboard, this);

        RecyclerView rvDefaultAmount = findViewById(R.id.rv_default_amount);
        rvDefaultAmount.setLayoutManager(new GridLayoutManager(context, 5));
        Integer[] defaultAmount = new Integer[]{100, 500, 1000, 2000, 5000};
        rvDefaultAmount.setAdapter(new CommonAdapter<Integer>(context, R.layout.bt_layout_keyboard_item, Arrays.asList(defaultAmount)) {
            @Override
            protected void convert(ViewHolder holder, Integer textValue, int position) {
                holder.setText(R.id.tv_item, String.valueOf(textValue));
                holder.setTextColor(R.id.tv_item, getResources().getColor(R.color.bt_text_color_deep_primary));
                holder.getView(R.id.tv_item).setBackgroundResource(R.drawable.bt_bg_keyboard_quick_item_selector);
                if(currentPos == position){
                    holder.getView(R.id.tv_item).setSelected(true);
                }else{
                    holder.getView(R.id.tv_item).setSelected(false);
                }
                holder.itemView.setOnClickListener(view -> {
                    if(editText == null || !editText.isEnabled()){
                        return;
                    }
                    editText.setText(String.valueOf(textValue));
                    currentPos = textValue > maxValue ? -1 : position;
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
                    if(editText == null || !editText.isEnabled()){
                        return;
                    }
                    String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : "";
                    value += number;
                    if(isNumeric(value)) {
                        editText.setText(value);
                        currentPos = -1;
                        rvDefaultAmount.getAdapter().notifyDataSetChanged();
                    }
                });
            }

        });

        findViewById(R.id.ll_expand).setOnClickListener(this);
        findViewById(R.id.ll_delete).setOnClickListener(view -> {
            if(editText != null){
                String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : " ";
                value = value.substring(0, value.length()-1);
                if(isNumeric(value)) {
                    editText.setText(value);
                }
                if(TextUtils.isEmpty(value)){
                    currentPos = -1;
                    rvDefaultAmount.getAdapter().notifyDataSetChanged();
                }
            }
        });
        findViewById(R.id.tv_max).setOnClickListener(view -> {
            if(editText == null || !editText.isEnabled()){
                return;
            }
            CharSequence hint = editText.getHint();
            String hintStr = !TextUtils.isEmpty(hint) ? hint.toString() : "-";
            hintStr = hintStr.substring(hintStr.indexOf("-") + 1, hintStr.length());
            if(isNumeric(hintStr)) {
                editText.setText(hintStr);
                currentPos = -1;
                rvDefaultAmount.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void show(){
        isShow = true;
        this.setVisibility(VISIBLE);
    }

    public void hide(){
        isShow = false;
        this.setVisibility(GONE);
    }

    public boolean isShowing() {
        return isShow;
    }

    private boolean isNumeric(String value){
        if (TextUtils.isEmpty(value)) {
            return true;
        }
        try {
            Double.parseDouble(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ll_expand){
            mKeyBoardListener.showKeyBoard(false);
            Log.e("test", "关闭=============" + isShow );
        }
    }
}
