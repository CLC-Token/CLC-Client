package com.btd.wallet.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btd.wallet.mvp.model.wallet.BaseRecordData;
import com.btd.wallet.mvp.model.wallet.RecordItem;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.WalletType;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * $Author: xhunmon
 * $Date: 2018-01-20
 * $Description:
 */
public class WalletRecordAdapter extends BaseQuickAdapter<BaseRecordData, BaseViewHolder> {

    public static final int ITEM_TYPE_NOMAL = 1;
    public static final int ITEM_TYPE_TIME = 2;
    private List<BaseRecordData> mData;

    public WalletRecordAdapter(Context context, List<BaseRecordData> data) {
        super(data);
        mData = data == null ? new ArrayList<>() : data;
        mContext = context;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mData.get(position).getDataType();
    }

    //返回数据集合的大小
    @Override
    public int getItemCount () {
        return mData.size();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TIME)
            return new TimeViewHolder(getItemView(R.layout.wallet_record_item_time, parent));
        else if (viewType == ITEM_TYPE_NOMAL)
            return new NormalViewHolder(getItemView(R.layout.wallet_record_item_normal, parent));
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseRecordData itemData) {
        if (helper instanceof TimeViewHolder) {
            String time = (String) itemData.getT();
            helper.setText(R.id.tv_date,time);
        }

        if(helper instanceof NormalViewHolder){
            RecordItem item = (RecordItem) itemData.getT();
            boolean isIn = item.getType().equals(mContext.getString(R.string.wallet_record_item_in));
            helper.setText(R.id.tv_date,item.getDate())
                    .setText(R.id.tv_type,item.getType())
                    .setText(R.id.fee,isIn?"0.00":item.getFee())
                    .setText(R.id.tv_unit,item.getCoinType()== WalletType.SDK_COIN_HDT
                            ? R.string.string_hdt : R.string.string_btd)
                    .setText(R.id.tv_fee_unit,item.getCoinType()== WalletType.SDK_COIN_HDT
                            ? R.string.string_hdt : R.string.string_btd)
                    .setText(R.id.tv_balance,isIn?"+"+item.getBalance():"-"+item.getBalance())
//                    .setText(R.id.tv_addr, item.getAddr())
                    .setText(R.id.tv_hash,item.getHash())
                    .setVisible(R.id.v_line,item.isLineShow())
                    .addOnClickListener(R.id.ll_address)
                    .addOnClickListener(R.id.ll_hash)
                    .setImageResource(R.id.iv_type, item.getCoinType()== WalletType.SDK_COIN_HDT
                            ? R.drawable.ex_hdt_ico : R.drawable.ex_ist_ico);
            if(TextUtils.isEmpty(item.getSystemAddressDesc())){
                helper.setText(R.id.tv_addr, item.getAddr());
            }else {
                int length = item.getAddr().length();
                if(length > 4){
                    String allAddr = String.format("%s（****%s）",item.getSystemAddressDesc(),item.getAddr().substring(length-4));
                    helper.setText(R.id.tv_addr, allAddr);
                }else {
                    helper.setText(R.id.tv_addr, item.getAddr());
                }
            }
        }
    }


    public void updateData(List<BaseRecordData> data){
        mData = data;
        notifyDataSetChanged();
    }

    public class NormalViewHolder extends BaseViewHolder {
        /*两种类型的图片，转出和转入*/
        public ImageView mIvType;
        /*交易的日期，例如：2018.01.16 12:00*/
        public TextView mTvDate;
        /*交易的类型*/
        public TextView mTvType;
        /*转账金额*/
        public TextView mTvBalance;
        /*账号的米袋地址*/
        public TextView mTvAddr;
        public TextView mTvHash;
        /*要判断时候当天的最后一条数据，进行显示或隐藏*/
        public View mVLine;

        public NormalViewHolder(View itemView) {
            super(itemView);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTvType = (TextView) itemView.findViewById(R.id.tv_type);
            mTvBalance = (TextView) itemView.findViewById(R.id.tv_balance);
            mTvAddr = (TextView) itemView.findViewById(R.id.tv_addr);
            mTvHash = (TextView) itemView.findViewById(R.id.tv_hash);
            mVLine = itemView.findViewById(R.id.v_line);
        }
    }

    public class TimeViewHolder extends BaseViewHolder {
        public TextView mTvDate;

        public TimeViewHolder(View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
