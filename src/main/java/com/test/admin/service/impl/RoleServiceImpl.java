package com.test.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.admin.dao.AdminUserDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.dao.RoleDAO;
import com.test.admin.pojo.Role;
import com.test.admin.service.RoleService;
import com.test.admin.shiro.MyShiroFilterFatcotyBean;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private AdminUserDAO adminUserDAO;
    @Autowired
    private PermsDAO permsDAO;

    @Override
    public PageInfo<Role> getRoleList(Role role, Integer offset, Integer pageSize) {
        PageHelper.startPage(offset, pageSize);
        return new PageInfo<>(roleDAO.getRoleList(role));
    }


    @Override
    public void addRole(Role role) {
        roleDAO.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDAO.updateRole(role);
    }


    @Override
    public List<Role> getRoleList(Role role) {
        return roleDAO.getRoleList(role);
    }

    @Override
    public void deleteRole(int[] ids) {
        Role role = new Role();
        //逻辑删除，把状态改成无效
        role.setStatus(0);
        for (int id : ids) {
            role.setId(id);
            roleDAO.updateRole(role);
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleDAO.getRoleId(id);
    }

    @Autowired
    private MyShiroFilterFatcotyBean shiroFilterFactoryBean;

    @Override
    public void addRoleMenu(int roleId, int[] menuIds) {
        permsDAO.deleteRoleMenuByRoleId(roleId);
        if (menuIds != null && menuIds.length > 0) {
            for (int menuId : menuIds) {
                permsDAO.addRoleMenu(roleId, menuId);
            }
        }
        shiroFilterFactoryBean.update();
    }

    @Override
    public List<Integer> getRoleMenuIds(Integer roleId) {
        return permsDAO.getRoleMenuIds(roleId);
    }
}
