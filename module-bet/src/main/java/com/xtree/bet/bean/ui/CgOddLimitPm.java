package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.bet.bean.response.pm.CgOddLimitInfo;
import com.xtree.bet.manager.BtCarManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 熊猫体育
 */
public class CgOddLimitPm implements CgOddLimit {
    private double btCount;
    private CgOddLimitInfo cgOddLimitInfo;
    private Map<String, String> cgName = new HashMap<>();

    public CgOddLimitPm(CgOddLimitInfo cgOddLimitInfo) {
        this.cgOddLimitInfo = cgOddLimitInfo;
        cgName.put("1", "单关");
        cgName.put("2001", "2串1");
        cgName.put("3001", "3串1");
        cgName.put("3004", "3串4");
        cgName.put("4001", "4串1");
        cgName.put("40011", "4串11");
        cgName.put("5001", "5串1");
        cgName.put("50026", "5串26");
        cgName.put("6001", "6串1");
        cgName.put("60057", "6串57");
        cgName.put("7001", "7串1");
        cgName.put("700120", "7串120");
        cgName.put("8001", "8串1");
        cgName.put("800247", "8串247");
        cgName.put("9001", "9串1");
        cgName.put("900502", "9串502");
        cgName.put("10001", "10串1");
        cgName.put("10001013", "10串1013");
    }

    @Override
    public int getCgCount() {
        //串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11×11），则为0；
        if (cgOddLimitInfo == null || TextUtils.isEmpty(getCgName())) {
            return 1;
        }
        if (TextUtils.equals("1", getCgType())) {
            return 1;
        }
        String[] ints = getCgName().split("串");
        int sn = Integer.parseInt(ints[0]);
        if (!ints[1].equals("1")) {
            sn = 0;
        }
        return sn;
    }

    /*串关类型 1：单关，2001：2串1，3001：3串1，3004：3串4，4001：4串1， 40011：4串11，5001：5串1，50026：5串26，6001：6串1， 60057：6串57
        ，7001：7串1，700120：7串120，8001：8串1， 800247：8串247，9001：9串1，900502：502，10001：10串1， 10001013：10串1013*/
    @Override
    public String getCgName() {
        if(cgOddLimitInfo == null){
            return "";
        } else  {
            return cgName.get(cgOddLimitInfo.type);
        }
    }

    @Override
    public String getCgType() {
        return cgOddLimitInfo.type;
    }

    @Override
    public double getDMin() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return Double.valueOf(cgOddLimitInfo.minBet);
    }

    @Override
    public double getDMax() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return Double.valueOf(cgOddLimitInfo.orderMaxPay);
    }

    @Override
    public double getCMin() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return Double.valueOf(cgOddLimitInfo.minBet);
    }

    @Override
    public double getCMax() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return Double.valueOf(cgOddLimitInfo.orderMaxPay);
    }

    @Override
    public double getDOdd() {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return Double.valueOf(cgOddLimitInfo.seriesOdds);
    }

    @Override
    public double getCOdd() {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return Double.valueOf(cgOddLimitInfo.seriesOdds);
    }

    @Override
    public double getWin(double amount) {
        return Double.valueOf(cgOddLimitInfo.seriesOdds) * amount;
    }

    @Override
    public int getBtCount() {
        if(cgOddLimitInfo == null || TextUtils.isEmpty(getCgName())){
            return 1;
        }
        String cgName = getCgName();
        if(TextUtils.equals("1", getCgType())){
            return 1;
        }
        String[] ints = cgName.split("串");
        int btCount = Integer.valueOf(ints[1]);
        if(btCount == 1){
            btCount = calculate(BtCarManager.size(), Integer.valueOf(ints[0]));
        }

        return btCount;
    }

//    public int calculate(int n, int m) {
//        try {
//            if (m == 0 || n == m) {
//                return 1;
//            }
//            return calculate(n - 1, m - 1) + calculate(n - 1, m);
//        }catch (Throwable e){
//            e.printStackTrace();
//        }
//        return 1;
//    }

    /**
     * 计算组合数 C(n, k)
     */
    public int calculate(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    /**
     * 计算阶乘
     */
    public static int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int num = 1;
        for (int i = 1; i <= n; i++) {
            num = num * i;
        }
        return num;
    }

    /**
     * 设置投注金额
     * @return
     */
    @Override
    public void setBtAmount(double count) {
        btCount = count;
    }
    /**
     * 获取投注金额
     * @return
     */
    @Override
    public double getBtAmount() {
        return btCount;
    }
    /**
     * 获取总投注金额
     * @return
     */
    @Override
    public double getBtTotalAmount() {
        return btCount * getBtCount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.btCount);
        dest.writeParcelable(this.cgOddLimitInfo, flags);
        dest.writeInt(this.cgName.size());
        for (Map.Entry<String, String> entry : this.cgName.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public void readFromParcel(Parcel source) {
        this.btCount = source.readDouble();
        this.cgOddLimitInfo = source.readParcelable(CgOddLimitInfo.class.getClassLoader());
        int cgNameSize = source.readInt();
        this.cgName = new HashMap<String, String>(cgNameSize);
        for (int i = 0; i < cgNameSize; i++) {
            String key = source.readString();
            String value = source.readString();
            this.cgName.put(key, value);
        }
    }

    protected CgOddLimitPm(Parcel in) {
        this.btCount = in.readDouble();
        this.cgOddLimitInfo = in.readParcelable(CgOddLimitInfo.class.getClassLoader());
        int cgNameSize = in.readInt();
        this.cgName = new HashMap<String, String>(cgNameSize);
        for (int i = 0; i < cgNameSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.cgName.put(key, value);
        }
    }

    public static final Creator<CgOddLimitPm> CREATOR = new Creator<CgOddLimitPm>() {
        @Override
        public CgOddLimitPm createFromParcel(Parcel source) {
            return new CgOddLimitPm(source);
        }

        @Override
        public CgOddLimitPm[] newArray(int size) {
            return new CgOddLimitPm[size];
        }
    };
}
