package com.zeta.crm.annoation;

import java.lang.annotation.*;

/**
 * 定义方法爱需要的对应资源的权限码
 * @author Zeta
 * @version 1.0
 * @date 2021/7/31 16:01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    //权限码，设置默认为空
    String code() default "";
}
