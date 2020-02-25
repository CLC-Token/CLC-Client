package com.btd.wallet.event;

import java.util.List;

/**
 * Description:    <br>
 * Author: cxh <br>
 * Date: 2019/4/16 11:03
 */
public class OfferWarning {

    public List<String> address;

    public OfferWarning(List<String> address) {
        this.address = address;
    }
}
