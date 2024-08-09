package com.xtree.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.R;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.databinding.LayoutReportFilterBinding;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.impl.FilterViewOnClickListerner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 查询公共组件 （含起始日期,截止日期,2个类型,1个状态）
 */
public class FilterView extends LinearLayout {

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)

    private List<IBaseVo> listType;
    private List<IBaseVo> listType2 = new ArrayList<>();
    private List<IBaseVo> listStatus;

    public interface IBaseVo {
        String getShowName();

        String getShowId();
    }

    ICallBack mCallBackType;
    //ICallBack mCallBackType2;
    //ICallBack mCallBackStatus;

    LayoutReportFilterBinding binding;

    public interface ICallBack {
        void onTypeChanged(IBaseVo vo);

    }

    public String getStartTime() {

        return binding.tvwDayStart.getText().toString() + " 00:00:00";
    }


    public String getStartDate() {

        return binding.tvwDayStart.getText().toString();
    }

    public String getEndTime() {

        return binding.tvwDayEnd.getText().toString() + " 23:59:59";
    }

    public String getEndDate() {

        return binding.tvwDayEnd.getText().toString();
    }

    public String getTypeId(String def) {
        if (binding.tvwType.getTag() == null) {
            return def;
        }
        return ((IBaseVo) binding.tvwType.getTag()).getShowId();
    }

    public String getTypeId2(String def) {
        if (binding.tvwType2.getTag() == null) {
            return def;
        }
        return ((IBaseVo) binding.tvwType2.getTag()).getShowId();
    }

    public String getStatusId(String def) {
        if (binding.tvwStatus.getTag() == null) {
            return def;
        }
        return ((IBaseVo) binding.tvwStatus.getTag()).getShowId();
    }

    public String getEdit(String def) {
        if (binding.edtInput.getText().toString().trim().length() == 0) {
            return def;
        }
        return binding.edtInput.getText().toString().trim();
    }

    public void setTopTotal(String count, String money) {
        binding.llTopTotal.setVisibility(View.VISIBLE);
        binding.tvwReturnMoney.setText(money);
        binding.tvwReturnCount.setText(count);
    }

    /**
     * 设置类型和状态的标题
     */
    public void setTypeTitle(String type, String type2, String status) {
        binding.tvwTypeTitle.setText(type);
        binding.tvwTypeTitle2.setText(type2);
        binding.tvwStatusTitle.setText(status);
    }

    /**
     * 设置类型列表, 默认选中第一个 （适合还没有网络请求数据）
     *
     * @param listType
     */
    public void setData(List<IBaseVo> listType) {
        this.listType = listType;
        setDefaultView();
    }

    public void setData(List<IBaseVo> listType, List<IBaseVo> listStatus) {
        this.listType = listType;
        this.listStatus = listStatus;
        setDefaultView();
    }

    public void setData(List<IBaseVo> listType, List<IBaseVo> listType2, List<IBaseVo> listStatus) {
        this.listType = listType;
        this.listType2 = listType2;
        this.listStatus = listStatus;
        setDefaultView();
    }

    /**
     * 设置类型2的列表, 默认选中第一个.
     * 适合类型1选项发生了变化, 类型2也要发生变化
     *
     * @param list
     */
    public void setType2(List<IBaseVo> list) {
        this.listType2 = list;
        if (listType2 != null && !listType2.isEmpty()) {
            binding.tvwType2.setText(listType2.get(0).getShowName());
            binding.tvwType2.setTag(listType2.get(0));
        }
    }

    public void setDefType(IBaseVo vo) {
        binding.tvwType.setText(vo.getShowName());
        binding.tvwType.setTag(vo);
    }

    public void setDefStatus(IBaseVo vo) {
        binding.tvwStatus.setText(vo.getShowName());
        binding.tvwStatus.setTag(vo);
    }

    public void setDefTop(String title, String value, String hint) {
        binding.tvwTopTitle.setText(title);
        binding.edtTop.setText(value);
        binding.edtTop.setHint(hint);
    }

    public void setDefEdit(String title, String value, String hint) {
        binding.tvwInputTitle.setText(title);
        binding.edtInput.setText(value);
        binding.edtInput.setHint(hint);
    }

    public void setEdit(String name) {
        binding.edtInput.setText(name);
    }

    public void setDayStart(String startDay) {
        binding.tvwDayStart.setText(startDay);
    }

    public void setDayEnd(String endDay) {
        binding.tvwDayEnd.setText(endDay);
    }

    public void setTypeCallBack(ICallBack mTypeCallBack) {
        this.mCallBackType = mTypeCallBack;
    }

    /**
     * 设置类型列表, 不会选中第一个 （适合已返回网络请求数据）
     */
    public void reSetData(List<IBaseVo> listType) {
        this.listType = listType;
    }

    public void reSetData(List<IBaseVo> listType, List<IBaseVo> listStatus) {
        this.listType = listType;
        this.listStatus = listStatus;
    }

    public void reSetData(List<IBaseVo> listType, List<IBaseVo> listType2, List<IBaseVo> listStatus) {
        this.listType = listType;
        this.listType2 = listType2;
        this.listStatus = listStatus;
    }

    private void setDefaultView() {
        if (listType != null && !listType.isEmpty()) {
            binding.tvwType.setText(listType.get(0).getShowName());
            binding.tvwType.setTag(listType.get(0));
        }
        if (listType2 != null && !listType2.isEmpty()) {
            binding.tvwType2.setText(listType2.get(0).getShowName());
            binding.tvwType2.setTag(listType2.get(0));
        }

        if (listStatus != null && !listStatus.isEmpty()) {
            binding.tvwStatus.setText(listStatus.get(0).getShowName());
            binding.tvwStatus.setTag(listStatus.get(0));
        }
    }

    public void setVisibility(int type, int type2, int status) {

        setVisibility(View.GONE, type, type2, status, View.GONE);
    }

    public void setVisibility(int type, int type2, int status, int input) {

        setVisibility(View.GONE, type, type2, status, input);
    }

    public void setVisibility(int top, int type, int type2, int status, int input) {
        binding.llTop.setVisibility(top);
        binding.llDivTop.setVisibility(top);
        binding.llType.setVisibility(type);
        binding.llDiv.setVisibility(type);
        binding.llType2.setVisibility(type2);
        binding.llDiv2.setVisibility(type2);
        binding.llStatus.setVisibility(status);
        binding.llDivStatus.setVisibility(status);
        binding.llInput.setVisibility(input);
        binding.llDivInput.setVisibility(input);
    }

    public void setQueryListener(OnClickListener lsn) {
        //binding.tvwSelect.setOnClickListener(lsn);
        binding.tvwSelect.setOnClickListener(v -> {
            String dayStart = getStartDate().replace("-", "");
            String dayEnd = getEndDate().replace("-", "");
            if (Integer.parseInt(dayEnd) < Integer.parseInt(dayStart)) {
                ToastUtils.showShort(getContext().getString(R.string.txt_tip_date));
                return;
            }
            binding.edtInput.clearFocus();
            lsn.onClick(v);
        });
    }

    public void setQueryListener(FilterViewOnClickListerner filterViewOnClickListerner) {
        binding.tvwSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterViewOnClickListerner.onClick(
                        getStartDate(),
                        getEndDate(),
                        getStartTime(),
                        getEndTime(),
                        getTypeId("0"),
                        getTypeId2("0"),
                        getStatusId("0")
                );
            }
        });
    }

    public FilterView(Context context) {
        super(context);
        init(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int getLayoutId() {
        return R.layout.layout_report_filter;
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, getLayoutId(), this);

        binding = LayoutReportFilterBinding.bind(findViewById(R.id.ll_root));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.filterView);

        binding.llType.setVisibility(typedArray.getInt(R.styleable.filterView_type1Visibility, View.VISIBLE));
        binding.llDiv.setVisibility(typedArray.getInt(R.styleable.filterView_type1Visibility, View.VISIBLE));
        binding.tvwTypeTitle.setText(typedArray.getString(R.styleable.filterView_typeName1));

        binding.llType2.setVisibility(typedArray.getInt(R.styleable.filterView_type2Visibility, View.GONE));
        binding.llDiv2.setVisibility(typedArray.getInt(R.styleable.filterView_type2Visibility, View.GONE));
        binding.tvwTypeTitle2.setText(typedArray.getString(R.styleable.filterView_typeName2));

        binding.llStatus.setVisibility(typedArray.getInt(R.styleable.filterView_statusVisibility, View.VISIBLE));
        binding.llDivStatus.setVisibility(typedArray.getInt(R.styleable.filterView_statusVisibility, View.VISIBLE));
        binding.tvwStatusTitle.setText(typedArray.getString(R.styleable.filterView_statusName));

        binding.tvwDayStart.setOnClickListener(v -> showDatePicker(binding.tvwDayStart, context.getString(R.string.txt_date_start)));
        binding.tvwDayEnd.setOnClickListener(v -> showDatePicker(binding.tvwDayEnd, context.getString(R.string.txt_date_end)));

        binding.tvwType.setOnClickListener(v -> showDialog(binding.tvwType, binding.tvwTypeTitle.getText(), listType, mCallBackType));
        binding.tvwType2.setOnClickListener(v -> showDialog(binding.tvwType2, binding.tvwTypeTitle2.getText(), listType2));
        binding.tvwStatus.setOnClickListener(v -> showDialog(binding.tvwStatus, binding.tvwStatusTitle.getText(), listStatus));

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6); // 6个月前
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String txtDayStart = TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd");
        String txtDayEnd = TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        binding.tvwDayStart.setText(txtDayStart);
        binding.tvwDayEnd.setText(txtDayEnd);
    }

    private void showDatePicker(TextView tvw, String title) {
        CfLog.i("****** ");
        new XPopup.Builder(getContext())
                .asCustom(DateTimePickerDialog.newInstance(getContext(), title, 3, date -> tvw.setText(date)))
                .show();
    }

    private void showDialog(TextView tvw, CharSequence title, List<IBaseVo> list) {
        showDialog(tvw, title, list, null);
    }

    private void showDialog(TextView tvw, CharSequence title, List<IBaseVo> list, ICallBack callBack) {
        CfLog.i("****** " + title);
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<IBaseVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                IBaseVo vo = get(position);
                binding2.tvwTitle.setText(vo.getShowName());
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i(vo.toString());
                    tvw.setText(vo.getShowName());
                    tvw.setTag(vo);
                    if (callBack != null) {
                        callBack.onTypeChanged(vo);
                    }
                    ppw.dismiss();
                });

            }
        };

        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title.toString(), adapter));
        ppw.show();
    }

    private static BasePopupView mpop = null;

    public static void showDialog(Context context, CharSequence title, List<IBaseVo> list, ICallBack callBack) {
        CfLog.i("****** " + title);

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<IBaseVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemTextBinding binding2;

                binding2 = ItemTextBinding.bind(holder.itemView);
                IBaseVo vo = get(position);
                binding2.tvwTitle.setText(vo.getShowName());
                binding2.tvwTitle.setOnClickListener(v -> {
                    CfLog.i(vo.toString());
                    if (callBack != null) {
                        callBack.onTypeChanged(vo);
                    }
                    if (mpop != null) {
                        mpop.dismiss();
                    }
                });

            }
        };

        adapter.addAll(list);
        mpop = new XPopup.Builder(context).asCustom(new ListDialog(context, title.toString(), adapter)).show();
    }
}
