package com.xtree.bet.bean.ui;

import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

/**
 * 玩法选项实时赔率及限额信息(为了适配FB和PM体育用)
 */
public interface BetConfirmOption extends BaseBean {
    /**
     * 获取投注信息唯一标识
     * @return
     */
    String getCode();
    /**
     * 获取玩法ID
     * @return
     */
    long getPlayTypeId();

    /**
     * 获取投注项信息
     * @return
     */
    Option getOption();

    /**
     * 获取单关最小投注额限制
     * @return
     */
    double getDanMin();
    /**
     * 获取单关最大投注额限制
     * @return
     */
    double getDanMax();

    /**
     * 玩法销售状态是否关闭，0暂停，1开售，-1未开售
     * @return
     */
    boolean isClose();
    /**
     * 足球让球当前比分， 如1-1
     * @return
     */
    String getScore();
    /**
     * 获取比赛双方名称
     * @return
     */
    String getTeamName();
    /**
     * 获取投注项类型
     * @return
     */
    String getOptionType();
    /**
     * 获取投注的比赛信息
     * @return
     */
    Match getMatch();

    /**
     * 设置服务器返回的真实数据
     * @param data
     */
    void setRealData(BaseBean data);

    /**
     * 获取服务器返回的真实数据
     * @return
     */
    BaseBean getRealData();
    /**
     * 获取投注玩法数据
     * @return
     */
    PlayType getPlayType();
    /**
     * 获取投注玩法类型数据
     * @return
     */
    OptionList getOptionList();

    /**
     * 设置比赛及投注信息
     * @param match
     */
    void setData(Match match, PlayType playType, OptionList optionList, Option option);
}
