package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionPm;
import com.xtree.bet.databinding.BtFbListItemPlayTypeItemOptionBinding;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 赛果
 */
public class OptionResultAdapter extends BaseAdapter<Option> {

    public OptionResultAdapter(Context context, List<Option> datas) {
        super(context, datas);
    }

    @Override
    public int layoutId() {
        return R.layout.bt_fb_list_item_play_type_item_option;
    }

    @Override
    protected void convert(ViewHolder holder, Option o, int position) {
        BtFbListItemPlayTypeItemOptionBinding binding = BtFbListItemPlayTypeItemOptionBinding.bind(holder.itemView);
        OptionPm option = (OptionPm) o;
        binding.tvOptionUnable.setVisibility(View.GONE);
        binding.tvOptionOdd.setVisibility(View.VISIBLE);
        binding.tvOptionName.setVisibility(View.VISIBLE);
        binding.tvOptionName.setText(option.optionInfo.ott + option.optionInfo.on);
        binding.tvOptionName.setTextSize(12);

        String result = "";
        switch (option.optionInfo.result) {
            case 2: {
                result = "走水";
                break;
            }
            case 3: {
                result = "输";
                break;
            }
            case 4: {
                result = "赢";
                break;
            }
            case 5: {
                result = "赢半";
                break;
            }
            case 6: {
                result = "输半";
                break;
            }
            case 7: {
                result = "赛事取消";
                break;
            }
            case 8: {
                result = "赛事延期";
                break;
            }
        }
        binding.tvOptionOdd.setText(result);
        binding.tvOptionOdd.setTextSize(14);

    }

}
