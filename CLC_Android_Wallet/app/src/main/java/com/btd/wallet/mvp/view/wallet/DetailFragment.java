package com.btd.wallet.mvp.view.wallet;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseFragment;
import com.btd.wallet.config.IntentKeys;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.dao.UpdateData;
import com.btd.wallet.event.EventTypeV2;
import com.btd.wallet.home.dialog.ConfirmDialog;
import com.btd.wallet.home.dialog.EditNameDialog;
import com.btd.wallet.home.dialog.PrivateKeyDialog;
import com.btd.wallet.home.popupwindow.CoinSelectDialog;
import com.btd.wallet.home.popupwindow.PayInputPwdDialog;
import com.btd.wallet.home.popupwindow.WalletDetailPop;
import com.btd.wallet.home.ui.WalletRecordFragment;
import com.btd.wallet.home.ui.WalletTransferFragment;

import com.btd.wallet.mvp.contract.wallet.DetailContract;
import com.btd.wallet.mvp.presenter.wallet.DetailPresenter;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 单个钱包详情页   <br>
 * Author: cxh <br>
 * Date: 2019/7/23 15:00
 */
public class DetailFragment extends BaseFragment<DetailContract.IPresenter>
        implements DetailContract.IView {

    @BindView(R.id.sv_body)
    ScrollView svBody;
    @BindView(R.id.iv_look)
    ImageView cbLook;
    @BindView(R.id.iv_btd)
    ImageView ivBtd;
    @BindView(R.id.iv_hdt)
    ImageView ivHdt;
    @BindView(R.id.txt_btd_freeze)
    TextView txtBtdFreeze;
    @BindView(R.id.txt_hdt_freeze)
    TextView txtHdtFreeze;
    @BindView(R.id.txt_btd_uinit)
    TextView txtBtdUinit;
    @BindView(R.id.txt_hdt_uinit)
    TextView txtHdtUinit;
    @BindView(R.id.txt_btd_value)
    TextView txtBtdValue;
    @BindView(R.id.txt_hdt_value)
    TextView txtHdtValue;
    @BindView(R.id.txt_account_name)
    TextView txtAccountName;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_star_address)
    TextView txtStarAddress;
    @BindView(R.id.ll_star_address)
    LinearLayout llStarAddress;
    @BindView(R.id.ll_btd)
    LinearLayout llBtd;
    @BindView(R.id.ll_hdt)
    LinearLayout llHdt;
    private boolean isChecked;
    private Dialog mDialog;
    private WalletDetailPop mDetailPop;

    public static DetailFragment newInstance(WalletConfig walletConfig) {
        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        args.putSerializable(SPKeys.walletInfo, walletConfig);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(int type) {
        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        args.putInt(IntentKeys.TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenter(mActivity, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_detail_wallet;
    }

    @Override
    protected int getMenuLayoutResId() {
        return R.menu.menu_wallet_detail;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_detail: {
                if (mDetailPop != null) {
                    mDetailPop.showPopupWindow(svBody);
                }
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        setTitle(R.string.wallet_detail_titile);
        initMenuPop();
    }

    @OnClick(value = {R.id.iv_scan, R.id.iv_look, R.id.txt_transfer, R.id.txt_record,
            R.id.ll_star_address, R.id.txt_address, R.id.iv_delete})
    public void onNewClick(View view) {
        switch (view.getId()) {
            case R.id.txt_record:
                start(WalletRecordFragment.newInstance(mPresenter.getCofig().getFromAddr()));
                break;
            case R.id.txt_transfer:
                if (mPresenter.getCofig().isFreezeBtd()) {
                    showToast(R.string.wallet_detail_freeze_btd);
                } else {
                    start(WalletTransferFragment.newInstance(mPresenter.getCofig(), WalletType.SDK_COIN_BTD));
                }
//                new CoinSelectDialog(mActivity, new CoinSelectDialog.OnPopItemClickListener() {
//                    @Override
//                    public void btd() {
//                        if (mPresenter.getCofig().isFreezeBtd()) {
//                            showToast(R.string.wallet_detail_freeze_btd);
//                        } else {
//                            start(WalletTransferFragment.newInstance(mPresenter.getCofig(), WalletType.SDK_COIN_BTD));
//                        }
//                    }
//
//                    @Override
//                    public void hdt() {
//                        if (mPresenter.getCofig().isFreezeHdt()) {
//                            showToast(R.string.wallet_detail_freeze_hdt);
//                        } else {
//                            start(WalletTransferFragment.newInstance(mPresenter.getCofig(), WalletType.SDK_COIN_HDT));
//                        }
//                    }
//                }).show();
                break;
            case R.id.iv_scan:
                showCodeDialog(mPresenter.getCode());
                break;
            case R.id.iv_delete:
                new ConfirmDialog(mActivity, MethodUtils.getString(R.string.delete_account),
                        getStr(R.string.wallet_detail_delete_dialog),
                        getStr(R.string.string_ok),
                        getStr(R.string.string_cancel), new UpdateData() {
                    @Override
                    public void update() {
                        deleteWallet();
                    }

                    @Override
                    public void cancel() {

                    }
                }).show();
                break;
            case R.id.ll_star_address:
            case R.id.txt_address:
                ClipboardManager manager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setText(mPresenter.getCofig().getFromAddr());
                showToast(R.string.copy_addr_success);
                break;
            case R.id.iv_look: {
                isChecked = !isChecked;
                if(isChecked){
                    llStarAddress.setVisibility(View.INVISIBLE);
                    txtAddress.setVisibility(View.VISIBLE);
                    cbLook.setImageResource(R.drawable.add_open);
                }else {
                    llStarAddress.setVisibility(View.VISIBLE);
                    txtAddress.setVisibility(View.INVISIBLE);
                    cbLook.setImageResource(R.drawable.add_close);
                }
                break;
            }
        }
    }

    private void initMenuPop() {
        WalletConfig config  = (WalletConfig) mBundle.getSerializable(SPKeys.walletInfo);;
        mDetailPop = new WalletDetailPop(mActivity,config);
        mDetailPop.setOnPopItemClickListener(new WalletDetailPop.OnPopItemClickListener() {
            @Override
            public void onRenameClick() {
                rename();
            }

            @Override
            public void onDeleteClicke() {
                new ConfirmDialog(mActivity, "",
                        getStr(R.string.wallet_detail_delete_dialog),
                        getStr(R.string.string_ok),
                        getStr(R.string.string_cancel), new UpdateData() {
                    @Override
                    public void update() {
                        deleteWallet();
                    }

                    @Override
                    public void cancel() {

                    }
                }).show();
            }

            @Override
            public void onRecode() {
                LogUtils.i("===click btn_record");
            }

            @Override
            public void lookPrivateKey() {
                lookPrivate();
            }

            @Override
            public void lookWord() {
                lookWordDialog();
            }
        });
    }

    @Override
    public void showInit(WalletConfig config) {
        llStarAddress.setVisibility(View.VISIBLE);
        txtAddress.setVisibility(View.INVISIBLE);
        txtStarAddress.setText(config.getFromAddr().substring(config.getFromAddr().length()-4));
        txtAddress.setText(config.getFromAddr());
        txtAccountName.setText(config.getNickName());
        if(config.isFreezeBtd()){
            ivBtd.setImageResource(R.drawable.account_logo_btd_block);
            txtBtdUinit.setTextColor(MethodUtils.getColor(R.color.grey));
            txtBtdValue.setTextColor(MethodUtils.getColor(R.color.grey));
            txtBtdFreeze.setVisibility(View.VISIBLE);
        }else {
            ivBtd.setImageResource(R.drawable.account_logo_btd);
            txtBtdUinit.setTextColor(MethodUtils.getColor(R.color.black20));
            txtBtdValue.setTextColor(MethodUtils.getColor(R.color.black20));
            txtBtdFreeze.setVisibility(View.GONE);
        }

        if(config.isFreezeHdt()){
            ivHdt.setImageResource(R.drawable.account_logo_hdt_block);
            txtHdtUinit.setTextColor(MethodUtils.getColor(R.color.grey));
            txtHdtValue.setTextColor(MethodUtils.getColor(R.color.grey));
            txtHdtFreeze.setVisibility(View.VISIBLE);
        }else {
            ivHdt.setImageResource(R.drawable.account_logo_hdt);
            txtHdtUinit.setTextColor(MethodUtils.getColor(R.color.black20));
            txtHdtValue.setTextColor(MethodUtils.getColor(R.color.black20));
            txtHdtFreeze.setVisibility(View.GONE);
        }
//        boolean showAboutBtd = StringUtils.getDecimalPlaces(Double.parseDouble(config.getBalanceBtd())) > 6;
//        boolean showAboutHdt = StringUtils.getDecimalPlaces(Double.parseDouble(config.getBalanceHdt())) > 6;
//        String btdValue = StringUtils.doubleFormat(Double.parseDouble(config.getBalanceBtd()));
//        String hdtValue = StringUtils.doubleFormat(Double.parseDouble(config.getBalanceHdt()));
        if(config.getBalanceBtd()==null||config.getBalanceBtd().equals("null")){
            config.setBalanceBtd("0");
        }
        if(config.getBalanceHdt()==null||config.getBalanceHdt().equals("null")){
            config.setBalanceHdt("0");
        }
        boolean showAboutBtd = StringUtils.getDecimalPlacesByString(config.getBalanceBtd()) > 6;
        boolean showAboutHdt = StringUtils.getDecimalPlacesByString(config.getBalanceHdt()) > 6;
        String btdValue = StringUtils.decimalToString(new BigDecimal(config.getBalanceBtd()==null?"0":config.getBalanceBtd()));
        String hdtValue = StringUtils.decimalToString(new BigDecimal(config.getBalanceHdt()==null?"0":config.getBalanceHdt()));


        txtBtdValue.setText(showAboutBtd
                ? (MethodUtils.getString(R.string.wallet_me_about)+btdValue)
                : btdValue);
        txtHdtValue.setText(showAboutHdt
                ? (MethodUtils.getString(R.string.wallet_me_about)+hdtValue)
                : hdtValue);

        llBtd.setVisibility(View.VISIBLE);
     //   llHdt.setVisibility(View.VISIBLE);
    }

    /**
     * 点击二维码，显示大图的二维码对话框
     */
    private void showCodeDialog(Bitmap code) {
        mDialog = new Dialog(mActivity, R.style.dialog_bottom_full);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_me_code, null);
        ImageView ivCode = (ImageView) view.findViewById(R.id.iv_code);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> mDialog.dismiss());
        ivCode.setImageBitmap(code);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    /**
     * 账户重命名
     */
    private void rename() {
        EditNameDialog dialog = new EditNameDialog(mActivity, mPresenter.getCofig().getNickName(), name -> {
            mPresenter.rename(name);
        });
        dialog.setMyTitle(getStr(R.string.wallet_detail_rename_title));
        dialog.setHint(txtAccountName.getText().toString());
        dialog.show();
    }


    /**
     * 删除米袋
     */
    private void deleteWallet() {
        onUMengEvent(EventTypeV2.DELETE_WALLET);
        WalletConfig config = DataSupport.where("fromAddr = ?", mPresenter.getCofig().getFromAddr())
                .findFirst(WalletConfig.class);
        if (config != null) {
            config.delete();
        }
        popTo(WalletFragment.class, false);
    }

    private void lookPrivate() {
        PayInputPwdDialog pop = new PayInputPwdDialog(mActivity, 1);
        pop.setOnPopItemClickListener(() -> {
            if(mPresenter != null){
                String pwd = pop.getPwd();
                String mPrivateKey = StringUtils.deCodePrivateNoLimit(mPresenter.getCofig().getPrivateKey(), pwd);
                if (TextUtils.isEmpty(mPrivateKey)) {
                    showToast(getStr(R.string.wallet_toast_error));
                    return;
                }
                new PrivateKeyDialog(mActivity, mPresenter.getCofig().getFromAddr(), mPrivateKey).show();
            }
        });
        pop.show();
    }

    private void lookWordDialog() {
        PayInputPwdDialog pop = new PayInputPwdDialog(mActivity, 2);
        pop.setOnPopItemClickListener(() -> {
            if(mPresenter != null){
                String pwd = pop.getPwd();
                String mPrivateKey = StringUtils.deCodePrivateNoLimit(mPresenter.getCofig().getWords(), pwd);
                if (TextUtils.isEmpty(mPrivateKey)) {
                    showToast(getStr(R.string.wallet_toast_error));
                    return;
                }
                String words=mPrivateKey;
                String[] str= words.split(" ");
                List<String> wordList=new ArrayList<>();
                for(int i=0;i<str.length;i++){
                    wordList.add(str[i]);
                }
                EventBus.getDefault().post(WordBackupSuccessFragment.newInstance(wordList));
            }
        });
        pop.show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog = null;
        }
    }
}
