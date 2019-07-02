package com.test.admin.service;


import com.github.pagehelper.PageInfo;
import com.test.admin.pojo.AdminUser;

import java.util.List;

public interface AdminUserService {


    AdminUser doLogin(String email, String password);

    PageInfo<AdminUser> getUserList(AdminUser user, Integer offset, Integer pageSize);

    void addUser(AdminUser user);

    void updateUser(AdminUser user);

    AdminUser getUserById(Integer id);

    void deleteUser(int[] ids);

    void addUserRole(int userId, int[] roleIds);

    List<Integer> getUserRoleIds(Integer userId);
}
