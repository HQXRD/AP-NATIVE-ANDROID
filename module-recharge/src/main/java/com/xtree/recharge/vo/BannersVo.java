package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

//import com.xtree.base.base.BaseBean;

// extends BaseBean
public class BannersVo implements Parcelable {

    public String title; // "虚拟体育 惊喜不断"
    public String ad_id; // "465"
    public String link = ""; // "#/newactivity/detail/225"
    public String start_time; // "2023-11-16"
    public String end_time; // "2023-12-01"
    public String sort; // "2"
    public String platform_id; // "25"
    public String actlink_id; // "0"
    public String position_pop; // "1"
    public String unlogin_pop; // "0"
    public String login_pop; // "0"
    public String pop_image; // "https://jxpicture.julaohuivip.com/2023/11/01/xnty_H5_banner0348.jpg"
    public String ad_position; // "4"
    public String pop_image_type; // "0"
    public String user_pop; // "1"
    public String pop_start_time; // "0000-00-00"
    public String pop_end_time; // "0000-00-00"
    public String design_pop_user; // "0"
    public String h5_target_link; // ""
    public String app_target_link; // ""
    public String picture; // "https://jxpicture.julaohuivip.com/2023/11/01/xnty_H5_banner0348.jpg"

    public BannersVo(String title) {
        this.title = title;
    }

    protected BannersVo(Parcel in) {
        title = in.readString();
        ad_id = in.readString();
        link = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        sort = in.readString();
        platform_id = in.readString();
        actlink_id = in.readString();
        position_pop = in.readString();
        unlogin_pop = in.readString();
        login_pop = in.readString();
        pop_image = in.readString();
        ad_position = in.readString();
        pop_image_type = in.readString();
        user_pop = in.readString();
        pop_start_time = in.readString();
        pop_end_time = in.readString();
        design_pop_user = in.readString();
        h5_target_link = in.readString();
        app_target_link = in.readString();
        picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(ad_id);
        dest.writeString(link);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeString(sort);
        dest.writeString(platform_id);
        dest.writeString(actlink_id);
        dest.writeString(position_pop);
        dest.writeString(unlogin_pop);
        dest.writeString(login_pop);
        dest.writeString(pop_image);
        dest.writeString(ad_position);
        dest.writeString(pop_image_type);
        dest.writeString(user_pop);
        dest.writeString(pop_start_time);
        dest.writeString(pop_end_time);
        dest.writeString(design_pop_user);
        dest.writeString(h5_target_link);
        dest.writeString(app_target_link);
        dest.writeString(picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BannersVo> CREATOR = new Creator<BannersVo>() {
        @Override
        public BannersVo createFromParcel(Parcel in) {
            return new BannersVo(in);
        }

        @Override
        public BannersVo[] newArray(int size) {
            return new BannersVo[size];
        }
    };

    @Override
    public String toString() {
        return "BannersVo{" +
                "title='" + title + '\'' +
                ", ad_id='" + ad_id + '\'' +
                ", link='" + link + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", sort='" + sort + '\'' +
                ", platform_id='" + platform_id + '\'' +
                ", actlink_id='" + actlink_id + '\'' +
                ", position_pop='" + position_pop + '\'' +
                ", unlogin_pop='" + unlogin_pop + '\'' +
                ", login_pop='" + login_pop + '\'' +
                ", pop_image='" + pop_image + '\'' +
                ", ad_position='" + ad_position + '\'' +
                ", pop_image_type='" + pop_image_type + '\'' +
                ", user_pop='" + user_pop + '\'' +
                ", pop_start_time='" + pop_start_time + '\'' +
                ", pop_end_time='" + pop_end_time + '\'' +
                ", design_pop_user='" + design_pop_user + '\'' +
                ", h5_target_link='" + h5_target_link + '\'' +
                ", app_target_link='" + app_target_link + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
