package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class GameVo implements Parcelable {

    public String id; //
    public int cid = 0; // 41
    public String cateId = "";
    public String name; // FB自营体育
    public String alias; // fbxc
    public int status = 1; // 0是维护, 1是正常, 2是下架
    public String maintenance_start; //维护开始
    public String maintenance_end; //维护结束

    public String playURL; // aggame/fishplay?autoThrad
    public boolean isH5 = false; // true:跳H5, false:原生的
    public String imageName; //
    public boolean twoImage = false; // 是否两张图片(左右不一样)
    public String gameId; //

    public String title; //
    public String picture; //
    public int typeId; // 类型(图片level)
    public int pId; // 父类型

    public GameVo() {
    }

    @Override
    public String toString() {
        return "GameVo{" +
                "id='" + id + '\'' +
                ", cid=" + cid +
                ", cateId=" + cateId +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", status=" + status +
                ", maintenance_start='" + maintenance_start + '\'' +
                ", maintenance_end='" + maintenance_end + '\'' +
                ", playURL='" + playURL + '\'' +
                ", isH5=" + isH5 +
                ", imageName='" + imageName + '\'' +
                ", twoImage=" + twoImage +
                ", gameId='" + gameId + '\'' +
                ", title='" + title + '\'' +
                ", picture='" + picture + '\'' +
                ", typeId=" + typeId +
                ", pId=" + pId +
                '}';
    }

    protected GameVo(Parcel in) {
        id = in.readString();
        cid = in.readInt();
        cateId = in.readString();
        name = in.readString();
        alias = in.readString();
        status = in.readInt();
        maintenance_start = in.readString();
        maintenance_end = in.readString();
        playURL = in.readString();
        isH5 = in.readByte() != 0;
        imageName = in.readString();
        twoImage = in.readByte() != 0;
        gameId = in.readString();
        title = in.readString();
        picture = in.readString();
        typeId = in.readInt();
        pId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(cid);
        dest.writeString(cateId);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeInt(status);
        dest.writeString(maintenance_start);
        dest.writeString(maintenance_end);
        dest.writeString(playURL);
        dest.writeByte((byte) (isH5 ? 1 : 0));
        dest.writeString(imageName);
        dest.writeByte((byte) (twoImage ? 1 : 0));
        dest.writeString(gameId);
        dest.writeString(title);
        dest.writeString(picture);
        dest.writeInt(typeId);
        dest.writeInt(pId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GameVo> CREATOR = new Creator<GameVo>() {
        @Override
        public GameVo createFromParcel(Parcel in) {
            return new GameVo(in);
        }

        @Override
        public GameVo[] newArray(int size) {
            return new GameVo[size];
        }
    };

}
