package com.zeta.crm.dao;

import com.zeta.crm.base.BaseMapper;
import com.zeta.crm.vo.User;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User, Integer> {
    //通过用户名查询用户对象，返回用户对象
    User queryUserByName(String userName);

    //查询所有的销售人员
    List<Map<String,Object>> queryAllSales();


}