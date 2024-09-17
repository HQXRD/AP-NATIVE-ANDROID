package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderInfoVo implements Parcelable {

    public String returncode; // "00"
    public String message; // "成功"
    public String merchant_order; // "PAY0017292024052818444600000656"
    public String status; // "11"
    public String create_time; // "2024-05-28 18:44:46"
    public String expire_time; // "2024-05-28 18:52:46"
    public int allow_cancel_wait; // 0
    public String cancel_wait_time; // ""
    public int allow_cancel; // 1
    public String allow_cancel_time; // "2024-05-28 18:45:26"
    public String bank_account; // "0005678"
    public String bank_account_name; // "柯帝斯"
    public String bank_code; // "PSBC"
    public String bank_name; // "中国邮政储蓄银行"
    public String bank_area; // "cn"
    public String qrcode; // ""

    protected OrderInfoVo(Parcel in) {
        returncode = in.readString();
        message = in.readString();
        merchant_order = in.readString();
        status = in.readString();
        create_time = in.readString();
        expire_time = in.readString();
        allow_cancel_wait = in.readInt();
        cancel_wait_time = in.readString();
        allow_cancel = in.readInt();
        allow_cancel_time = in.readString();
        bank_account = in.readString();
        bank_account_name = in.readString();
        bank_code = in.readString();
        bank_name = in.readString();
        bank_area = in.readString();
        qrcode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(returncode);
        dest.writeString(message);
        dest.writeString(merchant_order);
        dest.writeString(status);
        dest.writeString(create_time);
        dest.writeString(expire_time);
        dest.writeInt(allow_cancel_wait);
        dest.writeString(cancel_wait_time);
        dest.writeInt(allow_cancel);
        dest.writeString(allow_cancel_time);
        dest.writeString(bank_account);
        dest.writeString(bank_account_name);
        dest.writeString(bank_code);
        dest.writeString(bank_name);
        dest.writeString(bank_area);
        dest.writeString(qrcode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderInfoVo> CREATOR = new Creator<OrderInfoVo>() {
        @Override
        public OrderInfoVo createFromParcel(Parcel in) {
            return new OrderInfoVo(in);
        }

        @Override
        public OrderInfoVo[] newArray(int size) {
            return new OrderInfoVo[size];
        }
    };

    @Override
    public String toString() {
        return "OrderInfoVo { " +
                "returncode='" + returncode + '\'' +
                ", message='" + message + '\'' +
                ", merchant_order='" + merchant_order + '\'' +
                ", status='" + status + '\'' +
                ", create_time='" + create_time + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", allow_cancel_wait=" + allow_cancel_wait +
                ", cancel_wait_time='" + cancel_wait_time + '\'' +
                ", allow_cancel=" + allow_cancel +
                ", allow_cancel_time='" + allow_cancel_time + '\'' +
                ", bank_account='" + bank_account + '\'' +
                ", bank_account_name='" + bank_account_name + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_area='" + bank_area + '\'' +
                ", qrcode='" + qrcode + '\'' +
                '}';
    }

}
