package com.test.admin.service;


import com.github.pagehelper.PageInfo;
import com.test.admin.pojo.Role;

import java.util.List;

public interface RoleService {
    PageInfo<Role> getRoleList(Role role, Integer offset, Integer pageSize);

    List<Role> getRoleList(Role role);

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRole(int[] ids);

    Role getRoleById(Integer id);

    void addRoleMenu(int roleId, int[] menuIds);

    List<Integer>getRoleMenuIds(Integer roleId);
}
