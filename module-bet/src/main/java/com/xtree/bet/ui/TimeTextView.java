package com.xtree.bet.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.ui.Match;

public class TimeTextView extends AppCompatTextView {
    private Match match;
    private int times;
    private CountDownTimer countDownTimer;

    Runnable runnable = new Runnable() {

        public void run() {
            times += 1;
            setText(match.getStage() + " " + TimeUtils.sToMs(times));
            Log.i("上班1", match.getId() + "  time:  " + TimeUtils.sToMs(times));
            postDelayed(this, 1000);
        }
    };


    public TimeTextView(@NonNull Context context) {
        super(context);
    }

    public TimeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMatch(Match match) {
        this.match = match;
        times = match.getTimeS();
        countDownTimer = new CountDownTimer(900000000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                times += 1;
                //setText(match.getStage() + " " + TimeUtils.sToMs(times));
                Log.i("上班1", match.getId() + "-------" + match.getStage() + " " + TimeUtils.sToMs(times));
                //countdownText.setText("剩余时间：" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                //countdownText.setText("倒计时完成！");
            }
        };

        // 开始倒计时
        countDownTimer.start();
    }
}
