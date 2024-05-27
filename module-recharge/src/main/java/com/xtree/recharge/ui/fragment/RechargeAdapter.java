package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.ItemRechargeBinding;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Arrays;
import java.util.List;

public class RechargeAdapter extends CachedAutoRefreshAdapter<RechargeVo> {

    public List<String> bankCodeList = Arrays.asList("abc", "bcm", "ccb", "cmb", "cmb2", "icbc");
    public List<String> jsCode = Arrays.asList("onepayfix", "onepayfix2", "onepayfix3");
    public List<String> szrmb = Arrays.asList("ecny", "ecnyhqppay", "jxpayecny1", "jxpayecny2", "jxpayecny3");
    public List<String> ucybf = Arrays.asList("cryptohqppay2", "cryptotrchqppay2");
    public static final List<String> hqppaybank = Arrays.asList(
            "hqppaybank1", "hqppaybank1_sm", "hqppaybank2", "hqppaybank2_sm",
            "hqppaybank3", "hqppayunionpayscan", "hqppayunionpayscan1",
            "hqppayunionpayscan2", "hqppayunionpayscan2_sm"
    );
    public static final List<String> hqppayJx = Arrays.asList(
            "hqppay", "hqppay1", "hqppay2", "hqppay3", "hqppay4",
            "juxinpay", "juxinpay1", "juxinpay2", "jxpayjd", "jxpayjd1",
            "jxpaykj", "jxpaykj1", "jxpaytz", "jxpayyl", "jxpayyl1"
    );
    public static final List<String> hqppay6 = Arrays.asList("hqppay6"); // UC天下支付
    public static final List<String> onepay = Arrays.asList(
            "onepay", "onepay1", "onepay2", "onepay3", "onepay4",
            "onepayfix4", "onepayfix5", "onepay5", "onepay6",
            "onepay7", "onepay8"
    );
    Context ctx;

    ICallBack mCallBack;

    ItemRechargeBinding binding;

    View curView;
    String curBid; // 当前选中的充值渠道bid, (解决网络请求的数据回来,选中的渠道被取消问题)

    public interface ICallBack {
        void onClick(RechargeVo vo);
    }

    public RechargeAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_recharge, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

        RechargeVo vo = get(position);
        CfLog.d(vo.toInfo());
        binding = ItemRechargeBinding.bind(holder.itemView);
        binding.tvwTitle.setText(vo.title);
        binding.tvwTitle.setTag(vo.bid); // 弹窗时会用到
        if (vo.bid.equals(curBid)) {
            curView = binding.tvwTitle;
            curView.setSelected(true);
        }

        // 存款加赠5.00%
        if (vo.depositfee_disabled && !TextUtils.isEmpty(vo.depositfee_rate)) {
            binding.tvwTitle.setText(vo.title + "\n");
            String txt = String.format(ctx.getString(R.string.txt_deposit_fee), vo.depositfee_rate);
            binding.tvwDepRate.setText(txt);
            binding.tvwDepRate.setVisibility(View.VISIBLE);
        } else {
            binding.tvwDepRate.setVisibility(View.GONE);
        }

