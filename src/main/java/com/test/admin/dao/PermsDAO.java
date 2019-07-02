package com.test.admin.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermsDAO {
    void addRoleMenu(@Param("roleId") int roleId, @Param("menuId") int menuId);

    void deleteRoleMenuByRoleId(int roleId);

    List<Integer> getRoleMenuIds(Integer roleId);

    List<Integer> getRoleIdByMenuId(Integer menuId);

    List<Integer> getUserRoleIds(Integer userId);

    void addUserRole(@Param("userId") int userId, @Param("roleId") int roleId);

    void deleteUserRoleByUserId(int userId);

    List<String> getUserPerms(Integer userId);

    void deleteByMenuId(Integer menuId);
}
