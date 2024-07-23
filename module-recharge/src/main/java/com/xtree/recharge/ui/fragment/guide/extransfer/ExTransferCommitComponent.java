package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;
import com.xtree.recharge.ui.fragment.guide.RechargeNameComponent;

/**
 * 充值 取消等待 引导页面
 */

public class ExTransferCommitComponent implements Component {

    /**
     *付款银行卡 引导页面 页面点击回调
     * */
    public interface  IExTransferCommitCallback{
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
    private IExTransferCommitCallback iExTransferCommitCallback ;
    public ExTransferCommitComponent(IExTransferCommitCallback iExTransferCommitCallback){
        super();
        this.iExTransferCommitCallback = iExTransferCommitCallback ;
    }
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_ex_transfer_commit_item , null);
        ImageView jumpBt  = ll.findViewById(R.id.iv_guide_bank_jump);
        ImageView nextBt  = ll.findViewById(R.id.iv_guide_bank_next);
        ImageView previousBtn  = ll.findViewById(R.id.iv_guide_bank_pro);
        //上一步
        previousBtn.setOnClickListener(v->{
            if (this.iExTransferCommitCallback !=null){
                this.iExTransferCommitCallback.rechargeNamePrevious();
            }
        });

        //跳过点击事件
        jumpBt.setOnClickListener(v -> {
            if (this.iExTransferCommitCallback !=null){
                this.iExTransferCommitCallback.rechargeNameJump();
            }
        });
        //下一步
        nextBt.setOnClickListener(v -> {
            if (this.iExTransferCommitCallback !=null){
                this.iExTransferCommitCallback.rechargeNameNext();
            }
        });
        return ll;
    }

    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 0;
    }
}
