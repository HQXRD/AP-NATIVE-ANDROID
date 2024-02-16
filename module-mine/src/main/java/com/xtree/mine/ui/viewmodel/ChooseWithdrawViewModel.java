package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.BankCardCashVo;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.PlatWithdrawConfirmVo;
import com.xtree.mine.vo.PlatWithdrawVo;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTConfirmVo;
import com.xtree.mine.vo.USDTSecurityVo;
import com.xtree.mine.vo.VirtualCashVo;
import com.xtree.mine.vo.VirtualConfirmVo;
import com.xtree.mine.vo.VirtualSecurityVo;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * 选择支付银行卡ViewModel
 */
public class ChooseWithdrawViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<ChooseInfoVo> chooseInfoVoMutableLiveData = new MutableLiveData<>();//选择提款方式
    public MutableLiveData<BankCardCashVo> channelDetailVoMutableLiveData = new MutableLiveData<>();//提款页面数据详情
    public MutableLiveData<PlatWithdrawVo> platwithdrawVoMutableLiveData = new MutableLiveData<>();//提款提交
    public MutableLiveData<PlatWithdrawConfirmVo> platWithdrawConfirmVoMutableLiveData = new MutableLiveData<PlatWithdrawConfirmVo>();//银行卡提现确认

    public MutableLiveData<USDTCashVo> usdtCashVoMutableLiveData = new MutableLiveData<>();//USDT提款获取页面信息
    public MutableLiveData<USDTSecurityVo> usdtSecurityVoMutableLiveData = new MutableLiveData<>();//USDT 确认提款信息
    public MutableLiveData<USDTConfirmVo> usdtConfirmVoMutableLiveData = new MutableLiveData<>();//USDT完成提款申请

    public MutableLiveData<VirtualCashVo> virtualCashVoMutableLiveData = new MutableLiveData<>();//虚拟币提款获取页面信息
    public MutableLiveData<VirtualSecurityVo> virtualSecurityVoMutableLiveData = new MutableLiveData<>();//虚拟币 确认提款信息
    public MutableLiveData<VirtualConfirmVo> virtualConfirmVoMutableLiveData = new MutableLiveData<>();//虚拟币完成提款申请

    public MutableLiveData<AwardsRecordVo> awardrecordVoMutableLiveData = new MutableLiveData<>();//流水

    public ChooseWithdrawViewModel(@NonNull Application application) {
        super(application);
    }

    public ChooseWithdrawViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    /**
     * 获取提款方式
     */
    public void getChooseWithdrawInfo() {
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawInfo()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ChooseInfoVo>() {

                    @Override
                    public void onResult(ChooseInfoVo chooseInfoVo) {
                        CfLog.i("ChooseInfoVo = " + chooseInfoVo.toString());
                        if (chooseInfoVo.wdChannelList != null) {
                            for (int i = 0; i < chooseInfoVo.wdChannelList.size(); i++) {
                                if (chooseInfoVo.wdChannelList.get(i).configkey.contains("usdt")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gcnyt_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.usdtchanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_usdt) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;

                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("bank") ||
                                        chooseInfoVo.wdChannelList.get(i).configkey.contains("hipaytx") ||
                                        chooseInfoVo.wdChannelList.get(i).configkey.contains("generalchannel") ||
                                        chooseInfoVo.wdChannelList.get(i).configkey.contains("银行卡")) {
                                    //对应银行卡提款字段匹配
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.bankchanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_card_type);
                                    if (chooseInfoVo.bankcardstatus_rmb) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("ebpay")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.ebpaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_ebpay_type);
                                    if (chooseInfoVo.bankcardstatus_ebpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("topay")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.topaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_topay_type);
                                    if (chooseInfoVo.bankcardstatus_topay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("hiwallet")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.hiwalletchanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gcnyt_type);
                                    if (chooseInfoVo.bankcardstatus_hiwallet) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("gopay")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.gopaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gopay_type);
                                    if (chooseInfoVo.bankcardstatus_gopay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("mpay")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_mpay_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.mpaychanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_mpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("gobao")
                                ) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gobao_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.gobaochanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_gobao) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("okpay")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_okpay_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.okpaychanneluse_msg;
                                    CfLog.i("okpaychanneluse_msg = " + chooseInfoVo.okpaychanneluse_msg + " | = channeluseMessage =  " + chooseInfoVo.wdChannelList.get(i).channeluseMessage);
                                    if (chooseInfoVo.bankcardstatus_okpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                    } else
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                }

                            }
                        } else {

                        }

                        chooseInfoVoMutableLiveData.setValue(chooseInfoVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取银行卡提款页面总体信息
     */
    public void getChooseWithdrawBankDetailInfo(String usdtType) {
        HashMap<String, String> map = new HashMap<>();
        map.put("usdt_type", usdtType);
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawBankDetailsInfo()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BankCardCashVo>() {
                    @Override
                    public void onResult(BankCardCashVo bankCardCashVo) {
                        CfLog.i("BankCardCashVo = " + bankCardCashVo.toString());
                        for (int i = 0; i < bankCardCashVo.channel_list.size(); i++) {
                            BankCardCashVo.ChannelVo cv = bankCardCashVo.channel_list.get(i);
                            if (!(cv.fixamount_list instanceof String)) {
                                //针对多金额选项数据封装
                                for (int j = 0; j < ((ArrayList) cv.fixamount_list).size(); j++) {
                                    bankCardCashVo.channel_list.get(i).fixamountList.add(((ArrayList) cv.fixamount_list).get(j).toString());
                                }
                            } else {

                            }
                            if (bankCardCashVo.channel_list.get(i).thiriframe_status == 1) {
                                bankCardCashVo.channel_list.get(i).isWebView = 1;//需要展示webView页面
                            } else {
                                bankCardCashVo.channel_list.get(i).isWebView = 2;//需要展示webView页面
                            }
                            //isShowErrorView
                          /*  if (TextUtils.isEmpty(bankCardCashVo.channel_list.get(i).thiriframe_msg))
                            {
                                bankCardCashVo.channel_list.get(i).isShowErrorView = 1;
                            }
                            else
                            {
                                bankCardCashVo.channel_list.get(i).isShowErrorView = 0;
                            }
*/
                        }

                        channelDetailVoMutableLiveData.setValue(bankCardCashVo);
                    }
                });
        addSubscribe(disposable);

    }

    /**
     * 提款提交
     */
    public void getPlatWithdraw(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postPlatWithdrawBank(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PlatWithdrawVo>() {
                    @Override
                    public void onResult(PlatWithdrawVo platwithdrawVo) {
                        CfLog.i("PlatWithdrawVo = " + platwithdrawVo.toString());
                        platwithdrawVoMutableLiveData.setValue(platwithdrawVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 确认提交
     */
    public void postConfirmWithdraw(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postConfirmWithdrawBank(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PlatWithdrawConfirmVo>() {
                    @Override
                    public void onResult(PlatWithdrawConfirmVo platWithdrawConfirmVo) {
                        platWithdrawConfirmVoMutableLiveData.setValue(platWithdrawConfirmVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取USDT提款页面信息
     */
    public void getChooseWithdrawUSDT(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawUSDT(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTCashVo>() {
                    @Override
                    public void onResult(USDTCashVo usdtCashVo) {
                        usdtCashVoMutableLiveData.setValue(usdtCashVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取USDT提款 确认提款信息
     */
    public void postPlatWithdrawUSDT(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postPlatWithdrawUSDT(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTSecurityVo>() {
                    @Override
                    public void onResult(USDTSecurityVo usdtSecurityVo) {
                        usdtSecurityVoMutableLiveData.setValue(usdtSecurityVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * USDT提款完成申请
     */
    public void postConfirmWithdrawUSDT(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postConfirmWithdrawUSDT(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTConfirmVo>() {
                    @Override
                    public void onResult(USDTConfirmVo usdtConfirmVo) {
                        usdtConfirmVoMutableLiveData.setValue(usdtConfirmVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取虚拟币提款页面信息
     */
    public void getChooseWithdrawVirtual(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawVirtual(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualCashVo>() {
                    @Override
                    public void onResult(VirtualCashVo virtualCashVo) {
                        virtualCashVoMutableLiveData.setValue(virtualCashVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取虚拟币提款 确认提款信息
     */
    public void postPlatWithdrawVirtual(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postPlatWithdrawVirtual(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualSecurityVo>() {
                    @Override
                    public void onResult(VirtualSecurityVo virtualSecurityVo) {
                        virtualSecurityVoMutableLiveData.setValue(virtualSecurityVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 虚拟币提款完成申请
     */
    public void postConfirmWithdrawVirtual(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postConfirmWithdrawVirtual(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualConfirmVo>() {
                    @Override
                    public void onResult(VirtualConfirmVo virtualConfirmVo) {
                        virtualConfirmVoMutableLiveData.setValue(virtualConfirmVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取流水
     */
    public void getAwardRecord() {
        Disposable disposable = (Disposable) model.getApiService().getAwardRecord()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<AwardsRecordVo>() {
                    @Override
                    public void onResult(AwardsRecordVo awardrecordVo) {
                        if (awardrecordVo != null) {
                            awardrecordVoMutableLiveData.setValue(awardrecordVo);
                        } else {
                            CfLog.i("awardrecordVo IS NULL ");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        //ToastUtils.showLong("请求失败");
                    }
                });
        addSubscribe(disposable);
    }

}
