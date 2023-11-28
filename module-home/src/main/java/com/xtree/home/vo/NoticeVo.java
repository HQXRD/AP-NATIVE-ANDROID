package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class NoticeVo implements Parcelable {
    public int id; // 2233
    public String title; // 开心电竞场馆维护公告
    public boolean is_top; // false
    public boolean is_recommended; // false
    public boolean is_read; // true
    public String created_at; // 2023-10-05 00:12:44

    @Override
    public String toString() {
        return "NoticeVo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", is_top=" + is_top +
                ", is_recommended=" + is_recommended +
                ", is_read=" + is_read +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    protected NoticeVo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        is_top = in.readByte() != 0;
        is_recommended = in.readByte() != 0;
        is_read = in.readByte() != 0;
        created_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeByte((byte) (is_top ? 1 : 0));
        dest.writeByte((byte) (is_recommended ? 1 : 0));
        dest.writeByte((byte) (is_read ? 1 : 0));
        dest.writeString(created_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoticeVo> CREATOR = new Creator<NoticeVo>() {
        @Override
        public NoticeVo createFromParcel(Parcel in) {
            return new NoticeVo(in);
        }

        @Override
        public NoticeVo[] newArray(int size) {
            return new NoticeVo[size];
        }
    };
}
