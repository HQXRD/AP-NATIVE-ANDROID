package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.fb.ScoreInfo;

import java.util.List;

public class ScorePm implements Score{
    private String period;
    private List<Integer> scores;
    public ScorePm(String period, List<Integer> scores){
        this.period = period;
        this.scores = scores;
    }

    /**
     * 获取玩法
     * @return
     */
    @Override
    public String getPeriod() {
        return period;
    }

    /**
     * 获取比分
     * @return
     */
    @Override
    public List<Integer> getScores() {
        return scores;
    }
}
