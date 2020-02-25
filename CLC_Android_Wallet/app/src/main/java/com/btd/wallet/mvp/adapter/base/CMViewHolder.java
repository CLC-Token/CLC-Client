package com.btd.wallet.mvp.adapter.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.btd.library.base.util.LogUtils;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * <preErcToBtd>
 * 创建: 廖林涛 2017/6/17 16:48;
 * 版本: $Rev: 13538 $ $Date: 2019-01-29 17:14:05 +0800 (周二, 29 一月 2019) $
 * </preErcToBtd>
 */
public class CMViewHolder extends BaseViewHolder {

    public CMViewHolder(View view) {
        super(view);
    }

    /**
     * @param currentPosition
     *         item在data中的位置
     *
     * @return <p> recy的LayoutManager需要是LinearLayoutManager或子类
     */
    public static CMViewHolder getViewHolder(RecyclerView recy, int currentPosition) {
        try {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recy.getLayoutManager();
            int findFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (currentPosition - findFirstVisibleItemPosition >= 0) {
                // 得到要更新的item的view
                View view = recy.getChildAt(currentPosition - findFirstVisibleItemPosition);
//                if(currentPosition - findFirstVisibleItemPosition == 0 && recy.getChildCount() > 1){
//                    view = recy.getChildAt(recy.getChildCount() - 1);
//                }
                if (null != recy.getChildViewHolder(view)) {
                    return (CMViewHolder) recy.getChildViewHolder(view);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }

    /** 清空recyclerview的item动画 */
    public static void closeDefaultAnimator(RecyclerView recyclerView) {
        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
    }

}
