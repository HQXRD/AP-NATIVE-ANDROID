package com.xtree.mine.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.utils.AppUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.ImageUploadUtil
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.base.widget.GlideEngine
import com.xtree.base.widget.ImageFileCompressEngine
import com.xtree.base.widget.LoadingDialog
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentBindAddAwBinding
import com.xtree.mine.ui.viewmodel.BindCardViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.UserBankConfirmVo
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.ImageUtils
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import java.io.File


/**
 * 绑定支付宝、微信
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_AW_ADD)
class BindAWAddFragment : BaseFragment<FragmentBindAddAwBinding, BindCardViewModel>() {

    private var name: String = ""
    private var phone: String = ""
    private var nickName: String = ""
    private val controller = "security"
    private var action = ""
    private var qrcodeType = 0
    private var tokenSign: String? = null
    private var mark: String? = null
    private var accountName: String? = null
    private lateinit var mConfirmVo: UserBankConfirmVo
    private var imageRealPathString: String? = null //选择的图片地址
    private var imageSelector = false //是否已选择图片
    private var imageUri: Uri? = null
    private var typeName = ""
    var mProfileVo: ProfileVo? = null
    var digitCount = 0

    companion object {
        private const val ARG_TOKEN_SIGN = "tokenSign"
        private const val ARG_MARK = "mark"
    }

    override fun initView() {
        initArguments()
        binding.llRoot.setOnClickListener { hideKeyBoard() }
        binding.ivwBack.setOnClickListener {
            if (binding.llConfirm.visibility == View.VISIBLE) {
                binding.llAdd.visibility = View.VISIBLE
                binding.llConfirm.visibility = View.GONE
            } else {
                requireActivity().finish()
            }
        }
        binding.llRemittanceScreenshot.setOnClickListener { gotoSelectMedia() }

        binding.ivwNext.setOnClickListener { doNext() }
        binding.tvwSubmit.setOnClickListener { doSubmit() }
        binding.tvwBack.setOnClickListener {
            if (binding.llConfirm.visibility == View.VISIBLE) {
                binding.llAdd.visibility = View.VISIBLE
                binding.llConfirm.visibility = View.GONE
            }
        }

        binding.tvPaymentCode.setOnClickListener {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_AW_TIP, arguments)
        }

        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        if (mProfileVo != null && digitCount > 0) {
            binding.llVerify.visibility = View.VISIBLE
            binding.llAdd.visibility = View.GONE
            binding.llConfirm.visibility = View.GONE
            binding.tvwOldName.visibility = View.GONE
            binding.edtOldName.visibility = View.GONE
            binding.tvwNameWarning.visibility = View.GONE
            binding.tvwOldAcc.text = resources.getText(R.string.txt_verify_addr)
            binding.edtOldAcc.inputType = InputType.TYPE_CLASS_TEXT
            binding.edtOldAcc.hint = resources.getText(R.string.txt_enter_wallet_addr)
            binding.tvwAccWarning.visibility = View.GONE
            binding.tvwAccWarning.hint = resources.getText(R.string.txt_verify_addr_warning)
        } else {
            binding.llVerify.visibility = View.VISIBLE
            binding.llAdd.visibility = View.GONE
            binding.llConfirm.visibility = View.GONE
        }
        binding.tvwOldBack.setOnClickListener { requireActivity().finish() }
        binding.tvwOldSubmit.setOnClickListener { doVerify() }
    }

    private fun initArguments() {
        if (arguments != null) {
            tokenSign = requireArguments().getString(ARG_TOKEN_SIGN)
            mark = requireArguments().getString(ARG_MARK)
            accountName = requireArguments().getString("accountName")
            digitCount = requireArguments().getInt("digitCount", 0)
            if (!accountName.isNullOrEmpty()) {
                binding.etName.setText(accountName)
                binding.etName.isEnabled = false
            }

            when (mark) {
                getString(R.string.txt_bind_zfb_type) -> {
                    typeName = getString(R.string.txt_alipay)
                    binding.tvwTitle.text = getString(R.string.txt_bind_alipay)
                    binding.tvName.text = "*".plus(getString(R.string.txt_alipay_name))
                    binding.tvAccount.text = "*".plus(getString(R.string.txt_alipay_phone))
                    binding.tvNickname.text = "*".plus(getString(R.string.txt_alipay_nickname))
                    binding.tvwName.text = getString(R.string.txt_alipay_name)
                    binding.tvwPhone.text = getString(R.string.txt_alipay_phone)
                    binding.tvwNickname.text = getString(R.string.txt_alipay_nickname)
                    binding.tvwCode.text = getString(R.string.txt_alipay_code)
                    action = "adduseronepayzfb"
                    qrcodeType = 2
                    binding.ivAwIcon.setImageResource(R.mipmap.ic_alipay)
                    binding.tvMsg.text = getString(R.string.txt_bind_alipay).plus(getString(R.string.txt_succ))
                    binding.etPhone.hint = "手机号码/电子邮件地址/支付宝ID"
                    binding.etPhone.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }

                getString(R.string.txt_bind_wechat_type) -> {
                    typeName = getString(R.string.txt_wechat)
                    binding.tvwTitle.text = getString(R.string.txt_bind_wechat)
                    binding.tvName.text = "*".plus(getString(R.string.txt_wechat_name))
                    binding.tvAccount.text = "*".plus(getString(R.string.txt_wechat_phone))
                    binding.tvNickname.text = "*".plus(getString(R.string.txt_wechat_nickname))
                    binding.tvwName.text = getString(R.string.txt_wechat_name)
                    binding.tvwPhone.text = getString(R.string.txt_wechat_phone)
                    binding.tvwNickname.text = getString(R.string.txt_wechat_nickname)
                    binding.tvwCode.text = getString(R.string.txt_wechat_code)
                    action = "adduseronepaywx"
                    qrcodeType = 1
                    binding.ivAwIcon.setImageResource(R.mipmap.ic_wechat)
                    binding.tvMsg.text = getString(R.string.txt_bind_wechat).plus(getString(R.string.txt_succ))
                    binding.etPhone.hint = "微信号/手机号码/QQ号码/邮箱地址"
                    binding.etPhone.inputType = InputType.TYPE_CLASS_PHONE
                }
            }
            binding.tvTipAccount.text = typeName.plus("绑定的账户格式")
            binding.etNickname.hint = getString(R.string.txt_input_nickname, typeName)
            binding.tvSelectorTipImage.text = getString(R.string.txt_upload_payment_code, typeName)
            binding.tvPaymentCode.text = getString(R.string.txt_get_payment_code, typeName)
        }
    }

    override fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_bind_add_aw
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): BindCardViewModel {
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[BindCardViewModel::class.java]
    }

    override fun initViewObservable() {
        viewModel.liveDataBindAWCheck.observe(this) { vo: UserBankConfirmVo ->
            CfLog.i("******")
            mConfirmVo = vo
            setConfirmView()
        }
        viewModel.liveDataBindAWResult.observe(this) { vo: UserBankConfirmVo ->
            CfLog.i("******")
            viewModel.getProfile()
            binding.layoutRecharge.visibility = View.VISIBLE
            binding.llConfirm.visibility = View.GONE
            val type = SPUtils.getInstance().getString(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, getString(R.string.txt_go_recharge))
            binding.tvType.text = type
            binding.tvType.setOnClickListener {
                when (type) {
                    getString(R.string.txt_go_recharge) -> {
                        val bundle = Bundle()
                        bundle.putBoolean("isShowBack", true)
                        startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle)
                        requireActivity().finish()
                    }

                    getString(R.string.txt_go_withdraw) -> {
                        val bundle = Bundle()
                        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW).withBundle("viewType", bundle)
                            .navigation()
                        requireActivity().finish()
                    }
                }
            }
        }

        viewModel.liveDataVerify.observe(this) { isSuccess: Boolean ->
            CfLog.i("******")
            if (isSuccess) {
                binding.llVerify.visibility = View.GONE
                binding.llAdd.visibility = View.VISIBLE
                binding.llConfirm.visibility = View.GONE
            }
        }
    }

    private fun setConfirmView() {
        binding.llAdd.visibility = View.GONE
        binding.llConfirm.visibility = View.VISIBLE
        binding.tvwNameContent.text = name
        binding.tvwPhoneContent.text = phone
        binding.tvwNicknameContent.text = nickName
        val bitmap = BitmapFactory.decodeFile(imageRealPathString)
        binding.ivCode.setImageBitmap(bitmap)
    }

    private fun doVerify() {
        val account = binding.edtOldAcc.text.toString().trim()
        val account_name = binding.edtOldName.text.toString().trim()
        if (mProfileVo!!.binding_usdt_info != null && digitCount > 0) {
            if (account.isEmpty()) {
                ToastUtils.showLong(R.string.txt_enter_verify_wallet_addr)
                return
            }
        }
        if (account.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_bank_num)
            return
        }
        if (account_name.isEmpty() && digitCount == 0) {
            ToastUtils.showLong(R.string.txt_enter_account_name)
            return
        }
        val qMap = HashMap<String, Any>()
        qMap["client"] = "m"
        qMap["1"] = 1
        val map = java.util.HashMap<String, Any?>()
        map["account"] = account
        if (mProfileVo != null && digitCount > 0) {
            map["account_name"] = ""
            map["is_digital"] = "1"
        } else {
            map["account_name"] = account_name
            map["is_digital"] = "0"
        }
        map["nonce"] = UuidUtil.getID16()
        viewModel.doVerify(qMap, map)
    }

    private fun doNext() {
        name = binding.etName.text.toString().trim()
        phone = binding.etPhone.text.toString().trim()
        nickName = binding.etNickname.text.toString().trim()

        if (name.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_name, ToastUtils.ShowType.Fail)
            return
        }
        when (mark) {
            getString(R.string.txt_bind_zfb_type) -> {
                if (!(AppUtil.isPhone(phone) || AppUtil.isEmail(phone))) {
                    ToastUtils.showLong(R.string.txt_tip_account, ToastUtils.ShowType.Fail)
                    return
                }
            }

            getString(R.string.txt_bind_wechat_type) -> {
                if (!(AppUtil.isPhone(phone) || AppUtil.isEmail(phone) || AppUtil.isQQ(phone) || AppUtil.isWX(phone))) {
                    ToastUtils.showLong(R.string.txt_tip_account, ToastUtils.ShowType.Fail)
                    return
                }
            }
        }
        if (nickName.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_nickname, ToastUtils.ShowType.Fail)
            return
        }
        if (!imageSelector) {
            ToastUtils.showLong(getString(R.string.txt_tip_code, typeName), ToastUtils.ShowType.Fail)
            return
        }

        val queryMap = hashMapOf(
            "controller" to controller,
            "action" to action,
            "client" to "m"
        )

        val fileType = ImageUtils.getImageType(File(imageRealPathString))
        val filedata = "data:" + fileType + ";base64," + ImageUploadUtil.bitmapToString(imageRealPathString)
        //KLog.i("filedata", filedata)
        val map = HashMap<String, Any?>()
        map["check"] = tokenSign
        map["entrancetype"] = 0
        map["filetype"] = fileType
        map["filedata"] = filedata
        map["flag"] = "add"
        map["mark"] = mark
        map["nickname"] = nickName
        map["nonce"] = UuidUtil.getID()
        map["qrcode_type"] = qrcodeType
        map["submit"] = "sumbit"
        map["type"] = "m5"
        map["wxzfb_id"] = phone
        map["wxzfb_username"] = name

        LoadingDialog.show(context)
        viewModel.doBindAWByCheck(action, queryMap, map)
    }

    /**
     * 图片选择
     */
    private fun gotoSelectMedia() {
        PictureSelector.create(activity).openGallery(SelectMimeType.ofImage())
            .isDisplayCamera(false)
            .setMaxSelectNum(1)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCompressEngine(ImageFileCompressEngine.create())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    if (result != null) {
                        for (i in result.indices) {
                            imageRealPathString = result[i].compressPath
                            if (imageRealPathString.isNullOrEmpty()) {
                                imageRealPathString = result[i].realPath
                            }
                            val imageRealPath = File(imageRealPathString)
                            if (imageRealPath.exists()) {
                                val bitmap = BitmapFactory.decodeFile(imageRealPathString)
                                if (bitmap == null) {
                                    //未通过文件名取得bitmap
                                    ToastUtils.showError(requireContext().getString(R.string.txt_read_photo_permissions))
                                    imageSelector = false //未向界面设置了选中图片
                                    return
                                } else {
                                    binding.ivSelectorAdd.visibility = View.GONE
                                    binding.ivSelectorTipImage.visibility = View.VISIBLE
                                    binding.ivSelectorTipImage.setImageBitmap(bitmap)
                                    imageSelector = true //向界面设置了选中图片
                                }
                            } else {
                                CfLog.i("获取图片地址不存在是 ====== " + result[i].realPath)
                            }
                            imageUri = if (PictureMimeType.isContent(imageRealPathString)) {
                                Uri.parse(imageRealPathString)
                            } else {
                                Uri.fromFile(File(imageRealPathString))
                            }
                            CfLog.i("获取图片地址是 uri ====== $imageUri")
                        }
                    }
                }

                override fun onCancel() {}
            })
    }

    private fun doSubmit() {
        val queryMap = HashMap<String, Any?>()
        queryMap["controller"] = controller
        queryMap["action"] = action
        queryMap["client"] = "m"
        KLog.i("picurl", mConfirmVo.picurl)
        val map = HashMap<String, Any?>()
        map["check"] = tokenSign
        map["entrancetype"] = 0
        map["filetype"] = mConfirmVo.tyfiletypepe
        map["flag"] = "confirm"
        map["mark"] = mark
        map["nickname"] = nickName
        map["nonce"] = UuidUtil.getID()
        map["picurl"] = mConfirmVo.picurl
        map["submit"] = "sumbit"
        map["type"] = "m5"
        map["wxzfb_id"] = phone
        map["wxzfb_qrcodekey"] = mConfirmVo.wxzfb_qrcodekey
        map["wxzfb_username"] = name
        LoadingDialog.show(context)
        viewModel.doBindAwBySubmit(action, queryMap, map)
    }

}