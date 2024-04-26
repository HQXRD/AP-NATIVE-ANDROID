package com.xtree.base.widget.lottery;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.brv.BindingAdapter;
import com.xtree.base.R;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/22.
 * Describe: 彩票选注数据模型
 */
public class LotteryPickViewModel {
    public static final int SINGLE_MODE = 0x00;
    public static final int MULTI_MODE = 0x01;

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lottery_pick);
                    add(R.layout.dialog_lottery_play_collection);
                    add(R.layout.item_lottery_play_collection);
                }
            });

    public ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>();

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_lottery_pick) {
                View loView = view.findViewById(R.id.lottery_button);
                if (loView != null) {
                    loView.setOnClickListener(v -> {

                        LotteryPickModel model = (LotteryPickModel) bindingViewHolder.getModel();
                        if (mode == SINGLE_MODE) {
                            for (BindModel bindModel : bindModels) {
                                LotteryPickModel m = (LotteryPickModel) bindModel;
                                m.checked.set(model.number == m.number);
                            }
                        } else {
                            model.checked.set(Boolean.FALSE.equals(model.checked.get()));
                        }

                        if (pickListener != null) {
                            pickListener.onPick(getPick());
                        }
                    });
                }
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
        }
    };

    public LotteryPickView.onPickListener pickListener = null;

    private int mode = MULTI_MODE;

    public void initData(List<String> list) {

        bindModels.clear();

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                LotteryPickModel model = new LotteryPickModel(i, list.get(i));
                bindModels.add(model);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                LotteryPickModel model = new LotteryPickModel(i, String.valueOf(i));
                bindModels.add(model);
            }
        }
        datas.set(bindModels);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public List<Integer> getPick() {
        ArrayList<Integer> list = new ArrayList<>();
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                if (Boolean.TRUE.equals(model.checked.get())) {
                    list.add(model.number);
                }
            }
        }
        return list;
    }

    public void pickAll() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                model.checked.set(true);
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void pickLarge() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                if (model.number > 4) {
                    model.checked.set(true);
                } else {
                    model.checked.set(false);
                }
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void pickSmall() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                if (model.number <= 4) {
                    model.checked.set(true);
                } else {
                    model.checked.set(false);
                }
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void pickOdd() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                if (model.number % 2 == 1) {
                    model.checked.set(true);
                } else {
                    model.checked.set(false);
                }
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void pickEven() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                if (model.number % 2 == 0) {
                    model.checked.set(true);
                } else {
                    model.checked.set(false);
                }
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void pickClear() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                model.checked.set(false);
            }
        }
        if (pickListener != null) {
            pickListener.onPick(getPick());
        }
    }

    public void showOmission(List<Integer> list) {
        Integer max = Collections.max(list);
        for (int i = 0; i < list.size(); i++) {
            LotteryPickModel model = (LotteryPickModel) bindModels.get(i);
            int value = list.get(i);
            model.tag.set(String.valueOf(value));
            if (value == max) {
                model.markColor.set(BaseApplication.getInstance().getResources().getColor(R.color.red));
            } else {
                model.markColor.set(BaseApplication.getInstance().getResources().getColor(R.color.textColor));
            }
        }
    }

    public void showHotCold(List<Integer> list) {
        Integer max = Collections.max(list);
        Integer min = Collections.min(list);
        for (int i = 0; i < list.size(); i++) {
            LotteryPickModel model = (LotteryPickModel) bindModels.get(i);
            int value = list.get(i);
            model.tag.set(String.valueOf(value));
            if (value == max) {
                model.markColor.set(BaseApplication.getInstance().getResources().getColor(R.color.red));
            } else if (value == min) {
                model.markColor.set(BaseApplication.getInstance().getResources().getColor(R.color.cl_withdrawal));
            } else {
                model.markColor.set(BaseApplication.getInstance().getResources().getColor(R.color.textColor));
            }
        }
    }

    public void closeTag() {
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof LotteryPickModel) {
                LotteryPickModel model = (LotteryPickModel) bindModel;
                model.tag.set("");
            }
        }
    }
}
