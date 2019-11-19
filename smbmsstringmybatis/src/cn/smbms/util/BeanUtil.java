package cn.smbms.util;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class  BeanUtil {

    private static final ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");

    public static Object getBeans(String name) {

        return context.getBean(name);
    }

    public static ClassPathXmlApplicationContext getInstance() {

        return context;
    }

}
