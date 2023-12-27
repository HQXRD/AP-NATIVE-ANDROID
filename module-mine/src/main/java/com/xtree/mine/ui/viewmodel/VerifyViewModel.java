package com.xtree.mine.ui.viewmodel;

import android.app.Activity;
import android.app.Application;
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
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.ProfileVo;
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

    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile2 = new MutableLiveData<>();
    public MutableLiveData<VerificationCodeVo> liveDataCode = new MutableLiveData<>(); // 发送验证码 绑定用
    //    public MutableLiveData<VerificationCodeVo> liveDataCode2 = new MutableLiveData<>(); // 发送验证码 修改登录密码用
    //    public MutableLiveData<VerificationCodeVo> liveDataCode3 = new MutableLiveData<>(); // 发送验证码 验证其它业务用
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
    public MutableLiveData<Map<String, String>> liveDataChangePwd = new MutableLiveData<>(); // 修改密码

    public VerifyViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }


    public void getProfile() {
        getProfile(liveDataProfile);
    }

    public void getProfile2() {
        getProfile(liveDataProfile2);
    }

    private void getProfile(MutableLiveData<ProfileVo> liveData) {

        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, vo.auto_thrad_status);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        ToastUtils.showLong("请求失败");
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VerifyVo>() {
                    @Override
                    public void onResult(VerifyVo vo) {
                        CfLog.i(vo.toString());
                        liveData.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        ToastUtils.showLong("请求失败");
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void changePwd(Map<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().changePwd(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        CfLog.i(vo.toString());
                        doLogout();
                        liveDataChangePwd.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void doLogout() {

        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, "");
        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_VIP_INFO, "");
        SPUtils.getInstance().put(SPKeyGlobal.HOME_NOTICE_LIST, "[]");
        RetrofitClient.init();

    }

    public void goOthers(Activity ctx, String type, VerifyVo vo) {
        String title = "";
        String url = "";
        switch (type) {
            case Constant.RESET_LOGIN_PASSWORD:
                CfLog.i("****** 修改密码...");
                Bundle bundle = new Bundle();
                bundle.putString("tokenSign", vo.tokenSign);
                bundle.putString("mark", vo.mark);
                Intent intent = new Intent(ctx, ContainerActivity.class);
                intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_CHANGE_PWD);
                if (bundle != null) {
                    intent.putExtra(ContainerActivity.BUNDLE, bundle);
                }
                ctx.startActivity(intent);
                return;
            case Constant.BIND_CARD:
                title = "银行卡管理";
                url = DomainUtil.getDomain2() + "/user/userbankinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_USDT:
                title = "绑定USDT";
                url = DomainUtil.getDomain2() + "/user/userusdtinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_EBPAY:
                title = "绑定EBPAY";
                url = DomainUtil.getDomain2() + "/user/userebpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_TOPAY:
                title = "绑定TOPAY";
                url = DomainUtil.getDomain2() + "/user/usertopayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;

            case Constant.BIND_GCNYT:
            case Constant.BIND_HIWALLET:
                title = "绑定CNYT";
                url = DomainUtil.getDomain2() + "/user/userhiwalletinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_MPAY:
                title = "绑定MPAY";
                url = DomainUtil.getDomain2() + "/user/usermpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_GOBAO:
                title = "绑定GOBAO";
                url = DomainUtil.getDomain2() + "/user/usergobaoinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_GOPAY:
                title = "绑定GOPAY";
                url = DomainUtil.getDomain2() + "/user/usergopayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;
            case Constant.BIND_OKPAY:
                title = "绑定OKPAY";
                url = DomainUtil.getDomain2() + "/user/userokpayinfo?check=" + vo.tokenSign + "&mark=" + vo.mark;
                break;

            default:
                CfLog.e("****** type: " + type + ", " + vo.toString());
                return;
        }

        //new XPopup.Builder(ctx).asCustom(new BrowserDialog(ctx, title, url)).show();
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra("url", url);
        it.putExtra("title", title);
        ctx.startActivity(it);
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
