package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindCardAddBinding;
import com.xtree.mine.ui.dialog.BankSelectDialog;
import com.xtree.mine.ui.viewmodel.BindCardViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.UserBankConfirmVo;
import com.xtree.mine.vo.UserBankProvinceVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 绑定银行卡
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_CARD_ADD)
public class BindCardAddFragment extends BaseFragment<FragmentBindCardAddBinding, BindCardViewModel> {
    private static final String ARG_TOKEN_SIGN = "tokenSign";
    private static final String ARG_MARK = "mark";
    private final String controller = "security";
    private final String action = "adduserbank";

    private String mark = "bindcard";
    private String tokenSign;
    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    UserBankProvinceVo mUserBankInfoVo;
    UserBankProvinceVo.BankInfoVo mBankInfoVo;
    UserBankProvinceVo.AreaVo mProvince;
    UserBankProvinceVo.AreaVo mCity;
    UserBankConfirmVo mConfirmVo;
    List<UserBankProvinceVo.AreaVo> listCity = new ArrayList<>();

    public BindCardAddFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBankAndProvinceList();
    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            } else {
                getActivity().finish();
            }
        });

        binding.tvwChooseBank.setOnClickListener(v -> showChooseBank());
        binding.tvwChooseProvince.setOnClickListener(v -> showChooseProvince());
        binding.tvwChooseCity.setOnClickListener(v -> showChooseCity());

        binding.ivwNext.setOnClickListener(v -> doNext());
        //新增返回上一步操作
        binding.tvwBindBack.setOnClickListener(v -> {
            getActivity().finish();
        });
        binding.tvwSubmit.setOnClickListener(v -> doSubmit());

        binding.tvwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            mark = getArguments().getString(ARG_MARK);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_card_add;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindCardViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindCardViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataBankProvinceList.observe(this, vo -> {
            CfLog.i("******");
            mUserBankInfoVo = vo;
            if (vo.accountname != null && !vo.accountname.isEmpty()) {
                binding.edtName.setText(vo.accountname);
                binding.edtName.setEnabled(false);
            }
        });

        viewModel.liveDataCityList.observe(this, list -> {
            CfLog.i("******");
            listCity.clear();
            listCity.addAll(list);
        });
        viewModel.liveDataBindCardCheck.observe(this, vo -> {
            CfLog.i("******");
            mConfirmVo = vo;
            setConfirmView();
        });
        viewModel.liveDataBindCardResult.observe(this, vo -> {
            CfLog.i("******");
            //getActivity().finish();
            if (vo.msg_type == 3){
                //绑定成功 "绑定成功！温馨提示：新绑定卡需0小时后才能提现"
                binding.llConfirm.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.VISIBLE);
                binding.ivAwIcon.setImageResource(R.mipmap.bind_success);
                binding.tvBindMsg.setVisibility(View.GONE);
                binding.tvType.setText(getContext().getString(R.string.txt_confirm));
                binding.tvMsg.setText(getContext().getString(R.string.txt_bind_succ));
                binding.tvType.setOnClickListener(v -> {
                    viewModel.getProfile();
                    requireActivity().finish();
                });

            } else if (vo.msg_type == 1 ) {
                //"您提交的银行卡号格式不正确，请核对后重新提交！"
                binding.llConfirm.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.VISIBLE);
                binding.ivAwIcon.setImageResource(R.mipmap.bind_fail);
                binding.tvBindMsg.setVisibility(View.VISIBLE);
                binding.tvMsg.setVisibility(View.VISIBLE);
                binding.tvMsg.setText(getContext().getString(R.string.txt_bind_fail));
                if (vo.message !=null && !TextUtils.isEmpty(vo.message)){
                    binding.tvBindMsg.setText(vo.message);
                }else{
                    binding.tvBindMsg.setVisibility(View.INVISIBLE);
                }

                binding.tvType.setText(getContext().getString(R.string.txt_mine_rebind_usdt));

                binding.tvType.setOnClickListener(v -> {

                    requireActivity().finish();
                });

            }
        });
        viewModel.liveDataProfile.observe(this, vo -> {
            CfLog.i("******");
            getActivity().finish();
        });

    }

    private void setConfirmView() {
        binding.llAdd.setVisibility(View.GONE);
        binding.llConfirm.setVisibility(View.VISIBLE);

        binding.tvwNickname.setText(mConfirmVo.nickname);
        binding.tvwBank.setText(mConfirmVo.bank);
        binding.tvwBankProvince.setText(mConfirmVo.province);
        binding.tvwBankCity.setText(mConfirmVo.city);
        binding.tvwBranch.setText(mConfirmVo.branch);
        binding.tvwRealName.setText(mConfirmVo.account_name);
        binding.tvwAcc.setText(mConfirmVo.account);

    }

    private void getBankAndProvinceList() {
        HashMap map = new HashMap();
        map.put("controller", controller);
        map.put("action", action);
        map.put("client", "m");
        map.put("mark", mark);
        map.put("check", tokenSign);
        viewModel.getBankProvinceList(map);
    }

    private void getCityList() {
        HashMap map = new HashMap();
        map.put("province", mProvince.id + "#" + mProvince.name); // "22#云南",
        map.put("flag", "getCity");
        viewModel.getCityList(map);
    }

    private void doNext() {

        String account = binding.edtAcc.getText().toString().trim();
        String account_name = binding.edtName.getText().toString().trim();
        String branch = binding.edtBranch.getText().toString().trim();

        if (account.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_bank_num);
            return;
        }
        if (account_name.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_account_name);
            return;
        }

        if (mBankInfoVo == null) {
            ToastUtils.showLong(R.string.txt_choose_bank);
            return;
        }
        if (mProvince == null) {
            ToastUtils.showLong(R.string.txt_choose_province);
            return;
        }
        if (mCity == null) {
            ToastUtils.showLong(R.string.txt_choose_city);
            return;
        }
        if (branch.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_branch);
            return;
        }

        HashMap queryMap = new HashMap();
        queryMap.put("controller", controller);
        queryMap.put("action", action);
        queryMap.put("client", "m");
        queryMap.put("mark", mark);
        queryMap.put("check", tokenSign);

        HashMap map = new HashMap();
        map.put("flag", "add");
        map.put("controller", controller);
        map.put("action", action);
        //map.put("oldid", "");
        map.put("entrancetype", "0");
        map.put("account", account);  // "4500***1234"
        map.put("account_name", account_name); // "姓名"
        map.put("bank", mBankInfoVo.bank_id + "#" + mBankInfoVo.bank_name); // "111#上海银行",
        map.put("bank_id", mBankInfoVo.bank_id); // "111",
        map.put("bank_name", mBankInfoVo.bank_name); // "上海银行",
        map.put("province", mProvince.id + "#" + mProvince.name); // "22#云南",
        map.put("province_id", mProvince.id); // "22",
        map.put("province_name", mProvince.name); // "云南",
        map.put("city", mCity.id + "#" + mCity.name); // "23#丽江",
        map.put("city_id", mCity.id); // "23",
        map.put("city_name", mCity.name); // "丽江",
        map.put("branch", branch); // "丽江支行",
        //map.put("submit", "下一步"); // "下一步",
        map.put("nonce", UuidUtil.getID16());
        viewModel.doBindCardByCheck(queryMap, map);
    }

    private void doSubmit() {

        HashMap queryMap = new HashMap();
        queryMap.put("controller", controller);
        queryMap.put("action", action);
        queryMap.put("client", "m");
        queryMap.put("mark", mark);
        queryMap.put("check", tokenSign);

        HashMap map = new HashMap();
        map.put("flag", "confirm");
        map.put("controller", controller);
        map.put("action", action);
        map.put("nickname", "");
        map.put("bank_id", mConfirmVo.bank_id); // "111",
        map.put("bank", mConfirmVo.bank); // "上海银行",
        map.put("province_id", mConfirmVo.province_id); // "22",
        map.put("province", mConfirmVo.province); // "云南",
        map.put("city_id", mConfirmVo.city_id); // "23",
        map.put("city", mConfirmVo.city); // "丽江",
        map.put("branch", mConfirmVo.branch); // "丽江支行",
        map.put("account_name", mConfirmVo.account_name); // "姓名"
        map.put("account", mConfirmVo.account);  // "4500***1234"
        map.put("oldid", "");
        map.put("entrancetype", "0");

        map.put("nonce", UuidUtil.getID16());
        viewModel.doBindCardBySubmit(queryMap, map);
    }

    private void showChooseBank() {
        if (mUserBankInfoVo == null || mUserBankInfoVo.banklist == null) {
            return;
        }
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<UserBankProvinceVo.BankInfoVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                UserBankProvinceVo.BankInfoVo vo = get(position);
                binding2.tvwTitle.setText(vo.bank_name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + vo);
                    mBankInfoVo = vo;
                    binding.tvwChooseBank.setText(vo.bank_name);
                    ppw.dismiss();
                });

            }
        };

        ArrayList<UserBankProvinceVo.BankInfoVo> list = new ArrayList<>();
        list.addAll(mUserBankInfoVo.banklist);
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(new BankSelectDialog(getContext(), getString(R.string.txt_choose_bank), adapter, 70));
        ppw.show();
    }

    private void showChooseProvince() {
        if (mUserBankInfoVo == null || mUserBankInfoVo.provincelist == null) {
            return;
        }
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<UserBankProvinceVo.AreaVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                UserBankProvinceVo.AreaVo vo = get(position);
                binding2.tvwTitle.setText(vo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + vo);
                    if (mProvince == null || (mProvince != null && !mProvince.id.equals(vo.id))) {
                        mProvince = vo;
                        mCity = null;
                        listCity.clear();
                        binding.tvwChooseCity.setText("");
                        binding.tvwChooseProvince.setText(vo.name);
                        getCityList(); // 查询城市列表
                    }

                    ppw.dismiss();
                });

            }
        };

        adapter.addAll(mUserBankInfoVo.provincelist);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getString(R.string.txt_choose_province), adapter, 70));
        ppw.show();
    }

    private void showChooseCity() {
        if (mUserBankInfoVo == null || listCity.isEmpty()) {
            return;
        }
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<UserBankProvinceVo.AreaVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                UserBankProvinceVo.AreaVo vo = get(position);
                binding2.tvwTitle.setText(vo.name);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i("****** " + vo);
                    mCity = vo;
                    binding.tvwChooseCity.setText(vo.name);
                    ppw.dismiss();
                });

            }
        };

        adapter.addAll(listCity);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getString(R.string.txt_choose_city), adapter, 70));
        ppw.show();
    }

}