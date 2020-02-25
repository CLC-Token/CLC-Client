package com.peersafe.hdtsdk.inner;

import java.util.List;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2019/3/29 16:42
 */
public class TransferModel {

    /**
     * validated : true
     * ledger_index : 220358
     * ledger_hash : 8C8D383B21AEBAF29305161059C1B17BA279086093286FB19F21568EE7E94B8B
     * meta : {"AffectedNodes":[{"ModifiedNode":{"LedgerIndex":"151B463D576433FD893CA6FBEF201D610CE35B634D0A7214FE39AE144AD149AE","FinalFields":{"Account":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4","OwnerCount":6,"Flags":0,"Sequence":70,"Balance":"39999253"},"PreviousFields":{"Sequence":69,"Balance":"39999264"},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"AccountRoot","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}},{"ModifiedNode":{"LedgerIndex":"764F24DD3C960B391312B37D0D30BD30B08D70BCA30E4B17D0F95A63B551BA88","FinalFields":{"HighNode":"0000000000000000","LowNode":"0000000000000000","LowLimit":{"currency":"HDT","value":"100000000","issuer":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4"},"Flags":65536,"Balance":{"currency":"HDT","value":"1101.65","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"},"HighLimit":{"currency":"HDT","value":"0","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},"PreviousFields":{"Balance":{"currency":"HDT","value":"1102.66","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"}},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"RippleState","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}},{"ModifiedNode":{"LedgerIndex":"AD94BD3C91E3155CF960E1CD8DB5235C21169ABB003CA7DA8F0D8E145BB61608","FinalFields":{"HighNode":"0000000000000001","LowNode":"0000000000000000","LowLimit":{"currency":"HDT","value":"100000000","issuer":"z4vnwEVR6sNsKt8xKoh1GTvExEsVBghu6"},"Flags":65536,"Balance":{"currency":"HDT","value":"256.2","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"},"HighLimit":{"currency":"HDT","value":"0","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},"PreviousFields":{"Balance":{"currency":"HDT","value":"255.2","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"}},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"RippleState","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}}],"TransactionResult":"tesSUCCESS","TransactionIndex":0}
     * engine_result_code : 0
     * engine_result : tesSUCCESS
     * type : transaction
     * transaction : {"date":607163850,"Account":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4","Destination":"z4vnwEVR6sNsKt8xKoh1GTvExEsVBghu6","TransactionType":"Payment","SigningPubKey":"030859392D77B393E37D80CF0C227C403E17EA161E4C6E5D4B75AAB5AF5F5266A8","Amount":{"currency":"HDT","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"Fee":"11","SendMax":{"currency":"HDT","value":"1.01","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"Flags":2147483648,"Sequence":69,"LastLedgerSequence":220365,"TxnSignature":"3044022078C5BC8BAC9DFB4810525356E79D0D9E2EDC3853ED7A2F20BC2892FF3861FF3B02207099A4872C4B55F6CE152FD9094FC3217576E21D69A525CAC122FA2145F1F188","hash":"305F7204F558176E28C9ECB68AE831702BDBC2BAE90B823C00B7BB9EF11FB6EA"}
     * engine_result_message : The transaction was applied. Only final in a validated ledger.
     * status : closed
     */
    private boolean validated;
    private int ledger_index;
    private String ledger_hash;
    private MetaEntity meta;
    private int engine_result_code;
    private String engine_result;
    private String type;
    private TransactionEntity transaction;
    private String engine_result_message;
    private String status;

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setLedger_index(int ledger_index) {
        this.ledger_index = ledger_index;
    }

    public void setLedger_hash(String ledger_hash) {
        this.ledger_hash = ledger_hash;
    }

    public void setMeta(MetaEntity meta) {
        this.meta = meta;
    }

    public void setEngine_result_code(int engine_result_code) {
        this.engine_result_code = engine_result_code;
    }

