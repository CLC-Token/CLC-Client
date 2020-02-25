package com.btd.wallet.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;

import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 
 * 类名: UpdateVersionDialog</br> 包名：com.peace.work.ui </br> 描述: <更新/br>
 * 发布版本号：</br> 开发人员： 王太顺</br> 创建时间： 2015-6-27
 */
public class UpdateVersionDialog extends Dialog {
    private Context context;
    private VersionModel versionModel;
    private UpdateVersion updateVersion;
    private TextView btn_ok;
    private ProgressBar progressBar;
    private boolean isForce = false;
    UpdateVersionDialog(Context context, VersionModel versionModel, UpdateVersion updateVersion) {
      super(context, R.style.FullScreenBaseDialog);
      this.context = context;
      this.versionModel = versionModel;
      this.updateVersion = updateVersion;
    }

    private boolean isDownload = false;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_update);
      EventBus.getDefault().register(this);
      TextView txt_title = findViewById(R.id.txt_title);
      TextView txt_content = findViewById(R.id.txt_content);
      progressBar = findViewById(R.id.pb_progrss);
      txt_title.setText(context.getString(R.string.app_name) + " " + versionModel.getVersionName() + " " + MethodUtils.getString(R.string.string_issue));
      txt_content.setText(versionModel.getDescriptions()+"");

      //升级
      btn_ok = findViewById(R.id.btn_ok);
      btn_ok.setOnClickListener(startDownload());
      //取消
      View layout_close = findViewById(R.id.layout_close);
      isForce = versionModel.isForce();
      if(versionModel.isForce()){
          setCancelable(false);
          setCanceledOnTouchOutside(false);
          layout_close.setVisibility(View.GONE);
      }else{
          layout_close.setOnClickListener(v -> {
              updateVersion.cancel();
              UpdateVersionDialog.this.dismiss();
          });
      }

    }

    private View.OnClickListener startDownload() {
        return v -> {
          if(isDownload){
            LogUtils.d("已经在下载APP了!!!");
            return;
          }
          isDownload = true;
          updateVersion.update();
          btn_ok.setText(MethodUtils.getString(R.string.is_updateing , new Object[]{""}));
          progressBar.setVisibility(View.VISIBLE);
//          txt_update.setVisibility(View.VISIBLE);
  //        btn_ok.setEnabled(false);
  //        if(!versionModel.isIs_force()) {
  //            UpdateVersionDialog.this.dismiss();
  //        } else{
  //          if(!isDownload) {
  //             MethodUtils.showToast(context, context.getString(R.string.version_need_update));
  //             isDownload = true;
  //          }
  //        }
        };
    }

    /**
     *
     * 类名: updateVersion</br> 包名：com.peace.work.ui </br> 描述: </br> 发布版本号：</br>
     * 开发人员： 王太顺</br> 创建时间： 2015-6-27
     */
    public interface UpdateVersion {
        void update();

        void cancel();
    }

    void setBtnEnable(boolean enable){
      if(btn_ok!=null) {
        btn_ok.setEnabled(enable);
      }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateDownloadDialog(UpdateDownloadDialogEvent updateDownloadDialogEvent){
        try {
            int progress = updateDownloadDialogEvent.progress;
            if (btn_ok != null) {
                if (progress == -1) {
                    isDownload = false;
                    btn_ok.setText(R.string.update_error);
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    btn_ok.setText(MethodUtils.getString(R.string.is_updateing, new Object[]{progress}) + "%");
                }
            }
            if (progressBar != null) {
                progressBar.setProgress(progress);
            }
            if (progress >= 100) {
//                if (!isForce) {
//                    UpdateVersionDialog.this.dismiss();
//                }
                isDownload = false;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            dismiss();
        }
    }
}