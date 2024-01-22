package com.xtree.mine.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.MsgCenterDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentGooglePwdBinding;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.GooglePswVO;
import com.xtree.mine.vo.ProfileVo;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Google动态口令
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_GOOGLE_PWD)
public class GooglePwdFragment extends BaseFragment<FragmentGooglePwdBinding, VerifyViewModel> {
    //自定义二维码长宽
    private int height = 120;
    private int width = 120;
    private Bitmap bitmap;
    BasePopupView ppw = null;

    BasePopupView basePopupView = null;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache();
        viewModel.getProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.readCache();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_google_pwd;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VerifyViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(VerifyViewModel.class);
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.ivwCode1.setBackgroundResource(project.tqyb.com.library_res.R.mipmap.me_google_qrcode_android);
        binding.tvwPrompt3.setText("密钥: ");
        HashMap<String, String> map = new HashMap<>();
        viewModel.bindVerifySecrets(map);

        //绑定密钥按钮点击
        binding.btnBind.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edtGooglepsw.getText())) {
                ToastUtils.showLong("请先输入谷歌验证码");
            }
            //输入的不是数字
            else if (checkInputNum(binding.edtGooglepsw.getText().toString())) {
                showErrorDialog();
            }
            //输入的不是6位
            else if (!checkInputLength(binding.edtGooglepsw.getText().toString())) {
                showErrorDialog();
            } else {
                HashMap<String, String> map1 = new HashMap<>();
                String num = binding.edtGooglepsw.getText().toString().trim();
                map1.put("code", num);
                map1.put("nonce", UuidUtil.getID16());//传入UUID
                viewModel.bindVerifyGoogle(map1);
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

        //谷歌验证码获取文本
        viewModel.liveDataBindGoogleVerifyStr.observe(this, vo -> {
            GooglePswVO googlePswVO = vo;
            buildGoogleCode(googlePswVO.secret);
        });
        //谷歌动态码绑定
        viewModel.liveDataBindGoogleVerify.observe(this, vo -> {
            CfLog.i("***********");
            //返回上一级页面
            getActivity().finish();
        });
    }

    private void buildGoogleCode(String secre) {
        binding.tvwPrompt3.setText("密钥： " + secre);
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        //拼接二维码形式
        final String qrCode = "otpauth://totp" + "/as-" + mProfileVo.username + "?secret=" + secre;
        zxing(qrCode);
    }


    /**
     * 生产二维码
     *
     * @param name
     */
    private void zxing(String name) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//自定义长宽
        BitMatrix endCode = null;
        try {
            endCode = qrCodeWriter.encode(name, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            CfLog.e(e.toString());
        }

        endCode = updateBit(endCode, 0);
        int newWidth = endCode.getWidth();
        int newHeight = endCode.getHeight();

        int[] colors = new int[newWidth * newHeight];

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                if (endCode.get(i, j)) {
                    colors[i * newWidth + j] = Color.BLACK;
                } else {
                    colors[i * newWidth + j] = Color.WHITE;
                }
            }
        }
        bitmap = Bitmap.createBitmap(colors, newWidth, newHeight, Bitmap.Config.RGB_565);
        binding.ivwCode2.setImageBitmap(bitmap);

    }

    /**
     * 自定义生成BitMatrix
     *
     * @param matrix
     * @param margin
     * @return
     */
    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] res = matrix.getEnclosingRectangle();
        int resWidth = res[2] + tempM;
        int resHeight = res[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth - margin; i++) {
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + res[0], j - margin + res[1])) {
                    resMatrix.set(i, j);
                }
            }
        }

        return resMatrix;
    }

    /**
     * 绑定谷歌动态二维码错误提示
     */
    private void showErrorDialog() {
        String title = getString(R.string.txt_kind_tips);
        String msg = String.valueOf(R.string.txt_google_auth_fail);
        basePopupView = new XPopup.Builder(getContext()).asCustom(new MsgCenterDialog(getContext(), title, msg, () -> basePopupView.dismiss()));
        basePopupView.show();
    }

    /**
     * 判断輸入是否为纯数字
     *
     * @param inputString
     * @return true-是
     */
    private boolean checkInputNum(String inputString) {
        return inputString.trim().matches("-?\\\\d+(\\\\.\\\\d+)?");
    }

    /**
     * 判断输入数据是否为6位
     *
     * @param inputString
     * @return ture-是 false-否
     */
    private boolean checkInputLength(String inputString) {
        if (inputString.trim().length() == 6) {
            return true;
        }

        return false;
    }

}