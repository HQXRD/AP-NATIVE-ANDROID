package com.xtree.base.utils;

import java.math.BigDecimal;

/* 数字转换成汉字大写*/
public class NumberToUppercaseUtil {
    //汉语中数字大写
    private static final String [] CN_UPPER_NUMBER={"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    //汉语中货币单位大写
    private static final String [] CN_UPPER_MONETRAY_UNIT={"","拾","佰","仟"};
    private static final String [] CN_UPPER_SECTION ={"","萬","亿","萬亿"};

    public static String numberToCN(String inputNumber){
        if (inputNumber == null){
            return "";
        }
        BigDecimal money = new BigDecimal(inputNumber);
        StringBuffer stringBuffer = new StringBuffer();
        if (money.compareTo(BigDecimal.ZERO) == 0){
           return  "零元整";
        }
        String strMoney = money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        int integerPart  = Integer.parseInt(strMoney.substring(0,strMoney.indexOf(".")));
        int decimalPart  = Integer.parseInt(strMoney.substring(strMoney.indexOf(".")+1));
        // 转换整数部分
        int unit = 0; // 节权位计数器
        while (integerPart > 0) {
            int section = integerPart % 10000; // 取最后的四位数
            if (section != 0) {
                StringBuilder sectionStr = new StringBuilder();
                int count = 0; // 货币单位计数器
                while (section > 0) {
                    int digit = section % 10; // 取最后一位数
                    if (digit != 0) {
                        sectionStr.insert(0, CN_UPPER_MONETRAY_UNIT[count] + CN_UPPER_NUMBER[digit]);
                    } else if (count == 0 || count == 1) { // 若第一位或第二位为0，需添加“零”以保持正确性
                        sectionStr.insert(0, CN_UPPER_NUMBER[digit]);
                    }
                    count++;
                    section /= 10;
                }
                stringBuffer.insert(0, sectionStr.toString() + CN_UPPER_SECTION[unit]);
            }
            unit++;
            integerPart /= 10000;
        }
        // 处理小数部分
        if (decimalPart == 0) {
            stringBuffer.append("整");
        } else {
            stringBuffer.append(CN_UPPER_NUMBER[decimalPart / 10]).append("角").append(CN_UPPER_NUMBER[decimalPart % 10]).append("分");
        }
        return stringBuffer.toString();
    }


    private static final  String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final String CN_ZERO_FULL ="零元";
    private static final int  MONEY_PRECISION = 2;
    private static  String resultsText = "零元整";

  /*  public static String numberToUpper(final String inputNumber)
    {
        String outUpper = null ;
        if (inputNumber.length() > 16 || inputNumber.indexOf(".") == -1){
            outUpper = "" ;
        }else {
            outUpper =  operation(inputNumber);
        }
        return outUpper ;
    }
    private static String  operation(final String inputNumber){
        BigDecimal numberOfMoney = new BigDecimal(inputNumber);
        StringBuffer stringBuffer = new StringBuffer();
        //-1 小于0 ； 0 ；1 大于0
        int signum = numberOfMoney.signum() ;
        if (signum == 0){
            resultsText = CN_ZERO_FULL + CN_FULL ;
        }
        //四舍五入
        long number = numberOfMoney.movePointLeft(MONEY_PRECISION).setScale(0,4).abs().longValue();
        //小数点后两位
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0 ;
        boolean getZero = false;
        if (!(scale >0)){
            numIndex = 2 ;
            number /= 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale%10 > 0))){
            numIndex = 1;
            number /= 10;
            getZero = true ;
        }
        int zeroSize = 0;
        while (true){
            if (number < 0){
                break;
            }
            numUnit = (int)(number % 10) ;
            if (numUnit > 0 ){
                if ((numIndex == 9) && (zeroSize>= 3)){
                    stringBuffer.insert(0 , CN_UPPER_MONEY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize>= 3) ){
                    stringBuffer.insert(0 , CN_UPPER_MONEY_UNIT[10]);
                }if ((numIndex == 17) && (zeroSize>= 3) ){
                    stringBuffer.insert(0 , CN_UPPER_MONEY_UNIT[14]);
                }
                stringBuffer.insert(0 ,CN_UPPER_MONEY_UNIT[numIndex]);
                stringBuffer.insert(0, CN_UPPER_NUMBER[numIndex]);
                getZero = false ;
                zeroSize = 0 ;
            }else{
                ++zeroSize ;
                if (numIndex !=0 && numIndex != 1 && numIndex !=2 && numIndex !=6 && numIndex !=10 && numIndex !=14){
                    if (!getZero){
                        stringBuffer.insert(0 ,CN_UPPER_NUMBER[numUnit]);
                    }
                }
                if (numIndex == 2){
                    if (number>0){
                        stringBuffer.insert(0,CN_UPPER_MONEY_UNIT[numIndex]);
                    }
                } else if (((numIndex -2)%4 == 0) && (number %1000 > 0)) {
                    stringBuffer.insert(0 ,CN_UPPER_MONEY_UNIT[numIndex]);
                }
                getZero = false ;
            }

            number /= 10 ;
            ++numIndex ;
        }
        if (signum == -1){
            stringBuffer.insert(0,CN_NEGATIVE);
        }
        stringBuffer.append(CN_FULL);
        resultsText = stringBuffer.toString();
        return  resultsText;
    }*/


}
