package com.xtree.mine.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.ImageUploadUtil
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.base.widget.GlideEngine
import com.xtree.base.widget.ImageFileCompressEngine
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentBindAddAwBinding
import com.xtree.mine.ui.viewmodel.BindCardViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.UserBankConfirmVo
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.ToastUtils
import java.io.File

/**
 * 绑定支付宝、微信
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_AW_ADD)
class BindAWAddFragment : BaseFragment<FragmentBindAddAwBinding, BindCardViewModel>() {
    private val controller = "security"
    private var action = ""
    private var qrcodeType = 0
    private var tokenSign: String? = null
    private var mark: String? = null
    lateinit var mConfirmVo: UserBankConfirmVo
    private var imageRealPathString: String? = null //选择的图片地址
    private var imageSelector = false //是否已选择图片
    private var imageUri: Uri? = null
    override fun initView() {
        initArguments()
        binding.llRoot.setOnClickListener { hideKeyBoard() }
        binding.ivwBack.setOnClickListener {
            if (binding.llAdd.visibility == View.GONE) {
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
            if (binding.llAdd.visibility == View.GONE) {
                binding.llAdd.visibility = View.VISIBLE
                binding.llConfirm.visibility = View.GONE
            }
        }
    }

    private fun initArguments() {
        if (arguments != null) {
            tokenSign = requireArguments().getString(ARG_TOKEN_SIGN)
            mark = requireArguments().getString(ARG_MARK)
            var typeName = ""
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
                }
            }
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
        return ViewModelProvider(this, factory).get(BindCardViewModel::class.java)
    }

    override fun initViewObservable() {
        viewModel.liveDataBindCardCheck.observe(this) { vo: UserBankConfirmVo ->
            CfLog.i("******")
            mConfirmVo = vo
            setConfirmView()
        }
        viewModel.liveDataBindCardResult.observe(this) { vo: UserBankConfirmVo? ->
            CfLog.i("******")
            //getActivity().finish();
            viewModel.getProfile()
        }
        viewModel.liveDataProfile.observe(this) { vo: ProfileVo? ->
            CfLog.i("******")
            requireActivity().finish()
        }
    }

    private fun setConfirmView() {
        binding.llAdd.visibility = View.GONE
        binding.llConfirm.visibility = View.VISIBLE
        binding.tvwNickname.text = mConfirmVo.nickname
    }

    private fun doNext() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.toString().trim()
        val nickName = binding.etNickname.text.toString().trim()

        if (name.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_name, ToastUtils.ShowType.Fail)
            return
        }
        if (phone.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_phone, ToastUtils.ShowType.Fail)
            return
        }
        if (nickName.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_nickname, ToastUtils.ShowType.Fail)
            return
        }
        if (!imageSelector) {
            ToastUtils.showLong(R.string.txt_tip_code, ToastUtils.ShowType.Fail)
            return
        }

        val queryMap = hashMapOf(
            "controller" to controller,
            "action" to action,
            "client" to "m"
        )

        val map = hashMapOf(
            "check" to tokenSign,
            "entrancetype" to "0",
            "filetype" to "image/png",
            "filedata" to ImageUploadUtil.bitmapToString(imageRealPathString),
            "flag" to "add",
            "mark" to mark,
            "nickname" to nickName,
            "nonce" to UuidUtil.getID16(),
            "qrcode_type" to qrcodeType,
            "submit" to "sumbit",
            "type" to "m5",
            "wxzfb_id" to phone,
            "wxzfb_username" to name,
        )
        viewModel.doBindCardByCheck(queryMap, map)
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
                            if (TextUtils.isEmpty(imageRealPathString)) {
                                imageRealPathString = result[i].realPath
                            }
                            val imageRealPath = File(imageRealPathString)
                            if (imageRealPath.exists()) {
                                val bitmap = BitmapFactory.decodeFile(imageRealPathString)
                                binding.ivSelectorAdd.visibility = View.GONE
                                binding.ivSelectorTipImage.visibility = View.VISIBLE
                                binding.ivSelectorTipImage.setImageBitmap(bitmap)
                                imageSelector = true //向界面设置了选中图片
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
        queryMap["mark"] = mark
        queryMap["check"] = tokenSign
        val map = HashMap<Any?, Any?>()
        map["flag"] = "confirm"
        map["controller"] = controller
        map["action"] = action
        map["nickname"] = ""
        map["bank_id"] = mConfirmVo.bank_id // "111",
        map["bank"] = mConfirmVo.bank // "上海银行",
        map["province_id"] = mConfirmVo.province_id // "22",
        map["province"] = mConfirmVo.province // "云南",
        map["city_id"] = mConfirmVo.city_id // "23",
        map["city"] = mConfirmVo.city // "丽江",
        map["branch"] = mConfirmVo.branch // "丽江支行",
        map["account_name"] = mConfirmVo.account_name // "姓名"
        map["account"] = mConfirmVo.account // "4500***1234"
        map["oldid"] = ""
        map["entrancetype"] = "0"
        map["nonce"] = UuidUtil.getID16()
        viewModel.doBindCardBySubmit(queryMap, map)
    }

    companion object {
        private const val ARG_TOKEN_SIGN = "tokenSign"
        private const val ARG_MARK = "mark"
    }
}