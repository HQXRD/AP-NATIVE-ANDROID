package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;

public class DiscolourTextView extends AppCompatTextView {
    private boolean isBtCar;
    public DiscolourTextView(@NonNull Context context) {
        super(context);
    }

    public DiscolourTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscolourTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBtCar(boolean btCar) {
        isBtCar = btCar;
    }

    public void startUp() {
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_up));
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bt_icon_odd_up, 0);
        postDelayed(() -> {
            setSelected(isSelected());
            if(isBtCar){
                setTextColor(getContext().getResources().getColor(R.color.bt_color_car_dialog_hight_line2));
            }else{
                setTextColor(getContext().getResources().getColorStateList(R.color.bt_option_item_odd_selector));
            }

            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }, 3000);
    }

    public void startDown() {
        setTextColor(getContext().getResources().getColor(R.color.bt_color_odd_down));
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bt_icon_odd_down, 0);
        postDelayed(() -> {
            setSelected(isSelected());
            if(isBtCar){
                setTextColor(getContext().getResources().getColor(R.color.bt_color_car_dialog_hight_line2));
            }else{
                setTextColor(getContext().getResources().getColorStateList(R.color.bt_option_item_odd_selector));
            }
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }, 3000);
    }

    public void setOptionOdd(Option option) {
        if(isBtCar){
            setText("@" + option.getUiShowOdd());
        }else{
            setText(String.valueOf(option.getUiShowOdd()));
        }

        if (option.isUp()) {
            //Log.e("test", "====startUp=======");
            startUp();
        } else if (option.isDown()) {
            //Log.e("test", "====startDown=======");
            startDown();
        }
        option.reset();
    }
}
