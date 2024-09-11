package com.xtree.mine.ui.fragment.withdrawal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.mine.R;
import com.xtree.mine.vo.OtherWebWithdrawVo;

import java.util.ArrayList;

public class FruitHorOtherRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface IOtherFruitHorCallback {
        void callbackWithFruitHor(OtherWebWithdrawVo.ChannelInfo selectVo);
    }
    private ArrayList<OtherWebWithdrawVo.ChannelInfo> arrayList;
    private IOtherFruitHorCallback callback;
    public FruitHorOtherRecyclerViewAdapter(ArrayList<OtherWebWithdrawVo.ChannelInfo> arrayList, IOtherFruitHorCallback callback) {
        super();
        this.arrayList = arrayList;
        this.callback = callback;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_bank_withdrawal_top_child , parent,false) ;
        final OtherViewHolder viewHolder = new OtherViewHolder(itemView) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OtherViewHolder viewHolder = (OtherViewHolder) holder;
        viewHolder.showTextView.setText(arrayList.get(position).name);
        viewHolder.showLayout.setBackgroundResource(R.drawable.bg_dialog_top_bank_selected);
        viewHolder.showLayout.setOnClickListener(v -> {
            referArray(arrayList.get(position), arrayList);

            notifyDataSetChanged();
            if (callback != null) {
                callback.callbackWithFruitHor(arrayList.get(position));
            }

        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    private void referArray(OtherWebWithdrawVo.ChannelInfo viewModel, ArrayList<OtherWebWithdrawVo.ChannelInfo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).name.equals(viewModel.name)) {
                //arrayList.get(i).flag = true;
            } else {
                //arrayList.get(i).flag = false;
            }

        }
    }

    private static class OtherViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView showTextView;
        private LinearLayout showLayout;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            showTextView = itemView.findViewById(R.id.tv_top_child);
            showLayout = itemView.findViewById(R.id.cl_title);
        }

        public View getItemView() {
            return itemView;
        }
    }
}
