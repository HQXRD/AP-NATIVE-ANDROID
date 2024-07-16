package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.vo.PromotionCodeVo;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.SettingsVo;
import com.xtree.mine.vo.RegisterVerificationCodeVo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<MineRepository> {

    public SingleLiveData<LoginResultVo> liveDataLogin = new SingleLiveData<>();
    public SingleLiveData<BusinessException> liveDataLoginFail = new SingleLiveData<>();
    public SingleLiveData<LoginResultVo> liveDataReg = new SingleLiveData<>();
    public SingleLiveData<BusinessException> regErrorLiveData = new SingleLiveData<>();//验证码注册 异常
    public MutableLiveData<SettingsVo> liveDataSettings = new MutableLiveData<>();
    public MutableLiveData<PromotionCodeVo> promotionCodeVoMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<RegisterVerificationCodeVo> verificationCodeMutableLiveData = new MutableLiveData<RegisterVerificationCodeVo>();//获取注册验证码
    public SingleLiveData<BusinessException> liveDataLoginVerFail = new SingleLiveData<>();//登录需要使用验证码
    public MutableLiveData<RegisterVerificationCodeVo> verLoginCodeMutableLiveData = new MutableLiveData<RegisterVerificationCodeVo>();//获取注册验证码
    String public_key;

    public LoginViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
    }

    public void login(String userName, String pwd) {
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        //KLog.i("password: " + password);

        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
        String loginpass = RSAEncrypt.encrypt2(pwd, public_key);
        //KLog.i("loginpass: " + loginpass);

        if (TextUtils.isEmpty(loginpass)) {
            return;
        }

        HashMap<String, String> map = new HashMap();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "login");
        map.put("validcode", "");
        map.put("client_id", "10000005"); // h5:10000003, ios:10000004, android:10000005
        map.put("device_type", "app"); // pc,h5,app,(ios,android)
        map.put("loginpass", loginpass);
        map.put("nonce", UuidUtil.getID16());

        Disposable disposable = (Disposable) model.getApiService().login(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        if (vo == null || TextUtils.isEmpty(vo.token)) {
                            //后台对登录增加了某些新功能,可能异常返回为空(短时间登录多次/多次输错密码)
                            ToastUtils.showLong("登录异常，请稍候再试");
                            return;
                        }
                        KLog.i(vo.toString());
                        //ToastUtils.showLong("登录成功");
                        vo.userName = userName;
                        if (vo.twofa_required == 0) {
                            setLoginSucc(vo); // 不需要谷歌验证
                        }
                        // 登录成功后获取FB体育请求服务地址
                        //getFBGameTokenApi();
                        liveDataLogin.setValue(vo); // twofa_required=1 时需要谷歌验证
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        //super.onFail(t); // 弹提示 (改成弹窗提示了)
                        KLog.e(t.toString());

                        if (t.code == HttpCallBack.CodeRule.CODE_20208) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            liveDataLoginFail.setValue(t);
                        } else if (t.code == HttpCallBack.CodeRule.CODE_20204
                                ||t.code == HttpCallBack.CodeRule.CODE_20205
                                ||t.code == HttpCallBack.CodeRule.CODE_20206) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            liveDataLoginVerFail.setValue(t);
                        } else {
                            super.onFail(t);
                        }

                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 使用验证码登录
     * @param userName
     * @param pwd
     * @param key
     * @param validcode
     */
    public void loginAndVer(String userName, String pwd ,final String key , final String validcode){
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        //KLog.i("password: " + password);

        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
        String loginpass = RSAEncrypt.encrypt2(pwd, public_key);
        //KLog.i("loginpass: " + loginpass);

        if (TextUtils.isEmpty(loginpass)) {
            return;
        }

        HashMap<String, String> map = new HashMap();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "login");
        map.put("validcode", key+":"+validcode);//登录验证码
        map.put("client_id", "10000005"); // h5:10000003, ios:10000004, android:10000005
        map.put("device_type", "app"); // pc,h5,app,(ios,android)
        map.put("loginpass", loginpass);
        map.put("nonce", UuidUtil.getID16());

        Disposable disposable = (Disposable) model.getApiService().loginAndVer(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        if (vo == null || TextUtils.isEmpty(vo.token)) {
                            //后台对登录增加了某些新功能,可能异常返回为空(短时间登录多次/多次输错密码)
                            ToastUtils.showLong("登录异常，请稍候再试");
                            return;
                        }
                        KLog.i(vo.toString());
                        //ToastUtils.showLong("登录成功");
                        vo.userName = userName;
                        if (vo.twofa_required == 0) {
                            setLoginSucc(vo); // 不需要谷歌验证
                        }
                        // 登录成功后获取FB体育请求服务地址
                        //getFBGameTokenApi();
                        liveDataLogin.setValue(vo); // twofa_required=1 时需要谷歌验证
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        //super.onFail(t); // 弹提示 (改成弹窗提示了)
                        KLog.e(t.toString());

                        if (t.code == HttpCallBack.CodeRule.CODE_20208) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            liveDataLoginFail.setValue(t);
                        } else {
                            super.onFail(t);
                        }

                    }
                });
        addSubscribe(disposable);
    }

    public void setLoginSucc(LoginResultVo vo) {
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, vo.token);
        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, vo.token_type);
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.cookie.sessid);
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie.cookie_name);
        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.userName); // 用户名
        // 解决登录后,首页显示为未登录,过2秒才显示登录名和金额的问题
        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(new ProfileVo(vo.userName, "***")));
        RetrofitClient.init();
    }

    /**
     * @param userName
     * @param pwd
     * @param code
     * @param validcode
     */
    public void register(String userName, String pwd, String code, final String key,final String validcode) {
        HashMap<String, String> map = new HashMap();
        map.put("carryAuth", "false");

     /*   if (code == null) {
            //未获取到推广码 回传官方推广code
            map.put("code", "kygprka");
            map.put("id", "kygprka");
        } else {
            map.put("code", code);
            map.put("id", code);//
        }*/
        map.put("code", code);
        map.put("id", code);//

        map.put("nonce", UuidUtil.getID16());
        map.put("username", userName);
        map.put("userpass", pwd); // 明文
        map.put("validcode", key+":"+validcode);//注册验证码

        CfLog.e("*********** register  code1=" + map);
        Disposable disposable = (Disposable) model.getApiService().register(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        if (vo == null) {
                            CfLog.e("result is null");
                            return;
                        }
                        ToastUtils.showLong("注册成功");
                        vo.userName = userName;
                        vo.userpass = pwd;
                        setLoginSucc(vo);
                        // 登录成功后获取FB体育请求服务地址
                        //getFBGameTokenApi();
                        liveDataReg.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.e(t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        //super.onFail(t); // 弹提示 (改成弹窗提示了)
                        KLog.e(t.toString());

                        if (t.code == HttpCallBack.CodeRule.CODE_20208) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            regErrorLiveData.setValue(t);
                        } else if (t.code == HttpCallBack.CodeRule.CODE_20204
                                ||t.code == HttpCallBack.CodeRule.CODE_20205
                                ||t.code == HttpCallBack.CodeRule.CODE_20206) {
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("loginArgs", new Gson().toJson(map));
                            map2.put("data", t.data);
                            t.data = map2;
                            regErrorLiveData.setValue(t);
                            super.onFail(t);
                        } else {
                            super.onFail(t);
                        }

                    }
                });
        addSubscribe(disposable);
    }

    public void getSettings() {
        HashMap<String, String> map = new HashMap();
        /*map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code");*/
        map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code"+",register_captcha_switch");

        CfLog.e("**************** MAP = " + map);
        Disposable disposable = (Disposable) model.getApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SettingsVo>() {
                    @Override
                    public void onResult(SettingsVo vo) {
                        CfLog.e("setting --------------" + vo.toString());
                        public_key = vo.public_key
                                .replace("\n", "")
                                .replace("\t", " ")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

                        SPUtils.getInstance().put(SPKeyGlobal.PUBLIC_KEY, public_key);
                        SPUtils.getInstance().put("customer_service_url", vo.customer_service_url);
                        SPUtils.getInstance().put(SPKeyGlobal.PROMOTION_CODE, vo.promption_code);//推广code

                        liveDataSettings.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取 pro接口的code
     */
    public void getPromotion() {
        Disposable disposable = (Disposable) model.getApiService().getPromotion()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PromotionCodeVo>() {

                    @Override
                    public void onResult(PromotionCodeVo promotionCodeVo) {
                        SPUtils.getInstance().put(SPKeyGlobal.PROMOTION_CODE, promotionCodeVo.domian);//推广code
                        promotionCodeVoMutableLiveData.setValue(promotionCodeVo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
    /**
     * 获取注册 图形验证码头
     */
    public void getLoginCaptcha() {
        Disposable disposable = (Disposable) model.getApiService().getCaptcha()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RegisterVerificationCodeVo>() {

                    @Override
                    public void onResult(RegisterVerificationCodeVo vo) {
                        CfLog.e("captcha = " + vo.toString());
                        verLoginCodeMutableLiveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
    /**
     * 获取注册 图形验证码头
     */
    public void getCaptcha() {
        Disposable disposable = (Disposable) model.getApiService().getCaptcha()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RegisterVerificationCodeVo>() {

                    @Override
                    public void onResult(RegisterVerificationCodeVo vo) {
                        CfLog.e("captcha = " + vo.toString());
                        verificationCodeMutableLiveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
}
