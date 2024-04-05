package com.xtree.home.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.EleItemBinding;
import com.xtree.home.databinding.FragmentEleChildBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.Ele;
import com.xtree.home.vo.EleVo;
import com.xtree.home.vo.GameVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

public class EleChildFragment extends BaseFragment<FragmentEleChildBinding, HomeViewModel> {

    private int position;
    private EleVo eleVo;
    private GameVo gameVo;
    private CachedAutoRefreshAdapter<Ele> adapter;

    public EleChildFragment(int position, EleVo eleVo, GameVo gameVo) {
        this.position = position;
        this.eleVo = eleVo;
        this.gameVo = gameVo;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_ele_child;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    @Override
    public void initView() {
        binding.rvEleChild.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvEleChild.addItemDecoration(new SpacesItemDecoration(15));
        adapter = new CachedAutoRefreshAdapter<Ele>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.ele_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                EleItemBinding binding = EleItemBinding.bind(holder.itemView);
                Ele vo1 = get(position);
                CfLog.i(vo1.toString());
                Glide.with(EleChildFragment.this.getContext())
                        .load(DomainUtil.getDomain2() + vo1.getPicture())
                        .placeholder(R.mipmap.cm_placeholder_image)
                        .into(binding.ibGame);
                binding.tvGame.setText(vo1.getName());

                binding.ibGame.setOnClickListener(v -> {
                    if (ClickUtil.isFastClick()) {
                        return;
                    }
                    CfLog.i(vo1.toString());
                    String eventName = gameVo.name.length() > 2 ? gameVo.name.substring(0, 2) : "gm2";
                    TagUtils.tagEvent(getContext(), eventName, vo1.getId()); // 打点
                    BrowserActivity.start(getContext(), gameVo.name, DomainUtil.getDomain() + gameVo.playURL + vo1.getId(), false, true);
                });
            }

        };
        if (position == 0) {
            adapter.addAll(eleVo.getList());
        } else {
            ArrayList<Ele> hotList = new ArrayList();
            for (Ele ele : eleVo.getList()) {
                if (ele.is_hot().equals("true") || ele.is_hot().equals("1")) {
                    hotList.add(ele);
                }
            }
            adapter.addAll(hotList);
        }
        binding.rvEleChild.setAdapter(adapter);
    }

    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int spaces = 10;

        public SpacesItemDecoration(int spaces) {
            this.spaces = spaces;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            outRect.right = spaces;
            outRect.left = spaces;
            outRect.top = 0;
            outRect.bottom = spaces * 2;
        }
    }

}
