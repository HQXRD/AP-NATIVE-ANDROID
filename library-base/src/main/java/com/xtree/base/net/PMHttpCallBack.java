package com.xtree.base.net;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;

import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.PMBaseResponse;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
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
        BusinessException ex = new BusinessException(baseResponse.getCode(), baseResponse.getMsg());
        int code = baseResponse.getCode();
        switch (code) {
            case PMHttpCallBack.CodeRule.CODE_0:

                //请求成功, 正确的操作方式
                onResult((T) baseResponse.getData());
                break;
            case PMHttpCallBack.CodeRule.CODE_401013:
            case PMHttpCallBack.CodeRule.CODE_401026:
                //账号已登出，请重新登录
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                onError(ex);
                break;
            case PMHttpCallBack.CodeRule.CODE_400467:
                ResponseThrowable rError = new ResponseThrowable(PMHttpCallBack.CodeRule.CODE_400467, baseResponse.getMsg());
                onError(rError);
                break;
            default:
                ToastUtils.showShort(baseResponse.getMsg());
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
        } else if (t instanceof BusinessException) {
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
        public static final int CODE_401038 = 401038;
        public static final int CODE_401026 = 401026;
        public static final int CODE_401013 = 401013;
        public static final int CODE_400467 = 400467;



        //请求成功, 正确的操作方式
        static final int CODE_0 = 0;
        static final int CODE_10000 = 10000;
        static final int CODE_0000000 = 0000000;
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
}
