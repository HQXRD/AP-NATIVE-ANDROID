package com.xtree.bet.bean.ui;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.fb.VideoInfo;

import java.util.List;

public interface Match extends BaseBean {
    boolean isHead();
    void setHead(boolean isHead);
    /**
     * 设置是否展开赛事，true-展开，false-关闭
     * @return
     */
    void setExpand(boolean isExpand);

    /**
     * 获取是否展开赛事，true-展开，false-关闭
     * @return
     */
    boolean isExpand();
    /**
     * 获取比赛ID
     * @return
     */
    long getId();

    /**
     * 获取冠军赛赛事名称，用于展示名称
     * @return
     */
    String getChampionMatchName();
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
     * 获取实时比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    List<Integer> getScore(String... type);
    /**
     * 获取比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    List<Score> getScoreList(String... type);

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
     * 视频直播是否开始
     * @return
     */
    boolean isVideoStart();

    /**
     * 是否有动画直播
     * @return
     */
    boolean hasAs();
    /**
     * 动画直播是否开始
     * @return
     */
    boolean isAnimationStart();
    /**
     * 获取动画直播地址列表
     * @return
     */
    List<String> getVideoUrls();
    /**
     * 获取动画直播地址列表
     * @return
     */
    List<String> getAnmiUrls();
    /**
     * 获取联赛信息
     * @return
     */
    League getLeague();
    /**
     * 获取主队logo
     * @return
     */
    String getIconMain();

    /**
     * 获取客队logo
     * @return
     */
    String getIconVisitor();
    /**
     * 获取比赛是否未开始状态
     * @return
     */
    boolean isGoingon();
    /**
     * 获取开赛时间
     * @return
     */
    long getMatchTime();

    /**
     * 是否冠军赛事
     * @return
     */
    boolean isChampion();
    /**
     * 获取赛种ID，如足球，篮球
     */
    String getSportId();

    /**
     * 获取赛种名称，如足球，篮球
     */
    String getSportName();

    /**
     * PM获取播放器请求头信息
     * @return
     */
    String getReferUrl();

    /**
     * PM设置播放器请求头信息
     * @param referUrl
     */
    void setReferUrl(String referUrl);

    /**
     * 是否已经产生有角球
     * @return
     */
    boolean hasCornor();

    /**
     * 是否中立场
     * @return
     */
    boolean isNeutrality();

    /**
     * 获取赛制
     * @return
     */
    String getFormat();
}
