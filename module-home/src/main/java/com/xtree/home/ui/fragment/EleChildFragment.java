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

import me.xtree.mvvmhabit.base.BaseFragment;

public class EleChildFragment extends BaseFragment<FragmentEleChildBinding, HomeViewModel> {

    private int position;
    private int curPage = 1;
    private EleVo eleVo;
    private GameVo gameVo;
    private CachedAutoRefreshAdapter<Ele> adapter;

    public static EleChildFragment newInstance(int position, GameVo vo) {
        EleChildFragment fragment = new EleChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("gameVo", vo);
        fragment.setArguments(bundle);
        return fragment;
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
        //内存不足时，可能出现等于null
        if (getArguments() == null) {
            return;
        }
        position = getArguments().getInt("position");
        gameVo = getArguments().getParcelable("gameVo");
        binding.refreshLayout.setEnableRefresh(false);
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            requestData(curPage + 1);
        });
        binding.rvEleChild.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvEleChild.addItemDecoration(new SpacesItemDecoration(10));
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
                Glide.with(EleChildFragment.this.requireContext())
                        .load(DomainUtil.getH5Domain2() + vo1.getPicture())
                        .placeholder(R.mipmap.cm_placeholder)
                        .into(binding.ibGame);
                binding.tvGame.setText(vo1.getName());

                binding.ibGame.setOnClickListener(v -> {
                    if (ClickUtil.isFastClick()) {
                        return;
                    }
                    CfLog.i(vo1.toString());
                    String eventName = gameVo.name != null && gameVo.name.length() > 2 ? gameVo.name.substring(0, 2) : "gm2";
                    TagUtils.tagEvent(getContext(), eventName, vo1.getId()); // 打点
                    BrowserActivity.start(getContext(), gameVo.name, DomainUtil.getH5Domain() + gameVo.playURL + vo1.getId(), false, true);
                });
            }

        };
        requestData(1);
        binding.rvEleChild.setAdapter(adapter);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.liveDataEle.observe(getViewLifecycleOwner(), eleVo -> {
            if (eleVo.getList() == null || eleVo.getList().isEmpty()) {
                binding.refreshLayout.finishLoadMoreWithNoMoreData();
                return;
            } else {
                binding.refreshLayout.finishLoadMore();
            }
            adapter.addAll(eleVo.getList());
            curPage += 1;
        });
    }

    /**
     * 加载更多
     */
    private void requestData(int page) {
        viewModel.getEle(gameVo.cid, page, 20, gameVo.cateId, position);
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
