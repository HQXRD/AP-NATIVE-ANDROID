package com.xtree.home.ui.viewmodel;

import static com.xtree.base.utils.EventConstant.EVENT_RED_POINT;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.AppUpdateVo;
import com.xtree.base.vo.EventVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.MsgPersonListVo;
import com.xtree.base.vo.PMService;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.home.R;
import com.xtree.home.data.HomeRepository;
import com.xtree.home.vo.AugVo;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.CookieVo;
import com.xtree.home.vo.DataVo;
import com.xtree.home.vo.EleVo;
import com.xtree.home.vo.GameStatusVo;
import com.xtree.home.vo.GameVo;
import com.xtree.home.vo.NoticeVo;
import com.xtree.home.vo.PaymentDataVo;
import com.xtree.home.vo.PublicDialogVo;
import com.xtree.home.vo.RedPocketVo;
import com.xtree.home.vo.RewardRedVo;
import com.xtree.home.vo.SettingsVo;
import com.xtree.home.vo.VipInfoVo;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis on 2023/11/24.
 */

public class HomeViewModel extends BaseViewModel<HomeRepository> {

    public MutableLiveData<List<BannersVo>> liveDataBanner = new MutableLiveData<>();
    public MutableLiveData<List<BannersVo>> liveDataECLink = new MutableLiveData<>();
    public MutableLiveData<List<PublicDialogVo>> liveDataPublicLink = new MutableLiveData<>();
    public MutableLiveData<List<NoticeVo>> liveDataNotice = new MutableLiveData<>();
    //public MutableLiveData<List<GameStatusVo>> liveDataGameStatus = new MutableLiveData<>();
    public MutableLiveData<List<GameVo>> liveDataGames = new MutableLiveData<>();
    public MutableLiveData<Map> liveDataPlayUrl = new MutableLiveData<>();
    public MutableLiveData<String> liveDataFail41011 = new MutableLiveData<>();
    public MutableLiveData<CookieVo> liveDataCookie = new MutableLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();
    public MutableLiveData<VipInfoVo> liveDataVipInfo = new MutableLiveData<>();
    public MutableLiveData<Integer> liveDataMsgUnread = new MutableLiveData<>();
    public MutableLiveData<SettingsVo> liveDataSettings = new MutableLiveData<>();
    public MutableLiveData<HashMap<String, ArrayList<AugVo>>> liveDataAug = new MutableLiveData<>();
    public MutableLiveData<EleVo> liveDataEle = new MutableLiveData<>();
    public MutableLiveData<RedPocketVo> liveDataRedPocket = new MutableLiveData<>();
    public MutableLiveData<AppUpdateVo> liveDataUpdate = new MutableLiveData<>();//更新
    public MutableLiveData<GameVo> liveDataIntoAppGame = new MutableLiveData<>();//进入原生场馆

    HashMap<String, PaymentDataVo.RechargeVo> mapRechargeVo = new HashMap<>(); // 缓存的,充值渠道
    String public_key;

    public HomeViewModel(@NonNull Application application, HomeRepository repository) {
        super(application, repository);
    }

