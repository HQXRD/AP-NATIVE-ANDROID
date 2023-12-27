package com.xtree.component;

import androidx.annotation.Nullable;
import androidx.media3.datasource.DataSink;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.source.MediaSource;

import com.xtree.base.config.ModuleLifecycleConfig;
import com.xtree.base.utils.TagUtils;
import com.xtree.component.exosource.GSYExoHttpDataSourceFactory;

import java.io.File;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseApplication;
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener;
import tv.danmaku.ijk.media.exo2.ExoSourceManager;

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
    }
}
