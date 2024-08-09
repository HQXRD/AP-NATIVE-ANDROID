package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.R;
import com.xtree.mine.vo.MsgInfoVo;
import com.xtree.mine.vo.MsgPersonInfoVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class ListMsgInfoDialog extends BottomPopupView {
    int maxHeight = 40; // 最大高度百分比 10-100
    int level;
    MsgInfoVo msgInfoVo;
    MsgPersonInfoVo msgPersonInfoVo;

    TextView tvwTitle;
    ImageView ivwClose;
    TextView tvwMsgTitle;
    TextView tvwMsgDate;
    TextView tvwMsgContent;
    HashMap<String, Object> map;
    ProfileVo mProfileVo;

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

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

        initView();
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        ivwClose = findViewById(R.id.ivw_close);
        tvwMsgDate = findViewById(R.id.tvw_msg_date);
        tvwMsgTitle = findViewById(R.id.tvw_msg_title);

        tvwMsgContent = findViewById(R.id.tvw_msg_content);

        ivwClose.setOnClickListener(v -> dismiss());

        if (msgInfoVo != null) {
            tvwTitle.setText(msgInfoVo.title);
            tvwMsgDate.setText(msgInfoVo.created_at);
            tvwMsgTitle.setText(msgInfoVo.title);
            String txt = msgInfoVo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);

            tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (msgPersonInfoVo != null) {
            tvwTitle.setText(msgPersonInfoVo.title);
            tvwMsgDate.setText(msgPersonInfoVo.sent_at);
            tvwMsgTitle.setText(msgPersonInfoVo.title);
            CfLog.i(msgPersonInfoVo.content);
            String txt = msgPersonInfoVo.content.replace("<\\ span><\\ div><br\\/>", "<\\ div>");
            txt = txt.replace("<br/>", "");
            CfLog.i(txt);

            try {
                map = new Gson().fromJson(txt, new TypeToken<HashMap<String, Object>>() {
                }.getType());
                tvwMsgContent.setText(setMessageContent(map));
            } catch (JsonSyntaxException e) {
                CfLog.e(e.getMessage());
                tvwMsgContent.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
            }
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

    public String setMessageContent(HashMap<String, Object> map) {
        StringBuilder content = new StringBuilder();
        content.append("尊敬的用户：").append("\n");
        content.append(map.get("status").equals("8") ? "您的契约" : "已为您创建契约").append("\n\n");
        content.append("生效日期：").append(map.get("effect_date")).append("\n");
        content.append("契约内容").append("\n");

        List<String> unit;

        if (mProfileVo.frequency == 0) {
            unit = Arrays.asList("每日", "日薪");
        } else {
            unit = Arrays.asList("每小时", "时薪");
        }

        List<HashMap<String, Object>> ruleRldyjttList = new ArrayList<>();
        List<HashMap<String, Object>> ruleList = new ArrayList<>();

        if (map.get("rule_rldyjtt") != null) {
            String ruleRldyjtt = map.get("rule_rldyjtt").toString();
            if (ruleRldyjtt != null && !ruleRldyjtt.isEmpty()) {
                ruleRldyjttList = new Gson().fromJson(ruleRldyjtt, new TypeToken<List<HashMap<String, Object>>>() {
                }.getType());
            }
        }
        if (map.get("rule") != null) {
            String rule = map.get("rule").toString();
            if (rule != null && !rule.isEmpty()) {
                ruleList = new Gson().fromJson(rule, new TypeToken<List<HashMap<String, Object>>>() {
                }.getType());
            }
        }

        if (msgPersonInfoVo.type.equals("2") && !ruleRldyjttList.isEmpty()) {
            for (HashMap<String, Object> hashMap : ruleRldyjttList) {
                content.append("日量阶梯").append("\n");
                content.append("日投注额≥").append(hashMap.get("betbyday"))
                        .append("元，活跃人数≥").append(hashMap.get("activePeople"))
                        .append("，日薪").append(hashMap.get("wagesRatio")).append("元/万");
            }
        } else {
            if (msgPersonInfoVo.type.equals("1") || msgPersonInfoVo.type.equals("2")) {
                for (HashMap<String, Object> hashMap : ruleList) {
                    level = new Double((double) hashMap.get("level")).intValue();
                    content.append("规则")
                            .append(level).append(":")
                            .append("投注额≥").append(hashMap.get("min_bet"))
                            .append("元，且活跃玩家人数≥").append(hashMap.get("min_player")).append("，")
                            .append(unit.get(1)).append(hashMap.get("ratio")).append("元/万").append("\n");
                }
            } else if (msgPersonInfoVo.type.equals("3") || msgPersonInfoVo.type.equals("4") || msgPersonInfoVo.type.equals("6") || msgPersonInfoVo.type.equals("7") || msgPersonInfoVo.type.equals("9")) {
                for (HashMap<String, Object> hashMap : ruleList) {
                    level = new Double((double) hashMap.get("level")).intValue();
                    content.append("规则")
                            .append(level).append(":")
                            .append(unit.get(0))
                            .append("日有效投注额≥").append(hashMap.get("min_bet"))
                            .append("元，且活跃玩家人数≥").append(hashMap.get("min_player"));
                }
            } else if (msgPersonInfoVo.type.equals("5")) {
                for (HashMap<String, Object> hashMap : ruleList) {
                    level = new Double((double) hashMap.get("level")).intValue();
                    content.append("规则").append(level).append("\n");
                    content.append("连续亏损周期≥").append(hashMap.get("lose_streak"))
                            .append("周期活跃人数≥").append(hashMap.get("people"))
                            .append("且周期累计亏损额≥").append(hashMap.get("profit").equals("null") ? 0 : hashMap.get("profit"))
                            .append("元，分红").append(hashMap.get("ratio")).append("%").append("\n");
                }
            } else if (msgPersonInfoVo.type.equals("11")) {
                for (HashMap<String, Object> hashMap : ruleList) {
                    level = new Double((double) hashMap.get("level")).intValue();
                    content.append("方案").append(level).append("\n");
                    content.append("周期累计投注额≥").append(hashMap.get("cycle_stake_amount"))
                            .append("元,周期累计亏损额≥").append(hashMap.get("loss_amount"))
                            .append("元,活跃代理≥").append(hashMap.get("people"))
                            .append("人，分红").append(hashMap.get("ratio")).append("%<").append("\n");
                }
            } else if (msgPersonInfoVo.type.equals("20")) {
                for (HashMap<String, Object> hashMap : ruleList) {
                    level = new Double((double) hashMap.get("level")).intValue();
                    content.append("方案").append(level + 1).append("\n");
                    content.append("日投注额≥").append(hashMap.get("bet"))
                            .append("元，日活跃人数≥").append(hashMap.get("people"))
                            .append("人，日亏损额≥").append(hashMap.get("loss_amount"))
                            .append("元，分红：").append(hashMap.get("ratio")).append(hashMap.get("type").equals(0) ? "%" : "元").append("\n");
                }
            }
        }

        return content.toString();
    }
}