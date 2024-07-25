package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

/**
 * 充值 确认付款 等待到账 引导页面
 */

public class ExTransferPayeeComponent implements Component {

    /**
     *充值 确认付款 等待到账 引导页面 页面点击回调
     * */
    public interface  IExTransferPayeeCallback{
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
    private IExTransferPayeeCallback iExTransferPayeeCallback ;
    public ExTransferPayeeComponent(IExTransferPayeeCallback iExTransferPayeeCallback){
        super();
        this.iExTransferPayeeCallback = iExTransferPayeeCallback ;
    }
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_bank_item, null);
        ImageView jumpBt  = ll.findViewById(R.id.iv_guide_bank_jump);
        ImageView nextBt  = ll.findViewById(R.id.iv_guide_bank_next);
        ImageView previousBtn  = ll.findViewById(R.id.iv_guide_bank_pro);
        //上一步
        previousBtn.setOnClickListener(v->{
            if (this.iExTransferPayeeCallback !=null){
                this.iExTransferPayeeCallback.rechargeNamePrevious();
            }
        });

        //跳过点击事件
        jumpBt.setOnClickListener(v -> {
            if (this.iExTransferPayeeCallback !=null){
                this.iExTransferPayeeCallback.rechargeNameJump();
            }
        });
        //下一步
        nextBt.setOnClickListener(v -> {
            if (this.iExTransferPayeeCallback !=null){
                this.iExTransferPayeeCallback.rechargeNameNext();
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
