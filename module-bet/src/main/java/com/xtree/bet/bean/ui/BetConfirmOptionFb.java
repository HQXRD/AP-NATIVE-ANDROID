package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.fb.BtConfirmOptionInfo;

/**
 * 投注确认页选项
 */
public class BetConfirmOptionFb implements BetConfirmOption{
    private BtConfirmOptionInfo btConfirmOptionInfo;
    private Match match;
    private PlayType playType;
    private OptionList optionList;
    private Option mOption;
    private String teamName;
    private double oldOdd;

    public BetConfirmOptionFb(BtConfirmOptionInfo btConfirmOptionInfo, String teamName){
        this.btConfirmOptionInfo = btConfirmOptionInfo;
        this.teamName = teamName;
    }

    public BetConfirmOptionFb(Match match, PlayType playType, OptionList optionList, Option option){
        this.optionList = optionList;
        this.mOption = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
        oldOdd = option.getRealOdd();
    }
    /**
     * 设置比赛及投注信息
     * @return
     */
    public void setData(Match match, PlayType playType, OptionList optionList, Option option){
        this.optionList = optionList;
        this.mOption = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
    }
    /**
     * PM获取投注信息唯一标识
     * @return
     */
    @Override
    public String getMatchId() {
        return String.valueOf(getMatch().getId());
    }

    /**
     * 获取投注信息唯一标识
     * @return
     */
    @Override
    public String getCode() {
        StringBuffer code = new StringBuffer();
        code.append(match.getId());
        code.append(playType.getPlayType());
        code.append(playType.getPlayPeriod());
        code.append(optionList.getId());
        code.append(mOption.getOptionType());
        if(!TextUtils.isEmpty(mOption.getLine())){
            code.append(mOption.getLine());
        }
        return code.toString();
    }

    @Override
    public int getPlaceNum() {
        return 0;
    }

    @Override
    public String getOptionName() {
        String optionName;

        /**
         * 选项全称，投注框一般用全称展示 String getName() na
         */

        /**
         * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示 public String getSortName()  nm
         */

        if(btConfirmOptionInfo != null && btConfirmOptionInfo.op != null) {
            String name = btConfirmOptionInfo.op.na;
            String sortName = btConfirmOptionInfo.op.nm;
            if(name != null && sortName != null){
                optionName = name.length() > sortName.length() ? name : sortName;
            } else if (sortName == null) {
                optionName = name;
            } else {
                optionName = sortName;
            }
        }else{
            optionName = mOption.getName() + " " + mOption.getLine() == null ? "" : mOption.getLine();
        }

        return optionName;
    }

    @Override
    public String getPlayTypeId() {
        if(btConfirmOptionInfo != null) {
            return String.valueOf(btConfirmOptionInfo.mid);
        }else{
            return String.valueOf(optionList.getId());
        }
    }

    @Override
    public Option getOption() {
        if(btConfirmOptionInfo != null && btConfirmOptionInfo.op != null) {
            mOption = new OptionFb(btConfirmOptionInfo.op);
            if(oldOdd > 0) {
                CfLog.e("========oldodd======" + oldOdd);
                CfLog.e("========newodd======" + mOption.getRealOdd());
                mOption.setChange(oldOdd);
            }
        }
        return mOption;
    }

    /**
     * 玩法销售状态是否关闭，0暂停，1开售，-1未开售
     * @return
     */
    @Override
    public boolean isClose() {
        if(btConfirmOptionInfo != null) {
            return btConfirmOptionInfo.ss == 0 || btConfirmOptionInfo.ss == -1;
        }else{
            return false;
        }
    }

    @Override
    public String getScore() {
        if(btConfirmOptionInfo != null && !TextUtils.isEmpty(btConfirmOptionInfo.re)) {
            return btConfirmOptionInfo.re;
        }else{
            return "";
        }
    }

    @Override
    public String getTeamName() {
        if(TextUtils.isEmpty(match.getTeamMain())){
            return match.getChampionMatchName();
        }
        return teamName;
    }

    /**
     * 获取投注的比赛信息
     * @return
     */
    @Override
    public Match getMatch() {
        return match;
    }

    /**
     * 获取投注项类型
     * @return
     */
    @Override
    public String getOptionType() {
        if(btConfirmOptionInfo != null && btConfirmOptionInfo.op != null) {
            return String.valueOf(btConfirmOptionInfo.op.ty);
        }else{
            return mOption.getOptionType();
        }
    }

    /**
     * 获取投注玩法类型数据
     * @return
     */
    @Override
    public OptionList getOptionList(){
        return optionList;
    }

    /**
     * 获取服务器返回的真实数据
     * @return
     */
    @Override
    public void setRealData(BaseBean data) {
        if(btConfirmOptionInfo != null && btConfirmOptionInfo.op != null) {
            oldOdd = btConfirmOptionInfo.op.od;
        }
        this.btConfirmOptionInfo = (BtConfirmOptionInfo) data;
    }

    /**
     * 获取投注玩法数据
     * @return
     */
    @Override
    public PlayType getPlayType() {
        return playType;
    }

    @Override
    public BaseBean getRealData() {
        return btConfirmOptionInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.btConfirmOptionInfo, flags);
        dest.writeParcelable(this.optionList, flags);
        dest.writeParcelable(this.mOption, flags);
        dest.writeString(this.teamName);
    }

    public void readFromParcel(Parcel source) {
        this.btConfirmOptionInfo = source.readParcelable(BtConfirmOptionInfo.class.getClassLoader());
        this.optionList = source.readParcelable(OptionList.class.getClassLoader());
        this.mOption = source.readParcelable(Option.class.getClassLoader());
        this.teamName = source.readString();
    }

    protected BetConfirmOptionFb(Parcel in) {
        this.btConfirmOptionInfo = in.readParcelable(BtConfirmOptionInfo.class.getClassLoader());
        this.optionList = in.readParcelable(OptionList.class.getClassLoader());
        this.mOption = in.readParcelable(Option.class.getClassLoader());
        this.teamName = in.readString();
    }

    public static final Creator<BetConfirmOptionFb> CREATOR = new Creator<BetConfirmOptionFb>() {
        @Override
        public BetConfirmOptionFb createFromParcel(Parcel source) {
            return new BetConfirmOptionFb(source);
        }

        @Override
        public BetConfirmOptionFb[] newArray(int size) {
            return new BetConfirmOptionFb[size];
        }
    };
}
