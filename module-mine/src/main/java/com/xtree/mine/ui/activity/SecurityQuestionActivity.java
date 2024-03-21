package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_ACCOUNT_SECURITY;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentSecurityQuestionBinding;
import com.xtree.mine.ui.fragment.EnterAnswerDialog;
import com.xtree.mine.ui.fragment.FundPassWordFragment;
import com.xtree.mine.ui.fragment.SecurityQuestionFragment;
import com.xtree.mine.ui.viewmodel.SetQuestionsPwdModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.CheckQuestionVo;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/*密保Activity*/
@Route(path = PAGER_ACCOUNT_SECURITY)
public class SecurityQuestionActivity extends BaseActivity<FragmentSecurityQuestionBinding, SetQuestionsPwdModel> {
    private CheckQuestionVo checkQuestionVo;
    private BasePopupView showAnswerView;
    private BasePopupView showSetPSWView;

    private BasePopupView basePopupView;
    private ProfileVo mProfileVo;
    private String ques_num;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_security_question;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        if (mProfileVo == null) {
            return;
        }
        //未设置密保
        if (mProfileVo.has_securitypwd && (mProfileVo.set_question == null || !(mProfileVo.set_question instanceof ArrayList))) {
            showFundPSWDialog();
            return;
        }
        if (mProfileVo.set_question == null) {
            CfLog.e("mProfileVo.set_question == null");
            return;
        }

        ArrayList list = (ArrayList<?>) mProfileVo.set_question;
        //设置了密保
        if (list.size() != 3) {
            CfLog.e("****** ");
            return;
        }

