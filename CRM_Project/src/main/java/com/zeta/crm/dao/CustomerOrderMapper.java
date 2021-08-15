package com.zeta.crm.dao;

import com.zeta.crm.base.BaseMapper;
import com.zeta.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    //通过订单ID查询对应的订单记录
    Map<String, Object> queryOrderById(Integer orderId);
}