package com.btd.wallet.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.config.WalletType;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yzy on 2016/7/18.
 * 功能：字符串操作类
 */
public class StringUtils {


    //去掉未尾的0，并最多保留6位小数点
    public static String decimalToString(BigDecimal decimal){
        decimal=decimal.setScale(6,BigDecimal.ROUND_DOWN);
        String str= decimal.stripTrailingZeros().toPlainString();
        if(str.equalsIgnoreCase("0.000000")){
            return "0";
        }
        return str;
    }

    /**
     * 两个double相加，小数点后最大长度为6位
     *
     * @param isKill 是否要清除小数点后面的0
     * @return 返回string类型，去掉小数点后面的0
     */
    public static String addWallet(String x, String y, boolean isKill) {
        BigDecimal bd1 = new BigDecimal(x);
        BigDecimal bd2 = new BigDecimal(y);
        //  double sumDouble = bd1.add(bd2).doubleValue();
        BigDecimal bSum = bd1.add(bd2);
        if(!isKill){
            return bSum.toPlainString();
        }else{
            return decimalToString(bSum);
        }
    }
    /**
     * MD5加密
     *
     * @param str 要加密的字符串
     * @return
     */
    public static String string2MD5(String str) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        if (str != null) {
            str = str.trim();
        }
        return TextUtils.isEmpty(str);
    }













    /**
     * 将字符串为空、字符串为"null"的，转化为长度为0的字符串
     *
     * @param str
     * @return
     */
    public static String stringNoNull(String str) {
        String resultStr = "";
        if (!TextUtils.isEmpty(str)) {
            if (!"null".equals(str)) {
                resultStr = str;
            }
        }
        return resultStr;
    }



    /**
     * 字符串是否为null或'null'
     *
     * @param str
     * @return true 为空字符串
     */
    public static boolean isEmptyOrNull(String str) {
        return isEmpty(stringNoNull(str));
    }




    public static boolean startCheck(String reg, String string) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }


    /*****************************xhunmon start*****************************************/

    /**
     * 以前edittext禁止输入特殊字符，而不提示，导致用户搞错密码
     * @param localPrivateKey  本地加密的私钥
     * @param pwd 密码
     * @return 如果返回""，则是密码错误
     */
    public static String deCodePrivateNoLimit(String localPrivateKey, String pwd){
        String realPrivatekey = "";
        String key = StringUtils.deCodePrivate(localPrivateKey, pwd);
        LogUtils.i("privateKey: " + key);
        if (StringUtils.isEmptyOrNull(key) || !key.startsWith(WalletType.strWalletPre)) {
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < pwd.length(); i++) {
                if (StringUtils.startCheck("^[0-9A-Za-z]", String.valueOf(pwd.charAt(i)))) {
                    stringBuffer.append(pwd.charAt(i));
                }
            }
            if (pwd.length() != stringBuffer.length()) {
                LogUtils.i("add other char:" + stringBuffer.toString());
                key = StringUtils.deCodePrivate(localPrivateKey, stringBuffer.toString());
                if (StringUtils.isEmptyOrNull(key) || !key.startsWith(WalletType.strWalletPre)) {
                    return realPrivatekey;
                }
            } else {
                return realPrivatekey;
            }
        }
        realPrivatekey = key.substring(WalletType.strWalletPre.length());
        return realPrivatekey;
    }



    /**
     * 将本地保存的私钥密码进行解密
     * 注意：解密拿到返回值需要进行result.startsWith("CMBTR-")判断用户输入的密码是否正确
     *
     * @param privateKey 本地加密秘钥
     * @param pwd        用户密码
     * @return 解密后的结果
     */
    public static String deCodePrivate(String privateKey, String pwd) {
        String result = null;
        try {
            if (TextUtils.isEmpty(pwd)) {
                pwd = "***";//用户输入密码为空，默认解密结果错误
            }
            byte[] priKey = Base64.decode(privateKey, Base64.DEFAULT);
            if (priKey == null) {
                return null;
            }
            int len = priKey.length;
            byte[] key = StringUtils.string2MD5(pwd).toUpperCase().getBytes("UTF-8");
            int j = 0;
            for (int i = 0; i < len; i++) {
                priKey[i] = (byte) (priKey[i] ^ key[j]);
                j++;
                if (j == key.length) {
                    j = 0;
                }
            }
            result = new String(priKey, "UTF-8");
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return result;
    }


    /**
     * long（或系统时间）转成Date类型
     *
     * @param currentTime 要转换的long类型的时间
     * @param formatType  要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime);
        // 把date类型的时间转换为string
        String sDateTime = dateToString(dateOld, formatType);
        // 把String类型转换为Date类型
        Date date = stringToDate(sDateTime, formatType);
        return date;
    }

    /**
     * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param date
     * @param formatType
     * @return
     */
    public static String dateToString(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }

    /**
     * strTime要转换的string类型的时间，formatType
     *
     * @param strTime    时间格式必须要与formatType的时间格式相同
     * @param formatType 要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            LogUtils.e(e.toString());
        }
        return date;
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean inSameDay(Date date1, Date date2) {
        if (date2 == null) {
            return false;
        }
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        if ((year1 == year2) && (day1 == day2)) {
            return true;
        }
        return false;
    }




    /**
     * 出去小数点后面的0
     */
    public static String killZero(double value) {
        int b = (int) value;
        if (value == b) {
            return String.valueOf(b);
        } else {
            return doubleFormat(value);
        }
    }




    /**
     * 进行对私钥进行加密
     * CMBTR- 为前缀，解密后用来判断用户密码输入是否正确
     *
     * @param privateKey 本地加密秘钥
     * @param pwd        用户密码
     * @return 解密后的结果
     */
    public static String enCodePrivate(String privateKey, String pwd) {
        String str = null;
        try {
            //加上前缀
            byte[] text = (WalletType.strWalletPre + privateKey).getBytes("UTF-8");
            byte[] result = new byte[text.length];
            byte[] keyArray = string2MD5(pwd).toUpperCase().getBytes("UTF-8");
            byte key;
            int size = text.length;
            for (int i = 0; i < size; i++) {
                key = keyArray[i % keyArray.length];
                result[i] = (byte) (text[i] ^ key);
            }
            str = Base64.encodeToString(new String(result, "UTF-8").getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return str;
    }






    public static int getDecimalPlaces(BigDecimal d) {
        return (d.stripTrailingZeros().toPlainString() + "").length() - (d.stripTrailingZeros().toPlainString() + "").indexOf(".") - 1;
    }

    public static int getDecimalPlacesByString(String dStr) {
        BigDecimal d=new BigDecimal("0");
        if(dStr!=null||!dStr.equals("null")){
            d=new BigDecimal(dStr);
        }
        return (d.stripTrailingZeros().toPlainString() + "").length() - (d.stripTrailingZeros().toPlainString() + "").indexOf(".") - 1;
    }

    public static String doubleFormat(double num) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        // 不使用千分位，即展示为11672283.234，而不是11,672,283.234
        nf.setGroupingUsed(false);
        // 设置数的小数部分所允许的最小位数
        nf.setMinimumFractionDigits(0);
        // 设置数的小数部分所允许的最大位数
        nf.setMaximumFractionDigits(6);
        return nf.format(num);
    }










}
