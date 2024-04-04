package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.contract.GamePlatformEnum;
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
    public final MutableLiveData<String> buttonTextData = new MutableLiveData<>();

    public TransferResultViewModel(@NonNull Application application) {
        super(application);
    }

    public TransferResultViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);

    }

    public void initData(TransferResultModel model) {
        statusData.setValue(model.status);
        String fromName = GamePlatformEnum.checkCode(model.from).getName();;
        String toName = GamePlatformEnum.checkCode(model.to).getName();;
        if (model.status == 1) {
            statusTitleData.setValue("转账成功");
            buttonTextData.setValue("继续转账");
            statusImgData.setValue(getApplication().getDrawable(R.mipmap.icon_transfer_success));
            statusSubTitleData.setValue("您已从" + fromName + "转入" + toName + model.money + "元");
        } else {
            statusTitleData.setValue("转账失败");
            buttonTextData.setValue("返回上一步");
            statusImgData.setValue(getApplication().getDrawable(R.mipmap.icon_transfer_fail));
            statusSubTitleData.setValue("您从" + fromName + "转入" + toName + model.money + "元转账失败" + "\n" + model.errorMsg);
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
