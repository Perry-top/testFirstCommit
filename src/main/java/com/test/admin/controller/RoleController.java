package com.test.admin.controller;

import com.github.pagehelper.PageInfo;
import com.test.admin.pojo.Menu;
import com.test.admin.pojo.Role;
import com.test.admin.pojo.TableData;
import com.test.admin.service.MenuService;
import com.test.admin.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role.html")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @RequestMapping
    public String page() {
        return "/role";
    }

    @RequiresPermissions("sys:role:list")
    @RequestMapping(params = "act=table")
    @ResponseBody
    public TableData table(Role role, Integer page, Integer limit) {

        PageInfo<Role> pageInfo = roleService.getRoleList(role, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequiresPermissions({"sys:role:add","sys:role:update"})
    @RequestMapping(params = "act=edit")
    @ResponseBody
    public Map<String, Object> edit(Role role) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (role.getId() == null) {
                roleService.addRole(role);
            } else {
                roleService.updateRole(role);
            }
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(params = "act=menu_tree")
    @ResponseBody
    public List<Menu> tree() {
        return menuService.getMenuTree();
    }

    @RequiresPermissions("sys:role:assign")
    @RequestMapping(params = "act=role_menu")
    @ResponseBody
    public List<Integer> roleMenu(Integer roleId) {
        return roleService.getRoleMenuIds(roleId);
    }
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(params = "act=assign")
    @ResponseBody
    public Map<String, Object> assign(Integer roleId, int[] menuIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            roleService.addRoleMenu(roleId, menuIds);
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}
