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
import com.xtree.base.utils.StringUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChooseWithdrawaBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoMoYuVo;
import com.xtree.mine.vo.ChooseInfoVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 选择提款方式Dialog
 */
public class ChooseWithdrawalDialog extends BottomPopupView {

    public interface IChooseDialogBack {
        void closeDialog();

        /*网络异常关闭Dialog*/
        void closeDialogByError();

        void closeDialogByBind();

    }

    private String checkCode;
    private IChooseDialogBack callBack;
    private BasePopupView basePopupView = null;
    private DialogChooseWithdrawaBinding binding;
    private ChooseWithdrawViewModel viewModel;

    private LifecycleOwner owner;
    private Context context;
    private ChooseInfoMoYuVo chooseInfoVo;
    private BasePopupView customPopWindow = null; // 公共弹窗 底部弹窗
    private BasePopupView bindCardPopWindow = null; // 绑定银行卡 底部弹窗
    private BasePopupView otherWXPopupView = null;
    private BasePopupView otherZFBPopupView = null;
    private BasePopupView loadingView = null;
    private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;
    private FragmentActivity mActivity;

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

    public static ChooseWithdrawalDialog newInstance(Context context, LifecycleOwner owner, IChooseDialogBack callBack, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose) {
        ChooseWithdrawalDialog dialog = new ChooseWithdrawalDialog(context);

        dialog.context = context;
        dialog.owner = owner;
        dialog.callBack = callBack;
        dialog.bankWithdrawalClose = bankWithdrawalClose;
        return dialog;
    }

    public static ChooseWithdrawalDialog newInstance(Context context, LifecycleOwner owner, IChooseDialogBack callBack, BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose, final String checkCode, final FragmentActivity activity) {
        ChooseWithdrawalDialog dialog = new ChooseWithdrawalDialog(context);

        dialog.context = context;
        dialog.owner = owner;
        dialog.callBack = callBack;
        dialog.bankWithdrawalClose = bankWithdrawalClose;
        dialog.checkCode = checkCode;
        dialog.mActivity = activity;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
        initData();

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
        dismissMasksLoading();
        viewModel.chooseInfoMoYuVoMutableLiveData.observe(owner, vo -> {
            chooseInfoVo = vo;
            if (chooseInfoVo.networkStatus == 1 && callBack != null) {
                //网络异常
                callBack.closeDialogByError();
            } else {
                if (!TextUtils.isEmpty(chooseInfoVo.msg_type) && chooseInfoVo.msg_type.equals("2")) {
                    //异常状态
                    showErrorDialog(chooseInfoVo.message);

                } else if (!TextUtils.isEmpty(chooseInfoVo.ur_here) && TextUtils.equals("资金密码检查", chooseInfoVo.ur_here)) {
                    //异常状态 弹出资金密码输入页面
                    ToastUtils.showError(chooseInfoVo.ur_here);
                } else if (!TextUtils.isEmpty(chooseInfoVo.error)) {
                    showErrorDialog(chooseInfoVo.message);
                } else if (TextUtils.isEmpty(chooseInfoVo.error) || chooseInfoVo.error == null) {
                    checkCode = chooseInfoVo.check;
                    dismissMasksLoading();
                    referUI();
                }
            }
        });
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        showMaskLoading();
        viewModel.getChooseWithdrawInfo(checkCode);
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
        if (loadingView != null) {
            loadingView.dismiss();
        }

    }

