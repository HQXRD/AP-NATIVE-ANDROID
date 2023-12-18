package com.xtree.activity.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.activity.BR;
import com.xtree.activity.R;
import com.xtree.activity.databinding.FragmentNewsBinding;
import com.xtree.activity.ui.viewmodel.ActivityViewModel;
import com.xtree.activity.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.KLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment<FragmentNewsBinding, ActivityViewModel> {

    private static final String ARG_TYPE_ID = "typeId";
    private static final String ARG_TYPE_NAME = "typeName";
    private static final String ARG_LIST = "list";

    private int typeId;
    private String typeName;
    ArrayList<NewVo> list = new ArrayList<>();

    NewAdapter mAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param typeId   Parameter 1.
     * @param typeName Parameter 2.
     * @param list     Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    public static NewsFragment newInstance(int typeId, String typeName, ArrayList<NewVo> list) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE_ID, typeId);
        args.putString(ARG_TYPE_NAME, typeName);
        args.putParcelableArrayList(ARG_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        mAdapter = new NewAdapter(getContext());
        binding.rcvMain.setAdapter(mAdapter);
        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            typeId = getArguments().getInt(ARG_TYPE_ID);
            typeName = getArguments().getString(ARG_TYPE_NAME);
            list = getArguments().getParcelableArrayList(ARG_LIST);
        }
        CfLog.i("typeId: " + typeId + ", typeName: " + typeName + ", list: " + list.size());

        binding.tvwInfo.setText(typeName + ", " + typeId);
        //mAdapter.clear(); // 刚初始化,没有数据,无需clear
        mAdapter.addAll(list);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return 0;
        return R.layout.fragment_news;
    }

    @Override
    public int initVariableId() {
        //return 0;
        return BR.viewModel;
    }

    @Override
    public ActivityViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(ActivityViewModel.class);
    }

    @Override
    public void onDestroy() {
        KLog.i(this.getClass().getSimpleName() + "typeId: " + typeId + ", typeName: " + typeName);
        super.onDestroy();
    }

}