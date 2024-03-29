package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckFoot;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateAddModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateHeadModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtSearchUserResultModel;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.response.DividendAgrtCheckResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtCheckViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_dividendagrt_check);
                    add(R.layout.item_rebateagrt_create_head);
                    add(R.layout.item_rebateagrt_create_add);
                    add(R.layout.item_dividendagrt_check_foot);
                }
            });
    private final MutableLiveData<String> titleData = new MutableLiveData<>(getApplication().getString(R.string.txt_dividendagrt_title));
    private final RebateAgrtCreateHeadModel headModel = new RebateAgrtCreateHeadModel();
    private final RebateAgrtCreateAddModel addModel = new RebateAgrtCreateAddModel();
    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        addModel.setItemType(2);
        addModel.openAdd.set(false);
        DividendAgrtCheckFoot footModel = new DividendAgrtCheckFoot();
        footModel.setItemType(3);
        add(addModel);
        add(headModel);
        add(footModel);
    }};
    public MutableLiveData<RebateAgrtSearchUserResultModel> searchUserResultLiveData = new MutableLiveData<>();
    private DividendAgrtCheckRequest dividendAgrtCheckRequest;
    private WeakReference<FragmentActivity> mActivity = null;

    public DividendAgrtCheckViewModel(@NonNull Application application) {
        super(application);
    }

    public DividendAgrtCheckViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(DividendAgrtCheckRequest response) {
        //init data
        dividendAgrtCheckRequest = response;
        getDividendAgrt();
        datas.setValue(bindModels);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    private void formatItem() {
        //设置小标题
        for (int i = 0; i < bindModels.size(); i++) {
            BindModel bind = bindModels.get(i);
            if (bind instanceof DividendAgrtCheckModel) {
                DividendAgrtCheckModel model = (DividendAgrtCheckModel) bind;
                model.numText.set(getApplication().getString(R.string.txt_rules) + (i - 2));
            }
        }
    }

    /**
     * 获取契约详情
     */
    public void getDividendAgrt() {

        if (dividendAgrtCheckRequest == null) {
            ToastUtils.show("数据错误", ToastUtils.ShowType.Fail);
            return;
        }

        Disposable disposable = model.getDividendAgrtData(dividendAgrtCheckRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAgrtCheckResponse>() {
                    @Override
                    public void onResult(DividendAgrtCheckResponse response) {

                        DividendAgrtCheckResponse.DataDTO data = response.getData();
                        if (response != null && data != null) {
                            for (DividendAgrtCheckResponse.DataDTO.RuleDTO ruleDTO : data.getRule()) {
                                DividendAgrtCheckModel dividendAgrtCheckModel = new DividendAgrtCheckModel();
                                dividendAgrtCheckModel.setRatio(ruleDTO.getRatio());
                                dividendAgrtCheckModel.setNetProfit(ruleDTO.getNet_profit());
                                dividendAgrtCheckModel.setProfit(ruleDTO.getProfit());
                                dividendAgrtCheckModel.setPeople(ruleDTO.getPeople());
                                bindModels.add(dividendAgrtCheckModel);
                            }

                            headModel.user.set(data.getUsername());
                            formatItem();
                            datas.setValue(bindModels);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show(t.message, ToastUtils.ShowType.Fail);
                    }
                });
        addSubscribe(disposable);

    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
