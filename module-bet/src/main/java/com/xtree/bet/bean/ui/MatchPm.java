package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.VideoInfo;
import com.xtree.bet.constant.FBMatchPeriod;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.constant.PMMatchPeriod;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchPm implements Match {
    MatchInfo matchInfo;

    List<PlayType> playTypeList = new ArrayList<>();

    LeaguePm mLeague;
    /**
     * 播放器请求头
     */
    private String referUrl;

    public MatchPm(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
    }

    /**
     * 获取比赛ID
     *
     * @return
     */
    public long getId() {
        return Long.valueOf(matchInfo.mid);
    }

    /**
     * 获取冠军赛赛事名称，用于展示名称
     *
     * @return
     */
    @Override
    public String getChampionMatchName() {
        return matchInfo.tn;
    }

    /**
     * 获取主队名称
     *
     * @return
     */
    @Override
    public String getTeamMain() {
        return matchInfo.mhn;
    }

    /**
     * 获取客队名称
     *
     * @return
     */
    @Override
    public String getTeamVistor() {
        return matchInfo.man;
    }

    /**
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     *
     * @return
     */
    @Override
    public String getStage() {
        return PMMatchPeriod.getMatchPeriod(String.valueOf(matchInfo.mmp));
    }

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     *
     * @return
     */
    @Override
    public String getTime() {
        return TimeUtils.sToMs(Integer.valueOf(matchInfo.mst));
    }

    /**
     * 获取实时比分信息
     *
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Integer> getScore(String... type) {
        List<Integer> sc = new ArrayList<>();
        for (String str : matchInfo.msc) {
            if (str.contains(type[0] + "|") && matchInfo.msc != null && !matchInfo.msc.isEmpty()) {
                String score = str;
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
     * 获取比分信息
     *
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Score> getScoreList(String... type) {

        List<Score> scoreInfos = new ArrayList<>();
        if(type == null){
            return scoreInfos;
        }
        if (matchInfo.msc != null && !matchInfo.msc.isEmpty()) {
            for (String strScore : matchInfo.msc) {
                for (int i = 0; i < type.length; i++) {

                    List<Integer> sc = new ArrayList<>();
                    if (!TextUtils.isEmpty(strScore) && strScore.contains("|") && strScore.startsWith(type[i] + "|")) {

                        String[] scoreStrs = strScore.split("\\|");
                        String score = scoreStrs[1];
                        score = score.substring(score.indexOf("|") + 1, score.length());
                        if (!TextUtils.isEmpty(score) && score.contains(":")) {
                            sc.add(Integer.valueOf(score.split(":")[0]));
                            sc.add(Integer.valueOf(score.split(":")[1]));
                            scoreInfos.add(new ScorePm(scoreStrs[0], sc));
                        }
                        break;

                    }
                }
            }
        }

        return scoreInfos;
    }

    /**
     * 获取单个赛事玩法总数
     *
     * @return
     */
    @Override
    public int getPlayTypeCount() {
        return matchInfo.mc;
    }

    /**
     * 获取玩法列表
     *
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo : matchInfo.hps) {
            PlayTypePm playTypePm = new PlayTypePm(playTypeInfo);
            playTypeList.add(playTypePm);
        }
        return playTypeList;
    }

    /**
     * 是否有视频直播
     *
     * @return
     */
    @Override
    public boolean hasVideo() {
        return matchInfo.mms != -1 && !getVideoUrls().isEmpty();
    }

    @Override
    public boolean isVideoStart() {
        return matchInfo.mms == 2 || matchInfo.mms == 1;
    }

    /**
     * 是否有动画直播
     *
     * @return
     */
    @Override
    public boolean hasAs() {
        return matchInfo.mvs != -1 && !getAnmiUrls().isEmpty();
    }

    @Override
    public boolean isAnimationStart() {
        return matchInfo.mvs == 2 || matchInfo.mvs == 1;
    }

    @Override
    public List<String> getVideoUrls() {
        List<String> urls = new ArrayList<>();
        if (matchInfo != null && matchInfo.vs != null) {
            for (VideoInfo videoInfo : matchInfo.vs) {
                if (!TextUtils.isEmpty(videoInfo.flvUrl)) {
                    urls.add(videoInfo.flvUrl);
                } else if (!TextUtils.isEmpty(videoInfo.muUrl)) {
                    urls.add(videoInfo.muUrl);
                }
            }
        }
        return urls;
    }

    /**
     * 获取动画播放源信息
     *
     * @return
     */
    @Override
    public List<String> getAnmiUrls() {
        return matchInfo.as;
    }

    /**
     * 获取联赛信息
     *
     * @return
     */
    @Override
    public League getLeague() {
        LeagueInfo leagueInfo;
        if (mLeague == null) {
            leagueInfo = new LeagueInfo();
            mLeague = new LeaguePm(leagueInfo);
        } else {
            leagueInfo = mLeague.getLeagueInfo();
        }
        leagueInfo.picUrlthumb = matchInfo.lurl;
        leagueInfo.nameText = matchInfo.tn;
        if (!TextUtils.isEmpty(matchInfo.tid)) {
            leagueInfo.tournamentId = Long.valueOf(matchInfo.tid);
        }
        return mLeague;
    }

    /**
     * 获取主队logo
     *
     * @return
     */
    @Override
    public String getIconMain() {
        if (matchInfo == null || matchInfo.mhlu.isEmpty()) {
            return "";
        }
        String logoUrl = matchInfo.mhlu.get(0);
        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
        if (domain.endsWith("/") && logoUrl.startsWith("/")) {
            return domain.substring(domain.indexOf("/")) + logoUrl;
        } else if (!domain.endsWith("/") && !logoUrl.startsWith("/")) {
            return domain + "/" + logoUrl;
        } else {
            return domain + logoUrl;
        }
    }

    /**
     * 获取客队logo
     *
     * @return
     */
    @Override
    public String getIconVisitor() {
        if (matchInfo == null || matchInfo.malu.isEmpty()) {
            return "";
        }
        String logoUrl = matchInfo.malu.get(0);
        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
        if (domain.endsWith("/") && logoUrl.startsWith("/")) {
            return domain.substring(domain.indexOf("/")) + logoUrl;
        } else if (!domain.endsWith("/") && !logoUrl.startsWith("/")) {
            return domain + "/" + logoUrl;
        } else {
            return domain + logoUrl;
        }
    }

    /**
     * 获取比赛是否进行中状态
     *
     * @return
     */
    @Override
    public boolean isGoingon() {
        return !TextUtils.equals(matchInfo.mmp, "0") && !TextUtils.equals(matchInfo.mmp, "90")
                && !TextUtils.equals(matchInfo.mmp, "999") && !TextUtils.equals(matchInfo.mmp, "61");
    }

    /**
     * 获取比赛开始时间
     *
     * @return
     */
    @Override
    public long getMatchTime() {
        return Long.valueOf(matchInfo.mgt);
    }

    /**
     * 是否冠军赛事
     *
     * @return
     */
    @Override
    public boolean isChampion() {
        return this.matchInfo.mcg == 100;
    }

    /**
     * 获取赛种ID，如足球，篮球
     */
    @Override
    public String getSportId() {
        return matchInfo.csid;
    }
    /**
     * 获取赛种名称，如足球，篮球
     */
    @Override
    public String getSportName() {
        return matchInfo.csna;
    }

    @Override
    public String getReferUrl() {
        return referUrl;
    }

    /**
     * PM设置播放器请求头信息
     *
     * @param referUrl
     */
    @Override
    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    /**
     * 是否已经产生有角球
     *
     * @return
     */
    @Override
    public boolean hasCornor() {
        List<Integer> cornor = getScore(PMConstants.SCORE_TYPE_CORNER);
        return cornor.get(0) > 0 || cornor.get(1) > 0;
    }

    /**
     * 是否中立场
     *
     * @return
     */
    @Override
    public boolean isNeutrality() {
        return matchInfo.mng == 1;
    }

    @Override
    public String getFormat() {
        return matchInfo.mfo;
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
