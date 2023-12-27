package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xtree.bet.R;

import me.xtree.mvvmhabit.utils.StringUtils;

public class DiscolourTextView extends AppCompatTextView {
    public DiscolourTextView(@NonNull Context context) {
        super(context);
    }

    public DiscolourTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscolourTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startUp(){
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_up));
        postDelayed(() -> {
            setSelected(isSelected());
            setTextColor(getContext().getResources().getColor(R.color.bt_option_item_odd_selector));
        }, 5000);
    }

    public void startDown(){
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_down));
        postDelayed(() -> {
            setSelected(isSelected());
            setTextColor(getContext().getResources().getColor(R.color.bt_option_item_odd_selector));
        }, 5000);
    }

    /*public void setText(String newText) {
        CharSequence oldText = getText();
        super.setText(newText);
        if(oldText != null && newText != null){
            String oldStr = oldText.toString();
            if(oldStr.startsWith("@")){
                oldStr = oldStr.substring(1);
            }
            if(newText.startsWith("@")){
                newText = newText.substring(1);
            }
            if(StringUtils.isNumeric(oldStr) && StringUtils.isNumeric(newText)){
                if(Double.valueOf(newText) > Double.valueOf(oldStr)){
                    startUp();
                } else if (Double.valueOf(newText) < Double.valueOf(oldStr)) {
                    startDown();
                }
            }
        }
    }*/
}
