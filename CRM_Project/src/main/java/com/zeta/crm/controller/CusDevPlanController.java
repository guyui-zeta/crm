package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.base.ResultInfo;
import com.zeta.crm.enums.StateStatus;
import com.zeta.crm.query.CusDevPlanQuery;
import com.zeta.crm.query.SaleChanceQuery;
import com.zeta.crm.service.CusDevPlanService;
import com.zeta.crm.service.SaleChanceService;
import com.zeta.crm.utils.LoginUserUtil;
import com.zeta.crm.vo.CusDevPlan;
import com.zeta.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/25 12:58
 */
@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;
    /**
     * 进入客户开发计划页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 打开计划项开发与详情页面
     * @param id
     * @return
     */
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){

        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域中
        request.setAttribute("saleChance",saleChance);

        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 客户开发计划数据查询（分页对条件查询）
     * 默认情况是都查【只要is_valid=1都查出来】
     * 多条件时就加入条件参数【这就是动态SQL的牛逼之处】
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery)
    {
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");
    }


    /**
     * 更新计划项
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功！");
    }

    /**
     * 进入添加或修改计划项的页面
     * @return
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId, HttpServletRequest request,Integer id){
        //将营销机会ID设置到请求域中，给计划项页面获取
        request.setAttribute("sId",sId);
        //通过计划项Id查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        //将计划项数据设置到请求域汇总
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

    /**
     * 删除计划项
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项更新成功！");
    }
}
