package com.zeta.crm.aspect;

import com.zeta.crm.annoation.RequiredPermission;
import com.zeta.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 自定义的切面
 * @author Zeta
 * @version 1.0
 * @date 2021/7/31 16:05
 */
@Component //交给IOC处理
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;

    /**
     * 切面会拦截指定包下的指定注解
     * 拦截RequiredPermission注解
     *
     * @param pjp
     * @return
     */
    @Around(value = "@annotation(com.zeta.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        //得到当前登陆用户拥有的权限（session的作用域）
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        //判断用户是否用相关权限
        if(null == permissions || permissions.size() < 1){
            //抛出认证异常
            throw new AuthException();
        }
        //得到对应的目标方法
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上对应的状态码
        if(!(permissions.contains(requiredPermission.code()))){
            //如果权限中不包含当前方法爱上注解指定的权限码，则抛出异常
            throw new AuthException();
        }
        //pjp最后需要proceed，并抛出异常
        result = pjp.proceed();
        return result;
    }
}
