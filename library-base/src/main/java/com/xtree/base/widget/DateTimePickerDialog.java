package com.xtree.base.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogDatetimePickerBinding;
import com.xtree.base.utils.TimeUtils;

import java.util.Calendar;

/**
 * 日期选择器，滑轮样式
 * 默认日期范围 6个月前到现在
 */
public class DateTimePickerDialog extends BottomPopupView {

    String mFormat = "yyyy-MM-dd"; // "yyyy-MM-dd HH:mm:ss"
    String title = "";
    ICallBack mCallBack;

    String tmpDate = "";

    long minMillisecond = 0;
    long maxMillisecond = 0;
    DialogDatetimePickerBinding binding;

    public interface ICallBack {
        void setDate(String date);

    }

    private DateTimePickerDialog(@NonNull Context context) {
        super(context);
    }

    public static DateTimePickerDialog newInstance(@NonNull Context context, String title, ICallBack mCallBack) {
        DateTimePickerDialog dialog = new DateTimePickerDialog(context);
        dialog.title = title;
        dialog.mCallBack = mCallBack;
        return dialog;
    }

    /**
     * @param context        上下文
     * @param title          标题
     * @param minMillisecond 最小时间 (默认0,6个月前)
     * @param maxMillisecond 最大时间 (默认0,现在时间)
     * @param mCallBack      选中后的回调事件
     */
    public static DateTimePickerDialog newInstance(@NonNull Context context, String title, long minMillisecond, long maxMillisecond, ICallBack mCallBack) {
        DateTimePickerDialog dialog = new DateTimePickerDialog(context);
        dialog.title = title;
        dialog.minMillisecond = minMillisecond;
        dialog.maxMillisecond = maxMillisecond;
        dialog.mCallBack = mCallBack;
        return dialog;
    }

    public static DateTimePickerDialog newInstance(@NonNull Context context, String title, int minMonth, ICallBack mCallBack) {
        return newInstance(context, title, minMonth, "yyyy-MM-dd", mCallBack);
    }

    /**
     * @param context   上下文
     * @param title     标题
     * @param format    日期时间样式 默认: "yyyy-MM-dd"
     * @param minMonth  当前日期往前几个月 (默认0,6个月前)
     * @param mCallBack 选中后的回调事件
     */
    public static DateTimePickerDialog newInstance(@NonNull Context context, String title, int minMonth, String format, ICallBack mCallBack) {
        DateTimePickerDialog dialog = new DateTimePickerDialog(context);
        dialog.title = title;
        dialog.mCallBack = mCallBack;
        dialog.mFormat = format;
        if (minMonth > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - minMonth);
            dialog.minMillisecond = calendar.getTimeInMillis();
        }
        return dialog;
    }

    /**
     * 设置 日期时间样式 默认: "yyyy-MM-dd", 参考: "yyyy-MM-dd HH:mm:ss"
     *
     * @param format 日期时间样式
     */
    public void setDateTimeFormat(String format) {
        mFormat = format;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        binding = DialogDatetimePickerBinding.bind(findViewById(R.id.ll_root));
        //binding.tvwCancel.setText(R.string.txt_cancel);
        initView();
    }

    private void initView() {

        binding.tvwTitle.setText(title);
        binding.tvwCancel.setOnClickListener(v -> dismiss());
        binding.tvwConfirm.setOnClickListener(v -> {
            mCallBack.setDate(tmpDate);
            dismiss();
        });

        int[] array = new int[]{0, 1, 2}; // show year,money,day
        binding.dateTimePicker.setDisplayType(array);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
        binding.dateTimePicker.setMinMillisecond(calendar.getTimeInMillis());
        binding.dateTimePicker.setMaxMillisecond(System.currentTimeMillis());

        if (minMillisecond > 0) {
            binding.dateTimePicker.setMinMillisecond(minMillisecond);
        }
        if (maxMillisecond > 0) {
            binding.dateTimePicker.setMaxMillisecond(maxMillisecond);
        }
        binding.dateTimePicker.showLabel(true);
        binding.dateTimePicker.setOnDateTimeChangedListener((dateTimePicker, l) -> {
            tmpDate = TimeUtils.longFormatString(l, mFormat);
            //CfLog.d(tmpDate);
        });

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_datetime_picker;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 30 / 100);
    }
}
