package com.test.admin.controller;

import com.test.admin.pojo.Menu;
import com.test.admin.pojo.TableData;
import com.test.admin.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徒有琴
 */
@Controller
@RequestMapping("/system/menu.html")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping
    public String page() {
        return "/menu";
    }
    @RequiresPermissions("sys:menu:list")
    @RequestMapping(params = "act=table")
    @ResponseBody
    public TableData table() {
        List<Menu> menuList = menuService.getMenuList();
        return new TableData(menuList.size(), menuList);
    }
    @RequiresPermissions("sys:menu:delete")
    @RequestMapping(params = "act=delete")
    @ResponseBody
    public Map<String, Object> delete(int[] ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            menuService.deleteMenu(ids);
            result.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @RequiresPermissions("sys:menu:add")
    @RequestMapping(params = "act=add")
    @ResponseBody
    public Map<String, Object> add(Menu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            menuService.addMenu(menu);
            result.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @RequiresPermissions("sys:menu:update")
    @RequestMapping(params = "act=info")
    @ResponseBody
    public Menu info(Integer id) {
        return menuService.getMenuById(id);
    }
    @RequiresPermissions("sys:menu:update")
    @RequestMapping(params = "act=update")
    @ResponseBody
    public Map<String, Object> update(Menu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            menuService.updateMenu(menu);
            result.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @RequiresPermissions({"sys:menu:update","sys:menu:add"})
    @RequestMapping(params = "act=tree")
    @ResponseBody
    public List<Menu> tree() {
        return menuService.getMenuTree();
    }
}
