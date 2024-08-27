package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class FBAnnouncementInfo implements BaseBean {

    public Integer current;
    public Integer size;
    public Integer total;
    public Integer totalType;
    public List<RecordsDTO> records;

    public static class RecordsDTO {
        //公告ID
        public String id;
        //	公告标题
        public String ti;
        //公告内容
        public String co;
        //公告发布时间
        public String pt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.current);
        dest.writeValue(this.size);
        dest.writeValue(this.total);
        dest.writeValue(this.totalType);
        dest.writeList(this.records);
    }

    public void readFromParcel(Parcel source) {
        this.current = (Integer) source.readValue(Integer.class.getClassLoader());
        this.size = (Integer) source.readValue(Integer.class.getClassLoader());
        this.total = (Integer) source.readValue(Integer.class.getClassLoader());
        this.totalType = (Integer) source.readValue(Integer.class.getClassLoader());
        this.records = new ArrayList<RecordsDTO>();
        source.readList(this.records, RecordsDTO.class.getClassLoader());
    }

    public FBAnnouncementInfo() {
    }

    protected FBAnnouncementInfo(Parcel in) {
        this.current = (Integer) in.readValue(Integer.class.getClassLoader());
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.records = new ArrayList<RecordsDTO>();
        in.readList(this.records, RecordsDTO.class.getClassLoader());
    }

    public static final Creator<FBAnnouncementInfo> CREATOR = new Creator<FBAnnouncementInfo>() {
        @Override
        public FBAnnouncementInfo createFromParcel(Parcel source) {
            return new FBAnnouncementInfo(source);
        }

        @Override
        public FBAnnouncementInfo[] newArray(int size) {
            return new FBAnnouncementInfo[size];
        }
    };
}
