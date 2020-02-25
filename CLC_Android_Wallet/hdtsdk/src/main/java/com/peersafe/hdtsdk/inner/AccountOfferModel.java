package com.peersafe.hdtsdk.inner;

import java.util.List;

/**
 * Description: 查询到的挂单   <br>
 * Author: cxh <br>
 * Date: 2019/3/29 9:20
 */
public class AccountOfferModel {
    /**
     * ledger_current_index : 217819
     * offers : [{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":27,"quality":"9","taker_pays":{"currency":"HDT","value":"9","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":22,"quality":"11","taker_pays":{"currency":"HDT","value":"11","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":23,"quality":"12","taker_pays":{"currency":"HDT","value":"12","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":24,"quality":"15","taker_pays":{"currency":"HDT","value":"15","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":25,"quality":"14","taker_pays":{"currency":"HDT","value":"14","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"0.8","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":26,"quality":"10","taker_pays":{"currency":"HDT","value":"8","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":28,"quality":"8","taker_pays":{"currency":"HDT","value":"8","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"0.6333333333333335","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":30,"quality":"6","taker_pays":{"currency":"HDT","value":"3.8","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":31,"quality":"5","taker_pays":{"currency":"HDT","value":"5","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}},{"flags":0,"taker_gets":{"currency":"BTD","value":"1","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"},"seq":32,"quality":"4","taker_pays":{"currency":"HDT","value":"4","issuer":"zPCNUvT6fsCA97nMSfi6ZMrT2uxDivCDek"}}]
     * validated : false
     * marker : D992AB0FCB8BCDE67988CEE21D6D9F6773BD8107A0BBB24206FFB0A82BC61DEF
     * limit : 10
     * account : z4vnwEVR6sNsKt8xKoh1GTvExEsVBghu6
     */
    private int ledger_current_index;
    private List<OffersEntity> offers;
    private boolean validated;
    private String marker;
    private int limit;
    private String account;

    public void setLedger_current_index(int ledger_current_index) {
        this.ledger_current_index = ledger_current_index;
    }

    public void setOffers(List<OffersEntity> offers) {
        this.offers = offers;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getLedger_current_index() {
        return ledger_current_index;
    }

    public List<OffersEntity> getOffers() {
        return offers;
    }

    public boolean isValidated() {
        return validated;
    }

    public String getMarker() {
        return marker;
    }

    public int getLimit() {
        return limit;
    }

    public String getAccount() {
        return account;
    }

    public class OffersEntity {

        private int flags;
        private Taker_getsEntity taker_gets;
        private int seq;
        private String quality;
        private Taker_paysEntity taker_pays;

        public void setFlags(int flags) {
            this.flags = flags;
        }

        public void setTaker_gets(Taker_getsEntity taker_gets) {
            this.taker_gets = taker_gets;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public void setTaker_pays(Taker_paysEntity taker_pays) {
            this.taker_pays = taker_pays;
        }

        public int getFlags() {
            return flags;
        }

        public Taker_getsEntity getTaker_gets() {
            return taker_gets;
        }

        public int getSeq() {
            return seq;
        }

        public String getQuality() {
            return quality;
        }

        public Taker_paysEntity getTaker_pays() {
            return taker_pays;
        }

        public class Taker_getsEntity {

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

        public class Taker_paysEntity {

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
