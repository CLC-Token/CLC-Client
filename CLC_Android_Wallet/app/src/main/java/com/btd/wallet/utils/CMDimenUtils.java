package com.btd.wallet.utils;

import android.content.Context;
import android.util.DisplayMetrics;


public class CMDimenUtils
{

	/**
	 * dp 转 px
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp)
	{
		// 1px = 1dp * (dpi / 160)

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int dpi = metrics.densityDpi;

		return (int) (dp * (dpi / 160f) + 0.5f);
	}

	/**
	 * px 转 dp
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dp(Context context, int px)
	{
		// 1dp = 1px * 160 / dpi

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();

		int dpi = metrics.densityDpi;
		return (int) (px * 160f / dpi + 0.5f);
	}
	
}
