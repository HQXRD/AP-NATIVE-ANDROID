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
    private TextView jumpBtn ;
    private TextView nextBtn;

    public RechargeBankComponent(IRechargeBankCallback iRechargeBankCallback){
        super();
        this.iRechargeBankCallback = iRechargeBankCallback ;
    }
    @Override
    public View getView(LayoutInflater inflater) {
        /*LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.dialog_recharge_component_bank, null);
        jumpBtn = ll.findViewById(R.id.tvw_jump);
        nextBtn = ll.findViewById(R.id.tvw_next);

        //跳过点击事件
        jumpBtn.setOnClickListener(v -> {
            if (this.iRechargeBankCallback !=null){
                this.iRechargeBankCallback.rechargeBankJump();
            }
        });
        //下一步
        nextBtn.setOnClickListener(v -> {
            if (this.iRechargeBankCallback !=null){
                this.iRechargeBankCallback.rechargeBankNext();
            }
        });*/

        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);

        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.mipmap.re_bank_down_left_arrow);

        TextView textView = new TextView(inflater.getContext());
        textView.setText(R.string.txt_recharge_bank);
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        textView.setTextSize(14);
        //button承载View
        LinearLayout llBtn = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams paramBtn =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        llBtn.setOrientation(LinearLayout.HORIZONTAL);
        llBtn.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.red));
        llBtn.setLayoutParams(paramBtn);

        //跳过
        /*Button jumpBt = new Button(inflater.getContext());
        jumpBt.setText(R.string.txt_recharge_view_jump);
        jumpBt.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        jumpBt.setTextSize(12);
        jumpBt.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.bg_btn_rechager_jump));

        // 下一步
        Button nextBt = new Button(inflater.getContext());
        nextBt.setText(R.string.txt_recharge_view_next);
        nextBt.setTextColor(inflater.getContext().getResources().getColor(R.color.clr_edt_focused2));
        nextBt.setTextSize(12);
        nextBt.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.bg_btn_rechager_next));*/

       /* llBtn.removeAllViews();
        llBtn.addView(jumpBt);
        llBtn.addView(nextBt);*/

        ll.removeAllViews();

        ll.addView(imageView);
        ll.addView(textView);
        llBtn.removeAllViews();
        ll.addView(llBtn);
       /* ll.addView(jumpBt);
        ll.addView(nextBt);*/

        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ToastUtils.showError("引导层被点击了");
            }
        });
       /* //跳过点击事件
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
        });*/
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