        int visible = vo.firemark == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwHot.setVisibility(visible); // HOT
        int visible2 = vo.recommend == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwRcmd.setVisibility(visible2); // 推荐
        //binding.tvwTitle.setCompoundDrawables();
        //Drawable dr = ctx.getDrawable(R.drawable.rc_ic_pmt_selector);
        //dr.setLevel(Integer.parseInt(vo.bid));
        getImageLevel(vo, binding.tvwTitle);
        //binding.tvwTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, dr, null, null);
        //holder.itemView.setOnClickListener(v -> binding.tvwTitle.performClick());
        binding.tvwTitle.setOnClickListener(v -> {

            if (curView != null) {
                curView.setSelected(false);
            }

            v.setSelected(true);
            curView = v;
            curBid = vo.bid;
            mCallBack.onClick(vo);
        });
    }

    private void getImageLevel(RechargeVo bank, TextView tvwTitle) {
        switch (bank.paycode) {
            case "hqppaytopay":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_topay, R.mipmap.rc_ic_pmt_topay_selected, tvwTitle);
                break;
            case "hqppayokpay":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_okpay, R.mipmap.rc_ic_pmt_okpay_selected, tvwTitle);
                break;
            case "hqppaygopay":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_gopay, R.mipmap.rc_ic_pmt_gopay_selected, tvwTitle);
                break;
            case "ebpay":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_ebkc, R.mipmap.rc_ic_pmt_ebkc_selected, tvwTitle);
                break;
            case "abc":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_abc, R.mipmap.rc_ic_pmt_abc_selected, tvwTitle);
                break;
            case "bcm":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_bcm, R.mipmap.rc_ic_pmt_bcm_selected, tvwTitle);
                break;
            case "ccb":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_ccb, R.mipmap.rc_ic_pmt_ccb_selected, tvwTitle);
                break;
            case "cmb":
            case "cmb2":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_cmb, R.mipmap.rc_ic_pmt_cmb_selected, tvwTitle);
                break;
            case "icbc":
                addSelectorFromDrawable(R.mipmap.rc_ic_pmt_icbc, R.mipmap.rc_ic_pmt_icbc_selected, tvwTitle);
                break;
            default:
                if (jsCode.contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_jisuchongzhi, R.mipmap.rc_ic_pmt_jisuchongzhi_selected, tvwTitle);
                } else if (szrmb.contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_szrmb, R.mipmap.rc_ic_pmt_szrmb_selected, tvwTitle);
                } else if ((bank.title.contains("USDT") || bank.title.contains("TRC") || bank.title.contains("ERC")) && !ucybf.contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_usdt, R.mipmap.rc_ic_pmt_usdt_selected, tvwTitle);
                } else if (bank.title.contains("微信")) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_wx, R.mipmap.rc_ic_pmt_wx_selected, tvwTitle);
                } else if (bank.title.contains("支付宝")) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_zfb, R.mipmap.rc_ic_pmt_zfb_selected, tvwTitle);
                } else if (bank.title.contains("QQ")) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_qq, R.mipmap.rc_ic_pmt_qq_selected, tvwTitle);
                } else if (bank.title.contains("客服代充")) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_happy, R.mipmap.rc_ic_pmt_happy_selected, tvwTitle);
                } else if ("hiwallet".contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_cn, R.mipmap.rc_ic_pmt_cn_selected, tvwTitle);
                } else if (ucybf.contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_ucybf, R.mipmap.rc_ic_pmt_ucybf_selected, tvwTitle);
                }
                //} else if (hqppaybank.contains(bank.paycode)) {
                //    model = new XCDepositBankIcon(R.drawable.xc_hqppaybank, R.drawable.xc_hqppaybank_active);
                //} else if (hqppayJx.contains(bank.paycode)) {
                //    model = new XCDepositBankIcon(R.drawable.xc_hqppayJx, R.drawable.xc_hqppayJx_active);
                //} else if (hqppay6.contains(bank.paycode)) {
                //    model = new XCDepositBankIcon(R.drawable.xc_hqppay6, R.drawable.xc_hqppay6_active);
                //}
                else if (onepay.contains(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_yhk, R.mipmap.rc_ic_pmt_yhk_selected, tvwTitle);
                } else if (("hqppaympay").equals(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_mpay, R.mipmap.rc_ic_pmt_mpay_selected, tvwTitle);
                } else if (("hqppaygobao").equals(bank.paycode)) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_gobao, R.mipmap.rc_ic_pmt_gobao_selected, tvwTitle);
                } else if (bank.paycode.equals("manual")) {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_manual, R.mipmap.rc_ic_pmt_manual_selected, tvwTitle);
                } else {
                    addSelectorFromDrawable(R.mipmap.rc_ic_pmt_happy, R.mipmap.rc_ic_pmt_happy_selected, tvwTitle);
                }
                break;
        }

    }

    public void addSelectorFromDrawable(int idNormal, int idPress, TextView tv) {
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = ContextCompat.getDrawable(ctx, idNormal);
        Drawable press = ContextCompat.getDrawable(ctx, idPress);
        drawable.addState(new int[]{android.R.attr.state_pressed}, press);
        drawable.addState(new int[]{-android.R.attr.state_pressed}, normal);
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
    }

}
