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
import com.xtree.home.databinding.AugItemBinding;
import com.xtree.home.databinding.FragmentAugChildBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.AugVo;

import java.util.ArrayList;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseFragment;

public class AugChildFragment extends BaseFragment<FragmentAugChildBinding, HomeViewModel> {
    ArrayList<AugVo> mList;

    public AugChildFragment(ArrayList<AugVo> arrayList) {
        mList = arrayList;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_aug_child;
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
        binding.rvAugChild.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvAugChild.addItemDecoration(new SpacesItemDecoration(15));
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<AugVo>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.aug_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                AugItemBinding binding2 = AugItemBinding.bind(holder.itemView);
                AugVo vo = get(position);
                CfLog.i(vo.toString());
                Glide.with(AugChildFragment.this.getContext())
                        .load(DomainUtil.getDomain() + "webx/images/chess/aug/" + vo.getCode() + ".jpg")
                        .placeholder(R.mipmap.cm_placeholder_image)
                        .into(binding2.ibGame);

                binding2.ibGame.setOnClickListener(v -> {
                    CfLog.i(vo.toString());
                    TagUtils.tagEvent(getContext(), "AUG", vo.getId()); // 打点
                    if (ClickUtil.isFastClick()) {
                        return;
                    }
                    viewModel.getPlayUrl("au", vo.getId(), getString(R.string.txt_venue_aug));
                });
            }

        };
        adapter.addAll(mList);
        binding.rvAugChild.setAdapter(adapter);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.liveDataPlayUrl.observe(getViewLifecycleOwner(), map -> {
            String url = Objects.requireNonNull(map.get("url")).toString();
            String name = Objects.requireNonNull(map.get("name")).toString();
            // 跳转到游戏H5
            CfLog.i("URL: " + url);
            BrowserActivity.start(getContext(), name, url, false, true);
        });
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
