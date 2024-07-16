package com.xtree.bet.bean.ui;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.VideoInfo;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.constant.PMMatchPeriod;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchPm implements Match {
    private String className;
    MatchInfo matchInfo;

    LeaguePm mLeague;
    /**
     * 播放器请求头
     */
    private String referUrl;
    private boolean isHead;
    private boolean isExpand;

    public MatchPm(){
        this.className = getClass().getSimpleName();
    }

    public MatchPm(MatchInfo matchInfo) {
        this.className = getClass().getSimpleName();
        this.matchInfo = matchInfo;
    }

    @Override
    public boolean isHead() {
        return isHead;
    }

    @Override
    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    @Override
    public boolean isExpand() {
        return isExpand;
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
     * 是否足球比赛下半场
     * @return
     */
    @Override
    public boolean isFootBallSecondHalf() {
        return TextUtils.equals(matchInfo.mmp, "7") || TextUtils.equals(matchInfo.mmp, "31");
    }

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     *
     * @return
     */
    @Override
    public String getTime() {
        try {
            if (matchInfo != null && matchInfo.mst != null) {
                return TimeUtils.sToMs(Integer.valueOf(matchInfo.mst));
            } else {
                return "";
            }
        }catch (Exception e){
            return "";
        }

    }

    /**
     * 获取走表时间秒
     * @return
     */
    @Override
    public int getTimeS() {
        if(matchInfo != null && matchInfo.mst != null) {
            return Integer.valueOf(matchInfo.mst);
        }else {
            return 0;
        }
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
                    if (!TextUtils.isEmpty(score) && score.contains(":") && score.split(":").length > 1) {
                        sc.add(Double.valueOf(score.split(":")[0]).intValue()); // 修复小数转换整数异常
                        sc.add(Double.valueOf(score.split(":")[1]).intValue());
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
     * 获取上半场比分信息
     * @return
     */
    @Override
    public List<Integer> getFirstHalfScore() {
        return getScore(new String[]{String.valueOf(PMConstants.SCORE_TYPE_SCORE_SECOND_HALF)});
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
        if (matchInfo == null || matchInfo.mhlu == null || matchInfo.mhlu.isEmpty()) {
            return "";
        }
        String logoUrl = matchInfo.mhlu.get(0);
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_IMG_SERVICE_URL);
        if (TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC)) {
            domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
        }
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
        if (matchInfo == null || matchInfo.malu == null || matchInfo.malu.isEmpty() || TextUtils.isEmpty(matchInfo.malu.get(0))) {
            return "";
        }
        String logoUrl = matchInfo.malu.get(0);
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_IMG_SERVICE_URL);
        if (TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC)) {
            domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
        }
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

    /**
     * 获取赛制
     * @return
     */
    @Override
    public String getFormat() {
        return matchInfo.mfo;
    }

    @Override
    public boolean isHomeSide() {
        return TextUtils.equals(matchInfo.mat, "home");
    }

    /**
     * 是否需要显示发球方图标
     * @return
     */
    @Override
    public boolean needCheckHomeSide() {
        return TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_WQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_PQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_STPQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_YMQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_BBQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_SNK)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_BQ)
                || TextUtils.equals(matchInfo.csid, PMConstants.SPORT_ID_MSZQ);
    }

    /**
     * 是否篮球上下半场赛节配置
     * @return
     */
    @Override
    public boolean isBasketBallDouble() {
        if(matchInfo == null){
            return false;
        }
        return matchInfo.mle == 17;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeParcelable(this.matchInfo, flags);
        dest.writeParcelable(this.mLeague, flags);
        dest.writeString(this.referUrl);
        dest.writeByte(this.isHead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.matchInfo = source.readParcelable(MatchInfo.class.getClassLoader());
        this.mLeague = source.readParcelable(LeaguePm.class.getClassLoader());
        this.referUrl = source.readString();
        this.isHead = source.readByte() != 0;
        this.isExpand = source.readByte() != 0;
    }

    protected MatchPm(Parcel in) {
        this.className = in.readString();
        this.matchInfo = in.readParcelable(MatchInfo.class.getClassLoader());
        this.mLeague = in.readParcelable(LeaguePm.class.getClassLoader());
        this.referUrl = in.readString();
        this.isHead = in.readByte() != 0;
        this.isExpand = in.readByte() != 0;
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
