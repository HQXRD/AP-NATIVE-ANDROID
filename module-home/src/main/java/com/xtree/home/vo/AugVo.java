package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class AugVo implements Parcelable {

    private String id;
    private String cid;
    private String code;
    private String project_code;
    private String cn_name;
    private String pt_name;
    private String effective_ratio;
    private String flash_url;
    private String one_level;

    @Override
    public String toString() {
        // AugVo{id='979', cid='7', code='675', project_code='ZhaoCaiJinBaoBT', cn_name='招財進寶',
        // pt_name='Zhao Cai Jin Bao ITP AUG', effective_ratio='100.0', flash_url='/', one_level='老虎机'}
        return "AugVo{" +
                "id='" + id + '\'' +
                ", cid='" + cid + '\'' +
                ", code='" + code + '\'' +
                ", project_code='" + project_code + '\'' +
                ", cn_name='" + cn_name + '\'' +
                ", pt_name='" + pt_name + '\'' +
                ", effective_ratio='" + effective_ratio + '\'' +
                ", flash_url='" + flash_url + '\'' +
                ", one_level='" + one_level + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public String getPt_name() {
        return pt_name;
    }

    public void setPt_name(String pt_name) {
        this.pt_name = pt_name;
    }

    public String getEffective_ratio() {
        return effective_ratio;
    }

    public void setEffective_ratio(String effective_ratio) {
        this.effective_ratio = effective_ratio;
    }

    public String getFlash_url() {
        return flash_url;
    }

    public void setFlash_url(String flash_url) {
        this.flash_url = flash_url;
    }

    public String getOne_level() {
        return one_level;
    }

    public void setOne_level(String one_level) {
        this.one_level = one_level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.cid);
        dest.writeString(this.code);
        dest.writeString(this.project_code);
        dest.writeString(this.cn_name);
        dest.writeString(this.pt_name);
        dest.writeString(this.effective_ratio);
        dest.writeString(this.flash_url);
        dest.writeString(this.one_level);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.cid = source.readString();
        this.code = source.readString();
        this.project_code = source.readString();
        this.cn_name = source.readString();
        this.pt_name = source.readString();
        this.effective_ratio = source.readString();
        this.flash_url = source.readString();
        this.one_level = source.readString();
    }

    public AugVo() {
    }

    protected AugVo(Parcel in) {
        this.id = in.readString();
        this.cid = in.readString();
        this.code = in.readString();
        this.project_code = in.readString();
        this.cn_name = in.readString();
        this.pt_name = in.readString();
        this.effective_ratio = in.readString();
        this.flash_url = in.readString();
        this.one_level = in.readString();
    }

    public static final Parcelable.Creator<AugVo> CREATOR = new Parcelable.Creator<AugVo>() {
        @Override
        public AugVo createFromParcel(Parcel source) {
            return new AugVo(source);
        }

        @Override
        public AugVo[] newArray(int size) {
            return new AugVo[size];
        }
    };
}
