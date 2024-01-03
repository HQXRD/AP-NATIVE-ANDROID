package com.xtree.bet.ui.viewmodel.factory;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.data.BetRepository;
import com.xtree.bet.data.Injection;
import com.xtree.bet.data.PMInjection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by marquis on 2023/11/22.
 */
public class PMAppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile PMAppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final BetRepository mRepository;

    public static PMAppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (PMAppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PMAppViewModelFactory(application, PMInjection.provideHomeRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private PMAppViewModelFactory(Application application, BetRepository repository) {
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
