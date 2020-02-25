package com.btd.wallet.home.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.event.EventTypeV2;
import com.btd.wallet.manager.hdtsdk.ConnectCallBack;
import com.btd.wallet.manager.hdtsdk.ConnectObserver;
import com.btd.wallet.manager.hdtsdk.HDTManage;

import com.btd.wallet.utils.KeyboardUtils;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.PDialogUtil;
import com.btd.wallet.utils.StringUtils;
import com.btd.wallet.utils.TransferPDialogUtil;
import com.btd.wallet.utils.ViewClickUtil;
import com.peersafe.hdtsdk.api.HDTSdkApi;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * $Author: xhunmon
 * $Date: 2018-01-09
 * $Description: 转账页面：
 */

public class WalletTransferFragment extends BaseSupportFragment {

    public static final int SCAN_CODE = 101;

    /*手续费的提示*/
    @BindView(R.id.txt_expect_hint)
    TextView txtExpectHint;
    /*本账户的地址*/
    @BindView(R.id.txt_unit)
    TextView mTvUnit;
    @BindView(R.id.txt_unit_2)
    TextView mTvUnit2;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.txt_about)
    TextView txtAbout;
    /*要转账的目标地址*/
    @BindView(R.id.et_addr)
    EditText mEtAddr;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    /*转账金额*/
    @BindView(R.id.et_money)
    EditText mEtMoney;
    /*点击进行二维码扫描，返回一个二维码地址*/
    @BindView(R.id.iv_scan)
    ImageView mIvScan;
    /*确认，弹出要转账确认信息的对话框*/
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    private HDTManage mHDTManage = HDTManage.getInstance();
    /*本地获取的key，加密后的*/
    private String mLocalPrivateKey;
    private Dialog mDialog;
    private String mFromAddr;
    private Runnable mShowLoadingTask;
    /*转账的手续费，默认为0，*/
    private String mExpect = "0";
    private String mTotalMoney;
    private boolean isSdkTransfer = false;
    private String mToAddr;
    private String mPrivateKey;
    private String mBalance;
    private int coinType = 0;
    private ConnectObserver mObserver;

    public static WalletTransferFragment newInstance(WalletConfig mConfig, int coinType) {
        Bundle args = new Bundle();
        args.putSerializable(SPKeys.walletInfo, mConfig);
        args.putInt(SPKeys.coinType, coinType);
        WalletTransferFragment fragment = new WalletTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WalletTransferFragment newInstance(WalletConfig mConfig, String toAddreess, int coinType) {
        Bundle args = new Bundle();
        args.putSerializable(SPKeys.walletInfo, mConfig);
        args.putString(SPKeys.walletScanAddr, toAddreess);
        args.putInt(SPKeys.coinType, coinType);
        WalletTransferFragment fragment = new WalletTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet_transfer;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected void initView() {
        setTitle(R.string.wallet_transfer_title);
        setEditTextFilters(mEtMoney);
        WalletConfig walletConfig = (WalletConfig) mBundle.getSerializable(SPKeys.walletInfo);
        String scanAdrr = mBundle.getString(SPKeys.walletScanAddr);
        coinType = mBundle.getInt(SPKeys.coinType);
        if (walletConfig == null) {
            pop();
            return;
        }
        LogUtils.i("==walletConfig： " + walletConfig.toString() + ", coinType: " + coinType + ", scanAdrr: " + scanAdrr);
        walletConfig = DataSupport.where("fromAddr = ?", walletConfig.fromAddr).findFirst(WalletConfig.class);
        setTitle(walletConfig.nickName);
        mFromAddr = walletConfig.fromAddr;
        mBalance = coinType == WalletType.SDK_COIN_HDT ? walletConfig.balanceHdt : walletConfig.balanceBtd;
        txtAbout.setVisibility(StringUtils.getDecimalPlaces(new BigDecimal(mBalance)) > 6 ? View.VISIBLE : View.GONE);
      //  mBalance = StringUtils.doubleFormat(Double.parseDouble(mBalance));
        mBalance=StringUtils.decimalToString(new BigDecimal(mBalance));
        mTvUnit.setText(coinType == WalletType.SDK_COIN_HDT ? R.string.string_hdt : R.string.string_btd);
        mTvUnit2.setText(coinType == WalletType.SDK_COIN_HDT ? R.string.string_hdt : R.string.string_btd);
        if (!TextUtils.isEmpty(scanAdrr)) {
            mEtAddr.setText(scanAdrr);
        }
        mBalance = TextUtils.isEmpty(mBalance) ? "0" : mBalance;
        mTvBalance.setText(mBalance);
        if (mHDTManage.isConnected()) {
            getSdkFee();
        } else {
            mObserver = connect -> {
                if (connect) {
                    getSdkFee();
                }
            };
            mHDTManage.attach(mObserver);
        }
        WalletConfig config = DataSupport.where("fromAddr = ?", mFromAddr)
                .findFirst(WalletConfig.class);
        if (config != null) {
            mLocalPrivateKey = config.privateKey;
        }
        LogUtils.i("==mLocalPrivateKey： " + mLocalPrivateKey);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(value = {R.id.iv_scan, R.id.btn_confirm, R.id.ll_address, R.id.ll_amount, R.id.fl_remark})
    public void onClick(View view) {
        if (ViewClickUtil.isFastDoubleClick(view.getId())) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_scan:
                RxPermissions rxPermission = new RxPermissions(this);
                rxPermission.requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    Intent intent = new Intent(mActivity, CaptureActivity.class);
                                    intent.putExtra(SPKeys.fromType, CaptureActivity.FROM_TRANSFER);
                                    startActivityForResult(intent, SCAN_CODE);
                                    LogUtils.d(permission.name + " is granted.");
                                } else {
                                    LogUtils.d(permission.name + " is denied.");
                                    showToast(getStr(R.string.camear_is_forbid));
                                }
                            }
                        });
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            case R.id.ll_address:
                MethodUtils.delayShowSoft(mEtAddr);
                break;
            case R.id.ll_amount:
                MethodUtils.delayShowSoft(mEtMoney);
                break;
            case R.id.fl_remark:
                MethodUtils.delayShowSoft(mEtRemark);
                break;
        }
    }

    /**
     * 确认转账后，展示要确认转账的对话框
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void confirm() {
        String addr = mEtAddr.getText().toString().trim();
        String money = mEtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(addr)) {
            showToast(getStr(R.string.wallet_transfer_addr_toast));
            return;
        }
        if (!HDTManage.getInstance().isLegalAddress(addr)) {
            showToast(getStr(R.string.apply_invaild_eth_address));
            return;
        }

        if (TextUtils.isEmpty(money)) {
            showToast(getStr(R.string.wallet_transfer_money_toast));
            return;
        }

        if (mFromAddr.equals(addr)) {
            showToast(getStr(R.string.wallet_transfer_failed_to_yourself));
            return;
        }

        if(Double.valueOf(money) <= 0){
            showToast(R.string.wallet_transfer_amount_zero);
            return;
        }
        onUMengEvent(EventTypeV2.TRANSFER);
        /*判断够不够钱转账*/
        BigDecimal bgA = new BigDecimal(String.valueOf(mExpect));
        BigDecimal bgB = new BigDecimal(String.valueOf(money));
        BigDecimal add = bgA.add(bgB);

        BigDecimal balanceDecimal=new BigDecimal(mBalance);
       // if (Double.parseDouble(mBalance) >= add.doubleValue()) {
        if (balanceDecimal.compareTo(add)>-1) {
            showDialog();
        } else {
            showToast(getStr(R.string.wallet_transfer_less));
        }
    }


    private void showDialog() {
        mDialog = new Dialog(mActivity, R.style.dialog_bottom_full);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.wallet_transfer_dialog, null);
        initDialog(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        mDialog.show();
    }

    /**
     * 初始化底部弹出对话框
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initDialog(View view) {
        /*关闭对话框*/
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        });

        /*转出地址，就是本地址*/
        TextView tvAddrOuput = (TextView) view.findViewById(R.id.tv_addr_output);
        /*转入地址*/
        TextView tvAddrInput = (TextView) view.findViewById(R.id.txt_to);
        /*转出金额*/
        TextView tvAmout = (TextView) view.findViewById(R.id.tv_amount);
        /*手续费，注意：此处展现手续费需调用btrsdk接口*/
        TextView tvExpect = (TextView) view.findViewById(R.id.tv_expect);
        /*转出的总金额（转出金额+手续费）*/
        TextView tvTotal = (TextView) view.findViewById(R.id.tv_total);
        TextView tvUnit3 = (TextView) view.findViewById(R.id.txt_unit_3);
        if (coinType == WalletType.SDK_COIN_HDT) {
            ((TextView) view.findViewById(R.id.txt_unit_1)).setText(R.string.string_hdt);
            ((TextView) view.findViewById(R.id.txt_unit_2)).setText(R.string.string_hdt);
            ((TextView) view.findViewById(R.id.txt_unit_3)).setText(R.string.string_hdt);
        } else {
            ((TextView) view.findViewById(R.id.txt_unit_1)).setText(R.string.string_btd);
            ((TextView) view.findViewById(R.id.txt_unit_2)).setText(R.string.string_btd);
            ((TextView) view.findViewById(R.id.txt_unit_3)).setText(R.string.string_btd);
        }

        /*用户输入的密码，转账时需要通过用户输入的密码获取原始的米袋私钥*/
        EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
        Button btConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btConfirm.setEnabled(false);
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btConfirm.setEnabled(s.length() >= 8);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mToAddr = mEtAddr.getText().toString().trim();
        String money = mEtMoney.getText().toString();
