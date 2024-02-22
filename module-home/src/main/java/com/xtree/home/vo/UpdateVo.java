package com.xtree.home.vo;

import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * 更新
 */
public class UpdateVo extends BaseResponse {
    public String id; // ID
    public String app_name; // app名字
    public String platform; // 平台 android/ios
    public String version_name; // 版本 "1.0.0"
    public long version_code; // Build号 100
    public long version_code_min; // 最低Build号 100
    public String download_url; // 下载地址
    public int type; // 跟新类型  0:弱更, 1:强更, 2:热更
    public String content; // 更新内容
    public String config_url; // Config地址
    public String is_enable; // 是否激活：0未激活 1已激活
    public int interval_duration; // 间隔时间
    public String update_at; // 操作时间

    @Override
    public String toString() {
        return "UpdateVo{" +
                "app_name='" + app_name + '\'' +
                ", platform='" + platform + '\'' +
                ", version_name='" + version_name + '\'' +
                ", version_code=" + version_code +
                ", version_code_min=" + version_code_min +
                ", download_url='" + download_url + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", config_url='" + config_url + '\'' +
                ", is_enable='" + is_enable + '\'' +
                ", interval_duration=" + interval_duration +
                ", update_at='" + update_at + '\'' +
                '}';
    }
}
