package com.xtree.mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentSecurityVerifyChooseBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 安全验证-验证方式
 */
@Route(path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE)
public class SecurityVerifyChooseFragment extends BaseFragment<FragmentSecurityVerifyChooseBinding, VerifyViewModel> {
    private static final String ARG_TYPE = "type";

    private ProfileVo mProfileVo;
    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)
    BasePopupView ppw2 = null; // 居中弹窗
    String typeName;
    String type = "";

    public SecurityVerifyChooseFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_security_verify_choose;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VerifyViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(VerifyViewModel.class);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwChoose.setOnClickListener(v -> showDialog());

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

        binding.ivwMsg.setOnClickListener(v -> {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });
    }

    @Override
    public void initData() {
        typeName = getArguments().getString(ARG_TYPE);
        type = getArguments().getString(ARG_TYPE);
        CfLog.i("****** typeName: " + typeName);

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    private void setView() {
        CfLog.d("phone: " + mProfileVo.is_binding_phone + ", email: " + mProfileVo.is_binding_email);
        if (mProfileVo.is_binding_email && mProfileVo.is_binding_phone) {
            CfLog.d("******");
        } else if (mProfileVo.is_binding_email) {
            binding.llTop.setVisibility(View.INVISIBLE);
            Fragment mFragment = BindEmailFragment.newInstance(type, "");
            changeView(mFragment);
        } else if (mProfileVo.is_binding_phone) {
            binding.llTop.setVisibility(View.INVISIBLE);
            Fragment mFragment = BindPhoneFragment.newInstance(type, "");
            changeView(mFragment);
        } else {
            ppw2 = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", getResources().getString(R.string.txt_no_binding), "绑定手机", "绑定邮箱", new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    startBinding(Constant.BIND_PHONE);
                    getActivity().finish();
                    ppw2.dismiss();
                }

                @Override
                public void onClickRight() {
                    startBinding(Constant.BIND_EMAIL);
                    getActivity().finish();
                    ppw2.dismiss();
                }
            })).show();
        }
    }

    private void showDialog() {

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<String>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                //holder.itemView.findViewById(R.id.tvw_title);
                binding2 = ItemTextBinding.bind(holder.itemView);
                String txt = get(position);
                binding2.tvwTitle.setText(txt);

                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwChoose.setText(txt);
                    if (0 == position) {
                        CfLog.i("******");
                        goPhone();
                    } else {
                        CfLog.i("******");
                        goEmail();
                    }
                    ppw.dismiss();
                });

            }
        };

        ArrayList<String> list = new ArrayList();
        list.add(getString(R.string.txt_phone_verify));
        list.add(getString(R.string.txt_email_verify));
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "", adapter));
        ppw.show();
    }

    private void goPhone() {
        initData();

        if (mProfileVo.is_binding_phone) {
            if (Constant.BIND_PHONE.equals(typeName) || Constant.VERIFY_BIND_PHONE.equals(typeName)) {
                type = Constant.UPDATE_PHONE;
            } else if (Constant.BIND_EMAIL.equals(typeName) || Constant.UPDATE_EMAIL.equals(typeName)) {
                type = Constant.VERIFY_BIND_EMAIL;
            }

        } else {
            if (!mProfileVo.is_binding_email) {
                type = Constant.BIND_PHONE;
            } else {
                type = Constant.VERIFY_BIND_PHONE;
            }
        }

        Fragment mFragment = BindPhoneFragment.newInstance(type, "");
        changeView(mFragment);
    }

    private void goEmail() {
        initData();

        if (mProfileVo.is_binding_email) {
            if (Constant.BIND_EMAIL.equals(typeName) || Constant.VERIFY_BIND_EMAIL.equals(typeName)) {
                type = Constant.UPDATE_EMAIL;
            } else if (Constant.BIND_PHONE.equals(typeName) || Constant.UPDATE_PHONE.equals(typeName)) {
                type = Constant.VERIFY_BIND_PHONE;
            }

        } else {
            if (!mProfileVo.is_binding_phone) {
                type = Constant.BIND_EMAIL;
            } else {
                type = Constant.VERIFY_BIND_EMAIL;
            }
        }

        Fragment mFragment = BindEmailFragment.newInstance(type, "");
        changeView(mFragment);
    }

    private void changeView(Fragment mFragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main, mFragment);
        ft.commit();
    }

    private void startBinding(String verify) {
        Bundle bundle = new Bundle();
        bundle.putString("type", verify);
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        getContext().startActivity(intent);
    }

}