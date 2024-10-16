package com.xtree.mine.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMemberBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberDialog extends BottomPopupView {
    CachedAutoRefreshAdapter<List<String>> mAdapter; //
    String user;
    String userData;
    List<List<String>> detailUserData;
    int maxHeight = 70;

    TextView tvwTitle;
    TextView tvwName;
    ImageView ivwClose;
    RecyclerView rcvMain;

    public MemberDialog(@NonNull Context context) {
        super(context);
    }


    public MemberDialog(@NonNull Context context, String user, String userData) {
        super(context);
        this.user = user;
        this.userData = userData;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        ivwClose = findViewById(R.id.ivw_close);
        tvwName = findViewById(R.id.tvw_name);
        rcvMain = findViewById(R.id.rcv_main);

        tvwTitle.setText("触发会员");
        tvwName.setText(user);
        rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        detailData(userData);

        mAdapter = new CachedAutoRefreshAdapter<List<String>>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_member, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                List<String> vo = get(position);
                ItemMemberBinding binding = ItemMemberBinding.bind(holder.itemView);

                if (!vo.get(0).isEmpty()) {
                    binding.tvwItem1.setVisibility(VISIBLE);
                    binding.tvwItem1.setText(vo.get(0));
                }
                if (!vo.get(1).isEmpty()) {
                    binding.tvwItem2.setVisibility(VISIBLE);
                    binding.tvwItem2.setText(vo.get(1));
                }
                if (!vo.get(2).isEmpty()) {
                    binding.tvwItem3.setVisibility(VISIBLE);
                    binding.tvwItem3.setText(vo.get(2));
                }
                if (!vo.get(3).isEmpty()) {
                    binding.tvwItem4.setVisibility(VISIBLE);
                    binding.tvwItem4.setText(vo.get(3));
                }

            }
        };
        rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvMain.setAdapter(mAdapter);
        mAdapter.addAll(detailUserData);

        ivwClose.setOnClickListener(v -> dismiss());

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_all_member;
    }

    private void detailData(String userData) {
        List<String> dataList = Arrays.asList(userData.split(","));

        detailUserData = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i += 4) {
            List<String> sublist = dataList.subList(i, Math.min(i + 4, dataList.size()));
            if (sublist.size() < 4) {
                List<String> paddedSublist = new ArrayList<>(sublist);
                while (paddedSublist.size() < 4) {
                    paddedSublist.add("");
                }
                detailUserData.add(paddedSublist);
            } else {
                detailUserData.add(new ArrayList<>(sublist));
            }
        }
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 40;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }
}
