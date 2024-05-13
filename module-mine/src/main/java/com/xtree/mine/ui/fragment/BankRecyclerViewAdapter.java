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
import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;

import java.util.ArrayList;

/**
 * 银行卡提款顶部选项适配器
 */
public class BankRecyclerViewAdapter extends RecyclerView.Adapter {
    public interface IBankRecyclerCallback {
        public void callbackWithFruitHor(WithdrawalListVo selectVo);
    }

    private Context context;
    private ArrayList<WithdrawalListVo> arrayList;
    private IBankRecyclerCallback callback;

    public BankRecyclerViewAdapter(Context context, ArrayList<WithdrawalListVo> arrayList, IBankRecyclerCallback callback) {
        super();
        this.context = context;
        this.arrayList = arrayList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_bank_withdrawal_top_child, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.showTextView.setText(arrayList.get(position).title);
        if (arrayList.get(position).flag) {
            viewHolder.showLayout.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
        } else {
            viewHolder.showLayout.setBackgroundResource(R.drawable.bg_dialog_top_bank_noselected);
        }
        viewHolder.showLayout.setOnClickListener(v -> {
            referArray(arrayList.get(position), arrayList);

            notifyDataSetChanged();
            if (callback != null) {
                callback.callbackWithFruitHor(arrayList.get(position));
            }

        });
    }

    private void referArray(WithdrawalListVo viewModel, ArrayList<WithdrawalListVo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).title.equals(viewModel.title)) {
                arrayList.get(i).flag = true;
            } else {
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

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView showTextView;
        private LinearLayout showLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
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
            if (parent.getChildPosition(view) == 0) {
            }
        }
    }

}
