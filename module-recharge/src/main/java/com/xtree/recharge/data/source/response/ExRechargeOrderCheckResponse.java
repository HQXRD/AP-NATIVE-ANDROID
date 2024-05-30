package com.xtree.recharge.data.source.response;

import com.google.gson.annotations.SerializedName;
import com.xtree.recharge.vo.RechargeVo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by KAKA on 2024/5/29.
 * Describe:
 */
public class ExRechargeOrderCheckResponse {

    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * message
     */
    @SerializedName("message")
    private String message;
    /**
     * data
     */
    @SerializedName("data")
    private DataDTO data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private int timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        /**
         * platformOrder
         */
        @SerializedName("platformOrder")
        private String platformOrder;
        /**
         * payAmount
         */
        @SerializedName("payAmount")
        private String payAmount;
        /**
         * payBankCode
         */
        @SerializedName("payBankCode")
        private String payBankCode;
        /**
         * payName
         */
        @SerializedName("payName")
        private String payName;
        /**
         * payAccount
         */
        @SerializedName("payAccount")
        private String payAccount;
        /**
         * payBankName
         */
        @SerializedName("payBankName")
        private String payBankName;
        /**
         * returncode
         */
        @SerializedName("returncode")
        private String returncode;
        /**
         * status
         */
        @SerializedName("status")
        private String status;
        /**
         * message
         */
        @SerializedName("message")
        private String message;
        /**
         * merchantOrder
         */
        @SerializedName("merchant_order")
        private String merchantOrder;
        /**
         * createTime
         */
        @SerializedName("create_time")
        private String createTime;
        /**
         * expireTime
         */
        @SerializedName("expire_time")
        private String expireTime;
        /**
         * allowCancelWait
         */
        @SerializedName("allow_cancel_wait")
        private int allowCancelWait;
        /**
         * cancelWaitTime
         */
        @SerializedName("cancel_wait_time")
        private String cancelWaitTime;
        /**
         * allowCancel
         */
        @SerializedName("allow_cancel")
        private int allowCancel;
        /**
         * allowCancelTime
         */
        @SerializedName("allow_cancel_time")
        private String allowCancelTime;
        /**
         * bankAccount
         */
        @SerializedName("bank_account")
        private String bankAccount;
        /**
         * bankAccountName
         */
        @SerializedName("bank_account_name")
        private String bankAccountName;
        /**
         * bankCode
         */
        @SerializedName("bank_code")
        private String bankCode;
        /**
         * bankName
         */
        @SerializedName("bank_name")
        private String bankName;
        /**
         * bankArea
         */
        @SerializedName("bank_area")
        private String bankArea;
        /**
         * opBankList
         */
        @SerializedName("op_bank_list")
        private OpBankListDTO opBankList;
        /**
         * userBankInfo
         */
        @SerializedName("user_bank_info")
        private Object userBankInfo;

        public String getPlatformOrder() {
            return platformOrder;
        }

