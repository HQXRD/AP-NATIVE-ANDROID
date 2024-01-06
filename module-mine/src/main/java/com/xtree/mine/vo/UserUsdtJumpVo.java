package com.xtree.mine.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserUsdtJumpVo implements Parcelable {
    public String key = ""; // usdt
    public String mark = ""; // bindusdt
    public String tokenSign = "";
    public String title = ""; // 标题
    public boolean isShowType = false; // 是否需要显示类型/选择类型

    // 绑定/更新用
    public String controller = "security";
    public String action = ""; // adduserusdt
    //public String mark = ""; // bindusdt
    //public String tokenSign = "";
    public String id = ""; // old id
    public String type = ""; // ERC20_USDT,TRC20_USDT,""
    public String remind = ""; // 提醒(输入框下面显示的文字)

    @Override
    public String toString() {
        return "UserUsdtJumpVo{" +
                "key='" + key + '\'' +
                ", mark='" + mark + '\'' +
                ", tokenSign='" + tokenSign + '\'' +
                ", title='" + title + '\'' +
                ", isShowType=" + isShowType +
                ", controller='" + controller + '\'' +
                ", action='" + action + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", remind='" + remind + '\'' +
                '}';
    }

    public UserUsdtJumpVo() {
    }

    protected UserUsdtJumpVo(Parcel in) {
        key = in.readString();
        mark = in.readString();
        tokenSign = in.readString();
        title = in.readString();
        isShowType = in.readByte() != 0;
        controller = in.readString();
        action = in.readString();
        id = in.readString();
        type = in.readString();
        remind = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(mark);
        dest.writeString(tokenSign);
        dest.writeString(title);
        dest.writeByte((byte) (isShowType ? 1 : 0));
        dest.writeString(controller);
        dest.writeString(action);
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(remind);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserUsdtJumpVo> CREATOR = new Creator<UserUsdtJumpVo>() {
        @Override
        public UserUsdtJumpVo createFromParcel(Parcel in) {
            return new UserUsdtJumpVo(in);
        }

        @Override
        public UserUsdtJumpVo[] newArray(int size) {
            return new UserUsdtJumpVo[size];
        }
    };
}
