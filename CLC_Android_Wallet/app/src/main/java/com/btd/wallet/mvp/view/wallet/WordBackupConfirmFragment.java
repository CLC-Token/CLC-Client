package com.btd.wallet.mvp.view.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.btd.wallet.base.fragment.BaseSupportFragment;
import com.btd.wallet.mvp.view.dialog.ConfirmDialog;
import com.btd.wallet.mvp.view.dialog.listener.DialogListener;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.WordCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WordBackupConfirmFragment extends BaseSupportFragment {

    public static WordBackupConfirmFragment newInstance(List<String> words) {
        Bundle args = new Bundle();
        WordBackupConfirmFragment fragment = new WordBackupConfirmFragment();
        args.putSerializable("words",(Serializable) words);
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.layout_word)
    LinearLayout layout_word;

    @BindView(R.id.word_select)
    LinearLayout word_select;

    LayoutInflater inflater;

    List<String> words=new ArrayList<>();

    List<String> newWords=new ArrayList<>();

    WordView newWordView=new WordView();

    @Override
    protected int getContentView() {
        return R.layout.fragment_word_backupconfirm;
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
      List<String> newsList=  new ArrayList<>();
      for(int i=0;i<words.size();i++){
          newsList.add(words.get(i));
      }
        Collections.shuffle(newsList);
        wordView.initWords(mActivity, layout_word, newsList, false, new WordCallBack() {
            @Override
            public void callBack(int index, String word) {
                addWords(word);
            }
        },false);
    }

    private void addWords(String word){
        if(newWords.size()==12){
            showToast(getStr(R.string.atmost_recovery));
            return;
        }
        newWords.add(word);
        initNewWord();
    }

    private void initNewWord(){
        newWordView.initWords(mActivity, word_select, newWords, false, new WordCallBack() {
            @Override
            public void callBack(int index, String word) {
                newWords.remove(index);
                initNewWord();
            }
        },true);
    }


    @OnClick(value = {R.id.btn_ok})
    public void onNewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                checkSelect();
                break;

        }
    }

    private void checkSelect(){
        if(words.size()!=newWords.size()){
            showToast(getStr(R.string.word_confirm_tips));
            return;
        }
        Boolean result=true;
        for(int i=0;i<words.size();i++){
            if(!words.get(i).equals(newWords.get(i))){
                showToast(getStr(R.string.word_confirm_tips));
                result=false;
                break;
            }
        }
        if(result){
            new CreateSuccessDialog(mActivity, new DialogListener() {
                @Override
                public void cancel() {

                }

                @Override
                public void confirm() {
                    popTo(WalletFragment.class, false);
                }
            }).show();

//           new ConfirmDialog(mActivity, getStr(R.string.crate_success), new DialogListener() {
//               @Override
//               public void cancel() {
//
//               }
//
//               @Override
//               public void confirm() {
//
//
//               }
//           },false).show();
        }

    }
}
