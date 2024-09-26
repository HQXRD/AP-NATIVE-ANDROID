package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBonusReportBinding;
import com.xtree.mine.ui.dialog.BonusInfoDialog;
import com.xtree.mine.ui.dialog.BonusTipsDialog;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BounsReportVo;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 代理服务費
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BONUS_REPORT)
public class BonusReportFragment extends BaseFragment<FragmentBonusReportBinding, MineViewModel> {
    private List<FilterView.IBaseVo> listType = new ArrayList<>();
    ICallBack mCallBackType;
    BasePopupView ppw = null;
    private int level;
    private String starttime;
    private String endtime;
    private String status = "0";
    private int curPage = 0;
    private BounsReportVo mBounsReportVo;

    private BasePopupView ppwInfo = null;
    BonusAdapter mBonusAdapter;

    public interface IBaseVo {
        String getShowName();

        String getShowId();
    }

    public interface ICallBack {
        void onTypeChanged(FilterView.IBaseVo vo);

    }

    @Override
    public void initView() {
        level = SPUtils.getInstance().getInt(SPKeyGlobal.USER_LEVEL);
        if (level > 2) {
            binding.tvwUnder.setVisibility(View.GONE);
            binding.tvwUnderTitle.setVisibility(View.GONE);
        }

        Calendar calendar = Calendar.getInstance();
        String txtDayStart = TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd");
        String txtDayEnd = TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        binding.tvwStarttime.setText(txtDayStart);
        binding.tvwEndtime.setText(txtDayEnd);

        listType.add(new StatusVo("", "全部"));
        listType.add(new StatusVo(1, "已发放"));
        listType.add(new StatusVo(-1, "未发放"));
        listType.add(new StatusVo(2, "无抽水"));

        binding.tvwStatus.setText(listType.get(0).getShowName());
        binding.tvwStatus.setTag(listType.get(0));

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwInfo.setOnClickListener(v -> {
            BonusTipsDialog dialog = new BonusTipsDialog(getContext());
            new XPopup.Builder(getContext()).asCustom(dialog).show();
        });

        binding.tvwServerFee.setOnClickListener(v -> {
            BonusInfoDialog dialog = new BonusInfoDialog(getContext());
            new XPopup.Builder(getContext()).asCustom(dialog).show();
        });

        binding.tvwStarttime.setOnClickListener(v -> showDatePicker(binding.tvwStarttime, getContext().getString(com.xtree.base.R.string.txt_date_start)));

        binding.tvwEndtime.setOnClickListener(v -> showDatePicker(binding.tvwEndtime, getContext().getString(com.xtree.base.R.string.txt_date_end)));

        binding.tvwStatus.setOnClickListener(v -> showDialog(binding.tvwStatus, binding.tvwStatus.getText(), listType, mCallBackType));

        binding.btnCheck.setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            LoadingDialog.show(getActivity());
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

        mBonusAdapter = new BonusAdapter(getContext());
        binding.rcvMain.setAdapter(mBonusAdapter);
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bonus_report;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataBonusReport.observe(getViewLifecycleOwner(), vo -> {
            CfLog.i("******");
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();
            if (vo == null) {
                binding.refreshLayout.setEnableLoadMore(false);
                return;
            }

            mBounsReportVo = vo;
            curPage = vo.pageCount.p;

            int total_page = vo.pageCount.total_page;
            if (vo.pageCount.p < total_page) {
                binding.refreshLayout.setEnableLoadMore(true);
            } else {
                binding.refreshLayout.setEnableLoadMore(false);
            }

            if (curPage == 0) {
                mBonusAdapter.clear();
            }
            if (vo.list != null) {
                mBonusAdapter.addAll(vo.list);
            }
            if (mBonusAdapter.isEmpty()) {
                binding.tvwNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvwNoData.setVisibility(View.GONE);
            }

            binding.tvwTotal.setText(vo.totalCount.totalPrice);
            binding.tvwOwn.setText(vo.totalCount.myselfPrice);
            binding.tvwUnder.setText(vo.totalCount.childPrice);
        });
    }

    private void showDatePicker(TextView tvw, String title) {
        CfLog.i("****** ");
        new XPopup.Builder(getContext())
                .asCustom(DateTimePickerDialog.newInstance(getContext(), title, 3, date -> tvw.setText(date)))
                .show();
    }

    private void showDialog(TextView tvw, CharSequence title, List<FilterView.IBaseVo> list, ICallBack callBack) {
        CfLog.i("****** " + title);
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<FilterView.IBaseVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemTextBinding binding = ItemTextBinding.bind(holder.itemView);
                FilterView.IBaseVo vo = get(position);
                binding.tvwTitle.setText(vo.getShowName());
                binding.tvwTitle.setOnClickListener(v -> {
                    CfLog.i(vo.toString());
                    tvw.setText(vo.getShowName());
                    tvw.setTag(vo);
                    if (callBack != null) {
                        callBack.onTypeChanged(vo);
                    }
                    ppw.dismiss();
                });

            }
        };
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title.toString(), adapter));
        ppw.show();
    }

    private void requestData(int page) {
        starttime = binding.tvwStarttime.getText().toString();
        endtime = binding.tvwEndtime.getText().toString();
        status = ((FilterView.IBaseVo) binding.tvwStatus.getTag()).getShowId();
        ;

        HashMap<String, String> map = new HashMap<>();
        map.put("start_date", starttime);
        map.put("end_date", endtime);
        map.put("status", status);
        map.put("p", "" + page);
        map.put("pn", "10");
        map.put("client", "m");

        viewModel.getBonusReport(map);

    }
}
