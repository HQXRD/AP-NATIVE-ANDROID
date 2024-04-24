package com.xtree.bet.bean.ui;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.BtResultOptionInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ParcelCreator")
public class BtResultOptionPm implements BtResultOption {
    private BtRecordRsp.RecordsBean.DetailBean detailBean;

    private static Map<String, String> btResultMap = new HashMap<>();

    static {
        btResultMap.put("0", "无结果");
        btResultMap.put("2", "走水");
        btResultMap.put("3", "输");
        btResultMap.put("4", "赢");
        btResultMap.put("5", "赢半");
        btResultMap.put("6", "输半");
        btResultMap.put("7", "玩法取消");
        btResultMap.put("8", "赛事延期");
        btResultMap.put("11", "比赛延迟");
        btResultMap.put("12", "比赛中断");
        btResultMap.put("13", "未知");
        btResultMap.put("15", "比赛放弃");
        btResultMap.put("16", "异常盘口");
    }

    public BtResultOptionPm(BtRecordRsp.RecordsBean.DetailBean detailBean) {
        this.detailBean = detailBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    @Override
    public String getLeagueName() {
        return detailBean.matchName;
    }

    @Override
    public long getMatchTime() {
        if(detailBean == null || detailBean.beginTime == null){
            return 0;
        }
        return Long.valueOf(detailBean.beginTime);
    }

    @Override
    public String getOptionName() {
        return detailBean.marketValue;
    }

    @Override
    public String getPlayType() {
        return detailBean.playName;
    }

    @Override
    public String getScore() {
        if(detailBean == null || TextUtils.isEmpty(detailBean.scoreBenchmark)){
            return "";
        }
        return detailBean.scoreBenchmark;
    }

    @Override
    public String getOdd() {
        return "@" + detailBean.oddFinally;
    }

    @Override
    public String getTeamName() {
        return detailBean.matchInfo;
    }
    @Override
    public String getBtResult() {
        return btResultMap.get(String.valueOf(detailBean.betResult));
    }

    @Override
    public String getFullScore() {
        if(detailBean == null || TextUtils.isEmpty(detailBean.settleScore)){
            return "";
        }
        return detailBean.settleScore;
    }

    @Override
    public boolean isSettled() {
        return detailBean.betStatus == 1;
    }

    @Override
    public int getResultColor() {

        //投注项结算结果 0：无结果，2：走水，3：输，4：赢，5：赢一半， 6：输一半，7：赛事取消，8：赛事延期， 11：比赛延迟，12：比赛中断，13：未知， 15：比赛放弃，16：异常盘口

        if (detailBean.betResult == 4 || detailBean.betResult == 5) {
            return R.color.bt_color_option_win;
        } else if (detailBean.betResult == 3 || detailBean.betResult == 6) {
            return R.color.bt_color_option_lose;
        } else {
            return R.color.bt_color_option_draw;
        }
    }
}
