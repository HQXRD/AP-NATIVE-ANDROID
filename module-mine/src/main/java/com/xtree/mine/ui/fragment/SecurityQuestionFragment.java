package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.MD5Util;
import com.xtree.base.utils.RSAEncrypt;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogSetSecurityQuestionBinding;
import com.xtree.mine.ui.viewmodel.SetQuestionsPwdModel;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/*设定密保*/
public class SecurityQuestionFragment extends BottomPopupView {

    public interface ISecurityQuestionCallBack {
        void closeSecurityDialog();//关闭设定密保
    }

    private BasePopupView reSetFundView;
    private BasePopupView questionPPView1;
    private BasePopupView questionPPView2;

    private LifecycleOwner owner;
    private String accessToken;
    private String checkCode;//第一次设定密保问题使用chekCode
    private SetQuestionsPwdModel viewModel;
    private ISecurityQuestionCallBack callBack;
    private DialogSetSecurityQuestionBinding binding;
    private ItemTextBinding binding2;
    private boolean hide;
    private ProfileVo mProfileVo;

    private SecurityQuestionFragment(@NonNull Context context) {
        super(context);
    }

    public static SecurityQuestionFragment newInstance(@NonNull Context context, LifecycleOwner owner, final ProfileVo profileVo, ISecurityQuestionCallBack callBack, final String accessToken) {
        SecurityQuestionFragment dialog = new SecurityQuestionFragment(context);
        dialog.owner = owner;
        dialog.mProfileVo = profileVo;
        dialog.callBack = callBack;
        dialog.accessToken = accessToken;//重置资金密码使用
        return dialog;
    }

    public static SecurityQuestionFragment newInstance(@NonNull Context context, LifecycleOwner owner, ISecurityQuestionCallBack callBack, boolean hide, final String checkCode) {
        SecurityQuestionFragment dialog = new SecurityQuestionFragment(context);
        dialog.owner = owner;
        dialog.callBack = callBack;
        dialog.hide = hide;
        dialog.checkCode = checkCode;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        createQA();
        initView();
        initViewObservable();
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
    }

