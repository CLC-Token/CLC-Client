/*
 * Copyright (C) 2017 PeerSafe Technologies. All rights reserved.
 *
 * @Package: com.peersafe.zxwallet.module
 * @Description:
 * @author sunhaitao
 * @date 2017年08月31日  11:32:00
 */

package com.peersafe.hdtsdk.inner;

import java.util.List;

public class AccountLineModel {

    /**
     * account : rarHb1HTg1BdjjzATbQfPgS5x1BH3DV1vK
     * ledger_current_index : 201435
     * lines : [{"account":"rPZy8t4f2ga6CKeXMUM272zqF4VMPm3Hi3","balance":"0","currency":"USD","limit":"1000000000","limit_peer":"0","memos":[{"Memo":{"MemoData":"7274312E312E33322D342D67663865623331382D6469727479","MemoType":"636C69656E74"}}],"no_ripple":true,"no_ripple_peer":true,"quality_in":0,"quality_out":0},{"account":"rPZy8t4f2ga6CKeXMUM272zqF4VMPm3Hi3","balance":"21.3","currency":"CNY","limit":"0","limit_peer":"0","no_ripple":true,"no_ripple_peer":true,"quality_in":0,"quality_out":0}]
     * validated : false
     */

    private String account;
    private int ledger_current_index;
    private boolean validated;
    private List<LinesBean> lines;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getLedger_current_index() {
        return ledger_current_index;
    }

    public void setLedger_current_index(int ledger_current_index) {
        this.ledger_current_index = ledger_current_index;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public List<LinesBean> getLines() {
        return lines;
    }

    public void setLines(List<LinesBean> lines) {
        this.lines = lines;
    }

    public static class LinesBean {
        /**
         * account : rPZy8t4f2ga6CKeXMUM272zqF4VMPm3Hi3
         * balance : 0
         * currency : USD
         * limit : 1000000000
         * limit_peer : 0
         * memos : [{"Memo":{"MemoData":"7274312E312E33322D342D67663865623331382D6469727479","MemoType":"636C69656E74"}}]
         * no_ripple : true
         * no_ripple_peer : true
         * freeze_peer:true,
         * quality_in : 0
         * quality_out : 0
         */

        private String account;
        private String balance;
        private String currency;
        private String limit;
        private String limit_peer;
        private boolean no_ripple;
        private boolean no_ripple_peer;
        private boolean freeze_peer;
        private int quality_in;
        private int quality_out;
        private List<MemosBean> memos;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getLimit_peer() {
            return limit_peer;
        }

        public void setLimit_peer(String limit_peer) {
            this.limit_peer = limit_peer;
        }

        public boolean isNo_ripple() {
            return no_ripple;
        }

        public void setNo_ripple(boolean no_ripple) {
            this.no_ripple = no_ripple;
        }

        public boolean isNo_ripple_peer() {
            return no_ripple_peer;
        }

        public void setNo_ripple_peer(boolean no_ripple_peer) {
            this.no_ripple_peer = no_ripple_peer;
        }

        public boolean isFreezePeer() {
            return freeze_peer;
        }

        public void setFreezePeer(boolean freeze_peer) {
            this.freeze_peer = freeze_peer;
        }

        public int getQuality_in() {
            return quality_in;
        }

        public void setQuality_in(int quality_in) {
            this.quality_in = quality_in;
        }

        public int getQuality_out() {
            return quality_out;
        }

        public void setQuality_out(int quality_out) {
            this.quality_out = quality_out;
        }

        public List<MemosBean> getMemos() {
            return memos;
        }

        public void setMemos(List<MemosBean> memos) {
            this.memos = memos;
        }

        public static class MemosBean {
            /**
             * Memo : {"MemoData":"7274312E312E33322D342D67663865623331382D6469727479","MemoType":"636C69656E74"}
             */

            private MemoBean Memo;

            public MemoBean getMemo() {
                return Memo;
            }

            public void setMemo(MemoBean Memo) {
                this.Memo = Memo;
            }

            public static class MemoBean {
                /**
                 * MemoData : 7274312E312E33322D342D67663865623331382D6469727479
                 * MemoType : 636C69656E74
                 */

                private String MemoData;
                private String MemoType;

                public String getMemoData() {
                    return MemoData;
                }

                public void setMemoData(String MemoData) {
                    this.MemoData = MemoData;
                }

                public String getMemoType() {
                    return MemoType;
                }

                public void setMemoType(String MemoType) {
                    this.MemoType = MemoType;
                }
            }
        }
    }
}
