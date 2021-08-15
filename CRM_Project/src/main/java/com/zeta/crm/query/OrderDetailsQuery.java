package com.zeta.crm.query;

import com.zeta.crm.base.BaseQuery;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/3 15:35
 */
public class OrderDetailsQuery extends BaseQuery {
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    private Integer orderId;//订单ID
}