    private void initData() {
        viewModel = new SetQuestionsPwdModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initView() {
        binding = DialogSetSecurityQuestionBinding.bind(findViewById(R.id.ll_root_set_security_question));
        binding.ivwClose.setOnClickListener(v -> {
            dismiss();
            callBack.closeSecurityDialog();
        });
        if (hide) {
            binding.tvwRetrieveFundPassword.setVisibility(View.GONE);
            //第一次 设定密保 createQA()
            binding.tvwQuestion1.setText(createQA().get(0).content);
            binding.tvwQuestion2.setText(createQA().get(1).content);

        } else {
            ArrayList list = (ArrayList<?>) mProfileVo.set_question;
            //设置了密保
            if (list.size() != 3) {
                CfLog.e("****** ");
                return;
            }
            String key1 = String.valueOf(((ArrayList<?>) mProfileVo.set_question).get(0)).split("\\.")[0];
            String key2 = String.valueOf(((ArrayList<?>) mProfileVo.set_question).get(1)).split("\\.")[0];
            for (int i = 0; i < createQA().size(); i++) {
                if (key1.equals(createQA().get(i).key)) {
                    binding.tvwQuestion1.setText(createQA().get(i).content);
                }
                if (key2.equals(createQA().get(i).key)) {
                    binding.tvwQuestion2.setText(createQA().get(i).content);
                }
            }

        }

        //找回资金密码
        binding.tvwRetrieveFundPassword.setOnClickListener(v -> {
            showRetrieveFundDialog();
        });
        //设定密保
        binding.tvwResetPasswordNext.setOnClickListener(v -> {
            String firstAnswer = binding.etAnswer1.getText().toString().trim();
            String secondAnswer = binding.etAnswer2.getText().toString().trim();
            if (TextUtils.isEmpty(firstAnswer) || TextUtils.isEmpty(secondAnswer)) {
                ToastUtils.showError(getContext().getString(R.string.txt_fund_error_tip));
            } else if (binding.tvwQuestion1.getText().toString().trim().equals(binding.tvwQuestion2.getText().toString().trim())) {
                ToastUtils.showError(getContext().getString(R.string.txt_fund_question_error_tip));
            } else {
                String firstQuestion1 = binding.tvwQuestion1.getText().toString().trim();
                String secondQuestion2 = binding.tvwQuestion2.getText().toString().trim();
                HashMap<String, String> firstMap = new HashMap<>();
                HashMap<String, String> secondMap = new HashMap<>();

                for (int i = 0; i < createQA().size(); i++) {
                    if (firstQuestion1.equals(createQA().get(i).content)) {

                        firstMap.put("q", createQA().get(i).key);
                        firstMap.put("a", firstAnswer);
                    } else if (secondQuestion2.equals(createQA().get(i).content)) {
                        secondMap.put("q", createQA().get(i).key);
                        secondMap.put("a", secondAnswer);
                    }
                }
                ArrayList questions = new ArrayList();
                questions.add(firstMap);
                questions.add(secondMap);
                LoadingDialog.show(getContext());
                setSecurityQuestions(questions);

            }
        });
        //选择问题1
        binding.tvwQuestion1.setOnClickListener(v -> {
            showQuestion1(binding.tvwQuestion1);
        });
        //选择问题2
        binding.tvwQuestion2.setOnClickListener(v -> {
            showQuestion2(binding.tvwQuestion2);
        });
    }

    private void initViewObservable() {
        //找回资金密码
        viewModel.response2MutableLiveData.observe(owner, vo -> {

            if (vo.message != null && TextUtils.equals("修改成功", vo.message)) {
                //修改成功后 刷新个人信息
                viewModel.getProfile();
                ToastUtils.show(vo.message, ToastUtils.ShowType.Success);
                if (reSetFundView != null) reSetFundView.dismiss();
            } else if (vo.message != null && !TextUtils.isEmpty(vo.message)) {
                ToastUtils.showError(vo.message);
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //设置密保
        viewModel.setQuestionLiveData.observe(owner, vo -> {

            if (vo.message != null && TextUtils.equals("success", vo.message)) {
                ToastUtils.show(getContext().getString(R.string.txt_set_fund_question_success_tip), ToastUtils.ShowType.Success);
                //设置密保成功后要刷新用户个人数据
                //LoadingDialog.show(getContext());
                // viewModel.getProfile();

            } else if (vo.message != null && TextUtils.isEmpty(vo.message)) {
                // "设置密保失败，请稍后再试",
                ToastUtils.showError(vo.message);

            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }
        });
        //设置密保后刷新用户信息
        viewModel.profileVoMutableLiveData.observe(owner, vo -> {
            /*if (vo != null && vo.set_question instanceof ArrayList) {
                ArrayList list = (ArrayList) vo.set_question;
                for (int i = 0; i < list.size(); i++) {
                    CfLog.e("设置密保后刷新用户信心 = " + list.get(i));
                }
            }*/
            callBack.closeSecurityDialog();
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_set_security_question;
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

    /*密保问题答案bean类型*/
    public static class QABean {
        public QABean(final String key, final String content) {
            super();
            this.key = key;
            this.content = content;
        }

        public String content;
        public String key;
        public String q;
        public String a;

        @Override
        public String toString() {
            return "QABean{" +
                    "content='" + content + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    /*显示重置资金密码*/
    private void showRetrieveFundDialog() {
        //ReSetFundPSWDialog
        if (reSetFundView == null) {
            reSetFundView = new XPopup.Builder(getContext())
                    .moveUpToKeyboard(false)
                    .asCustom(new ReSetFundPSWDialog(getContext(), new ReSetFundPSWDialog.ICallBack() {
                        @Override
                        public void onClickCancel() {
                            reSetFundView.dismiss();
                        }

                        @Override
                        public void onClickSure(String firstPSW, String secondPSW) {
                            retrieveFundPSW(firstPSW, secondPSW);
                        }
                    }));
        }
        reSetFundView.show();
    }

    /*重置资金密码*/
    private void retrieveFundPSW(String firstPSW, String secondPSW) {
        if (TextUtils.isEmpty(firstPSW) || TextUtils.isEmpty(secondPSW)) {
            ToastUtils.showError(getContext().getString(R.string.txt_retrieve_fund_tip));
            return;
        }
        String newsecpass = MD5Util.generateMd5("") + MD5Util.generateMd5(firstPSW);
        // String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
        String public_key = SPUtils.getInstance().getString("public_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB");
        String loginpass = RSAEncrypt.encrypt2(firstPSW, public_key);
        firstPSW = RSAEncrypt.encrypt2(newsecpass, public_key);
        secondPSW = RSAEncrypt.encrypt2(newsecpass, public_key);

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("flag", "gener");
        map.put("newsecpass", loginpass);
        map.put("pwd", loginpass);
        map.put("nonce", UuidUtil.getID24());
        LoadingDialog.show(getContext());
        CfLog.e("retrieveFundPSW = " + map);

        viewModel.postRetrievePSW(map);
    }

    /*设置密保*/
    private void setSecurityQuestions(ArrayList qaBeanArrayList) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("nonce", UuidUtil.getID24());
        if (hide) {
            //第一次设置密保
            map.put("check", checkCode);
        } else {
            //重置密保
            map.put("check", "-1");
        }
        map.put("questions", qaBeanArrayList);
        CfLog.e("setSecurityQuestions" + map);
        viewModel.putSecurityQuestions(map);
    }

    /*选择问题1*/
    private void showQuestion1(final TextView showTextView) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<QABean>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                QABean bean = get(position);

                binding2.tvwTitle.setText(bean.content);
                binding2.tvwTitle.setOnClickListener(v -> {
                    showTextView.setText(bean.content);
                    questionPPView1.dismiss();
                });
            }

        };
        adapter.addAll(createQA());
        if (questionPPView1 == null) {
            questionPPView1 = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getContext().getString(R.string.txt_fund_question_selector_tip), adapter, 80));
        }
        questionPPView1.show();
    }

    /*选择问题2*/
    private void showQuestion2(final TextView showTextView) {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<QABean>() {
            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                QABean bean = get(position);

                binding2.tvwTitle.setText(bean.content);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwQuestion2.setText(bean.content);
                    questionPPView2.dismiss();
                });
            }

        };
        adapter.addAll(createQA());
        if (questionPPView2 == null) {
            questionPPView2 = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), getContext().getString(R.string.txt_fund_question_selector_tip), adapter, 80));
        }
        questionPPView2.show();
    }
}
