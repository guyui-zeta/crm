package com.zeta.crm.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zeta.crm.base.BaseController;
import com.zeta.crm.base.ResultInfo;
import com.zeta.crm.model.TreeModel;
import com.zeta.crm.service.ModuleService;
import com.zeta.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/28 9:53
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有资源列表
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){

        return moduleService.queryAllModules(roleId);
    }

    /**
     * 打开授权页面
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        //将需要授权的角色ID设置到请求域中
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }


    /**
     * 查询资源列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 进入资源/菜单管理页面
     */

    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /**
     * 添加资源
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功！");
    }
    /**
     * 修改资源
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("修改资源成功！");
    }

    /**
     * 修改资源
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除资源成功！");
    }

    /**
     * 打开添加资源的页面
     *
     * @param grade 层级
     * @param parentId 父菜单ID
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        //将数据设置到请求域中
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);

        return "module/add";
    }



    /**
     * 打开修改资源的页面
     * @param id
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer id, Model model){//model可以代替HttpServletrequest
        //将要修改的资源对象设置到请求域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }
}
