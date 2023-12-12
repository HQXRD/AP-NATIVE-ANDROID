package com.xtree.activity.vo;

public class CategoryVo implements Comparable<CategoryVo> {

    public int category_order; // 2,
    public String category; // "热门",

    public CategoryVo() {
    }

    public CategoryVo(int category_order, String category) {
        this.category_order = category_order;
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoryVo {" +
                "category_order=" + category_order +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int compareTo(CategoryVo other) {
        //return 0;

        int result = this.category_order - other.category_order;
        if (result > 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
