package com.btd.wallet.mvp.adapter.base;

import android.view.View;

import com.btd.wallet.utils.CheckUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

/**
 * <p>创建: 廖林涛 2017/5/14 9:34
 * <p>版本: $Rev: 15400 $ $Date: 2019-03-12 20:28:50 +0800 (周二, 12 3月 2019) $
 */
public class OnESItemClickListener<T, K extends BaseQuickAdapter> extends OnItemClickListener {

    private IESItemClick mListener;

    public OnESItemClickListener(IESItemClick<T, K> listener) {
        mListener = CheckUtils.checkNotNull(listener);
    }

    /**
     * 不要覆盖此方法
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        boolean isEdit = false;
        if (isEdit) {
            mListener.onItemEditClick(adapter, view, position, adapter.getItem(position));
        } else {
            mListener.onItemClick(adapter, view, position, adapter.getItem(position));
        }

    }

    /**
     * 不要覆盖此方法
     */
    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        boolean isEdit = false;
        if (isEdit) {
            mListener.onItemEditLongClick(adapter, view, position, adapter.getItem(position));
        } else {
            mListener.onItemLongClick(adapter, view, position, adapter.getItem(position));
        }
    }

    /**
     * 不要覆盖此方法
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        boolean isEdit = false;
        if (isEdit) {
            mListener.onItemEditChildClick(adapter, view, position, adapter.getItem(position));
        } else {
            mListener.onItemChildClick(adapter, view, position, adapter.getItem(position));
        }
    }

    /**
     * 不要覆盖此方法
     */
    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        boolean isEdit = false;
        if (isEdit) {
            mListener.onItemEditChildLongClick(adapter, view, position, adapter.getItem(position));
        } else {
            mListener.onItemChildLongClick(adapter, view, position, adapter.getItem(position));
        }
    }


    public interface IESItemClick<T, K extends BaseQuickAdapter> {


        /**
         * 覆盖此方法
         */
        void onItemClick(K adapter, View view, int position, T item);

        void onItemEditClick(K adapter, View view, int position, T item);

        /**
         * 覆盖此方法
         */
        void onItemLongClick(K adapter, View view, int position, T item);

        void onItemEditLongClick(K adapter, View view, int position, T item);


        /*------------onItemChildClick start-------------*/
        void onItemHeaderChildClick(K adapter, View view, int position, T item);

        void onItemHeaderEditChildClick(K adapter, View view, int position, T item);

        /**
         * 覆盖此方法
         */
        void onItemChildClick(K adapter, View view, int position, T item);

        void onItemEditChildClick(K adapter, View view, int position, T item);

        /*------------onItemChildLongClick start-------------*/

        void onItemHeaderChildLongClick(K adapter, View view, int position, T item);

        void onItemHeaderEditChildLongClick(K adapter, View view, int position, T item);

        /**
         * 覆盖此方法
         */
        void onItemChildLongClick(K adapter, View view, int position, T item);

        void onItemEditChildLongClick(K adapter, View view, int position, T item);
    }
    /*------------废弃方法 start-------------*/

    /**
     * 没有回调,不要覆盖
     */
    @Override
    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
