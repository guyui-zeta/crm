package com.zeta.crm.query;

import com.zeta.crm.base.BaseQuery;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/25 14:51
 */
public class CusDevPlanQuery extends BaseQuery {
    private Integer saleChanceId;//营销机会的主键

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
