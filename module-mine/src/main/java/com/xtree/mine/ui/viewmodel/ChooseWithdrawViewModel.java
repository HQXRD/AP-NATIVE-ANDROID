package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.HttpWithdrawalCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.BankCardCashVo;
import com.xtree.mine.vo.ChooseInfoMoYuVo;
import com.xtree.mine.vo.FundPassWordVerifyVo;
import com.xtree.mine.vo.FundPassWordVo;
import com.xtree.mine.vo.OtherWebWithdrawVo;
import com.xtree.mine.vo.PlatWithdrawConfirmVo;
import com.xtree.mine.vo.PlatWithdrawVo;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTConfirmVo;
import com.xtree.mine.vo.USDTSecurityVo;
import com.xtree.mine.vo.VirtualCashVo;
import com.xtree.mine.vo.VirtualConfirmVo;
import com.xtree.mine.vo.VirtualSecurityVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalBankInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalQuotaVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalSubmitVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalVerifyVo;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * 选择支付银行卡ViewModel
 */
public class ChooseWithdrawViewModel extends BaseViewModel<MineRepository> {
    public MutableLiveData<AwardsRecordVo> awardrecordVoMutableLiveData = new MutableLiveData<>();//流水

    public MutableLiveData<FundPassWordVo> fundPassWordVoMutableLiveData = new MutableLiveData<FundPassWordVo>();//魔域 输入资金密码
    public MutableLiveData<FundPassWordVerifyVo> fundPassWordVerifyVoMutableLiveData = new MutableLiveData<FundPassWordVerifyVo>();//魔域 输入资金密码/谷歌验证码
    public MutableLiveData<ChooseInfoMoYuVo> chooseInfoMoYuVoMutableLiveData = new MutableLiveData<>();//魔域 获取提款列表
    public MutableLiveData<BankCardCashVo> bankCardCashMoYuVoMutableLiveData = new MutableLiveData<>();//魔域 银行卡提款 信息展示bean
    public MutableLiveData<PlatWithdrawVo> platWithdrawMoYuVoMutableLiveData = new MutableLiveData<>();// 魔域 银行卡提款 提交
    public MutableLiveData<PlatWithdrawConfirmVo> platWithdrawConfirmMoYuVoMutableLiveData = new MutableLiveData<>();// 魔域 银行卡提款 确认

    public MutableLiveData<USDTCashVo> usdtCashMoYuVoMutableLiveData = new MutableLiveData<>();// 魔域 USDT提款获取页面信息
    public MutableLiveData<USDTSecurityVo> usdtSecurityMoYuVoMutableLiveData = new MutableLiveData<>();// 魔域 USDT 确认提款信息
    public MutableLiveData<USDTConfirmVo> usdtConfirmMoYuVoMutableLiveData = new MutableLiveData<>();// 魔域 USDT完成提款申请

    public MutableLiveData<VirtualCashVo> virtualCashMoYuVoMutableLiveData = new MutableLiveData<>();//魔域 虚拟币提款获取页面信息
    public MutableLiveData<VirtualSecurityVo> virtualSecurityMoYuVoMutableLiveData = new MutableLiveData<>();//魔域 虚拟币 确认提款信息
    public MutableLiveData<VirtualConfirmVo> virtualConfirmMuYuVoMutableLiveData = new MutableLiveData<>();//魔域  虚拟币完成提款申请

    public MutableLiveData<OtherWebWithdrawVo> otherWebWithdrawVoMutableLiveData = new MutableLiveData<>();//微信、支付宝提款

    //提款接入新接口
    //提款接入新接口
    public MutableLiveData<WithdrawalQuotaVo> quotaVoMutableLiveData = new MutableLiveData<>();//获取提款额度
    public MutableLiveData<String> quotaErrorData = new MutableLiveData<>();//获取提款提款额度错误
    public MutableLiveData<WithdrawalListVo> withdrawalListVoMutableLiveData = new MutableLiveData();//获取提现渠道

