package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.pm.BtConfirmInfo;
import com.xtree.bet.bean.response.pm.OptionInfo;

/**
 * 投注确认页选项
 */
public class BetConfirmOptionPm implements BetConfirmOption {
    private BtConfirmInfo btConfirmInfo;
    private Match match;
    private PlayType playType;
    private OptionList optionList;
    private Option option;
    private String teamName;
    private double oldOdd;

    public BetConfirmOptionPm(BtConfirmInfo btConfirmInfo, String teamName) {
        this.btConfirmInfo = btConfirmInfo;
        this.teamName = teamName;
    }

    public BetConfirmOptionPm(Match match, PlayType playType, OptionList optionList, Option option) {
        this.optionList = optionList;
        this.option = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
        oldOdd = option.getRealOdd();
    }

    /**
     * 设置比赛及投注信息
     *
     * @return
     */
    public void setData(Match match, PlayType playType, OptionList optionList, Option option) {
        this.optionList = optionList;
        this.option = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
    }

    /**
     * PM获取投注信息唯一标识
     *
     * @return
     */
    @Override
    public String getMatchId() {
        if (btConfirmInfo != null) {
            return String.valueOf(btConfirmInfo.matchInfoId);
        } else {
            return String.valueOf(getMatch().getId());
        }
    }

    /**
     * 获取投注信息唯一标识
     *
     * @return
     */
    @Override
    public String getCode() {
        /*StringBuffer code = new StringBuffer();
        code.append(match.getId());
        code.append(playType.getPlayType());
        code.append(playType.getPlayPeriod());
        code.append(optionList.getId());
        code.append(option.getOptionType());
        if (!TextUtils.isEmpty(option.getLine())) {
            code.append(option.getLine());
        }*/
        if(TextUtils.isEmpty(option.getCode())){
            return getMatchId() + option.getId();
        }else{
            return option.getCode();
        }
    }

    @Override
    public int getPlaceNum() {
        if(btConfirmInfo != null) {
            return btConfirmInfo.placeNum;
        }else{
            return optionList.getPlaceNum();
        }
    }

    @Override
    public String getOptionName() {
        if(!TextUtils.isEmpty(getmOption().getSortName())){
            return getmOption().getSortName();
        }else if(btConfirmInfo != null && !TextUtils.isEmpty(btConfirmInfo.marketValue)){
            return btConfirmInfo.marketValue;
        }else {
            return option.isBtHome() ? match.getTeamMain() : match.getTeamVistor();
        }
    }

    @Override
    public String getPlayTypeId() {
        if (btConfirmInfo != null && !TextUtils.isEmpty(btConfirmInfo.id)) {
            return btConfirmInfo.id;
        } else if(optionList.getId() > 0){
            return String.valueOf(optionList.getId());
        } else {
            return playType.getMarketId();
        }
    }

    @Override
    public Option getmOption() {
        if(btConfirmInfo != null && btConfirmInfo.marketOddsList != null && !btConfirmInfo.marketOddsList.isEmpty()){
            OptionInfo optionInfo = new OptionInfo();
            optionInfo.oid = btConfirmInfo.marketOddsList.get(0).id;
            optionInfo.onb = TextUtils.isEmpty(option.getSortName()) ? option.isBtHome() ? btConfirmInfo.home : btConfirmInfo.away : option.getSortName();
            optionInfo.on = TextUtils.isEmpty(option.getSortName()) ? option.isBtHome() ? btConfirmInfo.home : btConfirmInfo.away : option.getSortName();
            optionInfo.ov = btConfirmInfo.marketOddsList.get(0).oddsValue;
            return new OptionPm(optionInfo);
        }else {
            return option;
        }
    }

    /**
     * 玩法销售状态是否关闭，0暂停，1开售，-1未开售
     *
     * @return
     */
    @Override
    public boolean isClose() {
        if (btConfirmInfo == null) {
            return false;
        }
        boolean isColse = btConfirmInfo.marketOddsList.get(0).oddsStatus == 2; // 首先检查投注项的关闭情况，如果投注项未关闭，再检查赛事级别的开关状态
        if (!isColse) {
            isColse = btConfirmInfo.matchHandicapStatus == 1 || btConfirmInfo.matchHandicapStatus == 2 || btConfirmInfo.matchHandicapStatus == 11;
        }
        return isColse;
    }

    @Override
    public String getScore() {
        return "";
    }

    @Override
    public String getTeamName() {
        if (TextUtils.isEmpty(match.getTeamMain())) {
            return match.getChampionMatchName();
        }
        return teamName;
    }

    /**
     * 获取投注的比赛信息
     *
     * @return
     */
    @Override
    public Match getMatch() {
        return match;
    }

    /**
     * 获取投注项类型
     *
     * @return
     */
    @Override
    public String getOptionType() {
        if (btConfirmInfo != null && btConfirmInfo.marketOddsList != null && !btConfirmInfo.marketOddsList.isEmpty()) {
            return String.valueOf(btConfirmInfo.marketOddsList.get(0).oddsType);
        } else {
            return option.getOptionType();
        }
    }

    /**
     * 获取投注玩法类型数据
     *
     * @return
     */
    @Override
    public OptionList getOptionList() {
        return optionList;
    }

    /**
     * 获取服务器返回的真实数据
     *
     * @return
     */
    @Override
    public void setRealData(BaseBean data) {
        this.btConfirmInfo = (BtConfirmInfo) data;
    }

    /**
     * 获取投注玩法数据
     *
     * @return
     */
    @Override
    public PlayType getPlayType() {
        return playType;
    }

    @Override
    public BaseBean getRealData() {
        return btConfirmInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.btConfirmInfo, flags);
        dest.writeParcelable(this.match, flags);
        dest.writeParcelable(this.playType, flags);
        dest.writeParcelable(this.optionList, flags);
        dest.writeParcelable(this.option, flags);
        dest.writeString(this.teamName);
    }

    public void readFromParcel(Parcel source) {
        this.btConfirmInfo = source.readParcelable(BtConfirmInfo.class.getClassLoader());
        this.match = source.readParcelable(Match.class.getClassLoader());
        this.playType = source.readParcelable(PlayType.class.getClassLoader());
        this.optionList = source.readParcelable(OptionList.class.getClassLoader());
        this.option = source.readParcelable(Option.class.getClassLoader());
        this.teamName = source.readString();
    }

    protected BetConfirmOptionPm(Parcel in) {
        this.btConfirmInfo = in.readParcelable(BtConfirmInfo.class.getClassLoader());
        this.match = in.readParcelable(Match.class.getClassLoader());
        this.playType = in.readParcelable(PlayType.class.getClassLoader());
        this.optionList = in.readParcelable(OptionList.class.getClassLoader());
        this.option = in.readParcelable(Option.class.getClassLoader());
        this.teamName = in.readString();
    }

    public static final Creator<BetConfirmOptionPm> CREATOR = new Creator<BetConfirmOptionPm>() {
        @Override
        public BetConfirmOptionPm createFromParcel(Parcel source) {
            return new BetConfirmOptionPm(source);
        }

        @Override
        public BetConfirmOptionPm[] newArray(int size) {
            return new BetConfirmOptionPm[size];
        }
    };
}
