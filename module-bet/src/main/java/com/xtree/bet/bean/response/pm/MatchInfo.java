package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo implements BaseBean {
    /**
     * 玩法集id
     */
    public String mcid;
    /**
     * 赛种名称
     */
    public String csna;
    /**
     * 赛种id
     */
    public String csid;
    /**
     * 联赛id
     */
    public String tid;
    /**
     * 联赛名称
     */
    public String tn;
    /**
     * 联赛等级
     */
    public int tlev;
    /**
     * 主队id
     */
    public String mhid;
    /**
     * 赛事id
     */
    public String mid;
    /**
     * 赛事进行时间
     */
    public String mst;
    /**
     * 栏目类型 1：滚球，2：即将开赛，3：今日赛事，4：早盘
     */
    public int mcg;
    /**
     * 比赛阶段，参考附录 赛事阶段比对
     */
    public String mmp;
    /**
     * 视频状态 -1：没有配置视频源 ，0：已配置但不可用，1：已配置可用但暂未播放，2：已配置可用并播放中
     */
    public int mms;
    /**
     * 主队logo图标的url地址，默认取0，双打球队取 0、1元素
     */
    public List<String> mhlu;
    /**
     * 客队logo缩略图的url地址
     */
    public String malut;
    /**
     * 赛事开始时间
     */
    public String mgt;
    /**
     * 客队名称
     */
    public String man;
    /**
     * 客队id
     */
    public String maid;
    /**
     * 当前是第几盘或者第几局
     */
    public String mct;
    /**
     * 主队logo缩略图的url地址
     */
    public String mhlut;
    /**
     * 发球方 主：home，客：away
     */
    public String mat;
    /**
     * 视频直播地址集合
     */
    public List<VideoInfo> vs;
    /**
     * 动画直播地址集合
     */
    public List<String> as = new ArrayList<>();
    /**
     * 比赛是否结束
     */
    public int mo;
    /**
     * 是否支持赛前盘
     */
    public int mp;
    /**
     * 是否支持赛前盘赛事状态，参考附录 赛事状态
     */
    public int ms;
    /**
     * 是否中立场 1：中立场，0：非中立场
     */
    public String mng;
    /**
     * 赛节配置 足球： 0：default（默认90分钟）
     * 2 x 45 minutes，1：2 x 40 minutes，
     * 9：2 x 35 minutes，10：2 x 30 minutes，
     * 11：2 x 25 minutes，
     * 46：2 x 45 minutes with ABBA shootout format
     * 篮球：0： default（默认）
     * 4 x 10 minutes， 7：4 x 12 minutes，
     * 17：2 X 20 minutes 上下半场各20分钟
     */
    public int mle;
    /**
     * 动画状态 -1：没有配置动画源 ，0 ： 已配置但不可用 ，1： 已配置可用但暂未播放，2：已配置可用并播放中
     */
    public int mvs;
    /**
     * 客队logo图标的url地址
     */
    public List<String> malu;
    /**
     * 主队名称
     */
    public String mhn;
    /**
     * 赛制
     */
    public String mfo;
    /**
     * 盘口状态
     */
    public int mhs;
    /**
     * 总局数
     */
    public int mft;
    /**
     * [{S1|21:30}，{S2|21:30}，{S3|21:30}] --比分（比分类型|比分）详情参考比分档案 ：比分定义
     */
    public List<String> msc;
    /**
     * 排序值
     */
    public int orderNo;
    /**
     * 是否杯赛 1：是
     */
    public int isc;
    /**
     * 回合数
     */
    public int lod;
    /**
     * 玩法集合
     */
    public List<PlayTypeInfo> hps = new ArrayList<>();
    /**
     * 获取赛事时长，获取每节比赛的时长
     */
    public String mlet;
    /**
     * 第三方赛事id
     */
    public long srid;
    /**
     * 事件编码
     */
    public String cmec;
    /**
     *赛季id
     */
    public String seid;
    /**
     * 客队名称首字母
     */
    public List<String> frman;
    /**
     *联赛logo
     */
    public String lurl;
    /**
     * 数据源
     */
    public String cds;
    /**
     * 主队名称首字母
     */
    public List<String> frmhn;
    /**
     * 开始结束状态 1：start，0：stop（此字段只适用于特定事件编码），篮球暂停事件编码：time_start
     */
    public int mess;
    /**
     * 联赛是否热门 1：是，0：否
     */
    public int th;
    /**
     * 赛事盘口总数量
     */
    public int mc;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mcid);
        dest.writeString(this.csna);
        dest.writeString(this.csid);
        dest.writeString(this.tid);
        dest.writeString(this.tn);
        dest.writeInt(this.tlev);
        dest.writeString(this.mhid);
        dest.writeString(this.mid);
        dest.writeString(this.mst);
        dest.writeInt(this.mcg);
        dest.writeString(this.mmp);
        dest.writeInt(this.mms);
        dest.writeStringList(this.mhlu);
        dest.writeString(this.malut);
        dest.writeString(this.mgt);
        dest.writeString(this.man);
        dest.writeString(this.maid);
        dest.writeString(this.mct);
        dest.writeString(this.mhlut);
        dest.writeString(this.mat);
        dest.writeInt(this.mo);
        dest.writeInt(this.mp);
        dest.writeInt(this.ms);
        dest.writeString(this.mng);
        dest.writeInt(this.mle);
        dest.writeInt(this.mvs);
        dest.writeStringList(this.malu);
        dest.writeString(this.mhn);
        dest.writeString(this.mfo);
        dest.writeInt(this.mhs);
        dest.writeInt(this.mft);
        dest.writeStringList(this.msc);
        dest.writeTypedList(this.hps);
        dest.writeString(this.mlet);
        dest.writeLong(this.srid);
        dest.writeString(this.cmec);
        dest.writeString(this.seid);
        dest.writeStringList(this.frman);
        dest.writeString(this.lurl);
        dest.writeString(this.cds);
        dest.writeStringList(this.frmhn);
        dest.writeInt(this.mess);
        dest.writeInt(this.th);
        dest.writeInt(this.mc);
    }

    public void readFromParcel(Parcel source) {
        this.mcid = source.readString();
        this.csna = source.readString();
        this.csid = source.readString();
        this.tid = source.readString();
        this.tn = source.readString();
        this.tlev = source.readInt();
        this.mhid = source.readString();
        this.mid = source.readString();
        this.mst = source.readString();
        this.mcg = source.readInt();
        this.mmp = source.readString();
        this.mms = source.readInt();
        this.mhlu = source.createStringArrayList();
        this.malut = source.readString();
        this.mgt = source.readString();
        this.man = source.readString();
        this.maid = source.readString();
        this.mct = source.readString();
        this.mhlut = source.readString();
        this.mat = source.readString();
        this.mo = source.readInt();
        this.mp = source.readInt();
        this.ms = source.readInt();
        this.mng = source.readString();
        this.mle = source.readInt();
        this.mvs = source.readInt();
        this.malu = source.createStringArrayList();
        this.mhn = source.readString();
        this.mfo = source.readString();
        this.mhs = source.readInt();
        this.mft = source.readInt();
        this.msc = source.createStringArrayList();
        this.hps = source.createTypedArrayList(PlayTypeInfo.CREATOR);
        this.mlet = source.readString();
        this.srid = source.readLong();
        this.cmec = source.readString();
        this.seid = source.readString();
        this.frman = source.createStringArrayList();
        this.lurl = source.readString();
        this.cds = source.readString();
        this.frmhn = source.createStringArrayList();
        this.mess = source.readInt();
        this.th = source.readInt();
        this.mc = source.readInt();
    }

    public MatchInfo() {
    }

    protected MatchInfo(Parcel in) {
        this.mcid = in.readString();
        this.csna = in.readString();
        this.csid = in.readString();
        this.tid = in.readString();
        this.tn = in.readString();
        this.tlev = in.readInt();
        this.mhid = in.readString();
        this.mid = in.readString();
        this.mst = in.readString();
        this.mcg = in.readInt();
        this.mmp = in.readString();
        this.mms = in.readInt();
        this.mhlu = in.createStringArrayList();
        this.malut = in.readString();
        this.mgt = in.readString();
        this.man = in.readString();
        this.maid = in.readString();
        this.mct = in.readString();
        this.mhlut = in.readString();
        this.mat = in.readString();
        this.mo = in.readInt();
        this.mp = in.readInt();
        this.ms = in.readInt();
        this.mng = in.readString();
        this.mle = in.readInt();
        this.mvs = in.readInt();
        this.malu = in.createStringArrayList();
        this.mhn = in.readString();
        this.mfo = in.readString();
        this.mhs = in.readInt();
        this.mft = in.readInt();
        this.msc = in.createStringArrayList();
        this.hps = in.createTypedArrayList(PlayTypeInfo.CREATOR);
        this.mlet = in.readString();
        this.srid = in.readLong();
        this.cmec = in.readString();
        this.seid = in.readString();
        this.frman = in.createStringArrayList();
        this.lurl = in.readString();
        this.cds = in.readString();
        this.frmhn = in.createStringArrayList();
        this.mess = in.readInt();
        this.th = in.readInt();
        this.mc = in.readInt();
    }

    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel source) {
            return new MatchInfo(source);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };
}
