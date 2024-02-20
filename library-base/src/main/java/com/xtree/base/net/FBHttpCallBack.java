package com.xtree.base.net;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.widget.LoadingDialog;

import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public abstract class FBHttpCallBack<T> extends DisposableSubscriber<T> {
    public abstract void onResult(T t);

    @Override
    public void onNext(T o) {

        if (!(o instanceof BaseResponse)) {
            KLog.w("json is not normal");
            onResult(o);
            return;
        }
        BaseResponse baseResponse = (BaseResponse) o;
        ResponseThrowable ex = new ResponseThrowable(baseResponse.getStatus(), baseResponse.getMessage());
        int status = baseResponse.getStatus() == -1 ? baseResponse.getCode() : baseResponse.getStatus();
        switch (status) {
            case FBHttpCallBack.CodeRule.CODE_0:
            case FBHttpCallBack.CodeRule.CODE_10000:
                if (baseResponse.getAuthorization() != null) {
                    SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, baseResponse.getAuthorization().token);
                    SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, baseResponse.getAuthorization().token_type);
                }
                //请求成功, 正确的操作方式
                onResult((T) baseResponse.getData());
                break;
            case FBHttpCallBack.CodeRule.CODE_14010:
                //TOKEN失效
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

    public static final class CodeRule {
        //请求成功, 正确的操作方式
        static final int CODE_0 = 0;
        static final int CODE_10000 = 10000;
        public static final int CODE_14010 = 14010;
    }

}
