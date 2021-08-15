package com.zeta.crm.dao;

import com.zeta.crm.base.BaseMapper;
import com.zeta.crm.vo.SaleChance;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 多条件查询的接口不需要单独去定
     * 由于多个模块都有多条件查询，所以放到了父接口BaseMapper里面
     *
     */


}