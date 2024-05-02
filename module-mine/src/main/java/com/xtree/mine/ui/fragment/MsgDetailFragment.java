package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMsgDetailBinding;
import com.xtree.mine.ui.viewmodel.MsgViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Mine.PAGER_MSG_DETAIL)
public class MsgDetailFragment extends BaseFragment<FragmentMsgDetailBinding, MsgViewModel> {
    public static String MSG_DETAIL = "msg_detail";
    public static String MSG_PERSON_DETAIL = "msg_person_detail";

    private String checkMsgId;
    private String checkMsgPersonId;

    @Override
    public void initData() {
        if (getArguments() != null) {
            checkMsgId = getArguments().getString(MSG_DETAIL);
            checkMsgPersonId = getArguments().getString(MSG_PERSON_DETAIL);
        }
        CfLog.d("checkMsgId : " + checkMsgId);
        if (checkMsgId != null) {
            viewModel.getMessage(checkMsgId);
        }
        CfLog.d("checkMsgPersonId : " + checkMsgPersonId);
        if (checkMsgPersonId != null) {
            viewModel.getMessagePerson(checkMsgPersonId);
        }
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataMsgInfo.observe(this, vo -> {
            binding.tvwMsgTitle.setText(vo.title);
            binding.tvwMsgDate.setText(vo.created_at);
            String txt = vo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);
            binding.tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
        });

        viewModel.liveDataMsgPersonInfo.observe(this, vo -> {
            binding.tvwMsgTitle.setText(vo.title);
            binding.tvwMsgDate.setText(vo.sent_at);
            String txt = vo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);
            binding.tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_msg_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MsgViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MsgViewModel.class);
    }
}
