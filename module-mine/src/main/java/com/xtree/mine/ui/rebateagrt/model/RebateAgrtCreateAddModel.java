package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

import io.reactivex.functions.Consumer;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtCreateAddModel extends BindModel implements BindHead {

    private Consumer<String> consumer = null;
    public ObservableField<Boolean> openAdd = new ObservableField<>(true);

    public RebateAgrtCreateAddModel() {
    }

    public RebateAgrtCreateAddModel(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void add() {
        if (consumer != null) {
            try {
                consumer.accept("");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean getItemHover() {
        return false;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}
