package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;

import java.util.List;

import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级契约列表数据
 */
public class GameSubordinateagrtModel extends BindModel {

    private String sname = "未创建";
    private String status = "-1";
    private int statusColor = R.color.clr_txt_rebateagrt_default;

    private String userName;
    private String userID;
    private String signTime;
    private String effectDate;
    private String ruleRatio;
    private String createTime;
    //场馆类型
    private RebateAreegmentTypeEnum typeEnum;
    //规则集
    private List<GameSubordinateAgrteResponse.DataDTO.RuleDTO> rules = null;
    private Consumer<String> ratioCallback = null;

    public void setRatioCallback(Consumer<String> ratioCallback) {
        this.ratioCallback = ratioCallback;
    }

    /**
     * 工资比例详情
     */
    public void clickRatio() {
        if (ratioCallback != null && rules != null) {
            StringBuilder sb = new StringBuilder();
            switch (typeEnum) {
                case USER:
                case DAYREBATE:
                    for (int i = 0; i < rules.size(); i++) {
                        GameSubordinateAgrteResponse.DataDTO.RuleDTO ruleDTO = rules.get(i);
                        if (i > 0) {
                            sb.append(System.lineSeparator());
                        }
                        sb.append(ruleDTO.getRatio()).append("元/万");
                    }
                    break;
                default:
                    for (int i = 0; i < rules.size(); i++) {
                        GameSubordinateAgrteResponse.DataDTO.RuleDTO ruleDTO = rules.get(i);
                        if (i > 0) {
                            sb.append(System.lineSeparator());
                        }
                        sb.append(ruleDTO.getRatio()).append("%");
                    }
                    break;
            }
            try {
                ratioCallback.accept(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getStatusColor() {
        return BaseApplication.getInstance().getResources().getColor(statusColor);
    }

    public String getCheckString() {
        //如果是未创建状态则提示创建
        if (status.equals("-1")) {
            return BaseApplication.getInstance().getString(R.string.txt_create);
        } else {
            return BaseApplication.getInstance().getString(R.string.txt_view);
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null) {
            this.status = status;
            switch (status) {
                case "1": //1-已签订
                    statusColor = R.color.clr_txt_rebateagrt_profit;
                    break;
                case "4": //4-已终止
                    statusColor = R.color.clr_txt_rebateagrt_default;
                    break;
                case "8": //8-已更新
                    statusColor = R.color.clr_txt_rebateagrt_profit;
                    break;
                default:
                    statusColor = R.color.clr_txt_rebateagrt_default;
                    break;
            }
        }
    }

    public void setTypeEnum(RebateAreegmentTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public void setRules(List<GameSubordinateAgrteResponse.DataDTO.RuleDTO> rules) {
        //设置显示一条规则提示
        GameSubordinateAgrteResponse.DataDTO.RuleDTO ruleDTO = rules.get(0);
        switch (typeEnum) {
            case USER:
                ruleRatio = ruleDTO.getRatio() + "元/千";
                break;
            case DAYREBATE:
                ruleRatio = ruleDTO.getRatio() + "元/千";
                break;
            default:
                ruleRatio = ruleDTO.getRatio() + "%";
                break;
        }
        this.rules = rules;
    }

    public String getRuleRatio() {
        return ruleRatio;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
