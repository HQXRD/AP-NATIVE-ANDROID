package com.xtree.bet.constant;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.HotLeague;

import java.util.List;
import java.util.Map;

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
     * 获取比分类型
     * @return
     */
    public static String getScoreType(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return String.valueOf(FBConstants.SCORE_TYPE_SCORE);
        }else {
            return PMConstants.SCORE_TYPE_SCORE;
        }
    }

    /**
     * 获取红牌比分类型
     * @return
     */
    public static String getRedCardType(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return String.valueOf(FBConstants.SCORE_TYPE_RED_CARD);
        }else {
            return PMConstants.SCORE_TYPE_RED_CARD;
        }
    }

    /**
     * 获取黄牌比分类型
     * @return
     */
    public static String getYellowCardType(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return String.valueOf(FBConstants.SCORE_TYPE_YELLOW_CARD);
        }else {
            return PMConstants.SCORE_TYPE_YELLOW_CARD;
        }
    }

    public static int getBgMatchDetailTop(String sportId){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.getBgMatchDetailTop(sportId);
        }else {
            return PMConstants.getBgMatchDetailTop(sportId);
        }
    }

    /**
     * 获取足球的体育 ID
     * @return
     */
    public static String getFbSportId(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.SPORT_ID_FB;
        }else {
            return PMConstants.SPORT_ID_FB;
        }
    }

    /**
     * 获取篮球的体育 ID
     * @return
     */
    public static String getBsbSportId(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.SPORT_ID_BSB;
        }else {
            return PMConstants.SPORT_ID_BSB;
        }
    }

    /**
     * 获取热门联赛icon
     * @return
     */
    public static int getHotLeagueIcon(String code){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.getHotLeagueIcon(code);
        }else {
            return PMConstants.getHotLeagueIcon(code);
        }
    }

    /**
     * 获取足球热门联赛数据
     * @return
     */
    public static List<HotLeague> getHotFootBallLeagueTopList(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.getHotFootBallLeagueTopList();
        }else {
            return PMConstants.getHotFootBallLeagueTopList();
        }
    }

    /**
     * 获取足球热门联赛数据
     * @return
     */
    public static List<HotLeague> getHotBasketBallLeagueTopList(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            return FBConstants.getHotBasketFootBallLeagueTopList();
        }else {
            return PMConstants.getHotBasketFootBallLeagueTopList();
        }
    }

}
