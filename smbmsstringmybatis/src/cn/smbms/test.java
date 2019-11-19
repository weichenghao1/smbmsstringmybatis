package cn.smbms;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.util.BeanUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class test {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
        UserService userService = (UserService) context.getBean("userService");
        User user = new User();
        user.setId(2);
        user.setUserName("111");
        boolean modify = userService.modify(user);
        System.out.println(modify);

    }
}



