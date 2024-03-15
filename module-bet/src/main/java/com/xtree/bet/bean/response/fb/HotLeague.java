package com.xtree.bet.bean.response.fb;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class HotLeague implements BaseBean {
    public List<Long> leagueid;
    public String code;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.leagueid);
        dest.writeString(this.code);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.leagueid = new ArrayList<Long>();
        source.readList(this.leagueid, Long.class.getClassLoader());
        this.code = source.readString();
        this.name = source.readString();
    }

    public HotLeague() {
    }

    protected HotLeague(Parcel in) {
        this.leagueid = new ArrayList<Long>();
        in.readList(this.leagueid, Long.class.getClassLoader());
        this.code = in.readString();
        this.name = in.readString();
    }

    public static final Creator<HotLeague> CREATOR = new Creator<HotLeague>() {
        @Override
        public HotLeague createFromParcel(Parcel source) {
            return new HotLeague(source);
        }

        @Override
        public HotLeague[] newArray(int size) {
            return new HotLeague[size];
        }
    };
}
