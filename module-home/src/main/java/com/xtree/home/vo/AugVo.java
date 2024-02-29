package com.xtree.home.vo;

public class AugVo {

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

}
