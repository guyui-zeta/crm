package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.base.ResultInfo;
import com.zeta.crm.exceptions.ParamsException;
import com.zeta.crm.model.UserModel;
import com.zeta.crm.query.UserQuery;
import com.zeta.crm.service.UserService;
import com.zeta.crm.utils.LoginUserUtil;
import com.zeta.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/3 19:24
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登陆
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd)
    {
        //resultInfo用来保存反应结果的信息【包括响应结果代码】
        ResultInfo resultInfo = new ResultInfo();
        //通过try...catch捕获service层的异常，如果service层抛出异常，则表示登陆失败

        //调用service登陆方法
        UserModel userModel = userService.userLogin(userName,userPwd);

        //设置ResultInfo的result值（将数据返回给请求）
        resultInfo.setResult(userModel);
//        try {
//            //调用service登陆方法
//            UserModel userModel = userService.userLogin(userName,userPwd);
//
//            //设置ResultInfo的result值（将数据返回给请求）
//            resultInfo.setResult(userModel);
//
//        } catch (ParamsException p)
//        {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        } catch (Exception e)
//        {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登陆失败！");
//            e.printStackTrace();
//        }

        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage()
    {
        return "user/password";

    }    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    //更新操作用postMapping
    @RequestMapping("/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest request, String oldPassword, String newPassword, String repeatPassword)
    {
        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层的修改密码方法
        userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
//        try {
//            //获取cookie中的用户id
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            //调用service层的修改密码方法
//            userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
//        }catch (ParamsException p)//ParamsException是自己定义的异常
//        {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e)//Exception是未定义的的异常
//        {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 打开添加或修改用户的页面
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request){
        //判断id是否为空，不为空表示更新操作，查询用户对象数据
        if(id != null){
            //通过id查询用户对象
            User user = userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){

        userService.deleteByIds(ids);
        return success("用户删除成功！");

    }
}
