package com.drug.config;

import com.drug.filter.JWTFilter;
import com.drug.shiro.realm.UserRealm;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: shiro核心配置, 中央大脑 SecurityManager 相关配置 等同于之前玩 shiro-realm.ini的时候
 */
@Configuration
public class ShiroConfig {

    //配置安全管理器, 传入自己写的自定义域
    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager(UserRealm userRealm) {
        //使用默认的安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 可以添加多个认证，执行顺序是有影响的

        //将自定义的realm交给安全管理器统一调度管理
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        //1.创建shiro过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置,如果没有前后端分离配置这个（配置登录页面，登录成功页面，验证未成功页面）
        //4.配置过滤器集合
        /**
         * key ：访问连接
         * 支持通配符的形式
         * value：过滤器类型
         * shiro常用过滤器类型
         * anno ：匿名访问（表明此链接所有人可以访问）
         * authc ：认证后访问（表明此链接需登录认证成功之后可以访问）
         */
        //5.设置自定义jwt过滤器
        Map<String, Filter> jwt = new HashMap<>();
        jwt.put("jwt",new JWTFilter());
        filterFactory.setFilters(jwt);
        /*
         * 6设置所有的请求都经过我们的JWTfilter
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new HashMap<>();
//        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
//        // 访问401和404页面不通过我们的Filter
//        filterRuleMap.put("/401", "anon");
        filterFactory.setFilterChainDefinitionMap(filterRuleMap);
        return filterFactory;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * @return org.apache.shiro.session.mgt.SessionManager
     * @description // 自定义session管理器
     **/
    @Bean
    public SessionManager sessionManager() {
        DefaultSessionManager shiroSessionManager = new DefaultSessionManager();
        // 关闭session校验轮询
        shiroSessionManager.setSessionValidationSchedulerEnabled(false);
        return shiroSessionManager;
    }

    //开启shiro注解支持
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor
    authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    /**
     * Description : 管理Subject主体对象,生命周期的组件,用户只是打印下生产销毁日志什么的,请参考spring中bean的生命周期   <br/>
     *
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     **/
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }

}

