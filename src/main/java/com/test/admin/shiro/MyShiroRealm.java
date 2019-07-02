package com.test.admin.shiro;

import com.test.admin.dao.AdminUserDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.pojo.AdminUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

/**
 * @author 徒有琴
 */
public class MyShiroRealm extends AuthorizingRealm {

    private AdminUserDAO adminUserDAO;
    private PermsDAO permsDAO;

    public void setPermsDAO(PermsDAO permsDAO) {
        this.permsDAO = permsDAO;
    }

    public void setAdminUserDAO(AdminUserDAO adminUserDAO) {
        this.adminUserDAO = adminUserDAO;
    }

    //在权限验证的时候执行的,查询用户的角色和权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("doGetAuthorizationInfo");
        Integer userId = (Integer) principalCollection.getPrimaryPrincipal();
        List<Integer> roleIds = permsDAO.getUserRoleIds(userId);//用户拥有的角色id
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Integer roleId : roleIds) {
            info.addRole(roleId.toString());
        }
        List<String> perms = permsDAO.getUserPerms(userId);
        info.addStringPermissions(perms);
        return info;
    }

    //登陆
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("=========shiro===========");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        AdminUser user = adminUserDAO.getUserByEmail(email);
        if (user == null || user.getStatus() == 0) {
            return null;
        }
        //用户的唯一标识，密码，当前realm的名字
        return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), getName());
    }
}
