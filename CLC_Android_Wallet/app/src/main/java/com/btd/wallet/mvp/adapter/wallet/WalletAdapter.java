package com.btd.wallet.mvp.adapter.wallet;

import android.view.View;

import com.btd.wallet.pure.R;
import com.btd.wallet.mvp.model.wallet.WalletModel;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Description: 我的钱包   <br>
 * Author: cxh <br>
 * Date: 2019/6/25 11:56
 */
public class WalletAdapter extends BaseQuickAdapter<WalletModel, BaseViewHolder> {

    /*当前选择的item*/
    private int curSelected = -1;
    /*是否显示checkbox*/
    private boolean showCheckbox;

    public WalletAdapter(int layoutId, List<WalletModel> data) {
        this(layoutId, data,false);
    }

    public WalletAdapter(int layoutId, List<WalletModel> data, boolean checkbox) {
        super(layoutId, data);
        showCheckbox = checkbox;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletModel item) {
        helper.setText(R.id.txt_name, item.getNickName())
                .setText(R.id.txt_address, item.getAddress().substring(item.getAddress().length()-4));

  //      if(item.isLoadBalance()){
//            helper.setText(R.id.txt_btd_value, item.getBalanceBtd()<0
//                            ? MethodUtils.getString(R.string.string_normal_value)
//                            : StringUtils.doubleFormat(item.getBalanceBtd()))
//                    .setText(R.id.txt_hdt_value, item.getBalanceHdt()<0
//                            ? MethodUtils.getString(R.string.string_normal_value)
//                            : StringUtils.doubleFormat(item.getBalanceHdt()))
            helper.setText(R.id.txt_btd_value, (item.getBalanceBtdBigDecimal()==null||MethodUtils.bigDecimalCompareToZero(item.getBalanceBtdBigDecimal())==-1)
                    ? MethodUtils.getString(R.string.string_normal_value)
                    : StringUtils.decimalToString(item.getBalanceBtdBigDecimal()))
                    .setText(R.id.txt_hdt_value,(item.getBalanceHdtBigDecimal()==null|| MethodUtils.bigDecimalCompareToZero(item.getBalanceHdtBigDecimal())==-1)
                            ? MethodUtils.getString(R.string.string_normal_value)
                            : StringUtils.decimalToString(item.getBalanceHdtBigDecimal()))
                    .setVisible(R.id.txt_freeze_btd,item.isFreezeBtd())
                    .setVisible(R.id.txt_freeze_hdt,item.isFreezeHdt())
                    .setTextColor(R.id.txt_btd_value,item.isFreezeBtd()
                            ?MethodUtils.getColor(R.color.warmGrey)
                            :MethodUtils.getColor(R.color.darkBlueGreyTwo))
                    .setTextColor(R.id.txt_hdt_value,item.isFreezeHdt()
                            ?MethodUtils.getColor(R.color.warmGrey)
                            :MethodUtils.getColor(R.color.darkBlueGreyTwo))
                    .setVisible(R.id.txt_btd_about, item.getBalanceBtdBigDecimal()!=null&&StringUtils.getDecimalPlaces(item.getBalanceBtdBigDecimal())>6)
                    .setVisible(R.id.txt_hdt_about, item.getBalanceHdtBigDecimal()!=null&&StringUtils.getDecimalPlaces(item.getBalanceHdtBigDecimal())>6);
            helper.getView(R.id.txt_freeze_btd).setVisibility(item.isFreezeBtd()? View.VISIBLE:View.GONE);
            helper.getView(R.id.txt_freeze_hdt).setVisibility(item.isFreezeHdt()? View.VISIBLE:View.GONE);
            helper.getView(R.id.txt_btd_about).setVisibility((item.getBalanceBtdBigDecimal()!=null&&StringUtils.getDecimalPlaces(item.getBalanceBtdBigDecimal())>6)? View.VISIBLE:View.GONE);
            helper.getView(R.id.txt_hdt_about).setVisibility((item.getBalanceHdtBigDecimal()!=null&&StringUtils.getDecimalPlaces(item.getBalanceHdtBigDecimal())>6)? View.VISIBLE:View.GONE);
//        }else {
//            helper.setText(R.id.txt_btd_value,  MethodUtils.getString(R.string.string_normal_value))
//                    .setText(R.id.txt_hdt_value, MethodUtils.getString(R.string.string_normal_value))
//                    .setVisible(R.id.txt_freeze_btd,false)
//                    .setVisible(R.id.txt_freeze_hdt,false)
//                    .setTextColor(R.id.txt_btd_value,MethodUtils.getColor(R.color.darkBlueGreyTwo))
//                    .setTextColor(R.id.txt_hdt_value,MethodUtils.getColor(R.color.darkBlueGreyTwo));
//            helper.getView(R.id.txt_freeze_btd).setVisibility(View.GONE);
//            helper.getView(R.id.txt_freeze_hdt).setVisibility(View.GONE);
//            helper.getView(R.id.txt_btd_about).setVisibility(View.GONE);
//            helper.getView(R.id.txt_hdt_about).setVisibility(View.GONE);
//        }
        if(showCheckbox){
            helper.getView(R.id.iv_arrow).setVisibility(View.GONE);
            helper.getView(R.id.cb_select).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.iv_arrow).setVisibility(View.VISIBLE);
            helper.getView(R.id.cb_select).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        if(showCheckbox){
            if(curSelected == positions){
                holder.setChecked(R.id.cb_select,true);
            }else {
                holder.setChecked(R.id.cb_select,false);
            }
        }
    }

    public void setCurSelected(int curSelected){
        this.curSelected = curSelected;
        notifyDataSetChanged();
    }

    public int getCurSelected(){
        return curSelected;
    }
}
