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

        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_next_item , null);
        ImageView jumpBt  = ll.findViewById(R.id.iv_guide_bank_jump);
        ImageView nextBt  = ll.findViewById(R.id.iv_guide_bank_next);
        ImageView previousBtn  = ll.findViewById(R.id.iv_guide_bank_pro);

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
