package com.zeta.crm.config;

import com.zeta.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器的配置类
 *      决定哪些需要拦截，哪些不需要拦截
 * @author Zeta
 * @version 1.0
 * @date 2021/6/21 13:36
 */


@Configuration //配置类
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean //将方法的返回值交给IOC维护
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现了拦截器功能的实例对象，这里使用的是noLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                //设置需要被拦截的资源
                .addPathPatterns("/**")//默认拦截所有的资源
                //设置不需要被拦截的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");//放行的资源：静态资源、登录页面
    }
}
