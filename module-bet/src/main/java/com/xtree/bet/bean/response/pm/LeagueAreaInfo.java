package com.xtree.bet.bean.response.pm;

import java.util.List;

/**
 * 联赛所属区域
 */
public class LeagueAreaInfo {

    /**
     * id : 0
     * introduction : 热门联赛
     * spell : HOT
     * sportVOs : [{"id":"1","nameText":"足球","spell":"Z","tournamentList":[{"hotStatus":1,"id":"95","nameText":"澳大利亚甲级联赛","num":1,"picUrlthumb":"group1/M00/15/3C/CgURt2HPoWSAcMQsAAAZ40V-tUo842.png","regionId":"2","sportId":"1","tournamentId":"95","tournamentLevel":1}]}]
     */

    public String id;
    public String introduction;
    public String spell;
    public List<SportVOsBean> sportVOs;

    public static class SportVOsBean {
        /**
         * id : 1
         * nameText : 足球
         * spell : Z
         * tournamentList : [{"hotStatus":1,"id":"95","nameText":"澳大利亚甲级联赛","num":1,"picUrlthumb":"group1/M00/15/3C/CgURt2HPoWSAcMQsAAAZ40V-tUo842.png","regionId":"2","sportId":"1","tournamentId":"95","tournamentLevel":1}]
         */

        public String id;
        public String nameText;
        public String spell;
        public List<LeagueInfo> tournamentList;
    }
}
