package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.mine.R;
import com.xtree.mine.vo.BankCardCashVo;

import java.util.ArrayList;


/**
 * 取款顶部View Adapter 通用体现 通用提现4 大额提现 固额提现
 */
public class FruitHorRecyclerViewAdapter extends RecyclerView.Adapter
{
    private Context context ;
    private ArrayList<BankCardCashVo.ChannelVo> arrayList ;
    private IFruitHorCallback callback ;

    public FruitHorRecyclerViewAdapter(Context context , ArrayList<BankCardCashVo.ChannelVo> arrayList , IFruitHorCallback callback)
    {
        super();
        this.context = context ;
        this.arrayList = arrayList ;
        this.callback = callback ;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_bank_withdrawal_top_child , parent,false) ;
        final  ViewHolder viewHolder = new ViewHolder(itemView) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder ;
        viewHolder.showTextView.setText(arrayList.get(position).name);
        if (arrayList.get(position).flag)
        {
            viewHolder.showLayout.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
        }
        else
        {
            viewHolder.showLayout.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
        }
        viewHolder.showLayout.setOnClickListener( v -> {
            referArray(arrayList.get(position) ,arrayList);

            notifyDataSetChanged();
            if (callback != null)
            {
                callback.callbackWithFruitHor(arrayList.get(position));
            }

        });
    }
    private void referArray(BankCardCashVo.ChannelVo viewModel , ArrayList<BankCardCashVo.ChannelVo> arrayList)
    {
        for (int i = 0; i < arrayList.size(); i++)
        {
            if (arrayList.get(i).name.equals(viewModel.name))
            {
                arrayList.get(i).flag = true ;
            }
            else
            {
                arrayList.get(i).flag = false;
            }

        }
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    private void  referViewHolder(int position)
    {
        for (int i = 0; i < arrayList.size(); i++)
        {

        }
    }

    private static class  ViewHolder extends  RecyclerView.ViewHolder
    {
        private View  itemView ;
        private TextView showTextView ;
        private LinearLayout showLayout ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView ;
            showTextView = itemView.findViewById(R.id.tv_top_child);
            showLayout = itemView.findViewById(R.id.cl_title);
        }
        
        public View getItemView() {
            return itemView;
        }
    }

    /**
     *  自定义调节 RecyclerView item 间距方法
     */
    public static  final class SpacesItemDecoration extends RecyclerView.ItemDecoration
    {
        private int spaces = 10 ;
        public SpacesItemDecoration(int spaces)
        {
            this.spaces = spaces ;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.left = spaces ;
        if (parent.getChildPosition(view) == 0)
        {
           // outRect.right = spaces ;
            //outRect.left = -10 ;
        }
        }
    }
}
