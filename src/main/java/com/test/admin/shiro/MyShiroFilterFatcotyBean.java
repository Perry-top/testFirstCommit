package com.test.admin.shiro;

import com.test.admin.dao.MenuDAO;
import com.test.admin.dao.PermsDAO;
import com.test.admin.pojo.Menu;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 徒有琴
 */
public class MyShiroFilterFatcotyBean extends ShiroFilterFactoryBean {

    private MenuDAO menuDAO;
    private PermsDAO permsDAO;

    public void setPermsDAO(PermsDAO permsDAO) {
        this.permsDAO = permsDAO;
    }

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    private static String filterChainDefinitions=null;

    @Override
    public void setFilterChainDefinitions(String definitions) {
        filterChainDefinitions=definitions;
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }
        List<Menu> menuList = menuDAO.getAllMenu();
        for (Menu menu : menuList) {
            if (StringUtils.hasText(menu.getUrl())) {
                // menu.html=roles[1,2,3,4,5]
                //查出当前这个菜单有哪些角色可以访问
                List<Integer> roleIds = permsDAO.getRoleIdByMenuId(menu.getId());
                if(roleIds!=null&&roleIds.size()>0){
                    section.put(menu.getUrl(), "roles" + roleIds.toString());
                }
            }
        }
        section.put("/**", "authc");//必须放在最后
        this.setFilterChainDefinitionMap(section);
    }

    /**
     * 当重新给角色授权时使用，因为菜单的权限在项目启动的时候就加载了，所以后追加的权限需要再次追加进来
     */
    public synchronized void update() {
        try {
            AbstractShiroFilter shiroFilter = (AbstractShiroFilter) this.getObject();
            PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            // 过滤管理器
            DefaultFilterChainManager manager = (DefaultFilterChainManager) resolver.getFilterChainManager();
            // 清除权限配置
            manager.getFilterChains().clear();
            this.getFilterChainDefinitionMap().clear();

            // 重新设置权限
            this.setFilterChainDefinitions(filterChainDefinitions);


            Map<String, String> chains = this.getFilterChainDefinitionMap();
            if (!CollectionUtils.isEmpty(chains)) {
                Iterator var12 = chains.entrySet().iterator();

                while (var12.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) var12.next();
                    String url = (String) entry.getKey();
                    String chainDefinition = (String) entry.getValue();
                    manager.createChain(url, chainDefinition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
