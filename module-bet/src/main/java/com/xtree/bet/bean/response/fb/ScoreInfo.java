package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class ScoreInfo implements BaseBean {
    /**
     * 赛事阶段 , see enum: period
     */
    public int pe;
    /**
     * 比分类型，如 比分、角球、红黄牌等类型 , see enum: result_type_group
     */
    public int tyg;
    /**
     * 比分，数组形式，第一个数是主队分，第二个数是客对分
     */
    public List<Integer> sc;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pe);
        dest.writeInt(this.tyg);
        dest.writeList(this.sc);
    }

    public void readFromParcel(Parcel source) {
        this.pe = source.readInt();
        this.tyg = source.readInt();
        this.sc = new ArrayList<Integer>();
        source.readList(this.sc, Integer.class.getClassLoader());
    }

    public ScoreInfo() {
    }

    protected ScoreInfo(Parcel in) {
        this.pe = in.readInt();
        this.tyg = in.readInt();
        this.sc = new ArrayList<Integer>();
        in.readList(this.sc, Integer.class.getClassLoader());
    }

    public static final Creator<ScoreInfo> CREATOR = new Creator<ScoreInfo>() {
        @Override
        public ScoreInfo createFromParcel(Parcel source) {
            return new ScoreInfo(source);
        }

        @Override
        public ScoreInfo[] newArray(int size) {
            return new ScoreInfo[size];
        }
    };
}
