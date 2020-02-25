package com.btd.wallet.utils.recyclerviewflexibledivider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.btd.wallet.pure.R;
import com.btd.wallet.utils.CMDimenUtils;
import com.btd.wallet.utils.ListSpacingDecoration;

/**
 * RecyclerView ItemDecoration的工具类
 * <preErcToBtd>
 * 创建: 廖林涛 2017/6/2 10:18;
 * 版本: $Rev: 13538 $ $Date: 2019-01-29 17:14:05 +0800 (周二, 29 一月 2019) $
 * </preErcToBtd>
 */
public class CMItemDecUtils {

    public static HorizontalDividerItemDecoration.Builder getHorizontalBuilder(Context context) {
        return new HorizontalDividerItemDecoration
                .Builder(context)
                .colorResId(R.color.divider);
    }

    /** 返回群组-用户列表的分割线 */
    public static HorizontalDividerItemDecoration getUsers(Context context) {
        return getHorizontalBuilder(context)
                .marginResId(R.dimen.dp_70, R.dimen.dp_15)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        switch (position) {
                            case 0:
                                return true;
                            default:
                                return false;
                        }
                    }
                })
                .build();
    }

    /** 返回分割线 */
    public static HorizontalDividerItemDecoration getDefault(Context context) {
        return getHorizontalBuilder(context)
                .marginResId(R.dimen.dp_70, R.dimen.dp_15)
                .build();
    }


    /** 空白间距为1dp的分割线 */
    public static RecyclerView.ItemDecoration getListSpacingDecoration(Context context) {
        return new ListSpacingDecoration(CMDimenUtils.dp2px(context, 1));
    }
}
