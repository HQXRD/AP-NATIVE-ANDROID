package com.xtree.bet.bean.ui;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.MatchInfo;
import com.xtree.bet.bean.response.PlayTypeInfo;
import com.xtree.bet.bean.response.ScoreInfo;
import com.xtree.bet.bean.response.VideoInfo;
import com.xtree.bet.constant.MatchPeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchFb implements Match{
    MatchInfo matchInfo;

    List<PlayType> playTypeList = new ArrayList<>();

    public MatchFb(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
    }

    /**
     * 获取比赛ID
     * @return
     */
    public int getId(){
        return matchInfo.id;
    }

    /**
     * 获取冠军赛赛事名称，用于展示名称
     * @return
     */
    @Override
    public String getChampionMatchName() {
        return matchInfo.nm;
    }

    /**
     * 获取主队名称
     * @return
     */
    @Override
    public String getTeamMain() {
        if(matchInfo.ts == null || matchInfo.ts.isEmpty()){
            return "";
        }
        return matchInfo.ts.get(0).na;
    }

    /**
     * 获取客队名称
     * @return
     */
    @Override
    public String getTeamVistor() {
        if(matchInfo.ts == null || matchInfo.ts.isEmpty()){
            return "";
        }
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
     * 获取实时比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Integer> getScore(int type) {
        if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (scoreInfo.tyg == type) {
                    return scoreInfo.sc;
                }
            }
        }
        List<Integer> sc = new ArrayList<>();
        sc.add(0);
        sc.add(0);
        return sc;
    }

    /**
     * 获取比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Score> getScoreList(int type) {
        List<Score> scoreInfos = new ArrayList<>();
        if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (scoreInfo.tyg == type) {
                    scoreInfos.add(new ScoreFb(scoreInfo));
                }
            }
        }

        return scoreInfos;
    }

    /**
     * 获取单个赛事玩法总数
     * @return
     */
    @Override
    public int getPlayTypeCount() {
        return matchInfo.tms;
    }

    /**
     * 获取玩法列表
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo: matchInfo.mg) {
            PlayTypeFb playTypeFbAdapter = new PlayTypeFb(playTypeInfo);
            playTypeList.add(playTypeFbAdapter);
        }
        return playTypeList;
    }

    /**
     * 是否有视频直播
     * @return
     */
    @Override
    public boolean hasVideo() {
        return matchInfo.vs.have;
    }
    /**
     * 是否有动画直播
     * @return
     */
    @Override
    public boolean hasAs() {
        return matchInfo.as != null && !matchInfo.as.isEmpty();
    }

    @Override
    public VideoInfo getVideoInfo() {
        return matchInfo.vs;
    }

    @Override
    public List<String> getAnmiUrls() {
        return matchInfo.as;
    }

    /**
     * 获取联赛信息
     * @return
     */
    @Override
    public League getLeague() {
        return new LeagueFb(matchInfo.lg);
    }

    /**
     * 获取主队logo
     * @return
     */
    @Override
    public String getIconMain() {
        return matchInfo.ts.get(0).lurl;
    }

    /**
     * 获取客队logo
     * @return
     */
    @Override
    public String getIconVisitor() {
        return matchInfo.ts.get(1).lurl;
    }

    @Override
    public boolean isUnGoingon() {
        return matchInfo.ms == 4;
    }

    @Override
    public long getMatchTime() {
        return this.matchInfo.bt;
    }
    /**
     * 是否冠军赛事
     * @return
     */
    @Override
    public boolean isChampion() {
        return this.matchInfo.ty == 1;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || obj.getClass() != OptionFb.class){
            return false;
        }
        MatchFb optionFb = (MatchFb) obj;
        return getId() == optionFb.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(String.valueOf(getId()));
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

    protected MatchFb(Parcel in) {
        this.matchInfo = in.readParcelable(MatchInfo.class.getClassLoader());
        this.playTypeList = new ArrayList<>();
        in.readList(this.playTypeList, PlayType.class.getClassLoader());
    }

    public static final Creator<MatchFb> CREATOR = new Creator<MatchFb>() {
        @Override
        public MatchFb createFromParcel(Parcel source) {
            return new MatchFb(source);
        }

        @Override
        public MatchFb[] newArray(int size) {
            return new MatchFb[size];
        }
    };
}
