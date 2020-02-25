package com.btd.wallet.widget;

/**
 * Created by Administrator on 2015/5/5.
 */
public  class MultiConfigure {
    public static final int CONTENT_VIEW = 0;

    public static final int ERROR_VIEW = 1;

    public static final int EMPTY_VIEW = 2;

    public static final int LOADING_VIEW = 3;
    private static int  loadingViewResId=-1;
    private static int  emptyViewResId=-1;
    private static int  errorViewResId=-1;
    private static int  viewState=LOADING_VIEW;

    public static int getLoadingViewResId() {
        return loadingViewResId;
    }

    public static void setLoadingViewResId(int loadingViewResId) {
        MultiConfigure.loadingViewResId = loadingViewResId;
    }

    public static int getEmptyViewResId() {
        return emptyViewResId;
    }

    public static void setEmptyViewResId(int emptyViewResId) {
        MultiConfigure.emptyViewResId = emptyViewResId;
    }

    public static int getErrorViewResId() {
        return errorViewResId;
    }

    public static void setErrorViewResId(int errorViewResId) {
        MultiConfigure.errorViewResId = errorViewResId;
    }

    public static int getViewState() {
        return viewState;
    }

    public static void setViewState(int viewState) {
        MultiConfigure.viewState = viewState;
    }
}
