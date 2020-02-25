/**
 * Copyright (C) 2015 PeerSafe Technologies. All rights reserved.
 *
 * @Name: StringUtils.java
 * @Package: com.peersafe.shadowtalk.utils.common
 * @Description: 字符串操作工具包
 * @author zhangyang
 * @date 2015年6月24日 上午11:00:38
 */

package com.peersafe.hdtsdk.inner;

import android.text.InputFilter;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangyang
 * @Description
 * @date
 */

public class StringUtils
{
    private static final String TAG = StringUtils.class.getSimpleName();

//    public static SensitivewordFilter mSensitivewordFilter;
//
//    static {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mSensitivewordFilter = new SensitivewordFilter();
//            }
//        }).start();
//    }

    /**
     * @param input
     * @return
     * @Description 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     * @author zhangyang
     */
    public static boolean isEmpty(String input)
    {
        if (null == input || "".equals(input))
        {
            return true;
        }

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
                return false;
            }
        }
        return true;
    }

    public static String nullToEmpty(String input)
    {
        return isEmpty(input) ? "" : input;
    }

    /**
     * @param str
     * @return
     * @Description 判断是否为数字
     * @author zhangyang
     */
    public static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }

    /**
     * @param str
     * @return
     * @Description 判断是否为众享币浮点数字
     * @author caozhongzheng
     */
    public static boolean isDouble(String str)
    {
        Pattern pattern_normal;
        String regex_strict = "[0-9]{0,99}(\\.{0,1}[0-9]{1,2})?";//整数或浮点数（最多两位）;
        String regex_normal = "[0-9]{0,99}\\.{0,1}[0-9]{0,2}";//整数或浮点数（最多两位）;
        pattern_normal = Pattern.compile(regex_normal);

        return pattern_normal.matcher(str).matches();
    }

    /**
     * @param str
     * @return
     * @Description 去掉字符串后面的空格
     * @author zhangyang
     */
    public static String trimTailBlank(String str)
    {
        if (isEmpty(str))
        {
            return "";
        }

        return ("A" + str).trim().substring(1);
    }

    public static String ByteArrayToStr(byte[] byteArray) throws Exception
    {
        String value = new String(byteArray, "UTF-8");
        return value;
    }

    public static String ByteToStr(byte[] byteArray)
    {
        String value = "";
        try
        {
            value = ByteArrayToStr(byteArray);
        }
        catch (Exception e)
        {
        }
        return value;
    }

    public static byte[] StrToByteArray(String str)
    {

        try{
            byte[] strbyte = null;
            strbyte = str.getBytes("UTF-8");
            return strbyte;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static String[] toStringArray(Object[] values)
    {
        int length = values.length;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++)
        {
            Object object = values[i];
            if (object != null)
            {
                strings[i] = object.toString();
            } else
            {
                strings[i] = null;
            }
        }
        return strings;
    }

    /**
     * double类型的数保留后两位小数
     *
     * @param
     * @return
     */
    public static String formatDouble(double d)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

    /**
     * 判断输入的字符串是否为数字和字母的组合
     *
     * @param str
     * @return
     * @sunhaitao
     */
    public static boolean isLetterDigit(String str)
    {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
            { //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i)))
            {//用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    /**
     * 判断字符串是否只为英文字母
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str)
    {
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 过滤密码框输入空格
     */
    public static InputFilter getFilter()
    {
        return getFilter(" ");
    }

    /**
     * 过滤输入框输入
     *
     * @param filterString 换行：\n;空格等
     */
    public static InputFilter getFilter(final String filterString)
    {
        InputFilter filter = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned,
                                       int i2, int i3)
            {
                if (charSequence.equals(filterString))
                {
                    return "";
                } else
                {
                    return null;
                }
            }
        };

        return filter;
    }

    public static String getStringPercent(float f)
    {
        NumberFormat nf = NumberFormat.getPercentInstance();
        return nf.format(f);
    }

    public static String getHeadTailString(String str)
    {
        return getHeadTailString(str, 4, 4);
    }


    public static String refactorLotteryName(String lotteryName)
    {
        return "\"" + getHeadTailString(lotteryName) + "\"";
    }

    /**
     * 获取一个字符串头尾各head，tail长度的字符串
     *
     * @param str  源字符串
     * @param head 截取头长
     * @param tail 截取尾长
     *             eg: "123abc",2,2 返回 "12*bc"
     *             "123abc",3,3 返回 "123abc"
     *             "123abc",4,3 返回 "123abc"
     */
    public static String getHeadTailString(String str, int head, int tail)
    {
        if (isEmpty(str) || head <= 0 || tail <= 0 || head + tail >= str.length())
        {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.substring(0, head));
        sb.append("...");
        sb.append(str.substring(str.length() - tail));
        return sb.toString();
    }


//    /**
//     *  过滤敏感词
//     * */
//    public static String filter(String txt) {
//        long beginTime = System.currentTimeMillis();
//        Set<String> set = mSensitivewordFilter.getSensitiveWord(txt, 2);
//        String result = mSensitivewordFilter.replaceSensitiveWord(txt, 2, "*");
//        long endTime = System.currentTimeMillis();
//        OLLogger.i(TAG, "语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
//        Iterator<String> iterator = set.iterator();
//        while(iterator.hasNext()) {
//            String key = iterator.next();
//            OLLogger.i(TAG, "包含敏感词:"+key);
//        }
//        OLLogger.i(TAG, "总共消耗时间为：" + (endTime - beginTime));
//        return result;
//    }

    /**
     * 字符串转换成十六进制字符串
     * @param str 待转换的ASCII字符串
     * @return String
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     * @param src
     *            16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }
}
