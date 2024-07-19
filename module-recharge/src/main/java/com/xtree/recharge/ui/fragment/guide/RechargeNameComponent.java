package com.xtree.recharge.ui.fragment.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 充值 姓名 引导 页面
 */
public class RechargeNameComponent implements Component {
    /**
     *付款银行卡 引导页面 页面点击回调
     * */
    public interface  IRechargeNameCallback{
        /**
         * 上一步
         */
        public void  rechargeNamePrevious();
        /**
         * 跳过
         */
        public void rechargeNameJump();
        /**
         * 下一步
         */
        public void rechargeNameNext();
    }
    private IRechargeNameCallback iRechargeNameCallback ;

    public RechargeNameComponent(IRechargeNameCallback iRechargeNameCallback){
        super();
        this.iRechargeNameCallback = iRechargeNameCallback ;
    }
    
    
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        TextView textView = new TextView(inflater.getContext());
        textView.setText(R.string.txt_recharge_name);
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        textView.setTextSize(20);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.mipmap.re_name_down_right_arrow);
        ll.removeAllViews();
        ll.addView(textView);
        ll.addView(imageView);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ToastUtils.showError("引导层被点击了");
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
