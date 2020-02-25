package com.btd.wallet.mvp.model;

/**
 * Description:  当页面可视时刷新状态参数  <br>
 * Author: cxh <br>
 * Date: 2019/4/22 0022 17:51
 */
public class VisibleReFresh {

    private boolean home;
    private boolean find;
    private boolean me;
    private boolean erc;
    private boolean walletMe;
    private boolean exchangeErcRecord;
    private boolean exchangeBtdRecord;
    private boolean persion;
    private boolean loadUserInfo;
    private boolean hdtBtd;
    private boolean btd2HdtRecord;
    private boolean btd2HdtHome;
    private boolean contractDetail;
    private boolean contractHome;
    private boolean btnExitLogin;
    private boolean hasLocationPermission;

    public boolean isHasLocationPermission() {
        return hasLocationPermission;
    }

    public void setHasLocationPermission(boolean hasLocationPermission) {
        this.hasLocationPermission = hasLocationPermission;
    }

    public boolean isBtnExitLogin() {
        return btnExitLogin;
    }

    public void setBtnExitLogin(boolean btnExitLogin) {
        this.btnExitLogin = btnExitLogin;
    }

    public boolean isContractHome() {
        return contractHome;
    }

    public void setContractHome(boolean contractHome) {
        this.contractHome = contractHome;
    }

    public boolean isContractDetail() {
        return contractDetail;
    }

    public void setContractDetail(boolean contractDetail) {
        this.contractDetail = contractDetail;
    }

    public boolean isBtd2HdtHome() {
        return btd2HdtHome;
    }

    public void setBtd2HdtHome(boolean btd2HdtHome) {
        this.btd2HdtHome = btd2HdtHome;
    }

    public boolean isBtd2HdtRecord() {
        return btd2HdtRecord;
    }

    public void setBtd2HdtRecord(boolean btd2HdtRecord) {
        this.btd2HdtRecord = btd2HdtRecord;
    }

    public boolean isHdtBtd() {
        return hdtBtd;
    }

    public void setHdtBtd(boolean hdtBtd) {
        this.hdtBtd = hdtBtd;
    }

    public boolean isExchangeBtdRecord() {
        return exchangeBtdRecord;
    }

    public void setExchangeBtdRecord(boolean exchangeBtdRecord) {
        this.exchangeBtdRecord = exchangeBtdRecord;
    }

    public boolean isLoadUserInfo() {
        return loadUserInfo;
    }

    public void setLoadUserInfo(boolean loadUserInfo) {
        this.loadUserInfo = loadUserInfo;
    }

    public boolean isPersion() {
        return persion;
    }

    public void setPersion(boolean persion) {
        this.persion = persion;
    }

    public boolean isExchangeErcRecord() {
        return exchangeErcRecord;
    }

    public void setExchangeErcRecord(boolean exchangeErcRecord) {
        this.exchangeErcRecord = exchangeErcRecord;
    }

    public boolean isWalletMe() {
        return walletMe;
    }

    public void setWalletMe(boolean walletMe) {
        this.walletMe = walletMe;
    }

    public boolean isErc() {
        return erc;
    }

    public void setErc(boolean erc) {
        this.erc = erc;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isFind() {
        return find;
    }

    public void setFind(boolean find) {
        this.find = find;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }
}
