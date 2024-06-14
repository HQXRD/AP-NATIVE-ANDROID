package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChooseWithdrawaBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalBankInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalQuotaVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 选择提款方式Dialog
 */
public class ChooseWithdrawalDialog extends BottomPopupView implements IWithdrawListCallback {

    @Override
    public void onClickListItem(WithdrawalListVo itemVo) {
        CfLog.e("IWithdrawListCallback = " + itemVo.toString());
        selectorVo = itemVo;
        //银行卡类型
        if (TextUtils.equals("1", selectorVo.type)) {
            //获取当前选中的提款详情
            viewModel.getWithdrawalBankInfo(selectorVo.name);
        } else {
            //获取当前选中的提款详情
            viewModel.getWithdrawalInfo(itemVo.name);
        }

    }

    public interface IChooseDialogBack {
        void closeDialog();

        /*网络异常关闭Dialog*/
        void closeDialogByError();

        void closeDialogByFlow(final String money);//由于流水不足关闭Dialog

        void closeDialogByBind();
    }

    private IChooseDialogBack callBack;
    private BasePopupView basePopupView = null;
    private BasePopupView otherWXPopupView = null;
    private BasePopupView otherZFBPopupView = null;
    private BasePopupView bankPopupView = null;
    private BasePopupView loadingView = null;
    DialogChooseWithdrawaBinding binding;
    ChooseWithdrawViewModel viewModel;
    LifecycleOwner owner;
    Context context;
    ChooseInfoVo chooseInfoVo;
    BasePopupView errorPopView = null; // 底部弹窗

    private BasePopupView customPopWindow;
    private BasePopupView firstBankPopWindow;
    private FragmentActivity mActivity;

    private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;

    private WithdrawalQuotaVo quotaVo;//可提额度

    private WithdrawalListVo selectorVo;//被选中的取款类型
    private ArrayList<WithdrawalListVo> withdrawalListVoArrayList;//获取可提现渠道列表
    public static ArrayList<WithdrawalListVo> bankWithdrawalList = new ArrayList<>();//银行卡提款列表
    public static ArrayList<WithdrawalListVo> usdtWithdrawalList = new ArrayList<>();//USDT提款列表
    private WithdrawalInfoVo infoVo;//当前提款渠道详情
    private WithdrawalBankInfoVo bankInfoVo;//银行卡提现渠道详情

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choose_withdrawa;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    private ChooseWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public static ChooseWithdrawalDialog newInstance(Context context, LifecycleOwner owner, IChooseDialogBack callBack, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose, final FragmentActivity activity) {
        ChooseWithdrawalDialog dialog = new ChooseWithdrawalDialog(context);

        dialog.context = context;
        dialog.owner = owner;
        dialog.callBack = callBack;
        dialog.bankWithdrawalClose = bankWithdrawalClose;
        dialog.mActivity = activity;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
        initData();
        LoadingDialog.show(getContext());
        initViewObservable();
        requestData();

    }

