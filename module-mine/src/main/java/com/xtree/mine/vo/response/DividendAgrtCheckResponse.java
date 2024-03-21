package com.xtree.mine.vo.response;

import java.util.List;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtCheckResponse {
    private int status;
    private String msg;
    private DataDTO data;
    private String servertime;
    private int ts;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public static class DataDTO {
        private String id;
        private String type;
        private String userid;
        private String username;
        private String is_lvtop;
        private String user_level;
        private String cycle_type;
        private String cycle_date;
        private String member_type;
        private String parentid;
        private String lvtopid;
        private String status;
        private String token;
        private String effect_date;
        private String sign_time;
        private String create_time;
        private String bill_type;
        private String cycle_loss;
        private String rules_type;
        private List<RuleDTO> rule;
        private String peopleType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIs_lvtop() {
            return is_lvtop;
        }

        public void setIs_lvtop(String is_lvtop) {
            this.is_lvtop = is_lvtop;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getCycle_type() {
            return cycle_type;
        }

        public void setCycle_type(String cycle_type) {
            this.cycle_type = cycle_type;
        }

        public String getCycle_date() {
            return cycle_date;
        }

        public void setCycle_date(String cycle_date) {
            this.cycle_date = cycle_date;
        }

        public String getMember_type() {
            return member_type;
        }

        public void setMember_type(String member_type) {
            this.member_type = member_type;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getLvtopid() {
            return lvtopid;
        }

        public void setLvtopid(String lvtopid) {
            this.lvtopid = lvtopid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEffect_date() {
            return effect_date;
        }

        public void setEffect_date(String effect_date) {
            this.effect_date = effect_date;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getBill_type() {
            return bill_type;
        }

        public void setBill_type(String bill_type) {
            this.bill_type = bill_type;
        }

        public String getCycle_loss() {
            return cycle_loss;
        }

        public void setCycle_loss(String cycle_loss) {
            this.cycle_loss = cycle_loss;
        }

        public String getRules_type() {
            return rules_type;
        }

        public void setRules_type(String rules_type) {
            this.rules_type = rules_type;
        }

        public List<RuleDTO> getRule() {
            return rule;
        }

        public void setRule(List<RuleDTO> rule) {
            this.rule = rule;
        }

        public String getPeopleType() {
            return peopleType;
        }

        public void setPeopleType(String peopleType) {
            this.peopleType = peopleType;
        }

        public static class RuleDTO {
            private String level;
            private String profit;
            private String ratio;
            private String type;
            private String people;
            private String net_profit;
            private String day_people;
            private String week_people;
            private String day_sale;

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getRatio() {
                return ratio;
            }

            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPeople() {
                return people;
            }

            public void setPeople(String people) {
                this.people = people;
            }

            public String getNet_profit() {
                return net_profit;
            }

            public void setNet_profit(String net_profit) {
                this.net_profit = net_profit;
            }

            public String getDay_people() {
                return day_people;
            }

            public void setDay_people(String day_people) {
                this.day_people = day_people;
            }

            public String getWeek_people() {
                return week_people;
            }

            public void setWeek_people(String week_people) {
                this.week_people = week_people;
            }

            public String getDay_sale() {
                return day_sale;
            }

            public void setDay_sale(String day_sale) {
                this.day_sale = day_sale;
            }
        }
    }
}
