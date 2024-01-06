package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.fb.ScoreInfo;

import java.util.List;

public class ScoreFb implements Score{
    private ScoreInfo scoreInfo;
    public ScoreFb(ScoreInfo scoreInfo){
        this.scoreInfo = scoreInfo;
    }

    /**
     * 获取玩法
     * @return
     */
    @Override
    public int getPeriod() {
        return scoreInfo.pe;
    }

    /**
     * 获取比分
     * @return
     */
    @Override
    public List<Integer> getScores() {
        return scoreInfo.sc;
    }
}
