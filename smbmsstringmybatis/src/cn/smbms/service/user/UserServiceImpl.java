package cn.smbms.service.user;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 *
 * @author Administrator
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userDao;

    @Override
    public boolean add(User user) {
        boolean flag = false;
        int add = userDao.add(user);
        if (add > 0) {
            flag = true;
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        // TODO Auto-generated method stub

        return flag;
    }


    @Override
    public User login(String userCode, String userPassword) {
        User user = userDao.getLoginUser(userCode);
        //匹配密码
        if (null != user) {
            if (!user.getUserPassword().equals(userPassword))
                user = null;
        }
        return user;
    }


    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        // TODO Auto-generated method stub
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("queryUserName", queryUserName);
        HashMap.put("queryUserRole", queryUserRole);
        HashMap.put("currentPageNo", currentPageNo);
        HashMap.put("pageSize", pageSize);
        List<User> userList = userDao.getUserList(HashMap);
        return userList;
    }


    @Override
    public User selectUserCodeExist(String userCode) {
        // TODO Auto-generated method stub

        User user = null;
        try {
            user = userDao.getLoginUser(userCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteUserById(int id) {
        // TODO Auto-generated method stub
        boolean falg = false;
        int i = userDao.deleteUserById(id);
        if (i > 0) {
            falg = true;
        }

        return falg;
    }

    @Override
    public User getUserById(String id) {
        // TODO Auto-generated method stub
        User userById = userDao.getUserById(id);

        return userById;
    }

    @Override
    public boolean modify(User user) {
        // TODO Auto-generated method stub
        boolean flag = false;
        int modify = userDao.modify(user);
        if (modify > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updatePwd(int id, String userPassword) {
        // TODO Auto-generated method stub
        boolean flag = false;
        int i = userDao.updatePwd(id, userPassword);

        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        // TODO Auto-generated method stub
        int userCount = userDao.getUserCount(queryUserName, queryUserRole);
        return userCount;
    }

}
