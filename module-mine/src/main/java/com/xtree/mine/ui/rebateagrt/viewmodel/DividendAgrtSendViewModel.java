package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.dialog.DividendTipDialog;
import com.xtree.mine.ui.rebateagrt.fragment.DividendAgrtSendDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtSendHeadModel;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtSendModel;
import com.xtree.mine.vo.request.DividendAgrtSendQuery;
import com.xtree.mine.vo.request.DividendAgrtSendRequest;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.response.DividendAgrtSendReeponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_dividendagrt_send);
                    add(R.layout.item_dividendagrt_send_head);
                }
            });
    private final MutableLiveData<String> titleData = new MutableLiveData<>(getApplication().getString(R.string.txt_manualdividendpayout_title));
    //合计数额
    public MutableLiveData<String> total = new MutableLiveData<>("0.00");
    //可用余额
    public MutableLiveData<String> availablebalance = new MutableLiveData<>("0.00");
    private GameDividendAgrtRequest gameDividendAgrtRequest;
    private WeakReference<FragmentActivity> mActivity = null;

    private final DividendAgrtSendHeadModel headModel = new DividendAgrtSendHeadModel();
    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        add(headModel);
    }};
    @SuppressLint("StaticFieldLeak")
    private BasePopupView pop;

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            if (itemViewType == R.layout.item_dividendagrt_send) {
                DividendAgrtSendModel bindModel = (DividendAgrtSendModel) bindModels.get(layoutPosition);
                bindModel.checked.set(Boolean.FALSE.equals(bindModel.checked.get()));
                totalMoneyCount();
            }
        }

    };
    /**
     * 列表加载
     */
    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getDividendData();
        }
    };

    public DividendAgrtSendViewModel(@NonNull Application application) {
        super(application);
    }

    public DividendAgrtSendViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    /**
     * 计算所有选中契约的金额总计
     */
    private void totalMoneyCount() {
        float totalFloat = 0f;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof DividendAgrtSendModel) {
                DividendAgrtSendModel m = (DividendAgrtSendModel) bindModel;
                if (Boolean.TRUE.equals(m.checked.get())) {
                    float f = Float.parseFloat(m.getSelfMoney());
                    totalFloat += f;
                }
            }
        }
        total.setValue(decimalFormat.format(totalFloat));
    }

    public void initData(GameDividendAgrtRequest response) {
        //init data
        gameDividendAgrtRequest = response;
        datas.setValue(bindModels);
        getDividendData();
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 手动分红发放
     */
    public void sendDividendStep1() {

        DividendAgrtSendRequest dividendAgrtSendRequest = new DividendAgrtSendRequest();
        dividendAgrtSendRequest.setCycle_id(gameDividendAgrtRequest.cycle_id);
        dividendAgrtSendRequest.setType(gameDividendAgrtRequest.type);

        //选中用户
        HashSet<String> userIds = new HashSet<>();
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof DividendAgrtSendModel) {
                DividendAgrtSendModel m = (DividendAgrtSendModel) bindModel;
                if (Boolean.TRUE.equals(m.checked.get())) {
                    userIds.add(m.getUserid());
                }
            }
        }
        dividendAgrtSendRequest.setUserid(userIds);
        //检查参数
        if (userIds.size() <= 0) {
            showTipDialog(getApplication().getString(R.string.txt_send_dividend_tip3));
            return;
        }

        DividendAgrtSendQuery query = new DividendAgrtSendQuery();

        Disposable disposable = model.getDividendAgrtSendStep1Data(query, dividendAgrtSendRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAgrtSendReeponse>() {
                    @Override
                    public void onResult(DividendAgrtSendReeponse reeponse) {
                        if (reeponse.getStatus() == 1) {

                            showDividendTipDialog(getApplication()
                                    .getString(R.string.txt_send_dividend_tip1,
                                            String.valueOf(dividendAgrtSendRequest.getUserid().size()),
                                            total.getValue())
                            );
                        } else {
                            ToastUtils.show(reeponse.getMsg(), ToastUtils.ShowType.Fail);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show(t.getMessage(), ToastUtils.ShowType.Fail);
                    }
                });
        addSubscribe(disposable);
    }

    private void sendDividendStep2() {

        DividendAgrtSendRequest dividendAgrtSendRequest = new DividendAgrtSendRequest();
        dividendAgrtSendRequest.setCycle_id(gameDividendAgrtRequest.cycle_id);
        dividendAgrtSendRequest.setType(gameDividendAgrtRequest.type);

        //选中用户
        HashSet<String> userIds = new HashSet<>();
        for (BindModel bindModel : bindModels) {
            if (bindModel instanceof DividendAgrtSendModel) {
                DividendAgrtSendModel m = (DividendAgrtSendModel) bindModel;
                if (Boolean.TRUE.equals(m.checked.get())) {
                    userIds.add(m.getUserid());
                }
            }
        }
        dividendAgrtSendRequest.setUserid(userIds);

        DividendAgrtSendQuery query = new DividendAgrtSendQuery();

        Disposable disposable = model.getDividendAgrtSendStep2Data(query, dividendAgrtSendRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAgrtSendReeponse>() {
                    @Override
                    public void onResult(DividendAgrtSendReeponse reeponse) {
                        if (reeponse.getStatus() == 1) {
                            //发送完成消息
                            RxBus.getDefault().post(DividendAgrtSendDialogFragment.SENT);

                            total.setValue("0.00");
                            //重新加载列表数据
                            headModel.p = 1;
                            getDividendData();

                            showTipDialog(reeponse.getMsg());
                        } else {
                            ToastUtils.show(reeponse.getMsg(), ToastUtils.ShowType.Fail);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show(t.getMessage(), ToastUtils.ShowType.Fail);
                    }
                });
        addSubscribe(disposable);
    }

    private synchronized void getDividendData() {

        gameDividendAgrtRequest.p = headModel.p;
        Disposable disposable = model.getGameDividendAgrtData(gameDividendAgrtRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (gameDividendAgrtRequest.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<GameDividendAgrtResponse>() {
                    @Override
                    public void onResult(GameDividendAgrtResponse vo) {

                        if (vo != null) {

                            //p<=1说明是第一页数据
                            if (gameDividendAgrtRequest.p <= 1) {
                                bindModels.clear();
                                if (vo.getSelfPayStatus() != null) {
                                    headModel.setPayoff(vo.getSelfPayStatus().getPayoff());
                                    headModel.setOwe(vo.getSelfPayStatus().getOwe());
                                }
                                bindModels.add(headModel);
                            }

                            GameDividendAgrtResponse.ChildrenBillDTO childrenBill = vo.getChildrenBill();
                            if (childrenBill != null) {
                                List<GameDividendAgrtResponse.ChildrenBillDTO.DataDTO> data = childrenBill.getData();
                                if (data != null) {
                                    for (GameDividendAgrtResponse.ChildrenBillDTO.DataDTO dataDTO : data) {

                                        //只显示未结清契约
                                        if (dataDTO.getPay_status().equals("1")) {
                                            DividendAgrtSendModel sendModel = new DividendAgrtSendModel();
                                            sendModel.setBet(dataDTO.getBet());
                                            sendModel.setNetloss(dataDTO.getNetloss());
                                            sendModel.setPeople(dataDTO.getPeople());
                                            sendModel.setCycle_percent(dataDTO.getCycle_percent());
                                            sendModel.setUserid(dataDTO.getUserid());
                                            sendModel.setUserName(dataDTO.getUsername());
                                            sendModel.setSubMoney(dataDTO.getSub_money());
                                            sendModel.setSelfMoney(dataDTO.getSelf_money());
                                            sendModel.setProfitloss(dataDTO.getProfitloss());
                                            sendModel.setLoseStreak(dataDTO.getLose_streak());
                                            if (vo.getCurrentCycle() != null) {
                                                sendModel.setCycle(vo.getCurrentCycle().getTitle());
                                            }
                                            //设置契约状态
                                            sendModel.setPayStatu(dataDTO.getPay_status());
                                            for (Map.Entry<String, String> entry : vo.getBillStatus().entrySet()) {
                                                if (entry.getKey().equals(sendModel.getPayStatu())) {
                                                    sendModel.setPayStatuText(entry.getValue());
                                                }
                                            }
                                            bindModels.add(sendModel);
                                        }
                                    }
                                }
                            }

                            datas.setValue(bindModels);

                            //更新总数据
                            availablebalance.setValue(vo.getUser().getAvailablebalance());

                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
                            if (mobilePage != null &&
                                    mobilePage.total_page.equals(String.valueOf(gameDividendAgrtRequest.p))) {
                                loadMoreWithNoMoreData();
                            } else {
                                //如果本页不包含未结算数据则加载下一页
                                if (bindModels.size() <= 1) {
                                    headModel.p++;
                                    getDividendData();
                                } else {
                                    finishLoadMore(true);
                                }
                            }
                        } else {
                            finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        finishLoadMore(false);
                    }
                });

        addSubscribe(disposable);
    }

    /**
     * 提示弹窗
     */
    private void showTipDialog(String msg) {
        MsgDialog dialog = new MsgDialog(mActivity.get(), getApplication().getString(R.string.txt_kind_tips), msg, true, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                }
            }
        });

        pop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
    }

    private void showDividendTipDialog(String msg) {
        DividendTipDialog dialog = new DividendTipDialog(mActivity.get(), msg, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                if (pop != null) {
                    pop.dismiss();
                }
            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                }
                sendDividendStep2();
            }
        });

        pop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
