package com.test.admin.service.impl;

import com.test.admin.dao.MenuDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.pojo.Menu;
import com.test.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徒有琴
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDAO menuDAO;
    @Autowired
    private PermsDAO permsDAO;

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menuList = menuDAO.getAllMenu();
        return makeMenuTree(menuList);
    }

    public List<Menu> makeMenuTree(List<Menu> menuList) {
        List<Menu> firstMenu = new ArrayList<>();
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() == null) {
                firstMenu.add(menu);
            }
            menuMap.put(menu.getId(), menu);
        }
        for (Menu menu : menuList) {
            if (menu.getParentId() != null) {
                if (menuMap.containsKey(menu.getParentId()))
                    menuMap.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        return firstMenu;
    }

    public void makeChildrenMenu(Menu menu, int level, List<Menu> result) {
        menu.setLevel(level);
        result.add(menu);
        if (menu.getChildren().size() > 0) {
            for (Menu child : menu.getChildren()) {
                makeChildrenMenu(child, level + 1, result);
            }
        }
    }

    @Override
    public List<Menu> getMenuList() {
        List<Menu> menuList = menuDAO.getAllMenu();
        List<Menu> firstMenu = makeMenuTree(menuList);
        List<Menu> result = new ArrayList<>();
        for (Menu menu : firstMenu) {//1级菜单
            makeChildrenMenu(menu, 1, result);//
        }
        System.out.println(result);
        return result;
    }

    @Override
    public void deleteMenu(int[] ids) throws Exception {
        if (ids != null && ids.length > 0)
            for (int id : ids) {
                int childrenCount = menuDAO.getMenuCountByParentId(id);
                if (childrenCount > 0) {
                    throw new Exception("当前节点有子菜单，不能删除");
                }
                permsDAO.deleteByMenuId(id);//删除中间表
                menuDAO.deleteMenu(id);
            }
    }

    @Override
    public void addMenu(Menu menu) {
        menuDAO.addMenu(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuDAO.updateMenu(menu);
    }

    @Override
    public Menu getMenuById(int id) {
        return menuDAO.getMenuById(id);
    }

    @Override
    public Map<String, Object> getUserMenu(Integer userId) {
        List<Menu> menuList = menuDAO.getUserMenu(userId);
        List<String> perms = new ArrayList<>();
        List<Menu> firstMenu = new ArrayList<>();//导航栏左边出现的菜单
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() == null && menu.getType() != 3) {
                firstMenu.add(menu);
            }
            menuMap.put(menu.getId(), menu);
            perms.add(menu.getPerms());
        }
        for (Menu menu : menuList) {
            if (menu.getParentId() != null && menu.getType() != 3) {
                if (menuMap.containsKey(menu.getParentId()))
                    menuMap.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        Map<String, Object> userPerms = new HashMap<>();
        userPerms.put("menus", firstMenu);
        userPerms.put("perms", perms);
        return userPerms;
    }
}
