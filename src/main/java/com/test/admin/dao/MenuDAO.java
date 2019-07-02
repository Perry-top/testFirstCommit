package com.test.admin.dao;

import com.test.admin.pojo.Menu;

import java.util.List;

public interface MenuDAO {
    List<Menu> getAllMenu();

    int getMenuCountByParentId(int parentId);

    void deleteMenu(int id);

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    Menu getMenuById(int id);

    List<Menu>getUserMenu(Integer userId);
}
