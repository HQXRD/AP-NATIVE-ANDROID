package com.xtree.base.net;

import com.xtree.base.utils.AppUtil;

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
            case PMHttpCallBack.CodeRule.CODE_400527:
                onError(ex);
                break;
            case PMHttpCallBack.CodeRule.CODE_400489:
            case PMHttpCallBack.CodeRule.CODE_400492:
            case PMHttpCallBack.CodeRule.CODE_400496:
            case PMHttpCallBack.CodeRule.CODE_400503:
            case PMHttpCallBack.CodeRule.CODE_400522:
            case PMHttpCallBack.CodeRule.CODE_400528:
            case PMHttpCallBack.CodeRule.CODE_400529:

            case PMHttpCallBack.CodeRule.CODE_400493:
            case PMHttpCallBack.CodeRule.CODE_400494:
            case PMHttpCallBack.CodeRule.CODE_400500:
            case PMHttpCallBack.CodeRule.CODE_400501:
            case PMHttpCallBack.CodeRule.CODE_400525:
            case PMHttpCallBack.CodeRule.CODE_400531:
            case PMHttpCallBack.CodeRule.CODE_400537:
            case PMHttpCallBack.CodeRule.CODE_402038:
                ex.code = PMHttpCallBack.CodeRule.CODE_10000001;
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
            if (rError.code == 403) {
                AppUtil.goWeb403();
                return;
            }
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
         * 提前结算错误统一出口
         */
        public static final int CODE_10000001 = 10000001;
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
         * 提前结算功能暂不可用，请稍后再试
         */
        public static final int CODE_400527 = 400527;

        /**
         * 订单不存在 需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400489 = 400489;

        /**
         * 目前只支持足球提前结算 需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400492 = 400492;

        /**
         * 不符合提前结算条件，订单非待结算状态  需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400496 = 400496;

        /**
         * 用户不支持提前结算  需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400503 = 400503;

        /**
         * 目前只支持单关提前结算 需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400522 = 400522;

        /**
         * 投注项赛果已确认，不可提前结算 需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400528 = 400528;

        /**
         * 赛事已结束 需隐藏提前结算按钮，不支持的提前结算请求
         */
        public static final int CODE_400529 = 400529;

        /**
         * 提前结算金额不能超过注单金额
         */
        public static final int CODE_400493 = 400493;

        /**
         * 提前结算金额小数位超出限制
         */
        public static final int CODE_400494 = 400494;

        /**
         * 提交申请失败,请重试
         */
        public static final int CODE_400500 = 400500;

        /**
         * 最低提前结算金额为1
         */
        public static final int CODE_400501 = 400501;

        /**
         * 提前结算未通过
         */
        public static final int CODE_400525 = 400525;

        /**
         * 提交申请失败,提前结算时比分已变更
         */
        public static final int CODE_400531 = 400531;

        /**
         * 提交申请失败,提前结算金额已变更
         */
        public static final int CODE_400537 = 400537;

        /**
         * 提前结算异常
         */
        public static final int CODE_402038 = 402038;
    }
}
