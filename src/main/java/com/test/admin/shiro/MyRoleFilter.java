package com.test.admin.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author 徒有琴
 */
public class MyRoleFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        String[] roleIds = (String[]) o;//当前请求路径所需的权限列表
        for (String roleId : roleIds) {
            if(subject.hasRole(roleId)){
                return true;
            }
        }
        return false;
    }
}
