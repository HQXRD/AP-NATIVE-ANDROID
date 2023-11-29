package com.xtree.bet.bean.ui;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.MatchInfo;
import com.xtree.bet.bean.PlayTypeInfo;
import com.xtree.bet.bean.ScoreInfo;
import com.xtree.bet.constant.MatchPeriod;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchFbAdapter implements Match{
    MatchInfo matchInfo;

    List<PlayType> playTypeList = new ArrayList<>();

    public MatchFbAdapter(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
    }

    /**
     * 获取主队名称
     * @return
     */
    @Override
    public String getTeamMain() {
        return matchInfo.ts.get(0).na;
    }

    /**
     * 获取客队名称
     * @return
     */
    @Override
    public String getTeamVistor() {
        return matchInfo.ts.get(1).na;
    }

    /**
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     * @return
     */
    @Override
    public String getStage() {
        return MatchPeriod.getMatchPeriod(String.valueOf(matchInfo.mc.pe));
    }

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     * @return
     */
    @Override
    public String getTime() {
        return TimeUtils.sToMs(matchInfo.mc.s);
    }

    /**
     * 获取比分信息
     * @return
     */
    @Override
    public List<Integer> getScore() {
        for (ScoreInfo scoreInfo: matchInfo.nsg) {
            if(scoreInfo.pe == matchInfo.mc.pe){
                return scoreInfo.sc;
            }
        }
        return null;
    }

    /**
     * 获取玩法列表
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo: matchInfo.mg) {
            PlayTypeFbAdapter playTypeFbAdapter = new PlayTypeFbAdapter(playTypeInfo);
            playTypeList.add(playTypeFbAdapter);
        }
        return null;
    }


}
