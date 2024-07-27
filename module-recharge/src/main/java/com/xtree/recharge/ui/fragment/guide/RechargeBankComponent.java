package com.xtree.recharge.ui.fragment.guide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

/**
 * 付款银行卡 引导页面
 */
public class RechargeBankComponent  implements Component {
    /**
    *付款银行卡 引导页面 页面点击回调
     * */
    public interface  IRechargeBankCallback{
        /**
         * 跳过
         */
        public void rechargeBankJump();
        /**
         * 下一步
         */
        public void rechargeBankNext();
    }
    private IRechargeBankCallback iRechargeBankCallback ;
    private Context context;

    public RechargeBankComponent(final Context context ,IRechargeBankCallback iRechargeBankCallback){
        super();
        this.context = context;
        this.iRechargeBankCallback = iRechargeBankCallback ;
    }
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_bank_item , null);
        ImageView jumpBt  = ll.findViewById(R.id.iv_guide_bank_jump);
        ImageView nextBt  = ll.findViewById(R.id.iv_guide_bank_next);
        //跳过点击事件
        jumpBt.setOnClickListener(v -> {
            if (this.iRechargeBankCallback !=null){
                this.iRechargeBankCallback.rechargeBankJump();
            }
        });
        //下一步
        nextBt.setOnClickListener(v -> {
            if (this.iRechargeBankCallback !=null){
                this.iRechargeBankCallback.rechargeBankNext();
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
        return 0;
    }
}
