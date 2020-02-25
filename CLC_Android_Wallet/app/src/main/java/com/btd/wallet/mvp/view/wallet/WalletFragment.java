package com.btd.wallet.mvp.view.wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btd.library.base.util.LogUtils;

import com.btd.wallet.mvp.model.wallet.WalletConfig;
import com.btd.wallet.pure.R;
import com.btd.wallet.base.recycler.RefreshFragment;
import com.btd.wallet.config.SPKeys;
import com.btd.wallet.core.WorkApp;
import com.btd.wallet.event.ScanEvent;

import com.btd.wallet.home.popupwindow.WalletMePop;

import com.btd.wallet.mvp.adapter.wallet.WalletAdapter;
import com.btd.wallet.mvp.contract.wallet.WalletContract;

import com.btd.wallet.mvp.model.wallet.WalletModel;
import com.btd.wallet.mvp.presenter.wallet.WalletPresenter;

import com.btd.wallet.mvp.view.dialog.listener.DialogListener;

import com.btd.wallet.utils.KeyboardUtils;
import com.btd.wallet.utils.MethodUtils;
import com.btd.wallet.utils.StringUtils;
import com.btd.wallet.utils.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.btd.wallet.widget.MultiStateView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Description: 我的钱包   <br>
 * Author: cxh <br>
 * Date: 2019/6/25 11:56
 */
