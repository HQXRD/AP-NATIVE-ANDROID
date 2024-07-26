package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;
import com.xtree.recharge.R;

public class CommotActivity extends Activity {
    private ImageView testView ;
    LinearLayout ll ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_extransfer_commit_guide);
        /*ll = (LinearLayout) findViewById(R.id.ll_test);
        testView = (ImageView) findViewById(R.id.iv_rc_exp_bank_top1);*/

        showGuideByNext();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    private Guide nextGuide;
    /**
     * 显示银行卡 充值 下一步 引导页面
     */
    private void showGuideByNext(){

        GuideBuilder builder = new GuideBuilder();
       /* builder.setTargetView((ImageView) findViewById(R.id.iv_rc_exp_bank_top1))
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setOverlayTarget(true)
                .setAutoDismiss(false);*/

        builder.addComponent(new TestRechargeNameComponent(new TestRechargeNameComponent.IRechargeNameCallback() {
            @Override
            public void rechargeNamePrevious() {
                //showGuideByBank(vo);
                dismissNextGuide();

            }

            @Override
            public void rechargeNameJump() {
                //跳过
                //shipGuide();
                dismissNextGuide();
            }

            @Override
            public void rechargeNameNext() {
                dismissNextGuide();
                //showGuideByMoney(vo , false);
            }

        }));
        nextGuide = builder.createGuide();
        nextGuide.setShouldCheckLocInWindow(false);
        nextGuide.show(this );
       /* GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(ll)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);

        builder.addComponent(new ExTransferCommitComponent(new ExTransferCommitComponent.IExTransferCommitCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissNextGuide();
                finish();
            }

            @Override
            public void rechargeNameJump() {
                dismissNextGuide();

            }

            @Override
            public void rechargeNameNext() {
                dismissNextGuide();
                //跳转充值银行卡上传凭证信息页面
                //startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE_GUI);
            }

        }));
        nextGuide = builder.createGuide();
        nextGuide.show(this);*/
    }
    private void  dismissNextGuide(){
        if (nextGuide !=null){
            nextGuide.dismiss();;
            nextGuide = null;
        }
    }
}
