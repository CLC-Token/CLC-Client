package com.btd.wallet.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.core.WorkApp;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 文件工具类
 * $Date: 2019-04-06 18:53:41 +0800 (周六, 06 4月 2019) $
 */
public class CMFileUtils {

    /** 系统API获取外部存储路径 */
    private static final String ABSOLUTE_SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /** 存放在SD卡的显式文件夹_临时文件夹 */
    public static String SDPATH_DCIM_TEMP = ABSOLUTE_SDPATH + File.separator + "DCIM/temp/";

    /** 存放在SD卡/Android/包名/cache/backup/日期/ */
    private static final String SDPATH_BACK_UP = WorkApp.workApp.getExternalCacheDir() + "/backup/%1$s/";

    public static String WECHAT_DIR = ABSOLUTE_SDPATH + "/tencent/MicroMsg/Download";

    /** 存放在SD卡的显式文件夹 */
    public static final String SDPATH_SHOW = ABSOLUTE_SDPATH + "/" + WorkApp.DIR_NAME_SHOW + "/";
    /** 异常路径存放位置 **/
    public static String LOG_PATH = SDPATH_SHOW + "/Log/";
    /**
     * <p> 此公式来自于盒子,用于在查看照片信息的时候,比对是否为缩略图
     */
    public static String formatFileSize(long fileS) {
        String fileSizeString = null;
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "K";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "M";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "G";
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }

        if (null == fileSizeString) {
            fileSizeString = "";
        }

        return fileSizeString;
    }

    /**
     * <p> 此公式来自于盒子,下载的时候,进行容量判断
     */
    public static long formatFileSize(String size) {
        if (TextUtils.isEmpty(size)) {
            return 0;
        }
        try {
            String tempSize = size.toUpperCase();
            int end = tempSize.length();
            int base = 1;
            if (tempSize.endsWith("GB")) {
                end = end - 2;
                base = 1073741824;
            } else if (tempSize.endsWith("G")) {
                end = end - 1;
                base = 1073741824;
            } else if (tempSize.endsWith("MB")) {
                end = end - 2;
                base = 1048576;
            } else if (tempSize.endsWith("M")) {
                end = end - 1;
                base = 1048576;
            } else if (tempSize.endsWith("KB")) {
                end = end - 2;
                base = 1024;
            } else if (tempSize.endsWith("K")) {
                end = end - 1;
                base = 1024;
            } else if (tempSize.endsWith("B")) {
                end = end - 1;
                base = 1;
            }

            tempSize = tempSize.substring(0, end);
            double realSize = (Double.parseDouble(tempSize) * base);
            return (long) realSize;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return 0;
    }

    /***
     * 非法字符  <>*?:"\/| 空格 $ & ^
     */
    public static boolean isInValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) {
            return false;
        } else {
            // String pattern = "\\ / : * ? \" < > |";
            String p1 = "\\";
            String p2 = "/";
            String p3 = ":";
            String p4 = "*";
            String p5 = "?";
            String p6 = "\"";
            String p7 = "<";
            String p8 = ">";
            String p9 = "|";
            String p10 = "&";
            String p11 = "$";
            String p12 = "^";
            String[] ps = new String[]{p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12};

            for (String p : ps) {
                if (fileName.contains(p)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * <p> 根据mimeType判断文件是否可以预览
     *
     * @param mimeType
     *         文件类型
     *
     * @return 手机支持预览
     */
    public static boolean isCanPreview(String mimeType) {
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.setDataAndType(Uri.fromParts("file", "", null), mimeType);
        ResolveInfo ri = WorkApp.workApp.getPackageManager().resolveActivity(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return !(null == ri);
    }

    /**
     * <p> 格式化文件大小
     */
    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        double gigabyte = megaByte / 1024;
        if (gigabyte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigabyte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigabyte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    public static String formatCapacityVal(double value){
        String[] units = {"B","K","M","G","T","P"};
        DecimalFormat format = new DecimalFormat("0.0");
        for(int i=0;;i++){
            if(value<1024 || (i+1) >= units.length) {
                return format.format(value) + units[i];
            }
            value /= 1024;
        }
    }
}
