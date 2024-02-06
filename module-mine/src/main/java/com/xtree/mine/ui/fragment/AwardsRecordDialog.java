package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogChooseWithdrawaBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.ChooseInfoVo;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.Utils;


/**
 * 奖项流水
 */
public class AwardsRecordDialog extends BottomPopupView {

    public interface IAwardsDialogBack{
        public void  closeAwardsDialog();
    }
    private IAwardsDialogBack callBack ;
    private BasePopupView basePopupView = null;
    DialogChooseWithdrawaBinding binding ;
    ChooseWithdrawViewModel viewModel ;
    LifecycleOwner owner;
    Context context ;
    ChooseInfoVo chooseInfoVo ;
    BasePopupView ppw = null; // 底部弹窗
    private AwardsRecordVo awardsRecordVo ;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choose_withdrawa;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 75 / 100);
    }

    private AwardsRecordDialog(@NonNull Context context) {
        super(context);
    }
    public static AwardsRecordDialog newInstance(Context context, LifecycleOwner owner , AwardsRecordVo  awardsRecordVo,IAwardsDialogBack callBack)
    {
        AwardsRecordDialog dialog = new AwardsRecordDialog(context);
        context = context ;
        dialog.context = context ;
        dialog.owner =owner ;
        dialog.awardsRecordVo = awardsRecordVo ;
        dialog.callBack = callBack ;
        return dialog;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initData();
        initViewObservable();

    }
    private void initView()
    {
        binding = DialogChooseWithdrawaBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v->{
            dismiss();
            callBack.closeAwardsDialog();

        });
        binding.tvwTitle.setText(getContext().getString(R.string.txt_tip_unfinished_activity));

        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                if (callBack!=null)
                {
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

        ChooseAdapter adapter = new ChooseAdapter(getContext() ,awardsRecordVo.list);
        binding.lvChoose.setAdapter(adapter);

    }

    private void initData()
    {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable()
    {

    }

    /**
     * 请求网络数据
     */
    private void  requestData()
    {
        viewModel.getChooseWithdrawInfo();
    }
    private void  referUI()
    {
    }


    private class ChooseAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater ;
        private IChooseCallback  callBack ;
        private Context context ;
        private ArrayList<AwardsRecordVo.AwardsRecordInfo> awardsRecordInfoArrayList ;

        public ChooseAdapter(Context context , ArrayList<AwardsRecordVo.AwardsRecordInfo>  list )
        {
            this.context = context ;
            this.awardsRecordInfoArrayList = list ;
            this.callBack = callBack ;
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
            if (convertView == null)
            {
                convertView =  LayoutInflater.from(context).inflate(R.layout.dialog_choose_awards_item ,parent,false);
                holder = new ChooseAdapterViewHolder();
                holder.showInfoName = (TextView) convertView.findViewById(R.id.tvw_title);
                holder.showBonus = (TextView) convertView.findViewById(R.id.tvw_title_bonus);
                holder.showTurnover = (TextView) convertView.findViewById(R.id.tvw_required_turnover);
                holder.showContent = (TextView) convertView.findViewById(R.id.tvw_title_content);
                holder.showInfoLinear = convertView.findViewById(R.id.ll_root);
                convertView.setTag(holder);
             }
            else
            {
                holder = (ChooseAdapterViewHolder) convertView.getTag() ;
            }
            AwardsRecordVo.AwardsRecordInfo info = awardsRecordInfoArrayList.get(position) ;

            holder.showInfoName.setText(info.title);
            holder.showBonus.setText("需求流水"+info.money);
            holder.showTurnover.setText("剩余流水:"+info.deducted_turnover);
            holder.showContent.setText(info.bet_source_trans);

            return  convertView;
        }


        public  class ChooseAdapterViewHolder
        {
            public TextView showInfoName ;
            public TextView showBonus ;//奖金
            public TextView showTurnover;//流水
            public TextView showContent ;//内容
            public LinearLayout showInfoLinear;
        }
    }

}