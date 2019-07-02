package com.test.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.admin.dao.AdminUserDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.pojo.AdminUser;
import com.test.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {


    @Autowired
    private AdminUserDAO adminUserDAO;
    @Autowired
    private PermsDAO permsDAO;

    @Override
    public AdminUser doLogin(String email, String password) {
        AdminUser user = adminUserDAO.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    @Override
    public PageInfo<AdminUser> getUserList(AdminUser user, Integer offset, Integer pageSize) {
        PageHelper.startPage(offset, pageSize);
        return new PageInfo<>(adminUserDAO.getUserList(user));
    }

    @Override
    public void addUser(AdminUser user) {
        adminUserDAO.addUser(user);
    }

    @Override
    public void updateUser(AdminUser user) {
        adminUserDAO.updateUser(user);
    }

    @Override
    public AdminUser getUserById(Integer id) {
        return adminUserDAO.getUserById(id);
    }

    @Override
    public void deleteUser(int[] ids) {
        AdminUser user = new AdminUser();
        //逻辑删除
        user.setStatus(0);
        for (int id : ids) {
            user.setId(id);
            adminUserDAO.updateUser(user);
        }
    }

    @Override
    public void addUserRole(int userId, int[] roleIds) {
        permsDAO.deleteUserRoleByUserId(userId);
        if (roleIds != null && roleIds.length > 0) {
            for (int roleId : roleIds) {
                permsDAO.addUserRole(userId, roleId);
            }
        }
    }

    @Override
    public List<Integer> getUserRoleIds(Integer userId) {
        return permsDAO.getUserRoleIds(userId);
    }
}
