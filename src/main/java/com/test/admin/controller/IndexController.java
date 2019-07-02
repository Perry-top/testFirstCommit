package com.test.admin.controller;

import com.test.admin.pojo.AdminUser;
import com.test.admin.pojo.Menu;
import com.test.admin.service.AdminUserService;
import com.test.admin.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author 徒有琴
 */
@Controller
public class IndexController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("index.html")
    public String index(Model model) {
//        List<Menu> menuList = menuService.getMenuTree();
//        model.addAttribute("menuList", menuList);
        return "index";
    }

    @RequestMapping("side.html")
    @ResponseBody
    public Map<String, Object> side(HttpSession session) {
        //AdminUser user=(AdminUser)session.getAttribute("SESSION_USER");
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getPrincipal();
        return menuService.getUserMenu(userId);
    }

    @RequestMapping("login.html")
    public String login() {
        return "login";
    }

    @RequestMapping("error.html")
    public String error() {
        return "error";
    }

    @RequestMapping("logout.html")
    public String logout(HttpSession session) {
        //session.invalidate();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    @RequestMapping("dologin.html")
    public ModelAndView doLogin(String email, String password, HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        try {
            subject.login(token);//它会执行realm中的doGetAuthenticationInfo
            return new ModelAndView(new RedirectView("index.html"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ModelAndView("login", "message", "用户名或密码错误");
        }
//        AdminUser user = adminUserService.doLogin(email, password);
//        if (user == null) {
//            return new ModelAndView("login","message","用户名或密码错误");
//        } else {
//            session.setAttribute("SESSION_USER", user);
//            return new ModelAndView(new RedirectView("index.html"));
//        }
    }
}
