package me.xtree.mvvmhabit.http;

import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.ToastUtils;

public abstract class HttpCallBack<T> extends DisposableSubscriber<T> {
    public abstract void onResult(T t);
    @Override
    public void onNext(T o) {

        BaseResponse baseResponse = (BaseResponse) o;
        switch (baseResponse.getStatus()) {
            case ApiSubscriber.CodeRule.CODE_10000:
                //请求成功, 正确的操作方式
                onResult((T) baseResponse.getData());
                break;
            case ApiSubscriber.CodeRule.CODE_300:
                //请求失败，不打印Message
                KLog.e("请求失败");
                ToastUtils.showShort("错误代码:", baseResponse.getStatus());
                break;
            case ApiSubscriber.CodeRule.CODE_330:
                //请求失败，打印Message
                ToastUtils.showShort(baseResponse.getMessage());
                break;
            case ApiSubscriber.CodeRule.CODE_500:
                //服务器内部异常
                ToastUtils.showShort("错误代码:", baseResponse.getStatus());
                break;
            case ApiSubscriber.CodeRule.CODE_503:
                //参数为空
                KLog.e("参数为空");
                break;
            case ApiSubscriber.CodeRule.CODE_502:
                //没有数据
                KLog.e("没有数据");
                break;
            case ApiSubscriber.CodeRule.CODE_510:
                //无效的Token，提示跳入登录页
                ToastUtils.showShort("token已过期，请重新登录");
                //关闭所有页面
                AppManager.getAppManager().finishAllActivity();
                //跳入登录界面
                //*****该类仅供参考，实际业务Code, 根据需求来定义，******//
                break;
            case ApiSubscriber.CodeRule.CODE_530:
                ToastUtils.showShort("请先登录");
                break;
            case ApiSubscriber.CodeRule.CODE_551:
                ToastUtils.showShort("错误代码:", baseResponse.getStatus());
                break;

            case ApiSubscriber.CodeRule.CODE_20203:
                ToastUtils.showShort(baseResponse.getMessage());
                break;
            default:
                KLog.e("default: " + baseResponse);
                ToastUtils.showShort(baseResponse.getMessage());
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            ToastUtils.showShort(rError.message);
            return;
        }
        //其他全部甩锅网络异常
        ToastUtils.showShort("网络异常");
    }

    @Override
    public void onComplete() {

    }
}
