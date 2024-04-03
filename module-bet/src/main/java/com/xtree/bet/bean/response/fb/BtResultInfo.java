package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BtResultInfo implements BaseBean {
    /**
     * 单后订单状态code，由于系统为异步处理订单，下单后订单处于未确认状态
     */
    public int st;
    /**
     * 订单ID，返回为字符串
     */
    public String id;
    /**
     * 订单选项集合
     */
    public List<BtResultOptionInfo> ops = new ArrayList<>();
    /**
     * 据单原因码 , see enum: order_reject_type
     */
    public int rj;
    /**
     * 据单原因
     */
    public String rjs;
    /**
     * 用户输赢
     */
    public String uwl;
    /**
     * 注单类型(0:单注 1:串关 ) , see enum: series_type
     */
    public int sert;
    /**
     * 总注单数，单关为1，串关为子单个数，如4串4*11，则该字段为11
     */
    public int bn;
    /**
     * 选项个数
     */
    public int al;
    /**
     * 总投注额
     */
    public double sat;
    /**
     * 正常结算派奖金额
     */
    public double sa;
    /**
     * 是否接受赔率变更设置：0不接受，1 接受更好赔率，2接受任意赔率 , see enum: odds_change_enum
     */
    public int oc;
    /**
     * 订单结算时间，13位数字时间戳
     */
    public long stm;
    /**
     * 订单下单时间，13位数字时间戳
     */
    public long cte;
    /**
     * 订单取消时间，13位数字时间戳
     */
    public long ct;
    /**
     * 订单变更时间，13位数字时间戳
     */
    public long mt;
    /**
     * 第三方备注
     */
    public String rmk;
    /**
     * 单笔投注金额，单关时和总投注额相等，串关为子单投注额
     */
    public double us;
    /**
     * 串关类型，(例:3x1*3)
     */
    public String bt;
    /**
     * 选项个数，单关为1，串关为选项个数
     */
    public int ic;
    /**
     * 串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11*11），则为0；
     */
    public int sv;
    /**
     * 剩余可赢额，如有部分提前结算，该字段为剩余本金*赔率
     */
    public double lwa;
    /**
     * 可返还金额，包含本金
     */
    public double mla;
    /**
     * 最大可赢额，不包含本金
     */
    public double mwa;
    /**
     * 币种ID , see enum: currency
     */
    public int cid;
    /**
     * 汇率快照
     */
    public double exr;
    /**
     * 是否支持提前结算, 1:支持,0:不支持
     */
    public int co;
    /**
     * 是否二次结算
     */
    public boolean ss;
    /**
     * 提前结算报价数据
     */
    public BtCashOutPriceOrderInfo pr;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.st);
        dest.writeString(this.id);
        dest.writeTypedList(this.ops);
        dest.writeInt(this.rj);
        dest.writeString(this.rjs);
        dest.writeString(this.uwl);
        dest.writeInt(this.sert);
        dest.writeInt(this.bn);
        dest.writeInt(this.al);
        dest.writeDouble(this.sat);
        dest.writeDouble(this.sa);
        dest.writeInt(this.oc);
        dest.writeLong(this.stm);
        dest.writeLong(this.cte);
        dest.writeLong(this.ct);
        dest.writeLong(this.mt);
        dest.writeString(this.rmk);
        dest.writeDouble(this.us);
        dest.writeString(this.bt);
        dest.writeInt(this.ic);
        dest.writeInt(this.sv);
        dest.writeDouble(this.lwa);
        dest.writeDouble(this.mla);
        dest.writeDouble(this.mwa);
        dest.writeInt(this.cid);
        dest.writeDouble(this.exr);
        dest.writeInt(this.co);
        dest.writeByte(this.ss ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.st = source.readInt();
        this.id = source.readString();
        this.ops = source.createTypedArrayList(BtResultOptionInfo.CREATOR);
        this.rj = source.readInt();
        this.rjs = source.readString();
        this.uwl = source.readString();
        this.sert = source.readInt();
        this.bn = source.readInt();
        this.al = source.readInt();
        this.sat = source.readDouble();
        this.sa = source.readDouble();
        this.oc = source.readInt();
        this.stm = source.readInt();
        this.cte = source.readInt();
        this.ct = source.readInt();
        this.mt = source.readInt();
        this.rmk = source.readString();
        this.us = source.readDouble();
        this.bt = source.readString();
        this.ic = source.readInt();
        this.sv = source.readInt();
        this.lwa = source.readDouble();
        this.mla = source.readDouble();
        this.mwa = source.readDouble();
        this.cid = source.readInt();
        this.exr = source.readDouble();
        this.co = source.readInt();
        this.ss = source.readByte() != 0;
    }

    public BtResultInfo() {
    }

    protected BtResultInfo(Parcel in) {
        this.st = in.readInt();
        this.id = in.readString();
        this.ops = in.createTypedArrayList(BtResultOptionInfo.CREATOR);
        this.rj = in.readInt();
        this.rjs = in.readString();
        this.uwl = in.readString();
        this.sert = in.readInt();
        this.bn = in.readInt();
        this.al = in.readInt();
        this.sat = in.readDouble();
        this.sa = in.readDouble();
        this.oc = in.readInt();
        this.stm = in.readInt();
        this.cte = in.readInt();
        this.ct = in.readInt();
        this.mt = in.readInt();
        this.rmk = in.readString();
        this.us = in.readInt();
        this.bt = in.readString();
        this.ic = in.readInt();
        this.sv = in.readInt();
        this.lwa = in.readDouble();
        this.mla = in.readDouble();
        this.mwa = in.readDouble();
        this.cid = in.readInt();
        this.exr = in.readDouble();
        this.co = in.readInt();
        this.ss = in.readByte() != 0;
    }

    public static final Creator<BtResultInfo> CREATOR = new Creator<BtResultInfo>() {
        @Override
        public BtResultInfo createFromParcel(Parcel source) {
            return new BtResultInfo(source);
        }

        @Override
        public BtResultInfo[] newArray(int size) {
            return new BtResultInfo[size];
        }
    };
}
