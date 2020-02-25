package com.btd.wallet.mvp.view.wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.btd.wallet.mvp.view.dialog.listener.DialogListener;
import com.btd.wallet.pure.R;

public class CreateSuccessDialog extends Dialog {

    DialogListener listener;
    public CreateSuccessDialog( Context context) {
        super(context);
    }

    public CreateSuccessDialog(Context context, DialogListener listener) {
        this(context );
        this.listener=listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_success);
        findViewById(R.id.dialog_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSuccessDialog.this.dismiss();
                if(listener!=null){
                    listener.confirm();
                }
            }
        });
    }
}
