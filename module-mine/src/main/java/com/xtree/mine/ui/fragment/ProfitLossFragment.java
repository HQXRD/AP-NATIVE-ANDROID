package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentReportProfitBinding;
import com.xtree.mine.databinding.ItemReportProfitLossBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.OrderTypeVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.ProfitLossVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 盈亏报表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_PROFIT_LOSS)
public class ProfitLossFragment extends BaseFragment<FragmentReportProfitBinding, ReportViewModel> {

    CachedAutoRefreshAdapter<ProfitLossVo> mAdapter; // 主列表
    int curPage = 0;
    ProfitLossReportVo mReportVo;

    List<FilterView.IBaseVo> listType = new ArrayList<>();

    private String starttime;
    private String endtime;
    private String typeId = "0";
    //private String status = "0";
    private String userName = "";

    BasePopupView ppw;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvwTitle.setText(R.string.txt_report_profit); //
        binding.fvMain.setData(listType);
        binding.fvMain.setVisibility(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        binding.fvMain.setTypeTitle(getString(R.string.txt_bt_platform), null, null);
        binding.fvMain.setDayStart(binding.fvMain.getEndDate()); // 开始时
        userName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME, "");
        binding.fvMain.setDefEdit(getString(R.string.txt_user_name), "", userName);
        binding.refreshLayout.setEnableLoadMore(false); // 不带分页,不加载更多
        binding.refreshLayout.setEnableRefresh(false); // 不带分页,不下拉刷新
        //LoadingDialog.show(getContext());
        requestType();
        //requestData(1);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.fvMain.setQueryListener(v -> {
            LoadingDialog.show(getContext());
            curPage = 0;
            requestData(1);
        });

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            requestData(1);
        });
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            CfLog.i("****** load more");
            requestData(curPage + 1);
        });

        mAdapter = new CachedAutoRefreshAdapter<ProfitLossVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_report_profit_loss, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemReportProfitLossBinding binding2 = ItemReportProfitLossBinding.bind(holder.itemView);
                ProfitLossVo vo = get(position);

                binding2.tvwSbet.setText(vo.sbet);
                binding2.tvwSprize.setText(vo.sprize);
                binding2.tvwSeffectiveBet.setText(vo.seffective_bet);
                binding2.tvwSprofitLoss.setText(vo.sprofit_loss);
                binding2.tvwWages.setText(vo.wages);
                binding2.tvwSactivePrize.setText(vo.sactive_prize);

                binding2.tvwUserName.setText(vo.username);
                binding2.tvwDeposit.setText(vo.deposit);
                binding2.tvwFee.setText(vo.fee);
                binding2.tvwRegTime.setText(vo.registertime);
                binding2.tvwWithdraw.setText(vo.withdraw);
                binding2.tvwSpoint.setText(vo.spoint);
