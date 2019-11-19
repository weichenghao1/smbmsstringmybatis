package cn.smbms.dao.user;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    User getLoginUser(String userCode);

    //增加用户信息
    int add(User user);

    //通过条件查询-用户表记录数
    int getUserCount(@Param("userName") String userName, @Param("userRole") int userRole);


    //过userId删除user
    int deleteUserById(@Param("id") Integer delId);


    //通过userId获取user
    User getUserById(String id);

    //修改用户信息
    int modify(User user);

    //修改当前用户密码
    int updatePwd(@Param("id") int id, @Param("userPassword") String userPassword);


    //通过条件查询-userList
    public List<User> getUserList(Map<String, Object> map);

}
