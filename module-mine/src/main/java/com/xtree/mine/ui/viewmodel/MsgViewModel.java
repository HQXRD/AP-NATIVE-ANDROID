package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.MsgInfoVo;
import com.xtree.mine.vo.MsgListVo;
import com.xtree.mine.vo.MsgPersonInfoVo;
import com.xtree.mine.vo.MsgPersonListVo;
import com.xtree.mine.vo.MsgPersonVo;
import com.xtree.mine.vo.MsgVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class MsgViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<List<MsgVo>> liveDataMsg = new MutableLiveData<>();
    public MutableLiveData<Integer> liveDataMsgCount = new MutableLiveData<>();
    public MutableLiveData<List<MsgPersonVo>> liveDataMsgPerson = new MutableLiveData<>();
    public MutableLiveData<Integer> liveDataMsgPersonCount = new MutableLiveData<>();
    public MutableLiveData<MsgInfoVo> liveDataMsgInfo = new MutableLiveData<>();
    public MutableLiveData<MsgPersonInfoVo> liveDataMsgPersonInfo = new MutableLiveData<>();
    public MutableLiveData<Boolean> liveDataDeletePart = new MutableLiveData<>();
    public MutableLiveData<Boolean> liveDataDeleteAll = new MutableLiveData<>();
    Gson gson = new Gson();

    public MsgViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getMessageList(String page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("per_page", "10");
        map.put("sort", "-istop,-sendtime");

        Disposable disposable = (Disposable) model.getApiService().getMessageList(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgListVo>() {
                    @Override
                    public void onResult(MsgListVo vo) {
                        List<MsgVo> list = new ArrayList<>();
                        liveDataMsgCount.setValue(vo.count);
                        if (vo.list instanceof List) {
                            if (((List<?>) vo.list).size() > 0) {
                                String json = gson.toJson(vo.list);
                                list = Arrays.asList(gson.fromJson(json, MsgVo[].class));
                                CfLog.d(json);
                                if (page.equals("1")) {
                                    SPUtils.getInstance().put(SPKeyGlobal.MSG_INFO, json);
                                }
                                liveDataMsg.setValue(list);
                            } else {
                                SPUtils.getInstance().put(SPKeyGlobal.MSG_INFO, "");
                                liveDataMsg.setValue(list);
                            }
                        }
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

    public void getMessage(String id) {
        Disposable disposable = (Disposable) model.getApiService().getMessage(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgInfoVo>() {
                    @Override
                    public void onResult(MsgInfoVo vo) {
                        CfLog.i(vo.toString());
                        liveDataMsgInfo.setValue(vo);
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

    public void getMessagePersonList(String page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page);

        Disposable disposable = (Disposable) model.getApiService().getMessagePersonList(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgPersonListVo>() {
                    @Override
                    public void onResult(MsgPersonListVo vo) {
                        List<MsgPersonVo> list = new ArrayList<>();
                        liveDataMsgPersonCount.setValue(vo.count);
                        if (vo.list instanceof List) {
                            if (((List<?>) vo.list).size() > 0) {
                                String json = gson.toJson(vo.list);
                                list = Arrays.asList(gson.fromJson(json, MsgPersonVo[].class));
                                CfLog.d(json);
                                if (page.equals("1")) {
                                    SPUtils.getInstance().put(SPKeyGlobal.MSG_PERSON_INFO, json);
                                }
                                liveDataMsgPerson.setValue(list);
                            } else {
                                SPUtils.getInstance().put(SPKeyGlobal.MSG_PERSON_INFO, "");
                                liveDataMsgPerson.setValue(list);
                            }
                        }
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

    public void getMessagePerson(String id) {
        Disposable disposable = (Disposable) model.getApiService().getMessagePerson(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgPersonInfoVo>() {
                    @Override
                    public void onResult(MsgPersonInfoVo vo) {
                        CfLog.i(vo.toString());
                        liveDataMsgPersonInfo.setValue(vo);
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

    public void deletePartPersonInfo(List<String> info) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("checkboxes", info);
        map.put("nonce", UuidUtil.getID16());

        Disposable disposable = (Disposable) model.getApiService().deletePartPersonInfo(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse2>() {
                    @Override
                    public void onResult(BaseResponse2 vo) {
                        ToastUtils.showLong(vo.message);
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            return;
                        }
                        liveDataDeletePart.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("删除失败");
                        liveDataDeletePart.setValue(false);
                    }
                });
        addSubscribe(disposable);
    }

    public void deleteAllPersonInfo() {
        Disposable disposable = (Disposable) model.getApiService().deleteAllPersonInfo()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BaseResponse2>() {
                    @Override
                    public void onResult(BaseResponse2 vo) {
                        ToastUtils.showLong(vo.message);
                        if (vo.msg_type == 1 || vo.msg_type == 2) {
                            return;
                        }
                        liveDataDeleteAll.setValue(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        ToastUtils.showLong("删除失败");
                        liveDataDeleteAll.setValue(false);
                    }
                });
        addSubscribe(disposable);
    }

    public void readCache() {
        CfLog.i("******");
        List<MsgVo> msgListVo = new ArrayList<>();
        List<MsgPersonVo> msgPersonInfoListVo = new ArrayList<>();
        String msgJson = SPUtils.getInstance().getString(SPKeyGlobal.MSG_INFO);
        String msgPersonJson = SPUtils.getInstance().getString(SPKeyGlobal.MSG_PERSON_INFO);

        if (!msgJson.equals("")) {
            CfLog.e(msgJson);
            msgListVo = Arrays.asList(new Gson().fromJson(msgJson, MsgVo[].class));
        }
        if (!msgPersonJson.equals("")) {
            CfLog.e(msgPersonJson);
            msgPersonInfoListVo = Arrays.asList(new Gson().fromJson(msgPersonJson, MsgPersonVo[].class));
        }
        liveDataMsg.setValue(msgListVo);
        liveDataMsgPerson.setValue(msgPersonInfoListVo);
    }
}