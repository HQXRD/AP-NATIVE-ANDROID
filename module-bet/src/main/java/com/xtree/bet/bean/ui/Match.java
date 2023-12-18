package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public interface Match extends BaseBean {
    /**
     * 获取比赛ID
     * @return
     */
    int getId();
    /**
     * 获取主队名称
     * @return
     */
    String getTeamMain();
    /**
     * 获取客队名称
     * @return
     */
    String getTeamVistor();

    /**
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     * @return
     */
    String getStage();

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     * @return
     */
    String getTime();

    /**
     * 获取赛事比分
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    List<Integer> getScore(int type);

    /**
     * 获取单个赛事玩法总数
     * @return
     */
    int getPlayTypeCount();

    /**
     * 获取玩法列表
     * @return
     */
    List<PlayType> getPlayTypeList();

    /**
     * 是否有视频直播
     * @return
     */
    boolean hasVideo();

    /**
     * 是否有动画直播
     * @return
     */
    boolean hasAs();
}