    private void initView() {
        binding = DialogChooseWithdrawaBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> {
            dismiss();
            callBack.closeDialog();

        });
        binding.tvwTitle.setText(getContext().getString(R.string.txt_choose_withdrawal_method));
        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                if (callBack != null) {
                    callBack.closeDialog();
                }
            }

            @Override
            public void onDrag(int y, float percent, boolean isScrollUp) {

            }

            @Override
            public void onOpen() {

            }
        });
    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {

        //获取可提额度 刷新底部金额数据
        viewModel.quotaVoMutableLiveData.observe(owner, vo -> {
            quotaVo = vo;
        });
        // 获取可提现渠道列表
        viewModel.withdrawalListVoMutableLiveData.observe(owner, vo -> {
            withdrawalListVoArrayList = vo;
            if (withdrawalListVoArrayList != null && !withdrawalListVoArrayList.isEmpty()) {
                //业务正常 刷新列表UI

                sortingWithdrawalListByType("1", withdrawalListVoArrayList, bankWithdrawalList);
                sortingWithdrawalListByType("2", withdrawalListVoArrayList, usdtWithdrawalList);
                referListUI(sortTypeList(withdrawalListVoArrayList));

            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //获取当前渠道详情
        viewModel.withdrawalInfoVoMutableLiveData.observe(owner, vo -> {
            LoadingDialog.finish();
            infoVo = vo;
            CfLog.e("withdrawalInfoVoMutableLiveData=" + vo.toString());
            if (infoVo != null && !infoVo.user_bank_info.isEmpty()) {
                //业务正常 可以跳转下一级页面
                if (TextUtils.equals("1", selectorVo.type)) {
                    //选中的是银行卡提款
                    /*showBankWithdrawalDialog(selectorVo.name, infoVo, bankWithdrawalList);*/
                } else if (TextUtils.equals("2", selectorVo.type)) {
                    //选中的是USDT提款
                    showUSDTWithdrawalDialog(selectorVo.name, usdtWithdrawalList, infoVo);
                } else if (TextUtils.equals("onepayzfb", selectorVo.name) || TextUtils.equals("onepaywx", selectorVo.name)
                        || TextUtils.equals("支付宝提款", selectorVo.title) || TextUtils.equals("微信提款", selectorVo.title)) {
                    //选中的是微信/支付宝
                    showOtherWXWithdrawalDialog(selectorVo.name, selectorVo, infoVo);
                } else {
                    //选中其他虚拟币提款
                    showOtherVirtualWithdrawalDialog(selectorVo.name, selectorVo, infoVo);
                }
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //获取当前渠道详情 错误信息
        viewModel.withdrawalListErrorData.observe(owner, vo -> {
            final String message = vo;
            if (TextUtils.equals(getContext().getString(R.string.txt_bind_error_sys_tip), message) ||
                    message.contains(getContext().getString(R.string.txt_bind_error_sys_tip))) {
                //显示绑定的Dialog
                showBindTipDialog(selectorVo);
            } else {
                showErrorDialog(message);
            }
        });
        //获取银行卡提现渠道详情
        viewModel.withdrawalBankInfoVoMutableLiveData.observe(owner, vo -> {
            bankInfoVo = vo;
            CfLog.e("withdrawalBankInfoVoMutableLiveData=" + vo.toString());
        /*    if (!TextUtils.isEmpty(bankInfoVo.message) && !TextUtils.equals("success", bankInfoVo.message)) {
                //您尚未绑定
                if (TextUtils.equals(getContext().getString(R.string.txt_bind_error_sys_tip), bankInfoVo.message) ||
                        bankInfoVo.message.contains(getContext().getString(R.string.txt_bind_error_sys_tip))) {
                    //显示绑定的Dialog
                    showBindTipDialog(selectorVo);
                } else {
                    showErrorDialog(bankInfoVo.message);
                }
            } else*/
            if (bankInfoVo != null && !bankInfoVo.user_bank_info.isEmpty()) {
                //业务正常 可以跳转下一级页面
                if (TextUtils.equals("1", selectorVo.type)) {
                    //选中的是银行卡提款
                    showBankWithdrawalDialog(selectorVo.name, bankWithdrawalList, bankInfoVo);
                }
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        ////获取银行卡提现渠道详情 错误
        viewModel.bankInfoVoErrorData.observe(owner, vo -> {
            final String message = vo;
            if (message != null && !TextUtils.isEmpty(message)) {
                if (message.contains("您尚未绑定")) {
                    showBindTipDialog(selectorVo);
                } else {
                    showErrorDialog(message);
                }
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        // showMaskLoading();
        viewModel.getWithdrawQuota();
        viewModel.getWithdrawalList();

        /*viewModel.getChooseWithdrawInfo();*/
    }

    /**
     * 获取银行卡提现 渠道信息
     */
    private void requestBankInfoVo(final String name) {
        viewModel.getWithdrawalBankInfo(name);
    }

    /*显示銀行卡提款loading */
    private void showMaskLoading() {
        if (loadingView == null) {
            loadingView = new XPopup.Builder(getContext()).asCustom(new LoadingDialog(getContext()));
        }

        loadingView.show();
    }

    /*关闭loading*/
    private void dismissMasksLoading() {
        loadingView.dismiss();
    }

    /**
     * 提款列表适配器
     */
    public class WithdrawalListAdapter extends BaseAdapter {

        private IWithdrawListCallback callBack;
        private Context context;
        private ArrayList<WithdrawalListVo> withdrawalItemVoArrayList;
        private ArrayList<WithdrawalListVo> withdrawalItemVoList;

        public WithdrawalListAdapter(Context context, ArrayList<WithdrawalListVo> voArrayList, IWithdrawListCallback callBack) {
            this.context = context;
            this.withdrawalItemVoArrayList = voArrayList;
            this.callBack = callBack;
            this.withdrawalItemVoList = removeDuplicationBy2For(this.withdrawalItemVoArrayList);
        }

        @Override
        public int getCount() {
            return this.withdrawalItemVoList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.withdrawalItemVoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChooseAdapterViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_withdrawa_item, parent, false);
                holder = new ChooseAdapterViewHolder();
                holder.showInfoName = (TextView) convertView.findViewById(R.id.tv_choose_usdt);
                holder.showInfoLinear = convertView.findViewById(R.id.ll_choose_usdt);
                convertView.setTag(holder);
            } else {
                holder = (ChooseAdapterViewHolder) convertView.getTag();
            }

            final WithdrawalListVo vo = this.withdrawalItemVoList.get(position);

            if (TextUtils.equals("1", vo.type)) {
                holder.showInfoName.setText("银行卡提款");
            } else if (TextUtils.equals("2", vo.type)) {
                holder.showInfoName.setText("USDT提款");
            } else {
                holder.showInfoName.setText(vo.title);
            }
            holder.showInfoLinear.setOnClickListener(view -> {
                if (this.callBack != null && !ClickUtil.isFastClick()) {
                    final WithdrawalListVo itemVo = withdrawalItemVoList.get(position);
                    this.callBack.onClickListItem(itemVo);
                }
            });
            holder.showInfoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null && !ClickUtil.isFastClick()) {
                        final WithdrawalListVo itemVo = withdrawalItemVoList.get(position);
                        callBack.onClickListItem(itemVo);
                    }
                }
            });

            return convertView;
        }

        public class ChooseAdapterViewHolder {
            public TextView showInfoName;
            public LinearLayout showInfoLinear;
        }

        /**
         * List去重
         *
         * @param list
         * @return
         */
        public ArrayList<WithdrawalListVo> removeDuplicationBy2For(ArrayList<WithdrawalListVo> list) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(i).type.equals(list.get(j).type)) {
                        list.remove(i);
                    }
                }
            }
            return list;
        }
    }

    /**
     * 跳转银行卡提款页面
     */
    private void showBankWithdrawalDialog(final String name, final ArrayList<WithdrawalListVo> bankWithdrawalList, final WithdrawalBankInfoVo selectorInfoVo) {
        bankPopupView = new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
                .asCustom(BankWithdrawalDialog.newInstance(getContext(), owner, name, bankWithdrawalList, selectorInfoVo, new BankWithdrawalDialog.BankWithdrawalClose() {
                    @Override
                    public void closeBankWithdrawal() {

                        bankPopupView.dismiss();
                    }

                    @Override
                    public void closeBankByNumber() {

                    }
                }));
        bankPopupView.show();
    }

    /**
     * 显示其他虚拟币提款Dialog
     */
    private void showOtherVirtualWithdrawalDialog(final String name, final WithdrawalListVo listVo, final WithdrawalInfoVo infoVo) {

        basePopupView = new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
                .asCustom(VirtualWithdrawalDialog.newInstance(getContext(), owner, name, listVo, infoVo));
        basePopupView.show();
    }

    /**
     * 跳转USDT 提款
     */
    private void showUSDTWithdrawalDialog(final String name, final ArrayList<WithdrawalListVo> listVo, final WithdrawalInfoVo infoVo) {
        basePopupView = new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
                .asCustom(USDTWithdrawalDialog.newInstance(getContext(), owner, name, listVo, infoVo));

        basePopupView.show();
    }

    /**
     * 显示支付宝 微信提款方式
     *
     * @param
     */
    private void showOtherWXWithdrawalDialog(final String name, final WithdrawalListVo listVo, final WithdrawalInfoVo infoVo) {
        if (otherWXPopupView == null) {
            otherWXPopupView = new XPopup.Builder(getContext())
                    .moveUpToKeyboard(false)
                    .asCustom(OtherWebWithdrawalDialog.newInstance(getContext(), owner, name, listVo, infoVo));
        }
        otherWXPopupView.show();
    }

    /**
     * 显示首次绑定
     */
    private void showFirstBankDialog(final String showMessage) {
        if (firstBankPopWindow == null) {
            firstBankPopWindow = new XPopup.Builder(getContext())
                    .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                        @Override
                        public void onClickLeft() {
                            firstBankPopWindow.dismiss();
                        }

                        @Override
                        public void onClickRight() {
                            firstBankPopWindow.dismiss();
                        }
                    }));
            firstBankPopWindow.show();
        } else {
            firstBankPopWindow.show();
        }
    }

    /**
     * 绑定流程
     */
    private void showBindDialog(ChooseInfoVo.ChannelInfo channelInfo, String showMessage) {
        String errorMessage = "";
        String bindType = "";
        if (showMessage.contains("尚未绑定银行卡")) {
            errorMessage = "请先绑定银行卡后才可提款";
            bindType = getContext().getString(R.string.txt_bind_card_type);
        } /*else if (showMessage.contains("首次提款仅可使用银行卡方式提款")) {
            errorMessage = showMessage;
            bindType = getContext().getString(R.string.txt_bind_card_type);
        }*/ else if (showMessage.contains("尚未绑定极速微信地址")) {
            errorMessage = "请先绑定微信地址后才可提款";
            bindType = getContext().getString(R.string.txt_bind_wechat_type);
        } else if (showMessage.contains("尚未绑定支付宝账号")) {
            errorMessage = "请先绑定支付宝地址后才可提款";
            bindType = getContext().getString(R.string.txt_bind_zfb_type);
        } else {
            errorMessage = showMessage;
            bindType = channelInfo.bindType;
        }
        CfLog.e("errorMesssage =  " + errorMessage);
        String finalBindType = bindType;
        customPopWindow = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), errorMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        customPopWindow.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        //跳转绑定流程
                        Bundle bundle = new Bundle();
                        bundle.putString("type", finalBindType);
                        if (TextUtils.equals(finalBindType, getContext().getString(R.string.txt_bind_zfb_type))
                                || TextUtils.equals(finalBindType, getContext().getString(R.string.txt_bind_wechat_type))) {
                            // 绑定页面显示去提款按钮用
                            SPUtils.getInstance().put(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, getContext().getString(R.string.txt_go_withdraw));
                        }

                        String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY;//跳转左右滑动样式页面
                        Intent intent = new Intent(getContext(), ContainerActivity.class);
                        intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                        intent.putExtra(ContainerActivity.BUNDLE, bundle);
                        mActivity.startActivity(intent);
                        customPopWindow.dismiss();
                        callBack.closeDialogByBind();
                    }
                }));
        customPopWindow.show();
    }

    /**
     * 显示绑定Dialog
     */
    private void showBindTipDialog(final WithdrawalListVo selectorVo) {
        String errorMessage = "";
        String bindType = "";
        final String format = getContext().getResources().getString(R.string.txt_bind_tip);
        //其他提款途径提示语
        final String formatOtherType = getContext().getResources().getString(R.string.txt_bind_other_type_tip);

        //尚未绑定流程
        if (TextUtils.equals("1", selectorVo.type)) {
            //银行卡提款
            errorMessage = String.format(format, getContext().getResources().getString(R.string.txt_bind_bank_tip));
            bindType = getContext().getString(R.string.txt_bind_card_type);

        } else if (TextUtils.equals("2", selectorVo.type)) {
            //USDT提款
            errorMessage = String.format(format, getContext().getResources().getString(R.string.txt_bind_usdt_tip));
            bindType = getContext().getString(R.string.txt_bind_usdt_type);
        } else if (TextUtils.equals("4", selectorVo.type)) {
            //EBpay提款
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_ebpay_type);
        } else if (TextUtils.equals("5", selectorVo.type)) {
            //TOPAY提款7
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_topay_type);

        } else if (TextUtils.equals("6", selectorVo.type)) {
            //CNYT提款8"
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_gcnyt_type);
        } else if (TextUtils.equals("7", selectorVo.type)) {
            //MPAY提款10
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_mpay_type);
        } else if (TextUtils.equals("8", selectorVo.type)) {
            //GOBAO提款11
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_gobao_type);
        } else if (TextUtils.equals("10", selectorVo.type)) {
            //"GOPAY提款9",
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_gopay_type);
        } else if (TextUtils.equals("11", selectorVo.type)) {
            //"OKPAY提款12",
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_okpay_type);
        } else if (TextUtils.equals("13", selectorVo.type)) {
            //BOBI提款,
            errorMessage = String.format(formatOtherType, selectorVo.title);
            //不存在BOBI 绑定模式
            bindType = getContext().getString(R.string.txt_bind_gopay_type);
        } else if (TextUtils.equals("14", selectorVo.type)) {
            //极速微信提款13
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_wechat_type);
        } else if (TextUtils.equals("15", selectorVo.type)) {
            //极速支付宝提款17",
            errorMessage = String.format(formatOtherType, selectorVo.title);
            bindType = getContext().getString(R.string.txt_bind_zfb_type);
        }
        String finalBindType = bindType;
        customPopWindow = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), errorMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        customPopWindow.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        //跳转绑定流程
                        Bundle bundle = new Bundle();
                        bundle.putString("type", finalBindType);
                        if (TextUtils.equals(finalBindType, getContext().getString(R.string.txt_bind_zfb_type))
                                || TextUtils.equals(finalBindType, getContext().getString(R.string.txt_bind_wechat_type))) {
                            // 绑定页面显示去提款按钮用
                            SPUtils.getInstance().put(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, getContext().getString(R.string.txt_go_withdraw));
                        }

                        String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY;//跳转左右滑动样式页面
                        Intent intent = new Intent(getContext(), ContainerActivity.class);
                        intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                        intent.putExtra(ContainerActivity.BUNDLE, bundle);
                        mActivity.startActivity(intent);
                        customPopWindow.dismiss();
                        callBack.closeDialogByBind();
                    }
                }));
        customPopWindow.show();
    }

    /**
     * 显示异常Dialog
     */
    private void showErrorDialog(String showMessage) {
        if (showMessage == null) {
            return;
        }
        errorPopView = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        errorPopView.dismiss();
                        //callBack.closeDialog();
                    }

                    @Override
                    public void onClickRight() {
                        errorPopView.dismiss();
                        // callBack.closeDialog();
                    }
                }));
        errorPopView.show();
    }

    /**
     * 刷新列表
     *
     * @param vo
     */
    private void referListUI(ArrayList<WithdrawalListVo> vo) {

        if (vo != null && vo.size() > 0) {
            WithdrawalListAdapter adapter = new WithdrawalListAdapter(getContext(), vo, this);
            binding.lvChoose.setVisibility(View.VISIBLE);
            binding.lvChoose.setAdapter(adapter);

            binding.tvChooseTutorial.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            binding.tvChooseTutorial.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), BrowserActivity.class);
                intent.putExtra(BrowserActivity.ARG_TITLE, "USDT教程");
                intent.putExtra(BrowserActivity.ARG_URL, DomainUtil.getDomain2() + "/static/usdt-description/as/usdt_m.html");
                getContext().startActivity(intent);
            });

            String quota = "";
            if (quotaVo != null && quotaVo.quota != null) {
                if (TextUtils.equals("0", quotaVo.quota)) {
                    quota = "0.00";
                } else {
                    quota =String.format("%.2f",Double.valueOf(quotaVo.quota)) ;
                }
                String tip =
                        String.format(getContext().getString(R.string.txt_choose_withdrawal_tip),
                                quotaVo.fAvailableBalance, quota);
                binding.tvChooseTip.setVisibility(View.VISIBLE);
                binding.tvChooseTip.setText(tip);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvChooseTip.setTextColor(getContext().getColor(R.color.clr_grey_13));
                }
            }

            dismissMasksLoading();
        }
    }

    /**
     * 列表排序
     *
     * @param infoList
     * @return
     */
    private ArrayList<WithdrawalListVo> sortTypeList(ArrayList<WithdrawalListVo> infoList) {
        //列表去重
        TreeSet treeSet =  new TreeSet(infoList);
        infoList.clear();
        infoList.addAll(treeSet);

        CfLog.e("sortTypeList  infoList1= " + infoList.size());
        ArrayList<WithdrawalListVo> arrayList = new ArrayList<WithdrawalListVo>();
        for (int i = 0; i < infoList.size(); i++) {
            //只添加enable为 true状态的，即是开启该提款通道的体况方式
            if (infoList.get(i).enable == true) {
                arrayList.add(infoList.get(i));
            }
        }
        HashSet set = new HashSet(arrayList);
        arrayList.clear();
        arrayList.addAll(set);
        Collections.reverse(arrayList);
        //列表排序
        Collections.sort(arrayList);
        Collections.reverse(arrayList);
        return arrayList;
    }

    /**
     * 依据type类型分拣显示需要的数据类型
     *
     * @param list
     */
    public void sortingWithdrawalListByType(final String type, final ArrayList<WithdrawalListVo> list, ArrayList<WithdrawalListVo> targetList) {
        targetList.clear();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                WithdrawalListVo vo = list.get(i);
                //比对列表 将type为1的[银行卡提款类型]
                if (TextUtils.equals(type, vo.type)) {
                    targetList.add(vo);
                }
            }
            removeDupliByType(targetList);
        }
    }

    private static ArrayList<WithdrawalListVo> removeDupliByType(ArrayList<WithdrawalListVo> list) {
        ArrayList<WithdrawalListVo> wdList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().forEach(
                    p -> {
                        if (!wdList.contains(p)) {
                            wdList.add(p);
                        }
                    }
            );
        }
        TreeSet<WithdrawalListVo> treeSet = new TreeSet<WithdrawalListVo>(list);
        ArrayList<WithdrawalListVo> newList = new ArrayList<>(treeSet);
        return newList;
    }

}