    public MutableLiveData<WithdrawalInfoVo> withdrawalInfoVoMutableLiveData = new MutableLiveData<>();// 获取提现渠道 错误信息
    public MutableLiveData<String> withdrawalListErrorData = new MutableLiveData<>();// 获取提现渠道 错误信息
    public MutableLiveData<WithdrawalBankInfoVo> withdrawalBankInfoVoMutableLiveData = new MutableLiveData<>();// 银行卡获取提款渠道详细信息
    public MutableLiveData<String> bankInfoVoErrorData = new MutableLiveData<>();// / 银行卡获取提款渠道详细信息 错误信息

    public MutableLiveData<WithdrawalVerifyVo> verifyVoMutableLiveData = new MutableLiveData<>();//验证当前渠道信息
    public MutableLiveData<String> verifyVoErrorData = new MutableLiveData<>();//验证当前渠道信息 错位信息
    public MutableLiveData<WithdrawalSubmitVo> submitVoMutableLiveData = new MutableLiveData<>();//提款提交

    public MutableLiveData<String> submitVoErrorData = new MutableLiveData<>();//提款提交

    public ChooseWithdrawViewModel(@NonNull Application application) {
        super(application);
    }

    public ChooseWithdrawViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    /**
     * 【魔域】获取提款方式
     */
    public void getChooseWithdrawInfo(final String checkCode) {
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawInfo(checkCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())

                .subscribeWith(new HttpCallBack<ChooseInfoMoYuVo>() {

                    @Override
                    public void onResult(ChooseInfoMoYuVo chooseInfoVo) {
                        if (!TextUtils.isEmpty(chooseInfoVo.ur_here) || chooseInfoVo.wdChannelList == null || chooseInfoVo.wdChannelList.isEmpty()) {
                            //异常状态
                            chooseInfoMoYuVoMutableLiveData.setValue(chooseInfoVo);
                        } else {
                            for (int i = 0; i < chooseInfoVo.wdChannelList.size(); i++) {
                                if (chooseInfoVo.wdChannelList.get(i).configkey.contains("usdt")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("USDT提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_usdt_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.usdtchanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_usdt) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }

                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("bank")
                                        || chooseInfoVo.wdChannelList.get(i).configkey.contains("hipaytx")
                                        || chooseInfoVo.wdChannelList.get(i).configkey.contains("hipayht")
                                        || chooseInfoVo.wdChannelList.get(i).configkey.contains("generalchannel")
                                        || chooseInfoVo.wdChannelList.get(i).configkey.contains("银行卡")
                                        || chooseInfoVo.wdChannelList.get(i).configkey.contains("onepayfast3")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("银行卡提款")) {

                                    //对应银行卡提款字段匹配
                                    CfLog.e(" ChooseInfoVo.ChannelInfo = " + chooseInfoVo.wdChannelList.get(i).toString());
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.bankchanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_card_type);
                                    if (chooseInfoVo.bankcardstatus_rmb) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("ebpay")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("EBPAY提款")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.ebpaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_ebpay_type);
                                    if (chooseInfoVo.bankcardstatus_ebpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("topay")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("TOPAY提款")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.topaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_topay_type);
                                    if (chooseInfoVo.bankcardstatus_topay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("hiwallet")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("CNYT提款")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.hiwalletchanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gcnyt_type);
                                    if (chooseInfoVo.bankcardstatus_hiwallet) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("gopay")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("GOPAY提款")) {
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.gopaychanneluse_msg;
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gopay_type);
                                    if (chooseInfoVo.bankcardstatus_gopay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("mpay")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("MPAY提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_mpay_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.mpaychanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_mpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("gobao")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("GOBAO提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_gobao_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.gobaochanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_gobao) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("okpay")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("OKPAY提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_okpay_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.okpaychanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_okpay) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("onepayzfb")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("极速支付宝提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_zfb_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.onepayzfbchanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_onepayzfb) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                } else if (chooseInfoVo.wdChannelList.get(i).configkey.contains("onepaywx")
                                        || chooseInfoVo.wdChannelList.get(i).title.contains("极速微信提款")) {
                                    chooseInfoVo.wdChannelList.get(i).bindType = getApplication().getString(R.string.txt_bind_wechat_type);
                                    chooseInfoVo.wdChannelList.get(i).channeluseMessage = chooseInfoVo.onepaywxchanneluse_msg;
                                    if (chooseInfoVo.bankcardstatus_onepaywx) {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 1;
                                        chooseInfoVo.wdChannelList.get(i).isBind = true;
                                    } else {
                                        chooseInfoVo.wdChannelList.get(i).channeluse = 0;
                                        chooseInfoVo.wdChannelList.get(i).isBind = false;
                                    }
                                }
                            }
                            chooseInfoMoYuVoMutableLiveData.setValue(chooseInfoVo);
                        }
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";
                        Throwable throwable = t;
                        String message = throwable.getMessage();
                        CfLog.e("onError message =  " + message);
                        ChooseInfoMoYuVo chooseInfoVo = new ChooseInfoMoYuVo();
                        //链接超时
                        chooseInfoVo.networkStatus = 1; //链接超时
                        chooseInfoMoYuVoMutableLiveData.setValue(chooseInfoVo);

                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        Throwable throwable = t;
                        String message = throwable.getMessage();
                        CfLog.e("onFail message =  " + message);
                        ChooseInfoMoYuVo chooseInfoVo = new ChooseInfoMoYuVo();
                        chooseInfoVo.networkStatus = 1; //链接超时
                        chooseInfoMoYuVoMutableLiveData.setValue(chooseInfoVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域] 获取银行卡提款页面总体信息
     */
    public void getChooseWithdrawBankDetailInfo(final String checkCode) {
        CfLog.e("getChooseWithdrawBankDetailInfo = " + checkCode);
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawBankDetailsInfo(checkCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BankCardCashVo>() {
                    @Override
                    public void onResult(BankCardCashVo bankCardCashVo) {
                        if (bankCardCashVo.channel_list.size() > 0) {
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

                            }
                            if (bankCardCashVo.user != null) {
                                if (bankCardCashVo.user.username != null) {
                                    bankCardCashVo.user.username = StringUtils.splitWithdrawUserName(bankCardCashVo.user.username);
                                } else if (bankCardCashVo.user.nickname != null) {
                                    bankCardCashVo.user.nickname = StringUtils.splitWithdrawUserName(bankCardCashVo.user.nickname);
                                }

                            }
                            bankCardCashMoYuVoMutableLiveData.setValue(bankCardCashVo);
                        } else if (!TextUtils.isEmpty(bankCardCashVo.ur_here) && bankCardCashVo.ur_here.equals("资金密码检查")) {
                            bankCardCashMoYuVoMutableLiveData.setValue(bankCardCashVo);
                        } else {
                            bankCardCashMoYuVoMutableLiveData.setValue(bankCardCashVo);
                        }

                    }
                });
        addSubscribe(disposable);

    }

    /**
     * 【魔域】银行卡 提款提交
     */
    public void getPlatWithdrawMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuPlatWithdrawBank(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PlatWithdrawVo>() {
                    @Override
                    public void onResult(PlatWithdrawVo platWithdrawVo) {
                        if (platWithdrawVo.user != null) {
                            if (platWithdrawVo.user.username != null) {
                                platWithdrawVo.user.username = StringUtils.splitWithdrawUserName(platWithdrawVo.user.username);
                            }
                        }

                        platWithdrawMoYuVoMutableLiveData.setValue(platWithdrawVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 确认提交[魔域]
     */
    public void postConfirmWithdrawMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuConfirmWithdrawBank(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PlatWithdrawConfirmVo>() {
                    @Override
                    public void onResult(PlatWithdrawConfirmVo platWithdrawConfirmMoYuVo) {
                        platWithdrawConfirmMoYuVoMutableLiveData.setValue(platWithdrawConfirmMoYuVo);
                    }

                });
        addSubscribe(disposable);
    }

    /*
     *【魔域】 获取USDT提款页面信息
     */
    public void getChooseWithdrawUSDTMoYu(String checkCode, String usdtType) {
        Disposable disposable = (Disposable) model.getApiService().getMoYuChooseWithdrawUSDT(checkCode, usdtType)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTCashVo>() {
                    @Override
                    public void onResult(USDTCashVo usdtCashVo) {
                        if (usdtCashVo.user != null) {
                            if (usdtCashVo.user.username != null) {
                                usdtCashVo.user.username = StringUtils.splitWithdrawUserName(usdtCashVo.user.username);
                            } else if (usdtCashVo.user.nickname != null) {
                                usdtCashVo.user.nickname = StringUtils.splitWithdrawUserName(usdtCashVo.user.nickname);
                            }
                        }

                        usdtCashMoYuVoMutableLiveData.setValue(usdtCashVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域]获取USDT提款 确认提款信息
     */
    public void postPlatWithdrawUSDTMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuPlatWithdrawUSDT(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTSecurityVo>() {
                    @Override
                    public void onResult(USDTSecurityVo usdtSecurityVo) {
                        if (usdtSecurityVo.user != null) {
                            if (usdtSecurityVo.user.username != null) {
                                usdtSecurityVo.user.username = StringUtils.splitWithdrawUserName(usdtSecurityVo.user.username);
                            } else if (usdtSecurityVo.user.nickname != null) {
                                usdtSecurityVo.user.nickname = StringUtils.splitWithdrawUserName(usdtSecurityVo.user.nickname);
                            }
                        }
                        usdtSecurityMoYuVoMutableLiveData.setValue(usdtSecurityVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域]USDT提款完成申请
     */
    public void postConfirmWithdrawUSDTMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuConfirmWithdrawUSDT(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<USDTConfirmVo>() {
                    @Override
                    public void onResult(USDTConfirmVo usdtConfirmVo) {
                        usdtConfirmMoYuVoMutableLiveData.setValue(usdtConfirmVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域] 获取虚拟币提款页面信息
     */
    public void getChooseWithdrawVirtualMoYu(final String checkCode, final String usdtType) {
        Disposable disposable = (Disposable) model.getApiService().getMoYuChooseWithdrawVirtual(checkCode, usdtType)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualCashVo>() {
                    @Override
                    public void onResult(VirtualCashVo virtualCashVo) {
                        if (virtualCashVo.user != null) {
                            if (virtualCashVo.user.username != null) {
                                virtualCashVo.user.username = StringUtils.splitWithdrawUserName(virtualCashVo.user.username);
                            } else if (virtualCashVo.user.nickname != null) {
                                virtualCashVo.user.nickname = StringUtils.splitWithdrawUserName(virtualCashVo.user.nickname);
                            }
                        }
                        virtualCashMoYuVoMutableLiveData.setValue(virtualCashVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域] 获取虚拟币提款 确认提款信息
     */
    public void postPlatWithdrawVirtualMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuPlatWithdrawVirtual(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualSecurityVo>() {
                    @Override
                    public void onResult(VirtualSecurityVo virtualSecurityVo) {
                        if (virtualSecurityVo.user != null) {
                            if (virtualSecurityVo.user.username != null) {
                                virtualSecurityVo.user.username = StringUtils.splitWithdrawUserName(virtualSecurityVo.user.username);
                            } else if (virtualSecurityVo.user.nickname != null) {
                                virtualSecurityVo.user.nickname = StringUtils.splitWithdrawUserName(virtualSecurityVo.user.nickname);
                            }
                        }
                        virtualSecurityMoYuVoMutableLiveData.setValue(virtualSecurityVo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * [魔域]虚拟币提款完成申请
     */
    public void postConfirmWithdrawVirtualMoYu(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().postMoYuConfirmWithdrawVirtual(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<VirtualConfirmVo>() {
                    @Override
                    public void onResult(VirtualConfirmVo virtualConfirmVo) {
                        virtualConfirmMuYuVoMutableLiveData.setValue(virtualConfirmVo);
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

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";
                        Throwable throwable = t;
                        String message = throwable.getMessage();
                        CfLog.e("onError message =  " + message);
                        AwardsRecordVo awardrecordVo = new AwardsRecordVo();
                        //链接超时
                        awardrecordVo.networkStatus = 1; //链接超时
                        awardrecordVoMutableLiveData.setValue(awardrecordVo);

                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        String message = t.getMessage();
                        CfLog.e("onError message =  " + message);
                        AwardsRecordVo awardrecordVo = new AwardsRecordVo();
                        //链接超时
                        awardrecordVo.networkStatus = 1; //链接超时
                        awardrecordVoMutableLiveData.setValue(awardrecordVo);
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 校验资金密码(魔域)
     */
    public void getCheckPass(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getCheckPass(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FundPassWordVo>() {
                    @Override
                    public void onResult(FundPassWordVo vo) {
                        fundPassWordVoMutableLiveData.setValue(vo);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 校验资金密码/谷歌验证(魔域)
     */
    public void getCheckPassAndVerify(HashMap<String, String> map) {
        Disposable disposable = (Disposable) model.getApiService().getCheckPassAndVerify(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FundPassWordVerifyVo>() {
                    @Override
                    public void onResult(FundPassWordVerifyVo vo) {
                        fundPassWordVerifyVoMutableLiveData.setValue(vo);
                    }
                });
        addSubscribe(disposable);
    }

    /*获取其他提款方式*/
    public void getWithdrawOther(final String check, final String type) {
        Disposable disposable = (Disposable) model.getApiService().getChooseWithdrawOther(check, type)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<OtherWebWithdrawVo>() {
                    @Override
                    public void onResult(OtherWebWithdrawVo vo) {
                        CfLog.e("getWithdrawOther =  " + vo.toString());
                        if (vo != null) {
                            otherWebWithdrawVoMutableLiveData.setValue(vo);
                        }
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";
                        Throwable throwable = t;
                        String message = throwable.getMessage();
                        CfLog.e("onError message =  " + message);
                        AwardsRecordVo awardrecordVo = new AwardsRecordVo();
                        //链接超时
                        awardrecordVo.networkStatus = 1; //链接超时
                        awardrecordVoMutableLiveData.setValue(awardrecordVo);

                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        String message = t.getMessage();
                        CfLog.e("onError message =  " + message);
                        AwardsRecordVo awardrecordVo = new AwardsRecordVo();
                        //链接超时
                        awardrecordVo.networkStatus = 1; //链接超时
                        awardrecordVoMutableLiveData.setValue(awardrecordVo);
                    }

                });
        addSubscribe(disposable);

    }

//接入提款新接口

    /**
     * 提款获取可用额度
     */
    public void getWithdrawQuota() {
        Disposable disposable = (Disposable) model.getApiService().getWithdrawalQuota()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalQuotaVo>() {
                    @Override
                    public void onResult(WithdrawalQuotaVo vo) {
                        quotaVoMutableLiveData.setValue(vo);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        BusinessException exception = (BusinessException) t;
                        String errorMessage = exception.message;
                        CfLog.e("onFail --->errorMessage=" + errorMessage + "|t.getMessage()=" + t.getMessage());
                        quotaErrorData.setValue(errorMessage);
                        CfLog.e("withdrawalInfoVoMutableLiveData onFail message =  " + t.toString());
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 获取可提现渠道列表
     */
    public void getWithdrawalList(final String checkCode) {
        Disposable disposable = (Disposable) model.getApiService().getWithdrawalList(checkCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalListVo>() {
                    @Override
                    public void onResult(WithdrawalListVo withdrawalListVos) {
                        withdrawalListVoMutableLiveData.setValue(withdrawalListVos);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";

                        CfLog.e("onError message =  " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        CfLog.e("onError message =  " + t.toString());
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 获取当前选中的提款详情
     */
    public void getWithdrawalInfo(final String wtype, final String check) {
        //wtype
        //	hipayht
        Disposable disposable = (Disposable) model.getApiService().getWithdrawalInfo(wtype, check)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalInfoVo>() {
                    @Override
                    public void onResult(WithdrawalInfoVo vo) {

                        withdrawalInfoVoMutableLiveData.setValue(vo);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";

                        BusinessException exception = (BusinessException) t;
                        String errorMessage = t.getMessage();
                        CfLog.e("onError --->exception.getMessage()=" + exception.getMessage() + "|t.getMessage()=" + t.getMessage());
                        withdrawalListErrorData.setValue(errorMessage);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        BusinessException exception = (BusinessException) t;
                        String errorMessage = exception.message;
                        CfLog.e("onFail --->errorMessage=" + errorMessage + "|t.getMessage()=" + t.getMessage());
                        withdrawalListErrorData.setValue(errorMessage);
                        CfLog.e("withdrawalInfoVoMutableLiveData onFail message =  " + t.toString());
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 获取银行卡渠道详情
     *
     * @param wtype
     * @param check
     */
    public void getWithdrawalBankInfo(final String wtype, final String check) {
        Disposable disposable = (Disposable) model.getApiService().getWithdrawalBankInfo(wtype, check)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalBankInfoVo>() {
                    @Override
                    public void onResult(WithdrawalBankInfoVo vo) {

                        //开启固额
                        if (vo.money_fixed) {
                            if (vo.money_options instanceof ArrayList) {
                                ArrayList list = (ArrayList) vo.money_options;
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i) instanceof String) {
                                        vo.fixamountList.add((String) list.get(i));
                                        WithdrawalBankInfoVo.WithdrawalAmountVo amountVo = new WithdrawalBankInfoVo.WithdrawalAmountVo();
                                        amountVo.amount = (String) list.get(i);
                                        amountVo.flag = false;

                                        vo.amountVoList.add(amountVo);
                                    }
                                }
                            }
                        } else {
                            CfLog.e(" *********** getWithdrawalBankInfo 暂未开启固额");
                        }
                        withdrawalBankInfoVoMutableLiveData.setValue(vo);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";

                        CfLog.e("onError message =  " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        BusinessException exception = (BusinessException) t;
                        String errorMessage = exception.message;
                        CfLog.e("onFail --->errorMessage=" + errorMessage + "|t.getMessage()=" + t.getMessage());
                        bankInfoVoErrorData.setValue(errorMessage);
                        CfLog.e("withdrawalInfoVoMutableLiveData onFail message =  " + t.toString());
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 验证当前渠道信息
     */
    public void postWithdrawalVerify(final HashMap<String, Object> map) {
        Disposable disposable = (Disposable) model.getApiService().postWithdrawalVerify(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalVerifyVo>() {
                    @Override
                    public void onResult(WithdrawalVerifyVo vo) {
                        CfLog.e("withdrawalInfoVoMutableLiveData  vo .getStatus = " + vo);
                        verifyVoMutableLiveData.setValue(vo);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";

                        CfLog.e("onError message =  " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        BusinessException exception = (BusinessException) t;
                        String errorMessage = exception.message;
                        CfLog.e("onFail --->errorMessage=" + errorMessage + "|t.getMessage()=" + t.getMessage());
                        verifyVoErrorData.setValue(errorMessage);
                    }

                });
        addSubscribe(disposable);
    }

    /**
     * 提款提交
     */
    public void postWithdrawalSubmit(final HashMap<String, Object> map) {
        Disposable disposable = (Disposable) model.getApiService().postWithdrawalSubmit(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpWithdrawalCallBack<WithdrawalSubmitVo>() {
                    @Override
                    public void onResult(WithdrawalSubmitVo vo) {
                        CfLog.e("withdrawalInfoVoMutableLiveData  vo .getStatus = " + vo);
                        submitVoMutableLiveData.setValue(vo);
                    }

                    //增加网络异常抓取
                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);  ex.message = "连接超时";

                        CfLog.e("onError message =  " + t.toString());
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        // super.onFail(t);
                        BusinessException exception = (BusinessException) t;
                        String errorMessage = exception.message;
                        CfLog.e("onFail --->errorMessage=" + errorMessage + "|t.getMessage()=" + t.getMessage());
                        submitVoErrorData.setValue(errorMessage);
                    }

                });
        addSubscribe(disposable);
    }

}
