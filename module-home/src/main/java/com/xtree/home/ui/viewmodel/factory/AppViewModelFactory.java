package com.xtree.home.ui.viewmodel.factory;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.base.base.IModuleInit;
import com.xtree.home.data.HomeRepository;
import com.xtree.home.data.Injection;
import com.xtree.home.ui.viewmodel.HomeViewModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.xtree.mvvmhabit.base.BaseModel;

/**
 * Created by marquis on 2023/11/22.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final BaseModel mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideHomeRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, HomeRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(modelClass.getName());
            Constructor constructor = clazz.getConstructor(Application.class, HomeRepository.class);
            return (T) constructor.newInstance(mApplication, mRepository);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            return null;
        }
    }
}
