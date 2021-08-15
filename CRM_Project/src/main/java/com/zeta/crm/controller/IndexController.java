package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.service.PermissionService;
import com.zeta.crm.service.UserService;
import com.zeta.crm.utils.LoginUserUtil;
import com.zeta.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/2 11:15
 */

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        //ModelAndView模型和视图，字符串就是view视图
        return "index";
    }
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){


        //获取cookie中的用户ID【利用LoginUserUtil工具类】
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，【不能用request作用域，因为左边页面有很多跳转，点了就没了】
        User user = userService.selectByPrimaryKey(userId);
        //从cookie中拿到用户id【利用CookieUtil工具类】
        request.getSession().setAttribute("user",user);

        //通过当前登陆用户ID查询当前登陆用户拥有的资源列表（查询对应资源你的授权码）
        List<String> permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        //将集合设置到作用域中
        //不能用request，因为request只能用一次
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
