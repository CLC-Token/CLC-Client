package com.btd.library.base.util;

public class CMOtherUtils {
  public static StackTraceElement getCallerStackTraceElement() {
    return Thread.currentThread().getStackTrace()[4];
  }
}
