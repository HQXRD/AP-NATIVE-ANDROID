package com.xtree.component;

import com.bumptech.glide.Glide;
import com.xtree.base.config.ModuleLifecycleConfig;
import com.xtree.base.utils.TagUtils;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by goldze on 2018/6/21 0021.
 */

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
        TagUtils.initDeviceId(this);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
