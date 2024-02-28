package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.vo.MsgInfoVo;
import com.xtree.mine.vo.MsgPersonInfoVo;

public class ListMsgInfoDialog extends BottomPopupView {
    int maxHeight = 40; // 最大高度百分比 10-100
    MsgInfoVo msgInfoVo;
    MsgPersonInfoVo msgPersonInfoVo;

    TextView tvwTitle;
    ImageView ivwClose;
    TextView tvwMsgTitle;
    TextView tvwMsgContent;

    public ListMsgInfoDialog(@NonNull Context context) {
        super(context);
    }

    public ListMsgInfoDialog(@NonNull Context context, MsgInfoVo msgInfoVo, MsgPersonInfoVo msgPersonInfoVo) {
        super(context);
        this.msgInfoVo = msgInfoVo;
        this.msgPersonInfoVo = msgPersonInfoVo;
    }

    /**
     * @param context   Context
     * @param maxHeight 最大高度, 占屏幕高度的百分比 (推荐 30-90)
     */
    public ListMsgInfoDialog(@NonNull Context context, MsgInfoVo msgInfoVo, MsgPersonInfoVo msgPersonInfoVo, int maxHeight) {
        super(context);
        this.msgInfoVo = msgInfoVo;
        this.msgPersonInfoVo = msgPersonInfoVo;
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        ivwClose = findViewById(R.id.ivw_close);
        tvwMsgTitle = findViewById(R.id.tvw_msg_title);
        tvwMsgContent = findViewById(R.id.tvw_msg_content);

        ivwClose.setOnClickListener(v -> dismiss());

        if (msgInfoVo != null) {
            tvwTitle.setText(msgInfoVo.title);
            tvwMsgTitle.setText(msgInfoVo.title);
            String txt = msgInfoVo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);
            tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (msgPersonInfoVo != null) {
            tvwTitle.setText(msgPersonInfoVo.title);
            tvwMsgTitle.setText(msgPersonInfoVo.title);
            CfLog.i(msgInfoVo.content);
            String txt = msgInfoVo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);
            tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_msg;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 40;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }
}