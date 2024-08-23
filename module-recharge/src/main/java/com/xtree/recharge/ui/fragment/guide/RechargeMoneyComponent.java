package com.xtree.recharge.ui.fragment.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

/**
 * 充值金额 引导页面
 */
public class RechargeMoneyComponent implements Component {

    /**
     *付款银行卡 引导页面 页面点击回调
     * */
    public interface  IRechargeMoneyCallback{
        /**
         * 上一步
         */
        public void  rechargeMoneyPrevious();
        /**
         * 跳过
         */
        public void rechargeMoneyJump();
        /**
         * 下一步
         */
        public void rechargeMoneyNext();
    }
    private IRechargeMoneyCallback iRechargeMoneyCallback ;

    public RechargeMoneyComponent(IRechargeMoneyCallback iRechargeMoneyCallback){
        super();
        this.iRechargeMoneyCallback = iRechargeMoneyCallback ;
    }
    @Override
    public View getView(LayoutInflater inflater) {


        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_money_item , null);
        ImageView jumpBt  = ll.findViewById(R.id.iv_guide_bank_jump);
        ImageView nextBt  = ll.findViewById(R.id.iv_guide_bank_next);
        ImageView previousBtn  = ll.findViewById(R.id.iv_guide_bank_pro);
        //上一步
        previousBtn.setOnClickListener(v->{
            if (this.iRechargeMoneyCallback !=null){
                this.iRechargeMoneyCallback.rechargeMoneyPrevious();
            }
        });

        //跳过点击事件
        jumpBt.setOnClickListener(v -> {
            if (this.iRechargeMoneyCallback !=null){
                this.iRechargeMoneyCallback.rechargeMoneyJump();
            }
        });
        //下一步
        nextBt.setOnClickListener(v -> {
            if (this.iRechargeMoneyCallback !=null){
                this.iRechargeMoneyCallback.rechargeMoneyNext();
            }
        });
        return ll;
    }


    @Override public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 0;
    }
}
