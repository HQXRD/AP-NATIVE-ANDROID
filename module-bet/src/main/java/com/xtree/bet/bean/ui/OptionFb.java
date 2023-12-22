package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.OptionInfo;

public class OptionFb implements Option{
    private OptionInfo optionInfo;

    public OptionFb(OptionInfo optionInfo){
        this.optionInfo = optionInfo;
    }

    /**
     * 选项全称，投注框一般用全称展示
     */
    public String getName() {
        return optionInfo.na;
    }

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    public String getSortName() {
        return optionInfo.nm;
    }

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     * @return
     */
    public int getOptionType() {
        return optionInfo.ty;
    }

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double getOdd() {
        return optionInfo.od;
    }

    /**
     * 赔率
     */
    public double getBodd() {
        return optionInfo.bod;
    }

    /**
     * 赔率类型
     */
    public int getOddType() {
        return optionInfo.odt;
    }

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int getSettlementResult() {
        return optionInfo.otcm;
    }

    @Override
    public boolean setSelected(boolean isSelected) {
        return optionInfo.isSelected = isSelected;
    }

    /**
     * 是否选中
     * @return
     */
    @Override
    public boolean isSelected() {
        return optionInfo.isSelected;
    }

    /**
     * 	line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     * @return
     */
    @Override
    public String getLine() {
        return optionInfo.li;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.optionInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.optionInfo = source.readParcelable(OptionInfo.class.getClassLoader());
    }

    protected OptionFb(Parcel in) {
        this.optionInfo = in.readParcelable(OptionInfo.class.getClassLoader());
    }

    public static final Creator<OptionFb> CREATOR = new Creator<OptionFb>() {
        @Override
        public OptionFb createFromParcel(Parcel source) {
            return new OptionFb(source);
        }

        @Override
        public OptionFb[] newArray(int size) {
            return new OptionFb[size];
        }
    };
}
