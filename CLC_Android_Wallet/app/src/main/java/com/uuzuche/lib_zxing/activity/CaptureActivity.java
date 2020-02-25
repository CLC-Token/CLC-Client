package com.uuzuche.lib_zxing.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.config.Constants;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.config.WalletType;
import com.btd.wallet.core.WorkApp;

import com.btd.wallet.home.ui.QRScanTipUI;

import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StatusBarUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends FullScreenActivity {

    public static final int FROM_HOME = 0;
    public static final int FROM_TRANSFER = 1;
    public static final int FROM_DIG_WEB = 2;
    public static final int FROM_REG_CODE = 3;      /*注册时扫描邀请码*/

    @BindView(R.id.img_right2)
    ImageButton img_right2;

    @BindView(R.id.txt_back)
    TextView txt_back;

    @BindView(R.id.txt_title)
    TextView txt_title;

    public static final String LOGIN_CODE_PROTOCOL = "http://www.cume.cc?qtCode=";
    public static final int SCAN_LOGIN = 20212;
    public static final int SELECT_IMAGE = 20213;
    private int mFormType = -1;

    @Override
    protected int getContentView() {
        return R.layout.camera;
    }

    @Override
    protected void initView() {
//        if(WorkApp.firstCamera){
//            WorkApp.firstCamera = false;
//            finish();
//        }
        StatusBarUtil.setColor(this, MethodUtils.getColor(R.color.transparent),0);
        mFormType = getIntent().getIntExtra(SPKeys.fromType, -1);
        if(mFormType == -1){
            finish();
            return;
        }
        LogUtils.d("mFormType: "+mFormType);
    }

    @Override
    protected void initData() {
        try {
            if(mFormType == -1){
                finish();
                return;
            }
            CaptureFragment captureFragment = new CaptureFragment();
            captureFragment.setAnalyzeCallback(analyzeCallback);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            checkCode(result);

        }

        @Override
        public void onAnalyzeFailed() {
            MethodUtils.showToast(WorkApp.workApp, MethodUtils.getString(R.string.parse_qr_code_fail));
            CaptureActivity.this.finish();
        }
    };

    private void checkCode(String result){
        try {
            /*xhunmon180112: 如果扫描结果得到 WalletType.strWalletPre开头的就取后面，跳转到btr转账页面*/
            LogUtils.i("mFormType->"+mFormType+"; result->"+result);
            if(!TextUtils.isEmpty(result)){
                //从挖矿里面扫码进入，但是二维码不符合规则
                if(mFormType == FROM_REG_CODE){
                    String codeContent = "code=";
                    if(result.contains(codeContent)){
                        int start = result.indexOf(codeContent) + codeContent.length();
                        if(start < result.length()){
                            Intent intent = new Intent();
                            intent.putExtra(SPKeys.scanCode, result.substring(start));
                            setResult(0,intent);
                            finish();
                            return;
                        }
                    }
                }else if(mFormType == FROM_DIG_WEB){
                    if(result.startsWith(Constants.btdwalletRsp)){
//                        String url = result.substring(Constants.btdwalletRsp.length());
//                        EventBus.getDefault().postSticky(BandFragment.newInstance(url));
//                        finish();
                        return;
                    }
                }else if(mFormType == FROM_TRANSFER){
                    if (result.startsWith(WalletType.strWalletPre)){
                        Intent intent = new Intent();
                        intent.putExtra(SPKeys.walletScanAddr, result.substring(8));
                        setResult(0,intent);
                        finish();
                        return;
                    }
                }else if(mFormType == FROM_HOME){
                    //从主页扫码，万能
                    if (result.startsWith(WalletType.strWalletPre)){
//                        EventBus.getDefault().postSticky(WalletSelectFragment.newInstance(result.substring(8)));
//                        finish();
                        return;
                    }else if(result.startsWith(WalletType.strIstPayPre)){ //支付
//                        EventBus.getDefault().postSticky(PayBtrContentFragment.newInstance(
//                                result.substring(WalletType.strIstPayAllPre.length()), WalletType.SDK_COIN_BTD));
//                        finish();
                        return;
                    }else if(result.startsWith(WalletType.strHdtPayPre)){ //支付
//                        EventBus.getDefault().postSticky(PayBtrContentFragment.newInstance(
//                                result.substring(WalletType.strHdtPayAllPre.length()),WalletType.SDK_COIN_HDT));
//                        finish();
                        return;
                    }else if(result.startsWith(Constants.oauth2)){
//                        String url = result.substring(Constants.oauth2.length());
//                        EventBus.getDefault().postSticky(new AuthorEvent(url));
//                        finish();
                        return;
                    }else if(result.startsWith(Constants.btdwalletRsp)){
//                        String url = result.substring(Constants.btdwalletRsp.length());
//                        EventBus.getDefault().postSticky(BandFragment.newInstance(url));
//                        finish();
                        return;
                    }else if(result.startsWith(Constants.scanLogin)){
//                        String url = result.substring(Constants.scanLogin.length());
//                        EventBus.getDefault().postSticky(new ScanLoginEvent(url));
//                        finish();
                        return;
                    }
                }
            }

            Intent intent = new Intent(mActivity, QRScanTipUI.class);
            intent.putExtra(SPKeys.DATA, result);
            startActivity(intent);
            CaptureActivity.this.finish();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    @OnClick(value = {R.id.txt_back, R.id.img_right2, R.id.layout_by_album})
    public void onNewClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.img_right2:
                    /* 闪光灯的开关 */
                    if (img_right2.isSelected()) {
                        CodeUtils.isLightEnable(false);
                        img_right2.setSelected(false);
                    } else {
                        CodeUtils.isLightEnable(true);
                        img_right2.setSelected(true);
                    }
                    break;
                case R.id.txt_back:
                    finish();
                    break;
                case R.id.layout_by_album:{
                    //从相册选择
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    startActivityForResult(intent,SELECT_IMAGE);
                    break;
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SCAN_LOGIN){
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }else if(requestCode == SELECT_IMAGE){
            if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                final ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);  //选择的图片
                if(images ==null || images.size() ==0){
                    MethodUtils.showToast(this,getStr(R.string.please_select_picture));
                }else{
//                    PDialogUtil.startProgress(this,getStr(R.string.decoding_scan));
                    handler.postDelayed(() -> {
                        Result result = scanningImage(images.get(0).path ,images.get(0).name);
                        if (result != null) {
                            checkCode(result.getText());
                        } else {
                            MethodUtils.showToast(mActivity,getStr(R.string.parse_qr_code_fail));
                        }
//                        PDialogUtil.stopProgress();
                    },1000);
                }
            }
        }
    }

    Handler handler = new Handler();

    /**
     * 扫描二维码图片的方法
     * @param path
     * @return
     */
    public Result scanningImage(String path , String name) {
        if(TextUtils.isEmpty(path)){
            return null;
        }

// 压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 600);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
        int[] data = new int[width * height];
        scanBitmap.getPixels(data, 0, width, 0, 0, width, height);

        LogUtils.i("isStart-->path" , "bitmap is null?" + (scanBitmap == null?"true" : "false") + ";path:" + path + ";name:" + name);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1);
        } catch (Exception e) {
            LogUtils.i("isStart-->ScanCode",(e!=null?e.getMessage():"") + ";" + (e!=null?e.toString():""));
            e.printStackTrace();
        }
        return null;
    }


}