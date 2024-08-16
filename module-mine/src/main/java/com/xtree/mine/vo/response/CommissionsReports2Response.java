package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表查看返回体
 */
public class CommissionsReports2Response {


    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataDTOX data;
    @SerializedName("timestamp")
    private Integer timestamp;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTOX getData() {
        return data;
    }

    public void setData(DataDTOX data) {
        this.data = data;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTOX {
        @SerializedName("search")
        private SearchDTO search;
        @SerializedName("fields")
        private FieldsDTO fields;
        @SerializedName("allCount")
        private AllCountDTO allCount;
        @SerializedName("pageCount")
        private PageCountDTO pageCount;
        @SerializedName("data")
        private DataDTO data;

        public SearchDTO getSearch() {
            return search;
        }

        public void setSearch(SearchDTO search) {
            this.search = search;
        }

        public FieldsDTO getFields() {
            return fields;
        }

        public void setFields(FieldsDTO fields) {
            this.fields = fields;
        }

        public AllCountDTO getAllCount() {
            return allCount;
        }

        public void setAllCount(AllCountDTO allCount) {
            this.allCount = allCount;
        }

        public PageCountDTO getPageCount() {
            return pageCount;
        }

        public void setPageCount(PageCountDTO pageCount) {
            this.pageCount = pageCount;
        }

        public DataDTO getData() {
            return data;
        }

        public void setData(DataDTO data) {
            this.data = data;
        }

        public static class SearchDTO {
            @SerializedName("sort")
            private String sort;
            @SerializedName("start_month")
            private String startMonth;
            @SerializedName("end_month")
            private String endMonth;
            @SerializedName("username")
            private String username;
            @SerializedName("status")
            private String status;
            @SerializedName("page")
            private Integer page;
            @SerializedName("limit")
            private Integer limit;
            @SerializedName("controller")
            private String controller;
            @SerializedName("start_date")
            private String startDate;
            @SerializedName("sTime")
            private Integer sTime;
            @SerializedName("end_date")
            private String endDate;
            @SerializedName("eTime")
            private Integer eTime;
            @SerializedName("first_deposit_start_date")
            private String firstDepositStartDate;
            @SerializedName("first_deposit_end_date")
            private String firstDepositEndDate;
            @SerializedName("register_start_date")
            private String registerStartDate;
            @SerializedName("register_end_date")
            private String registerEndDate;
            @SerializedName("method")
            private Integer method;
            @SerializedName("method_code")
            private String methodCode;
            @SerializedName("data_type")
            private Integer dataType;
            @SerializedName("order")
            private String order;
            @SerializedName("offset")
            private Integer offset;
            @SerializedName("csv")
            private Boolean csv;
            @SerializedName("count")
            private Integer count;

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getStartMonth() {
                return startMonth;
            }

            public void setStartMonth(String startMonth) {
                this.startMonth = startMonth;
            }

            public String getEndMonth() {
                return endMonth;
            }

            public void setEndMonth(String endMonth) {
                this.endMonth = endMonth;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Integer getPage() {
                return page;
            }

            public void setPage(Integer page) {
                this.page = page;
            }

            public Integer getLimit() {
                return limit;
            }

            public void setLimit(Integer limit) {
                this.limit = limit;
            }

            public String getController() {
                return controller;
            }

            public void setController(String controller) {
                this.controller = controller;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public Integer getSTime() {
                return sTime;
            }

            public void setSTime(Integer sTime) {
                this.sTime = sTime;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public Integer getETime() {
                return eTime;
            }

            public void setETime(Integer eTime) {
                this.eTime = eTime;
            }

            public String getFirstDepositStartDate() {
                return firstDepositStartDate;
            }

            public void setFirstDepositStartDate(String firstDepositStartDate) {
                this.firstDepositStartDate = firstDepositStartDate;
            }

            public String getFirstDepositEndDate() {
                return firstDepositEndDate;
            }

            public void setFirstDepositEndDate(String firstDepositEndDate) {
                this.firstDepositEndDate = firstDepositEndDate;
            }

            public String getRegisterStartDate() {
                return registerStartDate;
            }

            public void setRegisterStartDate(String registerStartDate) {
                this.registerStartDate = registerStartDate;
            }

            public String getRegisterEndDate() {
                return registerEndDate;
            }

            public void setRegisterEndDate(String registerEndDate) {
                this.registerEndDate = registerEndDate;
            }

            public Integer getMethod() {
                return method;
            }

            public void setMethod(Integer method) {
                this.method = method;
            }

            public String getMethodCode() {
                return methodCode;
            }

            public void setMethodCode(String methodCode) {
                this.methodCode = methodCode;
            }

            public Integer getDataType() {
                return dataType;
            }

            public void setDataType(Integer dataType) {
                this.dataType = dataType;
            }

            public String getOrder() {
                return order;
            }

            public void setOrder(String order) {
                this.order = order;
            }

            public Integer getOffset() {
                return offset;
            }

            public void setOffset(Integer offset) {
                this.offset = offset;
            }

            public Boolean isCsv() {
                return csv;
            }

            public void setCsv(Boolean csv) {
                this.csv = csv;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }
        }

        public static class FieldsDTO {
            @SerializedName("month")
            private MonthDTO month;
            @SerializedName("username")
            private MonthDTO username;
            @SerializedName("registered_at")
            private MonthDTO registeredAt;
            @SerializedName("c_agency_model")
            private MonthDTO cAgencyModel;
            @SerializedName("plan_type")
            private MonthDTO planType;
            @SerializedName("activity_people")
            private MonthDTO activityPeople;
            @SerializedName("real_bet")
            private MonthDTO realBet;
            @SerializedName("profit")
            private FieldsDTO.ProfitDTO profit;
            @SerializedName("activity")
            private FieldsDTO.ProfitDTO activity;
            @SerializedName("gift")
            private FieldsDTO.ProfitDTO gift;
            @SerializedName("wages")
            private FieldsDTO.ProfitDTO wages;
            @SerializedName("pay_fee")
            private PayFeeDTO payFee;
            @SerializedName("fee")
            private FieldsDTO.ProfitDTO fee;
            @SerializedName("sys_adjust")
            private FieldsDTO.ProfitDTO sysAdjust;
            @SerializedName("deposit_agent_rate")
            private FieldsDTO.ProfitDTO depositAgentRate;
            @SerializedName("withdraw_agent_rate")
            private FieldsDTO.ProfitDTO withdrawAgentRate;
            @SerializedName("income")
            private FieldsDTO.ProfitDTO income;
            @SerializedName("adjust_income")
            private FieldsDTO.ProfitDTO adjustIncome;
            @SerializedName("last_remain")
            private FieldsDTO.ProfitDTO lastRemain;
            @SerializedName("remain")
            private FieldsDTO.ProfitDTO remain;
            @SerializedName("plan_ratio")
            private FieldsDTO.ProfitDTO planRatio;
            @SerializedName("adjust_ratio")
            private FieldsDTO.ProfitDTO adjustRatio;
            @SerializedName("due")
            private FieldsDTO.ProfitDTO due;
            @SerializedName("actual")
            private FieldsDTO.ProfitDTO actual;
            @SerializedName("status")
            private FieldsDTO.ProfitDTO status;
            @SerializedName("sent_by")
            private FieldsDTO.ProfitDTO sentBy;
            @SerializedName("adjust_note")
            private FieldsDTO.ProfitDTO adjustNote;
            @SerializedName("note")
            private FieldsDTO.ProfitDTO note;

            public MonthDTO getMonth() {
                return month;
            }

            public void setMonth(MonthDTO month) {
                this.month = month;
            }

            public MonthDTO getUsername() {
                return username;
            }

            public void setUsername(MonthDTO username) {
                this.username = username;
            }

            public MonthDTO getRegisteredAt() {
                return registeredAt;
            }

            public void setRegisteredAt(MonthDTO registeredAt) {
                this.registeredAt = registeredAt;
            }

            public MonthDTO getCAgencyModel() {
                return cAgencyModel;
            }

            public void setCAgencyModel(MonthDTO cAgencyModel) {
                this.cAgencyModel = cAgencyModel;
            }

            public MonthDTO getPlanType() {
                return planType;
            }

            public void setPlanType(MonthDTO planType) {
                this.planType = planType;
            }

            public MonthDTO getActivityPeople() {
                return activityPeople;
            }

            public void setActivityPeople(MonthDTO activityPeople) {
                this.activityPeople = activityPeople;
            }

            public MonthDTO getRealBet() {
                return realBet;
            }

            public void setRealBet(MonthDTO realBet) {
                this.realBet = realBet;
            }

            private FieldsDTO.ProfitDTO getProfit() {
                return profit;
            }

            public void setProfit(FieldsDTO.ProfitDTO profit) {
                this.profit = profit;
            }

            private FieldsDTO.ProfitDTO getActivity() {
                return activity;
            }

            public void setActivity(FieldsDTO.ProfitDTO activity) {
                this.activity = activity;
            }

            private FieldsDTO.ProfitDTO getGift() {
                return gift;
            }

            public void setGift(FieldsDTO.ProfitDTO gift) {
                this.gift = gift;
            }

            private FieldsDTO.ProfitDTO getWages() {
                return wages;
            }

            public void setWages(FieldsDTO.ProfitDTO wages) {
                this.wages = wages;
            }

            public PayFeeDTO getPayFee() {
                return payFee;
            }

            public void setPayFee(PayFeeDTO payFee) {
                this.payFee = payFee;
            }

            private FieldsDTO.ProfitDTO getFee() {
                return fee;
            }

            public void setFee(FieldsDTO.ProfitDTO fee) {
                this.fee = fee;
            }

            private FieldsDTO.ProfitDTO getSysAdjust() {
                return sysAdjust;
            }

            public void setSysAdjust(FieldsDTO.ProfitDTO sysAdjust) {
                this.sysAdjust = sysAdjust;
            }

            private FieldsDTO.ProfitDTO getDepositAgentRate() {
                return depositAgentRate;
            }

            public void setDepositAgentRate(FieldsDTO.ProfitDTO depositAgentRate) {
                this.depositAgentRate = depositAgentRate;
            }

            private FieldsDTO.ProfitDTO getWithdrawAgentRate() {
                return withdrawAgentRate;
            }

            public void setWithdrawAgentRate(FieldsDTO.ProfitDTO withdrawAgentRate) {
                this.withdrawAgentRate = withdrawAgentRate;
            }

            private FieldsDTO.ProfitDTO getIncome() {
                return income;
            }

            public void setIncome(FieldsDTO.ProfitDTO income) {
                this.income = income;
            }

            private FieldsDTO.ProfitDTO getAdjustIncome() {
                return adjustIncome;
            }

            public void setAdjustIncome(FieldsDTO.ProfitDTO adjustIncome) {
                this.adjustIncome = adjustIncome;
            }

            private FieldsDTO.ProfitDTO getLastRemain() {
                return lastRemain;
            }

            public void setLastRemain(FieldsDTO.ProfitDTO lastRemain) {
                this.lastRemain = lastRemain;
            }

            private FieldsDTO.ProfitDTO getRemain() {
                return remain;
            }

            public void setRemain(FieldsDTO.ProfitDTO remain) {
                this.remain = remain;
            }

            private FieldsDTO.ProfitDTO getPlanRatio() {
                return planRatio;
            }

            public void setPlanRatio(FieldsDTO.ProfitDTO planRatio) {
                this.planRatio = planRatio;
            }

            private FieldsDTO.ProfitDTO getAdjustRatio() {
                return adjustRatio;
            }

            public void setAdjustRatio(FieldsDTO.ProfitDTO adjustRatio) {
                this.adjustRatio = adjustRatio;
            }

            private FieldsDTO.ProfitDTO getDue() {
                return due;
            }

            public void setDue(FieldsDTO.ProfitDTO due) {
                this.due = due;
            }

            private FieldsDTO.ProfitDTO getActual() {
                return actual;
            }

            public void setActual(FieldsDTO.ProfitDTO actual) {
                this.actual = actual;
            }

            private FieldsDTO.ProfitDTO getStatus() {
                return status;
            }

            public void setStatus(FieldsDTO.ProfitDTO status) {
                this.status = status;
            }

            private FieldsDTO.ProfitDTO getSentBy() {
                return sentBy;
            }

            public void setSentBy(FieldsDTO.ProfitDTO sentBy) {
                this.sentBy = sentBy;
            }

            private FieldsDTO.ProfitDTO getAdjustNote() {
                return adjustNote;
            }

            public void setAdjustNote(FieldsDTO.ProfitDTO adjustNote) {
                this.adjustNote = adjustNote;
            }

            private FieldsDTO.ProfitDTO getNote() {
                return note;
            }

            public void setNote(FieldsDTO.ProfitDTO note) {
                this.note = note;
            }

            public static class MonthDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("type")
                private String type;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class ProfitDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("type")
                private String type;
                @SerializedName("title")
                private String title;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

            public static class PayFeeDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("type")
                private String type;
                @SerializedName("hidden")
                private Boolean hidden;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Boolean isHidden() {
                    return hidden;
                }

                public void setHidden(Boolean hidden) {
                    this.hidden = hidden;
                }
            }
        }

        public static class AllCountDTO {
            @SerializedName("due")
            private String due;

            public String getDue() {
                return due;
            }

            public void setDue(String due) {
                this.due = due;
            }
        }

        public static class PageCountDTO {
            @SerializedName("due")
            private Integer due;

            public Integer getDue() {
                return due;
            }

            public void setDue(Integer due) {
                this.due = due;
            }
        }

        public static class DataDTO {
            @SerializedName("fields")
            private FieldsDTOX fields;
            @SerializedName("total")
            private Integer total;
            @SerializedName("part")
            private List<PartDTO> part;

            public FieldsDTOX getFields() {
                return fields;
            }

            public void setFields(FieldsDTOX fields) {
                this.fields = fields;
            }

            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }

            public List<PartDTO> getPart() {
                return part;
            }

            public void setPart(List<PartDTO> part) {
                this.part = part;
            }

            public static class FieldsDTOX {
                @SerializedName("month")
                private FieldsDTO.ProfitDTO month;
                @SerializedName("username")
                private FieldsDTO.ProfitDTO username;
                @SerializedName("registered_at")
                private FieldsDTO.ProfitDTO registeredAt;
                @SerializedName("c_agency_model")
                private FieldsDTO.ProfitDTO cAgencyModel;
                @SerializedName("plan_type")
                private FieldsDTO.ProfitDTO planType;
                @SerializedName("activity_people")
                private FieldsDTO.ProfitDTO activityPeople;
                @SerializedName("real_bet")
                private FieldsDTO.ProfitDTO realBet;
                @SerializedName("profit")
                private FieldsDTO.ProfitDTO profit;
                @SerializedName("activity")
                private FieldsDTO.ProfitDTO activity;
                @SerializedName("gift")
                private FieldsDTO.ProfitDTO gift;
                @SerializedName("wages")
                private FieldsDTO.ProfitDTO wages;
                @SerializedName("fee")
                private FieldsDTO.ProfitDTO fee;
                @SerializedName("sys_adjust")
                private FieldsDTO.ProfitDTO sysAdjust;
                @SerializedName("deposit_agent_rate")
                private FieldsDTO.ProfitDTO depositAgentRate;
                @SerializedName("withdraw_agent_rate")
                private FieldsDTO.ProfitDTO withdrawAgentRate;
                @SerializedName("income")
                private FieldsDTO.ProfitDTO income;
                @SerializedName("adjust_income")
                private FieldsDTO.ProfitDTO adjustIncome;
                @SerializedName("last_remain")
                private FieldsDTO.ProfitDTO lastRemain;
                @SerializedName("remain")
                private FieldsDTO.ProfitDTO remain;
                @SerializedName("plan_ratio")
                private FieldsDTO.ProfitDTO planRatio;
                @SerializedName("adjust_ratio")
                private FieldsDTO.ProfitDTO adjustRatio;
                @SerializedName("due")
                private FieldsDTO.ProfitDTO due;
                @SerializedName("actual")
                private FieldsDTO.ProfitDTO actual;
                @SerializedName("status")
                private FieldsDTO.ProfitDTO status;
                @SerializedName("sent_by")
                private FieldsDTO.ProfitDTO sentBy;
                @SerializedName("adjust_note")
                private FieldsDTO.ProfitDTO adjustNote;
                @SerializedName("note")
                private FieldsDTO.ProfitDTO note;

                private FieldsDTO.ProfitDTO getMonth() {
                    return month;
                }

                public void setMonth(FieldsDTO.ProfitDTO month) {
                    this.month = month;
                }

                private FieldsDTO.ProfitDTO getUsername() {
                    return username;
                }

                public void setUsername(FieldsDTO.ProfitDTO username) {
                    this.username = username;
                }

                private FieldsDTO.ProfitDTO getRegisteredAt() {
                    return registeredAt;
                }

                public void setRegisteredAt(FieldsDTO.ProfitDTO registeredAt) {
                    this.registeredAt = registeredAt;
                }

                private FieldsDTO.ProfitDTO getCAgencyModel() {
                    return cAgencyModel;
                }

                public void setCAgencyModel(FieldsDTO.ProfitDTO cAgencyModel) {
                    this.cAgencyModel = cAgencyModel;
                }

                private FieldsDTO.ProfitDTO getPlanType() {
                    return planType;
                }

                public void setPlanType(FieldsDTO.ProfitDTO planType) {
                    this.planType = planType;
                }

                private FieldsDTO.ProfitDTO getActivityPeople() {
                    return activityPeople;
                }

                public void setActivityPeople(FieldsDTO.ProfitDTO activityPeople) {
                    this.activityPeople = activityPeople;
                }

                private FieldsDTO.ProfitDTO getRealBet() {
                    return realBet;
                }

                public void setRealBet(FieldsDTO.ProfitDTO realBet) {
                    this.realBet = realBet;
                }

                private FieldsDTO.ProfitDTO getProfit() {
                    return profit;
                }

                public void setProfit(FieldsDTO.ProfitDTO profit) {
                    this.profit = profit;
                }

                private FieldsDTO.ProfitDTO getActivity() {
                    return activity;
                }

                public void setActivity(FieldsDTO.ProfitDTO activity) {
                    this.activity = activity;
                }

                private FieldsDTO.ProfitDTO getGift() {
                    return gift;
                }

                public void setGift(FieldsDTO.ProfitDTO gift) {
                    this.gift = gift;
                }

                private FieldsDTO.ProfitDTO getWages() {
                    return wages;
                }

                public void setWages(FieldsDTO.ProfitDTO wages) {
                    this.wages = wages;
                }

                private FieldsDTO.ProfitDTO getFee() {
                    return fee;
                }

                public void setFee(FieldsDTO.ProfitDTO fee) {
                    this.fee = fee;
                }

                private FieldsDTO.ProfitDTO getSysAdjust() {
                    return sysAdjust;
                }

                public void setSysAdjust(FieldsDTO.ProfitDTO sysAdjust) {
                    this.sysAdjust = sysAdjust;
                }

                private FieldsDTO.ProfitDTO getDepositAgentRate() {
                    return depositAgentRate;
                }

                public void setDepositAgentRate(FieldsDTO.ProfitDTO depositAgentRate) {
                    this.depositAgentRate = depositAgentRate;
                }

                private FieldsDTO.ProfitDTO getWithdrawAgentRate() {
                    return withdrawAgentRate;
                }

                public void setWithdrawAgentRate(FieldsDTO.ProfitDTO withdrawAgentRate) {
                    this.withdrawAgentRate = withdrawAgentRate;
                }

                private FieldsDTO.ProfitDTO getIncome() {
                    return income;
                }

                public void setIncome(FieldsDTO.ProfitDTO income) {
                    this.income = income;
                }

                private FieldsDTO.ProfitDTO getAdjustIncome() {
                    return adjustIncome;
                }

                public void setAdjustIncome(FieldsDTO.ProfitDTO adjustIncome) {
                    this.adjustIncome = adjustIncome;
                }

                private FieldsDTO.ProfitDTO getLastRemain() {
                    return lastRemain;
                }

                public void setLastRemain(FieldsDTO.ProfitDTO lastRemain) {
                    this.lastRemain = lastRemain;
                }

                private FieldsDTO.ProfitDTO getRemain() {
                    return remain;
                }

                public void setRemain(FieldsDTO.ProfitDTO remain) {
                    this.remain = remain;
                }

                private FieldsDTO.ProfitDTO getPlanRatio() {
                    return planRatio;
                }

                public void setPlanRatio(FieldsDTO.ProfitDTO planRatio) {
                    this.planRatio = planRatio;
                }

                private FieldsDTO.ProfitDTO getAdjustRatio() {
                    return adjustRatio;
                }

                public void setAdjustRatio(FieldsDTO.ProfitDTO adjustRatio) {
                    this.adjustRatio = adjustRatio;
                }

                private FieldsDTO.ProfitDTO getDue() {
                    return due;
                }

                public void setDue(FieldsDTO.ProfitDTO due) {
                    this.due = due;
                }

                private FieldsDTO.ProfitDTO getActual() {
                    return actual;
                }

                public void setActual(FieldsDTO.ProfitDTO actual) {
                    this.actual = actual;
                }

                private FieldsDTO.ProfitDTO getStatus() {
                    return status;
                }

                public void setStatus(FieldsDTO.ProfitDTO status) {
                    this.status = status;
                }

                private FieldsDTO.ProfitDTO getSentBy() {
                    return sentBy;
                }

                public void setSentBy(FieldsDTO.ProfitDTO sentBy) {
                    this.sentBy = sentBy;
                }

                private FieldsDTO.ProfitDTO getAdjustNote() {
                    return adjustNote;
                }

                public void setAdjustNote(FieldsDTO.ProfitDTO adjustNote) {
                    this.adjustNote = adjustNote;
                }

                private FieldsDTO.ProfitDTO getNote() {
                    return note;
                }

                public void setNote(FieldsDTO.ProfitDTO note) {
                    this.note = note;
                }
            }

            public static class PartDTO {
                @SerializedName("id")
                private String id;
                @SerializedName("month")
                private String month;
                @SerializedName("third_type")
                private String thirdType;
                @SerializedName("user_id")
                private String userId;
                @SerializedName("agency_model")
                private String agencyModel;
                @SerializedName("registered_at")
                private String registeredAt;
                @SerializedName("plan_user_id")
                private String planUserId;
                @SerializedName("new_activity_people")
                private String newActivityPeople;
                @SerializedName("activity_people")
                private Integer activityPeople;
                @SerializedName("active_usernames")
                private String activeUsernames;
                @SerializedName("real_bet")
                private String realBet;
                @SerializedName("profit")
                private String profit;
                @SerializedName("activity")
                private String activity;
                @SerializedName("gift")
                private String gift;
                @SerializedName("wages")
                private String wages;
                @SerializedName("pay_fee")
                private String payFee;
                @SerializedName("fee")
                private String fee;
                @SerializedName("sys_adjust")
                private String sysAdjust;
                @SerializedName("income")
                private String income;
                @SerializedName("adjust_income")
                private String adjustIncome;
                @SerializedName("last_remain")
                private String lastRemain;
                @SerializedName("remain")
                private String remain;
                @SerializedName("plan_ratio")
                private String planRatio;
                @SerializedName("adjust_type")
                private String adjustType;
                @SerializedName("adjust_ratio")
                private String adjustRatio;
                @SerializedName("adjust")
                private String adjust;
                @SerializedName("due")
                private Integer due;
                @SerializedName("actual")
                private Integer actual;
                @SerializedName("status")
                private String status;
                @SerializedName("sent_by")
                private String sentBy;
                @SerializedName("note")
                private String note;
                @SerializedName("adjust_note")
                private String adjustNote;
                @SerializedName("review_note")
                private Object reviewNote;
                @SerializedName("sent_at")
                private String sentAt;
                @SerializedName("adjust_by")
                private Object adjustBy;
                @SerializedName("adjust_at")
                private Object adjustAt;
                @SerializedName("username")
                private String username;
                @SerializedName("c_agency_model")
                private String cAgencyModel;
                @SerializedName("plan_type")
                private String planType;
                @SerializedName("remark")
                private String remark;
                @SerializedName("adminname")
                private Object adminname;
                @SerializedName("r_sys_adjust")
                private Object rSysAdjust;
                @SerializedName("r_income")
                private Object rIncome;
                @SerializedName("title_registered_at")
                private String titleRegisteredAt;
                @SerializedName("deposit_agent_rate")
                private String depositAgentRate;
                @SerializedName("withdraw_agent_rate")
                private String withdrawAgentRate;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getMonth() {
                    return month;
                }

                public void setMonth(String month) {
                    this.month = month;
                }

                public String getThirdType() {
                    return thirdType;
                }

                public void setThirdType(String thirdType) {
                    this.thirdType = thirdType;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getAgencyModel() {
                    return agencyModel;
                }

                public void setAgencyModel(String agencyModel) {
                    this.agencyModel = agencyModel;
                }

                public String getRegisteredAt() {
                    return registeredAt;
                }

                public void setRegisteredAt(String registeredAt) {
                    this.registeredAt = registeredAt;
                }

                public String getPlanUserId() {
                    return planUserId;
                }

                public void setPlanUserId(String planUserId) {
                    this.planUserId = planUserId;
                }

                public String getNewActivityPeople() {
                    return newActivityPeople;
                }

                public void setNewActivityPeople(String newActivityPeople) {
                    this.newActivityPeople = newActivityPeople;
                }

                public Integer getActivityPeople() {
                    return activityPeople;
                }

                public void setActivityPeople(Integer activityPeople) {
                    this.activityPeople = activityPeople;
                }

                public String getActiveUsernames() {
                    return activeUsernames;
                }

                public void setActiveUsernames(String activeUsernames) {
                    this.activeUsernames = activeUsernames;
                }

                public String getRealBet() {
                    return realBet;
                }

                public void setRealBet(String realBet) {
                    this.realBet = realBet;
                }

                public String getProfit() {
                    return profit;
                }

                public void setProfit(String profit) {
                    this.profit = profit;
                }

                public String getActivity() {
                    return activity;
                }

                public void setActivity(String activity) {
                    this.activity = activity;
                }

                public String getGift() {
                    return gift;
                }

                public void setGift(String gift) {
                    this.gift = gift;
                }

                public String getWages() {
                    return wages;
                }

                public void setWages(String wages) {
                    this.wages = wages;
                }

                public String getPayFee() {
                    return payFee;
                }

                public void setPayFee(String payFee) {
                    this.payFee = payFee;
                }

                public String getFee() {
                    return fee;
                }

                public void setFee(String fee) {
                    this.fee = fee;
                }

                public String getSysAdjust() {
                    return sysAdjust;
                }

                public void setSysAdjust(String sysAdjust) {
                    this.sysAdjust = sysAdjust;
                }

                public String getIncome() {
                    return income;
                }

                public void setIncome(String income) {
                    this.income = income;
                }

                public String getAdjustIncome() {
                    return adjustIncome;
                }

                public void setAdjustIncome(String adjustIncome) {
                    this.adjustIncome = adjustIncome;
                }

                public String getLastRemain() {
                    return lastRemain;
                }

                public void setLastRemain(String lastRemain) {
                    this.lastRemain = lastRemain;
                }

                public String getRemain() {
                    return remain;
                }

                public void setRemain(String remain) {
                    this.remain = remain;
                }

                public String getPlanRatio() {
                    return planRatio;
                }

                public void setPlanRatio(String planRatio) {
                    this.planRatio = planRatio;
                }

                public String getAdjustType() {
                    return adjustType;
                }

                public void setAdjustType(String adjustType) {
                    this.adjustType = adjustType;
                }

                public String getAdjustRatio() {
                    return adjustRatio;
                }

                public void setAdjustRatio(String adjustRatio) {
                    this.adjustRatio = adjustRatio;
                }

                public String getAdjust() {
                    return adjust;
                }

                public void setAdjust(String adjust) {
                    this.adjust = adjust;
                }

                public Integer getDue() {
                    return due;
                }

                public void setDue(Integer due) {
                    this.due = due;
                }

                public Integer getActual() {
                    return actual;
                }

                public void setActual(Integer actual) {
                    this.actual = actual;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getSentBy() {
                    return sentBy;
                }

                public void setSentBy(String sentBy) {
                    this.sentBy = sentBy;
                }

                public String getNote() {
                    return note;
                }

                public void setNote(String note) {
                    this.note = note;
                }

                public String getAdjustNote() {
                    return adjustNote;
                }

                public void setAdjustNote(String adjustNote) {
                    this.adjustNote = adjustNote;
                }

                public Object getReviewNote() {
                    return reviewNote;
                }

                public void setReviewNote(Object reviewNote) {
                    this.reviewNote = reviewNote;
                }

                public String getSentAt() {
                    return sentAt;
                }

                public void setSentAt(String sentAt) {
                    this.sentAt = sentAt;
                }

                public Object getAdjustBy() {
                    return adjustBy;
                }

                public void setAdjustBy(Object adjustBy) {
                    this.adjustBy = adjustBy;
                }

                public Object getAdjustAt() {
                    return adjustAt;
                }

                public void setAdjustAt(Object adjustAt) {
                    this.adjustAt = adjustAt;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getCAgencyModel() {
                    return cAgencyModel;
                }

                public void setCAgencyModel(String cAgencyModel) {
                    this.cAgencyModel = cAgencyModel;
                }

                public String getPlanType() {
                    return planType;
                }

                public void setPlanType(String planType) {
                    this.planType = planType;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public Object getAdminname() {
                    return adminname;
                }

                public void setAdminname(Object adminname) {
                    this.adminname = adminname;
                }

                public Object getRSysAdjust() {
                    return rSysAdjust;
                }

                public void setRSysAdjust(Object rSysAdjust) {
                    this.rSysAdjust = rSysAdjust;
                }

                public Object getRIncome() {
                    return rIncome;
                }

                public void setRIncome(Object rIncome) {
                    this.rIncome = rIncome;
                }

                public String getTitleRegisteredAt() {
                    return titleRegisteredAt;
                }

                public void setTitleRegisteredAt(String titleRegisteredAt) {
                    this.titleRegisteredAt = titleRegisteredAt;
                }

                public String getDepositAgentRate() {
                    return depositAgentRate;
                }

                public void setDepositAgentRate(String depositAgentRate) {
                    this.depositAgentRate = depositAgentRate;
                }

                public String getWithdrawAgentRate() {
                    return withdrawAgentRate;
                }

                public void setWithdrawAgentRate(String withdrawAgentRate) {
                    this.withdrawAgentRate = withdrawAgentRate;
                }
            }
        }
    }
}
