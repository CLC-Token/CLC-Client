/*
 * Copyright (C) 2017 PeerSafe Technologies. All rights reserved.
 *
 * @Package: com.peersafe.zxwallet.module
 * @Description:
 * @author sunhaitao
 * @date 2017年08月31日  11:30:00
 */

package com.peersafe.hdtsdk.inner;

public class AccountInfoModel {
    /**
     * account_data : {"Account":"rarHb1HTg1BdjjzATbQfPgS5x1BH3DV1vK","Balance":"981849712","Flags":0,"LedgerEntryType":"AccountRoot","OwnerCount":3,"PreviousTxnID":"AF4782FE0B18D0760ACE1BCD9F7BBFCF6D7404C6EA9F1D7EAD077A25EBA868C4","PreviousTxnLgrSeq":171207,"Sequence":25,"index":"E24FEDBE895869A482984123FCE74182B0D88C3A827F571BDAD705D5EF901AA6"}
     * ledger_current_index : 201342
     * queue_data : {"txn_count":0}
     * validated : false
     */

    private AccountDataBean account_data;
    private int ledger_current_index;
    private QueueDataBean queue_data;
    private boolean validated;

    public AccountDataBean getAccount_data() {
        return account_data;
    }

    public void setAccount_data(AccountDataBean account_data) {
        this.account_data = account_data;
    }

    public int getLedger_current_index() {
        return ledger_current_index;
    }

    public void setLedger_current_index(int ledger_current_index) {
        this.ledger_current_index = ledger_current_index;
    }

    public QueueDataBean getQueue_data() {
        return queue_data;
    }

    public void setQueue_data(QueueDataBean queue_data) {
        this.queue_data = queue_data;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public static class AccountDataBean {
        /**
         * Account":"zf24r4Wo221SRg7aSRBDW611u2AGuAqowu",
         "Balance":"300999999901",
         "Flags":8388608,
         "LedgerEntryType":"AccountRoot",
         "OwnerCount":0,
         "PreviousTxnID":"103D605A12271BE2CC027D914724B319C85F545D34F341446DF7D909F53650D3",
         "PreviousTxnLgrSeq":382866,
         "Sequence":10,
         "TransferRate":1000100000,
         "TransferFeeMin":"0.01",
         "index":"FB1F5D5495EF36A16CB2E3100EDF6A2FB87FECAE6BEEE059F6B8BE97FF187832"
         */

        private String Account;
        private String Balance;
        private int Flags;
        private String LedgerEntryType;
        private int OwnerCount;
        private String PreviousTxnID;
        private int PreviousTxnLgrSeq;
        private int Sequence;
        private long TransferRate;
        private String TransferFeeMin;
        private String index;

        public String getAccount() {
            return Account;
        }

        public void setAccount(String Account) {
            this.Account = Account;
        }

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String Balance) {
            this.Balance = Balance;
        }

        public int getFlags() {
            return Flags;
        }

        public void setFlags(int Flags) {
            this.Flags = Flags;
        }

        public String getLedgerEntryType() {
            return LedgerEntryType;
        }

        public void setLedgerEntryType(String LedgerEntryType) {
            this.LedgerEntryType = LedgerEntryType;
        }

        public int getOwnerCount() {
            return OwnerCount;
        }

        public void setOwnerCount(int OwnerCount) {
            this.OwnerCount = OwnerCount;
        }

        public String getPreviousTxnID() {
            return PreviousTxnID;
        }

        public void setPreviousTxnID(String PreviousTxnID) {
            this.PreviousTxnID = PreviousTxnID;
        }

        public int getPreviousTxnLgrSeq() {
            return PreviousTxnLgrSeq;
        }

        public void setPreviousTxnLgrSeq(int PreviousTxnLgrSeq) {
            this.PreviousTxnLgrSeq = PreviousTxnLgrSeq;
        }

        public int getSequence() {
            return Sequence;
        }

        public void setSequence(int Sequence) {
            this.Sequence = Sequence;
        }

        public long getTransferRate() {
            return TransferRate;
        }

        public void setTransferRate(int TransferRate) {
            this.TransferRate = TransferRate;
        }

        public String getTransferFeeMin() {
            return TransferFeeMin;
        }

        public void setTransferFeeMin(String TransferFeeMin) {
            this.TransferFeeMin = TransferFeeMin;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

    public static class QueueDataBean {
        /**
         * txn_count : 0
         */

        private int txn_count;

        public int getTxn_count() {
            return txn_count;
        }

        public void setTxn_count(int txn_count) {
            this.txn_count = txn_count;
        }
    }
}
