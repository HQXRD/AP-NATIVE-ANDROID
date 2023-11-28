package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import com.xtree.mine.data.LoginRepository;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;

public class LoginViewModel extends BaseViewModel<LoginRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();
    public LoginViewModel(@NonNull Application application, LoginRepository repository) {
        super(application, repository);
    }

}
