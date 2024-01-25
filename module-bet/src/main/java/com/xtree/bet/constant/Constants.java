package com.xtree.bet.constant;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FB;

import android.text.TextUtils;

import me.xtree.mvvmhabit.utils.SPUtils;

public class Constants {

    public static int[] SPORT_ICON;

    public static int[] SPORT_ICON_NOMAL = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_zq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_lq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_snk_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_ymq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_mszq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_iceq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_qj_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_sq_selector};

    public static int[] SPORT_ICON_LIVE = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_live_all_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_zq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_lq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_snk_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_ymq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_mszq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_iceq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_qj_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_sq_selector};

    public static int[] SPORT_ICON_TODAY_CG = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_hot_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_zq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_lq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_snk_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_ymq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_mszq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_iceq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_qj_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_sq_selector};

    /**
     * 比赛详情页顶部背景图
     */
    public static int[] DETAIL_BG_SPORT_ICON = new int[]{
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_lq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_wq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_snk_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_pq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_ymq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bbq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_default_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_pq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_default_top};

    public static String getScoreType(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
            return String.valueOf(FBConstants.SCORE_TYPE_SCORE);
        }else {
            return PMConstants.SCORE_TYPE_SCORE;
        }
    }

}
