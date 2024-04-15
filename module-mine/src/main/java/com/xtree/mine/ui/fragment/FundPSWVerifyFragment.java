package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.FragmentFundPasswordVerifyBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.vo.FundPassWordVerifyVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**魔域输入资金密码/谷歌验证码*/
public class FundPSWVerifyFragment extends BottomPopupView {
    private static final int PS_LENGTH = 6; //资金密码长度为6
    //默认串码 请求失败后，二次请求时候要用第一次默认串码请求
    private static final String defaultID = UuidUtil.getID24();

    public interface IFundPWVerifyCallBack {
        void closeFundPWDialog();

        void closeFundPWDialogWithCode(final String checkCode);//联网获取数据成功，将返回的code带回上个页面
    }
    private FragmentFundPasswordVerifyBinding binding ;
    private ChooseWithdrawViewModel viewModel;
    private FundPassWordVerifyVo vo;
    private IFundPWVerifyCallBack iFundPassWordCallBack;
    private LifecycleOwner owner;

    public static FundPSWVerifyFragment newInstance(Context context, LifecycleOwner owner, IFundPWVerifyCallBack iFundPassWordCallBack) {
        FundPSWVerifyFragment dialog = new FundPSWVerifyFragment(context);
        dialog.owner = owner;
        dialog.iFundPassWordCallBack = iFundPassWordCallBack;
        return dialog;
    }

    public FundPSWVerifyFragment(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.fragment_fund_password_verify;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initData();
        initViewObservable();
    }

    private void initView() {
        binding = FragmentFundPasswordVerifyBinding.bind(findViewById(R.id.ll_fund_pw_verify_root));
        //关闭Dialog
        binding.ivwClose.setOnClickListener(v -> {
            dismiss();
            iFundPassWordCallBack.closeFundPWDialog();

        });

        //取消按钮
        binding.btnCancel.setOnClickListener(v -> {
            dismiss();
            iFundPassWordCallBack.closeFundPWDialog();
        });
        //确定按钮
        binding.btnSure.setOnClickListener(v -> {
            String inputString = binding.etPassword.getText().toString().trim();
            String inputVerifyString = binding.etVerify.getText().toString().toString();
            if (TextUtils.isEmpty(inputString)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_err));
            } else if (inputString.length() != PS_LENGTH) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_length_err));
            } else if (!StringUtils.isNumber(inputString)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_char_err));
            } else if (TextUtils.isEmpty(inputVerifyString)) {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_input_verify_err));
            } else {
                LoadingDialog.show(getContext());
                //联网
                getCheckPasswordAndVerify(inputString ,inputVerifyString);
            }
        });
        //下拉关闭 Dialog
        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                if (iFundPassWordCallBack != null) {
                    iFundPassWordCallBack.closeFundPWDialog();
                }
            }

            @Override
            public void onDrag(int y, float percent, boolean isScrollUp) {

            }

            @Override
            public void onOpen() {

            }
        });

    }

    private void initData() {
        viewModel = new ChooseWithdrawViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        viewModel.fundPassWordVerifyVoMutableLiveData.observe(owner, vo -> {
            this.vo = vo;
            if (vo != null) {
                if (!TextUtils.isEmpty(this.vo.status)) {
                    //返回正常
                    if ("1".equals(this.vo.status) && !TextUtils.isEmpty(this.vo.msg.checkcode)) {
                        iFundPassWordCallBack.closeFundPWDialogWithCode(this.vo.msg.checkcode);
                    } else if (!TextUtils.isEmpty(this.vo.message)) {
                        ToastUtils.showError(this.vo.message);
                    } else {
                        //返回异常
                        ToastUtils.showError(getContext().getString(R.string.txt_network_error));
                    }
                } else if (("2").equals(this.vo.msg_type) && (("资金密码错误").equals(this.vo.message))) {
                    ToastUtils.showError(this.vo.message);
                }
            } else {
                //返回异常
                ToastUtils.showError(getContext().getString(R.string.txt_network_error));
            }

        });
    }

    private HashMap initRequestMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "check");
        map.put("nextact", "bindsequestion");
        map.put("nextcon", "user");

        return map;
    }

    private void getCheckPasswordAndVerify(final  String secPass , final String code ){
        HashMap<String, String> map = initRequestMap();
        map.put("nonce", defaultID);
        map.put("secpass", secPass);
        map.put("code" , code);
        viewModel.getCheckPassAndVerify(map);
    }
}
