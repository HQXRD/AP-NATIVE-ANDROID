package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.TransferResultModel;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/4/3.
 * Describe: 转账结果页
 */
public class TransferResultViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    private final MutableLiveData<String> titleData = new MutableLiveData<>("转账详情");
    public final MutableLiveData<Integer> statusData = new MutableLiveData<>();
    public final MutableLiveData<Drawable> statusImgData = new MutableLiveData<android.graphics.drawable.Drawable>();
    public final MutableLiveData<String> statusTitleData = new MutableLiveData<>();
    public final MutableLiveData<String> statusSubTitleData = new MutableLiveData<>();

    public TransferResultViewModel(@NonNull Application application) {
        super(application);
    }

    public TransferResultViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(TransferResultModel model) {
        statusData.setValue(model.status);
        if (model.status == 1) {
            statusTitleData.setValue("转账成功");
            statusImgData.setValue(getApplication().getDrawable(R.mipmap.icon_transfer_success));
            statusSubTitleData.setValue("您已从" + model.from + "转入" + model.to + model.money + "元");
        } else {
            statusTitleData.setValue("转账失败");
            statusImgData.setValue(getApplication().getDrawable(R.mipmap.icon_transfer_fail));
            statusSubTitleData.setValue("您从" + model.from + "转入" + model.to + model.money + "元转账失败" + "\n转账失败(" + model.errorMsg + ")");
        }
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }
}
