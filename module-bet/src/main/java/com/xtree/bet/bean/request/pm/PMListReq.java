package com.xtree.bet.bean.request.pm;

import com.xtree.base.global.SPKeyGlobal;

import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class PMListReq {
    /**
     * 菜单ID（菜单接口返回的menuID）
     */
    private String euid;
    /**
     * 联赛id，多个用逗号(,)分隔
     */
    private String tid;
    /**
     * 一级菜单筛选类型 1：滚球 2-即将开赛 3-今日赛事 4-早盘 11-串关 100-冠军
     */
    private int type;
    /**
     * 排序 1：按联赛排序，2：按时间排序
     */
    private int sort;
    /**
     * 用户id
     */
    private String cuid = SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID);
    /**
     * 默认N，早盘串关Y，默认传递一个当天13位时间戳，精确到毫秒
     * 示例：1673409600000 对应格式化时间：2023-01-11 12:00:00 md = "" 所有日期(不包括今日) ，
     * md = "0" 今日， md="1673409600000" 某一天 更多早盘，指超过7天的赛事 md="-1674014400000"
     */
    private String md;
    /**
     * 不传或null 返回玩法有 【足球： 1L：全场独赢, 4L：全场让球, 2L：全场大小 网球 ：153L：独赢, 154L：让盘, 155L：让局】　　　　　
     * v2_h5 返回玩法有 【足球： 1L：全场独赢, 4L：全场让球, 2L：全场大小, 114L：角球大小 网球： 153L：独赢, 154L：让盘, 169L：总盘数 】　　　　　
     * v2_h5_st 返回玩法有 【足球： 1L：全场独赢, 4L：全场让球, 2L：全场大小, 17L：半场独赢, 19L：半场让球, 18L：半场大小 】
     */
    private String device;
    /**
     * 页数
     */
    private int cpn;
    /**
     * 每页行数，默认10
     */
    private int cps;
    /**
     * 指定赛事列表球种ID
     */
    private String csid;
    /**
     * 赛事id，多个用逗号分隔
     */
    private String mids;

    public void setEuid(String euid) {
        Integer iEuid = Integer.valueOf(euid);
        this.euid = String.valueOf(iEuid);
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setCpn(int cpn) {
        this.cpn = cpn;
    }

    public void setCps(int cps) {
        this.cps = cps;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }

    public String getMids() {
        return mids;
    }

    public void setMids(List<Long> mids) {
        if(mids != null){
            for (Long mid :
                    mids) {
                this.mids += mid + ",";
            }
        }
    }
}
