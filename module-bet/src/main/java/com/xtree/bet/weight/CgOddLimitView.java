package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CgOddLimitView extends LinearLayout {

    private List<View> viewList = new ArrayList<>();

    public CgOddLimitView(Context context) {
        super(context);
    }

    public CgOddLimitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CgOddLimitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(@Nullable Adapter adapter) {
        for (int position = 0; position < adapter.getItemCount(); position++) {
            View itemView = LayoutInflater.from(getContext()).inflate(adapter.layoutId(), null);
            adapter.convert(itemView, adapter.getItemData(position), position);
            addView(itemView);
            viewList.add(itemView);
            adapter.setViewList(viewList);
            adapter.setParent(this);
        }
    }

    public abstract static class Adapter<T> {
        protected Context mContext;
        protected int mLayoutId;
        protected LayoutInflater mInflater;
        protected List<T> mDatas = new ArrayList<>();
        protected List<View> viewList = new ArrayList<>();
        protected boolean sizeChange;
        protected CgOddLimitView parent;
        public abstract int layoutId();

        public Adapter(final Context context, List<T> datas)
        {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mLayoutId = layoutId();
            mDatas = datas;
        }

        protected T getItemData(int position){
            if(mDatas.size() > position) {
                return mDatas.get(position);
            }else {
                return null;
            }
        }

        protected abstract void convert(View view, T t, int position);

        public void setNewData(List<T> datas){

            if(datas.size() < mDatas.size()){
                sizeChange = true;
                for (int i = 0; i < mDatas.size() - datas.size(); i++) {
                    viewList.remove(i);
                    parent.removeViewAt(i);
                }
                mDatas.clear();
                mDatas.addAll(datas);
            }
            notifyDataSetChanged();
            sizeChange = false;
        }

        public int getItemCount() {
            int itemCount = mDatas.size();
            return itemCount;
        }

        public void notifyDataSetChanged(){
            for (int position = 0; position < viewList.size(); position++) {
                convert(viewList.get(position), getItemData(position), position);
            }
        }

        public void setViewList(List<View> viewList) {
            this.viewList = viewList;
        }

        public void setParent(CgOddLimitView cgOddLimitView){
            parent = cgOddLimitView;
        }
    }
}
