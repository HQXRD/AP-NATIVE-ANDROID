package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PaymentDataVo implements Parcelable {

    public String bankdirect_url;
    public int chongzhiListCount;
    public List<String> payCodeArr;
    public List<PaymentTypeVo> chongzhiList;
    public ProcessingDataVo processingData;

    public int showOnepayfixGuide;//"1"显示引导 ， “0”不显示

    //正在进行的极速充值订单id
    public int pendingOnepayfixBid;

    protected PaymentDataVo(Parcel in) {
        bankdirect_url = in.readString();
        chongzhiListCount = in.readInt();
        payCodeArr = in.createStringArrayList();
        chongzhiList = in.createTypedArrayList(PaymentTypeVo.CREATOR);
        processingData = in.readParcelable(ProcessingDataVo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankdirect_url);
        dest.writeInt(chongzhiListCount);
        dest.writeStringList(payCodeArr);
        dest.writeTypedList(chongzhiList);
        dest.writeParcelable(processingData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentDataVo> CREATOR = new Creator<PaymentDataVo>() {
        @Override
        public PaymentDataVo createFromParcel(Parcel in) {
            return new PaymentDataVo(in);
        }

        @Override
        public PaymentDataVo[] newArray(int size) {
            return new PaymentDataVo[size];
        }
    };

    @Override
    public String toString() {
        return "PaymentDataVo{" +
                "bankdirect_url='" + bankdirect_url + '\'' +
                ", chongzhiListCount=" + chongzhiListCount +
                ", payCodeArr=" + payCodeArr +
                ", chongzhiList=" + chongzhiList +
                ", processingData=" + processingData +
                '}';
    }

}
