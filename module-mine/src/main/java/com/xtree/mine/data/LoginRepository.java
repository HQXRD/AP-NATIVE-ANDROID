package com.xtree.mine.data;

import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.LocalDataSource;

import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;

public class LoginRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    @Override
    public Observable<Object> login() {
        return null;
    }

    @Override
    public Observable<BaseResponse<Object>> demoGet() {
        return null;
    }

    @Override
    public Observable<BaseResponse<Object>> demoPost(String catalog) {
        return null;
    }

    @Override
    public void saveUserName(String userName) {

    }

    @Override
    public void savePassword(String password) {

    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
