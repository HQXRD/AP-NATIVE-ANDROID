package com.xtree.mine.vo.withdrawVo;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * 选择提款模式
 */
public class WithdrawalListVo  {
    public String name;
    public String title;
    public String type;
    public boolean enable;//是否开启 true:开启 false:关闭该提款通道
    public boolean flag;//是否被选中 true:被选中 false：未被选中

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WithdrawalListVo that = (WithdrawalListVo) obj;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, title, type, enable);
    }

    @Override
    public String toString() {
        return "WithdrawalListVo{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", enable=" + enable +
                ", flag=" + flag +
                '}';
    }
    /*{
	"status": 10000,
	"message": "success",
	"data": [
		{
			"name": "usdt",
			"title": "USDT提款",
			"type": "2",
			"enable": true
		},
		{
			"name": "hipayht",
			"title": "固额提现",
			"type": "1",
			"enable": true
		},
		{
			"name": "onepayfast3",
			"title": "通用提现",
			"type": "1",
			"enable": true
		},
		{
			"name": "hipaytx",
			"title": "天下提现",
			"type": "1",
			"enable": true
		},
		{
			"name": "generalchannel",
			"title": "大额提现",
			"type": "1",
			"enable": true
		},
		{
			"name": "ebpay",
			"title": "EBPAY提款",
			"type": "4",
			"enable": true
		},
		{
			"name": "hiwallet",
			"title": "CNYT提款",
			"type": "6",
			"enable": true
		}
	],
	"timestamp": 1714027704
}*/
}
