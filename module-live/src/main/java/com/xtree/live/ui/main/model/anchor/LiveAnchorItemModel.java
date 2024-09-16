package com.xtree.live.ui.main.model.anchor;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/9/11.
 * Describe: 直播TAB列表数据模型
 */
public class LiveAnchorItemModel extends BindModel {
    private String text;
    private String title;//	直播间标题
    private String thumb;//	直播间封面
    private String matchId;//	比赛id
    private String avatar;//	主播头像
    private String userNickname;//	用户昵称
    private String heat;//		主播热度（后台-设置-网站信息-默认热度值 +（主播的v_user_count*v_user_multiple））
    private Integer isLive;//		直播状态：-1创建 0未直播 1正常 2取消，只返回返回状态为1的数据
    private ObservableBoolean playing = new ObservableBoolean(true);// 动画播放中

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public Integer getIsLive() {
        return isLive;
    }

    public void setIsLive(Integer isLive) {
        this.isLive = isLive;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {
        super.onViewAttachedToWindow(bindingViewHolder);
        setPlaying(true);
    }

    public ObservableBoolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing.set(playing);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {
        super.onViewDetachedFromWindow(bindingViewHolder);
        setPlaying(false);
    }
}
