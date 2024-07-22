package com.xtree.recharge.ui.fragment.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 充值金额  下一步 引导页面
 */
public class RechargeNextComponent implements Component {

    /**
     *付款银行卡 引导页面 页面点击回调
     * */
    public interface  IRechargeNextCallback{
        /**
         * 上一步
         */
        public void  rechargeNextPrevious();
        /**
         * 跳过
         */
        public void rechargeNextJump();
        /**
         * 下一步
         */
        public void rechargeNextNext();
    }
    private IRechargeNextCallback iRechargeNextCallback ;

    public RechargeNextComponent(IRechargeNextCallback iRechargeNextCallback){
        super();
        this.iRechargeNextCallback = iRechargeNextCallback ;
    }

    @Override
    public View getView(LayoutInflater inflater) {

        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);

        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.mipmap.re_next_up_right_arrow);

        TextView textView = new TextView(inflater.getContext());
        textView.setText(R.string.txt_recharge_nex);
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        textView.setTextSize(14);
        // 上一步
        Button previousBtn =  new Button(inflater.getContext());
        previousBtn.setText(R.string.txt_recharge_view_pro);
        previousBtn.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        previousBtn.setTextSize(12);
        previousBtn.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.bg_btn_rechager_jump));
        //跳过
        Button jumpBt = new Button(inflater.getContext());
        jumpBt.setText(R.string.txt_recharge_view_jump);
        jumpBt.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        jumpBt.setTextSize(12);
        jumpBt.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.bg_btn_rechager_jump));

        // 下一步
        Button nextBt = new Button(inflater.getContext());
        nextBt.setText(R.string.txt_recharge_view_next);
        nextBt.setTextColor(inflater.getContext().getResources().getColor(R.color.clr_edt_focused2));
        nextBt.setTextSize(12);
        nextBt.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.bg_btn_rechager_next));

        ll.removeAllViews();

        ll.addView(imageView);
        ll.addView(textView);
        ll.addView(previousBtn);
        ll.addView(jumpBt);
        ll.addView(nextBt);
        //上一步
        previousBtn.setOnClickListener(v->{
            if (this.iRechargeNextCallback !=null){
                this.iRechargeNextCallback.rechargeNextPrevious();
            }
        });

        //跳过点击事件
        jumpBt.setOnClickListener(v -> {
            if (this.iRechargeNextCallback !=null){
                this.iRechargeNextCallback.rechargeNextJump();
            }
        });
        //下一步
        nextBt.setOnClickListener(v -> {
            if (this.iRechargeNextCallback !=null){
                this.iRechargeNextCallback.rechargeNextNext();
            }
        });
        return ll;



    }


    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 20;
    }
}
