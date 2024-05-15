package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChooseAwardsBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.AwardsRecordVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.Utils;

/**
 * 奖项流水
 */
public class AwardsRecordDialog extends BottomPopupView implements ActivityRefreshPopWindow.IActivityRefreshCallback {

    @Override
    public void OnClickListener() {
        if (awardsRecordVo != null && !awardsRecordVo.list.isEmpty()){
            awardsRecordVo.list.clear();
            adapter.notifyDataSetChanged();
        }
        resetRequestData();
    }

    public interface IAwardsDialogBack {
        void closeAwardsDialog();
    }

    private IAwardsDialogBack callBack;
    DialogChooseAwardsBinding binding;
    ChooseWithdrawViewModel viewModel;
    LifecycleOwner owner;
    private AwardsRecordVo awardsRecordVo;
    private ChooseAdapter adapter;
    private ActivityRefreshPopWindow activityPopWindow;

    private int viewType;//根据awardsRecordVo 显示页面 0 不显示数据 ；1 显示数据

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choose_awards;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

    private AwardsRecordDialog(@NonNull Context context) {
        super(context);
    }

    public static AwardsRecordDialog newInstance(Context context, LifecycleOwner owner, IAwardsDialogBack callBack) {
        AwardsRecordDialog dialog = new AwardsRecordDialog(context);
        dialog.owner = owner;
        dialog.callBack = callBack;
        dialog.viewType = 3;
        return dialog;
    }

    public static AwardsRecordDialog newInstance(Context context, LifecycleOwner owner, AwardsRecordVo awardsRecordVo, IAwardsDialogBack callBack) {
        AwardsRecordDialog dialog = new AwardsRecordDialog(context);
        dialog.owner = owner;
        dialog.awardsRecordVo = awardsRecordVo;
        dialog.callBack = callBack;
        dialog.viewType = 1;
        return dialog;
    }

    public static AwardsRecordDialog newInstance(Context context, LifecycleOwner owner, AwardsRecordVo awardsRecordVo, int viewType, IAwardsDialogBack callBack) {
        AwardsRecordDialog dialog = new AwardsRecordDialog(context);
        dialog.owner = owner;
        dialog.awardsRecordVo = awardsRecordVo;

        dialog.callBack = callBack;
        dialog.viewType = viewType;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initData();
        initViewObservable();
        initView();
    }

    private void initView() {
        binding = DialogChooseAwardsBinding.bind(findViewById(R.id.ll_root));
        binding.tvwTitle.setText(getContext().getString(R.string.txt_tip_activity_withdraw));

        binding.ivwClose.setOnClickListener(v -> {
            closePopWindow();
            //callBack.closeAwardsDialog();
            dismiss();

        });
       /* binding.llReferData.setVisibility(VISIBLE);
        binding.tvwRefer.setOnClickListener(v->{
            if (!awardsRecordVo.list.isEmpty()){
                awardsRecordVo.list.clear();
                adapter.notifyDataSetChanged();
            }
            requestData();
        });*/

        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                if (callBack != null) {
                    closePopWindow();
                    callBack.closeAwardsDialog();
                }
            }
            @Override
            public void onDrag(int y, float percent, boolean isScrollUp) {

            }
            @Override
            public void onOpen() {

            }
        });

        if (awardsRecordVo !=null){
            if (awardsRecordVo.list!=null &&  !awardsRecordVo.list.isEmpty()) {
                binding.llNoData.setVisibility(View.GONE);
                binding.scChoose.setVisibility(View.VISIBLE);
                if (adapter == null){
                    adapter  = new ChooseAdapter(getContext(), awardsRecordVo.list);
                }
                binding.lvChoose.setAdapter(adapter);
            } else {
                binding.scChoose.setVisibility(View.GONE);
                binding.llNoData.setVisibility(View.VISIBLE);
            }
        }
        showPopWindow();

    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {

        viewModel.awardrecordVoMutableLiveData.observe(owner, vo->{
            awardsRecordVo = vo;
            if (awardsRecordVo != null && awardsRecordVo.list.size() > 0) {
                if (adapter == null){
                    adapter  = new ChooseAdapter(getContext(), awardsRecordVo.list);
                }
                adapter.setAwardsRecordInfoArrayList(awardsRecordVo.list);
                binding.lvChoose.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                binding.lvChoose.setVisibility(View.VISIBLE);
            } else {
                binding.lvChoose.setVisibility(View.GONE);
                binding.llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 刷新活动提款流水
     */
    private void  requestData(){
        LoadingDialog.show(getContext());
        viewModel.getAwardRecord();
    }
    private void  resetRequestData(){
        LoadingDialog.show(getContext());
        viewModel.getAwardRecord();
    }

    private class ChooseAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<AwardsRecordVo.AwardsRecordInfo> awardsRecordInfoArrayList;

        public void setAwardsRecordInfoArrayList(ArrayList<AwardsRecordVo.AwardsRecordInfo> awardsRecordInfoArrayList) {
            this.awardsRecordInfoArrayList = awardsRecordInfoArrayList;
        }

        public ChooseAdapter(Context context, ArrayList<AwardsRecordVo.AwardsRecordInfo> list) {
            this.context = context;
            this.awardsRecordInfoArrayList = list;
        }

        @Override
        public int getCount() {
            return this.awardsRecordInfoArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.awardsRecordInfoArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChooseAdapterViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.dialog_withdrawa_flow_item, parent, false);
                holder = new ChooseAdapterViewHolder();
                holder.showInfoName = (TextView) convertView.findViewById(R.id.tvw_title);
                holder.showBonus = (TextView) convertView.findViewById(R.id.tvw_title_bonus);
                holder.showTurnover = (TextView) convertView.findViewById(R.id.tvw_required_turnover);
                holder.showContent = (TextView) convertView.findViewById(R.id.tvw_title_content);
                convertView.setTag(holder);
            } else {
                holder = (ChooseAdapterViewHolder) convertView.getTag();
            }
            AwardsRecordVo.AwardsRecordInfo info = awardsRecordInfoArrayList.get(position);

            holder.showInfoName.setText(info.title);
            String bonusTip = String.format(getContext().getString(R.string.txt_awards_bonus_tip), info.money);
            holder.showBonus.setText(bonusTip);
            String reTurnover = String.format(getContext().getString(R.string.txt_awards_required_turnover_tip), info.dispensing_money);
            holder.showTurnover.setText(reTurnover);

            if (TextUtils.isEmpty(info.dispensing_money_left) ||TextUtils.equals("0" , info.dispensing_money_left)) {
                holder.showContent.setText(getContext().getString(R.string.txt_awards_remain_turnover_default_tip));
            } else {
                String remainTurnover = String.format(getContext().getString(R.string.txt_awards_remain_turnover_tip), info.dispensing_money_left);
                holder.showContent.setText(remainTurnover);
            }

            return convertView;
        }

        public class ChooseAdapterViewHolder {
            public TextView showInfoName;
            public TextView showBonus;//奖金
            public TextView showTurnover;//流水
            public TextView showContent;//内容
        }
    }

    /*展示外跳View*/
    private void showPopWindow() {
        if (activityPopWindow == null) {
            activityPopWindow = new ActivityRefreshPopWindow(getContext(),this);
        }
        activityPopWindow.setVisibility(View.VISIBLE);
        activityPopWindow.show();
    }
    private void  closePopWindow(){
        if (activityPopWindow !=null){
            activityPopWindow.closeView();
            activityPopWindow.setVisibility(View.GONE);
        }
    }

}
