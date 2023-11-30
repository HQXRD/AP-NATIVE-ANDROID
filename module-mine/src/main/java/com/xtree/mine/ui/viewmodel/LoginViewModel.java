package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.SPUtil;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.LoginResultVo;

import java.util.HashMap;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<MineRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public LoginViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }


    public void login(Context ctx, String userName, String pwd) {
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        CfLog.i("password: " + password);

        String public_key = SPUtil.get(ctx).get("public_key", "");
        String loginpass = RSAEncrypt.encrypt2(pwd, public_key);
        CfLog.i("loginpass: " + loginpass);

        if (TextUtils.isEmpty(loginpass)) {
            return;
        }

        HashMap<String, String> map = new HashMap();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "login");
        map.put("validcode", "");
        map.put("client_id", "10000004"); // h5:10000003, ios:10000004, android:10000005
        map.put("loginpass", loginpass);
        map.put("nonce", UUID.randomUUID().toString().replace("-", "")); //

        Disposable disposable = (Disposable) model.getApiService().login(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        KLog.i(vo.toString());
                        ToastUtils.showLong("登录成功");
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, vo.token);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, vo.token_type);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.cookie.sessid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie.cookie_name);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                        ToastUtils.showLong("登录失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void register(Context ctx,String userName,String pwd){
        HashMap<String, String> map = new HashMap();
        map.put("carryAuth","false");
        map.put("code","");
        map.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        map.put("username",userName.toString());
        map.put("userpass",pwd.toString());

        Disposable disposable = (Disposable) model.getApiService().register(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<String>() {
                    @Override
                    public void onResult(String result) {

                        String content = result;
                    }
                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                        ToastUtils.showLong("注册失败");
                    }
                });
        addSubscribe(disposable);
    }
}
