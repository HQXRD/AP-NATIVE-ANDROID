package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogTransferMoneyBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

@Route(path = RouterFragmentPath.Mine.PAGER_MEMBER_TRANSFER)
public class TransferMoneyDialog extends BaseFragment<DialogTransferMoneyBinding, MineViewModel> {
    private static final String ARG_USERNAME = "userName";
    private static final String ARG_USERID = "userId";
    private static final String ARG_VERIFY = "verify";
    //LifecycleOwner owner;
    //DialogTransferMoneyBinding binding;
    //MineViewModel viewModel;
    String checkCode;
    String username;
    String userid;
    BasePopupView ppw = null;
    ICallBack mCallBackType;
    ItemTextBinding binding2;
    List<String> list = new ArrayList<>();

    public interface ICallBack {
        void onTypeChanged(String vo);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.liveDataSendMoney.observe(this, vo -> {
            if (vo.msg_type.equals("3")) {
                viewModel.getBalance();
            }
            ToastUtils.showLong(vo.message);
            getActivity().finish();
        });

        viewModel.liveDataBalance.observe(this, vo -> {
            binding.tvwUserBalance.setText(vo.balance);
        });
    }

    @Override
    public void initData() {
        super.initData();
        list.add("充值转账");
        list.add("活动金转账");
        viewModel.getBalance();
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            userid = getArguments().getString(ARG_USERID);
            checkCode = getArguments().getString(ARG_VERIFY);
        }

        binding.tvwUserAccount.setText(username);

        binding.tvwUserMoneyKind.setOnClickListener(v -> showDialog(binding.tvwUserMoneyKind, binding.tvwUserMoneyKindTitle.getText(), list, mCallBackType));
        binding.ivwClose.setOnClickListener(v -> getActivity().finish());
        binding.btnCancel.setOnClickListener(v -> getActivity().finish());
        binding.btnConfirm.setOnClickListener(v -> {
            if (binding.etUserMoney.getText().toString().isEmpty() || Double.parseDouble(binding.etUserMoney.getText().toString()) == 0.0) {
                ToastUtils.showLong(R.string.text_null_or_zero);
                return;
            }

            String content = String.format(getContext().getString(R.string.txt_check_transfer), binding.etUserMoney.getText().toString());
            String txtRight = getContext().getString(R.string.text_confirm);
            String txtLeft = getContext().getString(R.string.text_cancel);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", content, txtLeft, txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    checkPassword();
                    ppw.dismiss();
                }
            }));
            ppw.show();
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_transfer_money;
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

    private void checkPassword() {
        String money = binding.etUserMoney.getText().toString();

        LoadingDialog.show(getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "confirm");
        map.put("check", checkCode);
        map.put("money", money);
        map.put("uid", userid);
        map.put("nonce", UuidUtil.getID16());
        if (list.get(0).equals(binding.tvwUserMoneyKind.getText().toString())) {
            map.put("ordertype", "0");
        } else if (list.get(1).equals(binding.tvwUserMoneyKind.getText().toString())) {
            map.put("ordertype", "367");
        }

        viewModel.sendMoney(map);
    }

    private void showDialog(TextView tvw, CharSequence title, List<String> list, ICallBack callBack) {
        CfLog.i("****** " + title);
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<String>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(com.xtree.base.R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                String vo = get(position);
                binding2.tvwTitle.setText(vo);
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i(vo.toString());
                    tvw.setText(vo);
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
}