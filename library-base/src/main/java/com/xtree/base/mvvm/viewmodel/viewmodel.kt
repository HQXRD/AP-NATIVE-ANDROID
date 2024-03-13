package com.xtree.base.mvvm.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 *Created by KAKA on 2024/3/12.
 *Describe:
 */
fun <VM : ViewModel> createViewModel(
    fragment: Fragment,
    factory: ViewModelProvider.Factory? = null,
    position: Int
): VM {
    val vbClass =
        (fragment.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
    val viewModel = vbClass[position] as Class<VM>
    return factory?.let {
        ViewModelProvider(
            fragment,
            factory
        ).get(viewModel)
    } ?: run {
        ViewModelProvider(fragment).get(viewModel)
    }
}

fun <VM : ViewModel> createActivityViewModel(
    fragment: Fragment,
    factory: ViewModelProvider.Factory? = null,
    position: Int
): VM {
    val vbClass =
        (fragment.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
    val viewModel = vbClass[position] as Class<VM>
    return factory?.let {
        ViewModelProvider(
            fragment.requireActivity(),
            factory
        ).get(viewModel)
    } ?: run {
        ViewModelProvider(fragment.requireActivity()).get(viewModel)
    }
}
