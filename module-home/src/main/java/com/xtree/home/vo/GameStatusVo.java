package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class GameStatusVo implements Parcelable {

    public int cid; // 41
    public String name; // FB自营体育
    public String alias; // fbxc
    public int status; // 0是维护，1是正常，2是下架
    public String maintenance_start; //维护开始
    public String maintenance_end; //维护结束

    @Override
    public String toString() {
        return "GameStatusVo{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", status=" + status +
                ", maintenance_start='" + maintenance_start + '\'' +
                ", maintenance_end='" + maintenance_end + '\'' +
                '}';
    }

    protected GameStatusVo(Parcel in) {
        cid = in.readInt();
        name = in.readString();
        alias = in.readString();
        status = in.readInt();
        maintenance_start = in.readString();
        maintenance_end = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cid);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeInt(status);
        dest.writeString(maintenance_start);
        dest.writeString(maintenance_end);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GameStatusVo> CREATOR = new Creator<GameStatusVo>() {
        @Override
        public GameStatusVo createFromParcel(Parcel in) {
            return new GameStatusVo(in);
        }

        @Override
        public GameStatusVo[] newArray(int size) {
            return new GameStatusVo[size];
        }
    };
}