    public void getBanners() {
        Disposable disposable = (Disposable) model.getApiService().getBanners()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannersVo>>() {
                    @Override
                    public void onResult(List<BannersVo> list) {
                        CfLog.i("****** ");
                        if (list.isEmpty()) {
                            // 没有数据时,banner会占满手机屏幕/白屏;加2条数据显示默认图片
                            list.add(new BannersVo("default"));
                            list.add(new BannersVo("default"));
                        } else if (list.size() == 1) {
                            list.add(new BannersVo("default"));
                        }
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_BANNER_LIST, new Gson().toJson(list));
                        liveDataBanner.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getECLink() {
        Disposable disposable = (Disposable) model.getApiService().getECLink()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannersVo>>() {
                    @Override
                    public void onResult(List<BannersVo> list) {
                        liveDataECLink.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPublicLink() {
        Disposable disposable = (Disposable) model.getApiService().getPublicLink()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<PublicDialogVo>>() {
                    @Override
                    public void onResult(List<PublicDialogVo> list) {
                        CfLog.i("publicLink        " + list);
                        liveDataPublicLink.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getNotices() {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            CfLog.e("no token, not get notices");
            return;
        }

        Disposable disposable = (Disposable) model.getApiService().getNotices()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<DataVo<NoticeVo>>() {
                    @Override
                    public void onResult(DataVo<NoticeVo> data) {
                        CfLog.i("****** ");
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_NOTICE_LIST, new Gson().toJson(data.list));
                        liveDataNotice.setValue(data.list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, Maybe no token. " + t.toString());
                        //super.onError(t);
                        //ToastUtils.showLong("获取公告失败");
                        liveDataNotice.setValue(new ArrayList<>());
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameStatus(Context ctx) {
        Disposable disposable = (Disposable) model.getApiService().getGameStatus()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<GameStatusVo>>() {
                    @Override
                    public void onResult(List<GameStatusVo> list) {
                        //ToastUtils.showLong("请求成功");
                        CfLog.i("****** list.size: " + list.size()); // 37
                        //CfLog.i(list.get(0).toString()); // GameStatusVo{cid=1, name='PT娱乐', alias='pt', status=1}
                        String json = readFromRaw(ctx, R.raw.games);
                        List<GameVo> mList = new Gson().fromJson(json, new TypeToken<List<GameVo>>() {
                        }.getType());
                        CfLog.i("mList.size: " + mList.size());
                        mList = joinList(mList, list);
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_GAME_LIST, new Gson().toJson(mList));
                        liveDataGames.setValue(mList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getFBGameTokenApi(boolean isFirst) {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        CfLog.i("****** ");
                        SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FB_DISABLED, fbService.isDisabled);
                        SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
                        BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
                        BtDomainUtil.setFbDomainUrl(fbService.getDomains());
                        if (!isFirst) {
                            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                                    withString("KEY_PLATFORM", BtDomainUtil.PLATFORM_FB).navigation();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getFBXCGameTokenApi(boolean isFirst) {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getFBXCGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FBXC_DISABLED, fbService.isDisabled);
                        SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                        BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                        BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
                        if (!isFirst) {
                            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                                    withString("KEY_PLATFORM", BtDomainUtil.PLATFORM_FBXC).navigation();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMGameTokenApi(boolean isFirst) {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getPMGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_DISABLED, pmService.isDisabled);
                        SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                        BtDomainUtil.setDefaultPmDomainUrl(pmService.getApiDomain());
                        if (!isFirst) {
                            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                                    withString("KEY_PLATFORM", BtDomainUtil.PLATFORM_PM).navigation();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMXCGameTokenApi(boolean isFirst) {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getPMXCGameTokenApi()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PMXC_TOKEN, pmService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PMXC_DISABLED, pmService.isDisabled);
                        SPUtils.getInstance().put(SPKeyGlobal.PMXC_API_SERVICE_URL, pmService.getApiDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PMXC_IMG_SERVICE_URL, pmService.getImgDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PMXC_USER_ID, pmService.getUserId());
                        BtDomainUtil.setDefaultPmxcDomainUrl(pmService.getApiDomain());
                        if (!isFirst) {
                            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                                    withString("KEY_PLATFORM", BtDomainUtil.PLATFORM_PMXC).navigation();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    private List<GameVo> joinList(List<GameVo> a, List<GameStatusVo> b) {
        List<GameVo> mList = new ArrayList<>();
        for (GameVo vo : a) {
            for (GameStatusVo vo2 : b) {
                if (vo.cid == vo2.cid) {
                    // cid=3时,是"AG娱乐",包含 AG真人,AG电子,AG捕鱼,AG街机
                    if (vo.cid != 3) {
                        vo.name = vo2.name;
                    }
                    vo.alias = vo2.alias;
                    vo.status = vo2.status;
                    vo.maintenance_start = vo2.maintenance_start;
                    vo.maintenance_end = vo2.maintenance_end;
                    break;
                }
            }

            if (vo.cid == 0 || TextUtils.isEmpty(vo.playURL)) {
                // 原生的,或者需要请求接口的
                CfLog.w("******: " + vo);
            }
            if (vo.status == 2 || vo.cid == 17 || vo.cid == 33 ) {
                //17:CQ9娱乐 33：MG电子
                // 已下架,不要加到列表里面了
                CfLog.w("not show: " + vo);
                continue;
            }
            //CfLog.d(vo.toString());
            mList.add(vo);
        }

        CfLog.i("size: " + mList.size());
        return mList;
    }

    public void getPlayUrl(String gameAlias, String gameId, String name) {
        if (TextUtils.isEmpty(gameId)) {
            gameId = "1";
        }
        int autoThrad = SPUtils.getInstance().getInt(SPKeyGlobal.USER_AUTO_THRAD_STATUS);

        HashMap<String, String> map = new HashMap();
        map.put("autoThrad", autoThrad + "");
        map.put("h5judge", "1");
        map.put("id", gameId);

        Disposable disposable = (Disposable) model.getApiService().getPlayUrl(gameAlias, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, Object>>() {
                    @Override
                    public void onResult(Map<String, Object> vo) {
                        // "url": "https://user-h5-bw3.d91a21f.com?token=7c9c***039a"
                        // "url": { "launch_url": "https://cdn-ali.***.com/h5V01/h5.html?sn=dy12&xxx" }
                        //CfLog.i(vo.toString());
                        if (!vo.containsKey("url")) {
                            return;
                        }

                        vo.put("name", name);
                        Object obj = vo.get("url");
                        if (obj instanceof String) {
                            liveDataPlayUrl.setValue(vo);
                        } else if (obj instanceof Map) {
                            // BG真人 又包了一层
                            if (((Map<String, ?>) obj).containsKey("launch_url")) {
                                String launch_url = ((Map<String, String>) obj).get("launch_url");
                                vo.put("url", launch_url);
                                liveDataPlayUrl.setValue(vo);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }

                    @Override
                    public void onFail41011(BusinessException t) {
                        super.onFail41011(t);
                        liveDataFail41011.setValue(t.message);
                    }
                });
        addSubscribe(disposable);
    }

    public void getSettings() {
        HashMap<String, String> map = new HashMap();
        map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code,ws_check_interval,ws_retry_number,ws_retry_waiting_time,ws_expire_time");
        Disposable disposable = (Disposable) model.getApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SettingsVo>() {
                    @Override
                    public void onResult(SettingsVo vo) {
                        CfLog.i("****** ");
                        public_key = vo.public_key
                                .replace("\n", "")
                                .replace("\t", " ")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

                        SPUtils.getInstance().put(SPKeyGlobal.PUBLIC_KEY, public_key);
                        SPUtils.getInstance().put("customer_service_url", vo.customer_service_url);
                        SPUtils.getInstance().put(SPKeyGlobal.WS_CHECK_INTERVAL, vo.ws_check_interval);
                        SPUtils.getInstance().put(SPKeyGlobal.WS_RETRY_NUMBER, vo.ws_retry_number);
                        SPUtils.getInstance().put(SPKeyGlobal.WS_EXPIRE_TIME, vo.ws_expire_time);
                        SPUtils.getInstance().put(SPKeyGlobal.WS_RETRY_WAITING_TIME, vo.ws_retry_waiting_time);
                        //SPUtils.getInstance().put(SPKeyGlobal.PROMOTION_CODE, vo.promption_code);//推广code

                        liveDataSettings.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
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
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
    /**
     * 获取 个人信息
     */
    public void getProfile() {
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
                        liveDataProfile.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getMessagePersonList() {
        Disposable disposable = (Disposable) model.getApiService().getMessagePersonList()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgPersonListVo>() {
                    @Override
                    public void onResult(MsgPersonListVo vo) {
                        liveDataMsgUnread.setValue(vo.unread);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        liveDataMsgUnread.setValue(0);
                        super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        liveDataMsgUnread.setValue(0);
                        super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getVipInfo() {
        Disposable disposable = (Disposable) model.getApiService().getVipInfo()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VipInfoVo>() {
                    @Override
                    public void onResult(VipInfoVo vo) {
                        CfLog.i(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_VIP_INFO, new Gson().toJson(vo));
                        liveDataVipInfo.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getAugList(Context context) {
        LoadingDialog.show(context);
        Disposable disposable = (Disposable) model.getApiService().getAugList()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<AugVo>>() {
                    @Override
                    public void onResult(List<AugVo> list) {
                        CfLog.i(list.toString());
                        HashMap<String, ArrayList<AugVo>> augMap = new HashMap<>();
                        for (AugVo value : list) {
                            if (!augMap.containsKey(value.getOne_level())) {
                                augMap.put(value.getOne_level(), new ArrayList<>());
                            }
                            augMap.get(value.getOne_level()).add(value);
                        }
                        Gson gson = new Gson();
                        SPUtils.getInstance().put(SPKeyGlobal.AUG_LIST, gson.toJson(augMap));
                        liveDataAug.setValue(augMap);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getEle(int id, int page, int pageSize, String cateId, int isHot) {
        HashMap map = new HashMap<>();
        map.put("platform_id", id);
        map.put("page", page);
        map.put("per_page", pageSize);
        map.put("cate_id", cateId);
        map.put("is_hot", isHot);
        Disposable disposable = (Disposable) model.getApiService().getEle(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<EleVo>() {
                    @Override
                    public void onResult(EleVo vo) {
                        CfLog.i(vo.toString().toString());
                        liveDataEle.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getRedPocket() {
        Disposable disposable = (Disposable) model.getApiService().getRedPocket()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RedPocketVo>() {
                    @Override
                    public void onResult(RedPocketVo vo) {
                        CfLog.i(vo.toString());
                        liveDataRedPocket.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        //super.onError(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        CfLog.e("error, " + t.toString()); // 活动不存在
                        //super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getRewardRed() {
        Disposable disposable = (Disposable) model.getApiService().getReward()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RewardRedVo>() {
                    @Override
                    public void onResult(RewardRedVo vo) {
                        if (vo != null) {
                            CfLog.d(vo.toString());
                            CfLog.d("Send change red point");
                            EventBus.getDefault().post(new EventVo(EVENT_RED_POINT, vo.result));
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

    public void readCache() {
        CfLog.i("******");
        Gson gson = new Gson();
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_BANNER_LIST, "[]");
        List list = gson.fromJson(json, new TypeToken<List<BannersVo>>() {
        }.getType());
        if (list.isEmpty()) {
            // 没有数据时,banner会占满手机屏幕/白屏;加2条数据显示默认图片
            list.add(new BannersVo("default"));
            list.add(new BannersVo("default"));
        } else if (list.size() == 1) {
            list.add(new BannersVo("default"));
        }
        liveDataBanner.setValue(list);

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_NOTICE_LIST, "[]");
        List list2 = gson.fromJson(json, new TypeToken<List<NoticeVo>>() {
        }.getType());
        liveDataNotice.setValue(list2);

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_GAME_LIST, "[]");
        List list3 = gson.fromJson(json, new TypeToken<List<GameVo>>() {
        }.getType());
        liveDataGames.setValue(list3);

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = gson.fromJson(json, ProfileVo.class);
        if (vo != null) {
            liveDataProfile.setValue(vo);
        }

        json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_VIP_INFO);
        VipInfoVo vo2 = gson.fromJson(json, VipInfoVo.class);
        if (vo2 != null) {
            liveDataVipInfo.setValue(vo2);
        }

    }

    /**
     * App更新接口
     */
    public void getUpdate() {
        Disposable disposable = (Disposable) model.getApiService().getUpdate()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<AppUpdateVo>() {
                    @Override
                    public void onResult(AppUpdateVo updateVo) {
                        if (updateVo == null) {
                            CfLog.e("data is null");
                            return;
                        }
                        liveDataUpdate.setValue(updateVo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取 充值列表
     */
    public void getPaymentsTypeList() {
        Disposable disposable = (Disposable) model.getApiService().getPaymentsTypeList()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PaymentDataVo>() {
                    @Override
                    public void onResult(PaymentDataVo vo) {
                        CfLog.i(new Gson().toJson(vo));
                        //CfLog.d("chongzhiListCount: " + vo.chongzhiListCount);
                        if (vo == null || vo.chongzhiList == null || vo.chongzhiList.isEmpty()) {
                            return;
                        }

                        for (int i = 0; i < vo.chongzhiList.size(); i++) {
                            PaymentDataVo.PaymentTypeVo t = vo.chongzhiList.get(i);
                            if (t.payChannelList == null || t.payChannelList.isEmpty()) {
                                continue;
                            }

                            for (int j = 0; j < t.payChannelList.size(); j++) {
                                PaymentDataVo.RechargeVo vo2 = t.payChannelList.get(j);
                                if (vo2.user_bank_info != null) {
                                    if (vo2.user_bank_info instanceof Map) {
                                        Map<String, String> map = (Map<String, String>) vo2.user_bank_info;
                                        for (Map.Entry<String, String> entry : map.entrySet()) {
                                            PaymentDataVo.RechargeVo.BankCardVo vo3 = new PaymentDataVo.RechargeVo.BankCardVo(entry.getKey(), entry.getValue());
                                            vo2.userBankList.add(vo3);
                                        }
                                    }
                                }
                                if (vo2.op_thiriframe_use && !vo2.phone_needbind) {
                                    getPaymentDetail(vo2.bid); // 查详情
                                }
                            }
                        }
                        SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_TYPE_OBJ, new Gson().toJson(vo));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }
                });

        addSubscribe(disposable);
    }

    public void getPaymentDetail(String bid) {
        Disposable disposable = (Disposable) model.getApiService().getPayment(bid)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PaymentDataVo.RechargeVo>() {
                    @Override
                    public void onResult(PaymentDataVo.RechargeVo vo) {
                        CfLog.d(vo.toString());
                        if (vo.user_bank_info != null && vo.user_bank_info instanceof Map) {
                            Map<String, String> map = (Map<String, String>) vo.user_bank_info;
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                PaymentDataVo.RechargeVo.BankCardVo vo3 = new PaymentDataVo.RechargeVo.BankCardVo(entry.getKey(), entry.getValue());
                                vo.userBankList.add(vo3);
                            }
                        }

                        CfLog.d("****** op_thiriframe_url: " + vo.title + ", " + vo.op_thiriframe_url);
                        CfLog.d("****** " + new Gson().toJson(vo));
                        mapRechargeVo.put(vo.bid, vo);
                        SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                    }
                });

        addSubscribe(disposable);
    }

    private String readFromRaw(Context context, int rawRes) {
        InputStream is = context.getResources().openRawResource(rawRes);
        return readText(is);
    }

    private String readText(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);

        StringBuffer sb = new StringBuffer("");
        String str;
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
