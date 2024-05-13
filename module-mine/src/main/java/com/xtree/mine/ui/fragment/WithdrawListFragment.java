package com.xtree.mine.ui.fragment;

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

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentWithdrawListBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalBankInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalInfoVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;
import com.xtree.mine.vo.WithdrawVo.WithdrawalQuotaVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 包网 提款列表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_WITHDRAW_LIST)
public class WithdrawListFragment extends BaseFragment<FragmentWithdrawListBinding, ChooseWithdrawViewModel> implements IWithdrawListCallback {
    private BasePopupView basePopupView;
    private BasePopupView bankPopupView;
    private BasePopupView loadingView;
    private BasePopupView errorPopView; // 底部弹窗
    private BasePopupView firstBankPopWindow;
    private BasePopupView bindPopWindow;
    private BasePopupView usdtPopView;//USDT提款彈窗
    private BasePopupView bankPopView;//銀行卡提款彈窗
    private BasePopupView ebpayPopView;//ebpay提款彈窗

    private WithdrawalListVo selectorItemVo;//选中的
    private WithdrawalBankInfoVo bankInfoVo;
    private HashMap<String, ArrayList<WithdrawalListVo>> wdMap;
    private ArrayList<WithdrawalListVo> wdList; //
    private ArrayList<WithdrawalListVo> showWithdrawalList = new ArrayList<>();
    public static  ArrayList<WithdrawalListVo> bankWithdrawalList = new ArrayList<>() ;//银行卡提款列表

