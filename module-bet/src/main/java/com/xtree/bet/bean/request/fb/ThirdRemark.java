package com.xtree.bet.bean.request.fb;

import com.xtree.base.utils.SystemUtil;

public class ThirdRemark {
    private String device = "9"; // android
    private String deviceDetail = SystemUtil.getDeviceBrand() + " " + SystemUtil.getDeviceModel() + " " + device + SystemUtil.getSystemVersion();
}
