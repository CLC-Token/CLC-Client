package com.btd.wallet.mvp.view.wallet;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.fragment.BaseFragment;
import com.btd.wallet.dao.UpdateData;
import com.btd.wallet.home.dialog.ConfirmDialog;
import com.btd.wallet.home.ui.ProtocolActivity;
import com.btd.wallet.mvp.contract.wallet.CreateContract;
import com.btd.wallet.mvp.presenter.wallet.CreatePresenter;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.SecureRandomUtils;
import com.peersafe.base.crypto.ecdsa.Seed;
import com.peersafe.hdtsdk.inner.ZXWalletManager;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.OnClick;

import static com.btd.wallet.config.SPKeys.isWalletAgree;

/**
 * Description: 创建钱包   <br>
 * Author: cxh <br>
 * Date: 2019/7/2 11:23
 */
public class CreateFragment extends BaseFragment<CreateContract.IPresenter>
        implements CreateContract.IView {

    private static final int REQUEST_PROTOCOL = 1235;

    @BindView(R.id.sv_body)
    ScrollView svBody;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.btn_create)
    Button btnCreate;

    @BindView(R.id.sv_success)
    ScrollView svSuccess;
//    @BindView(R.id.lav_top)
//    LottieAnimationView lavTop;
@BindView(R.id.lav_top)
ImageView lavTop;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.ll_creatting)
    LinearLayout llCreatting;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.txt_private)
    TextView txtPrivate;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private boolean isCopied = false;
    private String mCopy;
    private List<String> words;

    public static CreateFragment newInstance() {
        Bundle args = new Bundle();
        CreateFragment fragment = new CreateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CreatePresenter(mActivity, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_create_wallet;
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected void initView() {
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnCreate.setClickable(isChecked);
            btnCreate.setEnabled(isChecked);
        });
        checkAgreement();
    }

    @OnClick(value = {R.id.btn_create, R.id.ll_check, R.id.ll_pwd, R.id.ll_confirm_pwd, R.id.btn_ok})
    public void onNewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                if (!cbAgree.isChecked()) {
                    showToast(R.string.wallet_toast_agree);
                } else {

                    mPresenter.btnCreate();
                }
                break;
            case R.id.ll_check:
                startActivityForResult(new Intent(mActivity, ProtocolActivity.class), REQUEST_PROTOCOL);
                break;
            case R.id.ll_pwd:
                MethodUtils.delayShowSoft(etPwd);
                break;
            case R.id.ll_confirm_pwd:
                MethodUtils.delayShowSoft(etConfirmPwd);
                break;
            case R.id.btn_ok:
