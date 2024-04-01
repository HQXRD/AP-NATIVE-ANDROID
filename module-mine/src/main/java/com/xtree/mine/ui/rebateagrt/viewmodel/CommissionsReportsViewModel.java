package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.dialog.CommissionsRulesDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.CommissionsReportsModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.request.CommissionsReportsRequest;
import com.xtree.mine.vo.response.CommissionsReportsResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/29.
 * Describe: 佣金报表viewModel
 */
public class CommissionsReportsViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    private final MutableLiveData<String> titleData = new MutableLiveData<>(getApplication().getString(R.string.commissions_reports_title));
    private WeakReference<FragmentActivity> mActivity = null;
    private String startTime = "";
    private String endTime = "";
    public MutableLiveData<String> dateData = new MutableLiveData<>();
    public MutableLiveData<CommissionsReportsModel> infoData = new MutableLiveData<>();

    public CommissionsReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public CommissionsReportsViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        curMonth();
        getData();
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public synchronized void getData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        CommissionsReportsRequest request = new CommissionsReportsRequest();
        request.setStart_time(startTime);
        request.setEnd_time(endTime);
        Disposable disposable = (Disposable) model.getCommissionsData(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<CommissionsReportsResponse>() {
                    @Override
                    public void onResult(CommissionsReportsResponse vo) {
                        CommissionsReportsResponse.CommissionInfoDTO info = vo.getCommissionInfo();

                        if (info != null) {
                            CommissionsReportsModel m = new CommissionsReportsModel();
                            m.setActual(info.getActual());
                            m.setSent_at(info.getSentAt());
                            m.setIncome(info.getIncome());
                            m.setLast_remain(info.getLastRemain());
                            m.setRemain(info.getRemain());
                            m.setRatio(info.getRatio());
                            m.setStatus_name(info.getStatusName());
                            m.setActivity_people(info.getActivityPeople());
                            m.setAdjust_income(info.getAdjustIncome());
//                            m.setActual_incentives();
//                            m.setActive_agents();
//                            m.setIncentive_ratio();
                            infoData.setValue(m);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show("请求错误", ToastUtils.ShowType.Fail);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 设置当前月份查询
     */
    public void curMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdf.format(cal.getTime());
        endTime = sdf.format(new Date(System.currentTimeMillis()));
        dateData.setValue(startTime + "~" + endTime);
    }
    /**
     * 设置上个月份查询
     */
    public void lastMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdf.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endTime = sdf.format(cal.getTime());
        dateData.setValue(startTime + "~" + endTime);
    }
    /**
     * 设置上上个月份查询
     */
    public void beforeLastMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdf.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endTime = sdf.format(cal.getTime());
        dateData.setValue(startTime + "~" + endTime);
    }

    /**
     * 显示佣金规则制度
     */
    public void showCommissionsRules() {
        CommissionsRulesDialogFragment.show(mActivity.get(), CommissionsRulesDialogFragment.COMMISSIONS_MODE);
    }

    /**
     * 显示代理规则制度
     */
    public void showAgentRules() {
        CommissionsRulesDialogFragment.show(mActivity.get(), CommissionsRulesDialogFragment.AGENT_MODE);
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
