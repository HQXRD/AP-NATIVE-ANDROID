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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;

public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private EditText editText;
    private BtCarDialogFragment.KeyBoardListener listener;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setListener(BtCarDialogFragment.KeyBoardListener listener) {
        this.listener = listener;
    }
    public KeyboardView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_keyboard, this);

        RecyclerView rvDefaultAmount = findViewById(R.id.rv_default_amount);
        rvDefaultAmount.setLayoutManager(new GridLayoutManager(context, 5));
        Integer[] defaultAmount = new Integer[]{10, 50, 100, 500, 1000};
        rvDefaultAmount.setAdapter(new CommonAdapter<Integer>(context, R.layout.bt_layout_keyboard_item, Arrays.asList(defaultAmount)) {
            @Override
            protected void convert(ViewHolder holder, Integer i, int position) {
                holder.setText(R.id.tv_item, String.valueOf(i));
                holder.itemView.setOnClickListener(view -> editText.setText(String.valueOf(i)));
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
                    String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : "";
                    value += number;
                    editText.setText(value);
                });
            }

        });

        findViewById(R.id.ll_expand).setOnClickListener(this);
        findViewById(R.id.ll_delete).setOnClickListener(view -> {
            String value = !TextUtils.isEmpty(editText.getText()) ? editText.getText().toString() : " ";
            value = value.substring(0, value.length()-1);
            editText.setText(value);
        });
        findViewById(R.id.tv_max).setOnClickListener(view -> {
            CharSequence hint = editText.getHint();
            String hintStr = !TextUtils.isEmpty(hint) ? hint.toString() : "-";
            hintStr = hintStr.substring(hintStr.indexOf("-") + 1, hintStr.length());
            editText.setText(hintStr);
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ll_expand){
            listener.showKeyBoard(false);
        }
    }
}
