package com.xtree.bet.bean.ui;

import android.os.Parcel;

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

    @Override
    public boolean hasVideo() {
        return matchInfo.vs.have;
    }

    @Override
    public boolean hasAs() {
        return matchInfo.as != null && !matchInfo.as.isEmpty();
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
