package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.fb.OptionDataListInfo;
import com.xtree.bet.bean.response.fb.OptionInfo;
import com.xtree.bet.constant.SPKey;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.xtree.mvvmhabit.utils.SPUtils;

public class OptionFb implements Option {
    private int change;
    private OptionInfo mOptionInfo;

    private String code;

    private OptionDataListInfo optionList;

    public OptionFb(OptionInfo optionInfo) {
        this.mOptionInfo = optionInfo;
    }

    public OptionFb(OptionInfo optionInfo, OptionDataListInfo optionList) {
        this.mOptionInfo = optionInfo;
        this.optionList = optionList;
    }

    @Override
    public String getId() {
        return "";
    }

    /**
     * 选项全称，投注框一般用全称展示
     */
    public String getName() {
        if (mOptionInfo == null) {
            return "";
        }
        return mOptionInfo.na;
    }

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    public String getSortName() {
        return mOptionInfo.nm;
    }

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     *
     * @return
     */
    public String getOptionType() {
        return String.valueOf(mOptionInfo.ty);
    }

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double getUiShowOdd() {
        if (isHongKongMarket()) {
            BigDecimal bg = new BigDecimal(mOptionInfo.od - 1);
            return bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return mOptionInfo.od;
    }

    @Override
    public double getRealOdd() {
        return mOptionInfo.od;
    }

    /**
     * 赔率
     */
    public double getBodd() {
        return mOptionInfo.bod;
    }

    /**
     * 赔率类型
     */
    public int getOddType() {
        return mOptionInfo.odt;
    }

    /**
     * 是否香港盘
     *
     * @return
     */
    @Override
    public boolean isHongKongMarket() {
        try {
            return SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE) == 2;
        } catch (Exception e) {
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int getSettlementResult() {
        return mOptionInfo.otcm;
    }

    @Override
    public boolean setSelected(boolean isSelected) {
        return mOptionInfo.isSelected = isSelected;
    }

    /**
     * 是否选中
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return mOptionInfo.isSelected;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     *
     * @return
     */
    @Override
    public String getLine() {
        return mOptionInfo.li;
    }

    /**
     * 设置投注选择唯一标识
     *
     * @param code
     */
    @Override
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取投注选择唯一标识
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 设置投注项赔率的变化
     *
     * @return
     */
    @Override
    public void setChange(double oldOdd) {
        change = oldOdd < getRealOdd() ? 1 : oldOdd > getRealOdd() ? -1 : 0;
        //Log.e("test", "===========" + change);
        mOptionInfo.change = change;
    }

    /**
     * 赔率是否上升
     *
     * @return
     */
    @Override
    public boolean isUp() {
        return mOptionInfo.change == 1;
    }

    /**
     * 赔率是否下降
     *
     * @return
     */
    @Override
    public boolean isDown() {
        return mOptionInfo.change == -1;
    }

    @Override
    public void reset() {
        mOptionInfo.change = 0;
    }

    /**
     * 获取投注选项所属的投注线
     *
     * @return
     */
    @Override
    public OptionList getOptionList() {
        if (optionList == null) {
            return null;
        }
        return new OptionListFb(optionList);
    }

    @Override
    public boolean isBtHome() {
        return false;
    }

    /*@Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj){
            return true;
        }
        if(obj.getClass() != OptionFb.class){
            return false;
        }
        OptionFb optionFb = (OptionFb) obj;
        return getCode() == optionFb.getCode();
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mOptionInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.mOptionInfo = source.readParcelable(OptionInfo.class.getClassLoader());
    }

    protected OptionFb(Parcel in) {
        this.mOptionInfo = in.readParcelable(OptionInfo.class.getClassLoader());
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
