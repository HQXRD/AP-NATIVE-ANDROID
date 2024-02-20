package com.xtree.bet.bean.ui;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.BtResultOptionInfo;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ParcelCreator")
public class BtResultOptionFb implements BtResultOption {
    private BtResultOptionInfo btResultOptionInfo;

    private static Map<String, String> btResultMap = new HashMap<>();

    static {

        btResultMap.put("0", "未结算");
        btResultMap.put("2", "走水");
        btResultMap.put("3", "全输");
        btResultMap.put("4", "全赢");
        btResultMap.put("5", "赢半");
        btResultMap.put("6", "输半");
        btResultMap.put("7", "玩法取消");
    }

    public BtResultOptionFb(BtResultOptionInfo btResultOptionInfo) {
        this.btResultOptionInfo = btResultOptionInfo;
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
        return btResultOptionInfo.ln;
    }

    @Override
    public long getMatchTime() {
        return btResultOptionInfo.bt;
    }

    @Override
    public String getOptionName() {
        return btResultOptionInfo.onm;
    }

    @Override
    public String getPlayType() {
        return btResultOptionInfo.mgn;
    }

    @Override
    public String getScore() {
        if(btResultOptionInfo == null || TextUtils.isEmpty(btResultOptionInfo.bsc)){
            return "";
        }
        if (btResultOptionInfo.bsc.startsWith("S:")){
            return btResultOptionInfo.bsc.substring(2).trim();
        }
        return btResultOptionInfo.bsc;
    }

    @Override
    public String getOdd() {
        return "@" + btResultOptionInfo.bo;
    }

    @Override
    public String getTeamName() {
        return btResultOptionInfo.mn;
    }
    @Override
    public String getBtResult() {
        return btResultMap.get(String.valueOf(btResultOptionInfo.sr));
    }

    @Override
    public String getFullScore() {
        return btResultOptionInfo.rs;
    }

    @Override
    public boolean isSettled() {
        return btResultOptionInfo.sr != 0;
    }

    @Override
    public int getResultColor() {
        if (btResultOptionInfo.sr == 4 || btResultOptionInfo.sr == 5) {
            return R.color.bt_color_option_win;
        } else if (btResultOptionInfo.sr == 3 || btResultOptionInfo.sr == 6) {
            return R.color.bt_color_option_lose;
        } else {
            return R.color.bt_color_option_draw;
        }
    }
}