//                double tmp = Double.parseDouble(vo.sprofit_loss) + Double.parseDouble(vo.spoint)
//                        + Double.parseDouble(vo.wages);
//                String netProfit = String.valueOf(tmp);
                //String netProfit = new DecimalFormat("#.#").format(tmp); // 去掉小数点及后面
                binding2.tvwNetProfit.setText(vo.settlement); // 净输赢

                if (vo.username.equals(userName)) {
                    binding2.tvwMyself.setVisibility(View.VISIBLE);
                } else {
                    binding2.tvwMyself.setVisibility(View.INVISIBLE);
                }

                if (Double.parseDouble(vo.sprofit_loss) > 0) {
                    //binding2.tvwSprofitLoss.setText("+" + vo.sprofit_loss);
                    binding2.tvwSprofitLoss.setSelected(true); // +
                } else {
                    binding2.tvwSprofitLoss.setSelected(false); // -
                }

                binding2.tvwBtRecord.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", vo.userid);
                    bundle.putString("userName", vo.username);
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_BT_REPORT, bundle); // 投注记录
                });

                binding2.tvwUserName.setOnClickListener(v -> {
                    binding.fvMain.setEdit(binding2.tvwUserName.getText().toString());
                    LoadingDialog.show(getContext());
                    curPage = 0;
                    requestData(1);
                });
            }
        };
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();

        listType.add(new OrderTypeVo("", getString(R.string.txt_all)));
        listType.add(new OrderTypeVo("99", getString(R.string.txt_all_exclude_lottery)));

        //listStatus.add(new StatusVo(0, getString(R.string.txt_all)));
        //listStatus.add(new StatusVo(1, getString(R.string.txt_succ)));
        //listStatus.add(new StatusVo(2, getString(R.string.txt_fail)));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_profit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ReportViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ReportViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataGameType.observe(this, list -> {
            //listType.add(new ThirdGameTypeVo("0", getString(R.string.txt_all)));
            listType.addAll(list);
            binding.fvMain.setData(listType);
        });

        viewModel.liveDataProfitLoss.observe(this, vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            mReportVo = vo;
            curPage = vo.mobile_page.p;

            //int total_page = vo.mobile_page.total_page.equals("false") ? 0 : Integer.parseInt(vo.mobile_page.total_page);
            //if (vo.mobile_page.p < total_page) {
            //    binding.refreshLayout.setEnableLoadMore(true);
            //} else {
            //    binding.refreshLayout.setEnableLoadMore(false);
            //}

            //if (curPage == 1) {
            //    listType.clear();
            //    //listType.add(new OrderTypeVo(0, getString(R.string.txt_all)));
            //    listType.addAll(vo.fromgameArr);
            //
            //    binding.fvMain.reSetData(listType);
            //}

            //if (curPage == 1) {
            //    mAdapter.clear();
            //}
            mAdapter.clear(); // 不分页,直接清空
            mAdapter.addAll(vo.aProfitLoss);
            if (mAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
                binding.llBread.setVisibility(View.GONE);
                binding.llSum.llRoot.setVisibility(View.GONE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
                binding.llBread.setVisibility(View.VISIBLE);
                binding.llSum.llRoot.setVisibility(View.VISIBLE);
            }

            // 原先为Toast 修改成温馨提示
            if (vo.msg_type == 2) {
                ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", vo.message, true, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        ppw.dismiss();
                    }
                }));
                ppw.show();
            }

            // 本级
            if (vo.bread != null && !vo.bread.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                SpannableString spannableString = new SpannableString("");

                for (ProfitLossReportVo.UserVo t : vo.bread) {
                    sb.append(t.username).append(" > ");
                }

                if (sb.toString().endsWith(" > ")) {
                    sb.replace(sb.length() - 3, sb.length(), "");
                }

                spannableString = new SpannableString(sb.toString().trim());

                for (ProfitLossReportVo.UserVo t : vo.bread) {
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            binding.fvMain.setEdit(t.username);
                            LoadingDialog.show(getContext());
                            curPage = 0;
                            requestData(1);
                        }
                    };

                    int startIndex = sb.indexOf(t.username);
                    int endIndex = startIndex + t.username.length();

                    spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.textColor)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                binding.tvwBread.setText(spannableString);
                binding.tvwBread.setMovementMethod(LinkMovementMethod.getInstance());
            }

            // 本页合计
            if (vo.pageCount != null) {
                binding.llSum.tvwPgSbet.setText(vo.pageCount.sbet);
                binding.llSum.tvwPgSprize.setText(vo.pageCount.sprize);
                binding.llSum.tvwPgSprofitLoss.setText(vo.pageCount.sprofit_loss);
                binding.llSum.tvwPgFee.setText(vo.pageCount.fee);

                binding.llSum.tvwPgSpoint.setText(vo.pageCount.spoint);
                binding.llSum.tvwPgSeffectiveBet.setText(vo.pageCount.seffective_bet);
                binding.llSum.tvwPgWages.setText(vo.pageCount.wages);
                binding.llSum.tvwPgSactivePrize.setText(vo.pageCount.sactive_prize);

                binding.llSum.tvwPgNetProfit.setText(vo.pageCount.settlement); // 会员净输赢
            }

            // 总合计
            if (vo.totalCount != null) {
                binding.llSum.tvwSumSbet.setText(vo.totalCount.sbet);
                binding.llSum.tvwSumSprize.setText(vo.totalCount.sprize);
                binding.llSum.tvwSumSprofitLoss.setText(vo.totalCount.sprofit_loss);
                binding.llSum.tvwSumFee.setText(vo.totalCount.fee);

                binding.llSum.tvwSumSpoint.setText(vo.totalCount.spoint);
                binding.llSum.tvwSumSeffectiveBet.setText(vo.totalCount.seffective_bet);
                binding.llSum.tvwSumWages.setText(vo.totalCount.wages);
                binding.llSum.tvwSumSactivePrize.setText(vo.totalCount.sactive_prize);

                binding.llSum.tvwSumNetProfit.setText(vo.totalCount.settlement); // 会员净输赢
            }
        });

    }

    private void requestType() {
        viewModel.getThirdGameType();
    }

    private void requestData(int page) {
        CfLog.i();
        // /gameinfo/eprofitlossNew?starttime=2024-03-11&endtime=2024-03-12&username=DA1002&userid=&cid=&ordername=sbet&order=DESC&=&client=m
        starttime = binding.fvMain.getStartDate();
        endtime = binding.fvMain.getEndDate();
        typeId = binding.fvMain.getTypeId("");
        //status = binding.fvMain.getStatusId("0");
        userName = binding.fvMain.getEdit(""); // 用户输入一个不存在的用户名,查询,再清空,再查询
        if (userName.isEmpty()) {
            userName = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME, "");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("cid", typeId);
        map.put("username", userName); // ***********
        map.put("userid", ""); // ***********
        map.put("ordername", "sbet");
        map.put("order", "DESC");
        //map.put("status", status);
        //map.put("p", "" + page);
        //map.put("pn", "20");
        map.put("client", "m");

        CfLog.i(map.toString());
        viewModel.getProfitLoss(map);
    }

}