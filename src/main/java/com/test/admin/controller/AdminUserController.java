package com.test.admin.controller;

import com.github.pagehelper.PageInfo;
import com.test.admin.pojo.AdminUser;
import com.test.admin.pojo.Role;
import com.test.admin.pojo.TableData;
import com.test.admin.service.AdminUserService;
import com.test.admin.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/system/user.html")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;

    @RequestMapping
    public String page() {
        return "/user";
    }
    @RequiresPermissions("sys:user:list")
    @RequestMapping(params = "act=table")
    @ResponseBody
    public TableData table(AdminUser adminUser, Integer page, Integer limit) {
        PageInfo<AdminUser> pageInfo = adminUserService.getUserList(adminUser, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }
    @RequiresPermissions("sys:user:add")
    @RequestMapping(params = "act=add")
    @ResponseBody
    public Map<String, Object> add(AdminUser user) {
        Map<String, Object> result = new HashMap<>();
        try {
            adminUserService.addUser(user);
            result.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @RequiresPermissions("sys:user:assign")
    @RequestMapping(params = "act=role_tree")
   @ResponseBody
    public List<Role> roleList() {
        return roleService.getRoleList(null);
    }
    @RequiresPermissions("sys:user:assign")
    @RequestMapping(params = "act=user_role")
    @ResponseBody
    public List<Integer> userRole(Integer userId) {
        return adminUserService.getUserRoleIds(userId);
    }
    @RequiresPermissions("sys:user:assign")
    @RequestMapping(params = "act=assign_role")
    @ResponseBody
    public Map<String, Object>  assignRole(Integer userId, int[] roleIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            adminUserService.addUserRole(userId, roleIds);
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
