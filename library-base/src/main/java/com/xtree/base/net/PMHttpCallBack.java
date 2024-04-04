package com.xtree.base.net;

import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.http.PMBaseResponse;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.ToastUtils;

public abstract class PMHttpCallBack<T> extends DisposableSubscriber<T> {
    public abstract void onResult(T t);

    @Override
    public void onNext(T o) {

        if (!(o instanceof PMBaseResponse)) {
            KLog.w("json is not normal");
            onResult(o);
            return;
        }
        PMBaseResponse baseResponse = (PMBaseResponse) o;
        ResponseThrowable ex = new ResponseThrowable(baseResponse.getCode(), baseResponse.getMsg());
        int code = baseResponse.getCode();
        switch (code) {
            case PMHttpCallBack.CodeRule.CODE_0:

                //请求成功, 正确的操作方式
                onResult((T) baseResponse.getData());
                break;
            case PMHttpCallBack.CodeRule.CODE_401013://账号已登出，请重新登录
            case PMHttpCallBack.CodeRule.CODE_401026://账号已登出，请重新登录
            case PMHttpCallBack.CodeRule.CODE_400467:
            case PMHttpCallBack.CodeRule.CODE_401038:
            case PMHttpCallBack.CodeRule.CODE_400524:
            case PMHttpCallBack.CodeRule.CODE_400525:
                onError(ex);
                break;
            default:
                ToastUtils.showShort(baseResponse.getMsg());
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
        /**
         * token失效
         */
        public static final int CODE_401026 = 401026;
        /**
         * token失效
         */
        public static final int CODE_401013 = 401013;
        public static final int CODE_400467 = 400467;
        public static final int CODE_401038 = 401038;
        /**
         * 提前结算提交申请成功,请等待确认
         */
        public static final int CODE_400524 = 400524;
        /**
         * 提前结算未通过
         */
        public static final int CODE_400525 = 400525;


    }
}
