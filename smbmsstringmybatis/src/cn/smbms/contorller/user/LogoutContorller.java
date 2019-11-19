package cn.smbms.contorller.user;

import cn.smbms.tools.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/jsp")
public class LogoutContorller {


    @RequestMapping(value = "/logout.do")
    public ModelAndView doPost() throws ServletException, IOException {
        //清除session
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(Constants.USER_SESSION);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }
}
