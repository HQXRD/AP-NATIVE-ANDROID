package com.xtree.bet.bean.request.fb;

import com.xtree.base.utils.SystemUtil;

public class ThirdRemark {
    private String device = "19"; // android
    private String deviceDetail = SystemUtil.getDeviceBrand() + " " + SystemUtil.getDeviceModel() + " " + "Android " + SystemUtil.getSystemVersion();
}
