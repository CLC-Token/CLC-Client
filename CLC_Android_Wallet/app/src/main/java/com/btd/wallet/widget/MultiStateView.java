package com.btd.wallet.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.btd.library.base.util.LogUtils;
import com.btd.wallet.pure.R;
import com.btd.wallet.utils.MethodUtils;

/**
 * 
 * 类名: MultiStateView</br>
 * 包名：com.cm.weixiangji.view </br>
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 王太顺</br>
 * 创建时间： 2015年11月5日
 */
public class MultiStateView extends FrameLayout {
  // 定义页面类型
  private static final int UNKNOWN_VIEW = -1;

  private static final int CONTENT_VIEW = 0;

  private static final int ERROR_VIEW = 1;

  private static final int EMPTY_VIEW = 2;

  private static final int LOADING_VIEW = 3;

  public MultiStateViewClickListener getListener() {
    return listener;
  }

  public void setListener(MultiStateViewClickListener listener) {
    this.listener = listener;
  }

  // 枚举，切换页面用
  public enum ViewState {
    CONTENT, LOADING, EMPTY, ERROR
  }

  private LayoutInflater mInflater;

  private View mContentView;

  private View mLoadingView;

  private View mErrorView;

  private View mEmptyView;

  private ViewState mViewState = ViewState.CONTENT;
  Context mContext;
  LottieAnimationView lottie_start;

  public MultiStateView(Context context) {
    this(context, null);
  }

