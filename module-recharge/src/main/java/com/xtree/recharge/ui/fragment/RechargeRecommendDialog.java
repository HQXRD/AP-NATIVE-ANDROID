package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.Constant;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcRecommendBinding;
import com.xtree.recharge.databinding.ItemAmountBinding;
import com.xtree.recharge.vo.RechargeVo;

import java.util.List;

public class RechargeRecommendDialog extends CenterPopupView {
    Context ctx;
    String msg; // 消息内容
    String tutorialUrl; // 充值教程
    List<RechargeVo> list; //推荐的充值渠道列表
    RechargeVo curRechargeVo; // 当前选中的
    ICallBack mCallBack; // 当前充值方式回调

    DialogRcRecommendBinding binding;

    public interface ICallBack {
        void onCallBack(RechargeVo vo);
    }

    public RechargeRecommendDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeRecommendDialog(@NonNull Context context, String msg, String tutorialUrl, List<RechargeVo> list, RechargeVo curRechargeVo, ICallBack mCallBack) {
        super(context);
        this.ctx = context;
        this.msg = msg;
        this.tutorialUrl = tutorialUrl;
        this.list = list;
        this.curRechargeVo = curRechargeVo;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogRcRecommendBinding.bind(findViewById(R.id.cl_root));
        initView();
    }

    private void initView() {

        if (curRechargeVo == null) {
            binding.tvwUseCur.setVisibility(View.INVISIBLE);
        } else {
            binding.tvwUseCur.setVisibility(View.VISIBLE);
        }

        binding.tvwMsg.setText(HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvwTutorial.setOnClickListener(v -> {
            // 充值教程
            if (!TextUtils.isEmpty(tutorialUrl)) {
                String title = ctx.getString(R.string.txt_recharge_tutorial);
                new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, tutorialUrl)).show();
            }
        });
        binding.tvwCs.setOnClickListener(v -> {
            String title = ctx.getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.tvwUseCur.setOnClickListener(v -> {
            mCallBack.onCallBack(curRechargeVo);
            dismiss();
        });
        binding.ivwClose.setOnClickListener(v -> binding.tvwUseCur.performClick());

        CachedAutoRefreshAdapter<RechargeVo> mAdapter = new CachedAutoRefreshAdapter<RechargeVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_amount, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

                ItemAmountBinding binding2 = ItemAmountBinding.bind(holder.itemView);
                RechargeVo vo = get(position);
                binding2.tvwTitle.setLines(3); // 有些2行,有些1行
                binding2.tvwTitle.setText(vo.title);

                binding2.tvwTitle.setOnClickListener(v -> {
                    mCallBack.onCallBack(vo);
                    dismiss();
                });
            }
        };

        binding.rcvMain.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rcvMain.setAdapter(mAdapter);
        mAdapter.addAll(list);

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_recommend;
    }

}
