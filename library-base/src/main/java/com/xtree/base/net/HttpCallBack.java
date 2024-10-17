package com.xtree.base.net;

import static com.xtree.base.utils.EventConstant.EVENT_LOG_OUT;
import static me.xtree.mvvmhabit.http.ExceptionHandle.ERROR.HIJACKED_ERROR;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.fastest.ChangeApiLineUtil;
import com.xtree.base.net.fastest.FastestTopDomainUtil;
import com.xtree.base.net.fastest.SpeedApiLine;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.EventVo;
import com.xtree.base.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.subscribers.DisposableSubscriber;
import io.sentry.Sentry;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.HijackedException;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public abstract class HttpCallBack<T> extends DisposableSubscriber<T> {
    public abstract void onResult(T t);

    @Override
    public void onNext(T o) {
        LoadingDialog.finish();
        if (!(o instanceof BaseResponse)) {
            KLog.w("json is not normal");
            onResult(o);
            return;
        }
        BaseResponse baseResponse = (BaseResponse) o;
        BusinessException ex = new BusinessException(baseResponse.getStatus(), baseResponse.getMessage(), baseResponse.getData());
        int status = baseResponse.getStatus() == -1 ? baseResponse.getCode() : baseResponse.getStatus();
        switch (status) {
            case HttpCallBack.CodeRule.CODE_0:
            case HttpCallBack.CodeRule.CODE_10000:
                if (baseResponse.getAuthorization() != null) {
                    SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, baseResponse.getAuthorization().token);
                    SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, baseResponse.getAuthorization().token_type);
                }
                //请求成功, 正确的操作方式
                onResult((T) baseResponse.getData());
                break;
            case HttpCallBack.CodeRule.CODE_300:
                //请求失败，不打印Message
                KLog.e("请求失败");
                ToastUtils.showShort("错误代码:", baseResponse.getStatus());
                break;
            case HttpCallBack.CodeRule.CODE_330:
                //请求失败，打印Message
                ToastUtils.showShort(baseResponse.getMessage());
                break;
            case HttpCallBack.CodeRule.CODE_500:
                //服务器内部异常
                ToastUtils.showShort("错误代码:", baseResponse.getStatus());
                break;
            case HttpCallBack.CodeRule.CODE_503:
                //参数为空
                KLog.e("参数为空");
                break;
            case HttpCallBack.CodeRule.CODE_502:
                //没有数据
                KLog.e("没有数据");
                break;
            case CodeRule.CODE_10003:
            case CodeRule.CODE_10039:
            case CodeRule.CODE_20203:
            case CodeRule.CODE_30004:
                ToastUtils.showShort(baseResponse.getMessage());
                break;
            //case HttpCallBack.CodeRule.CODE_510:
            //    //无效的Token，提示跳入登录页
            //    ToastUtils.showShort("token已过期，请重新登录");
            //    //关闭所有页面
            //    AppManager.getAppManager().finishAllActivity();
            //    //跳入登录界面
            //    //*****该类仅供参考，实际业务Code, 根据需求来定义，******//
            //    break;
            //case HttpCallBack.CodeRule.CODE_530:
            //    ToastUtils.showShort("请先登录");
            //    break;
            //case HttpCallBack.CodeRule.CODE_551:
            case HttpCallBack.CodeRule.CODE_20101:
            case HttpCallBack.CodeRule.CODE_20102:
            case HttpCallBack.CodeRule.CODE_20103:
            case HttpCallBack.CodeRule.CODE_20106:
            case HttpCallBack.CodeRule.CODE_20107:
            case HttpCallBack.CodeRule.CODE_20217:
            case HttpCallBack.CodeRule.CODE_20111:
            case HttpCallBack.CodeRule.CODE_30003:
            case HttpCallBack.CodeRule.CODE_30713:
                KLog.e("登出状态,销毁token. " + baseResponse);
                SPUtils.getInstance().remove(SPKeyGlobal.USER_TOKEN);
                SPUtils.getInstance().remove(SPKeyGlobal.USER_SHARE_SESSID);
                SPUtils.getInstance().remove(SPKeyGlobal.HOME_PROFILE);
                SPUtils.getInstance().remove(SPKeyGlobal.HOME_VIP_INFO);
                SPUtils.getInstance().remove(SPKeyGlobal.HOME_NOTICE_LIST);
                SPUtils.getInstance().remove(SPKeyGlobal.USER_ID);
                //SPUtils.getInstance().remove(SPKeyGlobal.USER_NAME);
                SPUtils.getInstance().remove(SPKeyGlobal.MSG_PERSON_INFO);
                SPUtils.getInstance().remove(SPKeyGlobal.FB_TOKEN);
                SPUtils.getInstance().remove(SPKeyGlobal.FBXC_TOKEN);
                SPUtils.getInstance().remove(SPKeyGlobal.PM_TOKEN);
                SPUtils.getInstance().remove(SPKeyGlobal.PMXC_TOKEN);
                SPUtils.getInstance().remove(SPKeyGlobal.IS_FIRST_OPEN_BROWSER);
                RetrofitClient.init();
                ToastUtils.showShort("请重新登录");
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                EventBus.getDefault().post(new EventVo(EVENT_LOG_OUT, ""));
                break;
            case HttpCallBack.CodeRule.CODE_20208:
            case HttpCallBack.CodeRule.CODE_30018:
                // 异地登录/换设备登录
                // 谷歌验证
                onFail(ex);
                break;
            case CodeRule.CODE_900001:

//                ValidateResponse validateResponse = new Gson().fromJson(baseResponse.getDataString(), ValidateResponse.class);
//
//                if (validateResponse != null && validateResponse.getData() != null && validateResponse.getData().containsKey("ip")) {
//                    AppUtil.goGlobeVerify(validateResponse.getData().get("ip"));
//                } else {
//                    AppUtil.goGlobeVerify("");
//                }
                break;
            case HttpCallBack.CodeRule.CODE_100002:
                TagUtils.tagEvent(Utils.getContext(), "API JSON数据转换失败", DomainUtil.getApiUrl());
                ToastUtils.showShort("当前网络环境异常，切换线路中..."); // ("域名被劫持"  + "，切换线路中...");
                ChangeApiLineUtil.getInstance().start();
                break;
            default:
                KLog.e("status is not normal: " + baseResponse);
                onFail(ex);
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        LoadingDialog.finish();
        KLog.e("error: " + t.toString());
        Sentry.captureException(t);
        //t.printStackTrace();
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            //ToastUtils.showLong(rError.message + " [" + rError.code + "]");
            KLog.e("code: " + rError.code);
            if (rError.code == 403) {
                AppUtil.goWeb403();
            } else if (rError.code == HIJACKED_ERROR) {
                HijackedException hijackedException = (HijackedException) t.getCause();
                TagUtils.tagEvent(Utils.getContext(), "API JSON数据转换失败", hijackedException.getUrl());
                TagUtils.tagEvent(Utils.getContext(), "event_hijacked", t.getMessage());
                TagUtils.tagEvent(Utils.getContext(), "event_change_api_line_start", " [" + rError.code + "]域名被劫持，切换线路开始...");
                if (!SpeedApiLine.INSTANCE.isRunning()) {
                    ToastUtils.showShort("当前网络环境异常" + " [" + rError.code + "]，切换线路中..."); // ("域名被劫持"  + "，切换线路中...");
                    SpeedApiLine.INSTANCE.addHijeckedDomainList(((HijackedException) t.getCause()).getHost());
                }
                SpeedApiLine.INSTANCE.start();
            } else if (rError.code == 401) {
                TagUtils.tagEvent(Utils.getContext(), "401 鉴权失败");
                FastestTopDomainUtil.getInstance().start();
            } else {
                TagUtils.tagEvent(Utils.getContext(), "API 测速失败", DomainUtil.getApiUrl());
                TagUtils.tagEvent(Utils.getContext(), "event_network_error", DomainUtil.getApiUrl() + "：" + t.getMessage());
                TagUtils.tagEvent(Utils.getContext(), "event_change_api_line_start", " [" + rError.code + "]域名无法访问，切换线路开始...");
                if (!SpeedApiLine.INSTANCE.isRunning()) {
                    //ToastUtils.showShort("当前网络环境异常" + " [" + rError.code + "]，切换线路中...");
                    ToastUtils.showShort("切换线路中...");
                    SpeedApiLine.INSTANCE.addHijeckedDomainList(DomainUtil.getApiUrl());
                }
                SpeedApiLine.INSTANCE.start();
            }
            return;
        } else if (t instanceof BusinessException) {
            BusinessException rError = (BusinessException) t;
            ToastUtils.showLong(rError.message + " [" + rError.code + "]");
            return;
        }
        //其他全部甩锅网络异常
        ToastUtils.showShort("网络异常");
    }

    public void onFail(BusinessException t) {
        LoadingDialog.finish();
        KLog.e("error: " + t.toString());
        Sentry.captureException(t);
        if (t.code == 41011) {
            onFail41011(t);
            return;
        }
        ToastUtils.showLong(t.message + " [" + t.code + "]");
    }

    /**
     *该场馆禁止当前用户玩乐
     */
    public void onFail41011(BusinessException t) {

    }

    @Override
    public void onComplete() {
        LoadingDialog.finish();
    }

    public static final class CodeRule {
        //请求成功, 正确的操作方式
        static final int CODE_0 = 0;
        static final int CODE_10000 = 10000;
        /**
         * 返回数据非json
         */
        static final int CODE_100002 = 100002;
        //请求失败，不打印Message
        static final int CODE_300 = 300;
        //请求失败，打印Message
        static final int CODE_330 = 330;
        //服务器内部异常
        static final int CODE_500 = 500;
        //参数为空
        static final int CODE_503 = 503;
        //没有数据
        static final int CODE_502 = 502;
        //无效的Token
        //static final int CODE_510 = 510;
        //未登录
        //static final int CODE_530 = 530;
        //请求的操作异常终止：未知的页面类型
        //static final int CODE_551 = 551;

        static final int CODE_10003 = 10003; // TOO_MANY_REQ = '请求太频繁',
        static final int CODE_10039 = 10039; // SAME_REQ = '重复提交',
        // 登出状态,销毁当前 token
        static final int CODE_20101 = 20101;
        static final int CODE_20102 = 20102;
        static final int CODE_20103 = 20103;
        static final int CODE_20106 = 20106; // KICKED = '账号已在其他地方登录，请重新登录',
        static final int CODE_20107 = 20107; // 长时间未操作，请重新登录
        static final int CODE_20111 = 20111;
        public static final int CODE_20208 = 20208; // 异地登录(本次登录并非常用设备或地区， 需要进行安全验证)
        public static final int CODE_30018 = 30018; // 谷歌验证
        static final int CODE_30003 = 30003;
        static final int CODE_30004 = 30004; // 被踢下线, 禁止登录
        static final int CODE_30713 = 30713;
        static final int CODE_20203 = 20203; //用户名或密码错误
        static final int CODE_20217 = 20217; //已修改密码或被踢出
        public static final int CODE_20204 = 20204;//需要用户获取登录验证码
        public static final int CODE_20205 = 20205;
        public static final int CODE_20206 = 20206;
        public static final int CODE_900001 = 900001; // 全局验证
    }

}
