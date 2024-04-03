package com.xtree.mine.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.vo.UserBankProvinceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KAKA on 2024/4/3.
 * Describe: 开户银行选择弹窗
 */
public class BankSelectDialog extends BottomPopupView {

    String title; // 标题
    CachedAutoRefreshAdapter<UserBankProvinceVo.BankInfoVo> adapter;
    int maxHeight = 40; // 最大高度百分比 10-100

    TextView tvwTitle;
    ImageView ivwClose;
    RecyclerView rcvMain;
    private EditText search;
    private List<UserBankProvinceVo.BankInfoVo> data;

    public BankSelectDialog(@NonNull Context context) {
        super(context);
    }

    public BankSelectDialog(@NonNull Context context, String title, CachedAutoRefreshAdapter<UserBankProvinceVo.BankInfoVo> adapter) {
        super(context);
        this.title = title;
        this.adapter = adapter;
        data = adapter.getData();
    }

    /**
     * @param context   Context
     * @param title     标题
     * @param adapter   适配器
     * @param maxHeight 最大高度, 占屏幕高度的百分比 (推荐 30-90)
     */
    public BankSelectDialog(@NonNull Context context, String title, CachedAutoRefreshAdapter<UserBankProvinceVo.BankInfoVo> adapter, int maxHeight) {
        super(context);
        this.title = title;
        this.adapter = adapter;
        this.maxHeight = maxHeight;
        data = adapter.getData();
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }



    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        ivwClose = findViewById(R.id.ivw_close);
        rcvMain = findViewById(R.id.rcv_main);
        search = findViewById(R.id.dialog_search);

        tvwTitle.setText(title);
        rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvMain.setAdapter(adapter);

        ivwClose.setOnClickListener(v -> dismiss());

        search.clearFocus();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //模糊搜索
                String sc = s.toString().trim();
                ArrayList<UserBankProvinceVo.BankInfoVo> infoVos = new ArrayList<>();
                for (UserBankProvinceVo.BankInfoVo datum : data) {
                    if (datum.bank_name.contains(sc)) {
                        infoVos.add(datum);
                    }
                }
                if (sc.isEmpty()) {
                    adapter.setData(data);
                } else {
                    adapter.setData(infoVos);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bank_select;
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
