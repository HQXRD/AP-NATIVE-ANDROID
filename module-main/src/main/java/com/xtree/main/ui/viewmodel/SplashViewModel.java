package com.xtree.main.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.main.data.MainRepository;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis on 2023/11/24.
 */
public class SplashViewModel extends BaseViewModel<MainRepository> {
    public MutableLiveData<Void> inMainData = new MutableLiveData<>();
    public MutableLiveData<Void> reNewViewModel = new MutableLiveData<>();
    public MutableLiveData<Void> noWebData = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
    }

    public void getFBGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        CfLog.i("FBService****** ");
                        SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }
                });
        addSubscribe(disposable);
    }

    public void getFBXCGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getFBXCGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getPMGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                        inMainData.setValue(null);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        inMainData.setValue(null);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        inMainData.setValue(null);
                    }
                });
        addSubscribe(disposable);
    }

}
