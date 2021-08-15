package com.zeta.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zeta.crm.base.BaseService;
import com.zeta.crm.dao.OrderDetailsMapper;
import com.zeta.crm.query.OrderDetailsQuery;
import com.zeta.crm.vo.Customer;
import com.zeta.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/3 15:32
 */
@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 分页条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsByParam(OrderDetailsQuery orderDetailsQuery) {
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getLimit());
        //得到对应的分页对象,构造器中参数是待分类的对象
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailsQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }
}
