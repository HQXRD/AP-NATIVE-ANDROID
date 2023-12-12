package com.xtree.activity.vo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NewVo implements Parcelable {

    public int id; // 233,
    public int activity_class_id; // 24,
    public String class_label; // "",
    public int type; // 12,
    public int style; // 0,
    public String title; // "双12收官之战 放肆狂欢",
    public String url; // "/activity/233",
    public String start_at; // "2023-12-08",
    public String end_at; // "2023-12-12",
    public boolean is_recommended; // false,
    public int receive_type; // 5,
    public String image; // "https://jxpicture.julaohuivip.com/2023/12/08/s12_web_zty0351.jpg",
    public String h5_image; // "https://jxpicture.julaohuivip.com/2023/12/08/s12_h5_yhlb0351.jpg",
    public String h5_detail_image; // "https://jxpicture.julaohuivip.com/2023/12/08/s12_h5_zty0351.jpg",
    public String category; // "热门",
    public int category_order; // 2,
    public String icon_url; // "https://jxpicture.julaohuivip.com/2022/12/21/remen.svg",
    public int apply_btn; // 0,
    public int receive_btn; // 1,
    public String active_cover_image; // "https://jxpicture.julaohuivip.com/2023/12/08/s12_web_yhlb0351.jpg"

    protected NewVo(Parcel in) {
        id = in.readInt();
        activity_class_id = in.readInt();
        class_label = in.readString();
        type = in.readInt();
        style = in.readInt();
        title = in.readString();
        url = in.readString();
        start_at = in.readString();
        end_at = in.readString();
        is_recommended = in.readByte() != 0;
        receive_type = in.readInt();
        image = in.readString();
        h5_image = in.readString();
        h5_detail_image = in.readString();
        category = in.readString();
        category_order = in.readInt();
        icon_url = in.readString();
        apply_btn = in.readInt();
        receive_btn = in.readInt();
        active_cover_image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(activity_class_id);
        dest.writeString(class_label);
        dest.writeInt(type);
        dest.writeInt(style);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(start_at);
        dest.writeString(end_at);
        dest.writeByte((byte) (is_recommended ? 1 : 0));
        dest.writeInt(receive_type);
        dest.writeString(image);
        dest.writeString(h5_image);
        dest.writeString(h5_detail_image);
        dest.writeString(category);
        dest.writeInt(category_order);
        dest.writeString(icon_url);
        dest.writeInt(apply_btn);
        dest.writeInt(receive_btn);
        dest.writeString(active_cover_image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewVo> CREATOR = new Creator<NewVo>() {
        @Override
        public NewVo createFromParcel(Parcel in) {
            return new NewVo(in);
        }

        @Override
        public NewVo[] newArray(int size) {
            return new NewVo[size];
        }
    };

    @Override
    public String toString() {
        return "NewVo {" +
                "id=" + id +
                ", category_order=" + category_order +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", activity_class_id=" + activity_class_id +
                ", class_label='" + class_label + '\'' +
                ", type=" + type +
                ", style=" + style +
                ", url='" + url + '\'' +
                ", start_at='" + start_at + '\'' +
                ", end_at='" + end_at + '\'' +
                ", is_recommended=" + is_recommended +
                ", receive_type=" + receive_type +
                ", image='" + image + '\'' +
                ", h5_image='" + h5_image + '\'' +
                ", h5_detail_image='" + h5_detail_image + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", apply_btn=" + apply_btn +
                ", receive_btn=" + receive_btn +
                ", active_cover_image='" + active_cover_image + '\'' +
                '}';
    }

}
