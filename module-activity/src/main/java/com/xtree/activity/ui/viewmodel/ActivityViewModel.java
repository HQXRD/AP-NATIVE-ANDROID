package com.xtree.activity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.activity.data.ActivityRepository;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

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
                        SPUtils.getInstance().put(SPKeyGlobal.DC_NEWS_LIST, new Gson().toJson(list));
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

    public void readCache() {
        CfLog.i("******");
        Gson gson = new Gson();
        String json = SPUtils.getInstance().getString(SPKeyGlobal.DC_NEWS_LIST, "[]");
        ArrayList list = gson.fromJson(json, new TypeToken<ArrayList<NewVo>>() {
        }.getType());

        if(list.size() != 0) {
            liveDataNewList.setValue(list);
        }
    }

}
