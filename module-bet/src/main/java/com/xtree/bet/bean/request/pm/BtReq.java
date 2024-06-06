package com.xtree.bet.bean.request.pm;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.TagUtils;

import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class BtReq {
    private String cuid = SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID);
    //private String tenantId = "1";
    /**
     * 设备类型 1：H5，2：PC，3：Android，4：IOS，5：其他设备
     */
    private int deviceType = 3;
    /**
     *
     */
    private String deviceImei = TagUtils.getDeviceId(Utils.getContext());
    /**
     * 注单串关类型，或者多单关
     */
    private List<SeriesOrder> seriesOrders;
    /**
     * 是否自动接受赔率变化 1：自动接收更好的赔率 2：自动接受任何赔率变动 3：不自动接受赔率变动
     */
    private int acceptOdds = 2;
    /**
     * 是否开启 多单关投注模式，1：是，非1（0或者其他）：否
     */
    //private int openMiltSingle = 0;
    private int preBet = 0;

    public void setCuid() {
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if(TextUtils.equals(platform, PLATFORM_PMXC)){
            this.cuid = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_USER_ID);
        }
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public List<SeriesOrder> getSeriesOrders() {
        return seriesOrders;
    }

    public void setSeriesOrders(List<SeriesOrder> seriesOrders) {
        this.seriesOrders = seriesOrders;
    }

    public int getAcceptOdds() {
        return acceptOdds;
    }

    public void setAcceptOdds(int acceptOdds) {
        this.acceptOdds = acceptOdds;
    }

    /*public int getOpenMiltSingle() {
        return openMiltSingle;
    }

    public void setOpenMiltSingle(int openMiltSingle) {
        this.openMiltSingle = openMiltSingle;
    }*/
}
