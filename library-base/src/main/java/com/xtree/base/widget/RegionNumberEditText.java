package com.xtree.base.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.xtree.base.utils.CfLog;

import me.xtree.mvvmhabit.utils.ToastUtils;

public class RegionNumberEditText extends EditText {
    private Context context ;
    private int max ;
    private int min ;
    public RegionNumberEditText(Context context) {
        super(context);
        this.context = context ;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context ;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context ;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context ;
    }

    public void setRegion(int max , int min){
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        this.max = max;
        this.min = min ;
        this.setTextWatcher();
    }
    public void setTextWatcher(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0){
                    if (min != -1 && max != -1){
                        try {
                            int num = Integer.parseInt(s.toString());
                            if (num > max){
                                s= String.valueOf(max);
                                setText(s);
                                ToastUtils.showError("金额不能超过"+max+"元");
                            } else if (num < min) {
                                s = String.valueOf(min);
                            }
                        }catch (NumberFormatException exception){
                            CfLog.e(exception.toString());
                        }
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
