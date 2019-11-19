package cn.smbms.contorller.user;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import cn.smbms.util.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.management.relation.RoleResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/jsp/user")
public class UserContorller {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;


    @RequestMapping(value = "/pwdmodifyView1")
    private String updatePwd() {
        return "pwdmodify";
    }

    @RequestMapping(value = "/pwdmodifyView")
    public String updatePwd(@RequestParam(required = false) String newpassword, HttpSession session, Model model) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
            } else {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "pwdmodify";
    }


    @RequestMapping(value = "/pwdmodify.do")
    @ResponseBody
    private Map<String, String> getPwdByUserId(@RequestParam(value = "userPassword", required = false) String oldpassword, HttpSession session) {
        System.out.println(oldpassword);
        Object o = session.getAttribute(Constants.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (null == o) {//session过期*/
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) o).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return resultMap;

    }


    @RequestMapping("/modify.doo")
    private String modify1(@RequestParam(required = false, value = "uid") String uid, Model model, User user) {
        System.out.println("/modify.doo");
        user = userService.getUserById(uid);
        model.addAttribute("user", user);
        return "usermodify";
    }

    @RequestMapping(value = "/modify.do", method = RequestMethod.POST)
    private String modify(User user, HttpSession session, Model model, Integer uid) {
        System.out.println("modify.do");
        /*  user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());*/
        user.setId(uid);
        user.setModifyDate(new Date());
        System.out.println(user.getBirthday());
        if (userService.modify(user)) {
            return "redirect:/jsp/user/query.do";
        } else {
            return "usermodify";
        }
    }


    @RequestMapping("/view.do")
    private String getUserById(@RequestParam(required = false, value = "uid") String id, Model model)
            throws ServletException, IOException {
        System.out.println("getUserById");
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userview";
        }
        return "redirect:/jsp/user/query.do";
    }


    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    private Map<String, String> delUser(@RequestParam(required = false, value = "uid") String uid) throws ServletException, IOException {
        Map<String, String> resultMap = new HashMap<>();
        Integer delId = 0;
        System.out.println(uid);
        try {
            delId = Integer.parseInt(uid);
            System.out.println(delId);
        } catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        if (delId <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            if (userService.deleteUserById(delId)) {
                System.out.println("成功删除");
                resultMap.put("delResult", "true");
            } else {
                System.out.println("删除失败");
                resultMap.put("delResult", "false");
            }
        }
        return resultMap;
    }


    @RequestMapping(value = "/ucexist.do")
    private @ResponseBody
    Map<String, String> userCodeExist(@RequestParam(required = false, value = "userCode") String userCode)
            throws ServletException, IOException {
        System.out.println("userCodeExist");
        Map<String, String> resultMap = new HashMap<>();
        //判断用户账号是否可用
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }
        return resultMap;
    }


    @RequestMapping(value = "/getRoleList")
    private @ResponseBody
    List<Role> getRoleList() {
        return roleService.getRoleList();
    }


    @RequestMapping(value = "/query.do")
    private String query(Model model,
                         @RequestParam(required = false, value = "queryname") String queryUserName,
                         @RequestParam(required = false, value = "queryUserRole") String temp,
                         @RequestParam(required = false, value = "pageIndex") String pageIndex)
            throws ServletException, IOException {
        System.out.println("query");
        //查询用户列表

        int queryUserRole = 0;

        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;
        /**
         * http://localhost:8090/SMBMS/userlist.do
         * ----queryUserName --NULL
         * http://localhost:8090/SMBMS/userlist.do?queryname=
         * --queryUserName ---""
         */
        System.out.println("queryUserName servlet--------" + queryUserName);
        System.out.println("queryUserRole servlet--------" + queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }


        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "error";
            }
        }
        //总数量（表）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);

        return "userlist";


    }


    @RequestMapping(value = "/user.do")
    private String add(@ModelAttribute("user") User user, Model model)
            throws ServletException, IOException {
        System.out.println("add()================");
        System.out.println(user);
        if (userService.add(user)) {
            return "redirect:/jsp/user/query.do";
        } else {
            return "useradd";
        }
    }

    @RequestMapping(value = "useradd")
    private String useradd() {
        System.out.println("useradd");
        return "useradd";
    }
}

