package com.xtree.activity.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.xtree.activity.R;
import com.xtree.activity.databinding.ItemNewBinding;
import com.xtree.activity.vo.NewVo;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserDialog;

public class NewAdapter extends CachedAutoRefreshAdapter<NewVo> {

    Context ctx;
    ItemNewBinding binding;

    public NewAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_new, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        NewVo vo = get(position);
        binding = ItemNewBinding.bind(holder.itemView);

        Glide.with(ctx).load(vo.h5_image).placeholder(R.mipmap.dc_bg).into(binding.ivwImg);
        binding.tvwTitle.setText(vo.title);
        binding.tvwTime.setText(vo.start_at + "~" + vo.end_at);

        binding.tvwDetail.setOnClickListener(v -> getDetail(vo));
        binding.clRoot.setOnClickListener(v -> getDetail(vo));
    }

    private void getDetail(NewVo vo) {
        CfLog.i(vo.toString());
        if (ClickUtil.isFastClick()) {
            return;
        }
        TagUtils.tagEvent(ctx, "dc", vo.id);
        String url = getString(vo);
        new XPopup.Builder(ctx).moveUpToKeyboard(false).asCustom(new BrowserDialog(ctx, vo.title, url, true)).show();

        //Intent it = new Intent(ctx, BrowserActivity.class);
        //it.putExtra("url", url);
        //ctx.startActivity(it);
    }

    private String getString(NewVo vo) {
        String url = "";

        //线上id
        //添加測試id：198 上線後刪除
        if (vo.id == 173) {
            url = DomainUtil.getDomain2() + "/webapp/#/newactivity/64/1?aid=173";
        } else if (vo.id == 174) {
            url = DomainUtil.getDomain2() + "/webapp/#/newactivity/64/5?aid=174";
       //} else if (vo.id == 198) {
       //     url = DomainUtil.getDomain2() + "/webapp/#/newactivity/64/5?aid=198";
        } else if (vo.id == 135) {
            url = DomainUtil.getDomain2() + "/webapp/#/turntable/135";
        } else {
            url = DomainUtil.getDomain2() + "/webapp/#" + vo.url;
        }
        return url;
    }
}
