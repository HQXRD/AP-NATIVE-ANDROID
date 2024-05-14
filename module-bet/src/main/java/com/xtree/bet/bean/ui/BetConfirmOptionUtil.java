package com.xtree.bet.bean.ui;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;

import android.text.TextUtils;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BetConfirmOptionUtil {
    public static BetConfirmOption getInstance(Match match, PlayType playType, OptionList optionList, Option option){
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(platform, PLATFORM_PM)) {
            return new BetConfirmOptionFb(match, playType, optionList, option);
        }else {
            return new BetConfirmOptionPm(match, playType,optionList,option);
        }

    }
}
