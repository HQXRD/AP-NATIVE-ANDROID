package com.xtree.activity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.activity.data.ActivityRepository;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class ActivityViewModel extends BaseViewModel<ActivityRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public SingleLiveData<ArrayList<NewVo>> liveDataNewList = new SingleLiveData<>();

    public ActivityViewModel(@NonNull Application application, ActivityRepository repository) {
        super(application, repository);
    }

    public void getNewList() {
        Disposable disposable = (Disposable) model.getApiService().getNewList()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ArrayList<NewVo>>() {
                    @Override
                    public void onResult(ArrayList<NewVo> list) {
                        //CfLog.d(vo.toString());
                        liveDataNewList.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
