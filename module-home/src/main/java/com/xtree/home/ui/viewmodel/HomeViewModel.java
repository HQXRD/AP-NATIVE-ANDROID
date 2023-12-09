package com.xtree.home.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.vo.FBService;
import com.xtree.home.R;
import com.xtree.home.data.HomeRepository;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.CookieVo;
import com.xtree.home.vo.DataVo;
import com.xtree.home.vo.GameStatusVo;
import com.xtree.home.vo.GameVo;
import com.xtree.home.vo.LoginResultVo;
import com.xtree.home.vo.NoticeVo;
import com.xtree.home.vo.ProfileVo;
import com.xtree.home.vo.SettingsVo;
import com.xtree.home.vo.VipInfoVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.HttpCallBack;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by marquis on 2023/11/24.
 */

public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public MutableLiveData<List<BannersVo>> liveDataBanner = new MutableLiveData<>();
    public MutableLiveData<List<NoticeVo>> liveDataNotice = new MutableLiveData<>();
    //public MutableLiveData<List<GameStatusVo>> liveDataGameStatus = new MutableLiveData<>();
    public MutableLiveData<List<GameVo>> liveDataGames = new MutableLiveData<>();
    public MutableLiveData<Map> liveDataPlayUrl = new MutableLiveData<>();
    public MutableLiveData<String> liveDataUser = new MutableLiveData<>();
    public MutableLiveData<CookieVo> liveDataCookie = new MutableLiveData<>();
    public MutableLiveData<ProfileVo> liveDataProfile = new MutableLiveData<>();
    public MutableLiveData<VipInfoVo> liveDataVipInfo = new MutableLiveData<>();
    public MutableLiveData<SettingsVo> liveDataSettings = new MutableLiveData<>();
    public MutableLiveData<LoginResultVo> liveDataLoginResult = new MutableLiveData<>();

    String public_key;

    public HomeViewModel(@NonNull Application application, HomeRepository repository) {
        super(application, repository);
    }

    public void getBanners() {
        Disposable disposable = (Disposable) model.getApiService().getBanners()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannersVo>>() {
                    @Override
                    public void onResult(List<BannersVo> list) {
                        //ToastUtils.showLong("请求成功");
                        CfLog.i(list.get(0).toString());
                        SPUtils.getInstance().put(SPKeyGlobal.HOME_BANNER_LIST, new Gson().toJson(list));
                        liveDataBanner.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<DataVo<NoticeVo>>() {
                    @Override
                    public void onResult(DataVo<NoticeVo> data) {
                        CfLog.i(data.list.get(0).toString());
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<GameStatusVo>>() {
                    @Override
                    public void onResult(List<GameStatusVo> list) {
                        //ToastUtils.showLong("请求成功");
                        CfLog.i("list.size: " + list.size()); // 37
                        CfLog.i(list.get(0).toString()); // GameStatusVo{cid=1, name='PT娱乐', alias='pt', status=1}
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
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void getFBGameTokenApi(){
        String token = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMGameTokenApi(){
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, fbService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
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
                    vo.name = vo2.name;
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
            if (vo.status == 2) {
                // 已下架,不要加到列表里面了
                CfLog.w("not show: " + vo);
                continue;
            }
            CfLog.d(vo.toString());
            mList.add(vo);
        }

        CfLog.i("size: " + mList.size());
        return mList;
    }

    public void getPlayUrl(String gameAlias, String gameId) {
        if (TextUtils.isEmpty(gameId)) {
            gameId = "1";
        }
        int autoThrad = SPUtils.getInstance().getInt(SPKeyGlobal.USER_AUTO_THRAD_STATUS);

        HashMap<String, String> map = new HashMap();
        map.put("autoThrad", autoThrad + "");
        map.put("h5judge", "1");
        map.put("id", gameId);

        Disposable disposable = (Disposable) model.getApiService().getPlayUrl(gameAlias, map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> vo) {
                        // "url": "https://user-h5-bw3.d91a21f.com?token=7c9c***039a"
                        //CfLog.i(vo.toString());
                        liveDataPlayUrl.setValue(vo);
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

    public void getSettings() {
        HashMap<String, String> map = new HashMap();
        map.put("fields", "customer_service_url,public_key,barrage_api_url," +
                "x9_customer_service_url," + "promption_code,default_promption_code");
        Disposable disposable = (Disposable) model.getApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SettingsVo>() {
                    @Override
                    public void onResult(SettingsVo vo) {
                        //CfLog.i(vo.toString());
                        public_key = vo.public_key
                                .replace("\n", "")
                                .replace("\t", " ")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

                        SPUtils.getInstance().put(SPKeyGlobal.PUBLIC_KEY, public_key);
                        SPUtils.getInstance().put("customer_service_url", vo.customer_service_url);

                        liveDataSettings.setValue(vo);
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

    public void login(Context ctx, String userName, String pwd) {
        String password = MD5Util.generateMd5("") + MD5Util.generateMd5(pwd);
        //CfLog.d("password: " + password);
        String public_key = SPUtils.getInstance().getString("public_key", "");
        String loginpass = RSAEncrypt.encrypt2(pwd, public_key);
        //CfLog.d("loginpass: " + loginpass);

        if (TextUtils.isEmpty(loginpass)) {
            return;
        }

        HashMap<String, String> map = new HashMap();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "login");
        map.put("validcode", "");
        map.put("client_id", "10000005"); // h5:10000003, ios:10000004, android:10000005
        map.put("loginpass", loginpass);
        map.put("nonce", UUID.randomUUID().toString().replace("-", "")); //

        Disposable disposable = (Disposable) model.getApiService().login(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LoginResultVo>() {
                    @Override
                    public void onResult(LoginResultVo vo) {
                        CfLog.i(vo.toString());
                        ToastUtils.showLong("登录成功");
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN, vo.token);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_TOKEN_TYPE, vo.token_type);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_SESSID, vo.cookie.sessid);
                        SPUtils.getInstance().put(SPKeyGlobal.USER_SHARE_COOKIE_NAME, vo.cookie.cookie_name);

                        RetrofitClient.init();
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e(t.toString());
                        super.onError(t);
                        ToastUtils.showLong("登录失败");
                    }
                });
        addSubscribe(disposable);
    }

    public void getCookie() {
        Disposable disposable = (Disposable) model.getApiService().getCookie()
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        super.onError(t);
                        ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
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

    public void getVipInfo() {
        Disposable disposable = (Disposable) model.getApiService().getVipInfo()
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        super.onError(t);
                        //ToastUtils.showLong("请求失败");
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
