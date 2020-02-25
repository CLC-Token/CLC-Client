package com.btd.wallet.mvp.view.wallet;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btd.wallet.core.WorkApp;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.WordCallBack;

import java.util.ArrayList;
import java.util.List;

public class WordView {

    private List<String> words=new ArrayList<>();
    private LayoutInflater inflater;
    private LinearLayout superView;
    private Boolean needIndex;
    private Context context;
    private  Boolean isSelect;
    private WordCallBack callBack;


    public void initWords(Context context, LinearLayout layout, List<String> words, Boolean needIndex, WordCallBack callBack,Boolean isSelect){
        this.words=words;
        inflater=LayoutInflater.from(context);
        this.superView=layout;
        this.needIndex=needIndex;
        this.context=context;
        this.callBack=callBack;
        this.isSelect=isSelect;
        initView();

    }

    private void initView(){
        superView.removeAllViews();
        int width = WorkApp.mScreenWidth;         // 屏幕宽度（像素）
        int totalwith=width-(int)context.getResources().getDimension(R.dimen.dp_10)*2;
        if(isSelect){
            totalwith=width-(int)context.getResources().getDimension(R.dimen.dp_10)*5;
        }
        LinearLayout contenthor=null;
        int w=0;
        for(int i=0;i<words.size();i++){
            if(i==0) {
                contenthor =(LinearLayout) inflater.inflate(R.layout.word_layout, null);
                superView.addView(contenthor);
            }
            final View verView=inflater.inflate(R.layout.word_item,null);
            verView.setTag(i);
            verView.findViewById(R.id.word_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callBack!=null){
                       int tag= (int)verView.getTag();
                       callBack.callBack(tag,words.get(tag));
                    }
                }
            });
            TextView word_txt=verView.findViewById(R.id.word);
            word_txt.setText(words.get(i));
            if(needIndex){
                TextView index_txt=verView.findViewById(R.id.index);
                index_txt.setVisibility(View.VISIBLE);
                index_txt.setText(""+(i+1));
            }
            if(isSelect){
                verView.findViewById(R.id.close).setVisibility(View.VISIBLE);
            }else{
                verView.findViewById(R.id.close).setVisibility(View.GONE);
            }
            int verWidth=getViewWidth(verView);
            w=w+verWidth;
            if(w>totalwith){
                contenthor=(LinearLayout) inflater.inflate(R.layout.word_layout,null);
                superView.addView(contenthor);
                w=verWidth;
            }
            contenthor.addView(verView);

        }

    }


    protected int getViewWidth(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return width;



    }


}
