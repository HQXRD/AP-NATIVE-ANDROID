package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.widget.ListDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBankWithdrawalUsdtBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.BankCardCashVo;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTConfirmVo;
import com.xtree.mine.vo.USDTSecurityVo;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * USDT虚拟币提款
 */
public class USDTWithdrawalDialog extends BottomPopupView
{
    private String type = "USDT";//默认选中USDT提款
    private Context context ;
    private LifecycleOwner owner ;
    ChooseWithdrawViewModel viewModel ;
    private ChooseInfoVo.ChannelInfo channelInfo ;
    ArrayList<USDTCashVo.Usdtinfo> usdtinfoTRC = new ArrayList<>(); //TRC20地址
    private USDTCashVo.Usdtinfo selectUsdtInfo ;//选中的支付
    private USDTCashVo usdtCashVo ;

    private USDTSecurityVo usdtSecurityVo ;
    private USDTConfirmVo usdtConfirmVo ;
    @NonNull
    DialogBankWithdrawalUsdtBinding binding ;
    public USDTWithdrawalDialog(@NonNull Context context) {
        super(context);
    }
    public  static USDTWithdrawalDialog newInstance(Context context , LifecycleOwner owner , ChooseInfoVo.ChannelInfo channelInfo)
    {
        USDTWithdrawalDialog dialog = new USDTWithdrawalDialog(context) ;
        context = context ;
        dialog.context = context ;
        dialog.owner = owner ;
        dialog.channelInfo = channelInfo ;
        return dialog ;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_withdrawal_usdt;
    }
    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initData();
        initViewObservable();
        requestData();

    }

    private void  initView()
    {
        binding = DialogBankWithdrawalUsdtBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v-> dismiss());
        binding.tvwTitle.setText("请选择提款方式");

        binding.etInputMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
        //显示设置请求View
        if (binding.llVirtualTop.getVisibility() == View.VISIBLE)
        {
            //点击嗨钱包
              binding.llOtherUsdt.setOnClickListener(v->{
                  binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
                  binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
              });
              //点击usdt
            binding.llUsdt.setOnClickListener(v ->{
                binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
                binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            });
            //test 显不同状态页面
            binding.tvSetWithdrawalRequest.setOnClickListener( v -> {
                //ll_virtual_top  ll_virtual_confirm_view  ll_over_apply
                binding.llSetRequestView.setVisibility(View.VISIBLE);
                binding.llVirtualConfirmView.setVisibility(View.GONE);
                binding.llOverApply.setVisibility(View.GONE);
            });

            binding.tvConfirmWithdrawalRequest.setOnClickListener(v->{
                binding.llSetRequestView.setVisibility(View.GONE);
                binding.llVirtualConfirmView.setVisibility(View.VISIBLE);
                binding.llOverApply.setVisibility(View.GONE);
            });
            binding.tvOverWithdrawalRequest.setOnClickListener(v -> {
                binding.llSetRequestView.setVisibility(View.GONE);
                binding.llVirtualConfirmView.setVisibility(View.GONE);
                binding.llOverApply.setVisibility(View.VISIBLE);
            });
        }

    }
    private  void  initData()
    {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }
    private void  initViewObservable()
    {
        //USDT提款设置提款请求 返回model
        viewModel.usdtCashVoMutableLiveData.observe(owner, vo -> {
            usdtCashVo = vo ;
            for (int i = 0; i < usdtCashVo.usdtinfo.size(); i++)
            {
                if (usdtCashVo.usdtinfo.get(i).usdt_type.contains("TRC20"))
                {
                    usdtinfoTRC.add(usdtCashVo.usdtinfo.get(i));
                }
            }
            for (int i = 0; i < usdtinfoTRC.size(); i++)
            {
                CfLog.i("TRC20 = " +  usdtinfoTRC.get(0).usdt_type +" " + usdtinfoTRC.get(0).usdt_card);
            }
            selectUsdtInfo = usdtCashVo.usdtinfo.get(0) ;
            refreshSetUI();
        });
        //USDT确认提款信息
        viewModel.usdtSecurityVoMutableLiveData.observe(owner , vo->{
            usdtSecurityVo = vo ;
            refreshSecurityUI();
        });
        //USDT完成申请
        viewModel.usdtConfirmVoMutableLiveData.observe(owner , vo->{
            usdtConfirmVo = vo ;
            refreshConfirmUI();
         });

    }
    private void   requestData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("usdt_type",channelInfo.type);
        CfLog.i("requestData =" + channelInfo.toString());
        viewModel.getChooseWithdrawUSDT(map);
    }
    /** 刷新初始UI*/
    private void refreshSetUI()
    {
        binding.llSetRequestView.setVisibility(View.VISIBLE);
        if (usdtCashVo.channel_list.size() == 2)
        {
            binding.tvVirtualUsdt.setText(usdtCashVo.channel_list.get(0).title);
            binding.tvVirtualOther.setText(usdtCashVo.channel_list.get(1).title);
        }
        //注意：每天限制提款5次，您已提款1次 提款时间为00:01至00:00，您今日剩余提款额度为 199900.00元
        String notice = "注意：每天限制提款"+usdtCashVo.count+"次，提款时间为"+usdtCashVo.wraptime.starttime+"至"
         +usdtCashVo.wraptime.endtime+",您今日剩余提款额度为 "+usdtCashVo.rest+"元";
        binding.tvNotice.setText(notice);
        binding.tvUserNameShow.setText(usdtCashVo.user.username);
        binding.tvWithdrawalTypeShow.setText("USDT提款");
        binding.tvWithdrawalAmountMethod.setText(usdtCashVo.channel_list.get(0).title);//设置收款USDT账户
        binding.tvWithdrawalAmountShow.setText(usdtCashVo.user.availablebalance);
        String temp = usdtCashVo.usdtinfo.get(0).min_money+"元,最高"+usdtCashVo.usdtinfo.get(0).max_money+"元" ;
        binding.tvWithdrawalTypeShow1.setText(temp);
        binding.tvInfoExchangeRateShow.setText(usdtCashVo.exchangerate);
        binding.tvCollectionUsdt.setText(usdtCashVo.usdtinfo.get(0).usdt_type + " " +usdtCashVo.usdtinfo.get(0).usdt_card);
        //注册监听
        initListener();

    }
    private void initListener()
    {
        binding.llUsdt.setOnClickListener(v->{
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            type = "USDT" ;
        });
        binding.tvVirtualUsdt.setOnClickListener(v -> {
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            type = "USDT" ;
        });
        //选中非USDT提款 提币地址更换为支持TRC20
        binding.llOtherUsdt.setOnClickListener(v->{
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            type = "TRC" ;
            //显示TRC地址
            String collection = usdtinfoTRC.get(0).usdt_type +" " + usdtinfoTRC.get(0).usdt_card;
            selectUsdtInfo = usdtinfoTRC.get(0);
            String temp = selectUsdtInfo.min_money+"元,最高"+selectUsdtInfo.max_money+"元" ;
            binding.tvWithdrawalTypeShow1.setText(temp);
            binding.tvCollectionUsdt.setText(collection);
        });
        binding.tvVirtualOther.setOnClickListener(v->{
            binding.llUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
            binding.llOtherUsdt.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
            type = "TRC" ;
            //显示TRC地址
            String collection = usdtinfoTRC.get(0).usdt_type +" " + usdtinfoTRC.get(0).usdt_card;
            selectUsdtInfo = usdtinfoTRC.get(0);
            String temp = selectUsdtInfo.min_money+"元,最高"+selectUsdtInfo.max_money+"元" ;
            binding.tvWithdrawalTypeShow1.setText(temp);
            binding.tvCollectionUsdt.setText(collection);
        });
        //提款金额输入框与提款金额显示View
        binding.etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                binding.tvInfoWithdrawalAmountShow.setText(s.toString());
                //换算到账个数 tv_info_actual_number_show
                String temp = s.toString() ;
                if (temp !=null && !TextUtils.isEmpty(temp))
                {
                    float f1 =  Float.parseFloat(temp) ;
                    float f2 =Float.parseFloat(usdtCashVo.exchangerate);
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.format(f1/f2);
                    binding.tvInfoActualNumberShow.setText( df.format(f1/f2));
                }
                else if (TextUtils.isEmpty(temp))
                {
                    binding.tvInfoActualNumberShow.setText("0");
                }
                else
                {
                    binding.tvInfoActualNumberShow.setText("0");
                }
            }
        });
        //点击USDT收款地址
        binding.tvCollectionUsdt.setOnClickListener(v ->{
            showAllCollectionDialog(type);
        });
        binding.llCollectionUsdtInput.setOnClickListener(v ->{
            showAllCollectionDialog(type);
        });
        //点击下一步
        binding.ivNext.setOnClickListener(v->{
            if (TextUtils.isEmpty(binding.etInputMoney.getText().toString()))
            {
                ToastUtils.showLong("请输入正确金额");
            }
           /* else if (StringUtils.isNumber(binding.tvInfoActualNumberShow.getText().toString().trim()))
            {
                ToastUtils.showLong("请输入正确金额2");
            }*/
            else if (Integer.valueOf(binding.etInputMoney.getText().toString()) > Integer.valueOf(selectUsdtInfo.max_money))
            {
                ToastUtils.showLong("请输入正确金额");
            }
            else if (Integer.valueOf(binding.etInputMoney.getText().toString())<Integer.valueOf(selectUsdtInfo.min_money))
            {
                ToastUtils.showLong("请输入正确金额");
            }
            else
            {
                requestWithdrawUSDT();
            }
        });
    }
    /** 刷新确认提款UI*/
    private void  refreshSecurityUI()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvConfirmWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llSetRequestView.setVisibility(View.GONE);
        binding.llVirtualConfirmView.setVisibility(View.VISIBLE);
        //usdtSecurityVo
        binding.tvConfirmUserNameShow.setText(usdtSecurityVo.user.username);
        binding.tvConfirmWithdrawalTypeShow.setText(usdtCashVo.user.availablebalance);
        binding.tvConfirmAmountShow.setText(usdtSecurityVo.usdt_type);
        binding.tvWithdrawalVirtualTypeShow.setText(usdtSecurityVo.usdt_type);

        binding.tvWithdrawalActualArrivalShow.setText(usdtSecurityVo.datas.arrive);
        binding.tvWithdrawalExchangeRateShow.setText(usdtSecurityVo.exchangerate);
        binding.tvWithdrawalAddressShow.setText(usdtSecurityVo.usdt_card);
        binding.tvWithdrawalHandlingFeeShow.setText(usdtSecurityVo.ourfee);
        //下一步
        binding.ivConfirmNext.setOnClickListener(v->{
            requestConfirmUSDT();
        });
        //上一步
        binding.ivConfirmPrevious.setOnClickListener(v->{

        });
    }
    /** 刷新完成申请UI*/
    private void  refreshConfirmUI()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvOverWithdrawalRequest.setTextColor(getContext().getColor(R.color.red));
        }
        binding.llVirtualConfirmView.setVisibility(View.GONE);
        binding.llOverApply.setVisibility(View.VISIBLE);
        if (usdtConfirmVo.msg_detail.equals("账户提款申请成功") && usdtConfirmVo.msg_type.equals("2"))
        {
            binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply);
        }
        else
        {
            binding.tvOverMessgae1.setText("账户提款申请失败");
            binding.tvOverDetail.setText(usdtConfirmVo.msg_detail);
            binding.ivOverApply.setBackgroundResource(R.mipmap.ic_over_apply_err);
        }
        //继续提现
        binding.ivContinueConfirmNext.setOnClickListener(v->{
            dismiss();
        });
        //关闭
        binding.ivContinueConfirmPrevious.setOnClickListener(v->{
            dismiss();
        });
    }

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)
   /** 显示USDT收款地址*/
    private void showAllCollectionDialog(String  type)
    {
        if (type.contains("TRC"))
        {
            showCollectionDialog(usdtinfoTRC);
        }
        else
        {
            showCollectionDialog(usdtCashVo.usdtinfo);
        }
    }
    private void showCollectionDialog(ArrayList<USDTCashVo.Usdtinfo>list)
    {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<USDTCashVo.Usdtinfo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }
            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                USDTCashVo.Usdtinfo vo = get(position);
                selectUsdtInfo = vo ;
                String showMessage = vo.usdt_type +" " + vo.usdt_card;

                binding2.tvwTitle.setText(showMessage);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvCollectionUsdt.setText(showMessage);
                    String temp = vo.min_money+"元,最高"+vo.max_money+"元" ;
                    CfLog.i("------onBindViewHolder = " +temp );
                    binding.tvWithdrawalTypeShow1.setText(temp);
                    ppw.dismiss();
                });
            }
        };
        adapter.clear();
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "请选择收款地址", adapter, 40));
        ppw.show();
    }

    /** 设置提款 请求 下一步*/
    private void requestWithdrawUSDT()
    {
        //et_input_money
        String money = binding.etInputMoney.getText().toString();
        HashMap<String , String> map = new HashMap<>();
        map.put("action","platwithdraw");
        map.put("channel_child" , "1");
        map.put("channel_typenum","1");
        map.put("check","");
        map.put("controller","security");
        map.put("flag","withdraw");
        map.put("money",money);
        if (type.equals("USDT"))
        {
            map.put("name","usdt");
        }
        else
        {
            map.put("name","TRC20_USDT");
        }
        String usdtid = null;
        if (selectUsdtInfo == null)
        {
            CfLog.i("requestWithdrawUSDT ==== selectUsdtInfo == null> ");
        }
        else
        {
            usdtid= selectUsdtInfo.id ;
        }

        map.put("usdtid",usdtid);
        String usdtType = channelInfo.type ;
        map.put("usdtType",usdtType);

       /* {
            "action": "platwithdraw",
         "channel_child": 1,
         "channel_typenum": 1,
         "check": "",
         "controller": "security",
         "flag": "withdraw",
         "money": 7,
         "name": "usdt",
         "usdtid": "3212",
         "usdtType": "2"
        }*/

        viewModel.postPlatWithdrawUSDT(map);
    }
    /** 设置提款 完成申请*/
    private void requestConfirmUSDT()
    {
        HashMap<String , String> map = new HashMap<>();
        map.put("action","platwithdraw");
        map.put("channel_child",null);
        map.put("check","");
        map.put("controller","security");
        map.put("flag","confirm");
        map.put("handing_fee","");
        map.put("money","");
        map.put("name","");
        map.put("play_source","");
        map.put("plot_id","");
        map.put("smscode","");
        map.put("smstype","");
        map.put("usdt_type","");
        map.put("usdtid","");

        viewModel.postConfirmWithdrawUSDT(map);

       /* {
            "action": "platwithdraw",
         "channel_child": null,
         "check": "",
         "controller": "security",
         "flag": "confirm",
         "handing_fee": "1.40",
         "money": 5.6,
         "name": "usdt",
         "play_source": 1,
         "plot_id": "54",
         "smscode": "",
         "smstype": "",
         "usdt_type": "2",
         "usdtid": 3212
        }*/

    }
}
