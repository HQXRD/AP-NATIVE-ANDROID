package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AdduserVo;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.MarketingVo;
import com.xtree.mine.vo.MemberManagerVo;
import com.xtree.mine.vo.QuestionVo;
import com.xtree.mine.vo.SendMoneyVo;
import com.xtree.mine.vo.VipInfoVo;
import com.xtree.mine.vo.VipUpgradeInfoVo;
import com.xtree.mine.vo.request.AdduserRequest;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by marquis
 */
public class MineViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<Boolean> liveDataLogout = new SingleLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();
    public SingleLiveData<BalanceVo> liveDataBalance = new SingleLiveData<>(); // 中心钱包
    public SingleLiveData<Boolean> liveData1kRecycle = new SingleLiveData<>(); // 1键回收
    public SingleLiveData<VipUpgradeInfoVo> liveDataVipUpgrade = new SingleLiveData<>(); // Vip升级资讯
    public SingleLiveData<VipInfoVo> liveDataVipInfo = new SingleLiveData<>(); // Vip个人资讯
    public SingleLiveData<String> liveDataQuestionWeb = new SingleLiveData<>(); // 常见问题
    public SingleLiveData<MemberManagerVo> liveDataMemberManager = new SingleLiveData<>(); // 团队管理
    public SingleLiveData<String> liveDataCheckPassword = new SingleLiveData<>(); // 检查资金密码
    public SingleLiveData<SendMoneyVo> liveDataSendMoney = new SingleLiveData<>(); // 转账成功
    public SingleLiveData<MarketingVo> liveDataMarketing = new SingleLiveData<>();
    public SingleLiveData<MarketingVo> liveDataPostMark = new SingleLiveData<>();
    public SingleLiveData<AdduserVo> liveDataAdduser = new SingleLiveData<>();

    public MineViewModel(@NonNull Application application, MineRepository repository) {
        super(application, repository);
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
        liveDataLogout.setValue(true);
    }

    public void getProfile() {
        Disposable disposable = (Disposable) model.getApiService().getProfile()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfileVo>() {
                    @Override
                    public void onResult(ProfileVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, vo.auto_thrad_status);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_PROFILE, new Gson().toJson(vo));
                        SPUtils.getInstance().put(SPKeyGlobal.USER_ID, vo.userid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_NAME, vo.username);
                        liveDataProfile.setValue(vo);
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

    public void getBalance() {
        Disposable disposable = (Disposable) model.getApiService().getBalance()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_CENTRAL_BLC, vo.balance);
                        liveDataBalance.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void do1kAutoRecycle() {
        Disposable disposable = (Disposable) model.getApiService().do1kAutoRecycle()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d("******");
                        liveData1kRecycle.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveData1kRecycle.setValue(false);
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getVipUpgradeInfo() {
        Disposable disposable = (Disposable) model.getApiService().getVipUpgradeInfo()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VipUpgradeInfoVo>() {
                    @Override
                    public void onResult(VipUpgradeInfoVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.VIP_INFO, new Gson().toJson(vo));
                        liveDataVipUpgrade.setValue(vo);
                        String vipInfoJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO, "");
                        if (!vipInfoJson.isEmpty()) {
                            VipInfoVo vipInfoVo = new Gson().fromJson(vipInfoJson, VipInfoVo.class);
                            if (vipInfoVo != null) {
                                liveDataVipInfo.setValue(vipInfoVo);
                            }
                        }
                        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
                        if (!vipInfoJson.isEmpty()) {
                            ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
                            if (mProfileVo != null) {
                                liveDataProfile.setValue(mProfileVo);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getQuestionWeb() {
        Disposable disposable = (Disposable) model.getApiService().getQuestionWeb()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<QuestionVo>() {
                    @Override
                    public void onResult(QuestionVo vo) {
                        CfLog.d(vo.toString());
                        if (!TextUtils.isEmpty(vo.content)) {
                            SPUtils.getInstance().put(SPKeyGlobal.QUESTION_WEB, vo.content);
                            liveDataQuestionWeb.setValue(vo.content);
                        } else {
                            ToastUtils.showLong("请重新登录");
                            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                            finish(); // 关闭页面
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getMemberManager(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getMemberManager(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MemberManagerVo>() {
                    @Override
                    public void onResult(MemberManagerVo vo) {
                        CfLog.d(vo.toString());
                        liveDataMemberManager.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void checkMoneyPassword(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().checkMoneyPassword(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object vo) {
                        CfLog.d(vo.toString());
                        HashMap<String, Object> map = new HashMap<>((LinkedTreeMap) vo);
                        if (map.get("msg_type") != null) {
                            String msgType = map.get("msg_type").toString();
                            String message = map.get("message").toString();
                            if (!msgType.isEmpty()) {
                                onFail(new BusinessException(0, message)); // 先用0代替status
                                return;
                            }
                        }
                        HashMap<String, Object> checkCode = new HashMap<>((LinkedTreeMap) map.get("msg"));
                        liveDataCheckPassword.setValue(checkCode.get("checkcode").toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void sendMoney(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().sendMoney(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SendMoneyVo>() {
                    @Override
                    public void onResult(SendMoneyVo vo) {
                        CfLog.d(vo.toString());
                        liveDataSendMoney.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void marketing() {
        Disposable disposable = (Disposable) model.getApiService().marketing()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MarketingVo>() {
                    @Override
                    public void onResult(MarketingVo vo) {
                        CfLog.d(vo.toString());
                        liveDataMarketing.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void postMarketing(HashMap map, Context context) {
        LoadingDialog.show(context);
        Disposable disposable = (Disposable) model.getApiService().postMarketing(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MarketingVo>() {
                    @Override
                    public void onResult(MarketingVo vo) {
                        CfLog.d(vo.toString());
                        ToastUtils.showLong(vo.getSMsg());
                        liveDataPostMark.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void adduser(AdduserRequest request) {
        Disposable disposable = (Disposable) model.getApiService().adduser(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse2>() {
                    @Override
                    public void onResult(BaseResponse2 vo) {
                        CfLog.d(vo.toString());
                        ToastUtils.showLong(vo.message);
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            return;
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void readCache() {
        CfLog.i("******");
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = new Gson().fromJson(json, ProfileVo.class);
        if (vo != null) {
            liveDataProfile.setValue(vo);
        }

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO);
        VipInfoVo mVipInfoVo = new Gson().fromJson(json, VipInfoVo.class);
        liveDataVipInfo.setValue(mVipInfoVo);
    }

    public void readVipCache() {
        CfLog.i("******");
        String VipJson = SPUtils.getInstance().getString(SPKeyGlobal.VIP_INFO);
        VipUpgradeInfoVo vo = new Gson().fromJson(VipJson, VipUpgradeInfoVo.class);
        if (vo != null) {
            CfLog.d(vo.toString());
            SPUtils.getInstance().put(SPKeyGlobal.VIP_INFO, new Gson().toJson(vo));
            liveDataVipUpgrade.setValue(vo);
            String vipInfoJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO, "");
            if (!vipInfoJson.isEmpty()) {
                VipInfoVo vipInfoVo = new Gson().fromJson(vipInfoJson, VipInfoVo.class);
                if (vipInfoVo != null) {
                    liveDataVipInfo.setValue(vipInfoVo);
                }
            }
            String homeJson = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
            if (!vipInfoJson.isEmpty()) {
                ProfileVo mProfileVo = new Gson().fromJson(homeJson, ProfileVo.class);
                if (mProfileVo != null) {
                    liveDataProfile.setValue(mProfileVo);
                }
            }
        }
    }

    public void readQuestionWebCache() {
        CfLog.i("******");
        String json = SPUtils.getInstance().getString(SPKeyGlobal.QUESTION_WEB, "");
        if (!json.isEmpty()) {
            liveDataQuestionWeb.setValue(json);
        }
    }
}
