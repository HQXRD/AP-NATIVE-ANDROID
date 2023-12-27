package com.xtree.bet.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xtree.bet.R;

/**
 * Created by guoshuyu on 2017/4/1.
 * 使用正常播放按键和loading的播放器
 */

public class CustomGSYVideoPlayer extends StandardGSYVideoPlayer {

    public CustomGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomGSYVideoPlayer(Context context) {
        super(context);
    }

    public CustomGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.bt_video_layout_custom;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        ImageView mVolumeButton = (ImageView) findViewById(R.id.iv_volume);
        mVolumeButton.setOnClickListener(v -> {
            if(getGSYVideoManager().isPlaying()) {
                //静音
                GSYVideoManager.instance().setNeedMute(!GSYVideoManager.instance().isNeedMute());
                if(GSYVideoManager.instance().isNeedMute()){
                    mVolumeButton.setImageResource(R.mipmap.bt_icon_voice_open);
                }else{
                    mVolumeButton.setImageResource(R.mipmap.bt_icon_voice_close);
                }
            }
        });
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.mipmap.bt_video_pause);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.mipmap.bt_video_click_play);
            } else {
                imageView.setImageResource(R.mipmap.bt_video_click_play);
            }
        }
    }
}
