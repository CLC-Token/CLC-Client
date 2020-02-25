package com.btd.wallet.mvp.view.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.pure.R;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WordBackupSuccessFragment extends BaseSupportFragment {

    public static WordBackupSuccessFragment newInstance(List<String> words) {
        Bundle args = new Bundle();
        WordBackupSuccessFragment fragment = new WordBackupSuccessFragment();
        args.putSerializable("words",(Serializable) words);
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.layout_word)
    LinearLayout layout_word;

    LayoutInflater inflater;

    List<String> words=new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_word_backupsuccess;
    }
    @Override
    protected void initView() {
        inflater=LayoutInflater.from(getContext());
        words = (List<String>) mBundle.getSerializable("words");
        initWords();

    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }


    private void initWords(){
        WordView wordView=new WordView();

        wordView.initWords(mActivity,layout_word,words,true,null,false);
    }


    @OnClick(value = {R.id.btn_ok})
    public void onNewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                EventBus.getDefault().post(WordBackupConfirmFragment.newInstance(words));
                break;

        }
    }
}
