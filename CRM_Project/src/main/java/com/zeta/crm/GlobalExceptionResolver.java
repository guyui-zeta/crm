package com.zeta.crm;

import com.alibaba.fastjson.JSON;
import com.zeta.crm.base.ResultInfo;
import com.zeta.crm.exceptions.AuthException;
import com.zeta.crm.exceptions.NoLoginException;
import com.zeta.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理类
 * 作用：
 *      1.降低耦合性，不需要每次遇到都try catch
 *      2.提升用户使用体验，将异常封装好返回，异常显示在idea里，而不是页面里面
 * @author Zeta
 * @version 1.0

 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 异常处理方法
     * 方法的返回值：
     *      1.返回视图（view）
     *      2.返回数据（json）
     * 如何判断方法的返回值？
     *      通过方法上是否声明ResponseBody注解【通过反射来获取】
     *      如果未声明，则返回视图
     *      如果声明了，则返回数据
     *
     * @param request  request请求对象
     * @param response  response响应对象
     * @param handler  方法对象
     * @param e  异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {

        /**
         * 非法请求拦截
         *  判断是否抛出未登录异常
         *      如果抛出该异常，则要求用户登录，重定向跳转到登录页面
         *
         */
        if(e instanceof NoLoginException)
        {
            //重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        /**
         * 设置默认的异常处理（返回视图）
         */
        ModelAndView modelAndView = new ModelAndView("error");//默认error
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试...");

        //判断方法上是否有ResponseBody注解
        //判断Handler对象是否是方法对象HandlerMethod
        if(handler instanceof HandlerMethod)
        {
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取这个方法上面声明的ResponseBody注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody对象是否为空（如果对象为空，则表示返回的是视图，否则为数据）
            if(responseBody == null)
            {
                /**
                 * 返回视图
                 */
                //判断异常类型
                if(e instanceof ParamsException)//如果是自己设置的参数异常
                {
                    ParamsException p = (ParamsException) e;
                    //设置视图的异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                //认证异常
                }else if(e instanceof AuthException)
                {
                    AuthException a = (AuthException) e;
                    //设置视图的异常信息
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
                //return modelAndView;
            }
            else
            {
                /**
                 * 返回数据
                 */
                //设置默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试！");
                //判断异常类型是否是自定义异常
                if(e instanceof ParamsException)
                {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if(e instanceof AuthException)//认证异常
                {
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }

                //设置响应类型及编码格式（响应JSON格式数据）【为了防止乱码】
                response.setContentType("application/json;charset=UTF-8");
                //得到字符输出流
                PrintWriter out = null;
                try {
                    //得到输出流
                    out = response.getWriter();
                    //将需要返回的对象转换成json格式的字符串
                    String json = JSON.toJSONString(resultInfo);
                    //输出数据
                    out.write(json);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } finally {
                    //如果对象不为空，则关闭
                    if(out != null)
                    {
                        out.close();
                    }
                    return null;
                }

            }


        }

        return modelAndView;
    }
}
