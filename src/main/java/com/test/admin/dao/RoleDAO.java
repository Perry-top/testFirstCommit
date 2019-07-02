package com.test.admin.dao;


import com.test.admin.pojo.Role;

import java.util.List;

public interface RoleDAO {

    List<Role> getRoleList(Role role);//查询角色列表

    void addRole(Role role);

    void updateRole(Role role);

    Role getRoleId(Integer id);
}
