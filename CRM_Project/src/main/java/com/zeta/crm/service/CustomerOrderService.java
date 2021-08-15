package com.zeta.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zeta.crm.base.BaseService;
import com.zeta.crm.dao.CustomerOrderMapper;
import com.zeta.crm.query.CustomerOrderQuery;
import com.zeta.crm.query.CustomerQuery;
import com.zeta.crm.vo.Customer;
import com.zeta.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/3 13:10
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {
    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 多条件分页查询客户(返回的数据格式必须满足LayUI中数据表格要求的格式map)
     *
     *
     * @return
     */
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery)
    {
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        //得到对应的分页对象,构造器中参数是待分类的对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }
    //通过订单ID查询对应的订单记录
    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderMapper.queryOrderById(orderId);
    }
}
