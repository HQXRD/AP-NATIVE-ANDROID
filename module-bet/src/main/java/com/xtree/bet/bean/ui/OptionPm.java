package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.bet.bean.response.pm.OptionDataListInfo;
import com.xtree.bet.bean.response.pm.OptionInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.constant.SPKey;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.xtree.mvvmhabit.utils.SPUtils;

public class OptionPm implements Option{
    private String className;
    private int change;
    public OptionInfo optionInfo;

    private String code;

    private OptionDataListInfo optionList;
    private PlayTypeInfo playTypeInfo;

    public OptionPm(OptionInfo optionInfo){
        this.optionInfo = optionInfo;
        this.className = getClass().getSimpleName();
    }

    public OptionPm(OptionInfo optionInfo, OptionDataListInfo optionList, PlayTypeInfo playTypeInfo){
        this.optionInfo = optionInfo;
        this.optionList = optionList;
        this.playTypeInfo = playTypeInfo;
        this.className = getClass().getSimpleName();
    }

    @Override
    public String getId() {
        if(optionInfo == null || optionInfo.oid == null){
            return "";
        }
        return optionInfo.oid;
    }

    /**
     * 投注项投注名称展示值
     */
    public String getName() {
        if(optionInfo == null){
            return "";
        }
        return optionInfo.on;
    }

    /**
     * 投注项列表页名称展示值
     */
    public String getSortName() {
        if(!TextUtils.isEmpty(optionInfo.onb)){
            return optionInfo.onb;
        } else if(!TextUtils.isEmpty(optionInfo.otv)){
            return optionInfo.otv;
        } else {
            return optionInfo.on;
        }
    }

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     * @return
     */
    public String getOptionType() {
        return optionInfo.ot;
    }

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double getUiShowOdd() {
        if(isHongKongMarket()){
            BigDecimal bg = new BigDecimal(getRealOdd() - 1);
            return bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        BigDecimal bg = new BigDecimal(getRealOdd());
        return bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public double getRealOdd() {
        return optionInfo.ov / 100000;
    }

    /**
     * 赔率
     */
    public double getBodd() {
        return optionInfo.ov;
    }

    /**
     * 赔率类型
     */
    public int getOddType() {
        return 0;
    }

    /**
     * 是否香港盘
     * @return
     */
    @Override
    public boolean isHongKongMarket() {
        return SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE) == 2;
    }

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int getSettlementResult() {
        return 0;
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
        return "";
    }
    /**
     * 设置投注选择唯一标识
     * @param code
     */
    @Override
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 获取投注选择唯一标识
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }
    /**
     * 设置投注项赔率的变化
     * @return
     */
    @Override
    public void setChange(double oldOdd) {
        change = oldOdd < getRealOdd() ? 1 : oldOdd > getRealOdd() ? -1 : 0;
        //Log.e("test", "===========" + change);
        optionInfo.change = change;
    }
    /**
     * 赔率是否上升
     * @return
     */
    @Override
    public boolean isUp() {
        return optionInfo.change == 1;
    }
    /**
     * 赔率是否下降
     * @return
     */
    @Override
    public boolean isDown() {
        return optionInfo.change == -1;
    }

    @Override
    public void reset() {
        optionInfo.change = 0;
    }
    /**
     * 获取投注选项所属的投注线
     * @return
     */
    @Override
    public OptionList getOptionList() {
        if(optionList == null){
            return null;
        }
        return new OptionListPm(optionList, playTypeInfo);
    }

    @Override
    public boolean isBtHome() {
        return TextUtils.equals(optionInfo.ots, "T1");
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
        dest.writeString(this.className);
        dest.writeInt(this.change);
        dest.writeParcelable(this.optionInfo, flags);
        dest.writeString(this.code);
        dest.writeParcelable(this.optionList, flags);
        dest.writeParcelable(this.playTypeInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.change = source.readInt();
        this.optionInfo = source.readParcelable(OptionInfo.class.getClassLoader());
        this.code = source.readString();
        this.optionList = source.readParcelable(OptionDataListInfo.class.getClassLoader());
        this.playTypeInfo = source.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    protected OptionPm(Parcel in) {
        this.className = in.readString();
        this.change = in.readInt();
        this.optionInfo = in.readParcelable(OptionInfo.class.getClassLoader());
        this.code = in.readString();
        this.optionList = in.readParcelable(OptionDataListInfo.class.getClassLoader());
        this.playTypeInfo = in.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    public static final Creator<OptionPm> CREATOR = new Creator<OptionPm>() {
        @Override
        public OptionPm createFromParcel(Parcel source) {
            return new OptionPm(source);
        }

        @Override
        public OptionPm[] newArray(int size) {
            return new OptionPm[size];
        }
    };
}
