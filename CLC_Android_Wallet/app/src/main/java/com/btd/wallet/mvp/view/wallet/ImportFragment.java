package com.btd.wallet.mvp.view.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseFragment;

import com.btd.wallet.config.Constants;
import com.btd.wallet.config.IntentKeys;
import com.btd.wallet.home.ui.ProtocolActivity;
import com.btd.wallet.mvp.contract.wallet.ImportContract;
import com.btd.wallet.mvp.presenter.wallet.ImportPresenter;

import com.btd.wallet.utils.MethodUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.btd.wallet.config.SPKeys.isWalletAgree;

/**
 * Description:  导入钱包  <br>
 * Author: cxh <br>
 * Date: 2019/7/3 11:08
 */
public class ImportFragment extends BaseFragment<ImportContract.IPresenter>
        implements ImportContract.IView {

    public static final int CONTRACT = 1;
    private static final int REQUEST_PROTOCOL = 1236;
    //私钥导入
    private int currentType=0;


    @BindView(R.id.et_private)
    EditText etPrivate;

    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.import_line1)
    View import_line1;
    @BindView(R.id.import_line2)
    View import_line2;

    @BindView(R.id.import_tx1)
    TextView import_tx1;

    @BindView(R.id.import_tx2)
    TextView import_tx2;


    public static ImportFragment newInstance() {
        Bundle args = new Bundle();
        ImportFragment fragment = new ImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImportFragment newInstance(int type) {
        Bundle args = new Bundle();
        ImportFragment fragment = new ImportFragment();
        args.putInt(IntentKeys.TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImportPresenter(mActivity, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_import_wallet;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected void initView() {
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnOk.setEnabled(isChecked));
        checkAgreement();
    }

    @OnClick(value = {R.id.btn_ok, R.id.ll_pwd, R.id.ll_confirm_pwd, R.id.fl_private,
            R.id.ll_check, R.id.txt_what_pri,R.id.import1,R.id.import2})
    public void onNewClick(View view) {
        switch (view.getId()) {
            case R.id.fl_private:
                MethodUtils.delayShowSoft(etPrivate);
                break;
            case R.id.btn_ok:
                mPresenter.btnOk();
                break;
            case R.id.ll_pwd:
                MethodUtils.delayShowSoft(etPwd);
                break;
            case R.id.ll_confirm_pwd:
                MethodUtils.delayShowSoft(etConfirmPwd);
                break;
            case R.id.ll_check:
                startActivityForResult(new Intent(mActivity, ProtocolActivity.class), REQUEST_PROTOCOL);
                break;
            case R.id.txt_what_pri:
             //   EventBus.getDefault().postSticky(BaseWebFragment.newInstance(Constants.URL_HELP,MethodUtils.getString(R.string.question)));
                break;
            case R.id.import1:
                importPrivateKey();
                break;
            case R.id.import2:
                importWord();
                break;
        }
    }

    private void importPrivateKey(){
        currentType=0;
        import_tx1.setTextColor(Color.parseColor("#007CFF"));
        import_line1.setVisibility(View.VISIBLE);
        import_tx2.setTextColor(Color.parseColor("#777777"));
        import_line2.setVisibility(View.GONE);
        etPrivate.setHint(MethodUtils.getString(R.string.wallet_import_paste_pri));
    }

    private void importWord(){
        currentType=1;
        import_tx2.setTextColor(Color.parseColor("#007CFF"));
        import_line2.setVisibility(View.VISIBLE);
        import_tx1.setTextColor(Color.parseColor("#777777"));
        import_line1.setVisibility(View.GONE);
        etPrivate.setHint(MethodUtils.getString(R.string.word_import_tips));
    }

    @Override
    public EditText getEt() {
        return etPwd;
    }

    @Override
    public EditText getEtAgain() {
        return etConfirmPwd;
    }

    @Override
    public EditText getEtPrivate() {
        return etPrivate;
    }

    @Override
    public Button getBtnOk() {
        return btnOk;
    }

    @Override
    public int currentType() {
        return currentType;
    }

    @Override
    public void finish(boolean toMainFragment) {
        if (toMainFragment) {
            popTo(WalletFragment.class, false);
        } else {
            pop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PROTOCOL) {
            if (data != null) {
                if (data.getBooleanExtra(isWalletAgree, false)) {
                    checkAgreement();
                }
            }
        }
    }

    /**
     * 已经同意了协议
     */
    private void checkAgreement() {
        cbAgree.setChecked(true);
        btnOk.setEnabled(cbAgree.isChecked());
    }
}
