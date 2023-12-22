package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事统计
 */
public class MatchTypeInfo implements BaseBean {
    /**
     * 分类类型，如 滚球、今日、早盘等
     */
    public int ty;
    /**
     * 分类描述
     */
    public String des;
    /**
     * 赛事总数
     */
    public int tc;
    /**
     * 每个类型下每个运动里的赛事统计信息
     */
    public List<MatchTypeStatisInfo> ssl = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ty);
        dest.writeString(this.des);
        dest.writeInt(this.tc);
        dest.writeTypedList(this.ssl);
    }

    public void readFromParcel(Parcel source) {
        this.ty = source.readInt();
        this.des = source.readString();
        this.tc = source.readInt();
        this.ssl = source.createTypedArrayList(MatchTypeStatisInfo.CREATOR);
    }

    public MatchTypeInfo() {
    }

    protected MatchTypeInfo(Parcel in) {
        this.ty = in.readInt();
        this.des = in.readString();
        this.tc = in.readInt();
        this.ssl = in.createTypedArrayList(MatchTypeStatisInfo.CREATOR);
    }

    public static final Creator<MatchTypeInfo> CREATOR = new Creator<MatchTypeInfo>() {
        @Override
        public MatchTypeInfo createFromParcel(Parcel source) {
            return new MatchTypeInfo(source);
        }

        @Override
        public MatchTypeInfo[] newArray(int size) {
            return new MatchTypeInfo[size];
        }
    };
}
