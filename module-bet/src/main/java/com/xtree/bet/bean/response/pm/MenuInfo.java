package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事统计
 */
public class MenuInfo implements BaseBean {
    /**
     * 赛事个数
     */
    public int count;
    /**
     * 备用字段1，在二级菜单中，是球种ID
     */
    public String field1;
    /**
     * 是否主菜单显示
     */
    public String field2;
    /**
     * 菜单等级
     */
    public int grade;
    /**
     * 菜单id
     */
    public int menuId;
    /**
     * 菜单名称
     */
    public String menuName;
    /**
     * 菜单类型：早盘/串关/滚球/今日等，参考附录：菜单类型枚举
     */
    public int menuType;
    /**
     * 父节点id
     */
    public int parentId;
    /**
     * 当为串关或早盘时该对象有值，里面对象属性与subList一致
     */
    public List<MenuInfo> topMenuList = new ArrayList<>();
    /**
     * 子菜单集合，里面对象属性与subList一致
     */
    public List<MenuInfo> subList = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.field1);
        dest.writeString(this.field2);
        dest.writeInt(this.grade);
        dest.writeInt(this.menuId);
        dest.writeString(this.menuName);
        dest.writeInt(this.menuType);
        dest.writeInt(this.parentId);
        dest.writeTypedList(this.topMenuList);
        dest.writeTypedList(this.subList);
    }

    public void readFromParcel(Parcel source) {
        this.count = source.readInt();
        this.field1 = source.readString();
        this.field2 = source.readString();
        this.grade = source.readInt();
        this.menuId = source.readInt();
        this.menuName = source.readString();
        this.menuType = source.readInt();
        this.parentId = source.readInt();
        this.topMenuList = source.createTypedArrayList(MenuInfo.CREATOR);
        this.subList = source.createTypedArrayList(MenuInfo.CREATOR);
    }

    public MenuInfo() {
    }

    protected MenuInfo(Parcel in) {
        this.count = in.readInt();
        this.field1 = in.readString();
        this.field2 = in.readString();
        this.grade = in.readInt();
        this.menuId = in.readInt();
        this.menuName = in.readString();
        this.menuType = in.readInt();
        this.parentId = in.readInt();
        this.topMenuList = in.createTypedArrayList(MenuInfo.CREATOR);
        this.subList = in.createTypedArrayList(MenuInfo.CREATOR);
    }

    public static final Creator<MenuInfo> CREATOR = new Creator<MenuInfo>() {
        @Override
        public MenuInfo createFromParcel(Parcel source) {
            return new MenuInfo(source);
        }

        @Override
        public MenuInfo[] newArray(int size) {
            return new MenuInfo[size];
        }
    };
}
