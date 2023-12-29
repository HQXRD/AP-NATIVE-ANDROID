package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.bet.R;
import com.xtree.bet.databinding.BtLayoutSwitchBinding;

/**
 *
 */
public class SwitchView extends LinearLayout {
    LinearLayout root;

    private String[] switchText;

    private BtLayoutSwitchBinding binding;

    private int index;

    private boolean checked;

    private OnCheckedListener checkedListener;

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setCurrentPosition(int positon) {
        if(binding.llSwitch != null && binding.llSwitch.getChildCount() == 2) {
            binding.llSwitch.getChildAt(positon).setSelected(true);
            if (positon == 0) {
                binding.llSwitch.getChildAt(1).setSelected(false);
            } else {
                binding.llSwitch.getChildAt(0).setSelected(false);
            }
        }
    }

    public interface OnCheckedListener{
        void onSelected(int index);
    }

    public SwitchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_switch, this);
        root = findViewById(R.id.ll_root);
        binding = BtLayoutSwitchBinding.bind(root);
        binding.llSwitch.getChildAt(0).setSelected(true);
        binding.llSwitch.getChildAt(1).setSelected(false);
        root.setOnClickListener(v -> {
            binding.llSwitch.getChildAt(0).setSelected(!binding.llSwitch.getChildAt(0).isSelected());
            binding.llSwitch.getChildAt(1).setSelected(!binding.llSwitch.getChildAt(1).isSelected());
            if(checkedListener != null){
                checked = !checked;
                if(checked){
                    index = 1;
                }else{
                    index = 0;
                }
                checkedListener.onSelected(index);
            }
        });
    }

    public SwitchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwitchView(@NonNull Context context) {
        super(context);
    }

    public void setCheckedListener(OnCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    public void setSwitchText(String[] switchText){
        if(switchText == null && switchText.length != 2){
            return;
        }
        this.switchText = switchText;
        binding.tvOne.setText(this.switchText[0]);
        binding.tvTwo.setText(this.switchText[1]);
    }


}
