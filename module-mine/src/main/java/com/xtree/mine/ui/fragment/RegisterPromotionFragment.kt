package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xtree.base.router.RouterFragmentPath
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentRegisterPromotionBinding
import com.xtree.mine.ui.viewmodel.MineViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment

/**
 * 注册推广
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REGISTER_PROMOTION)
class RegisterPromotionFragment : BaseFragment<FragmentRegisterPromotionBinding, MineViewModel>() {
    private lateinit var mAdapter: FragmentStateAdapter
    private val fragmentList = ArrayList<Fragment>()
    private val tabList = ArrayList<String>()

    override fun initView() {

        binding.ivwBack.setOnClickListener { requireActivity().finish() }

        mAdapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getItemCount(): Int {
                return fragmentList.size
            }
        }

        binding.vpMain.adapter = mAdapter
        binding.vpMain.isUserInputEnabled = true // ViewPager2 左右滑动

        TabLayoutMediator(binding.tblType, binding.vpMain) { tab: TabLayout.Tab, position: Int -> tab.setText(tabList[position]) }.attach()

        val bindMsgPersonFragment = MsgPersonListFragment()

        fragmentList.add(RegAccountFragment())
        fragmentList.add(bindMsgPersonFragment)

        tabList.add(getString(R.string.txt_reg_account))
        tabList.add(getString(R.string.txt_prom_links))

        mAdapter.notifyDataSetChanged()
    }

    override fun initData() {
        viewModel.marketing()
    }

    override fun initViewObservable() {
        viewModel.liveDataMarketing.observe(this) {

        }
    }


    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_register_promotion
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineViewModel {
        // return super.initViewModel();
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[MineViewModel::class.java]
    }


}