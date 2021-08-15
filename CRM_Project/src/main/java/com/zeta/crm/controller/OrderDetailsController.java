package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.query.OrderDetailsQuery;
import com.zeta.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/3 15:33
 */
@Controller
@RequestMapping("order_details")
public class OrderDetailsController extends BaseController {
    @Resource
    private OrderDetailsService orderDetailsService;

    /**
     * 分页条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery){
        return orderDetailsService.queryOrderDetailsByParam(orderDetailsQuery);

    }
}
