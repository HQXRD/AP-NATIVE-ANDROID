package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.xtree.bet.bean.response.fb.VideoInfo;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchPm implements Match{
    MatchInfo matchInfo;

    List<PlayType> playTypeList = new ArrayList<>();

    public MatchPm(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
    }

    /**
     * 获取比赛ID
     * @return
     */
    public long getId(){
        return Long.valueOf(matchInfo.mid);
    }

    /**
     * 获取冠军赛赛事名称，用于展示名称
     * @return
     */
    @Override
    public String getChampionMatchName() {
        return matchInfo.tn;
    }

    /**
     * 获取主队名称
     * @return
     */
    @Override
    public String getTeamMain() {
        return matchInfo.mhn;
    }

    /**
     * 获取客队名称
     * @return
     */
    @Override
    public String getTeamVistor() {
        return matchInfo.man;
    }

    /**
     * TODO
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     * @return
     */
    @Override
    public String getStage() {
        return "";
    }

    /**
     * TODO
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     * @return
     */
    @Override
    public String getTime() {
        return "";
    }

    /**
     * 获取实时比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Integer> getScore(int type) {
        List<Integer> sc = new ArrayList<>();
        for (String str : matchInfo.msc) {
            if (str.contains("S1") && matchInfo.msc != null && !matchInfo.msc.isEmpty()) {
                String score = matchInfo.msc.get(0);
                if (!TextUtils.isEmpty(score) && score.contains("|")) {
                    score = score.substring(score.indexOf("|") + 1, score.length());
                    if (!TextUtils.isEmpty(score) && score.contains(":")) {
                        sc.add(Integer.valueOf(score.split(":")[0]));
                        sc.add(Integer.valueOf(score.split(":")[1]));
                    }
                }
                return sc;
            }
        }
        sc.add(0);
        sc.add(0);
        return sc;
    }

    /**
     * TODO
     * 获取比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Score> getScoreList(int type) {
        List<Score> scoreInfos = new ArrayList<>();
        /*if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (scoreInfo.tyg == type) {
                    scoreInfos.add(new ScoreFb(scoreInfo));
                }
            }
        }*/

        return scoreInfos;
    }

    /**
     * 获取单个赛事玩法总数
     * @return
     */
    @Override
    public int getPlayTypeCount() {
        return matchInfo.mc;
    }

    /**
     * 获取玩法列表
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo: matchInfo.hps) {
            PlayTypePm playTypePm = new PlayTypePm(playTypeInfo);
            playTypeList.add(playTypePm);
        }
        return playTypeList;
    }

    /**
     * 是否有视频直播
     * @return
     */
    @Override
    public boolean hasVideo() {
        return matchInfo.mms != -1;
    }
    /**
     * 是否有动画直播
     * @return
     */
    @Override
    public boolean hasAs() {
        return matchInfo.mvs != -1;
    }

    /**
     * 获取视频源信息
     * @return
     */
    @Override
    public VideoInfo getVideoInfo() {
        return null;
    }

    /**
     * 获取动画播放源信息
     * @return
     */
    @Override
    public List<String> getAnmiUrls() {
        return null;
    }

    /**
     * 获取联赛信息
     * @return
     */
    @Override
    public League getLeague() {
        LeagueInfo leagueInfo = new LeagueInfo();
        leagueInfo.lurl = matchInfo.lurl;
        leagueInfo.tn = matchInfo.tn;
        leagueInfo.tid = Integer.valueOf(matchInfo.tid);
        return new LeaguePm(leagueInfo);
    }

    /**
     * 获取主队logo
     * @return
     */
    @Override
    public String getIconMain() {
        return matchInfo.mhlu.get(0);
    }

    /**
     * 获取客队logo
     * @return
     */
    @Override
    public String getIconVisitor() {
        return matchInfo.malu.get(1);
    }

    /**
     * 获取比赛是否未开始状态
     * @return
     */
    @Override
    public boolean isUnGoingon() {
        return TextUtils.equals(matchInfo.mmp, "0");
    }

    /**
     * 获取比赛开始时间
     * @return
     */
    @Override
    public long getMatchTime() {
        return Long.valueOf(matchInfo.mgt);
    }
    /**
     * 是否冠军赛事
     * @return
     */
    @Override
    public boolean isChampion() {
        return this.matchInfo.mcg == 100;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.matchInfo, flags);
        dest.writeList(this.playTypeList);
    }

    public void readFromParcel(Parcel source) {
        this.matchInfo = source.readParcelable(MatchInfo.class.getClassLoader());
        this.playTypeList = new ArrayList<>();
        source.readList(this.playTypeList, PlayType.class.getClassLoader());
    }

    protected MatchPm(Parcel in) {
        this.matchInfo = in.readParcelable(MatchInfo.class.getClassLoader());
        this.playTypeList = new ArrayList<>();
        in.readList(this.playTypeList, PlayType.class.getClassLoader());
    }

    public static final Creator<MatchPm> CREATOR = new Creator<MatchPm>() {
        @Override
        public MatchPm createFromParcel(Parcel source) {
            return new MatchPm(source);
        }

        @Override
        public MatchPm[] newArray(int size) {
            return new MatchPm[size];
        }
    };
}
