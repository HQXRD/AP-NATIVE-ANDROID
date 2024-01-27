package com.xtree.bet.ui.viewmodel.factory;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.data.BetRepository;
import com.xtree.bet.data.Injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis on 2023/11/22.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory FBXC_INSTANCE;
    private static volatile AppViewModelFactory FB_INSTANCE;
    private final Application mApplication;
    private final BetRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        String platform = SPUtils.getInstance().getString("KEY_PLATFORM");
        if (TextUtils.equals("fbxc", platform)) {
            if (FBXC_INSTANCE == null) {
                synchronized (AppViewModelFactory.class) {
                    if (FBXC_INSTANCE == null) {
                        FBXC_INSTANCE = new AppViewModelFactory(application, Injection.provideHomeRepository());
                    }
                }
            }
            return FBXC_INSTANCE;
        } else {
            if (FB_INSTANCE == null) {
                synchronized (AppViewModelFactory.class) {
                    if (FB_INSTANCE == null) {
                        FB_INSTANCE = new AppViewModelFactory(application, Injection.provideHomeRepository());
                    }
                }
            }
            return FB_INSTANCE;
        }
    }

    @VisibleForTesting
    public static void destroyInstance() {
        FBXC_INSTANCE = null;
    }

    private AppViewModelFactory(Application application, BetRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(modelClass.getName());
            Constructor constructor = clazz.getConstructor(Application.class, BetRepository.class);
            return (T) constructor.newInstance(mApplication, mRepository);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            return null;
        }
    }
}
