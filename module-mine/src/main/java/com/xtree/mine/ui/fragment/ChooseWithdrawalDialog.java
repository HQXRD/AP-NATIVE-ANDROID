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
import com.xtree.mine.vo.ChooseInfoVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.ContainerActivity;
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
    BasePopupView ppw = null; // 底部弹窗

    private BasePopupView customPopWindow;
    private FragmentActivity mActivity;

    private BankWithdrawalDialog.BankWithdrawalClose bankWithdrawalClose;

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
        LoadingDialog.show(getContext());
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
        viewModel.chooseInfoVoMutableLiveData.observe(owner, vo -> {
            dismissMasksLoading();
            chooseInfoVo = vo;
            if (chooseInfoVo.networkStatus == 1 && callBack != null) {
                //网络异常
                callBack.closeDialogByError();
            } else {
                if (!TextUtils.isEmpty(chooseInfoVo.msg_type) && TextUtils.equals("2", chooseInfoVo.msg_type)) {
                    //异常状态
                    //showErrorDialog(chooseInfoVo.message);
                    callBack.closeDialogByFlow(chooseInfoVo.message);
                    //this.dismiss();

                } else if (TextUtils.equals("chooseInfoVo.wdChannelList is Null", chooseInfoVo.error)) {
                    //异常状态 提款列表数据为空
                    ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    return;
                } else if (TextUtils.isEmpty(chooseInfoVo.error) || chooseInfoVo.error == null) {
                    referUI();
                } else {
                    //showErrorDialog(chooseInfoVo.message);
                    callBack.closeDialogByFlow(chooseInfoVo.message);
                    this.dismiss();
                }
            }

        });
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        showMaskLoading();
        viewModel.getChooseWithdrawInfo();
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

    private void referUI() {
        CfLog.e("referUI = " + chooseInfoVo.wdChannelList.size());
        if (chooseInfoVo.wdChannelList.size() > 0) {
            ChooseAdapter adapter = new ChooseAdapter(context, chooseInfoVo.wdChannelList, new IChooseCallback() {
                @Override
                public void onClick(String txt, ChooseInfoVo.ChannelInfo channelInfo) {

                    ChooseInfoVo.ChannelInfo channel = channelInfo;

                    if (channel.isBind == false) {
                        if (!TextUtils.isEmpty(channel.channeluseMessage) && !channel.isBind) {
                            showBindDialog(channel, channel.channeluseMessage);
                        } /*else if (TextUtils.equals("极速支付宝提款", txt) || TextUtils.equals("极速微信提款", txt)) {
                            showOtherWithdrawalDialog(channelInfo);
                        }*/ else {
                            showErrorDialog(channel.channeluseMessage);
                        }
                    } else if (channel.isBind && TextUtils.isEmpty(channel.channeluseMessage)) {
                        CfLog.i("txt = " + txt +"isBind = " + channel.isBind + " ||\nChooseAdapter channel = " + channel.toString());
                        if (TextUtils.equals("银行卡提款", txt)) {
                            showBankWithdrawalDialog(channelInfo);
                        } else if (TextUtils.equals("极速微信提款", txt) && TextUtils.equals("bindcardwx" , channel.bindType)) {
                            showOtherWXWithdrawalDialog(channelInfo);
                        } else if (TextUtils.equals("极速支付宝提款", txt) && TextUtils.equals("bindcardzfb" , channel.bindType)) {
                            showOtherZFBWithdrawalDialog(channelInfo);
                        }
                        else {
                            showUSDTWithdrawalDialog(channelInfo);
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

            String tip =
                    String.format(getContext().getString(R.string.txt_choose_withdrawal_tip),
                            StringUtils.formatToSeparate(Float.valueOf((chooseInfoVo.user.availablebalance))), chooseInfoVo.usdtInfo.quota);
            binding.tvChooseTip.setVisibility(View.VISIBLE);
            binding.tvChooseTip.setText(tip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvChooseTip.setTextColor(getContext().getColor(R.color.red));
            }
            LoadingDialog.finish();
        }
    }

    private class ChooseAdapter extends BaseAdapter {
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

    /**
     * 跳转银行卡提款页面
     */
    private void showBankWithdrawalDialog(ChooseInfoVo.ChannelInfo channelInfo) {
        bankPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).moveUpToKeyboard(false).asCustom(BankWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose, new BankWithdrawalDialog.BankWithdrawaDialogClose() {
            @Override
            public void closeBankByNumber() {
                bankPopupView.dismiss();
            }
        }));
        bankPopupView.show();
    }

    /**
     * 跳转USDT 提款
     */
    private void showUSDTWithdrawalDialog(ChooseInfoVo.ChannelInfo channelInfo) {
        if (channelInfo.title.contains("USDT")) {
            basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(USDTWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose));
        } else {
            basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(VirtualWithdrawalDialog.newInstance(getContext(), owner, channelInfo, bankWithdrawalClose));
        }

        basePopupView.show();
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
     * 绑定流程
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
        } else if (showMessage.contains("尚未绑定极速微信地址")) {
            errorMessage = "请先绑定微信地址后才可提款";
            bindType = getContext().getString(R.string.txt_bind_wechat_type);
        } else if (showMessage.contains("尚未绑定极速支付宝地址")) {
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
        ppw = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                        //callBack.closeDialog();
                    }

                    @Override
                    public void onClickRight() {
                        ppw.dismiss();
                        // callBack.closeDialog();
                    }
                }));
        ppw.show();
    }
}