        String key1 = String.valueOf(((ArrayList<?>) mProfileVo.set_question).get(0)).split("\\.")[0];
        String key2 = String.valueOf(((ArrayList<?>) mProfileVo.set_question).get(1)).split("\\.")[0];
        ques_num = String.valueOf(((ArrayList<?>) mProfileVo.set_question).get(2)).split("\\.")[0];//要显示问题
        String showContent = null;
        if (ques_num.equals("1")) {
            for (int i = 0; i < createQA().size(); i++) {
                if (key1.equals(createQA().get(i).key)) {
                    showContent = createQA().get(i).content;
                }
            }
        } else if (ques_num.equals("2")) {
            for (int i = 0; i < createQA().size(); i++) {
                if (key2.equals(createQA().get(i).key)) {
                    showContent = createQA().get(i).content;
                }
            }
        }
        showEnterSecretAnswerDialog(ques_num, showContent);
        //已设置密保显示密保Dialog
    }

    @Override
    public SetQuestionsPwdModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(SetQuestionsPwdModel.class);
    }

    @Override
    public void initViewObservable() {
        //检查
        viewModel.checkQuestionVoMutableLiveData.observe(this, vo -> {
            checkQuestionVo = vo;
            if (checkQuestionVo.data != null) {
                if (showAnswerView != null) {
                    showAnswerView.dismiss();
                }
                // checkQuestionVo.data.accessToken ;
                //跳转设置密保页面
                showSetPSWView(false, checkQuestionVo.data.accessToken);
            } else {
                if (TextUtils.isEmpty(checkQuestionVo.message)) {
                    ToastUtils.showError(this.getString(R.string.txt_network_error));
                } else {
                    ToastUtils.showError(checkQuestionVo.message);
                }

            }

        });
    }

    /*显示输入密保答案Dialog*/
    private void showEnterSecretAnswerDialog(final String key3, final String showContent) {
        if (showAnswerView == null) {
            showAnswerView = new XPopup.Builder(this).asCustom(new EnterAnswerDialog(this, key3, showContent, new EnterAnswerDialog.ICallBack() {
                @Override
                public void onClickCancel() {
                    showAnswerView.dismiss();
                    finish();
                }

                @Override
                public void onClickSure(String key, String answer, String quesNum) {
                    verifyQuestion(quesNum, answer);
                }
            }));
        }
        showAnswerView.show();
    }

    /*检查密保问题*/
    private void verifyQuestion(String dna_ques, final String answer) {
        HashMap<String, String> map = new HashMap<>();
       /* {
            "ans": "刘备",
                "dna_ques": 8,
                "flag": "check",
                "nextact": "bindsequestion",
                "nextcon": "user",
                "nonce": "24e1c5c512d6cda114efa987c0d994dc",
                "ques_num": 2
        }*/
        map.put("flag", "check");
        map.put("nextact", "bindsequestion");
        map.put("nextcon", "user");
        map.put("nonce", UuidUtil.getID24());
        map.put("ans", answer);
        //问题编号
        for (int i = 0; i < createQA().size(); i++) {
            if (dna_ques.equals(createQA().get(i).content)) {
                dna_ques = createQA().get(i).key;
            }
        }
        map.put("dna_ques", dna_ques);
        map.put("ques_num", ques_num);
        CfLog.e("verifyQuestion = " + map);

        LoadingDialog.show(this);
        if (viewModel == null) {
            CfLog.e("viewMode == null");
        } else {
            viewModel.postCheckQuestion(map);
        }

    }

    /*显示密保设定*/
    private void showSetPSWView(final boolean hide, final String accessToken) {
        if (showSetPSWView == null) {
            if (hide) {
                showSetPSWView = new XPopup.Builder(this).dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .moveUpToKeyboard(false)
                        .asCustom(SecurityQuestionFragment.newInstance(this, this, new SecurityQuestionFragment.ISecurityQuestionCallBack() {
                            @Override
                            public void closeSecurityDialog() {
                                showSetPSWView.dismiss();
                                finish();//关闭Activity
                            }
                        }, true));
            } else {
                showSetPSWView = new XPopup.Builder(this).dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .moveUpToKeyboard(false)
                        .asCustom(SecurityQuestionFragment.newInstance(this, this, new SecurityQuestionFragment.ISecurityQuestionCallBack() {
                            @Override
                            public void closeSecurityDialog() {
                                showSetPSWView.dismiss();
                                finish();//关闭Activity
                            }
                        }, accessToken));
            }

        }
        showSetPSWView.show();
    }

    /*显示魔域资金密码输入页面*/
    private void showFundPSWDialog() {
        if (basePopupView == null) {
            basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .moveUpToKeyboard(false)
                    .asCustom(FundPassWordFragment.newInstance(this, this, new FundPassWordFragment.IFundPassWordCallBack() {
                        @Override
                        public void closeFundPWDialog() {
                            //showNetError();
                            finish();
                        }

                        @Override
                        public void closeFundPWDialogWithCode(String checkCode) {
                            if (!TextUtils.isEmpty(checkCode)) {
                                showSetPSWView(true, "");
                            }
                            basePopupView.dismiss();
                        }

                    }));
        }

        basePopupView.show();
    }

    public final ArrayList<QABean> createQA() {
        ArrayList<QABean> qaBeanArrayList = new ArrayList<>();
        qaBeanArrayList.add(new QABean("4", "您母亲的姓名是？"));
        qaBeanArrayList.add(new QABean("8", "您配偶的生日？"));
        qaBeanArrayList.add(new QABean("13", "您的学号（或工号）是？？"));
        qaBeanArrayList.add(new QABean("5", "您母亲的生日是？"));
        qaBeanArrayList.add(new QABean("12", "您高中班主任的名字是？"));
        qaBeanArrayList.add(new QABean("1", "您父亲的姓名是？"));
        qaBeanArrayList.add(new QABean("10", "您小学班主任的名字是？"));
        qaBeanArrayList.add(new QABean("2", "您父亲的生日是？"));
        qaBeanArrayList.add(new QABean("7", "您配偶的姓名是？"));
        qaBeanArrayList.add(new QABean("11", "您初中班主任的名字是？"));
        qaBeanArrayList.add(new QABean("16", "您最熟悉的童年好友名字是？"));
        qaBeanArrayList.add(new QABean("17", "您最熟悉的学校宿舍室友名字是？"));
        qaBeanArrayList.add(new QABean("18", "您影响最大的人名字是？"));

        return qaBeanArrayList;
    }

    // 密保问题答案bean类型
    public class QABean {
        public QABean(final String key, final String content) {
            super();
            this.key = key;
            this.content = content;
        }

        public String content;
        public String key;
        public String q;
        public String a;
    }
}
