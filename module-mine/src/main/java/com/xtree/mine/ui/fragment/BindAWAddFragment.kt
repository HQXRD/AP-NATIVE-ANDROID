package com.xtree.mine.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
    }

    private fun initArguments() {
        if (arguments != null) {
            tokenSign = requireArguments().getString(ARG_TOKEN_SIGN)
            mark = requireArguments().getString(ARG_MARK)
            accountName = requireArguments().getString("accountName")
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
        return ViewModelProvider(this, factory)[BindCardViewModel::class.java]
    }

    override fun initViewObservable() {
        viewModel.liveDataBindCardCheck.observe(this) { vo: UserBankConfirmVo ->
            CfLog.i("******")
            mConfirmVo = vo
            setConfirmView()
        }
        viewModel.liveDataBindCardResult.observe(this) { vo: UserBankConfirmVo ->
            CfLog.i("******")
            //getActivity().finish();
            binding.layoutRecharge.visibility = View.VISIBLE
            binding.llConfirm.visibility = View.GONE
            binding.tvRecharge.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean("isShowBack", true)
                startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle)
            }
            viewModel.getProfile()
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

    private fun doNext() {
        name = binding.etName.text.toString().trim()
        phone = binding.etPhone.text.toString().trim()
        nickName = binding.etNickname.text.toString().trim()

        if (name.isEmpty()) {
            ToastUtils.showLong(R.string.txt_tip_name, ToastUtils.ShowType.Fail)
            return
        }
        if (phone.isEmpty() || phone.length != 11) {
            ToastUtils.showLong(R.string.txt_tip_phone, ToastUtils.ShowType.Fail)
            return
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
        KLog.i("filedata", filedata)
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
                            if (imageRealPathString.isNullOrEmpty()) {
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
        viewModel.doBindCardBySubmit(queryMap, map)
    }

}