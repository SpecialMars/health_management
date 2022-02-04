package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.PermissionDao;
import com.mars.dao.RoleDao;
import com.mars.dao.UserDao;
import com.mars.pojo.Permission;
import com.mars.pojo.Role;
import com.mars.pojo.User;
import com.mars.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * ClassName:UserServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/2/4 11:53
 * @Author:Mars
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询数据库，获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {

        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        Set<Role> roles = roleDao.findByUserId(user.getId());
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
                if (permissions != null && permissions.size() > 0) {
                    role.setPermissions(permissions);
                }
            }
        }
        user.setRoles(roles);

        return user;
    }
}
