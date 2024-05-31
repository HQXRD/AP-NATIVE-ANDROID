package com.xtree.mine.ui.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.CookieVo;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.UserUsdtJumpVo;
import com.xtree.mine.vo.VerificationCodeVo;
import com.xtree.mine.vo.VerifyVo;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class VerifyViewModel extends BaseViewModel<MineRepository> {

    public MutableLiveData<CookieVo> liveDataCookie = new MutableLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile2 = new MutableLiveData<>();
    public MutableLiveData<VerificationCodeVo> liveDataCode = new MutableLiveData<>(); // 发送验证码 绑定用
    //public MutableLiveData<VerificationCodeVo> liveDataCode2 = new MutableLiveData<>(); // 发送验证码 修改登录密码用
    //public MutableLiveData<VerificationCodeVo> liveDataCode3 = new MutableLiveData<>(); // 发送验证码 验证其它业务用
    public MutableLiveData<VerifyVo> liveDataSingleVerify1 = new MutableLiveData<>(); // 验证验证码(首次绑定用)
    public MutableLiveData<VerifyVo> liveDataSingleVerify2 = new MutableLiveData<>(); // 验证验证码(修改密码)
    public MutableLiveData<VerifyVo> liveDataSingleVerify3 = new MutableLiveData<>(); // 验证验证码(验证用)

    public MutableLiveData<VerifyVo> liveDataUpdateVerify1 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataUpdateVerify2 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataUpdateVerify3 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataUpdateVerify4 = new MutableLiveData<>(); // 验证验证码

    public MutableLiveData<VerifyVo> liveDataBindVerify1 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataBindVerify2 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataBindVerify3 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<VerifyVo> liveDataBindVerify4 = new MutableLiveData<>(); // 验证验证码
    public MutableLiveData<LoginResultVo> liveDataLogin = new MutableLiveData<>(); // 异地登录/换设备登录
    public MutableLiveData<Map<String, String>> liveDataChangePwd = new MutableLiveData<>(); // 修改密码
    public MutableLiveData<Map<String, String>> liveDataChangeFundsPwd = new MutableLiveData<>(); // 修改资金密码

    public VerifyViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getCookie() {
        Disposable disposable = (Disposable) model.getApiService().getCookie()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<CookieVo>() {
                    @Override
                    public void onResult(CookieVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie_name);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.sessid);
                        RetrofitClient.init();
                        liveDataCookie.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取用户个人信息
     */
    public void getProfile() {
        getProfile(liveDataProfile);
    }

    /**
     * 获取用户个人信息 (关闭当前页面)
     */
    public void getProfile2() {
        getProfile(liveDataProfile2);
    }

    private void getProfile(MutableLiveData<ProfileVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, vo.auto_thrad_status);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        SPUtils.getInstance().put(SPKeyGlobal.USER_ID, vo.userid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.username);
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 绑定用
     *
     * @param flag
     * @param sendtype
     * @param num
     */
    public void singleSend1(String flag, String sendtype, String num) {
        singleSend(flag, sendtype, num, liveDataCode);
    }

    /**
     * 修改登录密码用
     *
     * @param flag
     * @param sendtype
     * @param num
     */
    public void singleSend2(String flag, String sendtype, String num) {
        singleSend(flag, sendtype, num, liveDataCode);
    }

    /**
     * 验证其它业务用
     *
     * @param flag
     * @param sendtype
     * @param num
     */
    public void singleSend3(String flag, String sendtype, String num) {
        singleSend(flag, sendtype, num, liveDataCode);
    }

    private void singleSend(String flag, String sendtype, String num, MutableLiveData<VerificationCodeVo> liveData) {
        Disposable disposable = (Disposable) model.getApiService().singleSend(flag, sendtype, num)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerificationCodeVo>() {
                    @Override
                    public void onResult(VerificationCodeVo vo) {
                        CfLog.i(vo.toString());
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 绑定用
     * <p>绑定手机和邮箱接口，（点击确认，包括账户管理）<p/>
     *
     * @param map
     */
    public void singleVerify1(Map<String, String> map) {
        singleVerify(map, liveDataSingleVerify1);
    }

    /**
     * 修改密码
     *
     * @param map
     */
    public void singleVerify2(Map<String, String> map) {
        singleVerify(map, liveDataSingleVerify2);
    }

    /**
     * 验证用
     * <p>绑定手机和邮箱接口，（点击确认，包括账户管理）<p/>
     *
     * @param map
     */
    public void singleVerify3(Map<String, String> map) {
        singleVerify(map, liveDataSingleVerify3);
    }

    private void singleVerify(Map<String, String> map, MutableLiveData<VerifyVo> liveData) {
        Disposable disposable = (Disposable) model.getApiService().singleVerify(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerifyVo>() {
                    @Override
                    public void onResult(VerifyVo vo) {
                        if (vo != null) {
                            CfLog.i(vo.toString());
                        }
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void updateVerify1(Map<String, String> map) {
        updateVerify(map, liveDataUpdateVerify1);
    }

    public void updateVerify2(Map<String, String> map) {
        updateVerify(map, liveDataUpdateVerify2);
    }

    public void updateVerify3(Map<String, String> map) {
        updateVerify(map, liveDataUpdateVerify3);
    }

    public void updateVerify4(Map<String, String> map) {
        updateVerify(map, liveDataUpdateVerify4);
    }

    private void updateVerify(Map<String, String> map, MutableLiveData<VerifyVo> liveData) {
        Disposable disposable = (Disposable) model.getApiService().updateVerify(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerifyVo>() {
                    @Override
                    public void onResult(VerifyVo vo) {
                        CfLog.i("****** "); // vo.toString()
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void bindVerify1(Map<String, String> map) {
        bindVerify(map, liveDataBindVerify1);
    }

    public void bindVerify2(Map<String, String> map) {
        bindVerify(map, liveDataBindVerify2);
    }

    public void bindVerify3(Map<String, String> map) {
        bindVerify(map, liveDataBindVerify3);
    }

    public void bindVerify4(Map<String, String> map) {
        bindVerify(map, liveDataBindVerify4);
    }

    private void bindVerify(Map<String, String> map, MutableLiveData<VerifyVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().bindVerify(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerifyVo>() {
                    @Override
                    public void onResult(VerifyVo vo) {
                        CfLog.i("****** "); // vo.toString()
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void changeLoginPwd(Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().changeLoginPwd(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        CfLog.i(vo + "");
                        doLogout();
                        liveDataChangePwd.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void changeFundsPwd(Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().changeFundsPwd(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        liveDataChangeFundsPwd.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void sendCodeByLogin(Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().sendCodeByLogin(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerificationCodeVo>() {
                    @Override
                    public void onResult(VerificationCodeVo vo) {
                        CfLog.i(vo.toString());
                        liveDataCode.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void login(Map<String, String> map) {

        String userName = map.get("username");

        Disposable disposable = (Disposable) model.getApiService().login(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        CfLog.i(vo.toString());
                        //ToastUtils.showLong("登录成功");
                        vo.userName = userName;
                        if (vo.twofa_required == 0) {
                            setLoginSucc(vo);
                        }
                        liveDataLogin.setValue(vo);
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

    public void doLogout() {

        SPUtils.getInstance().remove(SPKeyGlobal.USER_TOKEN);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_SHARE_SESSID);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_PROFILE);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_VIP_INFO);
        SPUtils.getInstance().remove(SPKeyGlobal.HOME_NOTICE_LIST);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_ID);
        SPUtils.getInstance().remove(SPKeyGlobal.USER_NAME);
        SPUtils.getInstance().remove(SPKeyGlobal.MSG_PERSON_INFO);
        RetrofitClient.init();

    }

    public void goOthers(Activity ctx, String type, VerifyVo vo) {
        String title = "";
        String remind = "";
        //String url = "";
        switch (type) {
            case Constant.RESET_LOGIN_PASSWORD:
                CfLog.i("****** 修改登录密码...");
                startNextPage(ctx, RouterFragmentPath.Mine.PAGER_CHANGE_PWD, vo);
                return;
            case Constant.BIND_CARD:
                //title = ctx.getString(R.string.txt_bind_card); //"银行卡管理";
                //remind = ctx.getString(R.string.txt_remind_card);
                //url = DomainUtil.getDomain2() + "/user/userbankinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                startNextPage(ctx, RouterFragmentPath.Mine.PAGER_BIND_CARD, vo);
                return;
            case Constant.BIND_USDT:
                title = ctx.getString(R.string.txt_bind_usdt); //"绑定USDT";
                remind = ctx.getString(R.string.txt_remind_usdt_trc20) + ";\n" + ctx.getString(R.string.txt_remind_usdt_erc20);
                //url = DomainUtil.getDomain2() + "/user/userusdtinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                //startNextPage(ctx, RouterFragmentPath.Mine.PAGER_BIND_USDT, vo);
                startUsdt(ctx, title, remind, vo, true);
                return;
            case Constant.BIND_EBPAY:
                title = ctx.getString(R.string.txt_bind_ebpay); //"绑定EBPAY";
                remind = ctx.getString(R.string.txt_remind_ebpay);
                //url = DomainUtil.getDomain2() + "/user/userebpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                //startUsdt(ctx, title, vo);
                break;
            case Constant.BIND_TOPAY:
                title = ctx.getString(R.string.txt_bind_topay); //"绑定TOPAY";
                remind = ctx.getString(R.string.txt_remind_topay);
                //url = DomainUtil.getDomain2() + "/user/usertopayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;

            case Constant.BIND_GCNYT:
            case Constant.BIND_HIWALLET:
                title = ctx.getString(R.string.txt_bind_gcnyt); //"绑定CNYT";
                remind = ctx.getString(R.string.txt_remind_gcnyt);
                //url = DomainUtil.getDomain2() + "/user/userhiwalletinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_MPAY:
                title = ctx.getString(R.string.txt_bind_mpay); //"绑定MPAY";
                remind = ctx.getString(R.string.txt_remind_mpay);
                //url = DomainUtil.getDomain2() + "/user/usermpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_GOBAO:
                title = ctx.getString(R.string.txt_bind_gobao); //"绑定GOBAO";
                remind = ctx.getString(R.string.txt_remind_gobao);
                //url = DomainUtil.getDomain2() + "/user/usergobaoinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_GOPAY:
                title = ctx.getString(R.string.txt_bind_gopay); //"绑定GOPAY";
                remind = ctx.getString(R.string.txt_remind_gopay);
                //url = DomainUtil.getDomain2() + "/user/usergopayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_OKPAY:
                title = ctx.getString(R.string.txt_bind_okpay); //"绑定OKPAY";
                remind = ctx.getString(R.string.txt_remind_okpay);
                //url = DomainUtil.getDomain2() + "/user/userokpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_ALIPAY:
            case Constant.BIND_WECHAT:
                // "绑定支付宝" 或 "绑定微信"
                startAlipayWechat(ctx, type, vo);
                return;

            default:
                CfLog.e("****** type: " + type + ", " + vo.toString());
                return;
        }

        startUsdt(ctx, title, remind, vo);
        //new XPopup.Builder(ctx).asCustom(new BrowserDialog(ctx, title, url)).show();
        /*Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra("url", url);
        it.putExtra("title", title);
        ctx.startActivity(it);*/
    }

    private void startAlipayWechat(Context ctx, String type, VerifyVo vo) {
        Bundle bundle = new Bundle();
        //bundle.putString("mark", vo.mark);
        bundle.putString("mark", type); // 20240418 使用type是因为用户绑手机邮箱后直接跳过来但mark还是"bind"
        bundle.putString("tokenSign", vo.tokenSign);
        Intent intent = new Intent(ctx, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_BIND_ALIPAY_WECHAT);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        ctx.startActivity(intent);
    }

    private void startUsdt(Context ctx, String title, String remind, VerifyVo vo) {
        startUsdt(ctx, title, remind, vo, false);
    }

    private void startUsdt(Context ctx, String title, String remind, VerifyVo vo, boolean isShowType) {
        String key = vo.mark.replace("bind", ""); // usdt
        UserUsdtJumpVo t = new UserUsdtJumpVo();
        t.title = title;
        t.remind = remind;
        t.key = key;
        t.mark = vo.mark;
        t.tokenSign = vo.tokenSign;
        t.isShowType = isShowType;
        t.controller = "security";
        t.action = "adduser" + key; // adduserusdt
        t.type = key.toUpperCase(); // // ERC20_USDT,TRC20_USDT

        Bundle bundle = new Bundle();
        bundle.putParcelable("obj", t);
        Intent intent = new Intent(ctx, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_BIND_USDT);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        ctx.startActivity(intent);
    }

    private void startNextPage(Context ctx, String path, VerifyVo vo) {
        Bundle bundle = new Bundle();
        bundle.putString("mark", vo.mark);
        bundle.putString("tokenSign", vo.tokenSign);
        Intent intent = new Intent(ctx, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, path);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        ctx.startActivity(intent);
    }

    public void readCache() {
        CfLog.i("******");
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = new Gson().fromJson(json, ProfileVo.class);
        if (vo != null) {
            liveDataProfile.setValue(vo);
        }
    }

}
