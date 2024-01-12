package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;

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

    public void startUp() {
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_up));
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bt_icon_odd_up, 0);
        postDelayed(() -> {
            setSelected(isSelected());
            setTextColor(getContext().getResources().getColorStateList(R.color.bt_option_item_odd_selector));
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }, 3000);
    }

    public void startDown() {
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_down));
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bt_icon_odd_down, 0);
        postDelayed(() -> {
            setSelected(isSelected());
            setTextColor(getContext().getResources().getColorStateList(R.color.bt_option_item_odd_selector));
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }, 3000);
    }

    public void setOptionOdd(Option option) {
        setText(String.valueOf(option.getOdd()));
        if (option.isUp()) {
            //Log.e("test", "====startUp=======");
            startUp();
        } else if (option.isDown()) {
            //Log.e("test", "====startDown=======");
            startDown();
        }
        option.reset();
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
