package com.xtree.base.net;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.router.RouterActivityPath;

import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public abstract class HttpCallBack<T> extends DisposableSubscriber<T> {
    public abstract void onResult(T t);

    @Override
    public void onNext(T o) {

        BaseResponse baseResponse = (BaseResponse) o;
        BusinessException ex = new BusinessException(baseResponse.getStatus(), baseResponse.getMessage());
        int status = baseResponse.getStatus() == -1 ? baseResponse.getCode() : baseResponse.getStatus();
        switch (status) {
            case HttpCallBack.CodeRule.CODE_0:
            case HttpCallBack.CodeRule.CODE_10000:
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
            case HttpCallBack.CodeRule.CODE_510:
                //无效的Token，提示跳入登录页
                ToastUtils.showShort("token已过期，请重新登录");
                //关闭所有页面
                AppManager.getAppManager().finishAllActivity();
                //跳入登录界面
                //*****该类仅供参考，实际业务Code, 根据需求来定义，******//
                break;
            case HttpCallBack.CodeRule.CODE_530:
                ToastUtils.showShort("请先登录");
                break;
            case HttpCallBack.CodeRule.CODE_551:
            case HttpCallBack.CodeRule.CODE_20203:
                ToastUtils.showShort(baseResponse.getMessage());
                break;
            case HttpCallBack.CodeRule.CODE_20101:
            case HttpCallBack.CodeRule.CODE_20102:
            case HttpCallBack.CodeRule.CODE_20103:
            case HttpCallBack.CodeRule.CODE_20111:
            case HttpCallBack.CodeRule.CODE_30018:
            case HttpCallBack.CodeRule.CODE_30003:
            case HttpCallBack.CodeRule.CODE_30713:
                KLog.e("登出状态,销毁token. " + baseResponse);
                SPUtils.getInstance().put("user_token", "");
                SPUtils.getInstance().put("user_sessid", "");
                //ToastUtils.showShort("请重新登录");
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                break;
            case HttpCallBack.FBCodeRule.PB_CODE_14010:
                //账号已登出，请重新登录
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                onError(ex);
                break;
            default:
                KLog.e("status is not normal: " + baseResponse);
                ToastUtils.showShort(baseResponse.getMessage());
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        KLog.e("error: " + t.toString());
        //t.printStackTrace();
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            ToastUtils.showShort(rError.message);
            return;
        }else if(t instanceof BusinessException){
            BusinessException rError = (BusinessException) t;
            ToastUtils.showShort(rError.message);
            return;
        }
        //其他全部甩锅网络异常
        ToastUtils.showShort("网络异常");
    }

    @Override
    public void onComplete() {

    }

    public static final class CodeRule {
        //请求成功, 正确的操作方式
        static final int CODE_0 = 0;
        static final int CODE_10000 = 10000;
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
        static final int CODE_510 = 510;
        //未登录
        static final int CODE_530 = 530;
        //请求的操作异常终止：未知的页面类型
        static final int CODE_551 = 551;
        // 登出状态,销毁当前 token
        static final int CODE_20101 = 20101;
        static final int CODE_20102 = 20102;
        static final int CODE_20103 = 20103;
        static final int CODE_20111 = 20111;
        static final int CODE_30018 = 30018;
        static final int CODE_30003 = 30003;
        static final int CODE_30713 = 30713;
        //用户名或密码错误
        static final int CODE_20203 = 20203;
    }

    public static final class FBCodeRule {
        static final int PB_CODE_14010 = 14010;
    }
}
