package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/3/20.
 * Describe:
 */
public class DividendAgrtSendReeponse {

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
        @SerializedName("1")
        private _$1DTO _$1;
        @SerializedName("2")
        private _$2DTO _$2;
        @SerializedName("3")
        private _$3DTO _$3;
        @SerializedName("4")
        private _$4DTO _$4;

        public _$1DTO get_$1() {
            return _$1;
        }

        public void set_$1(_$1DTO _$1) {
            this._$1 = _$1;
        }

        public _$2DTO get_$2() {
            return _$2;
        }

        public void set_$2(_$2DTO _$2) {
            this._$2 = _$2;
        }

        public _$3DTO get_$3() {
            return _$3;
        }

        public void set_$3(_$3DTO _$3) {
            this._$3 = _$3;
        }

        public _$4DTO get_$4() {
            return _$4;
        }

        public void set_$4(_$4DTO _$4) {
            this._$4 = _$4;
        }

        public static class _$1DTO {
            private int pay_status;
            private int self_money;
            private int bill_num;

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
            }

            public int getSelf_money() {
                return self_money;
            }

            public void setSelf_money(int self_money) {
                this.self_money = self_money;
            }

            public int getBill_num() {
                return bill_num;
            }

            public void setBill_num(int bill_num) {
                this.bill_num = bill_num;
            }
        }

        public static class _$2DTO {
            private int pay_status;
            private int self_money;
            private int bill_num;

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
            }

            public int getSelf_money() {
                return self_money;
            }

            public void setSelf_money(int self_money) {
                this.self_money = self_money;
            }

            public int getBill_num() {
                return bill_num;
            }

            public void setBill_num(int bill_num) {
                this.bill_num = bill_num;
            }
        }

        public static class _$3DTO {
            private int pay_status;
            private int self_money;
            private int bill_num;

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
            }

            public int getSelf_money() {
                return self_money;
            }

            public void setSelf_money(int self_money) {
                this.self_money = self_money;
            }

            public int getBill_num() {
                return bill_num;
            }

            public void setBill_num(int bill_num) {
                this.bill_num = bill_num;
            }
        }

        public static class _$4DTO {
            private int pay_status;
            private int self_money;
            private int bill_num;

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
            }

            public int getSelf_money() {
                return self_money;
            }

            public void setSelf_money(int self_money) {
                this.self_money = self_money;
            }

            public int getBill_num() {
                return bill_num;
            }

            public void setBill_num(int bill_num) {
                this.bill_num = bill_num;
            }
        }
    }
}