public class WalletFragment extends RefreshFragment<WalletAdapter, WalletContract.IPresenter
        , WalletModel> implements WalletContract.IView {

    private static final int SCAN_ADD = 1003;

    @BindView(R.id.swipeLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fl_empty)
    FrameLayout flEmpty;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.cl_root)
    ConstraintLayout clRoot;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.ll_body)
    LinearLayout llBody;
    @BindView(R.id.txt_btd_about)
    TextView txtBtdAbout;
    @BindView(R.id.txt_btd_value)
    TextView txtBtdValue;
    @BindView(R.id.txt_hdt_about)
    TextView txtHdtAbout;
    @BindView(R.id.txt_hdt_value)
    TextView txtHdtValue;
    private WalletMePop mWalletMePop;
    private RxPermissions mRxPermission;

    public static WalletFragment newInstance() {
        Bundle args = new Bundle();
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected BaseQuickAdapter initAdapter() {
        return new WalletAdapter(R.layout.wallet_item, null);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WalletPresenter(mActivity, this);
    }

    @Override
    protected void initToolbar(View rootView) {
        super.initToolbar(rootView);
        setBack();
    }

    @Override
    protected void setBack() {
//        setNavigationIcon(R.drawable.home_scan);
//        setNavigationOnClickListener(v -> {
//            if (KeyboardUtils.isOpen(mActivity)) {
//                KeyboardUtils.hideKeyboard(mActivity, v);
//            }
//            scan();
//        });
//        if (mToolbar != null) {
//            LogUtils.i(TAG + "初始化左上角按钮,具备返回键功能");
//        }
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getStr(R.string.string_home));
        initListener();
        initMenuPop();
        refreshLayout.setOnRefreshListener(this);
        mRxPermission = new RxPermissions(this);

    }

    @Override
    protected int getMenuLayoutResId() {
        return R.menu.menu_wallet_me;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add: {
                if (mWalletMePop != null) {
                    mWalletMePop.showPopupWindow(clRoot);
                }
//        List<String> words=new ArrayList<>();
//        words.add("abc");
//        words.add("abcde");
//                words.add("abcd213e");
//
//                EventBus.getDefault().post(WordBackupSuccessFragment.newInstance(words));
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canOnRefresh() {
        return true;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onScanEvent(ScanEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        scan();
    }


    /**
     * 初始化监听
     */
    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            WalletModel model = mAdapter.getItem(position);
            if (model == null) {
                LogUtils.i("选择的Item为null");
                return;
            }
            WalletConfig config = new WalletConfig();
            config.setFromAddr(model.getAddress());
            config.setCode(model.getStrCode());
            config.setNickName(model.getNickName());
            config.setBalanceBtd(String.valueOf(model.getBalanceBtd()));
            config.setBalanceHdt(String.valueOf(model.getBalanceHdt()));
            config.setFreezeBtd(model.isFreezeBtd());
            config.setFreezeHdt(model.isFreezeHdt());
            config.setWords(model.getWords());
            EventBus.getDefault().post(DetailFragment.newInstance(config));
        });
    }

    private void initMenuPop() {
        mWalletMePop = new WalletMePop(mActivity);
        mWalletMePop.setOnPopItemClickListener(new WalletMePop.OnPopItemClickListener() {
            @Override
            public void onNewClick() {
                EventBus.getDefault().post(CreateFragment.newInstance());
            }

            @Override
            public void onImportClick() {
                EventBus.getDefault().post(ImportFragment.newInstance());
            }

            @Override
            public void onAbout() {
               // EventBus.getDefault().post(AboutContentFragment.newInstance());
            }

            @Override
            public void exchange() {

               // toEchangeErc();
            }

            @Override
            public void exchangeIst() {
               // //只能锁仓
               // EventBus.getDefault().post(ContractHomeFragment.newInstance());
            }

            @Override
            public void lock() {
            }

            @Override
            public void hdtExchange() {
              //  EventBus.getDefault().postSticky(HdtBtdMainFragment.newInstance());
            }
        });
    }

    @Override
    protected RecyclerView.ItemDecoration initItemDecoration() {
        return new HorizontalDividerItemDecoration
                .Builder(mActivity)
                .size(0)
                .color(MethodUtils.getColor(R.color.transparent))
                .build();
    }

    @Override
    public void loadFail(boolean isError, String text) {
        if (isError) {
            super.loadFail(isError, text);
        } else {

            flEmpty.setVisibility(View.VISIBLE);
            clRoot.setVisibility(View.GONE);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.empty_wallet, null);
            view.findViewById(R.id.btn_create).setOnClickListener(v ->
                    EventBus.getDefault().post(CreateFragment.newInstance()));
            view.findViewById(R.id.btn_import).setOnClickListener(v ->
                    EventBus.getDefault().post(ImportFragment.newInstance()));
            mAdapter.getData().clear();
            mMultiStateView.setViewState(MultiStateView.ViewState.EMPTY);
            mMultiStateView.setViewForState(view, MultiStateView.ViewState.EMPTY);
            loadFinish();
        }
    }

    /**
     * 扫描二维码
     */
    @SuppressLint("CheckResult")
    private void scan() {
        LogUtils.i(TAG + " scan");
        if(mRxPermission == null) return;
        mRxPermission.requestEach(Manifest.permission.CAMERA)
                .subscribe(permission -> {
                    if (permission.granted) {
                        Intent intent = new Intent(mActivity, CaptureActivity.class);
                        intent.putExtra(SPKeys.fromType, CaptureActivity.FROM_HOME);
                        startActivityForResult(intent, SCAN_ADD);
                        LogUtils.d(permission.name + " is granted.");
                    } else {
                        showToast(getStr(R.string.camear_is_forbid));
                        LogUtils.d(permission.name + " is denied.");
                    }
                });
    }

    @Override
    public void refreshItem(boolean finish, BigDecimal btd, BigDecimal hdt, boolean isEmpy) {
        if (isSupportVisible()) {

            if (finish) {
                pb.setVisibility(View.INVISIBLE);
                llBody.setVisibility(View.VISIBLE);
                txtBtdAbout.setVisibility(StringUtils.getDecimalPlaces(btd) > 6 ? View.VISIBLE : View.GONE);
                txtHdtAbout.setVisibility(StringUtils.getDecimalPlaces(hdt) > 6 ? View.VISIBLE : View.GONE);
//                txtBtdValue.setText(StringUtils.doubleFormat(btd));
//                txtHdtValue.setText(StringUtils.doubleFormat(hdt));
                txtBtdValue.setText(StringUtils.decimalToString(btd));
                txtHdtValue.setText(StringUtils.decimalToString(hdt));
            } else {
                llBody.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
            }
            if(!isEmpy){
                flEmpty.setVisibility(View.GONE);
                clRoot.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError() {
        if (isSupportVisible()) {
            showToast(R.string.wallet_detail_balance_failed);
            pb.setVisibility(View.INVISIBLE);
            llBody.setVisibility(View.VISIBLE);
            txtBtdAbout.setVisibility(View.GONE);
            txtHdtAbout.setVisibility(View.GONE);
            txtBtdValue.setText(R.string.string_default_value);
            txtHdtValue.setText(R.string.string_default_value);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }
    @Override
    public SmartRefreshLayout getRefrshLayout() {
        return refreshLayout;
    }

    @Override
    public void setStateVisiable(Boolean visiable) {
        mMultiStateView.setVisibility(visiable?View.VISIBLE:View.GONE);
        recycler.setVisibility(visiable?View.GONE:View.VISIBLE);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void startBrotherFragment(SupportFragment targetFragment) {
        EventBus.getDefault().removeStickyEvent(targetFragment);
        start(targetFragment);
    }
}
