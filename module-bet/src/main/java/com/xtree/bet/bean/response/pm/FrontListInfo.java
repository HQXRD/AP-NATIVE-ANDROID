package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class FrontListInfo implements BaseBean {

    /**
     * "nb": [{
     * "context": "篮球赛事：2022/05/07 15:05:00日本篮球B1联赛(东京电击 VS 宇都宫皇者)-因赛果更正：第4节独赢(东京电击)，所有相关受影响注单将会进行重新结算，如有不便之处，敬请见谅。",
     * "id": "76686",
     * "isShuf": 1,
     * "isTop": 0,
     * "noticeType": 2,
     * "noticeTypeName": "篮球赛事",
     * "sendTime": "05/07/22",
     * "sendTimeOther": "1651913591194",
     * "title": "比赛延期"
     * }, {
     * "context": "篮球赛事：2022/05/07 15:05:00日本篮球B1联赛(东京电击 VS 宇都宫皇者)-因赛果更正：第4节让分(东京电击)，所有相关受影响注单将会进行重新结算，如有不便之处，敬请见谅。",
     * "id": "76685",
     * "isShuf": 1,
     * "isTop": 0,
     * "noticeType": 2,
     * "noticeTypeName": "篮球赛事",
     * "sendTime": "05/07/22",
     * "sendTimeOther": "1651913548893",
     * "title": "比赛延期"
     * }],
     * "nen": "CS:GO赛事",
     * "net": 34
     * }, {
     * "mtl": [],
     * "nen": "其他赛事",
     * "net": 60
     * }, {
     * "mtl": [],
     * "nen": "活动公告",
     * "net": 100
     * }]
     */
    public String nen;
    public Integer net;
    public List<NbDTO> nb;

    public class NbDTO {
        public String context;
        public String id;
        public Integer isShuf;
        public Integer isTop;
        public Integer noticeType;
        public String noticeTypeName;
        public String sendTime;
        public String sendTimeOther;
        public String title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nen);
        dest.writeValue(this.net);
        dest.writeList(this.nb);
    }

    public void readFromParcel(Parcel source) {
        this.nen = source.readString();
        this.net = (Integer) source.readValue(Integer.class.getClassLoader());
        this.nb = new ArrayList<NbDTO>();
        source.readList(this.nb, NbDTO.class.getClassLoader());
    }

    public FrontListInfo() {
    }

    protected FrontListInfo(Parcel in) {
        this.nen = in.readString();
        this.net = (Integer) in.readValue(Integer.class.getClassLoader());
        this.nb = new ArrayList<NbDTO>();
        in.readList(this.nb, NbDTO.class.getClassLoader());
    }

    public static final Creator<FrontListInfo> CREATOR = new Creator<FrontListInfo>() {
        @Override
        public FrontListInfo createFromParcel(Parcel source) {
            return new FrontListInfo(source);
        }

        @Override
        public FrontListInfo[] newArray(int size) {
            return new FrontListInfo[size];
        }
    };
}
