package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.base.ResultInfo;
import com.zeta.crm.query.CustomerQuery;
import com.zeta.crm.service.CustomerService;
import com.zeta.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/2 21:18
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Resource
    private CustomerService customerService;

    /**
     * 分页多条件查询客户列表
     * @param customerQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 进入客户信息管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加客户信息成功！");
    }

    /**
     * 修改客户信息
     * @param customer
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("修改客户信息成功！");
    }

    /**
     * 删除客户信息
     * @param customer
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("删除客户信息成功！");
    }

    /**
     * 打开添加或修改客户信息的对话框
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(Integer id , HttpServletRequest request){
        //如果id不为空，则查询客户记录
        if(id != null){
            //通过id查询客户对象
            Customer customer = customerService.selectByPrimaryKey(id);
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    /**
     * 打开客户的订单页面
     * @return
     */
    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId,Model model){
        //通过客户ID查询客户记录，设置到请求域中
        model.addAttribute("customer",customerService.selectByPrimaryKey(customerId));
        return "customer/customer_order";
    }


    /**
     * 客户贡献分析
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * 查询客户构成(折线图)
     * @return
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String,Object> countCustomerMake(){
        return customerService.countCustomerMake();
    }

    /**
     * 查询客户构成(饼状图)
     * @return
     */
    @RequestMapping("countCustomerMake02")
    @ResponseBody
    public Map<String,Object> countCustomerMake02(){
        return customerService.countCustomerMake02();
    }

}