//        BigDecimal expect = TextUtils.isEmpty(mExpect) ? new BigDecimal("0") : new BigDecimal(mExpect);
//        BigDecimal money1 = TextUtils.isEmpty(money) ? new BigDecimal("0") :new BigDecimal(money);
        mTotalMoney = StringUtils.addWallet(mExpect, money, true);
        LogUtils.i("==money: " + money + ", mTotalMoney: " + mTotalMoney);
        tvAddrOuput.setText(mFromAddr);
        tvAddrInput.setText(mToAddr);
        tvAmout.setText(StringUtils.killZero(Double.parseDouble(money)));
        tvExpect.setText(mExpect);
        tvTotal.setText(mTotalMoney);

        btConfirm.setOnClickListener(v -> {
            if (ViewClickUtil.isFastDoubleClick(btConfirm.getId())) {
                return;
            }
            String pwd = etPwd.getText().toString();
            mPrivateKey = StringUtils.deCodePrivateNoLimit(mLocalPrivateKey, pwd);
            LogUtils.i("mPrivateKey: " + mPrivateKey);
            if (TextUtils.isEmpty(mPrivateKey)) {
                showToast(getStr(R.string.wallet_toast_error));
                mDialog.cancel();
                KeyboardUtils.hideKeyboard(mActivity, etPwd);
                return;
            }

            if (mHDTManage.isConnected()) {
                isSdkTransfer = true;
                TransferPDialogUtil.startProgress(mActivity, "", null);
                LogUtils.i("==mEtMoney.getText().toString(): " + mEtMoney.getText().toString());
                transfer();
            } else {

                mHDTManage.setSdkConnectedWithBlock(mActivity, true, new ConnectCallBack() {
                    @Override
                    public void hasConnect(boolean connect) {
                        if(connect){
                            isSdkTransfer = true;
                            TransferPDialogUtil.startProgress(mActivity, "", null);
                            transfer();
                        }
                    }
                });
            }
        });
    }


    /**
     * 停止并移除对话框
     */
    private void stopDialog() {
        TransferPDialogUtil.stopProgress();
    }

    /**
     * edittext只能输入数字和小数点，且小数点后只能输入六位
     *
     * @param money
     */
    private void setEditTextFilters(EditText money) {
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 6) {//0.xxxx
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 7);
                        money.setText(s);
                        money.setSelection(s.length());
                        showToast(getStr(R.string.wallet_transfer_length_error));
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {//判断输入："."
                    s = "0" + s;
                    money.setText(s);
                    money.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {//输入"05"格式时
                        money.setText(s.subSequence(0, 1));
                        money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 转账成功
     */
    private void toTransferSuccess() {
        mDialog.dismiss();
        startWithPop(WalletTransferSuccessFragment.newInstance());
    }

    /**
     * 转账失败
     */
    private void toTransferFailed() {
        startWithPop(WalletTransferFailedFragment.newInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        if (mObserver != null) {
            mHDTManage.detach(mObserver);
        }
    }

    private  void transfer() {
        String remark = mEtRemark.getText().toString();
        mHDTManage.transferCurrency(HDTSdkApi.CoinType.CLC,
                mPrivateKey, mToAddr,
                mEtMoney.getText().toString(),
                remark,
                "",
                (code, message, transferInfo) -> {
                    if(txtExpectHint != null) {
                        LogUtils.i("code: " + code + ", message:" + message
                                + ",transferInfo: " + (transferInfo == null ? "" : transferInfo.toString()));
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopDialog();
                                if (code == 0) {//转账成功，跳转到成功页面
                                    toTransferSuccess();
                                } else {
                                    toTransferFailed();
                                }
                            }
                        });

                    }
                });
    }

    /**
     * 获取余额
     * 注意：如果跟getSdkFee()并发请求会有一个获取不要
     */
    private void getBalance() {
        mHDTManage.getIssueCurrencyBalance(HDTSdkApi.CoinType.CLC, mFromAddr, (code, message, balance) -> {
            LogUtils.i("code: " + code + ", message: " + message + ", balance: " + balance);
            HDTManage.getHandler().post(() -> {
                if(txtExpectHint != null) {
                    if (code == 0 || code == 3) {
                      //  mBalance = StringUtils.doubleFormat(Double.parseDouble(balance));
                        mBalance=StringUtils.decimalToString(new BigDecimal(balance));
                        mTvBalance.setText(mBalance);
                      //  txtAbout.setVisibility(StringUtils.getDecimalPlaces(Double.parseDouble(balance)) > 6 ? View.VISIBLE : View.GONE);
                        txtAbout.setVisibility(StringUtils.getDecimalPlaces(new BigDecimal(balance)) > 6 ? View.VISIBLE : View.GONE);
                    } else {
                        LogUtils.i(mFromAddr + "获取余额失败");
                        showToast(getStr(R.string.wallet_detail_balance_failed));
                    }
                    if (PDialogUtil.isShowing()) {
                        PDialogUtil.stopProgress();
                    }
                }
            });
        });
    }

    /**
     * 获取sdk转账手续费
     * 注意：如果跟getBalance()并发请求会有一个获取不要
     */
    private void getSdkFee() {
        mHDTManage.getTransferFee((code, message, transferFee) -> {
            LogUtils.i("code: " + code + ", message: " + message + ", transferFee: " + transferFee);
            HDTManage.getHandler().post(() -> {
                if(txtExpectHint != null){//页面还在
                    if (code == 0) {
                        mExpect = TextUtils.isEmpty(transferFee) ? null : transferFee;
                        LogUtils.d("mTvBalance==null？ "+(mTvBalance ==null));
                        String text = MethodUtils.getString(R.string.wallet_transfer_expect_money_pre)
                                + mExpect + MethodUtils.getString(coinType == WalletType.SDK_COIN_HDT
                                ? R.string.wallet_transfer_expect_money_suffix_hdt
                                : R.string.wallet_transfer_expect_money_suffix_btd);
                        LogUtils.d("text: "+text);
                        txtExpectHint.setText(text);
                        getBalance();
                    } else {
                        showToast(R.string.string_get_fee_fail);
                        pop();
                    }
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("==onActivityResult resultCode: " + resultCode);
        if (data != null) {
            String scanAdrr = data.getStringExtra(SPKeys.walletScanAddr);
            if (!TextUtils.isEmpty(scanAdrr)) {
                mEtAddr.setText(scanAdrr);
            }
        }
    }
}