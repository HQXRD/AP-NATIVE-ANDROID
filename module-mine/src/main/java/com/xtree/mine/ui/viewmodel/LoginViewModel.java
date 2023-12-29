package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.FBService;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.SettingsVo;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<MineRepository> {

    public SingleLiveData<LoginResultVo> liveDataLogin = new SingleLiveData<>();
    public SingleLiveData<LoginResultVo> liveDataReg = new SingleLiveData<>();
    String public_key;

    public LoginViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void login(String userName, String pwd) {
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        CfLog.i("password: " + password);

        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
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
        map.put("client_id", "10000005"); // h5:10000003, ios:10000004, android:10000005
        map.put("loginpass", loginpass);
        map.put("nonce", UuidUtil.getID16());

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
                        RetrofitClient.init();
                        // 登录成功后获取FB体育请求服务地址
                        getFBGameTokenApi();
                        //liveDataLogin.setValue(vo);
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

    public void register(String userName, String pwd) {
        HashMap<String, String> map = new HashMap();
        map.put("carryAuth", "false");
        map.put("code", "");
        map.put("nonce", UuidUtil.getID16());
        map.put("username", userName);
        map.put("userpass", pwd); // 明文

        Disposable disposable = (Disposable) model.getApiService().register(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        ToastUtils.showLong("注册成功");
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, vo.token);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, vo.token_type);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.cookie.sessid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie.cookie_name);
                        RetrofitClient.init();
                        // 登录成功后获取FB体育请求服务地址
                        getFBGameTokenApi();
                        //liveDataReg.setValue(vo);
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

    public void getSettings() {
        HashMap<String, String> map = new HashMap();
        map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code");
        Disposable disposable = (Disposable) model.getApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SettingsVo>() {
                    @Override
                    public void onResult(SettingsVo vo) {
                        //CfLog.i(vo.toString());
                        public_key = vo.public_key
                                .replace("\n", "")
                                .replace("\t", " ")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

                        SPUtils.getInstance().put(SPKeyGlobal.PUBLIC_KEY, public_key);
                        SPUtils.getInstance().put("customer_service_url", vo.customer_service_url);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void getFBGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        KLog.e("========fbService.getToken()======" + fbService.getToken());
                        //finish();
                        liveDataLogin.setValue(null); // 登录成功,重新打开APP
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
