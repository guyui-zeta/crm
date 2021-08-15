package com.zeta.crm.dao;

import com.zeta.crm.base.BaseMapper;
import com.zeta.crm.query.CustomerQuery;
import com.zeta.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //通过客户名称查询客户对象
    Customer queryCustomerByName(String name);

    //查询客户贡献数据
    List<Map<String,Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);
    //查询客户构成
    List<Map<String,Object>> countCustomerMake();

}