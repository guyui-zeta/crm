package com.zeta.crm.query;

import com.zeta.crm.base.BaseQuery;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/3 13:15
 */
public class CustomerOrderQuery extends BaseQuery {
    private Integer cusId;//客户ID

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }
}
