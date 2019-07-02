package com.test.admin;

import com.test.admin.dao.AdminUserDAO;
import com.test.admin.dao.MenuDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.shiro.MyPermFilter;
import com.test.admin.shiro.MyRoleFilter;
import com.test.admin.shiro.MyShiroFilterFatcotyBean;
import com.test.admin.shiro.MyShiroRealm;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 徒有琴
 */
@Configuration
public class ShiroConfig {

    @Bean
    public MyShiroRealm realm(AdminUserDAO adminUserDAO, PermsDAO permsDAO) {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setAdminUserDAO(adminUserDAO);
        realm.setPermsDAO(permsDAO);
        return realm;
    }

    @Bean
    public MemoryConstrainedCacheManager cacheManager() {//缓存，不要每次都去获取用户权限
        return new MemoryConstrainedCacheManager();
    }

    //web环境下的shiro核心控制器
    @Bean
    public DefaultWebSecurityManager securityManager(MyShiroRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager());//开启缓存
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager, MenuDAO menuDAO, PermsDAO permsDAO) {
        MyShiroFilterFatcotyBean bean = new MyShiroFilterFatcotyBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login.html");//如果用户没登陆，跳转的地址
        bean.setUnauthorizedUrl("/error.html");//当用户没有权限的时候跳转的地址
        bean.setMenuDAO(menuDAO);
        bean.setPermsDAO(permsDAO);
        //anon不需要登陆验证 authc要登陆
        //查询数据库，查出来所有的菜单和菜单对应的角色，拼接到这个字符串里面
        bean.setFilterChainDefinitions("/login.html=anon\n" +
                "/dologin.html=anon\n" +
                "/static/**=anon\n");
        Map<String, Filter> filterMap = new HashMap<>();//重写shiro规则过滤器
        filterMap.put("roles", new MyRoleFilter());
        filterMap.put("perms", new MyPermFilter());
        bean.setFilters(filterMap);
        return bean;
    }
    /**
     * 让权限注解生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
