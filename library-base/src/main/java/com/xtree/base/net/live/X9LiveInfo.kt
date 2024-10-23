package com.xtree.base.net.live

import com.xtree.base.utils.TagUtils
import me.xtree.mvvmhabit.utils.Utils

/**
 *Created by KAKA on 2024/10/23.
 *Describe:
 */
object X9LiveInfo {

    var token = ""
    var sign = ""
    var visitor = ""
    var oaid = TagUtils.getDeviceId(Utils.getContext())
    var channel = ""
}