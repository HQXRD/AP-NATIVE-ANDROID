package com.xtree.bet;

import android.app.Application;

import com.xtree.base.base.IModuleInit;

import me.xtree.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2018/6/21 0021.
 */

public class BetModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        KLog.e("投注模块初始化 -- onInitAhead");
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        KLog.e("投注模块初始化 -- onInitLow");
        return false;
    }
}