    public WithdrawListFragment() {

    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        requestData();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_withdraw_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    @Override
    public void initViewObservable() {

        //获取提款额度
        viewModel.quotaVoMutableLiveData.observe(this, vo -> {
            referQuotaView(vo);
        });

        //获取提款列表
        viewModel.withdrawalListVoMutableLiveData.observe(this, vo -> {
            wdList = vo;
            buildBankList(wdList);
           wdList = removeDupliByType(wdList);

            wdMap =sortTypeList(wdList);
            referListUI(wdMap, wdList);
        });
        //获取当前渠道详情
        viewModel.withdrawalInfoVoMutableLiveData.observe(this, vo -> {
            referWithdrawalInfo(vo , null);
        });
        //获取银行卡渠道详情
        viewModel.bankInfoVoMutableLiveData.observe(this , vo->{
            if (vo != null){
                bankInfoVo = vo;
                referBankWithdrawalInfo( bankInfoVo);
            }
        });
    }

    private void requestData() {
        showMaskLoading();
        viewModel.getWithdrawalList();
        viewModel.getAwardRecord();
        viewModel.getWithdrawQuota();
    }

    /**
     *刷新显示公告区域
     */
    private void referQuotaView(final WithdrawalQuotaVo vo) {

        String quota = "";
        if (TextUtils.equals("0", vo.quota)) {
            quota = "0.00";
        } else {
            quota = vo.quota;
        }
        String tip =
                String.format(getContext().getString(R.string.txt_choose_withdrawal_tip),
                        vo.fAvailableBalance, quota);
        binding.tvChooseTip.setVisibility(View.VISIBLE);
        binding.tvChooseTip.setText(tip);
    }

    /**
     * 刷新列表
     * @param vo
     */
    private void referListUI( HashMap<String, ArrayList<WithdrawalListVo>> map  , ArrayList<WithdrawalListVo> vo) {
        if (vo != null && vo.size() > 0) {
            ChooseAdapter adapter = new ChooseAdapter(getContext(), map , vo, this);
            binding.lvChoose.setVisibility(View.VISIBLE);
            binding.lvChoose.setAdapter(adapter);
            binding.llChooseTip.setVisibility(View.VISIBLE);
            binding.tvChooseTutorial.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            binding.tvChooseTutorial.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), BrowserActivity.class);
                intent.putExtra(BrowserActivity.ARG_TITLE, getContext().getString(R.string.txt_withdrawal_list_tip));
                intent.putExtra(BrowserActivity.ARG_URL, DomainUtil.getDomain2() + "/static/usdt-description/as/usdt_m.html");
                getContext().startActivity(intent);
            });

            dismissMasksLoading();
        }
    }

    /**
     * 请求获取当前渠道详情
     *
     * @param itemVo 提现渠道列表
     */
    private void requestWithdrawInfo(final WithdrawalListVo itemVo) {
        if (itemVo != null && !TextUtils.isEmpty(itemVo.name)) {
            LoadingDialog.show(getContext());
            selectorItemVo = itemVo;
            if (TextUtils.equals("1", itemVo.type)){
                viewModel.getWithdrawalBankInfo(itemVo.name);
            }else {
                viewModel.getWithdrawalInfo(itemVo.name);
            }

        }
    }


    /**
     * 点击列表后 刷新状态
     * @param vo
     */
    private void referWithdrawalInfo(final WithdrawalInfoVo vo , final WithdrawalBankInfoVo bankInfoVo) {
        if (!TextUtils.isEmpty(vo.message)) {
            showErrorDialog(vo.message);
            return;
        } else {
          /*  if (TextUtils.equals("1", selectorItemVo.type)) {
                for (int i = 0; i < bankWithdrawalList.size(); i++) {
                    CfLog.e("bankWithdrawalList  info="+bankWithdrawalList.get(i).toString());
                }
                showBankWithdrawalDialog( bankInfoVo, bankWithdrawalList);
            } else*/
            if (TextUtils.equals("2", selectorItemVo.type) || selectorItemVo.title.contains("USDT")) {
                //usdt提款
                showUSDTWithdrawalDialog(vo);
            } else if (TextUtils.equals("4", selectorItemVo.type) || selectorItemVo.name.contains("ebpay")) {
                //ebpay 提款
                showEBPayWithdrawalDialog(vo);
            }
            else if (TextUtils.equals("6", selectorItemVo.type) || selectorItemVo.name.contains("hiwallet")) {
                showVirtualWithdrawalDialog(vo);
            }
        }
    }
    private void referBankWithdrawalInfo(final WithdrawalBankInfoVo bankInfoVo){
        if (selectorItemVo != null && TextUtils.equals("1",selectorItemVo.type)){
            showBankWithdrawalDialog( bankInfoVo, bankWithdrawalList);
        }
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
     * 显示异常PopView
     */
    private void showErrorDialog(final String message) {
        if (message == null) {
            return;
        }
        errorPopView = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), message, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        errorPopView.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        errorPopView.dismiss();
                    }
                }));
        errorPopView.show();
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
        bindPopWindow = new XPopup.Builder(getContext())
                .asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), errorMessage, false, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        bindPopWindow.dismiss();
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
                        startActivity(intent);
                        bindPopWindow.dismiss();
                    }
                }));
        bindPopWindow.show();
    }

    /**
     * 跳转银行卡提款页面
     */
    private void showBankWithdrawalDialog(final WithdrawalBankInfoVo bankInfoVo ,ArrayList<WithdrawalListVo> infoVoList) {

        bankPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false)
                .asCustom(BankWithdrawalDialog.newInstance(getContext(),this , selectorItemVo.name ,bankWithdrawalList ,bankInfoVo ,new BankWithdrawalDialog.IBankDialogCallback(){

                    @Override
                    public void closeBankDialog() {
                        bankPopupView = null;
                    }
                }));

        bankPopupView.show();
    }

    /**
     * 跳转USDT 提款
     */
    private void showUSDTWithdrawalDialog(WithdrawalInfoVo infoVo) {
        if (basePopupView == null) {

        }
        basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false)
                .asCustom(USDTWithdrawalDialog.newInstance(getContext(), this, selectorItemVo,infoVo, selectorItemVo.name, new USDTWithdrawalDialog.IUSDTDialogCallback() {
                    @Override
                    public void closeUSDTDialog() {
                        basePopupView = null;
                    }
                }));
        basePopupView.show();
    }

    /**
     * 跳转EBPay提款
     * @param infoVo
     */
    private void showEBPayWithdrawalDialog(final WithdrawalInfoVo infoVo) {
        if (basePopupView == null) {

        }
        basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false)
                .asCustom(EBPayWithdrawalDialog.newInstance(getContext(), this,selectorItemVo, infoVo,selectorItemVo.name, new EBPayWithdrawalDialog.IEBPayCallback() {
                    @Override
                    public void closeEBPayDialog() {
                        basePopupView = null;
                    }
                }));
        basePopupView.show();
    }

    private void showVirtualWithdrawalDialog(final  WithdrawalInfoVo infoVo){
        basePopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false)
                .asCustom(VirtualWithdrawalDialog.newInstance(getContext(), this, selectorItemVo, infoVo, selectorItemVo.name, new VirtualWithdrawalDialog.IVirtualDialogCallback() {
                    @Override
                    public void closeVirtualDialog() {
                        basePopupView = null;
                    }
                }));
        basePopupView.show();
    }

    /**
     * 显示支付宝 微信提款方式
     *
     * @param info
     */
    private void showOtherWXWithdrawalDialog(final ChooseInfoVo.ChannelInfo info) {
        /*if (otherWXPopupView == null) {
            otherWXPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(OtherWebWithdrawalDialog.newInstance(getContext(), owner, info));
        }
        otherWXPopupView.show();*/
    }

    private void showOtherZFBWithdrawalDialog(final ChooseInfoVo.ChannelInfo info) {
       /* if (otherZFBPopupView == null) {
            otherZFBPopupView = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(OtherWebWithdrawalDialog.newInstance(getContext(), owner, info));
        }
        otherZFBPopupView.show();*/
    }

    /**
     * IWithdrawListCallback 回调
     * @param itemVo
     */
    @Override
    public void onClickListItem(WithdrawalListVo itemVo) {
        if (TextUtils.equals("1", itemVo.type)){
            //点击了银行卡
            requestWithdrawInfo(itemVo);
        }
        else {
            requestWithdrawInfo(itemVo);
        }

    }

    private class ChooseAdapter extends BaseAdapter {
        private IWithdrawListCallback callBack;
        private Context context;
        private ArrayList<WithdrawalListVo> withdrawalItemVoArrayList;
        private HashMap<String, ArrayList<WithdrawalListVo>> map;
        private ArrayList<WithdrawalListVo> withdrawalItemVoList;
        public ChooseAdapter(Context context, HashMap<String, ArrayList<WithdrawalListVo>> map ,ArrayList<WithdrawalListVo> voArrayList, IWithdrawListCallback callBack) {
            this.context = context;
            this.map = map;
            this.withdrawalItemVoArrayList = voArrayList;
            this.callBack = callBack;
            this.withdrawalItemVoList = (ArrayList<WithdrawalListVo>) removeDuplicationBy2For(this.withdrawalItemVoArrayList);
        }

        /**
         * List去重
         * @param list
         * @return
         */
        public  List removeDuplicationBy2For(List<WithdrawalListVo> list) {
            for (int i=0;i<list.size();i++)
            {
                for (int j=i+1;j<list.size();j++)
                {
                    if(list.get(i).type.equals(list.get(j).type)){
                        list.remove(i);
                    }
                }
            }
            return list;
        }

        @Override
        public int getCount() {
            return  this.withdrawalItemVoList.size();
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

            final  WithdrawalListVo vo = this.withdrawalItemVoList.get(position);

            if (TextUtils.equals("1",vo.type)){
                holder.showInfoName.setText("银行卡提款");
            }else {
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
    }

    /**
     * 列表排序
     * @param infoList
     * @return
     */
    private HashMap<String, ArrayList<WithdrawalListVo>> sortTypeList(ArrayList<WithdrawalListVo> infoList){
        ArrayList<String> arrayList = new ArrayList<String>();
        HashMap<String ,ArrayList<WithdrawalListVo>> map = new HashMap<>();
        for (int i = 0; i < infoList.size(); i++) {
            arrayList.add(infoList.get(i).type);
        }

        HashSet set = new HashSet(arrayList);
        arrayList.clear();
        arrayList.addAll(set);

        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<WithdrawalListVo> showList = new ArrayList<>();
            for (int j = 0; j < infoList.size(); j++) {
                if (arrayList.get(i).equals(infoList.get(j).type)){
                    showList.add(infoList.get(j));
                    map.put(arrayList.get(i) , showList);
                }
            }
        }

        return map;
    }
    public  void buildBankList (final  ArrayList<WithdrawalListVo> list){
        if (list !=null && !list.isEmpty()){
            for (int i = 0; i < list.size(); i++) {
                if (bankWithdrawalList == null){
                    bankWithdrawalList = new ArrayList<>();
                }
                WithdrawalListVo vo = list.get(i);
                if (TextUtils.equals("1",vo.type)){
                    bankWithdrawalList.add(vo);
                }
            }
        }
    }
    private static  ArrayList<WithdrawalListVo> removeDupliByType(ArrayList<WithdrawalListVo> list){
        ArrayList<WithdrawalListVo> wdList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().forEach(
                    p ->{
                        if (!wdList.contains(p)){
                            wdList.add(p);
                        }
                    }
            );
        }


        return wdList;
    }
}
