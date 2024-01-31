package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChooseWithdrawaBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.ChooseInfoVo;
import java.util.ArrayList;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 选择提款方式Dialog
 */
public class ChooseWithdrawalDialog  extends BottomPopupView {
    private BasePopupView basePopupView = null;
    DialogChooseWithdrawaBinding binding ;
    ChooseWithdrawViewModel viewModel ;
    LifecycleOwner owner;
    Context context ;
    ChooseInfoVo chooseInfoVo ;
    BasePopupView ppw = null; // 底部弹窗

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choose_withdrawa;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 75 / 100);
    }

    private ChooseWithdrawalDialog(@NonNull Context context) {
        super(context);
    }
    public static ChooseWithdrawalDialog newInstance(Context context, LifecycleOwner owner)
    {
        ChooseWithdrawalDialog dialog = new ChooseWithdrawalDialog(context);
        context = context ;
        dialog.context = context ;
        dialog.owner =owner ;
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
    private void initView()
    {
        binding = DialogChooseWithdrawaBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v-> dismiss());
        binding.tvwTitle.setText(getContext().getString(R.string.txt_choose_withdrawal_method));
       
     
    }

    private void initData()
    {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable()
    {
            viewModel.chooseInfoVoMutableLiveData.observe(owner , vo ->{
                chooseInfoVo = vo;
               if (TextUtils.isEmpty(chooseInfoVo.error) || chooseInfoVo.error == null)
               {
                   referUI();
               }
               else
               {
                   showErrorDialog(chooseInfoVo.message);
               }
            });
    }

    /**
     * 请求网络数据
     */
    private void  requestData()
    {
        viewModel.getChooseWithdrawInfo();
    }
    private void  referUI()
    {
        if (chooseInfoVo.wdChannelList.size() > 0)
        {
            ChooseAdapter adapter = new ChooseAdapter(getContext(), chooseInfoVo.wdChannelList, new IChooseCallback() {
                @Override
                public void onClick(String txt , ChooseInfoVo.ChannelInfo channelInfo) {
                    String slectorChannel = txt ;
                    ChooseInfoVo.ChannelInfo channel = channelInfo;
                    if (channel.channeluse == 0)//显示弹窗
                    {
                        if (TextUtils.isEmpty(channelInfo.channeluseMessage))
                        {
                            String errorMessage = "请先绑"+channelInfo.configkey.toUpperCase()+"后才可提款";
                            showMessageDialog(channelInfo ,errorMessage);
                        }
                        else
                        {
                            showMessageDialog(channelInfo ,channelInfo.channeluseMessage);
                        }
                        CfLog.i("conClick" + channelInfo.channeluseMessage);

                    }
                    else
                    {
                        if (chooseInfoVo.bankchanneluse ==1 && txt.equals("银行卡提款"))
                        {
                            showBankWithdrawalDialog(channelInfo);
                        }
                        //银行卡提现通达打开，但点击的不是银行卡提款
                        else if (chooseInfoVo.bankchanneluse ==1 && !txt.equals("银行卡提款"))
                        {
                            showUSDTWithdrawalDialog(channelInfo);
                        }

                    }

                    CfLog.i("onClick   ---> 点击了 " + txt);
                }
            });
            binding.lvChoose.setAdapter(adapter);
            binding.llChooseTutorial.setVisibility(View.VISIBLE);
            binding.tvChooseTutorial.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //下划线TextView 点击事件
            binding.tvChooseTutorial.setOnClickListener(v->{
                CfLog.i(" binding.tvChooseTutorial. binding.tvChooseTutorial. binding.tvChooseTutorial.");
                
                Intent intent = new Intent(getContext() ,BrowserActivity.class);
                intent.putExtra(BrowserActivity.ARG_TITLE , "USDT教程");
               
                intent.putExtra(BrowserActivity.ARG_URL ,  DomainUtil.getDomain2()+"/static/usdt-description/as/usdt_m.html");
                getContext().startActivity(intent);
            });
            binding.llChooseTip.setVisibility(View.VISIBLE);

            String tipAvail ;
            String usdtAcail;
            tipAvail = "可用提款余额: "+ StringUtils.formatToSeparate(Float.valueOf(chooseInfoVo.user.availablebalance));
            if (chooseInfoVo.usdtInfo.blebalance == 0)
            {
                usdtAcail = "其中0.0可以使用虚拟币提款取出" ;
            }
            else
            {
                usdtAcail = "其中"+StringUtils.formatToSeparate(Float.valueOf(chooseInfoVo.usdtInfo.blebalance)) +"可以使用虚拟币提款取出";
            }
            String showChooseTip = tipAvail + usdtAcail ;
            binding.tvChooseTip.setVisibility(View.VISIBLE);
            binding.tvChooseTip.setText(showChooseTip);

        }
    }


    private class ChooseAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater ;
        private IChooseCallback  callBack ;
        private Context context ;
        private ArrayList<ChooseInfoVo.ChannelInfo> chooseInfoVoArrayList ;

        public ChooseAdapter(Context context , ArrayList<ChooseInfoVo.ChannelInfo>  list , IChooseCallback callBack)
        {
            this.context = context ;
            this.chooseInfoVoArrayList = list ;
            this.callBack = callBack ;
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
            if (convertView == null)
            {
                convertView =  LayoutInflater.from(context).inflate(R.layout.dialog_choose_withdrawa_item ,parent,false);
                holder = new ChooseAdapterViewHolder();
                holder.showInfoName = (TextView) convertView.findViewById(R.id.tv_choose_usdt);
                holder.showInfoLinear = convertView.findViewById(R.id.ll_choose_usdt);
                convertView.setTag(holder);
             }
            else
            {
                holder = (ChooseAdapterViewHolder) convertView.getTag() ;
            }

            holder.showInfoName.setText(chooseInfoVoArrayList.get(position).title);
            holder.showInfoLinear.setOnClickListener(view->{
                if (this.callBack != null)
                {
                    this.callBack.onClick(chooseInfoVoArrayList.get(position).title , chooseInfoVoArrayList.get(position));
                }
            });
            holder.showInfoLinear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null)
                    {
                        callBack.onClick(chooseInfoVoArrayList.get(position).title ,  chooseInfoVoArrayList.get(position));
                    }
                }
            });
            return  convertView;
        }


        public  class ChooseAdapterViewHolder
        {
            public TextView showInfoName ;
            public LinearLayout showInfoLinear;
        }
    }
    /**
     * 跳转银行卡提款页面
     */
    private void  showBankWithdrawalDialog( ChooseInfoVo.ChannelInfo channelInfo)
    {
        basePopupView = new XPopup.Builder(getContext()).asCustom(BankWithdrawalDialog.newInstance(getContext(),owner ,channelInfo));
        basePopupView.show();
    }
    /** 跳转USDT 提款*/
    private void  showUSDTWithdrawalDialog(ChooseInfoVo.ChannelInfo channelInfo)
    {
        if (channelInfo.title.contains("USDT"))
        {
            basePopupView = new XPopup.Builder(getContext()).asCustom(USDTWithdrawalDialog.newInstance(getContext(),owner ,channelInfo));
        }
        else
        {
            basePopupView = new XPopup.Builder(getContext()).asCustom(VirtualWithdrawalDialog.newInstance(getContext(),owner ,channelInfo));
        }

        basePopupView.show();
    }

    private void  showMessageDialog(ChooseInfoVo.ChannelInfo channelInfo ,String showMessage)
    {
        String errorMessage = "请先绑"+channelInfo.configkey.toUpperCase()+"后才可提款";
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), errorMessage, false, new MsgDialog.ICallBack()
        {
          @Override
             public void onClickLeft() {
              ppw.dismiss();
        }

        @Override
        public void onClickRight()
        {
            //跳转绑定流程
            Bundle bundle = new Bundle() ;
            bundle.putString("type" ,channelInfo.bindType);
    
            String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra(ContainerActivity.ROUTER_PATH, path);
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
            getContext().startActivity(intent);
            dismiss();
            ppw.dismiss();
        }
         }));
        ppw.show();
    }

    /** 显示异常Dialog*/
    private void  showErrorDialog(String showMessage)
    {
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), showMessage, true, new MsgDialog.ICallBack()
        {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight()
            {
                dismiss();
                ppw.dismiss();
            }
        }));
        ppw.show();
    }
}