    public void setEngine_result(String engine_result) {
        this.engine_result = engine_result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public void setEngine_result_message(String engine_result_message) {
        this.engine_result_message = engine_result_message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValidated() {
        return validated;
    }

    public int getLedger_index() {
        return ledger_index;
    }

    public String getLedger_hash() {
        return ledger_hash;
    }

    public MetaEntity getMeta() {
        return meta;
    }

    public int getEngine_result_code() {
        return engine_result_code;
    }

    public String getEngine_result() {
        return engine_result;
    }

    public String getType() {
        return type;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public String getEngine_result_message() {
        return engine_result_message;
    }

    public String getStatus() {
        return status;
    }

    public class MetaEntity {
        /**
         * AffectedNodes : [{"ModifiedNode":{"LedgerIndex":"151B463D576433FD893CA6FBEF201D610CE35B634D0A7214FE39AE144AD149AE","FinalFields":{"Account":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4","OwnerCount":6,"Flags":0,"Sequence":70,"Balance":"39999253"},"PreviousFields":{"Sequence":69,"Balance":"39999264"},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"AccountRoot","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}},{"ModifiedNode":{"LedgerIndex":"764F24DD3C960B391312B37D0D30BD30B08D70BCA30E4B17D0F95A63B551BA88","FinalFields":{"HighNode":"0000000000000000","LowNode":"0000000000000000","LowLimit":{"currency":"HDT","value":"100000000","issuer":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4"},"Flags":65536,"Balance":{"currency":"HDT","value":"1101.65","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"},"HighLimit":{"currency":"HDT","value":"0","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},"PreviousFields":{"Balance":{"currency":"HDT","value":"1102.66","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"}},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"RippleState","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}},{"ModifiedNode":{"LedgerIndex":"AD94BD3C91E3155CF960E1CD8DB5235C21169ABB003CA7DA8F0D8E145BB61608","FinalFields":{"HighNode":"0000000000000001","LowNode":"0000000000000000","LowLimit":{"currency":"HDT","value":"100000000","issuer":"z4vnwEVR6sNsKt8xKoh1GTvExEsVBghu6"},"Flags":65536,"Balance":{"currency":"HDT","value":"256.2","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"},"HighLimit":{"currency":"HDT","value":"0","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},"PreviousFields":{"Balance":{"currency":"HDT","value":"255.2","issuer":"zzzzzzzzzzzzzzzzzzzzBZbvji"}},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"RippleState","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}}]
         * TransactionResult : tesSUCCESS
         * TransactionIndex : 0
         */
        private List<AffectedNodesEntity> AffectedNodes;
        private String TransactionResult;
        private int TransactionIndex;

        public void setAffectedNodes(List<AffectedNodesEntity> AffectedNodes) {
            this.AffectedNodes = AffectedNodes;
        }

        public void setTransactionResult(String TransactionResult) {
            this.TransactionResult = TransactionResult;
        }

        public void setTransactionIndex(int TransactionIndex) {
            this.TransactionIndex = TransactionIndex;
        }

        public List<AffectedNodesEntity> getAffectedNodes() {
            return AffectedNodes;
        }

        public String getTransactionResult() {
            return TransactionResult;
        }

        public int getTransactionIndex() {
            return TransactionIndex;
        }

        public class AffectedNodesEntity {
            /**
             * ModifiedNode : {"LedgerIndex":"151B463D576433FD893CA6FBEF201D610CE35B634D0A7214FE39AE144AD149AE","FinalFields":{"Account":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4","OwnerCount":6,"Flags":0,"Sequence":70,"Balance":"39999253"},"PreviousFields":{"Sequence":69,"Balance":"39999264"},"PreviousTxnLgrSeq":220218,"LedgerEntryType":"AccountRoot","PreviousTxnID":"637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F"}
             */
            private ModifiedNodeEntity ModifiedNode;

            public void setModifiedNode(ModifiedNodeEntity ModifiedNode) {
                this.ModifiedNode = ModifiedNode;
            }

            public ModifiedNodeEntity getModifiedNode() {
                return ModifiedNode;
            }

            public class ModifiedNodeEntity {
                /**
                 * LedgerIndex : 151B463D576433FD893CA6FBEF201D610CE35B634D0A7214FE39AE144AD149AE
                 * FinalFields : {"Account":"zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4","OwnerCount":6,"Flags":0,"Sequence":70,"Balance":"39999253"}
                 * PreviousFields : {"Sequence":69,"Balance":"39999264"}
                 * PreviousTxnLgrSeq : 220218
                 * LedgerEntryType : AccountRoot
                 * PreviousTxnID : 637B1A8163229B66C4365DAD2A05A2D2E0866DCC416AD73A71E9C77EB3385D0F
                 */
                private String LedgerIndex;
                private FinalFieldsEntity FinalFields;
                private PreviousFieldsEntity PreviousFields;
                private int PreviousTxnLgrSeq;
                private String LedgerEntryType;
                private String PreviousTxnID;

                public void setLedgerIndex(String LedgerIndex) {
                    this.LedgerIndex = LedgerIndex;
                }

                public void setFinalFields(FinalFieldsEntity FinalFields) {
                    this.FinalFields = FinalFields;
                }

                public void setPreviousFields(PreviousFieldsEntity PreviousFields) {
                    this.PreviousFields = PreviousFields;
                }

                public void setPreviousTxnLgrSeq(int PreviousTxnLgrSeq) {
                    this.PreviousTxnLgrSeq = PreviousTxnLgrSeq;
                }

                public void setLedgerEntryType(String LedgerEntryType) {
                    this.LedgerEntryType = LedgerEntryType;
                }

                public void setPreviousTxnID(String PreviousTxnID) {
                    this.PreviousTxnID = PreviousTxnID;
                }

                public String getLedgerIndex() {
                    return LedgerIndex;
                }

                public FinalFieldsEntity getFinalFields() {
                    return FinalFields;
                }

                public PreviousFieldsEntity getPreviousFields() {
                    return PreviousFields;
                }

                public int getPreviousTxnLgrSeq() {
                    return PreviousTxnLgrSeq;
                }

                public String getLedgerEntryType() {
                    return LedgerEntryType;
                }

                public String getPreviousTxnID() {
                    return PreviousTxnID;
                }

                public class FinalFieldsEntity {
                    /**
                     * Account : zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4
                     * OwnerCount : 6
                     * Flags : 0
                     * Sequence : 70
                     * Balance : 39999253
                     */
                    private String Account;
                    private int OwnerCount;
                    private int Flags;
                    private int Sequence;
                    private String Balance;

                    public void setAccount(String Account) {
                        this.Account = Account;
                    }

                    public void setOwnerCount(int OwnerCount) {
                        this.OwnerCount = OwnerCount;
                    }

                    public void setFlags(int Flags) {
                        this.Flags = Flags;
                    }

                    public void setSequence(int Sequence) {
                        this.Sequence = Sequence;
                    }

                    public void setBalance(String Balance) {
                        this.Balance = Balance;
                    }

                    public String getAccount() {
                        return Account;
                    }

                    public int getOwnerCount() {
                        return OwnerCount;
                    }

                    public int getFlags() {
                        return Flags;
                    }

                    public int getSequence() {
                        return Sequence;
                    }

                    public String getBalance() {
                        return Balance;
                    }
                }

                public class PreviousFieldsEntity {
                    /**
                     * Sequence : 69
                     * Balance : 39999264
                     */
                    private int Sequence;
                    private String Balance;

                    public void setSequence(int Sequence) {
                        this.Sequence = Sequence;
                    }

                    public void setBalance(String Balance) {
                        this.Balance = Balance;
                    }

                    public int getSequence() {
                        return Sequence;
                    }

                    public String getBalance() {
                        return Balance;
                    }
                }
            }
        }
    }

    public class TransactionEntity {
        /**
         * date : 607163850
         * Account : zD6eLDsmHrJmhAS6CdrWRTdkWLj1ehJgp4
         * Destination : z4vnwEVR6sNsKt8xKoh1GTvExEsVBghu6
         * TransactionType : Payment
         * SigningPubKey : 030859392D77B393E37D80CF0C227C403E17EA161E4C6E5D4B75AAB5AF5F5266A8
         * Amount : {"currency":"HDT","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}
         * Fee : 11
         * SendMax : {"currency":"HDT","value":"1.01","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}
         * Flags : 2147483648
         * Sequence : 69
         * LastLedgerSequence : 220365
         * TxnSignature : 3044022078C5BC8BAC9DFB4810525356E79D0D9E2EDC3853ED7A2F20BC2892FF3861FF3B02207099A4872C4B55F6CE152FD9094FC3217576E21D69A525CAC122FA2145F1F188
         * hash : 305F7204F558176E28C9ECB68AE831702BDBC2BAE90B823C00B7BB9EF11FB6EA
         */
        private int date;
        private String Account;
        private String Destination;
        private String TransactionType;
        private String SigningPubKey;
        private AmountEntity Amount;
        private String Fee;
        private SendMaxEntity SendMax;
        private long Flags;
        private int Sequence;
        private int LastLedgerSequence;
        private String TxnSignature;
        private String hash;

        public void setDate(int date) {
            this.date = date;
        }

        public void setAccount(String Account) {
            this.Account = Account;
        }

        public void setDestination(String Destination) {
            this.Destination = Destination;
        }

        public void setTransactionType(String TransactionType) {
            this.TransactionType = TransactionType;
        }

        public void setSigningPubKey(String SigningPubKey) {
            this.SigningPubKey = SigningPubKey;
        }

        public void setAmount(AmountEntity Amount) {
            this.Amount = Amount;
        }

        public void setFee(String Fee) {
            this.Fee = Fee;
        }

        public void setSendMax(SendMaxEntity SendMax) {
            this.SendMax = SendMax;
        }

        public void setFlags(long Flags) {
            this.Flags = Flags;
        }

        public void setSequence(int Sequence) {
            this.Sequence = Sequence;
        }

        public void setLastLedgerSequence(int LastLedgerSequence) {
            this.LastLedgerSequence = LastLedgerSequence;
        }

        public void setTxnSignature(String TxnSignature) {
            this.TxnSignature = TxnSignature;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getDate() {
            return date;
        }

        public String getAccount() {
            return Account;
        }

        public String getDestination() {
            return Destination;
        }

        public String getTransactionType() {
            return TransactionType;
        }

        public String getSigningPubKey() {
            return SigningPubKey;
        }

        public AmountEntity getAmount() {
            return Amount;
        }

        public String getFee() {
            return Fee;
        }

        public SendMaxEntity getSendMax() {
            return SendMax;
        }

        public long getFlags() {
            return Flags;
        }

        public int getSequence() {
            return Sequence;
        }

        public int getLastLedgerSequence() {
            return LastLedgerSequence;
        }

        public String getTxnSignature() {
            return TxnSignature;
        }

        public String getHash() {
            return hash;
        }

        public class AmountEntity {
            /**
             * currency : HDT
             * value : 1
             * issuer : zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek
             */
            private String currency;
            private String value;
            private String issuer;

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }

            public String getCurrency() {
                return currency;
            }

            public String getValue() {
                return value;
            }

            public String getIssuer() {
                return issuer;
            }
        }

        public class SendMaxEntity {
            /**
             * currency : HDT
             * value : 1.01
             * issuer : zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek
             */
            private String currency;
            private String value;
            private String issuer;

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }

            public String getCurrency() {
                return currency;
            }

            public String getValue() {
                return value;
            }

            public String getIssuer() {
                return issuer;
            }
        }
    }
}
