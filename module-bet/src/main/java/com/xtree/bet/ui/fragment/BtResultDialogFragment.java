package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.BetConfirmOptionPm;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.databinding.BtLayoutBtResultBinding;
import com.xtree.bet.ui.adapter.BetResultOptionAdapter;
import com.xtree.bet.ui.adapter.CgBtResultAdapter;
import com.xtree.bet.ui.viewmodel.fb.FBBtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.bet.util.OptionDeserializer;
import com.xtree.bet.util.OptionListDeserializer;
import com.xtree.bet.util.PlayTypeDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注确认页面
 */
public class BtResultDialogFragment extends BaseDialogFragment<BtLayoutBtResultBinding, FBBtCarViewModel> {
    public final static String KEY_BT_OPTION = "KEY_BT_OPTION";
    public final static String KEY_BT_CGLIMITE = "KEY_BT_CGLIMITE";
    public final static String KEY_BT_RESULT = "KEY_BT_RESULT";
    /**
     * 投注项列表
     */
    List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
    List<CgOddLimit> cgOddLimitList = new ArrayList<>();
    List<BtResult> btResultList = new ArrayList<>();

    private BetResultOptionAdapter betResultOptionAdapter;
    private CgBtResultAdapter cgBtResultAdapter;

    public static BtResultDialogFragment getInstance(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, List<BtResult> btResultList){
        BtResultDialogFragment btResultDialogFragment = new BtResultDialogFragment();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_BT_OPTION, new Gson().toJson(betConfirmOptionList));
        bundle.putParcelableArrayList(KEY_BT_CGLIMITE, (ArrayList) cgOddLimitList);
        bundle.putParcelableArrayList(KEY_BT_RESULT, (ArrayList) btResultList);
        btResultDialogFragment.setArguments(bundle);
        return btResultDialogFragment;
    }

    @Override
    public void initView() {
        binding.rvBtOption.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvBtCg.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.tvClose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Gson gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Option.class, new OptionDeserializer())
                .registerTypeAdapter(OptionList.class, new OptionListDeserializer())
                .registerTypeAdapter(Match.class, new MatchDeserializer())
                .registerTypeAdapter(PlayType.class , new PlayTypeDeserializer())
                .create();
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        Type listType = TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC) ? new TypeToken<List<BetConfirmOptionPm>>() {
        }.getType() : new TypeToken<List<BetConfirmOptionFb>>() {
        }.getType();
        betConfirmOptionList = gson.fromJson(getArguments().getString(KEY_BT_OPTION), listType);
        cgOddLimitList = getArguments().getParcelableArrayList(KEY_BT_CGLIMITE);
        btResultList = getArguments().getParcelableArrayList(KEY_BT_RESULT);

        betResultOptionAdapter = new BetResultOptionAdapter(getContext(), betConfirmOptionList);
        binding.rvBtOption.setAdapter(betResultOptionAdapter);


        cgBtResultAdapter = new CgBtResultAdapter(getContext(), cgOddLimitList, btResultList);
        binding.rvBtCg.setAdapter(cgBtResultAdapter);

        boolean isSuccessed = true;
        for(BtResult btResult : btResultList){
            if(!btResult.isSuccessed()){
                isSuccessed = false;
                break;
            }
        }
        if(!isSuccessed){
            binding.tvResult.setText(getString(R.string.bt_bt_tip_failed));
            binding.ivResult.setBackgroundResource(R.mipmap.bt_icon_bt_failed);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                })
        );
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_layout_bt_result;
    }

    @Override
    public void initViewObservable() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.tv_close){
            dismiss();
        }
    }

    @Override
    public FBBtCarViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(FBBtCarViewModel.class);
    }
}
