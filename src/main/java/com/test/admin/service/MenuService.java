package com.test.admin.service;

import com.test.admin.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Menu> getMenuTree();

    List<Menu> getMenuList();

    void deleteMenu(int[] ids) throws Exception;

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    Menu getMenuById(int id);

    Map<String, Object> getUserMenu(Integer userId);
}