//                if (isCopied) {
//                    pop();
//                } else {
//                    isCopied = true;
//                    btnOk.setText(R.string.wallet_success_success);
//                    ClipboardManager c = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
//                    c.setText(mCopy);
////                    showToast(R.string.string_copy_plate);
//                    new ConfirmDialog(mActivity, "",
//                            getStr(R.string.create_success_dialog),
//                            new UpdateData() {
//                        @Override
//                        public void update() {
//                        }
//
//                        @Override
//                        public void cancel() {
//                        }
//                    },false).show();
//                }
                EventBus.getDefault().post(WordBackupSuccessFragment.newInstance(words));
                break;
        }
    }

    /**
     * 已经同意了协议
     */
    private void checkAgreement() {
        cbAgree.setChecked(true);
        btnCreate.setClickable(cbAgree.isChecked());
        btnCreate.setEnabled(cbAgree.isChecked());
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

    @Override
    public EditText getEt() {
        return etPwd;
    }

    @Override
    public EditText getEtAgain() {
        return etConfirmPwd;
    }

    @Override
    public Button getBtnCreate() {
        return btnCreate;
    }

    @Override
    public void success(String address, String privateKey,List<String> words) {
        llCreatting.setVisibility(View.GONE);
        llSuccess.setVisibility(View.VISIBLE);
        String add = MethodUtils.getString(R.string.create_success_address,new Object[]{address});
        String pri = MethodUtils.getString(R.string.create_success_private,new Object[]{privateKey});
        mCopy = add + "\n" + pri;
        txtAddress.setText(add);
        txtPrivate.setText(pri);
        this.words=words;
//        lavTop.clearAnimation();
//        lavTop.setRepeatCount(0);
//        lavTop.setProgress(1);
        lavTop.setVisibility(View.GONE);
        ivTop.setVisibility(View.VISIBLE);
    }

    @Override
    public void creatting() {
        svBody.setVisibility(View.GONE);
        svSuccess.setVisibility(View.VISIBLE);
//        lavTop.setAnimation("wallet_creatting.json");
//        lavTop.setRepeatCount(10);
//        lavTop.playAnimation();
        mToolbar.setVisibility(View.INVISIBLE);
//        lavTop.setVisibility(View.GONE);
//        ivTop.setVisibility(View.VISIBLE);
//        Glide.with(mActivity).asGif().load(R.drawable.wallet_creatting).apply(new RequestOptions()).into(ivTop);
    }

    @Override
    public void createFail() {
        svBody.setVisibility(View.VISIBLE);
        svSuccess.setVisibility(View.GONE);
        mToolbar.setVisibility(View.VISIBLE);
    }

    public static void generateMnemonic(Context context) throws Exception {
        MnemonicCode mnemonicCode = new MnemonicCode(context.getAssets().open("english.txt"), null);
        List<String> words=new ArrayList<>();
        SecureRandom secureRandom = SecureRandomUtils.secureRandom();
        byte[] initialEntropy = new byte[16];//算法需要，必须是被4整除
        secureRandom.nextBytes(initialEntropy);
        List<String> wd = mnemonicCode.toMnemonic(initialEntropy);
        if (wd == null || wd.size() != 12)
            throw new RuntimeException("generate word error");
        else {
            words.clear();
            words.addAll(wd);
            LogUtils.d(words.toString());
        }
        byte[] seed =MnemonicCode.toSeed(words, "");
        System.out.println("seed: " + new BigInteger(1,seed).toString(16));
        Seed seed1=new Seed(seed);
        ZXWalletManager.getInstance().generateWalletByWord(seed1);

//        BigInteger pri1 = getDeterministicKey(words, null, "m/44'/194'/0'/0/0").getPrivKey();
//        System.out.println("pri1: " + pri1.toString(16));
//
//
//        BigInteger pri2 = getDeterministicKey(words,seed,"m/44'/194'/0'/0/0").getPrivKey();
//        System.out.println("pri2: " + pri2.toString(16));
//
//        byte[] pri3 = getDeterministicKey(words,null, "m/44'/194'/0'/0/0").getPrivKeyBytes();
//        System.out.println("pri3: " + new BigInteger(1,pri3).toString(16));
//
//        byte[] pri4 = getDeterministicKey(words, seed,"m/44'/194'/0'/0/0").getPrivKeyBytes();
//        System.out.println("pri4: " + new BigInteger(1,pri4).toString(16));
//
//        String path = "m/44'/" +
//                194 +
//                "'/0'/0/0";
//        byte[] pri5 = getDeterministicKey(words, null,path).getPrivKeyBytes();
//        System.out.println("pri5: " + new BigInteger(1,pri5).toString(16));
//
//        NetworkParameters networkParameters = TestNet3Params.get();
//        BigInteger pribtc = getDeterministicKey(words,null,"m/44'/0'/0'/0/0").getPrivKey();
//        ECKey ecKey = ECKey.fromPrivate(pribtc);
//
//        String publicKey = ecKey.getPublicKeyAsHex();
//        String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
//        System.out.println("");
       // String address = ecKey.toAddress(networkParameters).toString();

    }

    public static DeterministicKey getDeterministicKey(List<String> words, byte[] seed, String path) {
        DeterministicSeed deterministicSeed = new DeterministicSeed(words, seed, "", 0);
        DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
        return deterministicKeyChain.getKeyByPath(parsePath(path), true);
    }
    public static List<ChildNumber> parsePath(@Nonnull String path) {
        String[] parsedNodes = path.replace("m", "").split("/");
        List<ChildNumber> nodes = new ArrayList<>();

        for (String n : parsedNodes) {
            n = n.replaceAll(" ", "");
            if (n.length() == 0) continue;
            boolean isHard = n.endsWith("'");
            if (isHard) n = n.substring(0, n.length() - 1);
            int nodeNumber = Integer.parseInt(n);
            nodes.add(new ChildNumber(nodeNumber, isHard));
        }

        return nodes;
    }

}