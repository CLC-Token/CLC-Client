/*
 * Copyright (C) 2017 PeerSafe Technologies. All rights reserved.
 *
 * @author zhangyang
 * @Package: com.peersafe.zxwallet.utils.log
 * @Description:
 * @date 2017年08月22日  10:32:00
 */

package com.peersafe.hdtsdk.log;

import android.util.Log;

public class ZXLogger
{
    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    private static int mLogLevel = VERBOSE;

    private static boolean mIsCloseLog = false;

    public static void v(String tag, String message)
    {
        if (mLogLevel <= VERBOSE && !mIsCloseLog)
        {
            Log.v(tag, message == null ? "" : message);
        }
    }

    public static void d(String tag, String message)
    {
        if (mLogLevel <= DEBUG && !mIsCloseLog)
        {
            Log.d(tag, message == null ? "" : message);
        }
    }

    public static void i(String tag, String message)
    {
        if (mLogLevel <= INFO && !mIsCloseLog)
        {
            Log.i(tag, message == null ? "" : message);
        }
    }

    public static void w(String tag, String message)
    {
        if (mLogLevel <= WARN && !mIsCloseLog)
        {
            Log.w(tag, message == null ? "" : message);
        }
    }

    public static void e(String tag, String message)
    {
        if (mLogLevel <= ERROR && !mIsCloseLog)
        {
            Log.e(tag, message == null ? "" : message);
        }
    }

    public void setLogLevel(int level)
    {
        mLogLevel = level;
    }

    public int getLogLevel()
    {
        return mLogLevel;
    }

    public void setCloseLog(boolean isClose)
    {
        mIsCloseLog = isClose;
    }

    public boolean getCloseLog()
    {
        return mIsCloseLog;
    }
}
