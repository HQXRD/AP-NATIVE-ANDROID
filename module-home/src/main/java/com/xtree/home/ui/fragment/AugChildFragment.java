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
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.AugItemBinding;
import com.xtree.home.databinding.FragmentAugChildBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.AugVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

public class AugChildFragment extends BaseFragment<FragmentAugChildBinding, HomeViewModel> {
    ArrayList<AugVo.DataDTO> mList;

    public AugChildFragment(ArrayList<AugVo.DataDTO> arrayList) {
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
        binding.rvAugChild.addItemDecoration(new SpacesItemDecoration(10));
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<AugVo.DataDTO>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.aug_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                AugItemBinding binding2 = AugItemBinding.bind(holder.itemView);
                AugVo.DataDTO vo = get(position);
                Glide.with(AugChildFragment.this.getContext())
                        .load(DomainUtil.getDomain() + "webx/images/chess/aug/" + vo.getCode() + ".jpg")
                        .into(binding2.ibGame);
                binding2.ibGame.setOnClickListener(v ->
                        BrowserActivity.start(getContext(), "AUG棋牌",
                                DomainUtil.getDomain() + "api/game/au/playurl?autoThrad=0&h5judge=1&id=" + vo.getId()));
            }

        };
        adapter.addAll(mList);
        binding.rvAugChild.setAdapter(adapter);
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
//            if (parent.getChildLayoutPosition(view) == 0 && parent.getChildLayoutPosition(view) == 1) {
//                outRect.top = 0;
//            }
        }
    }

}