    private void referUI() {
        if (chooseInfoVo.wdChannelList != null && !chooseInfoVo.wdChannelList.isEmpty()) {
            if (chooseInfoVo.wdChannelList.size() > 0) {
                ChooseAdapter adapter = new ChooseAdapter(context, chooseInfoVo.wdChannelList, new IChooseCallback() {
                    @Override
                    public void onClick(String txt, ChooseInfoVo.ChannelInfo channelInfo) {

                        ChooseInfoVo.ChannelInfo channel = channelInfo;
                        if (channel.isBind == false) {
                            if (!TextUtils.isEmpty(channel.channeluseMessage) && !channel.isBind) {
                                showBindDialog(channel, channel.channeluseMessage);
                            } else {
                                showErrorDialog(channel.channeluseMessage);
                            }
                        } else if (channel.isBind && TextUtils.isEmpty(channel.channeluseMessage)) {
                            if (TextUtils.equals("银行卡提款", txt) || TextUtils.equals("hipaytx", channel.configkey)) {
                                showBankWithdrawalDialog(channelInfo, checkCode);
                            }else if (TextUtils.equals("onepaywx", channel.configkey) && TextUtils.equals("bindcardwx", channel.bindType)) {
                                showOtherWXWithdrawalDialog(channelInfo);
                            } else if (TextUtils.equals("onepayzfb", channel.configkey) && TextUtils.equals("bindcardzfb", channel.bindType)) {
                                showOtherZFBWithdrawalDialog(channelInfo);
                            } else {
                                showUSDTWithdrawalDialog(channelInfo, checkCode);
                            }
                        } else if (channel.isBind && !TextUtils.isEmpty(channel.channeluseMessage)) {
                            showErrorDialog(channel.channeluseMessage);
                        }
                    }

                });
                binding.lvChoose.setVisibility(View.VISIBLE);
                binding.lvChoose.setAdapter(adapter);
                binding.llChooseTip.setVisibility(View.VISIBLE);
                binding.tvChooseTutorial.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                //下划线TextView 点击事件
                binding.tvChooseTutorial.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), BrowserActivity.class);
                    intent.putExtra(BrowserActivity.ARG_TITLE, "USDT教程");
                    intent.putExtra(BrowserActivity.ARG_URL, DomainUtil.getDomain2() + "/static/usdt-description/as/usdt_m.html");
                    getContext().startActivity(intent);
                });
                String tip = "";
                if (!chooseInfoVo.bankcardstatus_usdt && !chooseInfoVo.bankcardstatus_ebpay) {
                    tip =
                            String.format(getContext().getString(R.string.txt_choose_withdrawal_nobind_tip),
                                    StringUtils.formatToSeparate(Float.valueOf((chooseInfoVo.user.availablebalance))));
                } else {
                    tip =
                            String.format(getContext().getString(R.string.txt_choose_withdrawal_tip),
                                    StringUtils.formatToSeparate(Float.valueOf((chooseInfoVo.user.availablebalance))), chooseInfoVo.usdtInfo.quota);
                }

                binding.tvChooseTip.setVisibility(View.VISIBLE);
                binding.tvChooseTip.setText(tip);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvChooseTip.setTextColor(getContext().getColor(R.color.clr_withdrawal_list_bottom_text_tip_bg));
                }
            }
        } else {
            showErrorDialog(getContext().getString(R.string.txt_network_error));
        }

    }

    private class ChooseAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private IChooseCallback callBack;
        private Context context;
        private ArrayList<ChooseInfoVo.ChannelInfo> chooseInfoVoArrayList;

        public ChooseAdapter(Context context, ArrayList<ChooseInfoVo.ChannelInfo> list, IChooseCallback callBack) {
            this.context = context;
            this.chooseInfoVoArrayList = list;
            this.callBack = callBack;
        }

        @Override
        public int getCount() {
            return this.chooseInfoVoArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.chooseInfoVoArrayList.get(position);
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

            holder.showInfoName.setText(chooseInfoVoArrayList.get(position).title);
            holder.showInfoLinear.setOnClickListener(view -> {
                if (this.callBack != null && !ClickUtil.isFastClick()) {
                    this.callBack.onClick(chooseInfoVoArrayList.get(position).title, chooseInfoVoArrayList.get(position));
                }
            });
            holder.showInfoLinear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null && !ClickUtil.isFastClick()) {
                        callBack.onClick(chooseInfoVoArrayList.get(position).title, chooseInfoVoArrayList.get(position));
                    }
                }
            });
            return convertView;
        }

        public class ChooseAdapterViewHolder {
            public TextView showInfoName;
            public LinearLayout showInfoLinear;
        }
    }

    private void toBindCard() {
        String msg = getContext().getString(R.string.txt_rc_bind_bank_card_pls);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight() {

                Bundle bundle = new Bundle();
                bundle.putString("type", "bindcard");

                String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
                Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                intent.putExtra(ContainerActivity.BUNDLE, bundle);
                getContext().startActivity(intent);
                dismiss();
                bindCardPopWindow.dismiss();

            }
        });
        bindCardPopWindow = new XPopup.Builder(getContext()).asCustom(dialog);
        bindCardPopWindow.show();

    }

    /**
     * 显示支付宝 微信提款方式
     *
     * @param info
     */
    private void showOtherWXWithdrawalDialog(final ChooseInfoVo.ChannelInfo info) {
        if (otherWXPopupView == null) {
            otherWXPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(OtherWebWithdrawalDialog.newInstance(getContext(), owner, info));
        }
        otherWXPopupView.show();
    }

    private void showOtherZFBWithdrawalDialog(final ChooseInfoVo.ChannelInfo info) {
        if (otherZFBPopupView == null) {
            otherZFBPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(OtherWebWithdrawalDialog.newInstance(getContext(), owner, info));
        }
        otherZFBPopupView.show();
    }

    /**
     * 跳转银行卡提款页面
     */
    private void showBankWithdrawalDialog(ChooseInfoVo.ChannelInfo channelInfo, final String checkCode) {
        basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(BankWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose, checkCode));
        basePopupView.show();
    }

    /**
     * 跳转USDT 提款
     */
    private void showUSDTWithdrawalDialog(ChooseInfoVo.ChannelInfo channelInfo, final String checkCode) {
        if (channelInfo.title.contains("USDT")) {
            basePopupView = new XPopup.Builder(getContext()).asCustom(USDTWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose, checkCode, channelInfo.type));
        } else {
            basePopupView = new XPopup.Builder(getContext()).asCustom(VirtualWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose, checkCode, channelInfo.type));
        }

        basePopupView.show();
    }

    /**
     * 显示绑定Dialog
     */
    private void showBindDialog(ChooseInfoVo.ChannelInfo channelInfo, String showMessage) {
        String errorMessage = "";
        String bindType = "";
        if (showMessage.contains("尚未绑定银行卡")) {
            errorMessage = "请先绑定银行卡后才可提款";
            bindType = getContext().getString(R.string.txt_bind_card_type);
        } else if (showMessage.contains("首次提款仅可使用银行卡方式提款")) {
            errorMessage = showMessage;
            bindType = getContext().getString(R.string.txt_bind_card_type);
        } else {
            errorMessage = showMessage;
            bindType = channelInfo.bindType;
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
                           // SPUtils.getInstance().put(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, getContext().getString(R.string.txt_go_withdraw));
                        }

                        String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
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

        customPopWindow = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        customPopWindow.dismiss();
                        // callBack.closeDialog();

                    }

                    @Override
                    public void onClickRight() {
                        customPopWindow.dismiss();
                        // callBack.closeDialog();
                    }
                }));
        customPopWindow.show();
    }
}