        public void setPlatformOrder(String platformOrder) {
            this.platformOrder = platformOrder;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayBankCode() {
            return payBankCode;
        }

        public void setPayBankCode(String payBankCode) {
            this.payBankCode = payBankCode;
        }

        public String getPayName() {
            return payName;
        }

        public void setPayName(String payName) {
            this.payName = payName;
        }

        public String getPayAccount() {
            return payAccount;
        }

        public void setPayAccount(String payAccount) {
            this.payAccount = payAccount;
        }

        public String getPayBankName() {
            return payBankName;
        }

        public void setPayBankName(String payBankName) {
            this.payBankName = payBankName;
        }

        public String getReturncode() {
            return returncode;
        }

        public void setReturncode(String returncode) {
            this.returncode = returncode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMerchantOrder() {
            return merchantOrder;
        }

        public void setMerchantOrder(String merchantOrder) {
            this.merchantOrder = merchantOrder;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getAllowCancelWait() {
            return allowCancelWait;
        }

        public void setAllowCancelWait(int allowCancelWait) {
            this.allowCancelWait = allowCancelWait;
        }

        public String getCancelWaitTime() {
            return cancelWaitTime;
        }

        public void setCancelWaitTime(String cancelWaitTime) {
            this.cancelWaitTime = cancelWaitTime;
        }

        public int getAllowCancel() {
            return allowCancel;
        }

        public void setAllowCancel(int allowCancel) {
            this.allowCancel = allowCancel;
        }

        public String getAllowCancelTime() {
            return allowCancelTime;
        }

        public void setAllowCancelTime(String allowCancelTime) {
            this.allowCancelTime = allowCancelTime;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public String getBankAccountName() {
            return bankAccountName;
        }

        public void setBankAccountName(String bankAccountName) {
            this.bankAccountName = bankAccountName;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankArea() {
            return bankArea;
        }

        public void setBankArea(String bankArea) {
            this.bankArea = bankArea;
        }

        public OpBankListDTO getOpBankList() {
            return opBankList;
        }

        public void setOpBankList(OpBankListDTO opBankList) {
            this.opBankList = opBankList;
        }

        public Object getUserBankInfo() {
            return userBankInfo;
        }

        public void setUserBankInfo(Object userBankInfo) {
            this.userBankInfo = userBankInfo;
        }

        public static class OpBankListDTO {
            /**
             * used
             */
            @SerializedName("used")
            private List<RechargeVo.OpBankListDTO.BankInfoDTO> used;
            /**
             * top
             */
            @SerializedName("top")
            private List<RechargeVo.OpBankListDTO.BankInfoDTO> top;
            /**
             * hot
             */
            @SerializedName("hot")
            private List<RechargeVo.OpBankListDTO.BankInfoDTO> hot;
            /**
             * others
             */
            @SerializedName("others")
            private List<RechargeVo.OpBankListDTO.BankInfoDTO> others;

            /**
             * 用户绑定银行
             */
            private HashMap<String, String> mBind;

            public HashMap<String, String> getmBind() {
                return mBind;
            }

            public void setmBind(HashMap<String, String> mBind) {
                this.mBind = mBind;
            }

            public List<RechargeVo.OpBankListDTO.BankInfoDTO> getUsed() {
                return used;
            }

            public void setUsed(List<RechargeVo.OpBankListDTO.BankInfoDTO> used) {
                this.used = used;
            }

            public List<RechargeVo.OpBankListDTO.BankInfoDTO> getTop() {
                return top;
            }

            public void setTop(List<RechargeVo.OpBankListDTO.BankInfoDTO> top) {
                this.top = top;
            }

            public List<RechargeVo.OpBankListDTO.BankInfoDTO> getHot() {
                return hot;
            }

            public void setHot(List<RechargeVo.OpBankListDTO.BankInfoDTO> hot) {
                this.hot = hot;
            }

            public List<RechargeVo.OpBankListDTO.BankInfoDTO> getOthers() {
                return others;
            }

            public void setOthers(List<RechargeVo.OpBankListDTO.BankInfoDTO> others) {
                this.others = others;
            }

            public static class TopDTO {
                /**
                 * bankCode
                 */
                @SerializedName("bank_code")
                private String bankCode;
                /**
                 * bankName
                 */
                @SerializedName("bank_name")
                private String bankName;

                public String getBankCode() {
                    return bankCode;
                }

                public void setBankCode(String bankCode) {
                    this.bankCode = bankCode;
                }

                public String getBankName() {
                    return bankName;
                }

                public void setBankName(String bankName) {
                    this.bankName = bankName;
                }
            }

            public static class HotDTO {
                /**
                 * bankCode
                 */
                @SerializedName("bank_code")
                private String bankCode;
                /**
                 * bankName
                 */
                @SerializedName("bank_name")
                private String bankName;

                public String getBankCode() {
                    return bankCode;
                }

                public void setBankCode(String bankCode) {
                    this.bankCode = bankCode;
                }

                public String getBankName() {
                    return bankName;
                }

                public void setBankName(String bankName) {
                    this.bankName = bankName;
                }
            }

            public static class OthersDTO {
                /**
                 * bankCode
                 */
                @SerializedName("bank_code")
                private String bankCode;
                /**
                 * bankName
                 */
                @SerializedName("bank_name")
                private String bankName;

                public String getBankCode() {
                    return bankCode;
                }

                public void setBankCode(String bankCode) {
                    this.bankCode = bankCode;
                }

                public String getBankName() {
                    return bankName;
                }

                public void setBankName(String bankName) {
                    this.bankName = bankName;
                }
            }
        }
    }
}
