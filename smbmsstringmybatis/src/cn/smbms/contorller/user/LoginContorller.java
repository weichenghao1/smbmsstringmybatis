package cn.smbms.contorller.user;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.util.BeanUtil;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginContorller {

    @Resource
    private UserService userService;

    @RequestMapping("/login.do")
    public ModelAndView login(@RequestParam(required = false) String userCode, String userPassword, HttpServletRequest request, HttpSession session) {
        System.out.println("login ============ ");
        //调用service方法，进行用户匹配
        User user = userService.login(userCode, userPassword);
        ModelAndView modelAndView = new ModelAndView();
        if (null != user) {//登录成功
            //放入session
            session.setAttribute(Constants.USER_SESSION, user);
            //页面跳转（frame.jsp）
            modelAndView.setViewName("frame");
        } else {
            //页面跳转（login.jsp）带出提示信息--转发
            request.setAttribute("error", "用户名或密码不正确");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }


}
