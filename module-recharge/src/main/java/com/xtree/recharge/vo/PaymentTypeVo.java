package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 充值类型 (大类型)
 */
public class PaymentTypeVo implements Parcelable {

    public String id; // "1",
    public String method_sort; // "1",
    public int recommend; // "1",
    public int is_hot; // "1",
    public String dispay_title; // "极速充值",
    public String selected_image; // "/uploads/bankimg/1711349643_4358.png",
    public String un_selected_image; // "/uploads/bankimg/1711349643.png",
    public List<String> deChannelArr; // ["176","203","231","232"],
    public List<RechargeVo> payChannelList;

    public String toInfo() {
        return "PaymentTypeVo{" +
                "id='" + id + '\'' +
                ", method_sort='" + method_sort + '\'' +
                ", recommend='" + recommend + '\'' +
                ", is_hot='" + is_hot + '\'' +
                ", dispay_title='" + dispay_title + '\'' +
                ", selected_image='" + selected_image + '\'' +
                ", un_selected_image='" + un_selected_image + '\'' +
                ", deChannelArr=" + deChannelArr +
                '}';
    }

    @Override
    public String toString() {
        return "PaymentTypeVo{" +
                "id='" + id + '\'' +
                ", method_sort='" + method_sort + '\'' +
                ", recommend='" + recommend + '\'' +
                ", is_hot='" + is_hot + '\'' +
                ", dispay_title='" + dispay_title + '\'' +
                ", selected_image='" + selected_image + '\'' +
                ", un_selected_image='" + un_selected_image + '\'' +
                ", deChannelArr=" + deChannelArr +
                ", payChannelList=" + payChannelList +
                '}';
    }

    protected PaymentTypeVo(Parcel in) {
        id = in.readString();
        method_sort = in.readString();
        recommend = in.readInt();
        is_hot = in.readInt();
        dispay_title = in.readString();
        selected_image = in.readString();
        un_selected_image = in.readString();
        deChannelArr = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(method_sort);
        dest.writeInt(recommend);
        dest.writeInt(is_hot);
        dest.writeString(dispay_title);
        dest.writeString(selected_image);
        dest.writeString(un_selected_image);
        dest.writeStringList(deChannelArr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentTypeVo> CREATOR = new Creator<PaymentTypeVo>() {
        @Override
        public PaymentTypeVo createFromParcel(Parcel in) {
            return new PaymentTypeVo(in);
        }

        @Override
        public PaymentTypeVo[] newArray(int size) {
            return new PaymentTypeVo[size];
        }
    };
}
