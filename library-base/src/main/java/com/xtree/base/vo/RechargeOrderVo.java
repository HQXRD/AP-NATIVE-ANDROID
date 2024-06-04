package com.xtree.base.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 未完成的充值订单
 */
public class RechargeOrderVo implements Parcelable {
    public String id; // "33805428",
    public String money; // "100.0000",
    public String status; // "0", 0-待处理, 2-成功, 4-超时无效
    @SerializedName("bank_id")
    public String bankId; // "176" 充值渠道编号 (极速充值会用到)
    public String created; // "2024-01-11 09:41:45",
    public String modified; // "2024-01-11 09:41:45",
    public String cancel_status; // "0",
    public String agent_rate_cost_amount; // "1.1000",
    public String payport_nickname; // "固额快充",
    @SerializedName("sysparam_prefix")
    public String sysParamPrefix; // "hqppay2",
    public String amount; // null,
    public String fee; // null,
    public String timeout; // 0
    public String orderurl; // ""
    public String recharge_json_channel; // false
    public String recharge_json_exporetime; // 434
    public String cancel_reason; // ""

    protected RechargeOrderVo(Parcel in) {
        id = in.readString();
        money = in.readString();
        status = in.readString();
        bankId = in.readString();
        created = in.readString();
        modified = in.readString();
        cancel_status = in.readString();
        agent_rate_cost_amount = in.readString();
        payport_nickname = in.readString();
        sysParamPrefix = in.readString();
        amount = in.readString();
        fee = in.readString();
        timeout = in.readString();
        orderurl = in.readString();
        recharge_json_channel = in.readString();
        recharge_json_exporetime = in.readString();
        cancel_reason = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(money);
        dest.writeString(status);
        dest.writeString(bankId);
        dest.writeString(created);
        dest.writeString(modified);
        dest.writeString(cancel_status);
        dest.writeString(agent_rate_cost_amount);
        dest.writeString(payport_nickname);
        dest.writeString(sysParamPrefix);
        dest.writeString(amount);
        dest.writeString(fee);
        dest.writeString(timeout);
        dest.writeString(orderurl);
        dest.writeString(recharge_json_channel);
        dest.writeString(recharge_json_exporetime);
        dest.writeString(cancel_reason);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RechargeOrderVo> CREATOR = new Creator<RechargeOrderVo>() {
        @Override
        public RechargeOrderVo createFromParcel(Parcel in) {
            return new RechargeOrderVo(in);
        }

        @Override
        public RechargeOrderVo[] newArray(int size) {
            return new RechargeOrderVo[size];
        }
    };
}