  public MultiStateView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.mContext = context;
    init(attrs);
  }

  // 页面初始化
  private void init(AttributeSet attrs) {
    mInflater = LayoutInflater.from(getContext());
    // TypedArray a = getContext().obtainStyledAttributes(attrs,
    // R.styleable.MultiStateView);

    // int loadingViewResId =
    // a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
    int loadingViewResId = MultiConfigure.getLoadingViewResId();
    if (loadingViewResId > -1) {
      mLoadingView = mInflater.inflate(loadingViewResId, this, false);
//      ImageView img_tip = (ImageView) mLoadingView.findViewById(R.id.image_loading);
//      if(img_tip != null){
//        img_tip.setImageResource(R.drawable.loading_gif);
//        Glide.with(mActivity).loadSuccess(R.drawable.loading_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img_tip);
//      }
      addView(mLoadingView, mLoadingView.getLayoutParams());
    }

    // int emptyViewResId =
    // a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
    int emptyViewResId = MultiConfigure.getEmptyViewResId();
    if (emptyViewResId > -1) {
      mEmptyView = mInflater.inflate(emptyViewResId, this, false);
      addView(mEmptyView, mEmptyView.getLayoutParams());
    }

    // int errorViewResId =
    // a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
    int errorViewResId = MultiConfigure.getErrorViewResId();
    if (errorViewResId > -1) {
      mErrorView = mInflater.inflate(errorViewResId, this, false);
      addView(mErrorView, mErrorView.getLayoutParams());
    }

    // int viewState = a.getInt(R.styleable.MultiStateView_msv_viewState,
    // UNKNOWN_VIEW);
    int viewState = MultiConfigure.getViewState();
    if (viewState != UNKNOWN_VIEW) {
      switch (viewState) {
        case CONTENT_VIEW:
          mViewState = ViewState.CONTENT;
          break;

        case ERROR_VIEW:
          mViewState = ViewState.ERROR;
          break;

        case EMPTY_VIEW:
          mViewState = ViewState.EMPTY;
          break;

        case LOADING_VIEW:
          mViewState = ViewState.LOADING;
          break;
        default:
          break;
      }
    }

//    ImageView imageView = (ImageView) findViewById(R.id.image_loading);
//    if(imageView != null){
//      WorkApp.finalBitmap.displayRect(imageView, "assets://loading_gif.gif", null);
//      Glide.with(mActivity).loadSuccess(R.drawable.loading_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
//    }
//    View view = findViewById(R.id.lottie_start);
//    if(view != null) {
//      lottie_start = (LottieAnimationView) view;
////      lottie_start.setAnimation("loading_cloud.json");
////      lottie_start.playAnimation();
//    }
    // a.recycle();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (mContentView == null) {
        throw new IllegalArgumentException("Content view is not defined");
    }
    setView();
  }

  @Override
  public void addView(View child) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    super.addView(child);
  }

  @Override
  public void addView(View child, int index) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    super.addView(child, index);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    super.addView(child, index, params);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    super.addView(child, params);
  }

  @Override
  public void addView(View child, int width, int height) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    super.addView(child, width, height);
  }

  @Override
  protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    return super.addViewInLayout(child, index, params);
  }

  @Override
  protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params,
                                    boolean preventRequestLayout) {
    if (isValidContentView(child)) {
        mContentView = child;
    }
    return super.addViewInLayout(child, index, params, preventRequestLayout);
  }

  public View getView(ViewState state) {
    switch (state) {
      case LOADING:
        return mLoadingView;

      case CONTENT:
        return mContentView;

      case EMPTY:
        return mEmptyView;

      case ERROR:
        return mErrorView;

      default:
        return null;
    }
  }

  public ViewState getViewState() {
    return mViewState;
  }

  public void setViewState(ViewState state) {
    if (state != mViewState) {
      mViewState = state;
      setView();
    }
  }

  public void setViewStateAndContent(ViewState state , String content) {
    if (state != mViewState) {
      mViewState = state;
      setView();
      setEmptyViewContent(content);
    }
  }

  /**
   * 
   * 方法名: </br>
   * 详述: 空界面,设置提示语</br>
   * 开发人员：王太顺</br>
   * 创建时间：2015-10-30</br>
   * 
   * @param strContent
   */
  public void setEmptyViewContent(String strContent) {
    if(mErrorView != null){
      TextView txtContent = (TextView) mErrorView.findViewById(R.id.txt_content);
      txtContent.setText(strContent);
    }
    if(mEmptyView != null){
      TextView txtContent = (TextView) mEmptyView.findViewById(R.id.txt_content);
      txtContent.setText(strContent);
    }
  }

  /**
   * <p> 更新loading提示语
   * @param strContent
   */
  public void setLoadingViewContent(String strContent) {
    try {
      TextView txtContent = (TextView) mLoadingView.findViewById(R.id.txt_content);
      if(txtContent == null){
        return;
      }
      txtContent.setText(strContent);
      if (null == strContent || TextUtils.isEmpty(strContent)) {
        MethodUtils.setVisible(txtContent, false);
      } else {
        MethodUtils.setVisible(txtContent, true);
      }
    } catch (Exception e) {
      LogUtils.e(e.toString());
    }
  }

  /**
   * 
   * 方法名: </br>
   * 详述: </br>
   * 开发人员：王太顺</br>
   * 创建时间：2015-10-30</br>
   * 
   * @param imgId
   */
  public void setEmptyViewImg(int imgId) {
    ImageView img_tip = (ImageView) mEmptyView.findViewById(R.id.img_pri_tip);
    img_tip.setImageResource(imgId);
  }

  // 核心代码
  private void setView() {
    switch (mViewState) {
      case LOADING:
        if (mLoadingView == null) {
          // throw new NullPointerException("Loading View--加载页面为空");
          return;
        }

        mLoadingView.setVisibility(View.VISIBLE);
        if (mContentView != null) {
            mContentView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        break;

      case EMPTY:
        if (mEmptyView == null) {
          throw new NullPointerException("Empty View--" + MethodUtils.getString(R.string.empty_is_null));
        }
        if(lottie_start != null) {
          lottie_start.cancelAnimation();
        }
        mEmptyView.setVisibility(View.VISIBLE);
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mContentView != null) {
            mContentView.setVisibility(View.GONE);
        }
        break;

      case ERROR:
        if (mErrorView == null) {
          throw new NullPointerException("Error View--" + MethodUtils.getString(R.string.error_is_null));
        }
        if(lottie_start != null) {
          lottie_start.cancelAnimation();
        }
        mErrorView.setVisibility(View.VISIBLE);
        // setEmptyViewContent("亲,快检查网络是否异常");
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mContentView != null) {
            mContentView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        break;

      case CONTENT:
      default:
        if (mContentView == null) {
          // Should never happen, the view should throw an exception if no
          // content view is present upon creation
          throw new NullPointerException("Content View--" + MethodUtils.getString(R.string.main_is_null));
        }
        if(lottie_start != null) {
          lottie_start.cancelAnimation();
        }
        mContentView.setVisibility(View.GONE);
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        break;
    }
  }

  /**
   * Checks if the given {@link View} is valid for the Content View
   * 
   * @param view
   *          The {@link View} to check
   * @return
   */
  private boolean isValidContentView(View view) {
    if (mContentView != null && mContentView != view) {
      return false;
    }

    return view != mLoadingView && view != mErrorView && view != mEmptyView;
  }

  public void setViewForState(View view, ViewState state, boolean switchToState) {
    switch (state) {
      case LOADING:
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        mLoadingView = view;
        addView(mLoadingView);
        break;

      case EMPTY:
        if (mEmptyView != null) {
            removeView(mEmptyView);
        }
        mEmptyView = view;
        addView(mEmptyView);
        break;

      case ERROR:
        if (mErrorView != null) {
            removeView(mErrorView);
        }
        mErrorView = view;
        addView(mErrorView);
        break;

      case CONTENT:
        if (mContentView != null) {
            removeView(mContentView);
        }
        mContentView = view;
        addView(mContentView);
        break;
      default:
        break;
    }

    if (switchToState) {
        setViewState(state);
    }
  }

  public void setViewForState(View view, ViewState state) {
    setViewForState(view, state, false);
  }

  public void setViewForState(int layoutRes, ViewState state, boolean switchToState) {
    if (mInflater == null) {
        mInflater = LayoutInflater.from(getContext());
    }
    View view = mInflater.inflate(layoutRes, this, false);
    setViewForState(view, state, switchToState);
  }

  public void setViewForState(int layoutRes, ViewState state) {
    setViewForState(layoutRes, state, false);
  }

  private MultiStateViewClickListener listener;

  public interface MultiStateViewClickListener{
    public void onClick(View view);
  }

  public void addViewClick(int id){
     View view = findViewById(id);
     if(view!=null){
       view.setOnClickListener( (v) ->
         listener.onClick(v)
       );
     }
  }
}