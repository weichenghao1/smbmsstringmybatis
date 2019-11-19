package cn.smbms.service.role;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleList() {
        // TODO Auto-generated method stub
        List<Role> roleList = roleMapper.getRoleList();
        return roleList;
    }

}
